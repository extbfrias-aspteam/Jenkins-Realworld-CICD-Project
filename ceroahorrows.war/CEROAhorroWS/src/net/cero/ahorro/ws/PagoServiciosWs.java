package net.cero.ahorro.ws;

import net.cero.ahorro.servicios.PagoServicios;
import net.cero.data.PagoServiciosConsultaDTO;
import net.cero.data.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PagoServiciosWs {

    private PagoServicios pagoServicios;

    @Autowired
    public PagoServiciosWs(PagoServicios pagoServicios) {
        this.pagoServicios = pagoServicios;
    }

    @RequestMapping(value = "/proveedoresServicios")
    public ResponseEntity<Respuesta> consultaProveedoresServicios() {
        return ResponseEntity.ok(pagoServicios.getProveedores());
    }

    @RequestMapping(value = "/consultaPagoServicios")
    public ResponseEntity<Respuesta> consultaPagoServicios(@RequestBody @Valid PagoServiciosConsultaDTO pagoServiciosConsultaDTO, BindingResult bindingResult) {
        return ResponseEntity.ok(pagoServicios.consultaPagoServicios(pagoServiciosConsultaDTO, bindingResult));
    }
}
