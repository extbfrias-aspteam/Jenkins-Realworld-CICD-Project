package net.cero.ahorro.logica;

import java.util.Calendar;

public class GeneracionFolioLogic {

	public String getFolio(){
        String folio = "";
        try{
            Calendar fecha = Calendar.getInstance();
            int año = fecha.get(Calendar.YEAR);
            int soloAño = Integer.valueOf(String.valueOf(año).substring(2));
            int mes = fecha.get(Calendar.MONTH) + 1;
            int dia = fecha.get(Calendar.DAY_OF_MONTH);
            int hora = fecha.get(Calendar.HOUR_OF_DAY);
            int minuto = fecha.get(Calendar.MINUTE);
            int segundo = fecha.get(Calendar.SECOND);
            int miliSegundo = fecha.get(Calendar.MILLISECOND);
            int miliSegundoMod = (miliSegundo % 128);

            String binAño = ComplementaBinario(decimalToBinario(soloAño),7);
            String binMes = ComplementaBinario(decimalToBinario(mes),4);
            String binDia = ComplementaBinario(decimalToBinario(dia),5);
            String binHora = ComplementaBinario(decimalToBinario(hora),5);
            String binMinuto = ComplementaBinario(decimalToBinario(minuto),6);
            String binSegundo = ComplementaBinario(decimalToBinario(segundo),6);
            String binMiliSeg = ComplementaBinario(decimalToBinario(miliSegundo),7);
            String binMiliSegMod = ComplementaBinario(decimalToBinario(miliSegundoMod),7);

            String cadena = binAño + binMes + binDia + binHora + binMinuto + binSegundo + binMiliSegMod;
            folio = binarioToHex(cadena);
            //log.info("Fecha Actual: "+ dia + "/" + (mes) + "/" + año);
            //System.out.printf("Hora Actual: %02d:%02d:%02d %n", hora, minuto, segundo);
            //log.info("-------------Fecha desglosada----------------");
            //log.info("El año es: "+ soloAño + " Binario :: " + binAño);
            //log.info("El mes es: "+ mes + " Binario :: " + binMes);
            //log.info("El día es: "+ dia + " Binario :: " + binDia);
            //System.out.printf("La hora es: %02d %n", hora);
            //log.info("La hora es: " + hora + " Binario :: " + binHora);
            //log.info("El minuto es: " + minuto + " Binario :: " + binMinuto);
            //log.info("El segundo es: " + segundo + " Binario :: " + binSegundo);
            //log.info("El mili segundo es: " + miliSegundo + " Binario :: " + binMiliSeg);
            //log.info("El mili segundo mod 128: " + miliSegundoMod + " Binario :: " + binMiliSegMod);
            //log.info("cadena : " + cadena);
            //log.info("cadenaHex : " + binarioToHex(cadena));
        }catch(Exception e){
            e.printStackTrace();
        }
        return folio;
    }

    private String ComplementaBinario(String b, int r) {
        String binario = b;
        while(binario.length() < r){
            binario = "0" + binario;
        }
        return binario;
    }
    
    private String decimalToBinario(int numero){
        StringBuilder ala = new StringBuilder();
        int n = numero;
        String numerobinario = "";
        numerobinario = numerobinario + (n % 2);
        n = n / 2;

        while (n >= 2) {
            numerobinario = numerobinario + (n % 2);
            n = n / 2;
        }
        numerobinario = numerobinario + n;
        StringBuilder cadena = ala.append(numerobinario);
        cadena = ala.reverse();
        //log.info(cadena);
        return cadena.toString();
    }
    
    private String binarioToHex(String bin){
        String result = "";
        StringBuilder resultado = new StringBuilder();
        try{

            for ( int i = 0 ; i < bin.length()-1 ; i +=4 ) {
                int    numero  = Integer.parseInt( bin.substring( i, i+4) , 2 );
                String reprHex = Integer.toString( numero, 16 );
                resultado.append( reprHex );
            }
            result = resultado.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
