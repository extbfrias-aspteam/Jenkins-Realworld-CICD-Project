using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Api.Azul.Models.Entities;
using Npgsql;
using NpgsqlTypes;

namespace Asp.Api.Azul.Repositorys.AbonoRepository
{
    public class AbonoRepository : BaseRepository, IAbonoRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public AbonoRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }
        public async Task<bool> UpdateRetorno(string cveRastreo, int idEstadoPago, string infoAdicional, DateTime fecha, int idCausaDevolucion)
        {
            return await _resilientExecutor.ExecuteAsync(async ()=>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE abono SET id_estado_pago = @id_estado_pago, fecha_cancelado = @fecha_cancelado, id_causa_devolucion = @id_causa_devolucion  WHERE cve_rastreo = @cverastreo";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_estado_pago", idEstadoPago);
                        command.Parameters.AddWithValue("fecha_cancelado", fecha);
                        command.Parameters.AddWithValue("id_causa_devolucion", idCausaDevolucion);
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);

                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<bool> ExisteAbono(string cveRastreo, int folioPaquete, int folio, DateTime fechaOperacion)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT cve_rastreo FROM abono WHERE cve_rastreo = @cve_rastreo AND folio_paquete = @folio_paquete AND folio = @folio AND fecha_operacion = @fecha_operacion limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("cve_rastreo", cveRastreo);
                        command.Parameters.AddWithValue("folio_paquete", folioPaquete);
                        command.Parameters.AddWithValue("folio", folio);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperacion);
                        var reader = await command.ExecuteReaderAsync();
                        return reader.HasRows;
                    }
                }
            });
        }


        public async Task Insert(Abono abono)
        {
            await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    // var query = GetQueryInsert(abono);
                    var query = "SELECT insert_abono_eiyu(@cveRastreo, @nombreOrigen, @rfcOrigen, @cuentaOrigen, @conceptoPago, @refCob, @refNum, @nombreDestino, @rfcDestino, @cuentaDestino, @fechaOperacion, @fechaCreacion, @firma, @iva, @montoAbono, @idBancoOrigen, @idBancoDestino, @idMovimiento, @idTipoCuantaDestino, @idTipoPago, @idEstadoPago, @folioPaquete, @folio, @fechaBanxico, @idPblu, @idUdn, @Uuid,@IdRetiro);";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.Add(new NpgsqlParameter("@cveRastreo", NpgsqlDbType.Varchar) { Value = abono.CveRastreo });//
                        command.Parameters.Add(new NpgsqlParameter("@nombreOrigen", NpgsqlDbType.Varchar) { Value = abono.NombreOrigen });//
                        command.Parameters.Add(new NpgsqlParameter("@rfcOrigen", NpgsqlDbType.Varchar) { Value = abono.RfcOrigen });//
                        command.Parameters.Add(new NpgsqlParameter("@cuentaOrigen", NpgsqlDbType.Varchar) { Value = abono.CuentaOrigen });//
                        command.Parameters.Add(new NpgsqlParameter("@conceptoPago", NpgsqlDbType.Varchar) { Value = abono.ConceptoPago });//
                        command.Parameters.Add(new NpgsqlParameter("@refCob", NpgsqlDbType.Varchar) { Value = abono.RefCob });//
                        command.Parameters.Add(new NpgsqlParameter("@refNum", NpgsqlDbType.Varchar) { Value = abono.RefNum });//
                        command.Parameters.Add(new NpgsqlParameter("@nombreDestino", NpgsqlDbType.Varchar) { Value = abono.NombreDestino });//
                        command.Parameters.Add(new NpgsqlParameter("@rfcDestino", NpgsqlDbType.Varchar) { Value = abono.RfcDestino });//
                        command.Parameters.Add(new NpgsqlParameter("@cuentaDestino", NpgsqlDbType.Varchar) { Value = abono.CuentaDestino });//
                        command.Parameters.Add(new NpgsqlParameter("@fechaOperacion", NpgsqlDbType.Timestamp) { Value = abono.FechaOperacion });//

                        command.Parameters.Add(new NpgsqlParameter("@fechaCreacion", NpgsqlDbType.Timestamp) { Value = abono.FechaCreacion });//
                        command.Parameters.Add(new NpgsqlParameter("@firma", NpgsqlDbType.Varchar) { Value = abono.Firma });//
                        command.Parameters.Add(new NpgsqlParameter("@iva", NpgsqlDbType.Numeric) { Value = abono.Iva });//
                        command.Parameters.Add(new NpgsqlParameter("@montoAbono", NpgsqlDbType.Numeric) { Value = abono.MontoAbono });//
                        command.Parameters.Add(new NpgsqlParameter("@idBancoOrigen", NpgsqlDbType.Integer) { Value = abono.IdBancoOrigen });//
                        command.Parameters.Add(new NpgsqlParameter("@idBancoDestino", NpgsqlDbType.Integer) { Value = abono.IdBancoDestino });//
                        command.Parameters.Add(new NpgsqlParameter("@idMovimiento", NpgsqlDbType.Integer) { Value = abono.IdMovimiento });//
                        command.Parameters.Add(new NpgsqlParameter("@idTipoCuantaDestino", NpgsqlDbType.Integer) { Value = abono.IdTipoCuentaDestino });//
                        command.Parameters.Add(new NpgsqlParameter("@idTipoPago", NpgsqlDbType.Integer) { Value = abono.IdTipoPago });//
                        command.Parameters.Add(new NpgsqlParameter("@idEstadoPago", NpgsqlDbType.Integer) { Value = abono.IdEstadoPago });//
                        command.Parameters.Add(new NpgsqlParameter("@folioPaquete", NpgsqlDbType.Integer) { Value = abono.FolioPaquete });//
                        command.Parameters.Add(new NpgsqlParameter("@folio", NpgsqlDbType.Integer) { Value = abono.Folio });//
                        command.Parameters.Add(new NpgsqlParameter("@fechaBanxico", NpgsqlDbType.Timestamp) { Value = abono.FechaBanxico });//
                        command.Parameters.Add(new NpgsqlParameter("@idPblu", NpgsqlDbType.Integer) { Value = abono.IdPblu });//
                        command.Parameters.Add(new NpgsqlParameter("@idUdn", NpgsqlDbType.Integer) { Value = abono.IdUdn });//
                        command.Parameters.Add(new NpgsqlParameter("@Uuid", NpgsqlDbType.Varchar) { Value = abono.Uuid });//
                        command.Parameters.Add(new NpgsqlParameter("@IdRetiro", NpgsqlDbType.Varchar) { Value = abono.IdRetiro });//


                        var result = await command.ExecuteScalarAsync();
                        string response = result?.ToString() ?? "Error desconocido";

                        // Verificar si el resultado es "OK"
                        if (response != "OK")
                        {
                            throw new Exception($"Error en la inserción del abono: {response}");
                        }



                    }
                }
            });
        }

        public async Task<Abono?> GetAbonoByCveRastreo(string cveRastreo, DateTime fechaOperaciono)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                Abono? response = null;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT cve_rastreo, id_udn, id_pblu, monto_abono FROM abono WHERE cve_rastreo = @cverastreo AND fecha_operacion = @fecha_operacion  limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperaciono);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            response = new Abono()
                            {
                                CveRastreo = reader.GetString(0),
                                IdUdn = reader.GetValue(1) as int?,
                                IdPblu = reader.GetValue(2) as int?,
                                MontoAbono = reader.GetValue(3) as decimal?
                            };
                        }

                    }
                }

                return response;
            });
        }

        public async Task<bool> UpdateEstadoRetorno(string cveRastreo, int idEstadoPago, string infoAdicional, DateTime fecha, int idEstadoDevolucion, DateTime fechaOperacion)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE abono SET id_causa_devolucion=@idEstadoDevolucion, id_estado_pago = @id_estado_pago, fecha_cancelado = @fecha_cancelado  WHERE cve_rastreo = @cverastreo and fecha_operacion = @fecha_operacion";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_estado_pago", idEstadoPago);
                        command.Parameters.AddWithValue("fecha_cancelado", fecha);
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        command.Parameters.AddWithValue("idEstadoDevolucion", idEstadoDevolucion);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperacion);

                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<DtoDatosUdnAbono> ObtenerDatosUdn(string cuentaClabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                DtoDatosUdnAbono? datosUdn = null;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT u.id_udn, u.pblu FROM udn u INNER JOIN cuenta c ON u.id_udn = c.udn WHERE c.clabe = @Clabe";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("Clabe", cuentaClabe);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            datosUdn = new DtoDatosUdnAbono()
                            {
                                IdUdn = reader.GetInt32(reader.GetOrdinal("id_udn")),
                                IdPblu = reader.GetInt32(reader.GetOrdinal("pblu"))
                            };
                        }

                    }
                }

                return datosUdn ?? new DtoDatosUdnAbono { };
            });
        }
    }
}