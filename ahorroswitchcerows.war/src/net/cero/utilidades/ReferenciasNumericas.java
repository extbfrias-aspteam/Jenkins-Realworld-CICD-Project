package net.cero.utilidades;

import org.apache.commons.lang3.StringUtils;

public class ReferenciasNumericas {
    public static String generarRefNumerica()
    {
        String resultado="";
        Integer minValor = 1;
        Integer maxValor = 9999999;
        int valorNumerico = (int)Math.floor(Math.random()*(maxValor-minValor+1)+minValor);

        resultado = StringUtils.leftPad(String.valueOf(valorNumerico),7,"0");
        return resultado;
    }
}
