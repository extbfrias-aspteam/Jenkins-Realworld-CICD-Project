package net.cero.ahorro.servicios;

import net.cero.data.PagoServiciosConsultaDTO;
import net.cero.data.Respuesta;
import org.springframework.validation.BindingResult;

public interface PagoServicios {
    Respuesta getProveedores();

    Respuesta consultaPagoServicios(PagoServiciosConsultaDTO pagoServiciosConsultaDTO, BindingResult bindingResult);
}
