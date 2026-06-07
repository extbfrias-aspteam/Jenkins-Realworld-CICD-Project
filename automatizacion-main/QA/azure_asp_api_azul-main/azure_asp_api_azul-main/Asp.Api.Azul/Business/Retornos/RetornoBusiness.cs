using Asp.Api.Azul.Business.Abono;
using Asp.Api.Azul.Entities;
using Asp.Api.Azul.Kafka.Dtos.SpeiRetornos;
using Asp.Api.Azul.Kafka.Dtos.Traspasos;
using Asp.Api.Azul.Kafka.Producer;
using Asp.Api.Azul.Kafka.Topics;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Repositorys.CuentaRepository;
using Asp.Api.Azul.Repositorys.RetornoRepository;
using Confluent.Kafka;
using Microsoft.Extensions.Logging;
using Microsoft.IdentityModel.Tokens;
using System.Text;

namespace Asp.Api.Azul.Business.Retornos
{
    public class RetornoBusiness : IRetornoBusiness
    {
        private readonly IRetornoRepository _retornoRepository;
        private readonly IAbonoBusiness _abonoBusiness;
        private readonly ICuentaRepository _cuentaRepository;
        private readonly KafkaProducerService _kafkaProducer;
        public RetornoBusiness(IRetornoRepository retornoRepository, IAbonoBusiness abonoBusiness, ICuentaRepository cuentaRepository, KafkaProducerService kafkaProducer)
        {
            _retornoRepository = retornoRepository;
            _abonoBusiness = abonoBusiness;
            _cuentaRepository = cuentaRepository;
            _kafkaProducer = kafkaProducer;
        }
        public async Task InsertarRetornoAsync(RetornoRequest request, int id_pblu, string IDENTIFICADOR)
        {
            try
            {
                GenerarLog(IDENTIFICADOR, "InsertarRetornoAsync", $"Inicia validaciones parametros");
                if (string.IsNullOrEmpty(request.CausaDevolucion.ToString())) throw new Exception($"El campo causaDev es obligatorio.");
                if (string.IsNullOrEmpty(request.Uuid)) throw new Exception($"El campo Uuid es obligatorio.");
                if (string.IsNullOrEmpty(request.ClaveRastreo)) throw new Exception($"El campo claveRastreo es obligatorio");
                GenerarLog(IDENTIFICADOR, "InsertarRetornoAsync", $"Finaliza validaciones parametros");

                GenerarLog(IDENTIFICADOR, "InsertarRetornoAsync", $"Obtiene los datos del retorno con clave de rastreo: {request.ClaveRastreo} , idPblu: {id_pblu} , uuid: {request.Uuid}");
                var datosRetorno = await _retornoRepository.ObtenerDatosRetorno(request.ClaveRastreo, id_pblu, request.Uuid);

                if (datosRetorno == null)
                {
                    GenerarLog(IDENTIFICADOR, "InsertarRetornoAsync", $"Datos del retorno null");
                    throw new Exception($"No se han encontrado resultados con clave de rastreo: {request.ClaveRastreo} y el uuid: {request.Uuid}");
                }

                GenerarLog(IDENTIFICADOR, "InsertarRetornoAsync", $"Inicia verificacion del id de estado pago: {datosRetorno.IdEstadoPago}");
                if (datosRetorno.IdEstadoPago == EstadosAsp.ABONO_RETORNADO) throw new Exception($"La operación con clave de rastreo: {request.ClaveRastreo} ya ha sido retornada.");

                if (datosRetorno.IdEstadoPago == EstadosAsp.ABONO_POR_RETORNAR) throw new Exception($"La operación con clave de rastreo: {request.ClaveRastreo} esta por ser retornada.");

                if (datosRetorno.IdTipoPago == 1 || datosRetorno.IdTipoPago == 30)
                {
                    GenerarLog(IDENTIFICADOR, "InsertarRetornoAsync", $"El retorno sera de un pago tipo: {datosRetorno.IdTipoPago}");

                   
                    //Significa que es un abono de otro banco
                    var message = new SpeiRetornoSiesMotorDto
                    {
                        CuentaOrdenante = datosRetorno.CuentaOrdenante,
                        CuentaBeneficiario = datosRetorno.CuentaBeneficiario,
                        Monto = datosRetorno.Monto,
                        ClaveRastreo = datosRetorno.ClaveRastreo,
                        DevolucionId = request.CausaDevolucion.ToString(),
                        ConceptoPago = datosRetorno.ConceptoPago,
                        NombreOrdenante = datosRetorno.NombreOrdenante,
                        NombreBeneficiario = datosRetorno.NombreBeneficiario,
                        FechaOperacion = datosRetorno.FechaOperacion,
                        Folio = datosRetorno.Folio,
                        FolioPaquete = datosRetorno.FolioPaquete,
                        IdPblu = datosRetorno.IdPblu,
                        BancoOrigen = datosRetorno.BancoOrigen,
                        ConceptoOriginal = datosRetorno.ConceptoOriginal,
                        RefNum = datosRetorno.RefNum,
                    };
                    await _kafkaProducer.EnviarMensajeAsync(Topicos.RetornoSIES, message);

                    
                }
                else
                {
                    throw new Exception("Operación no permitida. Los retornos únicamente aplican para pagos tipo 1.");
                }



            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                throw;
            }
        }
        private void GenerarLog(string timestamp, string metodo, string text)
        {
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.ffffff");
            Console.WriteLine($"{current_time_formatt} INFO [{metodo} - {timestamp}] -> {text}");

        }
    }
}
