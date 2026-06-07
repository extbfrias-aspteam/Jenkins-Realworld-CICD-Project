using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.PaisRepository
{
    public class PaisRepository : IPaisRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public PaisRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<ASPPais?> GetById(int paisId)
        {
            return await _resilientExecutor.ExecuteAsync(async () => 
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT desc_pais, pais_id FROM asp_pais WHERE pais_id = @pais_id limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("pais_id", paisId);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var pais = new Entities.DataBase.ASPPais
                            {
                                DescPais = reader.GetValue(0)?.ToString(),
                                PaisId = reader.GetInt32(1)
                            };
                            return pais;
                        }
                    }

                    return null;
                }
            });
        }
    }
}