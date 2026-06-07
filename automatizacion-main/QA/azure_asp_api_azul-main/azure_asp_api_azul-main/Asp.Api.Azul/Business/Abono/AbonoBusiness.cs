using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Utils;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Repositorys.AbonoRepository;
using Asp.Api.Azul.Repositorys.CuentaRepository;
using Asp.Api.Azul.Repositorys.SaldoPbluRepository;
using Asp.Api.Azul.Repositorys.UdnRepository;
using Asp.Api.Azul.Services.KafkaPLD;
using Microsoft.Extensions.Configuration;
using Org.BouncyCastle.Asn1.X500;
using System.Globalization;
using System.Text.Json;
using System.Threading;

namespace Asp.Api.Azul.Business.Abono
{
    public class AbonoBusiness : IAbonoBusiness
    {
        private readonly IUdnRepository _udnRepository;
        private readonly IAbonoRepository _abonoRepository;
        private readonly ISaldoPbluRepository _saldoPbluRepository;

        private readonly IConfiguration _configuration;
        private readonly IKafkaPldService _kafkaPldService;
        private readonly LoggerHelper _logger;
        private readonly string URL_KAFKA;

        public AbonoBusiness(IUdnRepository udnRepository, IAbonoRepository abonoRepository,
            ISaldoPbluRepository saldoPbluRepository, IConfiguration configuration, IKafkaPldService kafkaPldService, LoggerHelper logger)
        {
            _udnRepository = udnRepository;
            _abonoRepository = abonoRepository;
            _saldoPbluRepository = saldoPbluRepository;
            _configuration = configuration;
            _kafkaPldService = kafkaPldService;
            _logger = logger;
            URL_KAFKA = _configuration["urls:SERVICIO_PLD_KAFKA"];

        }
        public async Task RecibeAbono(OrdenAbono ordenAbono, string firma, bool validarInexistencia = true)
        {
            try
            {
                _logger.LogInformation("\x1b[32m**************************************************\x1b[0m");
                _logger.LogInformation($"\x1b[32m******** Inicia petición registro abono con clave: {ordenAbono.CveRastreo} ********\x1b[0m");
                _logger.LogInformation("\x1b[32m**************************************************\x1b[0m");
                var datosUdn = await _abonoRepository.ObtenerDatosUdn(ordenAbono.CuentaDestino);

                if (datosUdn == null)
                {
                    _logger.LogInformation($"La cuenta destino ´{ordenAbono.CuentaDestino}´ no existe");
                    throw new ErrorConsultaCuenta($"No se encontró la cuenta con clabe {ordenAbono.CuentaDestino}");

                }

                var fechaOperacion =
                    DateTime.ParseExact(ordenAbono.FechaOperacion, "yyyy/MM/dd", CultureInfo.InvariantCulture);

                var folioPaquete = int.Parse(ordenAbono.FolioPaquete);
                var folio = int.Parse(ordenAbono.Folio);

                //Validamos que no exista el abono (que no sea repetido)
                if (validarInexistencia && await _abonoRepository.ExisteAbono(ordenAbono.CveRastreo, folioPaquete, folio, fechaOperacion))
                {
                    _logger.LogInformation($"Ya existe un pago con la clave de rastreo {ordenAbono.CveRastreo}");
                    throw new Exception($"Ya existe un pago con la clave de rastreo {ordenAbono.CveRastreo}");
                }

                var monto = ValidarMonto(ordenAbono.Monto);
                ValidarCuentaDestino(ordenAbono);


                //Validamos la cuenta destino


                var iva = 0m;
                if (!decimal.TryParse(ordenAbono.Iva, out iva))
                {
                    _logger.LogInformation("El Iva no es un número");
                    throw new ErrorMonto("El Iva no es un número");
                }

                //Guardamos el abono
                Entities.DataBase.Abono abono = new Entities.DataBase.Abono
                {
                    CveRastreo = ordenAbono.CveRastreo,
                    CuentaOrigen = ordenAbono.Clabe,
                    NombreDestino = ordenAbono.NombreDestino,
                    NombreOrigen = ordenAbono.NombreOrigen,
                    RfcOrigen = ordenAbono.RfcOrigen,
                    IdTipoCuentaDestino = int.Parse(ordenAbono.IdTipoCuentaDestino),
                    IdMovimiento = 1,
                    CuentaDestino = ordenAbono.CuentaDestino,
                    RfcDestino = ordenAbono.RfcDestino,
                    ConceptoPago = ordenAbono.ConceptoPago,
                    MontoAbono = monto,
                    Iva = iva,
                    RefCob = ordenAbono.RefCob,
                    RefNum = ordenAbono.RefNum,
                    IdBancoOrigen = int.Parse(ordenAbono.BancoOrigen),
                    IdBancoDestino = 90659,
                    IdTipoPago = int.Parse(ordenAbono.IdTipoPago),
                    FechaOperacion = fechaOperacion,
                    FechaCreacion = DateTime.Now,
                    Firma = firma,
                    FolioPaquete = folioPaquete,
                    Folio = folio,
                    FechaBanxico =
                        DateTimeOffset.FromUnixTimeMilliseconds(long.Parse(ordenAbono.FechaCaptura)).DateTime,

                    IdEstadoPago = ordenAbono.Estado,
                    IdCausaDevolucion = ordenAbono.IdCausaDevolucion,
                    IdPblu = datosUdn.IdPblu,
                    IdUdn = datosUdn.IdUdn,
                    Uuid = ordenAbono.Uuid,
                    IdRetiro = ordenAbono.IdRetiro
                };

                var kafkaValoresReferencia = new KafkaPldValoresReferencia
                {
                    Monto = monto,
                    ClaveRastreo = ordenAbono.CveRastreo,
                    CuentaReferencia = ordenAbono.CuentaDestino,
                    IdParticipante = datosUdn.IdPblu,
                    FechaTransaccion = DateTime.Now.ToString("yyyy-MM-dd"),
                    FechaOperacion = fechaOperacion.ToString("yyyy-MM-dd"),
                    Categoria = 3,
                    Sucursal = "Virtual"

                };
                var kafkaPldData = new KafkaConsumer
                {
                    Origen = 2,
                    Usuario = "01",
                    ValorReferencia = JsonSerializer.Serialize(kafkaValoresReferencia),
                    Fecha_envio = DateTime.Now.ToString("yyyy-MM-dd"),
                };
                //Registramos el abono.
                _logger.LogInformation("Inicia el insert del abono en la tabla de abono.");
                await _abonoRepository.Insert(abono);
                _logger.LogInformation("Finaliza el insert del abono en la tabla de abono.");

                _logger.LogInformation($"Inicia notificación webhook servicio KafkaPLD a la url: {URL_KAFKA}");
                await _kafkaPldService.NotificarAbonoKafkaPLD(URL_KAFKA, kafkaPldData, datosUdn.IdPblu, ordenAbono.CveRastreo);
                _logger.LogInformation($"Finaliza notificación webhook servicio KafkaPLD a la url: {URL_KAFKA}");

            }
            catch (ErrorGenerico eg) when (eg is ErrorConsultaCuenta ||
                                           eg is ErrorPagoExistente ||
                                           eg is ErrorMonto ||
                                           eg is ErrorCuentaTDD ||
                                           eg is ErrorCuentaMalformada)
            {

                _logger.LogError($"Ocurrió un error al guardar el abono con clave de rastreo: {ordenAbono.CveRastreo}", eg);

            }
            catch (Exception e)
            {
                _logger.LogError($"Ocurrió un error al guardar el abono con clave de rastreo: {ordenAbono.CveRastreo}", e);

            }

            _logger.LogInformation("\x1b[32m**************************************************\x1b[0m");
            _logger.LogInformation($"\x1b[32m******** Termina petición registro abono con clave: {ordenAbono.CveRastreo} ********\x1b[0m");
            _logger.LogInformation("\x1b[32m**************************************************\x1b[0m");
        }



