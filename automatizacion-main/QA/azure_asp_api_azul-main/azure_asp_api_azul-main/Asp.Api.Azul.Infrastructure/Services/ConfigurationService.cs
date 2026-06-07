using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Microsoft.Extensions.Configuration;
using Npgsql;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Infrastructure.Services
{
    public class ConfigurationService : IConfigurationService
    {
        private readonly IConfiguration _configuration;
        private readonly ICacheService _cacheService;

        public ConfigurationService(IConfiguration configuration, ICacheService cacheService)
        {
            _configuration = configuration;
            _cacheService = cacheService;
        }

        public async Task<T> Get<T>(string key)
        {
            string? value = null;

            using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
            {
                await connection.OpenAsync();
                var query = "SELECT value FROM configuration WHERE key = @key limit 1";
                using (var command = new NpgsqlCommand(query, connection))
                {
                    command.Parameters.AddWithValue("key", key);

                    var result = await command.ExecuteScalarAsync();
                    if (result != null && result != DBNull.Value)
                    {
                        value = result.ToString();
                    }
                }
            }

            if (value == null) return default;

            if (typeof(T) == typeof(bool))
            {
                if (value == "1" || value.ToLower() == "true")
                    return (T)(object)true;
                if (value == "0" || value.ToLower() == "false")
                    return (T)(object)false;
            }
            if (typeof(T).IsEnum)
            {
                if (Enum.TryParse(typeof(T), value, true, out var enumValue))
                    return (T)enumValue!;

                throw new InvalidOperationException(
                    $"El valor '{value}' no es válido para el enum {typeof(T).Name}");
            }


            return (T)Convert.ChangeType(value, typeof(T));
        }

        public async Task<T> GetWithCache<T>(string key, TimeSpan timeExpires)
        {
            if (_cacheService.Exists(key))
            {
                var cachedValue = _cacheService.Get<T>(key);
                return cachedValue!;
            }

            T dbValue = await Get<T>(key);

            _cacheService.Set(key, dbValue, timeExpires);

            return dbValue;
        }
    }
}
