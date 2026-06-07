using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.AspLogsRepository
{
    public class AspLogsRepository : BaseRepository, IAspLogsRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public AspLogsRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }
        public async Task<int> InsertCatalogo(CatalogoLog catalogo)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(catalogo);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(catalogo));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }
        public async Task<int> InsertCuenta(CuentaLog cuenta)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(cuenta);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(cuenta));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }
        public async Task<int> InsertLogin(LoginLog login)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(login);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(login));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        public async Task<int> InsertPago(PagoLog pago)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(pago);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(pago));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }
        public async Task<int> InsertSaldo(SaldoLog saldo)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(saldo);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(saldo));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }
        public async Task<int> InsertError(ErrorLog error)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(error);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(error));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        public async Task<int> InsertAsp(AspLog asp)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(asp);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(asp));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }
    }

    public interface IAspLogsRepository
    {
        Task<int> InsertCatalogo(CatalogoLog catalogo);
        Task<int> InsertCuenta(CuentaLog cuenta);
        Task<int> InsertLogin(LoginLog login);
        Task<int> InsertPago(PagoLog pago);
        Task<int> InsertSaldo(SaldoLog saldo);
        Task<int> InsertError(ErrorLog error);
        Task<int> InsertAsp(AspLog asp);
    }
}