        public async Task<bool> CambioEstadoRetorno(string cveRastreo, int estado, string causa, DateTime fecha,
            int idCausaDevolucion, string fechaOperacion)
        {
            try
            {
                _logger.LogInformation($"Inicia CambioEstadoRetorno con clave de rastreo: {cveRastreo}");
                var fhOp =
                    DateTime.ParseExact(fechaOperacion, "MM/dd/yyyy", CultureInfo.InvariantCulture);
                var abono = await _abonoRepository.GetAbonoByCveRastreo(cveRastreo, fhOp);
                if (abono == null)
                {
                    _logger.LogInformation($"No se encontró el abono con clave de rastreo: {cveRastreo}");
                    throw new ErrorClaveRastreoNoExiste($"No se encontró el abono con Cve Rastreo :{cveRastreo}");
                }

                _logger.LogInformation($"Inicia el update del cambio de estado del retorno (abono) a un estatus {estado}");
                await _abonoRepository.UpdateEstadoRetorno(cveRastreo, estado, causa, fecha, idCausaDevolucion, fhOp);
                _logger.LogInformation($"Finaliza el update del cambio de estado del retorno (abono)");

                //Disminuimos el saldo de la UDN
                _logger.LogInformation($"Inicia la disminución del saldo a la UDN {abono.IdUdn}");
                await _udnRepository.DisminuyeSaldo(abono.IdUdn ?? 0, abono.MontoAbono ?? 0);
                _logger.LogInformation($"Finaliza la disminución del saldo a la UDN {abono.IdUdn}");

                //Disminuimos el saldo del Pblu
                _logger.LogInformation($"Inicia la disminución del saldo a la tabla saldo_pblu {abono.IdPblu}");
                await _saldoPbluRepository.DisminuyeSaldo(abono.IdPblu ?? 0, abono.MontoAbono ?? 0);
                _logger.LogInformation($"Finaliza la disminución del saldo a la tabla saldo_pblu {abono.IdPblu}");
                return true;
            }
            catch (ErrorGenerico eg) when (eg is ErrorClaveRastreoNoExiste)
            {
                _logger.LogError($"Ocurrió un error al realizar el cambio de estado del retorno. ", eg);
                //var logError = _logsBusiness.RegistraErrorAzul(eg, 0, LogLevel.Error, "SpeiOutRechazadoIntegrationEventHandler");

            }
            catch (Exception ex)
            {
                _logger.LogError($"Ocurrió un error al realizar el cambio de estado del retorno. ", ex);
                //var logError = _logsBusiness.RegistraError(e, 0, LogLevel.Error, "SpeiOutRechazadoIntegrationEventHandler");
            }
            return false;
        }
        public async Task<bool> RecibeRetorno(OrdenAbono ordenAbono, string firma)
        {
            try
            {
                var fhOp =
                    DateTime.ParseExact(ordenAbono.FechaOperacion, "yyyy/MM/dd", CultureInfo.InvariantCulture);
                var abono = await _abonoRepository.GetAbonoByCveRastreo(ordenAbono.CveRastreo, fhOp);
                if (abono == null)
                {
                    ordenAbono.Estado = 13;//Por retornar
                    await RecibeAbono(ordenAbono, firma, validarInexistencia: false);
                    return true;
                }
                else
                {
                    return await _abonoRepository.UpdateRetorno(ordenAbono.CveRastreo, 13, "", DateTime.Now,
                          ordenAbono.IdCausaDevolucion ?? 0);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                throw;
            }
        }

        private decimal ValidarMonto(string montoString)
        {

            if (string.IsNullOrEmpty(montoString))
            {
                _logger.LogInformation("El monto es nulo o vacío.");
                throw new ErrorMonto("El monto es nulo");
            }

            if (!decimal.TryParse(montoString, out decimal monto))
            {
                _logger.LogInformation("El monto no es un número");
                throw new ErrorMonto("El monto no es un número");
            }

            if (monto <= 0)
            {
                _logger.LogInformation(monto == 0 ? "El monto está en cero" : "El monto es negativo");
                throw new ErrorMonto(monto == 0 ? "El monto está en cero" : "El monto es negativo");
            }
            return monto;
        }

        private void ValidarCuentaDestino(OrdenAbono ordenAbono)
        {
            switch (ordenAbono.IdTipoCuentaDestino)
            {
                case "3":
                    if (ordenAbono.CuentaDestino.Length != 16)
                    {
                        _logger.LogInformation("El número de tarjeta debe consistir de 16 dígitos");
                        throw new ErrorCuentaTDD("El número de tarjeta debe consistir de 16 dígitos");

                    }
                    for (int i = 0; i < ordenAbono.CuentaDestino.Length; i++)
                    {
                        int n = 0;
                        if (!int.TryParse(ordenAbono.CuentaDestino[i].ToString(), out n))
                        {
                            _logger.LogInformation("La cuenta debe ser numérica");
                            throw new ErrorCuentaMalformada("La cuenta debe ser numérica");
                        }
                    }

                    break;
                case "4":
                    if (ordenAbono.CuentaDestino.Length > 18)
                    {
                        _logger.LogInformation("La cuenta no tiene los dígitos indicados");
                        throw new ErrorCuentaMalformada("La cuenta no tiene los dígitos indicados");

                    }
                    for (int i = 0; i < ordenAbono.CuentaDestino.Length; i++)
                    {
                        int n = 0;
                        if (!int.TryParse(ordenAbono.CuentaDestino[i].ToString(), out n))
                        {
                            _logger.LogInformation("La cuenta debe ser numérica");
                            throw new ErrorCuentaMalformada("La cuenta debe ser numérica");
                        }
                    }

                    break;
                case "40":
                    if (ordenAbono.CuentaDestino.Length != 18)
                    {
                        _logger.LogInformation("La CLABE no tiene los dígitos correctos");
                        throw new ErrorCuentaMalformada("La CLABE no tiene los dígitos correctos");
                    }

                    break;
                case "10":
                    if (ordenAbono.CuentaDestino.Length != 10)
                    {
                        _logger.LogInformation("La cuenta MOVIL no tiene los dígitos correctos");
                        throw new ErrorCuentaMalformada("La cuenta MOVIL no tiene los dígitos correctos");
                    }
                    break;
            }
        }



    }
}
