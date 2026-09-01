package mx.net.asp.asp_pago_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AspPagoApiStorageService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/storage")
@Log4j2
@RequiredArgsConstructor
public class AspPagoApiStorageController {

    private final AspPagoApiStorageService aspPagoStorageService;
    private final ErrorHandler errorHandler;

    @PostMapping(value = "/biometria-asppago", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBiometria(
            @RequestParam(value = "idpersona", required = false) String idpersona,
            @RequestParam(value = "ineFront", required = false) MultipartFile ineFront,
            @RequestParam(value = "ineRev", required = false) MultipartFile ineRev,
            @RequestParam(value = "fotoSelfie", required = false) MultipartFile fotoSelfie
    ) {
        String response = aspPagoStorageService
                .uploadFilesBiometriaAspPago(idpersona, ineFront, ineRev, fotoSelfie);
        log.info("response /api/storage/biometria-asppago: {}", response);
        return ResponseEntity.ok(response);
    }
}