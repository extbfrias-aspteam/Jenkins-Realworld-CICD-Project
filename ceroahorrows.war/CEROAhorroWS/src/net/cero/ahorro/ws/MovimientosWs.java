package net.cero.ahorro.ws;

import net.cero.ahorro.servicios.MovimientosCuenta;
import net.cero.data.MovimientoCuentaRequest;
import net.cero.data.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MovimientosWs {

    private MovimientosCuenta movimientosCuenta;

    @Autowired
    public MovimientosWs(MovimientosCuenta movimientosCuenta){
        this.movimientosCuenta = movimientosCuenta;
    }
    @PostMapping(value = "/concentradoMovimientosCuenta")
    public ResponseEntity<Respuesta> consultaMovimientosCuenta(@Valid @RequestBody MovimientoCuentaRequest movimientoCuentaRequest, BindingResult bindingResult){
        return ResponseEntity.ok(movimientosCuenta.consultaMovimientosCuenta(movimientoCuentaRequest, bindingResult));
    }
}
