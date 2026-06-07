using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.ViewConciliacionHistRepository
{
    public class ViewConciliacionHistRepository : IViewConciliacionHistRepository
	{
		private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public ViewConciliacionHistRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
		{
			_configuration = configuration;
			_resilientExecutor = resilientExecutor;
		}

        public async Task<string?> GetByUdn(string udn)
        {
            string? saldo_udn_es = "0";
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT * FROM get_saldo_udn_es(@_udn)";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("_udn", Convert.ToInt16(udn));

						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							saldo_udn_es = reader.GetValue(0).ToString();
						}
					}
				}

				return saldo_udn_es;
			});
        }

		public async Task<List<ViewConciliacionHist>> GetBy(int idPblu)
		{
			var response = new List<ViewConciliacionHist>();

			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT * FROM get_saldos_udns(@id_pblu)";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_pblu", idPblu);

						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							response.Add(new ViewConciliacionHist
							{
								FechaOperacion = reader.GetValue(0) as DateTime?,
								IdUdn = reader.GetValue(1) as int?,
								Pblu = idPblu,
								MontoCargo = reader.GetValue(2) as decimal?,
								MontoAbono = reader.GetValue(3) as decimal?,
								TotalCargosMonto = reader.GetValue(4) as long?,
								TotalAbonosMonto = reader.GetValue(5) as long?,
								Saldo = (reader.GetValue(6) as decimal?) + ((reader.GetValue(3) as decimal?) - (reader.GetValue(2) as decimal?)),
								SaldoInicial = reader.GetValue(6) as decimal?,
								Descripcion = reader.GetString(7)?.ToString()
							});
						}
					}
				}

				return response;
			});
		}

        public async Task<string?> GetBy(string clabe)
        {
            string? saldo_udn = "0";
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT * FROM get_saldo_udn(@_clabe)";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("_clabe", clabe);

						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							saldo_udn = reader.GetValue(0).ToString();
						}
					}
				}

				return saldo_udn;
			});
        }
        public async Task<ViewConciliacionHist?> GetBy(int idPblu, int idUdn)
		{
			ViewConciliacionHist? response = null;
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT * FROM get_saldo_actual_udn(@id_udn)";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_udn", idUdn);

						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							response = new ViewConciliacionHist
							{
								IdUdn = idUdn,
								Pblu = idPblu,
								Descripcion = "Saldo Udn",
								Saldo = reader.GetValue(0) as decimal?,
								MontoCargo = reader.GetValue(1) as decimal?,
								MontoAbono = reader.GetValue(2) as decimal?,
								TotalCargosMonto = reader.GetValue(3) as long?,
								TotalAbonosMonto = reader.GetValue(4) as long?,
								SaldoInicial = reader.GetValue(5) as decimal?,
								FechaOperacion = reader.GetValue(6) as DateTime?,
							};
						}
					}
				}

				return response;
			});
		}

		public async Task<ViewConciliacionHist?> GetByUdnFecha(int idUdn, DateTime fecha)
		{
			ViewConciliacionHist? response = null;
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "SELECT id_udn, pblu, descripcion, fecha_operacion, saldo_inicial, total_abonos_monto, monto_abono, total_cargos_monto, monto_cargo, saldo FROM view_conciliacion_hist WHERE id_udn = @id_udn AND fecha_operacion = @fecha limit 1";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("id_udn", idUdn);
						command.Parameters.AddWithValue("fecha", fecha);

						var reader = await command.ExecuteReaderAsync();
						while (await reader.ReadAsync())
						{
							response = new ViewConciliacionHist
							{
								IdUdn = reader.GetValue(0) as int?,
								Pblu = reader.GetValue(1) as int?,
								Descripcion = reader.GetString(2)?.ToString(),
								FechaOperacion = reader.GetValue(3) as DateTime?,
								SaldoInicial = reader.GetValue(4) as decimal?,
								TotalAbonosMonto = reader.GetValue(5) as long?,
								MontoAbono = reader.GetValue(6) as decimal?,
								TotalCargosMonto = reader.GetValue(7) as long?,
								MontoCargo = reader.GetValue(8) as decimal?,
								Saldo = reader.GetValue(9) as decimal?
							};
						}
					}
				}

				return response;
			});
		}
	}
}