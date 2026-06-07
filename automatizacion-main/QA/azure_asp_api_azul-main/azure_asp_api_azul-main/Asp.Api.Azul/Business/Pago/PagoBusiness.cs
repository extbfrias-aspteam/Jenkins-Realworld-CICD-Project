using Asp.Api.Azul.Core.Commons.Constants;
using Asp.Api.Azul.Core.Commons.Enums;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Entities;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Services.MonitorPlus;
using Asp.Api.Azul.Infrastructure.Utils;
using Asp.Api.Azul.Kafka.Dtos.SpeiOut;
using Asp.Api.Azul.Kafka.Dtos.Traspasos;
using Asp.Api.Azul.Kafka.Producer;
using Asp.Api.Azul.Kafka.Topics;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Repositorys.CuentaRepository;
using Asp.Api.Azul.Repositorys.PagoRepository;
using Asp.Api.Azul.Repositorys.SaldoPbluRepository;
using Asp.Api.Azul.Repositorys.UdnRepository;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Traspasos.Core.domain.dtos;
using Asp.Api.Azul.Traspasos.Core.domain.enums;
using Asp.Cifrado.Services;
using Confluent.Kafka;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Org.BouncyCastle.Asn1.Ocsp;
using Org.BouncyCastle.Asn1.X500;
using System;
using System.ComponentModel.DataAnnotations;
using System.Globalization;
using System.IO.Compression;
using System.Numerics;
using System.Runtime.ConstrainedExecution;
using System.Runtime.Intrinsics.X86;
using System.Text;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Text.RegularExpressions;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Asp.Api.Azul.Business.Pago
{
    public class PagoBusiness : IPagoBusiness
    {
        private readonly IPagoRepository _pagoRepository;
        private readonly ICuentaRepository _cuentaRepository;
        private readonly IUdnRepository _udnRepository;
        private readonly ISaldoPbluRepository _saldoPbluRepository;
        private readonly IEncriptionService _encriptionService;
        private readonly IAspLogservice _aspLogservice;
        private readonly LoggerHelper _logger;
        private readonly KafkaProducerService _kafkaProducer;
        private readonly IMonitorPlusService _monitorPlusService;
        private readonly IConfigurationService _configurationService;
        private readonly IConfiguration _configuration;
        public PagoBusiness(IPagoRepository pagoRepository, ICuentaRepository cuentaRepository, IUdnRepository udnRepository, ISaldoPbluRepository saldoPbluRepository, IEncriptionService encriptionService, LoggerHelper logger, IAspLogservice aspLogservice, KafkaProducerService kafkaProducer, IMonitorPlusService monitorPlusService, IConfigurationService configurationService,IConfiguration configuration)

        {
            _pagoRepository = pagoRepository;
            _cuentaRepository = cuentaRepository;
            _udnRepository = udnRepository;
            _saldoPbluRepository = saldoPbluRepository;
            _encriptionService = encriptionService;
            _logger = logger;
            _aspLogservice = aspLogservice;
            _kafkaProducer = kafkaProducer;
            _monitorPlusService = monitorPlusService;
            _configurationService = configurationService;
                        _configuration = configuration;

        }

        #region Spei Out

        /// <summary>
        /// 
        /// </summary>
        /// <param name="jsonPago"></param>
        /// <param name="idPblu"></param>
        /// <param name="firma"></param>
        /// <param name="estado"></param>
        /// <param name="llave"></param>
        /// <returns>Tuple<string,string> donde Item1: CveRastreo y Item2: JsonPagoAsp</returns>
        /// <exception cref="ErrorPagoExistente"></exception>
        /// <exception cref="ErrorPeticionMalformada"></exception>
        /// <exception cref="ErrorConsultaCuenta"></exception>
        /// <exception cref="ErrorUdnNoExiste"></exception>
        /// <exception cref="ErrorClabeSinUDN"></exception>
        /// <exception cref="ErrorSaldoInsuficiente"></exception>
        /// <exception cref="ErrorClaveRastreoNoExiste"></exception>
        public async Task<DtoPagoValidado> ValidaPago(OrdenPagoDto ordenPago, string jsonPago, int idPblu, string firma, int estado, int llave, bool isPortal = false, string IDENTIFICADOR = "")
        {
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"Inicio metodo ValidaPago, clave de rastreo {ordenPago.CveRastreo}");
            string proveedor = "SIES";
            string correoPblu = GetEmailByIdPblu(idPblu);
            Entities.DataBase.Pago pago = new Entities.DataBase.Pago();
            bool toAsp = false;

            if (ordenPago.CtaDestino == ordenPago.Clabe)
                throw new ErrorPeticionMalformada("La cuenta CLABE origen y destino no deben ser iguales.");

            //Validamos si el concepto de pago es correcto
            if (!ValidarConcepto(ordenPago.ConceptoPago))
                throw new ErrorPeticionMalformada("El pago no cuenta con concepto o contiene caracteres no permitidos");

            //Validamos si la referencia númerica es correcta
            if (!ValidarReferenciaNumerica(ordenPago.RefNum))
                throw new ErrorPeticionMalformada("El campo RefNum debe ser numerico y longitud máxima de 7 caracteres");

            //Validamos si el nombre destino es correcto
            if (!ValidarNombreDestino(ordenPago.NombreDestino))
                throw new ErrorPeticionMalformada($"El campo nombreDestino no cumple con el formato: {ordenPago.NombreDestino}");



            //print
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"GetValidacionPago-> Inicia validación de pagos");
            ValidaPago _validaciones = await _pagoRepository.GetValidacionPago(idPblu, ordenPago.CveRastreo, int.Parse(ordenPago.BancoDestino), ordenPago.Clabe, isPortal, decimal.Parse(ordenPago.Monto), ordenPago.CtaDestino);
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"GetValidacionPago-> Finaliza validación de pagos");
            //print

            if (!_validaciones.Success)
            {
                throw new Exception(_validaciones.Mensaje);
            }

            proveedor = _validaciones.Proveedor;


            var nombreOrdenanteResult = _validaciones.NombreWithRFC;
            var nombreOrdenanteArray = nombreOrdenanteResult.Split(",");
            var nombreOrdenante = "";
            var rfcOrdenante = "";
            if (nombreOrdenanteArray.Length == 2)
            {
                nombreOrdenante = nombreOrdenanteArray[0];
                rfcOrdenante = nombreOrdenanteArray[1];
            }

            //print
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"GetTipoCuentaDestino-> Se obtiene el tipo cuenta destino");
            int idTipoCuentaDestino = GetTipoCuentaDestino(ordenPago.CtaDestino, int.Parse(ordenPago.BancoDestino));

            //Obtenemos el tipo de pago
            //print
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"GetTipoPago-> Se obtiene el tipo pago");
            int tipoPago = await GetTipoPago(int.Parse(ordenPago.BancoDestino), ordenPago.Clabe, ordenPago.CtaDestino, 1);
            if (tipoPago == 102) //Traspasos de Eiyu a ASP
            {
                tipoPago = 101;
                toAsp = true;
            }

            //print
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"GetTipoPago-> Se genera el objeto de pago");
            pago = ConvertOrdenPagoToPago(ordenPago, ordenPago.Clabe, firma);
            var fechaOperacion = DateTime.Today;
            if (fechaOperacion.DayOfWeek == DayOfWeek.Saturday)
            {
                fechaOperacion = fechaOperacion.AddDays(2);
            }
            else if (fechaOperacion.DayOfWeek == DayOfWeek.Sunday)
            {
                fechaOperacion = fechaOperacion.AddDays(1);
            }

            pago.FechaOperacion = fechaOperacion;
            pago.IdEstadoPago = estado;
            pago.IdTipoCuentaDestino = idTipoCuentaDestino;
            pago.IdTipoPago = tipoPago;




            pago.Json = jsonPago;
            pago.Llave = llave;
            pago.IdPblu = idPblu;
            pago.IdUdn = _validaciones.IdUdn;

            //Si es un pago EIYU y es un traspaso entra en esta condición
            if (toAsp == false && tipoPago == 101)
            {

                
                var uuid = Guid.NewGuid().ToString();
                pago.IdEstadoPago = 2;
                GenerarLog(IDENTIFICADOR, "ValidaPago", $"Inicia generación de Objecto de ABONO");
                var abono = GenerarAbono(pago, nombreOrdenante, IDENTIFICADOR, proveedor, tipoPago.ToString(), idTipoCuentaDestino.ToString(), uuid);
                GenerarLog(IDENTIFICADOR, "ValidaPago", $"Inicia insert del traspaso nuevo PL");
                var responseTraspaso = await _pagoRepository.InsertTraspaso(pago, nombreOrdenante, uuid);
                GenerarLog(IDENTIFICADOR, "ValidaPago", $"Finaliza insert del traspaso nuevo PL, respuesta: {JsonSerializer.Serialize(responseTraspaso)}");
                if (responseTraspaso?.Mensaje != "OK") throw new Exception("Error al registrar el traspaso. Consulte con el supervisor.");

                var cambioEstado = new DtoCambioEstado
                {
                    ClaveRastreo = pago.CveRastreo,
                    Estado = "2",
                    Causa = string.Empty,
                    Uuid = pago.Uuid??"UUID VACIO",
                    IdPblu = (int)pago.IdPblu
                };

                var jsonTraspasoEiyu = new DtoTraspasoEiyu
                {
                    IdPbluDestino = responseTraspaso.IdPbluDestino,
                    NoNotificarAbono = false,
                    Abono = abono,
                    CambioEstado = cambioEstado
                };

                var jsonAbono = JsonSerializer.Serialize(jsonTraspasoEiyu);
                /* var jsonAbono = JsonSerializer.Serialize(abono, new JsonSerializerOptions
                 {
                     PropertyNamingPolicy = JsonNamingPolicy.CamelCase
                 });*/

                var origenConfig = _configuration["ApiTraspasosCore:RequestOrigen"];
                var origen = origenConfig switch
                {
                    "Azure" => RequestOrigen.Azure,
                    "GCP" => RequestOrigen.GCP,
                    "PROCREA" => RequestOrigen.PROCREA,
                    "CERO" => RequestOrigen.CERO,
                    _ => RequestOrigen.Azure
                };

                GenerarLog(IDENTIFICADOR, "ValidaPago", $"RequestOri= {origen} or RequestOri= {origen.ToString()}");

                var jsonNotificarAbonoTraspaso = new NotificacionAbonoTraspasoDto
                {
                    IdPbluDestino = responseTraspaso.IdPbluDestino,
                    NoNotificarAbono = false,
                    Abono = abono,
                    RequestOri = origen
                };

                return new DtoPagoValidado
                {
                    CveRastreo = pago.CveRastreo,
                    PagoJson = { },
                    IdTipoPago = tipoPago,
                    TraspasoAsp = { },
                    Proveedor = proveedor,
                    toAsp = toAsp,
                    AbonoEiyu = jsonAbono,
                    CloudDestino = responseTraspaso.CloudDestino,
                    CloudOrigen = origen.ToString(),
                    TraspasosCore = jsonNotificarAbonoTraspaso,
                    CambioEstadoTraspaso = cambioEstado
                };


            }
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"InsertPagoPl-> Inicia insert del pago en base de datos");
            int respuesta = await _pagoRepository.InsertPagoPl(pago, pago.FolioPaquete ?? 0);
            GenerarLog(IDENTIFICADOR, "ValidaPago", $"InsertPagoPl-> Finaliza insert del pago en base de datos");
            if (respuesta == 2)
                throw new ErrorClaveRastreoNoExiste($"Error en la clave de rastreo, ya existe el pago o traspaso: {pago.CveRastreo} - {pago.FolioPaquete}");

            if (respuesta == 0)
                throw new Exception($"Error al registrar el pago");


            var traspaso = new DtoTraspasoAsp();
            string jsonPagoAsp = "";
            //print
            GenerarLog(IDENTIFICADOR, "ValidaPago", "Verificación tipoPago");
            if (tipoPago == 1)//Pago a terceros
            {

                if (proveedor == "ASP")
                {
                    GenerarLog(IDENTIFICADOR, "ValidaPago", "Inicia creación de objeto proveedor ASP");
                    //print
                    var pagoAsp = new
                    {
                        cuentaBeneficiario = pago.CuentaDestino ?? string.Empty,
                        nombreBeneficiario = pago.NombreDestino ?? string.Empty,
                        conceptoPago = pago.ConceptoPago ?? string.Empty,
                        nombreOrdenante = nombreOrdenante ?? string.Empty,
                        rfcBeneficiario = pago.RfcDestino ?? string.Empty,
                        referenciaNumerica = pago.RefNum ?? string.Empty,
                        bancoDestino = pago.IdBancoDestino.ToString(),
                        claveRastreo = pago.CveRastreo ?? string.Empty,
                        rfcOrdenante = "",
                        monto = pago.MontoCargo.ToString(),
                        tipoCuenta = pago.IdTipoCuentaDestino.ToString(),
                        cuentaOrdenante = pago.CuentaOrigen ?? string.Empty,
                        tipoPago = pago.IdTipoPago.ToString()
                    };
                    jsonPagoAsp = JsonSerializer.Serialize(pagoAsp);
                }
                else
                {
                    //print
                    GenerarLog(IDENTIFICADOR, "ValidaPago", "Inicia creación de objeto proveedor SIES");
                    pago.NombreDestino = pago.NombreDestino ?? string.Empty;
                    var nombreDestinoLength = pago.NombreDestino.Length;
                    pago.NombreDestino =
                        pago.NombreDestino.Substring(0, nombreDestinoLength <= 39 ? nombreDestinoLength : 39);


                    nombreOrdenante = nombreOrdenante ?? string.Empty;
                    var nombreOrdenanteLength = nombreOrdenante.Length;
                    nombreOrdenante =
                        nombreOrdenante.Substring(0, nombreOrdenanteLength <= 39 ? nombreOrdenanteLength : 39);

                    rfcOrdenante = rfcOrdenante ?? string.Empty;
                    var rfcOrdenanteLength = rfcOrdenante.Length;
                    rfcOrdenante =
                        rfcOrdenante.Substring(0, rfcOrdenanteLength <= 13 ? rfcOrdenanteLength : 13);

                    //print
                    GenerarLog(IDENTIFICADOR, "ValidaPago", "Llenado de información proveedor SIES");
                    var pagoSIES = new OrdenPagoSIES
                    {
                        CveEntidad = "ASPINTEGRA",
                        IdEmpresa = "1",
                        CuentaBeneficiario = LimpiaCaracteres(pago.CuentaDestino ?? string.Empty, false),
                        NombreBeneficiario = LimpiaCaracteres(pago.NombreDestino, true, true),
                        ConceptoPago = LimpiaCaracteres(pago.ConceptoPago ?? string.Empty, true),
                        NombreOrdenante = LimpiaCaracteres(nombreOrdenante, true, true),
                        RfcBeneficiario = LimpiaCaracteres(pago.RfcDestino ?? string.Empty, false),
                        ReferenciaNumerica = pago.RefNum ?? string.Empty,
                        IdInstitucionBen = pago.IdBancoDestino.ToString(),
                        IdInstitucionOrd = 90659.ToString(),
                        CveRastreo = pago.CveRastreo,
                        RfcOrdenante = LimpiaCaracteres(rfcOrdenante, false),
                        Monto = pago.MontoCargo.ToString() ?? string.Empty,
                        IdTipoCuentaBeneficiario = pago.IdTipoCuentaDestino.ToString(),
                        CuentaOrdenante = pago.CuentaOrigen ?? string.Empty,
                        IdTipoPago = pago.IdTipoPago.ToString(),
                        FechaCaptura = DateTimeFix.Now().ToString("yyyy-MM-dd HH:mm:ss"),
                        Envio = "1",
                        Reenvio = "1",
                        Verificado = "1",
                        IdTipoCuentaOrdenante = "40",
                        Iva = "0",
                        Topologia = "V",
                        Prioridad = "1",
                        IdAreaEmite = "10",
                        ReferenciaCobranza = DateTime.Today.ToString("ddMMyyyy")
                    };
                    var cadenaFirmar = GeneraCadenaFirmarSIES(pagoSIES);
                    Console.WriteLine($"Cadena Original: {cadenaFirmar}");
                    var firmaSies = _encriptionService.FirmarSIES(cadenaFirmar);
                    pagoSIES.FirmaCoreBancario = firmaSies;
                    jsonPagoAsp = JsonSerializer.Serialize(pagoSIES, new JsonSerializerOptions { DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull });
                    GenerarLog(IDENTIFICADOR, "ValidaPago", "Fin del llenado de información proveedor SIES");
                }
            }
            else//traspaso
            {
                //print
                GenerarLog(IDENTIFICADOR, "ValidaPago", "Inicia creación de objeto de un traspaso");
                //Generamos el abono que se notificara si el traspaso es exitoso
                proveedor = "ASP";
                var fechaOperativa = DateTime.Today;
                if (fechaOperativa.DayOfWeek == DayOfWeek.Saturday)
                {
                    fechaOperativa = fechaOperativa.AddDays(2);
                }
                else if (fechaOperativa.DayOfWeek == DayOfWeek.Sunday)
                {
                    fechaOperativa = fechaOperativa.AddDays(1);
                }
                var abono = new
                {
                    nombOrigen = nombreOrdenante, //
                    refNum = pago.RefNum, //
   
                };
                traspaso = new DtoTraspasoAsp
                {
                    CuentaOrdenante = pago.CuentaOrigen ?? string.Empty,
                    CuentaBeneficiario = pago.CuentaDestino ?? string.Empty,
                    Monto = pago.MontoCargo ?? 0m,
                    ConceptoPago = pago.ConceptoPago ?? string.Empty,
                    ClaveRastreo = pago.CveRastreo,
                    IdTipoPago = tipoPago,
                    FechaCaptura = DateTime.Now.ToString("dd/MM/yyyy HH:mm:ss"),
                    AbonoJson = JsonSerializer.Serialize(abono)
                };
                //print
                GenerarLog(IDENTIFICADOR, "ValidaPago", "Finaliza llenado de objeto traspasos");
            }

            //print fin de metodo
            GenerarLog(IDENTIFICADOR, "ValidaPago", "Fin del metodo de Valida Pago.");
            return new DtoPagoValidado
            {
                CveRastreo = pago.CveRastreo,
                PagoJson = jsonPagoAsp,
                IdTipoPago = tipoPago,
                TraspasoAsp = traspaso,
                Proveedor = proveedor,
                toAsp = toAsp,
                AbonoEiyu = string.Empty
            };
        }

        public string GetEmailByIdPblu(int idPblu)
        {
            //TODO: Esto asi estaba en el api de Blu, asi se quedara?
            return "procesos@asp.mx";
        }

        private string RegexConcepto = @"^[a-zA-Z0-9- ]+$";
        public bool ValidarConcepto(string concepto)
        {
            try
            {
                var matcher = Regex.Match(concepto, RegexConcepto);
                if (matcher.Success)
                    return true;
                if (string.IsNullOrEmpty(concepto))
                    return false;

                return false;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        private string RegexNombreDestino = @"[{}\[\]()]+";
        public bool ValidarNombreDestino(string nombreDestino)
        {
            try
            {
                var matcher = Regex.IsMatch(nombreDestino, RegexNombreDestino);
                if (matcher)
                    return false;
                if (string.IsNullOrEmpty(nombreDestino))
                    return false;

                return true;
            }
            catch (Exception e)
            {
                return false;
            }
        }

        string RegexRefNum = @"^[1-9]\d{0,6}$";

        public bool ValidarReferenciaNumerica(string referencia)
        {
            return Regex.IsMatch(referencia, RegexRefNum);

        }

        public int GetTipoCuentaDestino(string cuentaDestino, int idInstitucion)
        {
            var tipo = 40;
            switch (cuentaDestino.Length)
            {
                case 15:
                case 16:
                    tipo = 3; //Tarjeta
                    break;
                case 18:
                    tipo = 40; //Clabe
                    break;
                case 10:
                    tipo = 10; //Movil
                    break;
                default:
                    tipo = 0; //Indefinida
                    break;
            }

            //Aqui validan la cuentaDestino con cada tipo de cuenta revisando los numeros de digitos.
            if (tipo == 0)
                throw new ErrorCuentaMalformada("digitos invalidos para tipo indefinida");
            return tipo;
        }

        public async Task<int> GetTipoPago(int idBancoDestino, string cuentaOrigen, string cuentaDestino, int tipoPago)
        {
            if (idBancoDestino == 90659)//Pago mismo banco
            {
                //Revisamos si la cuenta destino existe en la tabla Cuenta
                if (!await _cuentaRepository.Existe(cuentaDestino))
                {
                    return 102;

                }


                return 101;

            }

            return tipoPago;
        }

        public Entities.DataBase.Pago ConvertOrdenPagoToPago(OrdenPagoDto ordenPago, string cuentaClabeOrigen, string firmaPago)
        {
            var pago = new Entities.DataBase.Pago();

            pago.CveRastreo = ordenPago.CveRastreo;
            pago.IdMovimiento = 0;
            pago.CuentaOrigen = cuentaClabeOrigen;
            pago.NombreDestino = ordenPago.NombreDestino;
            pago.IdTipoCuentaDestino = int.Parse(ordenPago.IdTipoCtaDestino);
            pago.CuentaDestino = ordenPago.CtaDestino;
            pago.RfcDestino = ordenPago.RfcDestino;
            var conceptoPago = ordenPago.ConceptoPago.Replace("\\u00a0", " ");
            pago.ConceptoPago = conceptoPago;
            pago.MontoCargo = decimal.Parse(ordenPago.Monto);
            pago.Iva = decimal.Parse(ordenPago.Iva);
            pago.RefCob = ordenPago.RefCob;
            pago.RefNum = ordenPago.RefNum;
            pago.IdBancoDestino = int.Parse(ordenPago.BancoDestino);
            pago.IdBancoOrigen = 90659;//Banco de Asp supongo
            pago.IdTipoPago = 1;
            pago.FechaCreacion = DateTime.Now;
            pago.Firma = firmaPago;
            pago.Uuid = ordenPago.Uuid;

            return pago;
        }

        // private Entities.DataBase.Abono 
        private DtoAbonoTraspaso GenerarAbono(Entities.DataBase.Pago pago, string nombreOrdenante, string IDENTIFICADOR, string proveedor, string tipoPago, string idTipoCuentaDestino,string uuid)

        {
          
            GenerarLog(IDENTIFICADOR, "ValidaPago", "Inicia creación de objeto de un traspaso");

            var fechaOperativa = DateTime.Today;
            if (fechaOperativa.DayOfWeek == DayOfWeek.Saturday)
            {
                fechaOperativa = fechaOperativa.AddDays(2);
            }
            else if (fechaOperativa.DayOfWeek == DayOfWeek.Sunday)
            {
                fechaOperativa = fechaOperativa.AddDays(1);
            }
            var abono = new DtoAbonoTraspaso
            {
                RfcDestino = pago.RfcDestino,
                CveRastreo = pago.CveRastreo,
                NombOrigen = nombreOrdenante,
                RefCob = pago.RefCob,
                IdTipoPago = tipoPago,
                ConceptoPago = pago.ConceptoPago,
                CausaDev = string.Empty,
                Folio_paquete = 0.ToString(),
                FhOperacion = fechaOperativa.ToString("yyyy/MM/dd"),
                IdTipoCtaDestino = idTipoCuentaDestino,
                NombreDestino = pago.NombreDestino,
                BancoOrigen = pago.IdBancoOrigen.ToString(),
                Monto = pago.MontoCargo.ToString(),
                RefNum = pago.RefNum ?? "",
                Iva = pago.Iva.ToString() ?? "0",
                CuentaReferencia = string.Empty,
                Folio = 0.ToString(),
                RfcOrigen = "ND",
                CuentaConcentradora = string.Empty,
                Clabe = pago.CuentaOrigen,
                CuentaDestino = pago.CuentaDestino,
                FechaCaptura= DateTimeOffset.Now.ToUnixTimeMilliseconds().ToString(),
                Uuid= uuid

            };

            //print
            GenerarLog(IDENTIFICADOR, "ValidaPago", "Finaliza llenado de objeto traspasos");
            return abono;
        }

        #endregion


        #region Spei Out Rechazado

        public async Task PagoRechazado(string cveRastreo, string descripcion)
        {
            try
            {
                _logger.LogInformation($"Inicia PagoRechazado con clave de rastreo: {cveRastreo}");
                _logger.LogInformation($"Se busca el pago con clave de rastreo: {cveRastreo}");
                var pago = await _pagoRepository.GetPagoByCveRastreo(cveRastreo);
                if (pago == null)
                {
                    _logger.LogInformation($"No se encontró el pago con Cve Rastreo :{cveRastreo}");
                    throw new ErrorClaveRastreoNoExiste($"No se encontró el pago con Cve Rastreo :{cveRastreo}");
                }

                _logger.LogInformation($"Se actualizara a rechazado el pago con clave :{cveRastreo}");
                await _pagoRepository.UpdateRechazado(cveRastreo, EstadosAsp.Rechazado, EstadosAsp.Rechazado, 30, descripcion,
                    DateTime.Now);

                _logger.LogInformation($"Inicia aumento al saldo de la UDN {pago.IdUdn}");
                //Aumentamos el saldo de la UDN
                await _udnRepository.AumentaSaldo(cveRastreo, pago.IdUdn ?? 0, pago.MontoCargo ?? 0m);

                _logger.LogInformation($"Finaliza aumento al saldo de la UDN {pago.IdUdn}");

                _logger.LogInformation($"Inicia aumento al saldo del PBLU {pago.IdPblu}");
                //Aumentamos el saldo del Pblu
                await _saldoPbluRepository.AumentaSaldo(pago.IdPblu ?? 0, pago.MontoCargo ?? 0m);
                _logger.LogInformation($"Finaliza aumento al saldo del PBLU {pago.IdPblu}");
            }
            catch (ErrorGenerico eg) when (eg is ErrorClaveRastreoNoExiste)
            {
                _logger.LogError($"Ocurrió un error al realizar el rechazo del pago con clave de rastreo: {cveRastreo} ", eg);

            }
            catch (Exception ex)
            {
                _logger.LogError($"Ocurrió un error al realizar el rechazo del pago con clave de rastreo: {cveRastreo} ", ex);

            }
        }

        #endregion


        #region Spei Out Pendiente
        /// <summary>
        /// Metodo que se ejecuta cuando el motor de pagos notifica que el pago ya fue recibido por ASP
        /// </summary>
        /// <param name="cveRastreo"></param>
        /// <param name="descripcion"></param>
        /// <exception cref="ErrorClaveRastreoNoExiste"></exception>
        public async Task<bool> PagoPendiente(string cveRastreo, string descripcion)
        {
            try
            {
                _logger.LogInformation($"Inicia PagoPendiente con clave de rastreo: {cveRastreo}");

                _logger.LogInformation($"Inicia actualización del pago con clave de rastreo: {cveRastreo} a un estado pendiente.");
                //Solo actualiza a un estado 99 cuando el id_estado_pago es un 8 y un 81 (el 81 se utiliza para el de pagos reintentos)
                bool actualizacionExitosa = await _pagoRepository.UpdateAceptado(cveRastreo, EstadosAsp.Pendiente, EstadosAsp.Pendiente, 30, descripcion,
                    DateTime.Now);
                _logger.LogInformation($"Finaliza actualización del pago con clave de rastreo: {cveRastreo} a un estado pendiente. {actualizacionExitosa}");
                return actualizacionExitosa;

            }
            catch (ErrorGenerico eg) when (eg is ErrorClaveRastreoNoExiste)
            {
                _logger.LogError($"Ocurrió un error al realizar la actualización a pendiente del pago con clave de rastreo: {cveRastreo} ", eg);
                throw;
            }
            catch (Exception ex)
            {
                _logger.LogError($"Ocurrió un error al realizar la actualización a pendiente del pago con clave de rastreo: {cveRastreo} ", ex);
                throw;
            }

        }

        #endregion


        #region Spei Out Cambio Estado

        public async Task<bool> PagoCambioEstado(string cveRastreo, int estado, string causa, DateTime fechaOperacion, int idCausaDevolucion)
        {
            try
            {
                _logger.LogInformation($"Inicia PagoCambioEstado con clave de rastreo: {cveRastreo}");
                _logger.LogInformation($"Se busca el pago con  clave de rastreo: {cveRastreo}");
                var pago = await _pagoRepository.GetPagoByCveRastreo(cveRastreo);
                if (pago == null)
                {
                    _logger.LogInformation($"No se encontró el pago con Cve Rastreo :{cveRastreo}");
                    throw new ErrorClaveRastreoNoExiste($"No se encontró el pago con Cve Rastreo :{cveRastreo}");
                }

                switch (estado)
                {
                    case 2://LIQUIDADA
                        _logger.LogInformation($"Inicia el update a liquidado al pago con clave de rastreo: {cveRastreo}");
                        await _pagoRepository.UpdateLiquidado(cveRastreo, estado, estado, 30, causa, DateTime.Now);
                        _logger.LogInformation($"Finaliza el update a liquidado al pago con clave de rastreo: {cveRastreo}");
                        break;
                    case 99://EN PROCESO
                    case 199://EN PROCESO
                    case 88://Pendiente de autorizar por monto superior al límite.
                        _logger.LogInformation($"Inicia el update a aceptado al pago con clave de rastreo: {cveRastreo}");
                        await _pagoRepository.UpdateAceptado(cveRastreo, estado, estado, 30, causa, fechaOperacion);
                        _logger.LogInformation($"Finaliza el update a aceptado al pago con clave de rastreo: {cveRastreo}");
                        break;
                    case 5://Rechazada por Banxico
                    case 9://Orden con errores
                           //Asignamos el estado 9 ("Rechazado ASP") para que la view de conciliacion, no lo considere al calcular el saldo
                        _logger.LogInformation($"Inicia el update a rechazado al pago con clave de rastreo: {cveRastreo}");
                        await _pagoRepository.UpdateRechazado(cveRastreo, 9, estado, 30, causa, fechaOperacion);
                        _logger.LogInformation($"Finaliza el update a rechazado al pago con clave de rastreo: {cveRastreo}");
                        break;
                    case 15://Devuelta
                        _logger.LogInformation($"Inicia el update a devuelta al pago con clave de rastreo: {cveRastreo}");
                        await _pagoRepository.UpdateRechazado(cveRastreo, estado, estado, 30, causa, fechaOperacion);
                        _logger.LogInformation($"Finaliza el update a devuelta al pago con clave de rastreo: {cveRastreo}");
                        break;
                    case 6://Cancelada
                        _logger.LogInformation($"Inicia el update a cancelada al pago con clave de rastreo: {cveRastreo}");
                        await _pagoRepository.UpdateCancelado(cveRastreo, estado, estado, 30, causa, fechaOperacion);
                        _logger.LogInformation($"Finaliza el update a cancelada al pago con clave de rastreo: {cveRastreo}");
                        break;
                    case 11://Acuse de recibido
                        _logger.LogInformation($"Inicia el update a acuse de recibido al pago con clave de rastreo: {cveRastreo}");
                        await _pagoRepository.UpdateAceptado(cveRastreo, estado, estado, 30, causa, fechaOperacion);
                        _logger.LogInformation($"Finaliza el update a acuse de recibido al pago con clave de rastreo: {cveRastreo}");
                        break;
                }

                _logger.LogInformation($"Inicia verificación de estado para iniciar un aumento de saldo al estado: {estado}");
                if (estado != 2 && estado != 99 && estado != 9 && estado != 11 /* && estado != 5 && estado != 15*/)
                {
                    //Realizamos la devolucion de los saldos
                    //Aumentamos el saldo de la UDN
                    _logger.LogInformation($"Inicia aumento de saldo a la UDN: {pago.IdUdn}");
                    await _udnRepository.AumentaSaldo(cveRastreo, pago.IdUdn ?? 0, pago.MontoCargo ?? 0m);
                    _logger.LogInformation($"Finaliza aumento de saldo a la UDN: {pago.IdUdn}");

                    _logger.LogInformation($"Inicia aumento de saldo al PBLU: {pago.IdPblu}");
                    //Aumentamos el saldo del Pblu
                    await _saldoPbluRepository.AumentaSaldo(pago.IdPblu ?? 0, pago.MontoCargo ?? 0m);
                    _logger.LogInformation($"Finaliza aumento de saldo al PBLU: {pago.IdPblu}");
                }
                return true; //retornamos true si todo estuvo OK

            }
            catch (ErrorGenerico eg) when (eg is ErrorClaveRastreoNoExiste)
            {
                _logger.LogError($"Ocurrió un error al actualizar el cambio de estado al pago con clave de rastreo: {cveRastreo} ", eg);

            }
            catch (Exception ex)
            {
                _logger.LogError($"Ocurrió un error al actualizar el cambio de estado al pago con clave de rastreo: {cveRastreo} ", ex);

            }
            return false;
        }

        #endregion

        #region  Insert de multiples cambios de estado
        public async Task<bool> MultiplesCambiosDeEstado(List<string> ClavesDeRastreo, int IdEstadoPago)
        {
            try
            {
                _logger.LogInformation($"Inicia multiples actualizaciones de cambios de estado: {JsonSerializer.Serialize(ClavesDeRastreo)}");
                bool actualizacionExitosa = await _pagoRepository.InsertMultiplesEstados(ClavesDeRastreo, IdEstadoPago);
                _logger.LogInformation($"Finaliza multiples actualizaciones de cambios de estado, resultado actualizacionExitosa: {actualizacionExitosa}.");
                return actualizacionExitosa;
            }
            catch (Exception ex)
            {
                _logger.LogError($"Ocurrió un error al realizar multiples update de cambio de estado a los pagos con clave de rastreo: {JsonSerializer.Serialize(ClavesDeRastreo)} ", ex);
                throw;
            }
        }
        #endregion

        #region Cadena Firma SIES

        private string GeneraCadenaFirmarSIES(OrdenPagoSIES ordenPago)
        {
            var cadena = "";
            cadena += FormateaString(ordenPago.NombreOrdenante); //
            cadena += FormateaInt(ordenPago.IdTipoCuentaOrdenante); //
            cadena += FormateaString(ordenPago.CuentaOrdenante); //
            cadena += FormateaString(ordenPago.RfcOrdenante); //
            cadena += FormateaString(ordenPago.NombreBeneficiario); //
            cadena += FormateaInt(ordenPago.IdTipoCuentaBeneficiario); //
            cadena += FormateaString(ordenPago.CuentaBeneficiario); //
            cadena += FormateaString(ordenPago.RfcBeneficiario); //
            cadena += FormateaString(ordenPago.ConceptoPago); //
            cadena += FormateaImporte(ordenPago.Monto); //
            cadena += FormateaImporte(ordenPago.Iva); //
            cadena += FormateaInt(ordenPago.ReferenciaNumerica); //
            cadena += FormateaString(ordenPago.ReferenciaCobranza); //ReferenciaCobranza -> ReferenciaCobranza
            cadena += FormateaInt(ordenPago.IdTipoPago); //
            cadena += FormateaString(ordenPago.Topologia); //
            cadena += FormateaInt(ordenPago.Prioridad); //
            cadena += FormateaInt(ordenPago.IdTipoOperacion); //
            cadena += FormateaString(ordenPago.NombreBeneficiario2); //
            cadena += FormateaString(ordenPago.RfcBeneficiario2); //
            cadena += FormateaString(ordenPago.CuentaBeneficiario2); //
            cadena += FormateaString(ordenPago.ConceptoPago2); //
            cadena += FormateaInt(ordenPago.IdTipoCuentaBeneficiario2); //
            cadena += FormateaString(ordenPago.CveRastreo); //
            cadena += FormateaInt(ordenPago.IdInstitucionOrd); //
            cadena += FormateaInt(ordenPago.IdInstitucionBen); //
            cadena += FormateaInt(ordenPago.FolioPaqueteDevExt); //
            cadena += FormateaImporte(ordenPago.MontoOriginalDevExt); //
            cadena += FormateaImporte(ordenPago.MontoInteresDevExt); //
            //cadena += FormateaString(ordenPago.FechaCaptura); //
            cadena += FormateaString(Convert.ToDateTime(ordenPago.FechaCaptura).ToString("ddMMyyyyHHmmss"));

            cadena += FormateaString(ordenPago.FechaOperacion); //
            cadena += FormateaString(ordenPago.FechaOperacionDevExt); //

            //DATOS NECESARIOS PARA FIRMA POR PI
            cadena += FormateaInt(ordenPago.IdTipoCuentaOrdIndirecto);
            cadena += FormateaImporte(ordenPago.TipoCambio);


            return cadena;
        }


        private string FormateaString(string valor)
        {
            if (string.IsNullOrEmpty(valor))
                return "";

            return valor.Trim();
        }

        private string FormateaInt(string valor)
        {
            if (string.IsNullOrEmpty(valor))
                return "0";
            return valor;
        }

        private string FormateaImporte(string valor)
        {
            if (string.IsNullOrEmpty(valor))
                return "0.00";
            var importe = decimal.Parse(valor);
            //return importe.ToString("N2");
            return importe.ToString("F2", CultureInfo.InvariantCulture);
        }

        public string LimpiaCaracteres(string src, bool eliminaPunto, bool stringWithoutNumber = false)
        {
            StringBuilder sb = new StringBuilder();
            int[] ascii = { 162, 164, 165, 167, 169, 170, 171, 172, 174, 176, 177, 178, 179, 180, 181, 182, 184, 185, 186, 187, 188, 189, 190, 198, 208, 216, 222, 223, 230, 231, 248, 254 };
            int[] ascciInv = { 192, 193, 194, 195, 196, 197, 200, 201, 202, 203, 204, 205, 206, 207, 210, 211, 212, 213, 214, 217, 218, 219, 220, 224, 225, 226, 227, 228, 229, 232, 233, 234, 235, 236, 237, 238, 239, 242, 243, 244, 245, 246, 249, 250, 251, 252, 253, 255 };
            int[] ascciVal = { 65, 65, 65, 65, 65, 65, 69, 69, 69, 69, 73, 73, 73, 73, 79, 79, 79, 79, 79, 85, 85, 85, 85, 97, 97, 97, 97, 97, 97, 101, 101, 101, 101, 105, 105, 105, 105, 111, 111, 111, 111, 111, 117, 117, 117, 117, 121, 121 };
            string original;
            int ascval;

            original = src;
            try
            {
                original = original.Replace('?', ' ');
                original = original.Replace('ñ', 'n').Replace('Ñ', 'N');
                original = original.Replace('á', 'a').Replace('é', 'e').Replace('í', 'i').Replace('ó', 'o').Replace('ú', 'u');
                original = original.Replace('Á', 'A').Replace('É', 'E').Replace('Í', 'I').Replace('Ó', 'O').Replace('Ú', 'U');
                original = System.Text.RegularExpressions.Regex.Replace(original, "[^a-zA-Z0-9 ,-_/]", "");

                original = original.Replace('¤', 'ñ')
                                    .Replace('¥', 'Ñ')
                                    .Replace('Á', 'A')
                                    .Replace('É', 'E')
                                    .Replace('Í', 'I')
                                    .Replace('Ó', 'O')
                                    .Replace('Ú', 'U')
                                    .Replace('¡', 'í')
                                    .Replace('¢', 'ó')
                                    .Replace('£', 'ú')
                                    .Replace('u', 'ü')
                                    .Replace('\'', '´')
                                    .Replace("{", "").Replace("}", "")
                                    .Replace("[", "").Replace("]", "")
                                    .Replace("(", "").Replace(")", "");


                if (eliminaPunto)
                {
                    original = original.Replace(".", "");
                }
                if (stringWithoutNumber)
                {
                    original = Regex.Replace(original, @"\d", "");
                }

                foreach (char c in original)
                {
                    ascval = (int)c;
                    if (ascval > 256)
                    {
                        sb.Append((char)38); // equivale a "&"
                    }
                    else
                    {
                        sb.Append((char)ascval); // equivale a:
                    }
                }
                original = sb.ToString();

                foreach (int asciiValue in ascii)
                {
                    original = original.Replace((char)asciiValue, (char)32);
                }

                for (int i = 0; i < ascciInv.Length; i++)
                {
                    original = original.Replace((char)ascciInv[i], (char)ascciVal[i]);
                }
            }
            catch (Exception e)
            {
                original = src;
            }
            return original;
        }

        #endregion
        private void GenerarLog(string timestamp, string metodo, string text)
        {
            _logger.LogInformation($"{metodo} - {text}");

        }

        public async Task<DatosCuentaPrevFraudeDto> ObtenerDatosCuenta(string IDENTIFICADOR, string clabe, decimal monto)
        {
            try
            {
                GenerarLog(IDENTIFICADOR, "ObtenerDatosCuenta", $"ObtenerDatosCuenta-> Inicia la consulta para obtener los datos de la cuenta");
             
                var datos = await _pagoRepository.ObtenerDatosCuenta(clabe);
                GenerarLog(IDENTIFICADOR, "ObtenerDatosCuenta", $"ObtenerDatosCuenta-> Termina la consulta para obtener los datos de la cuenta");
                datos.Monto = monto;
                GenerarLog(IDENTIFICADOR, "ObtenerDatosCuenta", $"ObtenerDatosCuenta-> Datos de la cuenta: {JsonSerializer.Serialize(datos)}");
       
                return datos;

            }
            catch(Exception ex)
            {
                GenerarLog(IDENTIFICADOR, "ObtenerDatosCuenta", $"ObtenerDatosCuenta-> Error: {ex.Message}:{ex.StackTrace}");
        
                throw new Exception("Ocurrio un error al obtener los datos de la cuenta.");
            }
        }

        public async Task ValidarPago(string IDENTIFICADOR, int id_pblu, bool isPortal, OrdenPagoDto ordenPago)
        {
            if (ordenPago.CtaDestino == ordenPago.Clabe)
                throw new ErrorPeticionMalformada("La cuenta CLABE origen y destino no deben ser iguales.");

            //Validamos si el concepto de pago es correcto
            if (!ValidarConcepto(ordenPago.ConceptoPago))
                throw new ErrorPeticionMalformada("El pago no cuenta con concepto o contiene caracteres no permitidos");

            //Validamos si la referencia númerica es correcta
            if (!ValidarReferenciaNumerica(ordenPago.RefNum))
                throw new ErrorPeticionMalformada("El campo RefNum debe ser numérico, de longitud máxima de 7 caracteres y no debe iniciar con 0.");

            //Validamos si el nombre destino es correcto
            if (!ValidarNombreDestino(ordenPago.NombreDestino))
                throw new ErrorPeticionMalformada($"El campo nombreDestino no cumple con el formato: {ordenPago.NombreDestino}");

            GenerarLog(IDENTIFICADOR, "ValidarPago", $"GetValidacionPago-> Inicia validación de pagos");
            ValidaPago _validaciones = await _pagoRepository.GetValidacionPago(id_pblu, ordenPago.CveRastreo, int.Parse(ordenPago.BancoDestino), ordenPago.Clabe, isPortal, decimal.Parse(ordenPago.Monto), ordenPago.CtaDestino);
            GenerarLog(IDENTIFICADOR, "ValidarPago", $"GetValidacionPago-> Finaliza validación de pagos");
            //print

            if (!_validaciones.Success)
            {
                throw new Exception(_validaciones.Mensaje);
            }
            GenerarLog(IDENTIFICADOR, "ValidarPago", $"GetValidacionPago-> Validacion exitosa.");
        }

        public async Task<SpeiOutDto> ProcesarPagoValidado(DtoDatosOriginalesPago datosOriginalesPago)
        {
     
            var claveRastreo = datosOriginalesPago.OrdenPago.CveRastreo;
            var IdPblu = datosOriginalesPago.IdPblu;
            var cuentaDestino = datosOriginalesPago.OrdenPago.CtaDestino;
            var bancoDestino = datosOriginalesPago.OrdenPago.BancoDestino;
            var clabe = datosOriginalesPago.OrdenPago.Clabe;
            try
            {

               
                _logger.LogInformation($"Inicio metodo ProcesarPagoValidado, clave de rastreo {claveRastreo}");
                string proveedor = "SIES";
                string correoPblu = GetEmailByIdPblu(datosOriginalesPago.IdPblu);
                Entities.DataBase.Pago pago = new Entities.DataBase.Pago();


                _logger.LogInformation($"GetValidacionPago-> Inicia validación de pagos");
               
                ValidaPago _validaciones = await _pagoRepository.GetValidacionPago(IdPblu, claveRastreo, int.Parse(bancoDestino), clabe, datosOriginalesPago.IsPortal, decimal.Parse(datosOriginalesPago.OrdenPago.Monto), cuentaDestino);
                _logger.LogInformation($"GetValidacionPago-> Finaliza validación de pagos");
                //print

                if (!_validaciones.Success)
                {
                    _logger.LogInformation($"Error al validar el pago, {_validaciones.Mensaje}. Se notifica al participante el cambio de estado");
                    var message = new TraspasoCambioEstadoDto
                    {

                        ClaveRastreo = claveRastreo,
                        Estado = EstadosAsp.Cancelado.ToString(),
                        Causa = _validaciones.Mensaje,
                        FechaOperacion = DateTimeOffset.Now.ToUnixTimeMilliseconds().ToString(),
                        IdPblu = IdPblu
                    };
                    _logger.LogInformation($"Inicia envio mediante Kafka Mensaje de cambio de estado: {JsonSerializer.Serialize(message)}");
                    await _kafkaProducer.EnviarMensajeAsync(Topicos.TraspasoCambioEstadoHandlerKafka, message);
                    _logger.LogInformation($"Termina el envio mediante Kafka. Finaliza el flujo");
                    throw new Exception(_validaciones.Mensaje);
                }

                proveedor = _validaciones.Proveedor;


                var nombreOrdenanteResult = _validaciones.NombreWithRFC;
                var nombreOrdenanteArray = nombreOrdenanteResult.Split(",");
                var nombreOrdenante = "";
                var rfcOrdenante = "";
                if (nombreOrdenanteArray.Length == 2)
                {
                    nombreOrdenante = nombreOrdenanteArray[0];
                    rfcOrdenante = nombreOrdenanteArray[1];
                }

            
                _logger.LogInformation($"GetTipoCuentaDestino-> Se obtiene el tipo cuenta destino");
                int idTipoCuentaDestino = GetTipoCuentaDestino(cuentaDestino, int.Parse(bancoDestino));

                _logger.LogInformation($"GetTipoPago-> Se genera el objeto de pago");
                pago = ConvertOrdenPagoToPago(datosOriginalesPago.OrdenPago, clabe, datosOriginalesPago.Firma);
                var fechaOperacion = DateTime.Today;
                if (fechaOperacion.DayOfWeek == DayOfWeek.Saturday)
                {
                    fechaOperacion = fechaOperacion.AddDays(2);
                }
                else if (fechaOperacion.DayOfWeek == DayOfWeek.Sunday)
                {
                    fechaOperacion = fechaOperacion.AddDays(1);
                }

                pago.FechaOperacion = fechaOperacion;
                pago.IdEstadoPago = 8;
                pago.IdTipoCuentaDestino = idTipoCuentaDestino;
                pago.IdTipoPago = 1;




                pago.Json = datosOriginalesPago.JsonPago;
                pago.Llave = int.Parse(datosOriginalesPago.Llave);
                pago.IdPblu = datosOriginalesPago.IdPblu;
                pago.IdUdn = _validaciones.IdUdn;

                _logger.LogInformation($"InsertPagoPl-> Inicia insert del pago en base de datos");
                int respuesta = await _pagoRepository.InsertPagoPl(pago, pago.FolioPaquete ?? 0);
                _logger.LogInformation($"InsertPagoPl-> Finaliza insert del pago en base de datos");
                if (respuesta == 2)
                    throw new ErrorClaveRastreoNoExiste($"Error en la clave de rastreo, ya existe el pago o traspaso: {pago.CveRastreo} - {pago.FolioPaquete}");

                if (respuesta == 0)
                    throw new Exception($"Error al registrar el pago");


                var traspaso = new DtoTraspasoAsp();
                string jsonPagoAsp = "";
      
                    if (proveedor == "ASP")
                    {
                    _logger.LogInformation($"Inicia creación de objeto proveedor ASP");
              
                        //print
                        var pagoAsp = new
                        {
                            cuentaBeneficiario = pago.CuentaDestino ?? string.Empty,
                            nombreBeneficiario = pago.NombreDestino ?? string.Empty,
                            conceptoPago = pago.ConceptoPago ?? string.Empty,
                            nombreOrdenante = nombreOrdenante ?? string.Empty,
                            rfcBeneficiario = pago.RfcDestino ?? string.Empty,
                            referenciaNumerica = pago.RefNum ?? string.Empty,
                            bancoDestino = pago.IdBancoDestino.ToString(),
                            claveRastreo = pago.CveRastreo ?? string.Empty,
                            rfcOrdenante = "",
                            monto = pago.MontoCargo.ToString(),
                            tipoCuenta = pago.IdTipoCuentaDestino.ToString(),
                            cuentaOrdenante = pago.CuentaOrigen ?? string.Empty,
                            tipoPago = pago.IdTipoPago.ToString()
                        };
                        jsonPagoAsp = JsonSerializer.Serialize(pagoAsp);
                    }
                    else
                    {
                    _logger.LogInformation($"Inicia creación de objeto proveedor SIES");

                    pago.NombreDestino = pago.NombreDestino ?? string.Empty;
                        var nombreDestinoLength = pago.NombreDestino.Length;
                        pago.NombreDestino =
                            pago.NombreDestino.Substring(0, nombreDestinoLength <= 39 ? nombreDestinoLength : 39);


                        nombreOrdenante = nombreOrdenante ?? string.Empty;
                        var nombreOrdenanteLength = nombreOrdenante.Length;
                        nombreOrdenante =
                            nombreOrdenante.Substring(0, nombreOrdenanteLength <= 39 ? nombreOrdenanteLength : 39);

                        rfcOrdenante = rfcOrdenante ?? string.Empty;
                        var rfcOrdenanteLength = rfcOrdenante.Length;
                        rfcOrdenante =
                            rfcOrdenante.Substring(0, rfcOrdenanteLength <= 13 ? rfcOrdenanteLength : 13);

                     

                    _logger.LogInformation($"Llenado de información proveedor SIES");
                    var pagoSIES = new OrdenPagoSIES
                        {
                            CveEntidad = "ASPINTEGRA",
                            IdEmpresa = "1",
                            CuentaBeneficiario = LimpiaCaracteres(pago.CuentaDestino ?? string.Empty, false),
                            NombreBeneficiario = LimpiaCaracteres(pago.NombreDestino, true, true),
                            ConceptoPago = LimpiaCaracteres(pago.ConceptoPago ?? string.Empty, true),
                            NombreOrdenante = LimpiaCaracteres(nombreOrdenante, true, true),
                            RfcBeneficiario = LimpiaCaracteres(pago.RfcDestino ?? string.Empty, false),
                            ReferenciaNumerica = pago.RefNum ?? string.Empty,
                            IdInstitucionBen = pago.IdBancoDestino.ToString(),
                            IdInstitucionOrd = 90659.ToString(),
                            CveRastreo = pago.CveRastreo,
                            RfcOrdenante = LimpiaCaracteres(rfcOrdenante, false),
                            Monto = pago.MontoCargo.ToString() ?? string.Empty,
                            IdTipoCuentaBeneficiario = pago.IdTipoCuentaDestino.ToString(),
                            CuentaOrdenante = pago.CuentaOrigen ?? string.Empty,
                            IdTipoPago = pago.IdTipoPago.ToString(),
                            FechaCaptura = DateTimeFix.Now().ToString("yyyy-MM-dd HH:mm:ss"),
                            Envio = "1",
                            Reenvio = "1",
                            Verificado = "1",
                            IdTipoCuentaOrdenante = "40",
                            Iva = "0",
                            Topologia = "V",
                            Prioridad = "1",
                            IdAreaEmite = "10",
                            ReferenciaCobranza = DateTime.Today.ToString("ddMMyyyy")
                        };
                        var cadenaFirmar = GeneraCadenaFirmarSIES(pagoSIES);
                        Console.WriteLine($"Cadena Original: {cadenaFirmar}");
                        var firmaSies = _encriptionService.FirmarSIES(cadenaFirmar);
                        pagoSIES.FirmaCoreBancario = firmaSies;
                        jsonPagoAsp = JsonSerializer.Serialize(pagoSIES, new JsonSerializerOptions { DefaultIgnoreCondition = JsonIgnoreCondition.WhenWritingNull });

                    _logger.LogInformation($"Fin del llenado de información proveedor SIES");
                }
                

                /* return new DtoPagoValidado
                 {
                     CveRastreo = pago.CveRastreo,
                     PagoJson = jsonPagoAsp,
                     IdTipoPago = tipoPago,
                     TraspasoAsp = traspaso,
                     Proveedor = proveedor,
                     toAsp = toAsp,
                     AbonoEiyu = string.Empty
                 };*/
                SpeiOutDto eventMessage = null;
                if (proveedor == "ASP")
                {
                    var respuestaEventBus = _encriptionService.Encript(jsonPagoAsp, 0);

                    eventMessage = new SpeiOutDto
                    {
                        Pago = respuestaEventBus.JsonCifrado,
                        ClaveRastreo = claveRastreo,
                        IdPblu = IdPblu,
                        Key = respuestaEventBus.Key,
                        Signature = respuestaEventBus.Firma,
                        Proveedor = proveedor
                    };

                }
                else
                {
                    eventMessage = new SpeiOutDto
                    {
                        Pago = jsonPagoAsp,
                        ClaveRastreo = claveRastreo,
                        IdPblu = IdPblu,
                        Key = string.Empty,
                        Signature = string.Empty,
                        Proveedor = proveedor
                    };
                    // await _kafkaProducer.EnviarMensajeAsync(Topicos.SpeiOutSies, eventMessage);
                   // await _kafkaProducer.EnviarMensajeAsync(Topicos.PrevFraude_SolicitudIniciada, eventMessage);
                }


                //print fin de metodo
                /*
            Validacion monitor plus
         */

                var tipoValidacion = await _configurationService.GetWithCache<TipoValidacionAntifraude>(GeneralConstants.MonitorPlus.USE_MONITORPLUS, TimeSpan.FromMinutes(5));
                //bandera para decidir si escoger monitor plus o prev de fraudes
                if (tipoValidacion == TipoValidacionAntifraude.MonitorPlus || tipoValidacion == TipoValidacionAntifraude.Ambos)
                {
                    _logger.LogInformation($"SE UTILIZA EL CANAL DE MONITOR PLUS");

                    var respuestaMonitorPlus = await _monitorPlusService.ValidarOperacionMonitorPlus(datosOriginalesPago.OrdenPago, IdPblu);
                    if (!respuestaMonitorPlus.Success)

                    {
                        _logger.LogInformation("El pago fallo al validarse en Monitor Plus. El flujo termina y se notifica al participante el cambio de estado");

                        var message = new TraspasoCambioEstadoDto
                        {

                            ClaveRastreo = claveRastreo,
                            Estado = EstadosAsp.Cancelado.ToString(),
                            Causa = "Error al procesar el pago.",
                            FechaOperacion = DateTimeOffset.Now.ToUnixTimeMilliseconds().ToString(),
                            IdPblu = IdPblu
                        };
                        _logger.LogInformation($"Inicia envio mediante Kafka Mensaje de cambio de estado: {JsonSerializer.Serialize(message)}");
                        await _kafkaProducer.EnviarMensajeAsync(Topicos.TraspasoCambioEstadoHandlerKafka, message);
                        _logger.LogInformation($"Termina el envio mediante Kafka. Finaliza el flujo");
                        throw new Exception($"No se pudo validar el pago en monitor plus: {respuestaMonitorPlus.Message}");

                    }


                }
                //print fin de metodo
                _logger.LogInformation($"Fin del metodo de Valida Pago.");

                return eventMessage;
       
            }
            catch (Exception ex) {
                _logger.LogError($"Ocurrio un error",ex);
                await _aspLogservice.RegistraError("","POST",datosOriginalesPago,new {},ex,IdPblu,claveRastreo);
                return null;
            }
            
           
        }
    }
}
