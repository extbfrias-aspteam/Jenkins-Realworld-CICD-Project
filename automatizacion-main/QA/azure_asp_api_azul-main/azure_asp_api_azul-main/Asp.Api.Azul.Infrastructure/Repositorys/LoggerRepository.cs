using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Repositorys;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Microsoft.Extensions.Configuration;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Infrastructure.Repositorys
{
    public class LoggerRepository : ILoggerRepository
    {
        private readonly IConfiguration _configuration;
        public LoggerRepository(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public async Task InsertLogMonitorPlus(MonitorPlusLog log)
        {

            using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("LogsDbConnection")))
            {
                await connection.OpenAsync();

                var query = @"INSERT INTO monitor_plus_log
                      (endpoint, http_method, request_body, response_body, id_pblu, status_code,
                       headers_request, clave_rastreo, stack_trace, error_message, info_adicional)
                      VALUES
                      (@endpoint, @http_method, @request_body, @response_body, @id_pblu, @status_code,
                       @headers_request, @clave_rastreo, @stack_trace, @error_message, @info_adicional);";

                using (var command = new NpgsqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("@endpoint", log.Endpoint);
                    command.Parameters.AddWithValue("@http_method", log.HttpMethod);
                    command.Parameters.AddWithValue("@request_body", log.RequestBody);
                    command.Parameters.AddWithValue("@response_body", log.ResponseBody);
                    command.Parameters.AddWithValue("@id_pblu", log.IdPblu);
                    command.Parameters.AddWithValue("@status_code", log.StatusCode);
                    command.Parameters.AddWithValue("@headers_request", log.HeadersRequest);
                    command.Parameters.AddWithValue("@clave_rastreo", log.ClaveRastreo);
                    command.Parameters.AddWithValue("@stack_trace", log.StackTrace);
                    command.Parameters.AddWithValue("@error_message", log.ErrorMessage);
                    command.Parameters.AddWithValue("@info_adicional", log.InfoAdicional);

                    await command.ExecuteNonQueryAsync();
                }
            }


        }
    }
}
