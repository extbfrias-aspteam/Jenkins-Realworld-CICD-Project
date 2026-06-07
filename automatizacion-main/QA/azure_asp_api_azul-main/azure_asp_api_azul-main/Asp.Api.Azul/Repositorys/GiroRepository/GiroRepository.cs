using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.GiroRepository
{
    public class GiroRepository : IGiroRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public GiroRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<ASPGiro?> GetById(int giroId)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT desc_actividad, gir_id, desc_giro, act_id, clave_cnbv, clave_fnd FROM asp_giro WHERE gir_id = @gir_id limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("gir_id", giroId);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var giro = new ASPGiro
                            {
                                DescActividad = reader.GetValue(0)?.ToString(),
                                GiroId = reader.GetInt32(1),
                                DescGiro = reader.GetValue(2)?.ToString(),
                                ActId = reader.GetValue(3) as int?,
                                ClaveCnbv = reader.GetValue(4)?.ToString(),
                                ClaveFnd = reader.GetValue(5)?.ToString()
                            };
                            return giro;
                        }
                    }

                    return null;
                }
            });
        }
    }
}