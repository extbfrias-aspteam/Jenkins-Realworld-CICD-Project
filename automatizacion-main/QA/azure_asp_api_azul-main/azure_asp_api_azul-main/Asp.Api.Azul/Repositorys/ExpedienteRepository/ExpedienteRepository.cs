using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;
using NpgsqlTypes;

namespace Asp.Api.Azul.Repositorys.ExpedienteRepository
{
    public class ExpedienteRepository : IExpedienteRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public ExpedienteRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<bool> UpdateClabe(int idExpediente, string clabe)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE expediente SET clabe = @clabe WHERE id = @id_expediente";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_expediente", idExpediente);
                        command.Parameters.AddWithValue("clabe", clabe);
                        return await command.ExecuteNonQueryAsync() > 0;
                    }
                }
            });
        }

        public async Task<int> Insert(Expediente expediente)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(expediente);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(expediente));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        private NpgsqlParameter[] GetParametersInsert(Expediente expediente)
        {
            List<NpgsqlParameter> parameters = new List<NpgsqlParameter>();
            var properties = typeof(Expediente).GetProperties();
            foreach (var property in properties)
            {
                var value = property.GetValue(expediente);
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

        private string GetQueryInsert(Expediente expediente)
        {

            List<string> columns = new List<string>();
            List<string> columnsArroba = new List<string>();
            var properties = typeof(Expediente).GetProperties();
            foreach (var property in properties)
            {
                var value = property.GetValue(expediente);
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

            var query = "INSERT INTO expediente(" +
                        string.Join(", ", columns) +
                        ") VALUES (" +
                        string.Join(", ", columnsArroba) +
                        ") RETURNING id_persona";
            return query;
        }
    }
}