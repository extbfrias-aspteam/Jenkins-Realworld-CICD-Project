using Asp.Api.Azul;
using Microsoft.AspNetCore;
using Serilog;
using Serilog.Events;

Log.Logger = new LoggerConfiguration()
    .MinimumLevel.Debug()
    .MinimumLevel.Override("Microsoft", LogEventLevel.Warning)
    .Enrich.FromLogContext()
    .WriteTo.Console(outputTemplate: "[{Timestamp:yyyy-MM-dd HH:mm:ss.fff} {Level:u3}] [{SourceContext}] {Message:lj}{NewLine}{Exception}")
    .WriteTo.File(
        path: "logs/apiazul_traceProcess-.log",
        rollingInterval: RollingInterval.Hour,
        retainedFileCountLimit: 72,
        fileSizeLimitBytes: 10_000_000,
        rollOnFileSizeLimit: true,
        shared: true)
.CreateLogger();

try
{
    Log.Information("Starting application Asp.Api.Azul ...");
    await BuildWebHost(args).RunAsync();
}
catch (Exception ex)
{
    Log.Fatal(ex, "Fatal error in start application.");
}
finally
{
    Log.CloseAndFlush();
}

IWebHost BuildWebHost(string[] args) =>
    WebHost
        .CreateDefaultBuilder(args)
        .ConfigureAppConfiguration(cb =>
        {
            var sources = cb.Sources;
            sources.Insert(3, new Microsoft.Extensions.Configuration.Json.JsonConfigurationSource()
            {
                Optional = true,
                Path = "appsettings.QA.json",
                ReloadOnChange = false
            });
        })
        .UseStartup<Startup>()
        .UseSerilog()
        .Build();