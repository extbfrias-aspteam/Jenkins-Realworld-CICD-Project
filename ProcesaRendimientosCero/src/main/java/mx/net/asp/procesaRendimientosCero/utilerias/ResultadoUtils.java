package mx.net.asp.procesaRendimientosCero.utilerias;

import mx.net.asp.procesaRendimientosCero.model.ResultadoProcesaRendimientos;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResultadoUtils {

    public static void agregarRegistro(List<ResultadoProcesaRendimientos> resultadoProcesaMultiCuentasList, ResultadoProcesaRendimientos resultado,
                                       String resultadoProceso, String mensaje) {
        resultado.setResultado(resultadoProceso);
        resultado.setMensaje(mensaje);
        resultadoProcesaMultiCuentasList.add(resultado);
    }
}
