package net.cero.ahorro.ws;

import net.cero.ahorro.servicios.RecargaMovil;
import net.cero.data.RecargaMovilDTO;
import net.cero.data.Respuesta;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RecargaMovilWs {
    private final RecargaMovil recargaMovil;

    public RecargaMovilWs(RecargaMovil recargaMovil) {
        this.recargaMovil = recargaMovil;
    }

    @RequestMapping(value = "/recargaMovil",method = RequestMethod.POST)
    public ResponseEntity<Respuesta> recargaMovil(@RequestBody @Valid RecargaMovilDTO recargaMovilDTO, BindingResult bindingResult){
        return ResponseEntity.ok(this.recargaMovil.getRecargaMovil(recargaMovilDTO, bindingResult));
    }

    @RequestMapping(value = "/catalogoCompaniasTelefonicas", method = RequestMethod.GET)
    public ResponseEntity<Respuesta> consultaCatalogoComaniasTelefonicas() {
        return ResponseEntity.ok(this.recargaMovil.consultaCatalogoCompanias());
    }
}
