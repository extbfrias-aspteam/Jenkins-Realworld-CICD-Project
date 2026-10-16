package net.cero.ahorro.spei.enviospei;


import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import net.cero.ahorro.spei.enviospei.nuevospeicero.CEnviaSpei;
import net.cero.ahorro.spei.enviospei.servicioscero.ClaveValorWS;
import net.cero.ahorro.spei.enviospei.servicioscero.ServiciosCeroWS;
import net.cero.ahorro.spei.enviospei.servicioscero.ServiciosWS;
import net.cero.model.CuentaAspOBJ;
import net.cero.data.Respuesta;
import net.cero.model.MovimientoSpeiOBJ;
import net.cero.model.UsuarioOBJ;
import net.cero.data.nuevospei.SolicitanteOBJ;
import net.cero.req.general.HeaderWS;
import net.cero.req.transferenciaspei.TransferenciaSpeiReq;
import net.cero.res.spei.TransferenciaSPEIResponse;
import net.cero.spring.config.Apps;
import net.cero.spring.dao.ConsultaMovimientosSpeiDAO;
import net.cero.spring.dao.CuentaAspDAO;
import net.cero.spring.dao.UsuarioDAO;
import net.cero.ws.data.RespuestaSVC;
import net.cero.ws.data.ToolsR;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que modela todo el comportamiento para realizar una operacion SPEI junto con la obteneción de todos los campos
 * necesarios para su ejecución
 * @author AASTORGA
 */
@Service
@Log4j2
public class NuevoSpeiOutService {
    private final ServiciosCeroWS serviciosCeroWS;
    private final ServiciosWS serviciosWS;
    private final ClaveValorWS claveValorWS;
    private final CuentaAspDAO cuentaAspDao;

    private final UsuarioDAO usuarioDAO;
    private final ConsultaMovimientosSpeiDAO consultaMovimientosSpeiDAO;

    private static final Gson gson = ToolsR.GBuilder();

    public NuevoSpeiOutService(ServiciosCeroWS serviciosCeroWS,
                               ServiciosWS serviciosWS,
                               ClaveValorWS claveValorWS,
                               UsuarioDAO usuarioDAO,
                               ConsultaMovimientosSpeiDAO consultaMovimientosSpeiDAO) {
        this.serviciosCeroWS = serviciosCeroWS;
        this.serviciosWS = serviciosWS;
        this.claveValorWS = claveValorWS;
        this.usuarioDAO = usuarioDAO;
        this.consultaMovimientosSpeiDAO = consultaMovimientosSpeiDAO;

        ApplicationContext context = Apps.getInstance().getApplicationContext();
        this.cuentaAspDao = context.getBean("CuentaAspDao", CuentaAspDAO.class);
    }

