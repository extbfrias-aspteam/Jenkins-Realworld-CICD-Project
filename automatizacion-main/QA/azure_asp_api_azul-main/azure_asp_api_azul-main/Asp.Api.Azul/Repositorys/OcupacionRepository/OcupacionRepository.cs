using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.OcupacionRepository
{
    public class OcupacionRepository : IOcupacionRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public OcupacionRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<AspOcupacion?> getOptionalOcupacionById(int ocuId)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT desc_ocupacion, ocu_id FROM asp_ocupacion WHERE ocu_id = @ocuId limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("ocuId", ocuId);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var nacionalidad = new AspOcupacion
                            {
                                descOcupacion = reader.GetString(0),
                                ocuId = reader.GetInt32(1)
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