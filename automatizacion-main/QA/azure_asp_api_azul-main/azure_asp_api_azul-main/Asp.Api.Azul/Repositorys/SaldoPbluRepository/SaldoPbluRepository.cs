using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.SaldoPbluRepository
{
    public class SaldoPbluRepository : ISaldoPbluRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public SaldoPbluRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
			_resilientExecutor = resilientExecutor;
        }

        public async Task<bool> AumentaSaldo(int idPblu, decimal monto)
        {
	        using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
	        {
		        await connection.OpenAsync();
		        var query = "UPDATE saldo_pblu SET saldo = saldo + @monto WHERE id_pblu = @id_pblu";
		        using (var command = new NpgsqlCommand(query, connection))
		        {
			        command.Parameters.AddWithValue("monto", monto);
			        command.Parameters.AddWithValue("id_pblu", idPblu);
			        return await command.ExecuteNonQueryAsync() > 0;
		        }
	        }
        }

		public async Task<bool> DisminuyeSaldo(int idPblu, decimal monto)
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
				using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
				{
					await connection.OpenAsync();
					var query = "UPDATE saldo_pblu SET saldo = saldo - @monto WHERE id_pblu = @id_pblu";
					using (var command = new NpgsqlCommand(query, connection))
					{
						command.Parameters.AddWithValue("monto", monto);
						command.Parameters.AddWithValue("id_pblu", idPblu);
						return await command.ExecuteNonQueryAsync() > 0;
					}
				}
			});
		}
	}
}