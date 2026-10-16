package net.cero.ahorro.servicios;

import net.cero.data.ConsultaOperacionesCodiReqDTO;
import net.cero.data.Respuesta;
import org.springframework.validation.BindingResult;

public interface CodiService {

    Respuesta consultaOperacionesCodi(ConsultaOperacionesCodiReqDTO consultaOperacionesCodiDTO, BindingResult bindingResult);
}
