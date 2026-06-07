using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.ViewPbluCrtRepository
{
    public class ViewPbluCrtRepository : IViewPbluCrtRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public ViewPbluCrtRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<ViewPbluCrt?> GetCertificadoActivo(int idPblue)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_pblu, id_tipo_part, descripcion, id_certificado, activo, username FROM view_pblu_crt WHERE id_pblu = @idPblue limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("idPblue", idPblue);
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            var viewPblueCrt = new ViewPbluCrt
                            {
                                IdPblu = reader.GetInt32(0),
                                IdTipoPart = reader.GetInt32(1),
                                Descripcion = reader.GetString(2),
                                IdCertificado = reader.GetInt32(3),
                                Activo = reader.GetBoolean(4),
                                Username = reader.GetString(5)
                            };
                            return viewPblueCrt;
                        }
                    }
                    return null;
                }
            });
        }
    }
}