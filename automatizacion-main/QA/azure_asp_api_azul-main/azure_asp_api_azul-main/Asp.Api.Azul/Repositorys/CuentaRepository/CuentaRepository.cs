using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.CuentaRepository
{
    public class CuentaRepository : BaseRepository, ICuentaRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public CuentaRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<Cuenta?> GetByClabe(string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT clabe,uuid,estado,token,activo,fecha_creacion,usuario_creacion,id_persona,asp_cuenta,asp_id_cuenta,id_estatus_ahorro,udn,nivel,blu_black_list,blu_limite,monto_permitido,pblu,fecha_actualizacion,usuario_actualizacion,no_notificar_abono,actualizar,info_adicional, bloqueo FROM cuenta WHERE clabe = @clabe limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("clabe", clabe);
                        var reader = await command.ExecuteReaderAsync();
                        if (await reader.ReadAsync())
                        {
                            var cuenta = new Entities.DataBase.Cuenta
                            {
                                Clabe = reader.GetString(0),
                                Uuid = reader.GetValue(1)?.ToString(),
                                Estado = reader.GetString(2),
                                Token = reader.GetValue(3)?.ToString(),
                                Activo = reader.GetBoolean(4),
                                FechaCreacion = reader.GetValue(5) as DateTime?,
                                UsuarioCreacion = reader.GetValue(6)?.ToString(),
                                IdPersona = reader.GetValue(7) as int?,
                                AspCuenta = reader.GetValue(8)?.ToString(),
                                AspIdCuenta = reader.GetValue(9) as int?,
                                IdEstatusAhorro = reader.GetValue(10) as int?,
                                Udn = reader.GetInt32(11),
                                Nivel = reader.GetValue(12) as int?,
                                BluBlackList = reader.GetValue(13) as bool?,
                                BluMontoLimite = reader.GetValue(14) as decimal?,
                                MontoPermitido = reader.GetValue(15) as bool?,
                                Pblu = reader.GetValue(16) as int?,
                                FehaActualizacion = reader.GetValue(17) as DateTime?,
                                UsuarioActualizacion = reader.GetValue(18)?.ToString(),
                                NoNotificarAbono = reader.GetValue(19) as bool?,
                                Actualizar = reader.GetValue(20) as int?,
                                InfoAdicional = reader.GetValue(21)?.ToString()
                            };
                            return cuenta;
                        }
                    }

                    return null;
                }
            });
        }

        public async Task<bool> IsActiveDigitalizacionPblu(int id_pblu)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    connection.Open();
                    var query = "SELECT bandera_digitalizacion_documentos FROM pblu WHERE id_pblu = @id_pblu LIMIT 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_pblu", id_pblu);

                        using (var reader = command.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                // Retorna true o false si el campo no es NULL
                                if (!reader.IsDBNull(0))
                                {
                                    return reader.GetBoolean(0);
                                }

                                // Si el valor es NULL, puedes decidir qué hacer
                                throw new InvalidOperationException("El campo bandera_digitalizacion_documentos es NULL.");
                            }
                        }
                    }
                }

                // Si no se encuentra ningún registro, lanza una excepción o devuelve un valor predeterminado
                throw new KeyNotFoundException($"No se encontró ningún registro con id_pblu = {id_pblu}.");
            });
        }

        public bool GetCamposObligatorios()
        {
            using var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection"));
            connection.Open();
            var query = "SELECT CASE WHEN valor = '1' THEN TRUE ELSE FALSE END FROM parametros_api WHERE descripcion = 'Parametros_Obligatorios'";
            using var command = new NpgsqlCommand(query, connection);
            return (bool)command.ExecuteScalar();
        }

        public async Task<bool> Existe(string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT clabe FROM cuenta WHERE clabe = @clabe limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("clabe", clabe);
                        var reader = await command.ExecuteReaderAsync();
                        return reader.HasRows;
                    }
                }
            });
        }

        public async Task<int> Insert(Cuenta cuenta)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(cuenta);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(cuenta));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        public async Task<bool> ActualizaEstado(Cuenta cuenta)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE cuenta SET estado = @estado WHERE clabe = @clabe";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("estado", cuenta.Estado);
                        command.Parameters.AddWithValue("clabe", cuenta.Clabe);

                        var res = await command.ExecuteNonQueryAsync();

                        return res > 0;
                    }
                }
            });
        }

        public async Task<string> GenerarCuentaClabe(int idPblu, int idUdn)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                var clabeResult = string.Empty;
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT public.crear_cuenta_clabe(@id_pblu, @id_udn)";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_pblu", idPblu);
                        command.Parameters.AddWithValue("id_udn", idUdn);

                        using (var result = await command.ExecuteReaderAsync())
                        {
                            if (result.HasRows)
                            {
                                await result.ReadAsync(); // Lee la primera fila
                                clabeResult = result.GetValue(0)?.ToString();
                                await Console.Out.WriteLineAsync($"Cuenta clabe generada {clabeResult}");
                            }
                            else
                            {
                                throw new Exception("La función retornó una clabe null.");
                            }
                        }
                    }
                }
                return clabeResult ?? string.Empty;
            });
        }

        public async Task<bool> VerificarCuentaByPblu(int idPblu,string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT EXISTS ( SELECT 1 FROM cuenta WHERE clabe = @Clabe AND pblu = @IdPblu)";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("Clabe", clabe);
                        command.Parameters.AddWithValue("IdPblu", idPblu);
                        bool exists = (bool)await command.ExecuteScalarAsync();
                        return exists;
                    }
                }
            });
        }

        public async Task<bool> ActivarCuentaClabe(string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE cuenta SET activo = true WHERE clabe = @clabe";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("clabe", clabe);
                        var rowsAffected = await command.ExecuteNonQueryAsync();
                        return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
                    }
                }
            });
        }
    }
}