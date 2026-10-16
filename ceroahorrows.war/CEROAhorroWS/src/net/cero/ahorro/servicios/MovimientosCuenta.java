package net.cero.ahorro.servicios;

import net.cero.data.MovimientoCuentaRequest;
import net.cero.data.Respuesta;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

public interface MovimientosCuenta {
    public Respuesta consultaMovimientosCuenta(@Valid MovimientoCuentaRequest movimientoCuentaRequest, BindingResult bindingResult);
}
