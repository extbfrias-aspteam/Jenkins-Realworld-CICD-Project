using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;
using NpgsqlTypes;

namespace Asp.Api.Azul.Repositorys.HistorialSaldoPbluRepository
{
	public class HistorialSaldoPbluRepository : IHistorialSaldoPbluRepository
	{
		private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public HistorialSaldoPbluRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
		{
			_configuration = configuration;
			_resilientExecutor = resilientExecutor;
		}

		public async Task<HistorialSaldoPblu?> GetByIdUdnAndLastFechaOperativa(int idUdn)
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT id_udn, fecha_creacion, fecha_operativa, saldo_inicial, saldo_final, usuario_creacion FROM historial_saldo_pblu WHERE id_udn = @id_udn ORDER BY fecha_operativa DESC LIMIT 1";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_udn", idUdn);
						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							var historialSaldoPblu = new HistorialSaldoPblu()
							{
								IdUdn = reader.GetInt32(0),
								FechaCreacion = reader.GetValue(1) as DateTime?,
								FechaOperativa = reader.GetDateTime(2),
								SaldoInicial = reader.GetValue(3) as decimal?,
								SaldoFinal = reader.GetValue(4) as decimal?,
								UsuarioCreacion = reader.GetValue(5)?.ToString()
							};
							return historialSaldoPblu;
						}
					}

					return null;
				}
			});
		}

		public async Task<bool> ExisteByIdUdnAndFechaOperativa(int idUdn, DateTime fechaOperativa)
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT id_udn, fecha_creacion, fecha_operativa, saldo_inicial, saldo_final, usuario_creacion FROM historial_saldo_pblu WHERE id_udn = @id_udn AND fecha_operativa = @fecha_operativa LIMIT 1";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_udn", idUdn);
						command.Parameters.AddWithValue("fecha_operativa", fechaOperativa);
						var reader = await command.ExecuteReaderAsync();
						return reader.HasRows;
					}
				}
			});
		}

		public async Task<bool> UpdateSaldoFinal(int idUdn, DateTime fechaOperativa, decimal saldoFinal)
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "UPDATE historial_saldo_pblu SET saldo_final = @saldo_final WHERE id_udn = @id_udn AND fecha_operativa = @fecha_operativa";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_udn", idUdn);
						command.Parameters.AddWithValue("fecha_operativa", fechaOperativa);
						command.Parameters.AddWithValue("saldo_final", saldoFinal);
						return await command.ExecuteNonQueryAsync() > 0;
					}
				}
			});
		}

		public async Task Insert(HistorialSaldoPblu historialSaldoPblu)
		{
			await _resilientExecutor.ExecuteAsync(async () =>

			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();

					var query = GetQueryInsert<HistorialSaldoPblu>(historialSaldoPblu, "historial_saldo_pblu");

					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddRange(GetParametersInsert<HistorialSaldoPblu>(historialSaldoPblu));
						var result = await command.ExecuteNonQueryAsync();
					}
				}
			});
		}

		private NpgsqlParameter[] GetParametersInsert<T>(T registro)
		{
			List<NpgsqlParameter> parameters = new List<NpgsqlParameter>();
			var properties = typeof(T).GetProperties();
			foreach (var property in properties)
			{
				var value = property.GetValue(registro);
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

		private string GetQueryInsert<T>(T registro, string tableName)
		{

			List<string> columns = new List<string>();
			List<string> columnsArroba = new List<string>();
			var properties = typeof(T).GetProperties();
			foreach (var property in properties)
			{
				var value = property.GetValue(registro);
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

			var query = "INSERT INTO "+tableName+"(" +
						string.Join(", ", columns) +
						") VALUES (" +
						string.Join(", ", columnsArroba) +
						")";
			return query;
		}
	}
}