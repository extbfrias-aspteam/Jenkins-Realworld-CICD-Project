package mx.net.asp.asp_pago_api.utilerias;

import com.google.gson.Gson;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Objects;

@Component
public class CifradoUtil {

    @Value("${aes.key.general}")
    private String cipherKey;
    @Value("${aes.key.password}")
    private String passCipherKey;
    @Value("${aes.key.cambio.pass}")
    private String cambioPassCipherKey;
    @Value("${aes.key.initial}")
    private String initialCypherKey;
    @Value("${encoding.format}")
    private String encoding;
    private final ErrorHandler errorHandler;
    private final Gson gson;
    private static final String ALGO_CIFRADO = "AES/CBC/PKCS5PADDING";
    private static final String ALGO_HASH = "SHA-512";
    private static final String ALGO_AES = "AES";
    private static final String generalErrorMessage = "No fue posible desencriptar la información";

    public CifradoUtil(
            ErrorHandler errorHandler,
            Gson gson) {
        this.errorHandler = errorHandler;
        this.gson = gson;
    }

    public String sha512(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGO_HASH);
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(hash);
        } catch (Exception ex) {
            throw new RuntimeException("Error al calcular SHA-512", ex);
        }
    }

    public String decryptB64(String key, String iv, String encrypted) {
        return cryptB64(key, iv, encrypted, Cipher.DECRYPT_MODE);
    }

    public String cryptB64(String key, String iv, String data, int cipherMode) {
        try {
            byte[] keyBytes = Hex.decodeHex(key);
            byte[] ivBytes = Hex.decodeHex(iv);
            byte[] dataBytes = cipherMode == Cipher.DECRYPT_MODE ? Base64.decodeBase64(data)
                    : data.getBytes(StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance(ALGO_CIFRADO);
            cipher.init(cipherMode, new SecretKeySpec(keyBytes, ALGO_AES), new IvParameterSpec(ivBytes));
            byte[] resultBytes = cipher.doFinal(dataBytes);

            return cipherMode == Cipher.DECRYPT_MODE ? new String(resultBytes, StandardCharsets.UTF_8)
                    : Base64.encodeBase64String(resultBytes);
        } catch (Exception ex) {
            throw new RuntimeException("Error al realizar operación de cifrado/desencriptado", ex);
        }
    }

    public String encriptaInformacionB64(String keySource, String data) {
        Objects.requireNonNull(keySource, "La llave no puede ser nula");
        Objects.requireNonNull(data, "La información a encriptar no puede ser nula");

        String key = keySource.substring(0, 32);
        String iv = keySource.substring(32, 64);
        return cryptB64(key, iv, data, Cipher.ENCRYPT_MODE);
    }

    public String desencriptaInformacionB64(String keySource, String data) {
        Objects.requireNonNull(keySource, "La llave no puede ser nula");
        Objects.requireNonNull(data, "La información a desencriptar no puede ser nula");

        String key = keySource.substring(0, 32);
        String iv = keySource.substring(32, 64);
        return decryptB64(key, iv, data);
    }

    public String generaKeySource(String... inputs) {
        StringBuilder keySource = new StringBuilder();
        for (String input : inputs) {
            keySource.append(sha512(input));
        }
        return keySource.toString();
    }

    public String getFolioMensajeCobro() {
        try {
            Calendar fecha = Calendar.getInstance();
            String binFolio = String.format("%7s%4s%5s%5s%6s%6s%7s%7s",
                    decimalToBinario(fecha.get(Calendar.YEAR) % 100),
                    decimalToBinario(fecha.get(Calendar.MONTH) + 1),
                    decimalToBinario(fecha.get(Calendar.DAY_OF_MONTH)),
                    decimalToBinario(fecha.get(Calendar.HOUR_OF_DAY)),
                    decimalToBinario(fecha.get(Calendar.MINUTE)),
                    decimalToBinario(fecha.get(Calendar.SECOND)),
                    decimalToBinario(fecha.get(Calendar.MILLISECOND) % 128));

            return binarioToHex(binFolio);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar Folio:: ", e);
        }
    }

    public static String decrypt(String key, String data) {
        try {

            String[] parts = data.split(":");
            IvParameterSpec iv = new IvParameterSpec(java.util.Base64.getDecoder().decode(parts[1]));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance(ALGO_CIFRADO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] decodedEncryptedData = java.util.Base64.getDecoder().decode(parts[0]);

            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original);
        } catch (Exception ex) {
            throw new RuntimeException("Error al realizar operación de cifrado/desencriptado", ex);
        }
    }

    public RespuestaDTO decodeAndDecryptRequest(String request) {
        return decodeAndDecryptRequest(request, cipherKey);
    }

    public RespuestaDTO decodeAndDecryptRequest(String request, String llaveCifrado) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            String jsonURLDecoded = URLDecoder.decode(request, encoding);
            jsonURLDecoded = jsonURLDecoded.replace(" ", "+");
            jsonURLDecoded = cleanJson(jsonURLDecoded);
            respuesta.setData(desencriptaInformacionB64(generaKeySource(llaveCifrado), jsonURLDecoded));
        } catch (Exception e) {
            errorHandler.handleException(e);
            return new RespuestaDTO(-1, generalErrorMessage, null);
        }
        return respuesta;
    }

    public RespuestaDTO decodeAndDecryptPassword(String request) {
        RespuestaDTO respuestaDTO;
        try {
            respuestaDTO = decodeAndFormatJson(request);
            if (respuestaDTO.getCodigo() == 0) {
                String keySource = generaKeySource(cambioPassCipherKey);
                String jsonDecoded = decrypt(keySource.substring(0, 16), respuestaDTO.getData());
                respuestaDTO.setData(jsonDecoded);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            return new RespuestaDTO(-1, generalErrorMessage, null);
        }
        return respuestaDTO;
    }

    public RespuestaDTO decodeAndFormatJson(String request) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            String decoded = URLDecoder.decode(request, encoding);
            decoded = decoded.replace(" ", "+");
            respuesta.setData(cleanJson(decoded));
            return respuesta;
        }  catch (Exception e) {
            errorHandler.handleException(e);
            return new RespuestaDTO(-1, generalErrorMessage, null);
        }
    }

    private String cleanJson(String json) {
        if (json.endsWith("}")) {
            return json.substring(0, json.length() - 1);
        }
        return json;
    }

    public String encryptResponse(RespuestaDTO respuestaDTO) {
        return encriptaInformacionB64(generaKeySource(cipherKey), new Gson().toJson(respuestaDTO));
    }

    public String encryptResponse(String json, String llaveCifrado) {
        return encriptaInformacionB64(generaKeySource(llaveCifrado), json);
    }

    public String encryptResponse(String json) {
        return encriptaInformacionB64(generaKeySource(cipherKey), json);
    }

    private String decimalToBinario(int numero) {
        return String.format("%s", Integer.toBinaryString(numero));
    }

    private String binarioToHex(String bin) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bin.length(); i += 4) {
            int num = Integer.parseInt(bin.substring(i, i + 4), 2);
            result.append(Integer.toHexString(num));
        }
        return result.toString();
    }

    public RespuestaDTO obtenerRespuestaDto(RespuestaDTO respuestaDTO) {
        RespuestaDTO respDTO = new RespuestaDTO();
        try {
            respDTO = gson.fromJson(respuestaDTO.getData(), RespuestaDTO.class);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return respDTO;
    }
}
