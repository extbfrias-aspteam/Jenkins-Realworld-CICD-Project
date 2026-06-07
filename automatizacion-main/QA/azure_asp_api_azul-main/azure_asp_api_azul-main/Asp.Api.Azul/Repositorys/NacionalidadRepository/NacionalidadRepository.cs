using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.NacionalidadRepository
{
    public class NacionalidadRepository : INacionalidadRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public NacionalidadRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<ASPNacionalidad?> findByPaisId(int id)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_nacionalidad,desc_nacionalidad,pais_id FROM asp_nacionalidad WHERE id_nacionalidad = @idNacionalidad limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("idNacionalidad", id);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var nacionalidad = new ASPNacionalidad
                            {
                                IdNacionalidad = reader.GetInt32(0),
                                DescNacionalidad = reader.GetValue(1)?.ToString(),
                                PaisId = reader.GetValue(2) as int?
                            };
                            return nacionalidad;
                        }
                    }
                }
                return null;
            });
        }
    }
}