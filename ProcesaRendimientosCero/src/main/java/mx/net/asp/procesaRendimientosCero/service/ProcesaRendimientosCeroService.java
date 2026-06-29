package mx.net.asp.procesaRendimientosCero.service;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.net.asp.procesaRendimientosCero.dao.*;
import mx.net.asp.procesaRendimientosCero.dto.RespuestaDTO;
import mx.net.asp.procesaRendimientosCero.model.*;
import mx.net.asp.procesaRendimientosCero.utilerias.ErrorHandler;
import mx.net.asp.procesaRendimientosCero.utilerias.FechaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.RespuestaUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.ResultadoUtils;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresGenerales;
import mx.net.asp.procesaRendimientosCero.utilerias.errores.ErroresInversiones;
import mx.net.asp.procesaRendimientosCero.ws.asp.MSConsultaSaldo;
import mx.net.asp.procesaRendimientosCero.ws.asp.response.ConsultaSaldoResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

@Component
@Log4j2
@RequiredArgsConstructor
public class ProcesaRendimientosCeroService {

    private final AhorroTransaccionService ahorroTransaccionService;
    private final BitacoraInversionesService bitacoraInversionesService;
    private final EnviaSMSService enviaSMSService;

    private final MSConsultaSaldo msConsultaSaldo;

    private final InversionesDAO inversionesDAO;
    private final NucleoCentralDAO nucleoCentralDAO;
    private final MovimientosCajaDAO movimientosCajaDAO;
    private final ProcreaDAO procreaDAO;
    private final ConceptosCuentaDAO conceptosCuentaDAO;
    private final AhorroDAO ahorroDAO;

    private final ErrorHandler errorHandler;
    private final Gson gson;
    private final FechaUtils fechaUtils;

    @Value("${clave.nombre.inversion}")
    private String NOM_INVERSION;

    @Value("${cve.alta.no.reinv}")
    private String cveAltaNoReinv;
    @Value("${cve.tipo.cta.inversion}")
    private String cveTipoCuentaInversion;
    @Value("${cve.alta.nombre.inversion}")
    private String cveAltaNombreInversion;
    @Value("${cve.update.nombre.inversion}")
    private String cveUpdateNombreInversion;
    @Value("${cve.traspaso.rendimientos}")
    private String cveTraspRend;
    @Value("${cve.traspaso.capital.reinversion}")
    private String cveTraspCapitalReinv;

    @Value("${cve.ini.proc.trasp.rend}")
    private String cveIniProcTraspRend;
    @Value("${cve.fin.proc.trasp.rend}")
    private String cveFinProcTraspRend;

    @Value("${cve.ini.proc.reinv}")
    private String cveIniProcReinv;
    @Value("${cve.fin.proc.reinv}")
    private String cveFinProcReinv;

    @Value("${obs.bitacora.inversiones.def}")
    private String obsDefBitacoraInv;
    @Value("${obs.bitacora.inversiones.nombre}")
    private String obsNombreBitacoraInv;
    @Value("${obs.bitacora.inversiones.alta.no.reinv}")
    private String obsAltaNoReinvBitacoraInv;
    @Value("${obs.bitacora.inversiones.monto}")
    private String obsMontoBitacoraInv;

    @Value("${obs.ini.proceso.trasp.rend}")
    private String obsIniProcTraspRend;
    @Value("${obs.fin.proceso.trasp.rend}")
    private String obsFinProcTraspRend;
    @Value("${obs.ini.proceso.reinv}")
    private String obsIniProcReinv;
    @Value("${obs.fin.proceso.reinv}")
    private String obsFinProcReinv;


    @Value("${secret.key.uuid}")
    private String SECRET_KEY_UUID;
    @Value("${trace.suffix}")
    private String TRACE_SUFFIX;

    @Value("${scheduler.rendimientos.runOnStartup:false}")
    private boolean runOnStartup;

    @PostConstruct
    public void runOnStartup() {
        if (runOnStartup) {
            IniciaProcesoRendimientos();
        }
    }

