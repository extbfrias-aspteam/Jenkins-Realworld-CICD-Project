using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;
using NpgsqlTypes;

namespace Asp.Api.Azul.Repositorys.LogsRepository
{
    public class LogsRepository : ILogsRepository
	{
		private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public LogsRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
		{
			_configuration = configuration;
			_resilientExecutor = resilientExecutor;
		}

		#region Insert

		public async Task<int> Insert(Logs log)
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = GetQueryInsert(log);
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddRange(GetParametersInsert(log));
						var idLog = await command.ExecuteScalarAsync();
						return idLog == null ? 0 : (int)idLog;
					}
				}
			});
		}
        public async Task InsertRegistroTiempo(RegistroTiemposLogs log)
        {
			await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
				{
					await connection.OpenAsync();

					var query = "INSERT INTO tiempos_logs (clave_rastreo, id_pblu, peticion, tiempo_llegada, tiempo_respuesta, respuesta) " +
						"VALUES(@cve_rastreo, @idPblu, @peticion, @tiempo_llegada, @tiempo_respuesta, @respuesta);";

					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("@cve_rastreo", log.cve_rastreo);
						command.Parameters.AddWithValue("@idPblu", log.idPblu);
						command.Parameters.AddWithValue("@peticion", log.peticion);
						command.Parameters.AddWithValue("@tiempo_llegada", log.tiempo_llegada);
						command.Parameters.AddWithValue("@tiempo_respuesta", log.tiempo_respuesta);
						command.Parameters.AddWithValue("@respuesta", log.respuesta);

						var res = await command.ExecuteScalarAsync() as int?;


					}
				}
			});
        }
        private NpgsqlParameter[] GetParametersInsert(Logs log)
		{
			List<NpgsqlParameter> parameters = new List<NpgsqlParameter>();
			var properties = typeof(Logs).GetProperties();
			foreach (var property in properties)
			{
				var value = property.GetValue(log);
				if (value != null)
				{
					var attr = property.GetCustomAttributes(true)
						.FirstOrDefault(x => x.GetType() == typeof(DbColumnAttribute));
					if (attr != null)
					{
						var attribute = attr as DbColumnAttribute;
						if (attribute != null)
						{
							var columnName = attribute.GetColumnName();
							
							var parameter = new NpgsqlParameter(columnName, value);
							if (property.PropertyType == typeof(string))
							{
								parameter.NpgsqlDbType = NpgsqlDbType.Varchar;
								parameter.Size = attribute.GetSize();
							} 
							parameters.Add(parameter);
							
						}
					}

				}
			}

			return parameters.ToArray();
		}
		private string GetQueryInsert(Logs log)
		{

			List<string> columns = new List<string>();
			List<string> columnsArroba = new List<string>();
			var properties = typeof(Logs).GetProperties();
			foreach (var property in properties)
			{
				var value = property.GetValue(log);
				if (value != null)
				{
					var attr = property.GetCustomAttributes(true)
						.FirstOrDefault(x => x.GetType() == typeof(DbColumnAttribute));
					if (attr != null)
					{
						var attribute = attr as DbColumnAttribute;
						if (attribute != null)
						{
							var columnName = attribute.GetColumnName();
							columns.Add(columnName);
							columnsArroba.Add($"@{columnName}");
						}
					}

				}
			}

			var query = "INSERT INTO logs (" +
			            string.Join(", ", columns) +
			            ") VALUES (" +
			            string.Join(", ", columnsArroba) +
			            ") RETURNING id_log";
			return query;
		}

		#endregion
	}
}