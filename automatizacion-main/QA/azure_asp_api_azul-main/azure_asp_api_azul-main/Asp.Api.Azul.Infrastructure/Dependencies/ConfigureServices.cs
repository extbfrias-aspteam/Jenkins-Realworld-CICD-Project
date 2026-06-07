using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Repositorys;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Infrastructure.Repositorys;
using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Api.Azul.Infrastructure.Services;
using Asp.Api.Azul.Infrastructure.Services.MonitorPlus;
using Asp.Api.Azul.Infrastructure.Utils;
using Microsoft.Extensions.DependencyInjection;

namespace Asp.Api.Azul.Infrastructure.Dependencies
{
    public static class ConfigureServices
    {
        public static IServiceCollection Configure(this IServiceCollection services)
        {
            services.AddSingleton<LoggerHelper>();
            services.AddHttpClient();

            services.AddScoped<ILoggerRepository, LoggerRepository>();
            services.AddScoped<ILoggerService, LoggerService>();
            services.AddScoped<IContingencyServices, ContingencyServices>();
            services.AddScoped<IAspEiyuMonitorLoggingService, AspEiyuMonitorLoggingService>();
            services.AddScoped<IKafkaProducerService, KafkaProducerService>();
            services.AddScoped<ICacheService, CacheService>();
            services.AddScoped<IConfigurationService, ConfigurationService>();
            services.AddSingleton<IJwtCacheServiceMonitorPlus, JwtCacheServiceMonitorPlus>();
            services.AddScoped<IMonitorPlusService, MonitorPlusService>();
            services.AddSingleton<PollyPolicies>();
            services.AddSingleton<ResilientExecutor>();
            services.AddTransient<HttpClient>();


            services.AddHttpClient("TraspasosCoreClient", (sp, client) =>
            {
                client.Timeout = TimeSpan.FromMinutes(3);
            })

           .AddPolicyHandler((sp, request) =>
           {
               var policies = sp.GetRequiredService<PollyPolicies>();
               return policies.GetRetryPolicy();
           })
           .AddPolicyHandler((sp, request) =>
           {
               var policies = sp.GetRequiredService<PollyPolicies>();
               return policies.GetTimeoutPolicy();
           });


            return services;
        }
    }
}