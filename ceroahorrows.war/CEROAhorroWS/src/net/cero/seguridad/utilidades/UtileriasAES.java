package net.cero.seguridad.utilidades;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class UtileriasAES {
    public String sha512(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String sha512Hex(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));

            return new String(Hex.encodeHex(hash));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String decrypt(String key, String iv, String encrypted) {
        // System.out.println("key :: " + key);
        // System.out.println("iv :: " + iv);
        // System.out.println("encrypted :: " + encrypted);
        byte[] bytesOfKey = null;
        byte[] ivBytes = null;
        byte[] encryptedBytes = null;
        // byte[] bytesOfKey = DatatypeConverter.parseHexBinary(key);
        try {
            bytesOfKey = Hex.decodeHex(key.toCharArray());
        } catch (Exception e) {

        }

        // byte[] ivBytes = DatatypeConverter.parseHexBinary(iv);
        try {
            ivBytes = Hex.decodeHex(iv.toCharArray());
            ;
        } catch (Exception e) {

        }

        // byte[] encryptedBytes = DatatypeConverter.parseHexBinary(encrypted);
        try {
            encryptedBytes = Hex.decodeHex(encrypted.toCharArray());
        } catch (Exception e) {

        }

        byte[] resultBytes = null;

        Cipher cipher = null;

        String decrypted = "";

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bytesOfKey, "AES"), new IvParameterSpec(ivBytes));

            resultBytes = cipher.doFinal(encryptedBytes);

            decrypted = new String(resultBytes, "UTF-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return decrypted;
    }

    public String decryptB64Php(String key, String iv, String encrypted) {
        byte[] bytesOfKey = null;
        byte[] ivBytes = null;
        byte[] encryptedBytes = null;

        bytesOfKey = new byte[key.length() / 2];
        for (int i = 0; i < bytesOfKey.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(key.substring(index, index + 2), 16);
            bytesOfKey[i] = (byte) v;
        }

        ivBytes = new byte[iv.length() / 2];
        for (int i = 0; i < ivBytes.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(iv.substring(index, index + 2), 16);
            ivBytes[i] = (byte) v;
        }

        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
            byte[] checkSumInputBytes = encrypted.getBytes("UTF-8");
            localMessageDigest.update(checkSumInputBytes);
            encryptedBytes = localMessageDigest.digest();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        byte[] resultBytes = null;

        Cipher cipher = null;

        String decrypted = "";

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bytesOfKey, "AES"), new IvParameterSpec(ivBytes));

            resultBytes = cipher.doFinal(encryptedBytes);

            decrypted = new String(resultBytes, "UTF-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return decrypted;

    }

    public String decryptB64(String key, String iv, String encrypted) {
        // System.out.println("key :: " + key);
        // System.out.println("iv :: " + iv);
        // System.out.println("encrypted :: " + encrypted);
        byte[] bytesOfKey = null;
        byte[] ivBytes = null;
        byte[] encryptedBytes = null;
        // byte[] bytesOfKey = DatatypeConverter.parseHexBinary(key);
        try {
            bytesOfKey = Hex.decodeHex(key.toCharArray());
        } catch (Exception e) {

        }

        // byte[] ivBytes = DatatypeConverter.parseHexBinary(iv);
        try {
            ivBytes = Hex.decodeHex(iv.toCharArray());
            ;
        } catch (Exception e) {

        }

        // byte[] encryptedBytes = DatatypeConverter.parseHexBinary(encrypted);
        try {
            encryptedBytes = Base64.decodeBase64(encrypted.getBytes());
        } catch (Exception e) {
            System.out.println("error decryptB64");
        }

        byte[] resultBytes = null;

        Cipher cipher = null;

        String decrypted = "";

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(bytesOfKey, "AES"), new IvParameterSpec(ivBytes));

            resultBytes = cipher.doFinal(encryptedBytes);

            decrypted = new String(resultBytes, "UTF-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return decrypted;
    }

    public String cryptB64(String key, String iv, String decrypted) {
        //System.out.println("key :: " + key);
        //System.out.println("iv :: " + iv);
        //System.out.println("encrypted :: " + decrypted);
        byte[] bytesOfKey = null;
        byte[] ivBytes = null;
        byte[] decryptedBytes = null;
        try {
            bytesOfKey = Hex.decodeHex(key.toCharArray());
        } catch (Exception e) {

        }

        try {
            ivBytes = Hex.decodeHex(iv.toCharArray());
            ;
        } catch (Exception e) {

        }

        try {
            // decryptedBytes =
            // org.apache.commons.codec.binary.Hex.decodeHex(decrypted.toCharArray());
            decryptedBytes = decrypted.getBytes("UTF-8");
        } catch (Exception e) {
            // Log.e("decryptedBytes", e.getMessage());
            try {
                System.out.println("error cryptB64");
                decryptedBytes = decrypted.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        byte[] resultBytes = null;

        Cipher cipher = null;

        String encrypted = "";

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(bytesOfKey, "AES"), new IvParameterSpec(ivBytes));

            resultBytes = cipher.doFinal(decryptedBytes);

            encrypted = new String(Base64.encodeBase64(resultBytes), "UTF-8");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encrypted;
    }

    public static String encryptB64(String key, String initVector, String value) {
        try {
            // TODO ESTE METODO SE CREO PARA PROBAR
            IvParameterSpec iv = new IvParameterSpec(
                    Hex.decodeHex(initVector.toCharArray()));
            SecretKeySpec skeySpec = new SecretKeySpec(Hex.decodeHex(key.toCharArray()),
                    "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            // System.out.println("encrypted string: " +
            // Arrays.toString(Base64.encode(encrypted)));

            return new String(Base64.encodeBase64(encrypted));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String encriptaInformacionB64(String keySource, String data) {
        String result = "";
        try {
            String key = keySource.substring(0, 32);
            String iv = keySource.substring(32, 64);

            result = cryptB64(key, iv, data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String desencriptaInformacionB64Php(String keySource, String data) {
        String result = "";
        try {
            String key = keySource.substring(0, 32);
            String iv = keySource.substring(32, 64);

            result = decryptB64Php(key, iv, data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String desencriptaInformacionB64(String keySource, String data) {
        String result = "";
        try {
            String key = keySource.substring(0, 32);
            String iv = keySource.substring(32, 64);

            result = decryptB64(key, iv, data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String generaHmac(String cadena, String hmacKey) {
        // System.out.println("cadena :: " + cadena);
        // System.out.println("hmacKey :: " + hmacKey);

        Mac sha256_HMAC;
        String hmac;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            // SecretKeySpec secret_key = new SecretKeySpec(
            // DatatypeConverter.parseHexBinary(hmacKey), "HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(
                    Hex.decodeHex(hmacKey.toCharArray()), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] result = sha256_HMAC.doFinal(cadena.getBytes("UTF-8"));
            hmac = new String(Hex.encodeHex(result));
        } catch (Exception e) {
            return "";
        }

        return hmac;
    }

    public String generaHmacB64(String cadena, String hmacKey) {
        // System.out.println("cadena :: " + cadena);
        // System.out.println("hmacKey :: " + hmacKey);

        Mac sha256_HMAC;
        String hmac;
        String hmacResult;
        try {
            sha256_HMAC = Mac.getInstance("HmacSHA256");
            // SecretKeySpec secret_key = new SecretKeySpec(
            // DatatypeConverter.parseHexBinary(hmacKey), "HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(
                    Hex.decodeHex(hmacKey.toCharArray()), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            byte[] result = sha256_HMAC.doFinal(cadena.getBytes("UTF-8"));
            hmac = new String(Hex.encodeHex(result));
            byte[] hmacHex = Hex.decodeHex(hmac.toCharArray());
            byte[] base64 = Base64.encodeBase64(hmacHex);
            hmacResult = new String(base64);
        } catch (Exception e) {
            return "";
        }

        return hmacResult;
    }

    public String base64Encoder(String cadena) {
        byte[] data = new byte[0];
        String result = "";
        try {
            data = cadena.getBytes("UTF-8");
            // String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            byte[] base64 = Base64.encodeBase64(data);
            result = new String(base64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String base64Decoder(String cadena) {
        byte[] data = new byte[0];
        String result = "";
        try {
            data = cadena.getBytes("UTF-8");
            // String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            byte[] base64 = Base64.decodeBase64(data);
            result = new String(base64);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String base64DecoderHex(String cadena) {
        byte[] data = new byte[0];
        String result = "";
        try {
            data = cadena.getBytes("UTF-8");
            // String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            byte[] base64 = Base64.decodeBase64(data);
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < base64.length; i++) {
                String hex = Integer.toHexString(0xff & base64[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            result = hexString.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String generaConsecutivo(String s) {
        String consecutivo = s;
        while (consecutivo.length() < 3) {
            consecutivo = "0" + consecutivo;
        }
        return consecutivo;
    }

    public String xorHex(String a, String b) {
        // TODO: Validation
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            // System.out.println("a.charAt(i) = " + a.charAt(i) + " /
            // fromHex(a.charAt(i)) = " + fromHex(a.charAt(i)));
            // System.out.println("b.charAt(i) = " + b.charAt(i) + " /
            // fromHex(b.charAt(i)) = " + fromHex(b.charAt(i)));
            // System.out.println("Result :: " + (fromHex(a.charAt(i)) ^
            // fromHex(b.charAt(i))));
            // System.out.println("Result toHex:: " + toHex(fromHex(a.charAt(i))
            // ^ fromHex(b.charAt(i))));
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }

    public String xorHex2(String a, String b) {
        // TODO: Validation
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }

    private static int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }

    private char toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }

    public String getTelefonoFromDEV(String dev) {
        String telefono = "";
        int separador = 0;

        separador = dev.indexOf("/");
        telefono = dev.substring(0, separador);

        return telefono;
    }

    public String getConsecutivoFromDEV(String dev) {
        String consecutivo = "";
        int separador = 0;

        separador = dev.indexOf("/");
        consecutivo = dev.substring(separador + 1, dev.length());

        return consecutivo;
    }

    private String binarioToHex(String bin) {
        String result = "";
        StringBuilder resultado = new StringBuilder();
        try {

            for (int i = 0; i < bin.length() - 1; i += 4) {
                int numero = Integer.parseInt(bin.substring(i, i + 4), 2);
                String reprHex = Integer.toString(numero, 16);
                resultado.append(reprHex);
            }
            result = resultado.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String decimalToBinario(int numero) {
        StringBuilder ala = new StringBuilder();
        int n = numero;
        String numerobinario = "";
        numerobinario = numerobinario + (n % 2);
        n = n / 2;

        while (n >= 2) {
            numerobinario = numerobinario + (n % 2);
            n = n / 2;
        }
        numerobinario = numerobinario + n;
        StringBuilder cadena = ala.append(numerobinario);
        cadena = ala.reverse();
        // System.out.println(cadena);
        return cadena.toString();
    }

    public String ComplementaBinario(String b, int r) {
        String binario = b;
        while (binario.length() < r) {
            binario = "0" + binario;
        }
        return binario;
    }

    public String getFolioMensajeCobro() {
        String folio = "";
        try {
            Calendar fecha = Calendar.getInstance();
            int año = fecha.get(Calendar.YEAR);
            int soloAño = Integer.valueOf(String.valueOf(año).substring(2));
            int mes = fecha.get(Calendar.MONTH) + 1;
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            int hora = fecha.get(Calendar.HOUR_OF_DAY);
            int minuto = fecha.get(Calendar.MINUTE);
            int segundo = fecha.get(Calendar.SECOND);
            int miliSegundo = fecha.get(Calendar.MILLISECOND);
            int miliSegundoMod = (miliSegundo % 128);

            String binAño = ComplementaBinario(decimalToBinario(soloAño), 7);
            String binMes = ComplementaBinario(decimalToBinario(mes), 4);
            String binDia = ComplementaBinario(decimalToBinario(dia), 5);
            String binHora = ComplementaBinario(decimalToBinario(hora), 5);
            String binMinuto = ComplementaBinario(decimalToBinario(minuto), 6);
            String binSegundo = ComplementaBinario(decimalToBinario(segundo), 6);
            String binMiliSeg = ComplementaBinario(decimalToBinario(miliSegundo), 7);
            String binMiliSegMod = ComplementaBinario(decimalToBinario(miliSegundoMod), 7);

            String cadena = binAño + binMes + binDia + binHora + binMinuto + binSegundo + binMiliSegMod;
            folio = binarioToHex(cadena);
            // System.out.println("Fecha Actual: "+ dia + "/" + (mes) + "/" +
            // año);
            // System.out.printf("Hora Actual: %02d:%02d:%02d %n", hora, minuto,
            // segundo);
            // System.out.println("-------------Fecha
            // desglosada----------------");
            // System.out.println("El año es: "+ soloAño + " Binario :: " +
            // binAño);
            // System.out.println("El mes es: "+ mes + " Binario :: " + binMes);
            // System.out.println("El día es: "+ dia + " Binario :: " + binDia);
            // System.out.printf("La hora es: %02d %n", hora);
            // System.out.println("La hora es: " + hora + " Binario :: " +
            // binHora);
            // System.out.println("El minuto es: " + minuto + " Binario :: " +
            // binMinuto);
            // System.out.println("El segundo es: " + segundo + " Binario :: " +
            // binSegundo);
            // System.out.println("El mili segundo es: " + miliSegundo + "
            // Binario :: " + binMiliSeg);
            // System.out.println("El mili segundo mod 128: " + miliSegundoMod +
            // " Binario :: " + binMiliSegMod);
            // System.out.println("cadena : " + cadena);
            // System.out.println("cadenaHex : " + binarioToHex(cadena));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return folio;
    }

    public String claveRatreo(Long consecutivo) {
        Calendar fechaConvertir = Calendar.getInstance();
        SimpleDateFormat Fecha;
        Date fecha = (Date) Calendar.getInstance().getTime();
        String sFecha = "";
        String cve;

        Fecha = new SimpleDateFormat("yyyyMMdd");
        fechaConvertir.setTime(Calendar.getInstance().getTime());
        sFecha = Fecha.format(fecha);
        cve = String.format("%s%s%s             ", sFecha, "90659ASPOPC00", generaReferenciaNumerica(consecutivo));
        cve = cve.substring(0, 29).trim();
        return cve;
    }

    private int generaReferenciaNumerica(Long consecutivo) {
        Calendar fechaConvertir = Calendar.getInstance();
        String ref;
        int refNum;
        int dia;
        int min;
        int hora;
        int seg;
        int mil;
        Long iseq;
        String seq;

        iseq = 0L;
        try {
            // iseq= ifzSpeiDAO.obtenReferencia();
            iseq = consecutivo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        fechaConvertir.setTime(Calendar.getInstance().getTime());
        dia = fechaConvertir.get(Calendar.DAY_OF_YEAR);
        hora = fechaConvertir.get(Calendar.HOUR_OF_DAY);
        min = fechaConvertir.get(Calendar.MINUTE);
        seg = fechaConvertir.get(Calendar.SECOND);
        mil = fechaConvertir.get(Calendar.MILLISECOND);

        if (iseq != 0) {
            seq = String.format("%04d", iseq);
            System.out.println("uso la secuencia :: " + seq);
        } else {
            seq = String.valueOf(hora + min + seg + mil);
            System.out.println("uso la fecha :: " + seq);
        }

        ref = String.format("%s%s000000000", dia, seq);
        System.out.println("referencia antes del corte :: " + ref);
        ref = ref.substring(0, 7);
        System.out.println("referencia despues del corte :: " + ref);
        refNum = (int) Integer.valueOf(ref);
        return refNum;
    }

    public String generaKeySource(String s1, String s2, String s3) {
        String keySource = "";
        try {
            keySource = sha512(s1);
            keySource = sha512(keySource + s2 + s3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keySource;
    }

    public String generaKeySource(String s1, String s2) {
        String keySource = "";
        try {
            keySource = sha512(s1);
            keySource = sha512(keySource + s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keySource;
    }

    public String generaKeySource(String s1) {
        String keySource = "";
        try {
            keySource = sha512(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keySource;
    }

    public String getKey(String keySource) {
        String key = "";
        try {
            key = keySource.substring(0, 32);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public String getIv(String keySource) {
        String iv = "";
        try {
            iv = keySource.substring(32, 64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iv;
    }

    public String getClaveHmac(String keySource) {
        String claveHmac = "";
        try {
            claveHmac = keySource.substring(64, 128);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claveHmac;
    }

    public String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public String getMonto(Double monto) {
        String result = "";
        NumberFormat formato = NumberFormat.getCurrencyInstance();

        result = formato.format(monto);

        return result;
    }

    public String generaMaskedKeySource(String s1, String s2) {
        String maskedKeySource = "";
        try {
            String shaTmp = sha512Hex(s1);
            // System.out.println("shaTmp :: " + shaTmp);
            // System.out.println("s2 :: " + s2);
            maskedKeySource = xorHex(shaTmp, s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maskedKeySource;
    }

    public String generaKeySourceLocal(String s1, String s2) {
        String keySourceLocal = "";
        try {
            keySourceLocal = sha512(s1 + s1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keySourceLocal;
    }

    public String rsaDecrypt(PrivateKey key, String data){

        byte[] input = DatatypeConverter.parseBase64Binary(data);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA");

            Key privKey = key;

            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainText = cipher.doFinal(input);
            System.out.println("plain : " + new String(plainText));

            return new String(plainText);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {

            e.printStackTrace();
        } catch (InvalidKeyException e) {

            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {

            e.printStackTrace();
        } catch (BadPaddingException e) {

            e.printStackTrace();
        }

        return "";

    }

    public String getSecurePin(String pin, String referencia, String key1, String key2){
        String securePin = "";
        try{
            iso9564 crypt = new iso9564();
            crypt.setDebugEnable(0, 0);
            crypt.setPanPadChar('0');
            crypt.setPinPadChar('F');
            crypt.setKeyEncripterTralaterKey("1111222233334444", "1111111111111111");

            String referenciaTmp="9001"+referencia+"01";
            pin=referenciaTmp.substring(4, 15)+"1"+pin;
            securePin=crypt.getPvv(pin, key1, key2);

        }catch (Exception e){
            e.printStackTrace();
        }
        return securePin;
    }

    public Double redondeaCentabos(Double monto, Integer decimalesPermitidos) {
        Double montoRendondeado = monto;
        try {
            String[] splitter = monto.toString().split("\\.");

            Integer numDecimales = splitter[1].length();
            if (numDecimales > 2) {
                long factor = (long) Math.pow(10, decimalesPermitidos);
                montoRendondeado = montoRendondeado * factor;
                long tmp = Math.round(montoRendondeado);
                montoRendondeado = (double) tmp / factor;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return montoRendondeado;
    }
}
