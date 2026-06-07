using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.ContribuyenteRepository
{
    public class ContribuyenteRepository : BaseRepository, IContribuyenteRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public ContribuyenteRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<Contribuyente?> GetByPMoral(int idPersonaPersonaMoral)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query =
                        "SELECT id_contribuyente, representante, p_moral, activo, fecha_creacion, fecha_actualizacion, usuario_creacion FROM contribuyente WHERE p_moral = @p_moral limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("p_moral", idPersonaPersonaMoral);
                        var reader = await command.ExecuteReaderAsync();
                        if (await reader.ReadAsync())
                        {

                            var udn = new Contribuyente
                            {
                                IdContribuyente = reader.GetInt32(0),
                                Representante = reader.GetInt32(1),
                                PersonaMoral = reader.GetInt32(2),
                                Activo = reader.GetBoolean(3),
                                FechaCreacion = reader.GetValue(4) as DateTime?,
                                FechaActualizacion = reader.GetValue(5) as DateTime?,
                                UsuarioCreacion = reader.GetValue(6)?.ToString(),
                            };
                            return udn;
                        }
                    }

                    return null;
                }
            });
        }

        public async Task<int> Insert(Contribuyente contribuyente)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(contribuyente);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(contribuyente));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        public async Task<bool> Update(Contribuyente contribuyente)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = $"UPDATE contribuyente SET fecha_actualizacion = @fecha_actualizacion WHERE id_contribuyente = @id_contribuyente";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("fecha_actualizacion", contribuyente.FechaActualizacion ?? DateTime.Now);
                        command.Parameters.AddWithValue("id_contribuyente", contribuyente.IdContribuyente);

                        var res = await command.ExecuteNonQueryAsync();
                        return res > 0;
                    }
                }
            }); ;
        }
    }
}