    /**
     * Metodo principal para realizar la operacion del SPEI
     * @param request Objeto con los datos necesarios para realizar la operacion
     * @return Objeto Respuesta con el resultaod de la operacion
     */
    public Respuesta procesarEnvioSpei(TransferenciaSpeiReq request)
    {
        Respuesta respuesta = new Respuesta();
        try{
            log.info("Inicia procesamiento de SPEI OUT: {}",request);
            Double montoDepositar = request.getMonto();
            String clabeSpeiDestino = request.getCuentaBeneficiaria();
            String bancoSpeiDestino = "";
            String bancoDescSpeiDestino = "";
            Long usuarioId = request.getHeader().getIdUsuario();

            CuentaAspOBJ cuentaOrigen = null;
            cuentaOrigen = cuentaAspDao.buscaCuentaPorCuentaOCLABE(request.getCuentaOrigen());

            if(cuentaOrigen == null)
            {
                respuesta.setMensaje("La cuenta origen no existe, no es posible realizar la operación.");
                respuesta.setCodigo(6);
                return respuesta;
            }

            if(cuentaOrigen.getEstatusId() != 1)
            {
                respuesta.setMensaje(String.format("La cuenta no se encuentra activa. Su estado actual es %s"
                        ,cuentaOrigen.getEstatusDescripcion()));
                respuesta.setCodigo(8);
                return respuesta;
            }

            UsuarioOBJ usuario = usuarioDAO.consultaUsuarioPorId((int)request.getHeader().getIdUsuario());
            if(usuario == null)
            {
                respuesta.setMensaje("El usuario proporcionado no existe. Favor de verificarlo.");
                respuesta.setCodigo(7);
                return respuesta;
            }

            HeaderWS header = new HeaderWS();
            header.setIdUsuario(usuarioId.longValue());
            header.setIdSucursal(1L);

            RespuestaSVC respClabe = serviciosWS.ValidaClabeSpeiDest(clabeSpeiDestino);
            if (respClabe.getErrores().getCodigoError() != 0) {
                respuesta.setMensaje(respClabe.getErrores().getDescError());
                respuesta.setCodigo(Integer.parseInt(String.valueOf(respClabe.getErrores().getCodigoError())));
                return respuesta;
            }
            bancoSpeiDestino = ToolsR._T(respClabe.getBody().getValor("ID_B"));
            bancoDescSpeiDestino = ToolsR._T(respClabe.getBody().getValor("NOMBRE_B"));
            String institucionSpeiDestino = ToolsR._T(respClabe.getBody().getValor("ID_INST"));


            RespuestaSVC respRef = serviciosWS.obtenerReferenciaSpei();
            if (respRef.getErrores().getCodigoError() != 0) {
                respuesta.setMensaje(respRef.getErrores().getDescError());
                respuesta.setCodigo(Integer.parseInt(String.valueOf(respRef.getErrores().getCodigoError())));
                return respuesta;
            }
            RespuestaSVC respAut = serviciosWS.obtenerAutorizacionSpei(header, cuentaOrigen.getProductoAhorroId(),
                    montoDepositar);
            if (respAut.getErrores().getCodigoError() != 0) {
                respuesta.setMensaje(respAut.getErrores().getDescError());
                respuesta.setCodigo(Integer.parseInt(String.valueOf(respAut.getErrores().getCodigoError())));
                return respuesta;
            }
            RespuestaSVC RespApp = claveValorWS.getValorCatalogoWS("ASP_CTRL","claveValorTipoAplicacion");

            SolicitanteOBJ solicitante = serviciosCeroWS.cargarDatosSolicitante(cuentaOrigen.getPersonaId());
            String t_nombreCompleto = solicitante.getNombreCompleto().length() > 40
                    ? solicitante.getNombreCompleto().substring(0, 40) : solicitante.getNombreCompleto();


            Calendar fechaCaptura = Calendar.getInstance();
            Map<String, Object> pojoSpei = new HashMap<>();
            pojoSpei.put("NombreOrd", t_nombreCompleto);
            pojoSpei.put("IdTipoCuentaOrd", "40");
            pojoSpei.put("CuentaOrd", cuentaOrigen.getClabeInterbancaria());
            pojoSpei.put("RfcOrd", solicitante.getRfc());
            pojoSpei.put("CorreoOrd", solicitante.getCorreo());
            pojoSpei.put("IdTipoCuentaBenef", "40");
            pojoSpei.put("NombreBenef", request.getNombreBenefSpeiDestino());
            pojoSpei.put("CuentaBenef", clabeSpeiDestino);
            pojoSpei.put("RfcBenef", request.getRfcBenefSpeiDestino());
            pojoSpei.put("CorreoBenef", request.getCorreoBenefSpeiDestino());
            pojoSpei.put("IdInstitucionBen", institucionSpeiDestino);
            pojoSpei.put("Monto", montoDepositar);
            pojoSpei.put("Iva", 0D);
            pojoSpei.put("ReferenciaNumerica", ToolsR._I(respRef.getBody().getValor("REF_SPEI")));
            pojoSpei.put("IdTipoPago", "1");
            pojoSpei.put("StatusOperacion",
                    ToolsR._T(respAut.getBody().getValor("AUT_SPEI")).substring(0, 1).equals("1") ? 0 : 88);
            pojoSpei.put("FechaCaptura", fechaCaptura.getTime());
            pojoSpei.put("UsuarioId", usuarioId);
            pojoSpei.put("TipoOperacion", 1);
            pojoSpei.put("IdOperacion", 0);
            pojoSpei.put("App", ToolsR._I(RespApp.getBody().getValor("ID")));
            pojoSpei.put("ConceptoPago", request.getConceptoPago());
            pojoSpei.put("EnvioAutomatico", ToolsR._T(respAut.getBody().getValor("AUT_SPEI")).substring(0, 1));

            CEnviaSpei cEnviaSpei = new CEnviaSpei();

            //VERIFICA SALDO FINAL ANTES DE ENVIAR SPEI
            log.info("Consulta de saldo de la cuenta para validar si tiene saldo disponible: {}",cuentaOrigen.getCuenta());
            Double saldoDisponible = serviciosCeroWS.metodoConsultaSaldoCuenta(cuentaOrigen.getCuenta());

            if (montoDepositar > saldoDisponible) {
                respuesta.setMensaje("Saldo insuficiente para la transferencia SPEI.");
                respuesta.setCodigo(5);
                return respuesta;
            }
            Long bancoId = 659L;

            log.info("Inicia proceso de registro de movimiento SPEI.");
            cEnviaSpei.procesa(pojoSpei);
            log.info("Finaliza proceso de registro de movimiento SPEI: {},{}",cEnviaSpei.getId(),cEnviaSpei.getDescr());
            if (cEnviaSpei.getId() != 0) {
                respuesta.setMensaje(cEnviaSpei.getDescr());
                respuesta.setCodigo(-1);
                return respuesta;
            } else {
                Double montoRetirar = montoDepositar;
                String cve_rastreo = "";
                try {
                    cve_rastreo = cEnviaSpei.getDescr().split(",")[1];
                } catch (Exception e) {
                }
                Map<String, Object> datos = new HashMap<>();
                datos.put("NOMBRE_BANCO", bancoDescSpeiDestino);
                datos.put("NOMBRE_SOL", solicitante.getNombreCompleto());
                datos.put("REFERENCIA", ToolsR._I(respRef.getBody().getValor("REF_SPEI")));
                datos.put("CVE_RASTREO", cve_rastreo);

                log.info("====== DATOS DE LA OPERACION ANTES DE BUSCAR EL MOVIMIENTO============cuentaOrigen.getClabeInterbancaria():" +
                                " {},clabeSpeiDestino: {},montoDepositar: {},fechaCaptura.getTime():{}",
                        cuentaOrigen.getClabeInterbancaria(),clabeSpeiDestino,montoDepositar,fechaCaptura.getTime());
                MovimientoSpeiOBJ ultimoMovimiento = consultaMovimientosSpeiDAO
                        .consultarUltimoMovimiento(cuentaOrigen.getClabeInterbancaria(),clabeSpeiDestino,montoDepositar);
                log.info("ultimoMovimiento: {}",ultimoMovimiento);
                log.info("Inicia proceso de retiro de la cuenta.");
                String claveRastreo = (ultimoMovimiento != null ? ultimoMovimiento.getClaveRastreo() : "");
                Integer idSpei = (ultimoMovimiento != null ? ultimoMovimiento.getIdSpei() : 0);
                serviciosCeroWS.procesoRetirar("SPEI", datos,bancoDescSpeiDestino
                        ,request.getNombreBenefSpeiDestino(),clabeSpeiDestino,montoRetirar,cuentaOrigen.getCuenta()
                        ,request.getHeader(),bancoId,claveRastreo
                        ,idSpei,"ASPRT");
                log.info("Finaliza proceso de retiro de la cuenta.");


                TransferenciaSPEIResponse response = new TransferenciaSPEIResponse();
                if(ultimoMovimiento != null)
                {
                    response.setClaveRastreo(ultimoMovimiento.getClaveRastreo());

                    response.setBancoOrigen(ultimoMovimiento.getBancoOrigen().trim());
                    response.setCuentaOrigen(ultimoMovimiento.getCuentaOrigen());
                    response.setClaveRastreo(ultimoMovimiento.getClaveRastreo());
                    response.setCuentaBeneficiario(ultimoMovimiento.getCuentaBeneficiario());
                    response.setBancoDestino(ultimoMovimiento.getBancoDestino().trim());
                    response.setFechaOperacion(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ultimoMovimiento
                            .getFechaOperacion().toLocalDateTime()));
                    response.setMonto(ultimoMovimiento.getMonto());
                }
                respuesta.setCodigo(0);
                respuesta.setMensaje("Operación realizada con éxito");
                respuesta.setData(gson.toJson(response));
            }
        }
        catch(Exception e)
        {
            log.error("Ocurrio un error al procesar la petición del spei OUT.");
            respuesta.setCodigo(-1);
            respuesta.setMensaje("Ocurrio un error al procesar la petición del spei OUT.");
        }
        finally{
            log.info("Finaliza procesamiento de SPEI OUT: {}",request);
        }

        return respuesta;
    }
}
