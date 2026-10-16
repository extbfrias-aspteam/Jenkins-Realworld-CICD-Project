package net.cero.ahorro.servicios;

import net.cero.data.RecargaMovilDTO;
import net.cero.data.Respuesta;
import org.springframework.validation.BindingResult;

public interface RecargaMovil {
    Respuesta getRecargaMovil(RecargaMovilDTO recargaMovilDTO, BindingResult bindingResult);

    Respuesta consultaCatalogoCompanias();
}
