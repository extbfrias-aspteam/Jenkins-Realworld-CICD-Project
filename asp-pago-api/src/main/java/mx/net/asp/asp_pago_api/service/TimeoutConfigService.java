package mx.net.asp.asp_pago_api.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
public class TimeoutConfigService {
    @Value("${timeout.default}")
    private int timeoutDefault;

    private int connectTimeout = timeoutDefault; // Valor por defecto 60 segundos
    private int readTimeout = timeoutDefault;    // Valor por defecto 60 segundos
}
