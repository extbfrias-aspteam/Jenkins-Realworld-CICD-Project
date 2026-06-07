using Asp.Api.Azul.Entities;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;
using NpgsqlTypes;

namespace Asp.Api.Azul.Repositorys.PagoRepository
{
    public class PagoRepository : BaseRepository, IPagoRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public PagoRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<bool> UpdateRechazado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu, string infoAdicional, DateTime fechaRechazo)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                var fechaOperacion = ObtenerFechaHoras(fechaRechazo);
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE pago SET estado_asp = @estado_asp, estado_pblu = @estado_pblu, id_estado_pago = @id_estado_pago, info_adicional = @info_adicional, fecha_rechazo = @fecha_rechazo,fecha_operacion = @fecha_operacion WHERE cve_rastreo = @cverastreo";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("estado_asp", idEstadoAsp);
                        command.Parameters.AddWithValue("estado_pblu", idEstadoPblu);
                        command.Parameters.AddWithValue("id_estado_pago", idEstadoPago);
                        command.Parameters.AddWithValue("info_adicional", infoAdicional);
                        command.Parameters.AddWithValue("fecha_rechazo", fechaRechazo);
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperacion);

                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<bool> UpdateAceptado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu, string infoAdicional, DateTime fechaAceptado)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                var fechaOperacion = ObtenerFechaHoras(fechaAceptado);
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE pago SET estado_asp = @estado_asp, estado_pblu = @estado_pblu, id_estado_pago = @id_estado_pago, info_adicional = @info_adicional, fecha_aceptado = @fecha_aceptado,fecha_operacion = @fecha_operacion WHERE cve_rastreo = @cverastreo AND id_estado_pago IN (8, 81)";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("estado_asp", idEstadoAsp);
                        command.Parameters.AddWithValue("estado_pblu", idEstadoPblu);
                        command.Parameters.AddWithValue("id_estado_pago", idEstadoPago);
                        command.Parameters.AddWithValue("info_adicional", infoAdicional);
                        command.Parameters.AddWithValue("fecha_aceptado", fechaAceptado);
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperacion);

                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<bool> UpdateCancelado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu, string infoAdicional, DateTime fechaCancelado)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                var fechaOperacion = ObtenerFechaHoras(fechaCancelado);
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE pago SET estado_asp = @estado_asp, estado_pblu = @estado_pblu, id_estado_pago = @id_estado_pago, info_adicional = @info_adicional, fecha_cancelado = @fecha_cancelado,fecha_operacion = @fecha_operacion WHERE cve_rastreo = @cverastreo";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("estado_asp", idEstadoAsp);
                        command.Parameters.AddWithValue("estado_pblu", idEstadoPblu);
                        command.Parameters.AddWithValue("id_estado_pago", idEstadoPago);
                        command.Parameters.AddWithValue("info_adicional", infoAdicional);
                        command.Parameters.AddWithValue("fecha_cancelado", fechaCancelado);
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperacion);

                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<bool> UpdateLiquidado(string cveRastreo, int idEstadoPago, int idEstadoAsp, int idEstadoPblu, string infoAdicional, DateTime fechaLiquidado)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                var fechaOperacion = ObtenerFechaHoras(fechaLiquidado);
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE pago SET estado_asp = @estado_asp, estado_pblu = @estado_pblu, id_estado_pago = @id_estado_pago, info_adicional = @info_adicional, fecha_liquidado = @fecha_liquidado,fecha_operacion = @fecha_operacion WHERE cve_rastreo = @cverastreo";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("estado_asp", idEstadoAsp);
                        command.Parameters.AddWithValue("estado_pblu", idEstadoPblu);
                        command.Parameters.AddWithValue("id_estado_pago", idEstadoPago);
                        command.Parameters.AddWithValue("info_adicional", infoAdicional);
                        command.Parameters.AddWithValue("fecha_liquidado", fechaLiquidado);
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        command.Parameters.AddWithValue("fecha_operacion", fechaOperacion);

                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<Pago?> GetPagoByCveRastreo(string cveRastreo)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                Pago? response = null;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT cve_rastreo, id_udn, id_pblu, monto_cargo FROM pago WHERE cve_rastreo = @cverastreo limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("cverastreo", cveRastreo);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            response = new Pago
                            {
                                CveRastreo = reader.GetString(0),
                                IdUdn = reader.GetValue(1) as int?,
                                IdPblu = reader.GetValue(2) as int?,
                                MontoCargo = reader.GetValue(3) as decimal?
                            };
                        }

                    }
                }

                return response;
            });
        }

        public async Task<int> InsertPagoPl(Pago pago, int folioquete)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                int? respuesta = 0;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "select insertspeioutpl (@cve_rastreo, @cuenta_origen, @concepto_pago, @ref_cob, @ref_num, @nombre_destino, " +
                            "@rfc_destino, @cuenta_destino, @fecha_operacion, @fecha_creacion, @firma, @iva, @monto_cargo, " +
                            "@id_banco_origen, @id_banco_destino, @id_movimiento, @id_tipo_cuanta_destino, @id_tipo_pago, @id_estado_pago, @uuid, " +
                            "@json, @llave, @id_pblu, @id_udn)";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.Add(new NpgsqlParameter("@cve_rastreo", NpgsqlDbType.Varchar) { Value = pago.CveRastreo });
                        command.Parameters.Add(new NpgsqlParameter("@cuenta_origen", NpgsqlDbType.Varchar) { Value = pago.CuentaOrigen });
                        command.Parameters.Add(new NpgsqlParameter("@concepto_pago", NpgsqlDbType.Varchar) { Value = pago.ConceptoPago });
                        command.Parameters.Add(new NpgsqlParameter("@ref_cob", NpgsqlDbType.Varchar) { Value = pago.RefCob });
                        command.Parameters.Add(new NpgsqlParameter("@ref_num", NpgsqlDbType.Varchar) { Value = pago.RefNum });
                        command.Parameters.Add(new NpgsqlParameter("@nombre_destino", NpgsqlDbType.Varchar) { Value = pago.NombreDestino });
                        command.Parameters.Add(new NpgsqlParameter("@rfc_destino", NpgsqlDbType.Varchar) { Value = pago.RfcDestino });
                        command.Parameters.Add(new NpgsqlParameter("@cuenta_destino", NpgsqlDbType.Varchar) { Value = pago.CuentaDestino });
                        command.Parameters.Add(new NpgsqlParameter("@fecha_operacion", NpgsqlDbType.Timestamp) { Value = pago.FechaOperacion });
                        command.Parameters.Add(new NpgsqlParameter("@fecha_creacion", NpgsqlDbType.Timestamp) { Value = pago.FechaCreacion });
                        command.Parameters.Add(new NpgsqlParameter("@firma", NpgsqlDbType.Varchar) { Value = pago.Firma });
                        command.Parameters.Add(new NpgsqlParameter("@iva", NpgsqlDbType.Numeric) { Value = pago.Iva });
                        command.Parameters.Add(new NpgsqlParameter("@monto_cargo", NpgsqlDbType.Numeric) { Value = pago.MontoCargo });
                        command.Parameters.Add(new NpgsqlParameter("@id_banco_origen", NpgsqlDbType.Integer) { Value = pago.IdBancoOrigen });
                        command.Parameters.Add(new NpgsqlParameter("@id_banco_destino", NpgsqlDbType.Integer) { Value = pago.IdBancoDestino });
                        command.Parameters.Add(new NpgsqlParameter("@id_movimiento", NpgsqlDbType.Integer) { Value = pago.IdMovimiento });
                        command.Parameters.Add(new NpgsqlParameter("@id_tipo_cuanta_destino", NpgsqlDbType.Integer) { Value = pago.IdTipoCuentaDestino });
                        command.Parameters.Add(new NpgsqlParameter("@id_tipo_pago", NpgsqlDbType.Integer) { Value = pago.IdTipoPago });
                        command.Parameters.Add(new NpgsqlParameter("@id_estado_pago", NpgsqlDbType.Integer) { Value = pago.IdEstadoPago });
                        command.Parameters.Add(new NpgsqlParameter("@uuid", NpgsqlDbType.Varchar) { Value = pago.Uuid });
                        command.Parameters.Add(new NpgsqlParameter("@json", NpgsqlDbType.Varchar) { Value = pago.Json });
                        command.Parameters.Add(new NpgsqlParameter("@llave", NpgsqlDbType.Integer) { Value = pago.Llave });
                        command.Parameters.Add(new NpgsqlParameter("@id_pblu", NpgsqlDbType.Integer) { Value = pago.IdPblu });
                        command.Parameters.Add(new NpgsqlParameter("@id_udn", NpgsqlDbType.Integer) { Value = pago.IdUdn });
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            respuesta = reader.GetValue(0) as int?;
                        }

                    }
                }
                return respuesta ?? 0;
            });
        }

        public async Task<InsertTraspasoResponse?> InsertTraspaso(Pago pago, string nombreOrdenante, string UuidAbono)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                InsertTraspasoResponse? response = null;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "select* from generar_traspaso_eiyu (@cve_rastreo, @cuenta_origen, @concepto_pago, @ref_cob, @ref_num, @nombre_destino, " +
                            "@rfc_destino, @cuenta_destino, @fecha_operacion, @fecha_creacion, @firma, @iva, @monto_cargo, " +
                            "@id_banco_origen, @id_banco_destino, @id_movimiento, @id_tipo_cuanta_destino, @id_tipo_pago, @id_estado_pago, @uuid, " +
                            "@json, @llave, @id_pblu, @id_udn,@NombreOrdenante,@fechaBanxico,@UuidAbono)";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.Add(new NpgsqlParameter("@cve_rastreo", NpgsqlDbType.Varchar) { Value = pago.CveRastreo });
                        command.Parameters.Add(new NpgsqlParameter("@cuenta_origen", NpgsqlDbType.Varchar) { Value = pago.CuentaOrigen });
                        command.Parameters.Add(new NpgsqlParameter("@concepto_pago", NpgsqlDbType.Varchar) { Value = pago.ConceptoPago });
                        command.Parameters.Add(new NpgsqlParameter("@ref_cob", NpgsqlDbType.Varchar) { Value = pago.RefCob });
                        command.Parameters.Add(new NpgsqlParameter("@ref_num", NpgsqlDbType.Varchar) { Value = pago.RefNum });
                        command.Parameters.Add(new NpgsqlParameter("@nombre_destino", NpgsqlDbType.Varchar) { Value = pago.NombreDestino });
                        command.Parameters.Add(new NpgsqlParameter("@rfc_destino", NpgsqlDbType.Varchar) { Value = pago.RfcDestino });
                        command.Parameters.Add(new NpgsqlParameter("@cuenta_destino", NpgsqlDbType.Varchar) { Value = pago.CuentaDestino });
                        command.Parameters.Add(new NpgsqlParameter("@fecha_operacion", NpgsqlDbType.Timestamp) { Value = pago.FechaOperacion });
                        command.Parameters.Add(new NpgsqlParameter("@fecha_creacion", NpgsqlDbType.Timestamp) { Value = pago.FechaCreacion });
                        command.Parameters.Add(new NpgsqlParameter("@firma", NpgsqlDbType.Varchar) { Value = pago.Firma });
                        command.Parameters.Add(new NpgsqlParameter("@iva", NpgsqlDbType.Numeric) { Value = pago.Iva });
                        command.Parameters.Add(new NpgsqlParameter("@monto_cargo", NpgsqlDbType.Numeric) { Value = pago.MontoCargo });
                        command.Parameters.Add(new NpgsqlParameter("@id_banco_origen", NpgsqlDbType.Integer) { Value = pago.IdBancoOrigen });
                        command.Parameters.Add(new NpgsqlParameter("@id_banco_destino", NpgsqlDbType.Integer) { Value = pago.IdBancoDestino });
                        command.Parameters.Add(new NpgsqlParameter("@id_movimiento", NpgsqlDbType.Integer) { Value = pago.IdMovimiento });
                        command.Parameters.Add(new NpgsqlParameter("@id_tipo_cuanta_destino", NpgsqlDbType.Integer) { Value = pago.IdTipoCuentaDestino });
                        command.Parameters.Add(new NpgsqlParameter("@id_tipo_pago", NpgsqlDbType.Integer) { Value = pago.IdTipoPago });
                        command.Parameters.Add(new NpgsqlParameter("@id_estado_pago", NpgsqlDbType.Integer) { Value = pago.IdEstadoPago });
                        command.Parameters.Add(new NpgsqlParameter("@uuid", NpgsqlDbType.Varchar) { Value = pago.Uuid });
                        command.Parameters.Add(new NpgsqlParameter("@json", NpgsqlDbType.Varchar) { Value = pago.Json });
                        command.Parameters.Add(new NpgsqlParameter("@llave", NpgsqlDbType.Integer) { Value = pago.Llave });
                        command.Parameters.Add(new NpgsqlParameter("@id_pblu", NpgsqlDbType.Integer) { Value = pago.IdPblu });
                        command.Parameters.Add(new NpgsqlParameter("@id_udn", NpgsqlDbType.Integer) { Value = pago.IdUdn });
                        //Abono
                        command.Parameters.Add(new NpgsqlParameter("@NombreOrdenante", NpgsqlDbType.Varchar) { Value = nombreOrdenante });
                        command.Parameters.Add(new NpgsqlParameter("@UuidAbono", NpgsqlDbType.Varchar) { Value = UuidAbono });
                        command.Parameters.Add(new NpgsqlParameter("@fechaBanxico", NpgsqlDbType.Timestamp)
                        {
                            Value = DateTime.SpecifyKind(DateTimeOffset.UtcNow.DateTime, DateTimeKind.Unspecified)
                        });


                        var reader = await command.ExecuteReaderAsync();
                        if (await reader.ReadAsync())
                        {
                            response = new InsertTraspasoResponse
                            {
                                Mensaje = reader.GetString(0),
                                IdPbluDestino = reader.GetInt32(1),
                                CloudDestino = reader.IsDBNull(2) ? "" : reader.GetString(2)


                            };

                        }

                    }
                }
                return response;
            });
        }
        public async Task<ValidaPago> GetValidacionPago(int idPblu, string cveRastreo, int bancoDestino, string clabeOrigen, bool isPortal, decimal monto, string ctaDestino)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                ValidaPago? response = null;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT * from ValidacionPagoSpeiOut(@idPblu,@cveRastreo,@bancoDestino,@clabeOrigen,@isPortal,@monto,@ctaDestino)";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("idPblu", idPblu);
                        command.Parameters.AddWithValue("cveRastreo", cveRastreo);
                        command.Parameters.AddWithValue("bancoDestino", bancoDestino);
                        command.Parameters.AddWithValue("clabeOrigen", clabeOrigen);
                        command.Parameters.AddWithValue("isPortal", isPortal);
                        command.Parameters.AddWithValue("monto", monto);
                        command.Parameters.AddWithValue("ctaDestino", ctaDestino);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var nombre = reader.GetValue(5)?.ToString();
                            var apellidoPaterno = reader.GetValue(6)?.ToString();
                            var apellidoMaterno = reader.GetValue(7)?.ToString();
                            var rfc = reader.GetValue(8)?.ToString();
                            var mensaje = reader.GetValue(2)?.ToString();
                            mensaje = string.IsNullOrEmpty(mensaje) ? "" : mensaje;
                            apellidoPaterno = string.IsNullOrEmpty(apellidoPaterno) ? "" : $" {apellidoPaterno}";
                            apellidoMaterno = string.IsNullOrEmpty(apellidoMaterno) ? "" : $" {apellidoMaterno}";
                            var nombreOrdenante = $"{nombre}{apellidoPaterno}{apellidoMaterno}";
                            nombreOrdenante = string.IsNullOrEmpty(nombreOrdenante) ? "ND" : nombreOrdenante;
                            rfc = string.IsNullOrEmpty(rfc) ? "ND" : rfc;
                            // return $"{nombreOrdenante.Replace(",", "")},{rfc.Replace(",", "")}";
                            response = new ValidaPago
                            {
                                Success = reader.GetBoolean(0),
                                IdMensaje = (int)reader.GetValue(1),
                                Mensaje = mensaje,
                                IdUdn = (int)(reader.GetValue(3) ?? 0), //validar donde sea null, asignarle un 0
                                Proveedor = reader.GetString(4),
                                NombreWithRFC = $"{nombreOrdenante.Replace(",", "")},{rfc.Replace(",", "")}"

                            };
                        }

                    }
                }

                return response ?? new ValidaPago();
            });
        }

        public async Task<bool> InsertMultiplesEstados(List<string> ClavesDeRastreo, int IdEstadoPago)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = $"UPDATE pago set id_estado_pago=@IdEstadoPago WHERE (cve_rastreo = ANY (@ClavesToUpdate)) and id_estado_pago=@IdEstadoPagoAnterior";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("ClavesToUpdate", ClavesDeRastreo.ToArray());
                        command.Parameters.AddWithValue("IdEstadoPago", IdEstadoPago);
                        command.Parameters.AddWithValue("IdEstadoPagoAnterior", EstadosAsp.EnProceso);
                        return await command.ExecuteNonQueryAsync() > 0;

                    }
                }
            });
        }
      
        private DateTime ObtenerFechaHoras(DateTime fecha)
        {
            return fecha.Date;
        }

        public async Task<DatosCuentaPrevFraudeDto?> ObtenerDatosCuenta(string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT * FROM obtener_info_cuenta_alerta(@Clabe);";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("Clabe", clabe);
                        await using var reader = await command.ExecuteReaderAsync();

                        DatosCuentaPrevFraudeDto datos = null;
                        if (await reader.ReadAsync())
                        {
                            string GetSafeString(string columnName)
                            {
                                var ordinal = reader.GetOrdinal(columnName);
                                return reader.IsDBNull(ordinal) || string.IsNullOrWhiteSpace(reader.GetString(ordinal))
                                    ? "NA"
                                    : reader.GetString(ordinal);
                            }

                            datos = new DatosCuentaPrevFraudeDto
                            {
                                Clabe = reader.GetString(reader.GetOrdinal("referencia_clabe")),
                                FechaAltaClabe = reader.GetDateTime(reader.GetOrdinal("fecha_alta_clabe")),
                                TitularNombre = GetSafeString("titular_nombre"),
                                TitularTelefono = GetSafeString("titular_telefono"),
                                TitularCorreo = GetSafeString("titular_correo"),
                                FechaOperacion = DateTime.Now,
                                Monto = 0m
                            };
                        }

                        return datos;
                    }
                }
            });
        }
    }
}