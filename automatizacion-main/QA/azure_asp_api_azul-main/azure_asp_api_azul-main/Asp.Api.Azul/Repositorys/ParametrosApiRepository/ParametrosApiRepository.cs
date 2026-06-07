using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Cifrado.Entities.DataBase;
using Npgsql;

namespace Asp.Cifrado.Repositorys.ParametrosApiRepository
{
    public class ParametrosApiRepository : IParametrosApiRepository
	{
		private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public ParametrosApiRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
		{
			_configuration = configuration;
			_resilientExecutor = resilientExecutor;
		}

		public async Task<ParametrosApi> GetById(int idParam)
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT id_param, version, descripcion, activo, valor, fecha_creacion, usuario_creacion FROM parametros_api WHERE id_param = @id_param limit 1";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_param", idParam);
						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							var parametroApi = new ParametrosApi
							{
								IdParam = reader.GetInt32(0),
								Version = reader.GetInt32(1),
								Descripcion = reader.GetString(2),
								Activo = reader.GetBoolean(3),
								Valor = reader.GetString(4),
								FechaCreacion = reader.GetValue(5) as DateTime?,
								UsuarioCreacion = reader.GetValue(6)?.ToString()
							};
							return parametroApi;
						}
					}

					return null;
				}
			});
		}
	}
}