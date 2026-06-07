using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Repositorys.HistorialSaldoPbluRepository;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.UdnRepository
{
    public class UdnRepository : BaseRepository, IUdnRepository
    {

        private readonly IConfiguration _configuration;
        private readonly IHistorialSaldoPbluRepository _historialSaldoPblu;
        private readonly ResilientExecutor _resilientExecutor;

        public UdnRepository(IConfiguration configuration, IHistorialSaldoPbluRepository historialSaldoPblu, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _historialSaldoPblu = historialSaldoPblu;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<Udn?> GetById(int idUdn)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    //consultar prefijo_clabe y contador_clabe de udn_clabe
                    await connection.OpenAsync();
                    var query = "SELECT u.id_udn, descripcion, saldo, activo, fecha_creacion, usuario_creacion, pblu, saldo_min, notificacion_activa, uc.prefijo_clabe, uc.contador_clabe, clabe, monto_limite, bloqueado FROM udn u, udn_clabe uc WHERE u.id_udn = @id_udn and u.id_udn = uc.id_udn limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_udn", idUdn);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var udn = new Entities.DataBase.Udn
                            {
                                IdUdn = reader.GetInt32(0),
                                Descripcion = reader.GetString(1),
                                Saldo = reader.GetDecimal(2),
                                Activo = reader.GetBoolean(3),
                                FechaCreacion = reader.GetValue(4) as DateTime?,
                                UsuarioCreacion = reader.GetValue(5)?.ToString(),
                                Pblu = reader.GetInt32(6),
                                SaldoMin = reader.GetDecimal(7),
                                NotificacionActiva = reader.GetBoolean(8),
                                PrefijoClabe = reader.GetValue(9)?.ToString(),
                                ContadorClabe = reader.GetValue(10) as int?,
                                Clabe = reader.GetValue(11)?.ToString(),
                                MontoLimite = reader.GetValue(12) as decimal?
                            };
                            return udn;
                        }
                    }

                    return null;
                }
            });
        }

        public async Task<Udn?> GetByClabe(string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    //consultar prefijo_clabe y contador_clabe de udn_clabe
                    await connection.OpenAsync();
                    var query = "SELECT u.id_udn, descripcion, saldo, activo, fecha_creacion, usuario_creacion, pblu, saldo_min, notificacion_activa, uc.prefijo_clabe, uc.contador_clabe, clabe, monto_limite FROM udn u, udn_clabe uc WHERE clabe = @clabe and u.id_udn = uc.id_udn limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("clabe", clabe);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var udn = new Entities.DataBase.Udn
                            {
                                IdUdn = reader.GetInt32(0),
                                Descripcion = reader.GetString(1),
                                Saldo = reader.GetDecimal(2),
                                Activo = reader.GetBoolean(3),
                                FechaCreacion = reader.GetValue(4) as DateTime?,
                                UsuarioCreacion = reader.GetValue(5)?.ToString(),
                                Pblu = reader.GetInt32(6),
                                SaldoMin = reader.GetDecimal(7),
                                NotificacionActiva = reader.GetBoolean(8),
                                PrefijoClabe = reader.GetValue(9)?.ToString(),
                                ContadorClabe = reader.GetValue(10) as int?,
                                Clabe = reader.GetValue(11)?.ToString(),
                                MontoLimite = reader.GetValue(12) as decimal?
                            };
                            return udn;
                        }
                    }

                    return null;
                }
            });
        }

        public async Task<List<Udn>> GetByIdPblu(int idPblu)
        {
            List<Udn> response = new List<Udn>();
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    //consultar prefijo_clabe y contador_clabe de udn_clabe
                    await connection.OpenAsync();
                    var query = "SELECT u.id_udn, descripcion, saldo, activo, fecha_creacion, usuario_creacion, pblu, saldo_min, notificacion_activa, uc.prefijo_clabe, uc.contador_clabe, clabe, monto_limite FROM udn u, udn_clabe uc WHERE pblu = @pblu and u.id_udn = uc.id_udn";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("pblu", idPblu);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            response.Add(new Entities.DataBase.Udn
                            {
                                IdUdn = reader.GetInt32(0),
                                Descripcion = reader.GetString(1),
                                Saldo = reader.GetDecimal(2),
                                Activo = reader.GetBoolean(3),
                                FechaCreacion = reader.GetValue(4) as DateTime?,
                                UsuarioCreacion = reader.GetValue(5)?.ToString(),
                                Pblu = reader.GetInt32(6),
                                SaldoMin = reader.GetDecimal(7),
                                NotificacionActiva = reader.GetBoolean(8),
                                PrefijoClabe = reader.GetValue(9)?.ToString(),
                                ContadorClabe = reader.GetValue(10) as int?,
                                Clabe = reader.GetValue(11)?.ToString(),
                                MontoLimite = reader.GetValue(12) as decimal?
                            });
                        }
                    }

                    return response;
                }
            });
        }

        public async Task AumentaSaldo(string claveRastreo,int idUdn, decimal monto)
        {
            DateTime currentTime = DateTime.Now;
            string IDENTIFICADOR = "ID_" + currentTime.ToString("yyyyMMddHHmmssffffff");
            Console.WriteLine($"[AumentaSaldo]- {IDENTIFICADOR}: {claveRastreo}");
            await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT aumenta_saldo_udn(@claveRastreo,@idUdn, @monto)";
                    using (var command = new NpgsqlCommand(query, connection))
                    {

                        command.Parameters.AddWithValue("claveRastreo", claveRastreo);
                        command.Parameters.AddWithValue("idUdn", idUdn);
                        command.Parameters.AddWithValue("monto", monto);

                        var result = await command.ExecuteScalarAsync();
                        string response = result?.ToString() ?? "Error desconocido";

                        // Verificar si el resultado es "OK"
                        if (response != "OK")
                            throw new Exception($"Error en  aumentar el saldo de la UDN: {response}");
                    }
                }
            });
        }

        public async Task<bool> DisminuyeSaldo(int idUdn, decimal monto)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE udn SET saldo = saldo - @monto WHERE id_udn = @idUdn";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("monto", monto);
                        command.Parameters.AddWithValue("idUdn", idUdn);
                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<string> GetClabe(int idUdn)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT clabe FROM udn WHERE id_udn = @id_udn";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_udn", idUdn);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var clabe = reader.GetValue(0) as string;
                            return clabe ?? "";
                        }
                    }
                }
                return "";
            });
        }

        public async Task<bool> UpdateClabe(int idUdn, string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE udn SET clabe = @clabe WHERE id_udn = @idUdn";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("clabe", clabe);
                        command.Parameters.AddWithValue("idUdn", idUdn);
                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<Dictionary<string, object>> InsertarUDN(UDN udn, int IdPblue, string Username)
        {
            Dictionary<string, object> response = new Dictionary<string, object>();
            int idUdn = await ExisteDescripcionUdn(udn.Descripcion, IdPblue);
            if (idUdn != -1)
            {
                Console.WriteLine($"idUdn !=-1 {idUdn}");
                response["Estado"] = true;
                response.Add("id_udn", idUdn.ToString("D3"));
                return response;
            }

            string prefijo_clabe = await GetLastClabe(IdPblue, udn.Descripcion);
            bool esNumero = int.TryParse(prefijo_clabe, out int resultado);

            response.Add("Estado", true);
            response.Add("Exception", "");
            try
            {
                string descripcion = udn.Descripcion;
                decimal saldo = 0;
                DateTime fecha_creacion = DateTime.Now;
                UDN newUDN = new()
                {
                    //viene del endpoint
                    Descripcion = descripcion,
                    //default
                    Saldo = saldo,
                    //default
                    Activo = true,
                    //default
                    Fecha_creacion = fecha_creacion,
                    //usuario se extrae del jwt
                    Usuario_creacion = Username,
                    // este pblu se extrae del jwt
                    Pblu = IdPblue,
                    //default
                    Saldo_min = 100,
                    //default
                    Notificacion_activa = true,
                    //default
                    Prefijo_clabe = null, //prefijo_clabe, //AHORA SE GUARDA EN UDN_CLABE
                    //default
                    Contador_clabe = null, //0, //AHORA SE GUARDA EN UDN_CLABE
                    //default
                    Clabe = "",
                    //default
                    Monto_limite = 300000
                };

                return await _resilientExecutor.ExecuteAsync(async () =>
                {
                    using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                    {
                        await connection.OpenAsync();
                        var query = GetQueryInsert(newUDN) + " RETURNING id_udn";
                        using (var command = new NpgsqlCommand(query, connection))
                        {
                            command.Parameters.AddRange(GetParametersInsert(newUDN));
                            var insertedId = await command.ExecuteScalarAsync();
                            if (insertedId != null && int.TryParse(insertedId.ToString(), out int id_udn))
                            {

                                HistorialSaldoPblu nuevoHistorial = new()
                                {
                                    IdUdn = id_udn,
                                    FechaCreacion = fecha_creacion,
                                    FechaOperativa = FechaOperativaSencilla(),
                                    SaldoInicial = 0,
                                    SaldoFinal = 0,
                                    UsuarioCreacion = Username

                                };
                                await _historialSaldoPblu.Insert(nuevoHistorial);

                                response["Estado"] = true;
                                response.Add("id_udn", id_udn);

                                //insertar la udn en udn_clabe
                                UDN_CLABE udnClabe = new() { Id_udn = id_udn, Contador_clabe = 0, Prefijo_clabe = prefijo_clabe };
                                var query_udn = GetQueryInsert(udnClabe);
                                command.Parameters.Clear();
                                command.CommandText = query_udn;
                                command.Parameters.AddRange(GetParametersInsert(udnClabe));
                                await command.ExecuteNonQueryAsync();

                                return response;

                            }
                            throw new Exception("No se ha podido insertar en la base de datos.");
                        }
                    }
                });
            }
            catch (Exception ex)
            {
                response["Estado"] = false;
                response["Exception"] = ex;

                return response;
            }
        }


        public async Task<string> GetLastClabe(int IdPblue, string descripcion)
        {
            int idAssert = 39; //39 assert
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    //consultar prefijo_clabe y contador_clabe de udn_clabe
                    await connection.OpenAsync();
                    var query = "SELECT MAX(uc.prefijo_clabe::int)+1 as prefijo_clabe from udn u, udn_clabe uc where pblu=@IdPblue and u.id_udn = uc.id_udn";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@IdPblue", IdPblue);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            if (reader.IsDBNull(0))
                            {
                                Console.WriteLine("el prefijo clabe es null, se retornara 001");
                                return "001";
                            }

                            int prefijoClabe = reader.GetInt32(0); // Obtener el valor de la columna prefijo_clabe
                            if (IdPblue == idAssert)
                            {
                                Console.WriteLine("IdPblue == idAssert");
                                if (prefijoClabe > 99999)
                                    throw new Exception("Has superado el límite de creaciones de UDN.");

                                if (prefijoClabe == 1001)
                                {
                                    Console.WriteLine("prefijoClabe == 1001");
                                    prefijoClabe = 1759;
                                }
                            }

                            if (IdPblue != idAssert && prefijoClabe > 999)
                                throw new Exception("Has superado el límite de creaciones de UDN.");

                            return prefijoClabe.ToString("D3");
                        }
                    }
                }
                return "001";
            });
        }

        public async Task<int> ExisteDescripcionUdn(string descripcion, int idpblu)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT max(id_udn) FROM udn WHERE descripcion =@descripcion and pblu=@idpblu and activo=true";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("@descripcion", descripcion);
                        command.Parameters.AddWithValue("@idpblu", idpblu);

                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            if (reader.IsDBNull(0))
                            {
                                Console.WriteLine("Esta descripción no ha sido registrada");
                                return -1;
                            }
                            else
                            {
                                Console.WriteLine("Esta descripción ya esta registrada, se retorna el id de la udn");
                                return reader.GetInt32(0);
                            }
                        }
                    }
                }

                return -1;
            });
        }

        public async Task<bool> VerificaUdnByIdPblu(int idPblu, int id_udn)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT EXISTS ( SELECT 1 FROM udn WHERE id_udn  = @IdUdn AND pblu = @IdPblu)";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("IdUdn", id_udn);
                        command.Parameters.AddWithValue("IdPblu", idPblu);
                        bool exists = (bool)await command.ExecuteScalarAsync();
                        return exists;
                    }
                }
            });
        }

        private DateTime FechaOperativaSencilla()
        {
            var fechaOperativa = DateTime.Today;
            if (fechaOperativa.DayOfWeek == DayOfWeek.Saturday)
            {
                fechaOperativa = fechaOperativa.AddDays(2);
            }
            else if (fechaOperativa.DayOfWeek == DayOfWeek.Sunday)
            {
                fechaOperativa = fechaOperativa.AddDays(1);
            }
            return fechaOperativa;
        }
    }
}