    //@Scheduled(cron = "0 0 18 * * *") // Todos los días a las 6 PM
    @Scheduled(cron = "${scheduler.rendimientos.cron}")
    public RespuestaDTO IniciaProcesoRendimientos() {
        RespuestaDTO respuesta = new RespuestaDTO();

        List<ResultadoProcesaRendimientos> resultadoLogList = new ArrayList<>();
        ResultadoProcesaRendimientos resultado = new ResultadoProcesaRendimientos();
        try {
            //Obtener cola de traspasos rendimientos
            List<RendimientoPendOBJ> rendimientoPendList = inversionesDAO.obtenerListadoRendimientos("P");
            if (rendimientoPendList == null || rendimientoPendList.isEmpty()) {
                ResultadoProcesaRendimientos resultadoRend = new ResultadoProcesaRendimientos();
                resultadoRend.setProceso("DEPOSITO RENDIMIENTOS");
                ResultadoUtils.agregarRegistro(resultadoLogList, resultadoRend,
                        "XXXX",
                        ErroresInversiones.descError.get(ErroresInversiones.RENDIMIENTOS_PEND_NOT_FOUND));
                log.info("No se encontró cola de rendimientos");
            }

            for (RendimientoPendOBJ rendimientoPendOBJ : rendimientoPendList) {
                // Generar UUID por registro procesado
                String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "-" + TRACE_SUFFIX;
                String traceSignature = firmar(traceId);

                ThreadContext.put("myUuid", traceId);
                ThreadContext.put("myUuidSigned", traceSignature);

                resultado = new ResultadoProcesaRendimientos();
                resultado.setCuentaInversion(rendimientoPendOBJ.getCuentaInversion());
                resultado.setCuentaPadre(rendimientoPendOBJ.getCuentaPadre());
                resultado.setProceso("DEPOSITO RENDIMIENTOS");

                procesaRendimientoPendiente(rendimientoPendOBJ, resultadoLogList, resultado);
                //actualizar el estatus a procesado (X)
                inversionesDAO.actualizaEstatusRendimientoPendById(rendimientoPendOBJ.getId(), "X");
            }

            List<ReinversionPendOBJ> reinversionPendList = inversionesDAO.obtenerListadoReinversiones("P");
            if (reinversionPendList == null || reinversionPendList.isEmpty()) {
                ResultadoProcesaRendimientos resultadoReinv = new ResultadoProcesaRendimientos();
                resultadoReinv.setProceso("REINVERSION");
                ResultadoUtils.agregarRegistro(resultadoLogList, resultadoReinv,
                        "XXXX",
                        ErroresInversiones.descError.get(ErroresInversiones.REINVERSIONES_PEND_NOT_FOUND));
                log.info("No se encontró cola de reinversiones");
            }

            for (ReinversionPendOBJ reinversionPendOBJ : reinversionPendList) {
                // Generar UUID por registro procesado
                String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "-" + TRACE_SUFFIX;
                String traceSignature = firmar(traceId);

                ThreadContext.put("myUuid", traceId);
                ThreadContext.put("myUuidSigned", traceSignature);

                resultado = new ResultadoProcesaRendimientos();
                resultado.setCuentaInversion(reinversionPendOBJ.getCuentaInversion());
                resultado.setCuentaPadre(reinversionPendOBJ.getCuentaPadre());
                resultado.setProceso("REINVERSION");

                procesaReinversionesPendientes(reinversionPendOBJ, resultadoLogList, resultado);
                //actualizar el estatus a procesado (X)
                inversionesDAO.actualizaEstatusReinversionPendById(reinversionPendOBJ.getId(), "X");
            }

            List<DetalleInversionOBJ> inversionesActivasList = inversionesDAO.obtenerInversionesActivasFechaFin();
            if (inversionesActivasList == null || inversionesActivasList.isEmpty()) {
                ResultadoProcesaRendimientos resultadoCanc = new ResultadoProcesaRendimientos();
                resultadoCanc.setProceso("CANCELACION");
                LocalDate hoy = LocalDate.now();
                ResultadoUtils.agregarRegistro(resultadoLogList, resultadoCanc,
                        "XXXX",
                        ErroresInversiones.getMensajeError(ErroresInversiones.INVERSIONES_ACTIVAS_NOT_FOUND, hoy.toString()));
                log.info("No se encontraron inversiones con fecha final de hoy para ser canceladas.");
            }

            for (DetalleInversionOBJ inversionActiva : inversionesActivasList) {
                // Generar UUID por registro procesado
                String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "-" + TRACE_SUFFIX;
                String traceSignature = firmar(traceId);

                ThreadContext.put("myUuid", traceId);
                ThreadContext.put("myUuidSigned", traceSignature);

                resultado = new ResultadoProcesaRendimientos();
                resultado.setCuentaInversion(inversionActiva.getCuentaInversion());
                resultado.setCuentaPadre(inversionActiva.getCuentaPadre());
                resultado.setProceso("CANCELACION");

                procesaCancelacionInversion(inversionActiva, resultadoLogList, resultado);
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            ResultadoProcesaRendimientos resultadoErr = new ResultadoProcesaRendimientos();
            ResultadoUtils.agregarRegistro(resultadoLogList, resultadoErr,
                    "ERROR EXCEPTION",
                    e.getMessage());
        } finally {
            if (resultadoLogList != null && !resultadoLogList.isEmpty()) {
                ahorroDAO.insertarLogResultadoBatch(resultadoLogList);
            }
            // Limpieza garantizada del contexto
            log.info("=== Fin de proceso Rendimientos ===");
            ThreadContext.clearAll();
        }

        return respuesta;
    }

    private void procesaRendimientoPendiente(RendimientoPendOBJ rendimientoPendOBJ, List<ResultadoProcesaRendimientos> resultadoList,
                                             ResultadoProcesaRendimientos resultado) {
        RespuestaDTO respuesta;
        Long idProcesoBit = 0L;
        BitacoraInversionesOBJ bitacoraOBJ = null;
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(Locale.of("es", "MX"));
        try {
            log.info("=== Se procesa cuenta [{}] en proceso de rendimientos ===", rendimientoPendOBJ.getCuentaInversion());

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(rendimientoPendOBJ.getCuentaInversion(), cveIniProcTraspRend,
                    cveTipoCuentaInversion,
                    String.format(obsIniProcTraspRend, rendimientoPendOBJ.getCuentaPadre(), formatoImporte.format(rendimientoPendOBJ.getMonto())),
                    0, idProcesoBit);
            idProcesoBit = bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            //Recorrer cola de traspasos rendimientos
            PosGlobalCuentaOBJ posGlobCuentaObj = nucleoCentralDAO.obtenerCuentaPosicionGlobalByCuentaAh(rendimientoPendOBJ.getCuentaPadre());
            if (posGlobCuentaObj == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.CUENTA_NOT_FOUND)
                                + " | ID COLA REND: " + rendimientoPendOBJ.getId());
                //Saltamos al siguiente elemento de la lista
                return;
            }

            //Llenar objeto de transferencia
            TransferenciaCuentasOBJ obj = ahorroTransaccionService.getTransferenciaCuentasOBJ(
                    rendimientoPendOBJ.getCuentaInversion(), rendimientoPendOBJ.getCuentaPadre(), rendimientoPendOBJ.getMonto().doubleValue(),
                    0.0, 0.0, false, posGlobCuentaObj.isTienePlastico(), posGlobCuentaObj.getPersonaId(),
                    "AHORRO", "DEBITO");

            //Realizar deposito a la cuenta facil (admin-plasticos-services)
            respuesta = ahorroTransaccionService.procesaDepositoRendimientoCtaFacil(obj, rendimientoPendOBJ.getIdMov());
            if (respuesta.getCodigo() != 0) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        respuesta.getMensaje()
                                + " | ID COLA REND: " + rendimientoPendOBJ.getId());

                bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(rendimientoPendOBJ.getCuentaInversion(), cveTraspRend,
                        cveTipoCuentaInversion,
                        String.format("ERROR al realizar el deposito de rendimientos a la cuenta %s: %s|MONTO: %s|ID COLA REND: %s",
                                rendimientoPendOBJ.getCuentaInversion(), respuesta.getMensaje(), formatoImporte.format(obj.getMonto()),
                                rendimientoPendOBJ.getId()), respuesta.getCodigo(), idProcesoBit);
                bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

                //Saltamos al siguiente elemento de la lista
                return;
            }

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(rendimientoPendOBJ.getCuentaInversion(), cveTraspRend,
                    cveTipoCuentaInversion,
                    String.format(obsMontoBitacoraInv, rendimientoPendOBJ.getCuentaPadre(), formatoImporte.format(rendimientoPendOBJ.getMonto())),
                    0, idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            enviaSMSService.procesaSMSAbono(obj);

            ResultadoUtils.agregarRegistro(
                    resultadoList,
                    resultado,
                    "OK",
                    String.format(
                            "DEPÓSITO REALIZADO CON ÉXITO | ID DEPÓSITO: %s | ID COLA REND: %s",
                            StringUtils.isBlank(respuesta.getData()) ? "" : respuesta.getData(),
                            rendimientoPendOBJ.getId()
                    )
            );
        } catch (Exception e) {
            errorHandler.handleException(e);
            ResultadoUtils.agregarRegistro(resultadoList, resultado,
                    "ERROR EXCEPTION",
                    e.getMessage()
                            + " | ID COLA REND: " + rendimientoPendOBJ.getId());
        } finally {
            String mensaje = obsFinProcTraspRend + ": " + resultado.getMensaje();

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(rendimientoPendOBJ.getCuentaInversion(), cveFinProcTraspRend,
                    cveTipoCuentaInversion, mensaje, 0, idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            if (idProcesoBit != null && idProcesoBit > 0)
                bitacoraInversionesService.actualizaIdBitacoraInversionesAsync(idProcesoBit);
        }
    }

    private void procesaReinversionesPendientes(ReinversionPendOBJ reinversionPendOBJ, List<ResultadoProcesaRendimientos> resultadoList,
                                                ResultadoProcesaRendimientos resultado) {
        RespuestaDTO respuesta = new RespuestaDTO();
        Long idProcesoBit = 0L;
        BitacoraInversionesOBJ bitacoraOBJ = null;
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance(Locale.of("es", "MX"));
        try {
            log.info("=== Se procesa cuenta [{}] en proceso de reinversiones ===", reinversionPendOBJ.getCuentaInversion());

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(reinversionPendOBJ.getCuentaInversion(), cveIniProcReinv,
                    cveTipoCuentaInversion,
                    obsIniProcReinv,
                    0, idProcesoBit);
            idProcesoBit = bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            PosGlobalCuentaOBJ posGlobCuentaObj = nucleoCentralDAO.obtenerCuentaPosicionGlobalByCuentaAh(reinversionPendOBJ.getCuentaPadre());
            if (posGlobCuentaObj == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.CUENTA_NOT_FOUND)
                                + " | ID COLA REINV: " + reinversionPendOBJ.getId());
                //Saltamos al siguiente elemento de la lista
                return;
            }
            //Validar que no este presente en ahorro_no_reinvertir
            Boolean existe = inversionesDAO.existeNoReinvertirByCuenta(reinversionPendOBJ.getCuentaInversion());
            if (Boolean.TRUE.equals(existe)) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.REINVERSION_NO_ACTIVA)
                                + " | ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            //Obtener datos de la inversion
            DetalleInversionOBJ detalleInversionOBJ = inversionesDAO.obtenerDetalleInversionByCuenta(reinversionPendOBJ.getCuentaInversion());
            if (detalleInversionOBJ == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.DETALLE_INVERSION_NOT_FOUND)
                                + "| ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            //Obtener datos reinversion
            DatosReinversionOBJ datosReinversionOBJ = inversionesDAO.obtenerDatosReinversionByCuenta(reinversionPendOBJ.getCuentaInversion());
            if (datosReinversionOBJ == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.MODALIDAD_NOT_FOUND)
                                + "| ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            log.info("### Datos de la Reinversion:: {}", datosReinversionOBJ);

            ModalidadOBJ modalidadOBJ = inversionesDAO.obtenerModalidadByTipoId(detalleInversionOBJ.getTipoModalidadId());
            if (modalidadOBJ == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.MODALIDAD_NOT_FOUND)
                                + "| ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            if (!validarMontoRango(datosReinversionOBJ.getCapitalReinvertir(), modalidadOBJ.getMontoMin(), modalidadOBJ.getMontoMax())) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.RANGO_INVALIDO)
                                + "| ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            //Obtener saldo, evaluar el saldo vs el monto a reinvertir y mas validaciones
            respuesta = validaSaldo(reinversionPendOBJ.getCuentaPadre(), 0.0);
            if (respuesta.getCodigo() != 0) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        respuesta.getMensaje()
                                + " | ID COLA REINV: " + reinversionPendOBJ.getId());

                log.info("Se actualiza el estatus a C en ahorro_contrato de la cuenta {}", reinversionPendOBJ.getCuentaInversion());
                //update ahorro_contrato set estatus='C' where cuenta = p_cuenta;
                procreaDAO.actualizarEstatusAhorroContrato(reinversionPendOBJ.getCuentaInversion());
                log.info("Se actualiza el estatus a V en ahorro_rendimiento_vigente de la cuenta {}", reinversionPendOBJ.getCuentaInversion());
                // update ahorro_rendimiento_vigente set estatus= 'V' where rendimiento_vigente_id  = vid_ren;
                procreaDAO.actualizarEstatusAhorroRendimientoVigente(detalleInversionOBJ.getRendimientoVigenteId());
                return;
            }

            //Validar si la cuenta de inversion tiene saldo
            Double saldoReal = inversionesDAO.obtenerSaldoByCuenta(reinversionPendOBJ.getCuentaInversion());
            if (saldoReal != 0) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.RANGO_INVALIDO)
                                + " | ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            // update ahorro_rendimiento_vigente set estatus= 'V' where rendimiento_vigente_id  = vid_ren;
            procreaDAO.actualizarEstatusAhorroRendimientoVigente(detalleInversionOBJ.getRendimientoVigenteId());

            PlazoPorcentajeOBJ plazoPorcentajeOBJ = obtenerPlazoPorcentaje(detalleInversionOBJ.getPlazo(), detalleInversionOBJ.getTipoModalidadId());
            if (plazoPorcentajeOBJ == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.RENDIMIENTO_NOT_FOUND)
                                + " | ID COLA REINV: " + reinversionPendOBJ.getId());
                return;
            }

            //Insertamos el registro en ahorro_no_reinvertir
            inversionesDAO.insertaAhorroNoReinvertirByCuenta(detalleInversionOBJ.getCuentaInversion());
            //registrar bitacora
            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(reinversionPendOBJ.getCuentaInversion(), cveAltaNoReinv,
                    cveTipoCuentaInversion, String.format(obsAltaNoReinvBitacoraInv, detalleInversionOBJ.getCuentaPadre(), "INSERT desde procesa-rendimientos-cero"), 0, idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            // Actualizamos el campo activo a false en ahorro_datos_reinversion
            inversionesDAO.actualizarAhorroDatosReinversionByCuenta(detalleInversionOBJ.getCuentaInversion());

            TransferenciaCuentasOBJ obj = ahorroTransaccionService.getTransferenciaCuentasOBJ(
                    detalleInversionOBJ.getCuentaPadre(), detalleInversionOBJ.getCuentaInversion(), datosReinversionOBJ.getCapitalReinvertir().doubleValue(),
                    0.0, 0.0, posGlobCuentaObj.isTienePlastico(), false, posGlobCuentaObj.getPersonaId(),
                    "DEBITO", "AHORRO");

            //select count(cuenta) into vdisposicion from ahorro_rendimiento_vigente where cuenta = pp_cuenta and estatus ='V';
            Integer vdisposicion = inversionesDAO.obtenerInversionesVencidasByCuenaPadre(detalleInversionOBJ.getCuentaInversion());
            //vdisposicion := vdisposicion+1;
            vdisposicion += 1;
            //v_fecfinal := dia_habil(pp_feccalc+vplazo_nuevo,'D');
            //LocalDate v_fecfinal = dia_habil(reinversionPendOBJ.getFechaCalc(), plazoPorcentajeOBJ.getPlazo(), 'D');
            LocalDate fechaCalc = reinversionPendOBJ.getFechaCalc(); // ya es LocalDate
            int plazo = plazoPorcentajeOBJ.getPlazo(); // por ejemplo: 5
            // Sumar días
            LocalDate fechaBase = fechaCalc.plusDays(plazo);
            // Llamar al DAO para obtener día hábil
            LocalDate v_fecfinal = inversionesDAO.obtenerDiaHabil(fechaBase, "D");
            //Registrar en ahorro_rendimiento_vigente
            inversionesDAO.insertaAhorroRendimientoVigente(reinversionPendOBJ.getCuentaInversion(), plazo, plazoPorcentajeOBJ.getPorcentaje(),
                    plazoPorcentajeOBJ.getRendimientoId(), vdisposicion, datosReinversionOBJ.getCapitalReinvertir().doubleValue(), fechaCalc, v_fecfinal,
                    detalleInversionOBJ.getRendimientoVigenteId(), plazoPorcentajeOBJ.getTasaId());

            //update ahorro_rendimiento_vigente set interes = ahorro_int_aplazo_proyecta_constancia(cuenta) where estatus ='A' and cuenta = pp_cuenta;
            procreaDAO.actualizarInteresAhorroRendimientoVigente(reinversionPendOBJ.getCuentaInversion());
            //update ahorro_saldos set fecha_deposito=pp_fecha, fecha_corte=pp_feccalc,intereses=0,iva=0,isr=0,retenciones=0,desviacion=0,dias=0,ide=0 where cuenta= pp_cuenta;
            movimientosCajaDAO.actualizarAhorroSaldos(reinversionPendOBJ.getCuentaInversion(), reinversionPendOBJ.getFechaPlazo(), reinversionPendOBJ.getFechaCalc());

            //Realizar retiro a la cuenta facil (admin-plasticos-services)
            //se sustiyuye perform caja_depositoahorro(cast(1 as int2), pp_fecha, 9, vvcta_destino, v_mtoreinversion, 0, null, '', '', 27, null, null, null, null) ;
            //Realizar deposito a la cuenta de inversion
            //perform caja_depositoahorro(cast(1 as int2), pp_fecha, 9, pp_cuenta, v_mtoreinversion, 0, null, '', '', 26, null, null, null, null) ;
            respuesta = ahorroTransaccionService.procesaRetiroReinversionCtaFacil(obj);
            if (respuesta.getCodigo() != 0) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        respuesta.getMensaje()
                                + " | ID COLA REINV: " + reinversionPendOBJ.getId());

                bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(reinversionPendOBJ.getCuentaInversion(), cveTraspCapitalReinv,
                        cveTipoCuentaInversion,
                        String.format("ERROR al realizar el deposito de reinversion a la cuenta %s: %s|MONTO: %s|ID COLA REINV: %s",
                                reinversionPendOBJ.getCuentaInversion(), respuesta.getMensaje(), formatoImporte.format(obj.getMonto()),
                                reinversionPendOBJ.getId()), respuesta.getCodigo(), idProcesoBit);
                bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

                //Saltamos al siguiente elemento de la lista
                return;
            }

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(reinversionPendOBJ.getCuentaInversion(), cveTraspCapitalReinv,
                    cveTipoCuentaInversion,
                    String.format(obsMontoBitacoraInv, reinversionPendOBJ.getCuentaPadre(), formatoImporte.format(obj.getMonto())),
                    0, idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            enviaSMSService.procesaSMSCargo(obj);

            //update ahorro_avisos_retiro set estatus='X' where estatus<>'E' and cuenta = pp_cuenta and fecha_retiro <=pp_fecha;
            movimientosCajaDAO.actualizarAvisosDeRetiro(reinversionPendOBJ.getCuentaInversion(), reinversionPendOBJ.getFechaPlazo());

            //Insertar o actualizar el titulo de la inversion
            procesarConceptoCuenta(reinversionPendOBJ.getCuentaInversion(), datosReinversionOBJ.getTituloReinversion(), reinversionPendOBJ.getCuentaPadre(), idProcesoBit);
            //Registrar edo cuenta si es final de mes
            procesarEdoCuenta(reinversionPendOBJ.getCuentaInversion(), reinversionPendOBJ.getFechaCalc(),
                    reinversionPendOBJ.getFechaPlazo(), datosReinversionOBJ.getCapitalReinvertir());

            ResultadoUtils.agregarRegistro(
                    resultadoList,
                    resultado,
                    "OK",
                    String.format(
                            "REINVERSIÓN REALIZADA CON ÉXITO | ID DEPÓSITO: %s | ID COLA REINV: %s",
                            StringUtils.isBlank(respuesta.getData()) ? "" : respuesta.getData(),
                            reinversionPendOBJ.getId()
                    )
            );
        } catch (Exception e) {
            errorHandler.handleException(e);
            ResultadoUtils.agregarRegistro(resultadoList, resultado,
                    "ERROR EXCEPTION",
                    e.getMessage()
                            + " | ID COLA REINV: " + reinversionPendOBJ.getId());
        } finally {
            String mensaje = obsFinProcReinv + ": " + resultado.getMensaje();

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(reinversionPendOBJ.getCuentaInversion(), cveFinProcReinv,
                    cveTipoCuentaInversion, mensaje, 0, idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

            if (idProcesoBit != null && idProcesoBit > 0)
                bitacoraInversionesService.actualizaIdBitacoraInversionesAsync(idProcesoBit);
        }
    }

    private void procesaCancelacionInversion(DetalleInversionOBJ inversionActiva, List<ResultadoProcesaRendimientos> resultadoList,
                                             ResultadoProcesaRendimientos resultado) {
        try {
            log.info("=== Se procesa cuenta [{}] en proceso de cancelacion ===", inversionActiva.getCuentaInversion());
            //valida si se llego a la fecha final
            LocalDate hoy = LocalDate.now();
            LocalDate fechaFinLD = fechaUtils.convertirADateLocal(inversionActiva.getFechaFinD());
            log.info("Fecha final plazo:: {}", inversionActiva.getFechaFinD());
            log.info("Fecha de hoy:: {}", hoy);
            if (!fechaFinLD.isEqual(hoy)) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.getMensajeError(ErroresInversiones.NOT_FECHA_FIN, inversionActiva.getCuentaInversion()));
                return;
            }
            // La fecha fin es hoy
            log.info("Hoy es la fecha de vencimiento de {}", inversionActiva.getCuentaInversion());

            //Valida que la cuenta no tenga saldo
            Double saldo = inversionesDAO.obtenerSaldoByCuenta(inversionActiva.getCuentaInversion());
            if (saldo == null) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.getMensajeError(ErroresInversiones.SALDO_NOT_FOUND, inversionActiva.getCuentaInversion()));
                return;
            }
            if (saldo > 0) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.getMensajeError(ErroresInversiones.CUENTA_CON_SALDO, inversionActiva.getCuentaInversion()));
                return;
            }

            //Validar que no este presente en ahorro_no_reinvertir
            Boolean existe = inversionesDAO.existeNoReinvertirByCuenta(inversionActiva.getCuentaInversion());
            if (!Boolean.TRUE.equals(existe)) {
                ResultadoUtils.agregarRegistro(resultadoList, resultado,
                        "ERROR",
                        ErroresInversiones.descError.get(ErroresInversiones.REINVERSION_ACTIVA));
                return;
            }

            log.info("Se actualiza el estatus a C en ahorro_contrato de la cuenta {}", inversionActiva.getCuentaInversion());
            //update ahorro_contrato set estatus='C' where cuenta = p_cuenta;
            procreaDAO.actualizarEstatusAhorroContrato(inversionActiva.getCuentaInversion());
            log.info("Se actualiza el estatus a V en ahorro_rendimiento_vigente de la cuenta {}", inversionActiva.getCuentaInversion());
            // update ahorro_rendimiento_vigente set estatus= 'V' where rendimiento_vigente_id  = vid_ren;
            procreaDAO.actualizarEstatusAhorroRendimientoVigente(inversionActiva.getRendimientoVigenteId());

            ResultadoUtils.agregarRegistro(
                    resultadoList,
                    resultado,
                    "OK",
                    String.format(
                            "CANCELACION REALIZADA CON ÉXITO | CUENTA DE INVERSION: %s | ID REND VIG: %s",
                            inversionActiva.getCuentaInversion(),
                            inversionActiva.getRendimientoVigenteId()
                    )
            );
        } catch (Exception e) {
            errorHandler.handleException(e);
            ResultadoUtils.agregarRegistro(resultadoList, resultado,
                    "ERROR EXCEPTION",
                    e.getMessage());
        }
    }

    public void procesarEdoCuenta(String cuenta, LocalDate fechaCalc, LocalDate fechaPlazo, BigDecimal monto) {
        LocalDate ultimoDia = ultimoDiaDelMes(fechaCalc);

        if (fechaCalc.equals(ultimoDia)) {
            inversionesDAO.insertarEdoCuenta(cuenta, fechaCalc, fechaPlazo, monto);
        }
    }

    private RespuestaDTO procesarConceptoCuenta(String cuentaInversion, String tituloInv, String cuentaPadre, Long idProcesoBit) {
        RespuestaDTO respuesta;
        BitacoraInversionesOBJ bitacoraOBJ = null;
        ConceptoCuentaOBJ conceptoCuentaOBJ = conceptosCuentaDAO.obtenerConceptoCuentaProcreaByClave(NOM_INVERSION, cuentaInversion);
        if (conceptoCuentaOBJ != null) {
            respuesta = actualizarConceptoCuenta(tituloInv, NOM_INVERSION, cuentaInversion, conceptoCuentaOBJ.getId());
            if (respuesta.getCodigo() != 0) {
                bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(cuentaInversion, cveUpdateNombreInversion,
                        cveTipoCuentaInversion,
                        respuesta.getMensaje() + "UPDATE desde procesa-rendimientos-cero",
                        respuesta.getCodigo(), idProcesoBit);
                bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

                return respuesta;
            }

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(cuentaInversion, cveUpdateNombreInversion,
                    cveTipoCuentaInversion,
                    String.format(obsNombreBitacoraInv, cuentaPadre, tituloInv, "UPDATE desde procesa-rendimientos-cero"),
                    respuesta.getCodigo(), idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);
        } else {
            respuesta = insertarConceptoCuenta(tituloInv, NOM_INVERSION, cuentaInversion);
            if (respuesta.getCodigo() != 0) {
                bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(cuentaInversion, cveUpdateNombreInversion,
                        cveTipoCuentaInversion,
                        respuesta.getMensaje() + "|OBS: procesa-rendimientos-cero",
                        respuesta.getCodigo(), idProcesoBit);
                bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);

                return respuesta;
            }

            bitacoraOBJ = bitacoraInversionesService.getBitacoraInversionesOBJ(cuentaInversion, cveAltaNombreInversion,
                    cveTipoCuentaInversion,
                    String.format(obsNombreBitacoraInv, cuentaPadre, tituloInv, "|OBS: procesa-rendimientos-cero"),
                    respuesta.getCodigo(), idProcesoBit);
            bitacoraInversionesService.registraBitacoraInversiones(bitacoraOBJ);
        }
        return respuesta;
    }

    private RespuestaDTO insertarConceptoCuenta(String valor, String clave, String cuenta) {
        log.info("Se registra concepto {} con valor {} para la cuenta {}", clave, valor, cuenta);
        return conceptosCuentaDAO.guardarConceptoCuentaProcrea(clave, valor, cuenta);
    }

    private RespuestaDTO actualizarConceptoCuenta(String valor, String clave, String cuenta, Integer idConcepto) {
        log.info("Se actualiza concepto {} con valor {} para la cuenta {}", clave, valor, cuenta);
        return conceptosCuentaDAO.actualizarConceptoCuentaProcreaById(idConcepto, valor);
    }

    private RespuestaDTO validaSaldo(String cuentaah, Double monto) {
        RespuestaDTO respuesta = new RespuestaDTO();
        try {
            respuesta = msConsultaSaldo.consultarSaldo(cuentaah);
            if (respuesta.getCodigo() == 0) {
                ConsultaSaldoResponse saldoObj = gson.fromJson(respuesta.getData(), ConsultaSaldoResponse.class);
                if (saldoObj.getSaldo() >= monto) {
                    RespuestaUtils.respuestaExito(respuesta);
                } else {
                    RespuestaUtils.asignarError(respuesta,
                            ErroresInversiones.SALDO_INSUFICIENTE,
                            ErroresInversiones.descError.get(ErroresInversiones.SALDO_INSUFICIENTE));
                }
            } else if (respuesta.getCodigo() == ErroresGenerales.ERROR_WS) {
                RespuestaUtils.asignarError(respuesta,
                        ErroresInversiones.ERROR_CONSULTA_SALDO,
                        ErroresInversiones.descError.get(ErroresInversiones.ERROR_CONSULTA_SALDO));
            }
        } catch (Exception e) {
            errorHandler.handleException(e);
            RespuestaUtils.asignarError(respuesta,
                    ErroresGenerales.ERROR_INTERNO,
                    ErroresGenerales.descError.get(ErroresGenerales.ERROR_INTERNO));
        }
        return respuesta;
    }

    public static boolean validarMontoRango(BigDecimal monto, Double montoMin, Double montoMax) {
        BigDecimal min = BigDecimal.valueOf(montoMin);
        return monto.compareTo(min) >= 0;
    }

    private void registrarBitacoraInversiones(String cuenta, String claveEvento, String mensaje, Integer codigo, Long idProcesoBit) {
        BitacoraInversionesOBJ bitacora = bitacoraInversionesService.getBitacoraInversionesOBJ(
                cuenta, claveEvento, cveTipoCuentaInversion, mensaje, codigo, idProcesoBit);
        bitacoraInversionesService.registraBitacoraInversionesAsync(bitacora);
    }

    private PlazoPorcentajeOBJ obtenerPlazoPorcentaje(Integer plazo, Integer tipoModalidadId) {
        return inversionesDAO.obtenerRendimientoIdByPlazo(plazo, tipoModalidadId);
    }

    public LocalDate ultimoDiaDelMes(LocalDate fecha) {
        return fecha.withDayOfMonth(fecha.lengthOfMonth());
    }

    private String firmar(String traceId) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY_UUID.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] firma = hmac.doFinal(traceId.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(firma);
        } catch (Exception e) {
            throw new RuntimeException("Error al firmar el TraceId", e);
        }
    }
}
