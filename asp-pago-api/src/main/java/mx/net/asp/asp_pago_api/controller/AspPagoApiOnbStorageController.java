package mx.net.asp.asp_pago_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.asp_pago_api.service.AspPagoApiOnbService;
import mx.net.asp.asp_pago_api.utilerias.ErrorHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/onb/storage/")
@Log4j2
@RequiredArgsConstructor
public class AspPagoApiOnbStorageController {

    private final AspPagoApiOnbService aspPagoApiOnbService;
    private final ErrorHandler errorHandler;

    @PostMapping(value = "/biometria-asppago", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBiometria(
            @RequestParam(value = "curp", required = false) String curp,
            @RequestParam(value = "ineFront", required = false) MultipartFile ineFront,
            @RequestParam(value = "ineRev", required = false) MultipartFile ineRev,
            @RequestParam(value = "fotoSelfie", required = false) MultipartFile fotoSelfie
    ) {
        String response = aspPagoApiOnbService
                .uploadFilesBiometriaAspPago(curp, ineFront, ineRev, fotoSelfie);
        log.info("response /onb/storage/biometria-asppago: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/obtenerDatosOcr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadBiometria(
            @RequestParam(value = "files", required = false) MultipartFile[] files
    ) {
        String response = aspPagoApiOnbService.obtenerDatosOCR(files);
        log.info("response /onb/storage/obtenerDatosOcr: {}", response);
        return ResponseEntity.ok(response);
    }
}