using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Api.Azul.Models.Entities;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.RetornoRepository
{
    public class RetornoRepository : IRetornoRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public RetornoRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<DtoRetorno?> ObtenerDatosRetorno(string claveRastreo, int id_pblu, string uuid)
        {
            DtoRetorno? datosRetornos = null;
            return await _resilientExecutor.ExecuteAsync(async () => {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {


                    var query = "select ua.uuid,ua.id_retiro ,ab.cuenta_origen,ab.cuenta_destino ,ab.monto_abono,ab.cve_rastreo,ab.nombre_origen ,ab.nombre_destino,ab.fecha_operacion ,ab.folio ,ab.folio_paquete,ab.id_pblu ,ab.id_banco_origen ,ab.concepto_pago ,ab.ref_num,ab.id_tipo_pago,ab.id_estado_pago from abono ab inner join uuid_abonos ua on ab.cve_rastreo =ua.clave_rastreo  where ab.cve_rastreo =@ClaveRastreo and ab.id_pblu=@IdPblu and ua.uuid=@Uuid";


                    await connection.OpenAsync();

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("ClaveRastreo", claveRastreo);
                        command.Parameters.AddWithValue("IdPblu", id_pblu);
                        command.Parameters.AddWithValue("Uuid", uuid);
                        var reader = await command.ExecuteReaderAsync();

                        if (await reader.ReadAsync())
                        {

                            datosRetornos = new DtoRetorno
                            {
                                CuentaOrdenante = reader.GetString(reader.GetOrdinal("cuenta_origen")),
                                CuentaBeneficiario = reader.GetString(reader.GetOrdinal("cuenta_destino")),
                                Monto = reader.GetDecimal(reader.GetOrdinal("monto_abono")).ToString(),
                                ClaveRastreo = reader.GetString(reader.GetOrdinal("cve_rastreo")),
                                DevolucionId = "0", //es 0 porque el registro nace con devolución null
                                BancoOrigen = reader.GetInt32(reader.GetOrdinal("id_banco_origen")).ToString(),
                                ConceptoPago = "DEVOLUCION",
                                NombreOrdenante = reader.GetString(reader.GetOrdinal("nombre_origen")),
                                NombreBeneficiario = reader.GetString(reader.GetOrdinal("nombre_destino")),
                                FechaOperacion = reader.GetDateTime(reader.GetOrdinal("fecha_operacion")).ToString("MM/dd/yyyy"),//.ToString("MM/dd/yyyy"),
                                Folio = reader.GetInt32(reader.GetOrdinal("folio")).ToString(),
                                FolioPaquete = reader.GetInt32(reader.GetOrdinal("folio_paquete")).ToString(),
                                IdPblu = reader.GetInt32(reader.GetOrdinal("id_pblu")),
                                ConceptoOriginal = reader.GetString(reader.GetOrdinal("concepto_pago")),
                                RefNum = reader.GetString(reader.GetOrdinal("ref_num")),
                                IdTipoPago = reader.GetInt32(reader.GetOrdinal("id_tipo_pago")),
                                IdEstadoPago = reader.GetInt32(reader.GetOrdinal("id_estado_pago")),
                                IdRetiro = reader.GetString(reader.GetOrdinal("id_retiro")),
                            };

                        }
                    }
                    return datosRetornos;
                }
            });
        }
    }
}