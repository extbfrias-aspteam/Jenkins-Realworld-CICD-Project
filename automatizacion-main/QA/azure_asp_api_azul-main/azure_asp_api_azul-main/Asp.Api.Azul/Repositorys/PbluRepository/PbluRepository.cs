using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;
using Const = Asp.Api.Azul.Core.Commons.Constants.GeneralConstants;

namespace Asp.Api.Azul.Repositorys.PbluRepository
{
    public class PbluRepository : IPbluRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ICacheService _cacheService;
        private readonly ResilientExecutor _resilientExecutor;

        public PbluRepository(IConfiguration configuration, ICacheService cacheService, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _cacheService = cacheService;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<string?> ObtenerBloqueoAltaReferencia(int idPblu)
        {
            using var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection"));
            await connection.OpenAsync();

            var query = @"
                SELECT bloquear_alta_referencia
                FROM pblu
                WHERE id_pblu = @idPblu
                LIMIT 1;
            ";

            using var cmd = new NpgsqlCommand(query, connection);
            cmd.Parameters.AddWithValue("idPblu", idPblu);

            var result = await cmd.ExecuteScalarAsync();

            return result?.ToString();
        }
        public async Task<List<Pblu>> GetAll()
		{
            var data = _cacheService.Get<List<Pblu>>(Const.CacheKeys.PBLU_LIST);

            if (data is null)
            {
                List<Pblu> response = new List<Pblu>();
                await _resilientExecutor.ExecuteAsync(async () =>
                {
                    using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                    {
                        await connection.OpenAsync();
                        var query = "SELECT id_pblu, prefijo_pblu, id_tipo_part, clabe_pblu, descripcion, activo, fecha_creacion, usuario_creacion, contador_udn, rfc, domicilio, localidad, colonia, codigo_postal, telefono, email, fecha_inicio_monto, fecha_fin_monto, blu_limite FROM pblu;";
                        using (var command = new NpgsqlCommand(query, connection))
                        {
                            var reader = await command.ExecuteReaderAsync();
                            while (await reader.ReadAsync())
                            {

                                response.Add(new Pblu
                                {
                                    IdPblu = reader.GetInt32(0),
                                    PrefijoPblu = reader.GetValue(1) as int?,
                                    IdTipoPart = reader.GetInt32(2),
                                    ClabePblu = reader.GetString(3),
                                    Descripcion = reader.GetString(4),
                                    Activo = reader.GetBoolean(5),
                                    FechaCreacion = reader.GetValue(6) as DateTime?,
                                    UsuarioCreacion = reader.GetValue(7)?.ToString(),
                                    ContadorUdn = reader.GetValue(8) as int?,
                                    Rfc = reader.GetValue(9)?.ToString(),
                                    Domicilio = reader.GetValue(10)?.ToString(),
                                    Localidad = reader.GetValue(11)?.ToString(),
                                    Colonia = reader.GetValue(12)?.ToString(),
                                    CodigoPostal = reader.GetValue(13)?.ToString(),
                                    Telefono = reader.GetValue(14)?.ToString(),
                                    Email = reader.GetValue(15)?.ToString(),
                                    FechaInicioMonto = reader.GetValue(16) as DateTime?,
                                    FechaFinMonto = reader.GetValue(17) as DateTime?,
                                    BluLimite = reader.GetValue(18) as decimal?
                                });
                            }
                        }
                    }

                    _cacheService.Set(Const.CacheKeys.PBLU_LIST, response, Const.CacheTimes.ONE_HOUR);

                    data = response;
                });
            }

            return data;
        }

        public async Task<Pblu?> GetById(int idPblu)
        {
            var data = await GetAll();
            var pblu = data.Where(x => x.IdPblu == idPblu).FirstOrDefault();
            return pblu;

            // PLEASE REMOVE IN NEXT TOUCH ;)
            //
            //using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
            //{
            //    await connection.OpenAsync();
            //    var query = "SELECT id_pblu, prefijo_pblu, id_tipo_part, clabe_pblu, descripcion, activo, fecha_creacion, usuario_creacion, contador_udn, rfc, domicilio, localidad, colonia, codigo_postal, telefono, email, fecha_inicio_monto, fecha_fin_monto, blu_limite, limite_operativo, bloqueado FROM pblu WHERE id_pblu = @id_pblu LIMIT 1";
            //    using (var command = new NpgsqlCommand(query, connection))
            //    {
            //        command.Parameters.AddWithValue("id_pblu", idPblu);
            //        var reader = await command.ExecuteReaderAsync();
            //        while (await reader.ReadAsync())
            //        {
            //            var pblu = new Pblu
            //            {
            //                IdPblu = reader.GetInt32(0),
            //                PrefijoPblu = reader.GetValue(1) as int?,
            //                IdTipoPart = reader.GetInt32(2),
            //                ClabePblu = reader.GetString(3),
            //                Descripcion = reader.GetString(4),
            //                Activo = reader.GetBoolean(5),
            //                FechaCreacion = reader.GetValue(6) as DateTime?,
            //                UsuarioCreacion = reader.GetValue(7)?.ToString(),
            //                ContadorUdn = reader.GetValue(8) as int?,
            //                Rfc = reader.GetValue(9)?.ToString(),
            //                Domicilio = reader.GetValue(10)?.ToString(),
            //                Localidad = reader.GetValue(11)?.ToString(),
            //                Colonia = reader.GetValue(12)?.ToString(),
            //                CodigoPostal = reader.GetValue(13)?.ToString(),
            //                Telefono = reader.GetValue(14)?.ToString(),
            //                Email = reader.GetValue(15)?.ToString(),
            //                FechaInicioMonto = reader.GetValue(16) as DateTime?,
            //                FechaFinMonto = reader.GetValue(17) as DateTime?,
            //                BluLimite = reader.GetValue(18) as decimal?,
            //                LimiteOperativo = reader.GetValue(19) as decimal?
            //            };
            //            return pblu;
            //        }
            //    }
            //    return null;
            //}
        }
    }
}