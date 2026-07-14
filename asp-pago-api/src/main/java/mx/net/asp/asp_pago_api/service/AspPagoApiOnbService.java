package mx.net.asp.asp_pago_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.dto.RespuestaDTO;
import mx.net.asp.asp_pago_api.utilerias.CifradoUtil;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import mx.net.asp.asp_pago_api.ws.asp.WsAspPagoStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Log4j2
@RequiredArgsConstructor
public class AspPagoApiOnbService {

    private final CifradoUtil cifradoUtil;
    private final ErrorHandler errorHandler;
    private final WsAspPagoStorage wsAspPagoStorage;
    private final TimeoutConfigService timeoutConfigService;

    @Value("${timeout.extended}")
    private Integer timeoutExtended;
    @Value("${ms.documents.onb.upload.files.biometria.asppago}")
    private String pathSubeArchivosBiometriaAspPagoOnb;
    @Value("${ms.documents.onb.obtener.datos.ocr}")
    private String pathObtenerDatosOCR;

    public String uploadFilesBiometriaAspPago(
            String identificador,
            MultipartFile ineFront,
            MultipartFile ineRev,
            MultipartFile fotoSelfie
    ) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            increaseTimeout();

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("curp", identificador);

            if (ineFront != null && !ineFront.isEmpty()) {
                body.add("ineFront", multipartFileToResource(ineFront));
            }
            if (ineRev != null && !ineRev.isEmpty()) {
                body.add("ineRev", multipartFileToResource(ineRev));
            }
            if (fotoSelfie != null && !fotoSelfie.isEmpty()) {
                body.add("fotoSelfie", multipartFileToResource(fotoSelfie));
            }

            respuestaDTO = wsAspPagoStorage.enviarPeticion(pathSubeArchivosBiometriaAspPagoOnb, body,
                    HttpMethod.POST, null);
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    public String obtenerDatosOCR(
            MultipartFile[] files
    ) {
        RespuestaDTO respuestaDTO = new RespuestaDTO();
        try {
            increaseTimeout();

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            if (files != null) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        body.add("files", multipartFileToResource(file));
                    }
                }
            }

            respuestaDTO = wsAspPagoStorage.enviarPeticion(
                    pathObtenerDatosOCR,
                    body,
                    HttpMethod.POST,
                    null
            );
        } catch (Exception e) {
            errorHandler.handleException(e);
        }
        return cifradoUtil.encryptResponse(respuestaDTO);
    }

    private void increaseTimeout() {
        timeoutConfigService.setConnectTimeout(timeoutExtended);
        timeoutConfigService.setReadTimeout(timeoutExtended);
    }

    private Resource multipartFileToResource(MultipartFile file) throws IOException {
        return new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
    }
}