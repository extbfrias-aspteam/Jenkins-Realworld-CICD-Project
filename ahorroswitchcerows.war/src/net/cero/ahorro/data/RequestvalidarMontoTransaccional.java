/**
 * RequestvalidarMontoTransaccional.java
 * ASP Integra Opciones  2024-12-22
 * https://www.aspintegraopciones.com/fr/home/
 * @autor rodolfo
 */
package net.cero.ahorro.data;

import net.cero.ws.data.HeaderWS;

/**
 * 
 */
public class RequestvalidarMontoTransaccional {
private HeaderWS header;

private double importe;
private String cuenta;
/**
 * @return the header
 */
public HeaderWS getHeader() {
    return header;
}
/**
 * @param header the header to set
 */
public void setHeader(HeaderWS header) {
    this.header = header;
}
/**
 * @return the importe
 */
public double getImporte() {
    return importe;
}
/**
 * @param importe the importe to set
 */
public void setImporte(double importe) {
    this.importe = importe;
}
/**
 * @return the cuenta
 */
public String getCuenta() {
    return cuenta;
}
/**
 * @param cuenta the cuenta to set
 */
public void setCuenta(String cuenta) {
    this.cuenta = cuenta;
}
}
