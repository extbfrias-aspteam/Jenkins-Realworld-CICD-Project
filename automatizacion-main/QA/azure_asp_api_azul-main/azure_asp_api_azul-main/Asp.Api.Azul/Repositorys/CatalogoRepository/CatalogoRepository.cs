using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.CatalogoRepository
{
    public class CatalogoRepository: ICatalogoRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public CatalogoRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<List<DtoBanco>> GetBanco()
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                List<DtoBanco> response = new List<DtoBanco>();
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_banco, descripcion  FROM banco";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            response.Add(new DtoBanco
                            {
                                IdBanco = reader.GetInt32(0),
                                Descripcion = reader.GetString(1)
                            });
                        }
                    }

                    return response;
                }
            });
        }
    }
}