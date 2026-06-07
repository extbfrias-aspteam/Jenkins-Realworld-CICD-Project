using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.AccessRepository
{
    public class AccessRepository : IAccessRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public AccessRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<Access?> FindByUser(string paeamString)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_pblu, id_perfil, username, password, intentos_acceso, fecha_creacion, usuario_creacion FROM access WHERE username = @username limit 1";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("username", paeamString);
                        var reader = await command.ExecuteReaderAsync();
                        if (await reader.ReadAsync())
                        {
                            var access = new Access
                            {
                                IdPblue = reader.GetInt32(0),
                                IdPerfil = reader.GetValue(1) as int?,
                                Username = reader.GetString(2),
                                Password = reader.GetString(3),
                                IntentosAcceso = reader.GetValue(4) as int?,
                                FechaCreacion = reader.GetValue(5) as DateTime?,
                                UsuarioCreacion = reader.GetValue(6)?.ToString()
                            };
                            return access;
                        }
                    }
                }
                return null;
            });
        }
    }
}