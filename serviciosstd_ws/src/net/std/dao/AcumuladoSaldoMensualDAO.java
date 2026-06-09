package net.std.dao;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Map;

public class AcumuladoSaldoMensualDAO {

    private static final Logger log = LogManager.getLogger(AcumuladoSaldoMensualDAO.class);

    /*procrea*/
    private JdbcTemplate jdbcTemplatePr;
    /*cero*/
    private JdbcTemplate  jdbcTemplate;
    private String leerTransaccionAcumulable;
    private String obtenerAcumuladoMensual;
    private String obtenerValorUdis;
    private String obtenerParametroUdi;


    public Double buscarSaldoAcumuladoMensualCuenta(String cuenta,int mes,int anio)
    {
        Double saldoAcumuladoMensual = 0d;
        try{
            saldoAcumuladoMensual = jdbcTemplate.queryForObject(obtenerAcumuladoMensual, new Object[]{cuenta,anio,mes},Double.class);
            if(saldoAcumuladoMensual == null)
            {
                saldoAcumuladoMensual = 0d;
            }
        }
        catch (DataAccessException ex) {
            log.info("No encontró registros");
        }
        catch(Exception e){
            log.error("Ocurrio un error dentro del metodo buscarSaldoAcumuladoMensualCuenta",e);
        }
        return saldoAcumuladoMensual;
    }

    public boolean buscarEsAcumulable(String claveTrans)
    {
        Boolean esAcumulable = false;
        try{
            esAcumulable = jdbcTemplate.queryForObject(leerTransaccionAcumulable,Boolean.class,claveTrans);
            if(esAcumulable == null)
            {
                esAcumulable = false;
            }
        }
        catch (DataAccessException ex) {
            log.info("No encontró registros");
            esAcumulable = false;
        }
        catch(Exception e){
            log.error("Ocurrio un error dentro del metodo buscarLimiteMensualCuenta",e);
            esAcumulable = false;
        }
        return esAcumulable;
    }

    public int buscarMaxLimUdisCF()
    {
        Integer valorUdi = 0;
        try{
            valorUdi = jdbcTemplatePr.queryForObject(obtenerParametroUdi,Integer.class);
            if(valorUdi == null)
            {
                valorUdi = 0;
            }
        }
        catch (DataAccessException ex) {
            log.info("No encontró registros");
            valorUdi = 0;
        }
        catch(Exception e){
            log.error("Ocurrio un error dentro del metodo buscarLimiteMensualCuenta",e);
            valorUdi = 0;
        }
        return valorUdi;
    }

    public Double buscarValorUdi(LocalDate fecha)
    {
        Double valorUdiFinal = 0d;
        try{
            int limCiclos = 12;
            while(limCiclos > 0)
            {
                int anio = fecha.get(ChronoField.YEAR);
                int mes = fecha.get(ChronoField.MONTH_OF_YEAR);
                Map<String, Object> valoresUdi = null;
                try{
                    log.info("qObtenerValorUdis: {},anio:{}, mes: {}",obtenerValorUdis,anio,mes);
                    valoresUdi = jdbcTemplatePr.queryForMap(obtenerValorUdis,new Object[]{anio}) ;
                }
                catch (DataAccessException ex) {
                }

                if(valoresUdi != null)
                {
                    String claveMes="";
                    switch(mes){
                        case 1:
                            claveMes ="m_01";
                            break;
                        case 2:
                            claveMes ="m_02";
                            break;
                        case 3:
                            claveMes ="m_03";
                            break;
                        case 4:
                            claveMes ="m_04";
                            break;
                        case 5:
                            claveMes ="m_05";
                            break;
                        case 6:
                            claveMes ="m_06";
                            break;
                        case 7:
                            claveMes ="m_07";
                            break;
                        case 8:
                            claveMes ="m_08";
                            break;
                        case 9:
                            claveMes ="m_09";
                            break;
                        case 10:
                            claveMes ="m_10";
                            break;
                        case 11:
                            claveMes ="m_11";
                            break;
                        case 12:
                            claveMes ="m_12";
                            break;
                        default:
                            break;
                    }
                    if(!StringUtils.isBlank(claveMes))
                    {
                        if(valoresUdi.get(claveMes) != null)
                        {
                            valorUdiFinal = (double) valoresUdi.get(claveMes);
                            log.info("Udi Encontrada en anio: {},mes: {}, valor: {}",anio,mes,valorUdiFinal);
                            break;
                        }
                    }
                }
                fecha=fecha.minusMonths(1);
                limCiclos--;
            }
        }
        catch(Exception e){
            log.error("Ocurrio un error dentro del metodo buscarLimiteMensualCuenta",e);
        }
        return valorUdiFinal;
    }

    public JdbcTemplate getJdbcTemplatePr() {
        return jdbcTemplatePr;
    }

    public void setJdbcTemplatePr(JdbcTemplate jdbcTemplatePr) {
        this.jdbcTemplatePr = jdbcTemplatePr;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getLeerTransaccionAcumulable() {
        return leerTransaccionAcumulable;
    }

    public void setLeerTransaccionAcumulable(String leerTransaccionAcumulable) {
        this.leerTransaccionAcumulable = leerTransaccionAcumulable;
    }

    public String getObtenerAcumuladoMensual() {
        return obtenerAcumuladoMensual;
    }

    public void setObtenerAcumuladoMensual(String obtenerAcumuladoMensual) {
        this.obtenerAcumuladoMensual = obtenerAcumuladoMensual;
    }

    public String getObtenerValorUdis() {
        return obtenerValorUdis;
    }

    public void setObtenerValorUdis(String obtenerValorUdis) {
        this.obtenerValorUdis = obtenerValorUdis;
    }

    public String getObtenerParametroUdi() {
        return obtenerParametroUdi;
    }

    public void setObtenerParametroUdi(String obtenerParametroUdi) {
        this.obtenerParametroUdi = obtenerParametroUdi;
    }
}
