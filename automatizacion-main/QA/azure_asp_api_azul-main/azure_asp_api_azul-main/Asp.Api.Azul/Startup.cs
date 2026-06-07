using Asp.Api.Azul.Business.Abono;
using Asp.Api.Azul.Business.Authentication;
using Asp.Api.Azul.Business.Cuenta;
using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Business.Pago;
using Asp.Api.Azul.Business.Retornos;
using Asp.Api.Azul.Business.Saldo;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Infrastructure.Configurations;
using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Api.Azul.Kafka.Consumers.Services.PrevencionFraudes;
using Asp.Api.Azul.Kafka.Consumers.Services.Spei_Out;
using Asp.Api.Azul.Kafka.Consumers.Services.Spei_Retornos;
using Asp.Api.Azul.Kafka.Consumers.Services.SpeiIn;
using Asp.Api.Azul.Kafka.Producer;
using Asp.Api.Azul.Kafka.Topics;
using Asp.Api.Azul.Repositorys.AbonoRepository;
using Asp.Api.Azul.Repositorys.AccessRepository;
using Asp.Api.Azul.Repositorys.AspLogsRepository;
using Asp.Api.Azul.Repositorys.CatalogoRepository;
using Asp.Api.Azul.Repositorys.ContribuyenteRepository;
using Asp.Api.Azul.Repositorys.CuentaRepository;
using Asp.Api.Azul.Repositorys.ExpedienteRepository;
using Asp.Api.Azul.Repositorys.GiroRepository;
using Asp.Api.Azul.Repositorys.HistorialSaldoPbluRepository;
using Asp.Api.Azul.Repositorys.LogsRepository;
using Asp.Api.Azul.Repositorys.NacionalidadRepository;
using Asp.Api.Azul.Repositorys.OcupacionRepository;
using Asp.Api.Azul.Repositorys.PagoRepository;
using Asp.Api.Azul.Repositorys.PaisRepository;
using Asp.Api.Azul.Repositorys.PbluRepository;
using Asp.Api.Azul.Repositorys.PersonaRepository;
using Asp.Api.Azul.Repositorys.RetornoRepository;
using Asp.Api.Azul.Repositorys.SaldoPbluRepository;
using Asp.Api.Azul.Repositorys.UdnRepository;
using Asp.Api.Azul.Repositorys.ViewConciliacionHistRepository;
using Asp.Api.Azul.Repositorys.ViewPbluCrtRepository;
using Asp.Api.Azul.Repositorys.ViewPbluUdnRepository;
using Asp.Api.Azul.Services;
using Asp.Api.Azul.Services.EncriptionBackgroundService;
using Asp.Api.Azul.Services.JwtService;
using Asp.Api.Azul.Services.KafkaPLD;
using Asp.Api.Azul.Services.TraspasosCoreService;
using Asp.Api.Azul.Utilities.GestionTokens;
using Asp.Cifrado.Repositorys.CertificadoRepository;
using Asp.Cifrado.Repositorys.ParametrosApiRepository;
using Asp.Cifrado.Services;
using Autofac;
using Autofac.Extensions.DependencyInjection;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Diagnostics.HealthChecks;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Diagnostics.HealthChecks;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using System.Text;

namespace Asp.Api.Azul;

public class Startup
{
    public Startup(IConfiguration configuration)
    {
        Configuration = configuration;
    }
    public IConfiguration Configuration { get; }

    public virtual IServiceProvider ConfigureServices(IServiceCollection services)
    {
        services.AddHealthChecks()
               .AddCheck("self", () => HealthCheckResult.Healthy());

        services.AddControllers()
                .AddJsonOptions(options => options.JsonSerializerOptions.WriteIndented = true);


        services.AddAuthentication(options =>
        {
            options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
            options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
            options.DefaultScheme = JwtBearerDefaults.AuthenticationScheme;
        }).AddJwtBearer(options =>
        {
            options.SaveToken = true;
            options.RequireHttpsMetadata = false;
            options.TokenValidationParameters = new TokenValidationParameters()
            {
                ValidateIssuer = true,
                ValidateAudience = true,
                ValidAudience = Configuration["Jwt:Issuer"],
                ValidIssuer = Configuration["Jwt:Issuer"],
                IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(Configuration["Jwt:Key"])),
            };
        });

        services.AddSwaggerGen(options =>
        {
            //options.DescribeAllEnumsAsStrings();
            options.SwaggerDoc("v1", new OpenApiInfo
            {
                Title = "Api EYU",
                Version = "v1",
                Description = "Api EYU"
            });
            options.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
            {
                In = ParameterLocation.Header,
                Description = "Token",
                Name = "Authorization",
                Type = SecuritySchemeType.Http,
                BearerFormat = "JWT",
                Scheme = "bearer"
            });
            options.AddSecurityRequirement(new OpenApiSecurityRequirement
            {
                {
                    new OpenApiSecurityScheme
                    {
                        Reference = new OpenApiReference
                        {
                            Type=ReferenceType.SecurityScheme,
                            Id="Bearer"
                        }
                    },
                    new string[]{}
                }
            });
        });




        services.AddRouting(options => options.LowercaseUrls = true);



        // Register services of Infrastrcutre
        Asp.Api.Azul.Infrastructure.Dependencies.ConfigureServices.Configure(services);


        services.AddCors(options =>
        {
            options.AddPolicy("CorsPolicy",
                builder =>
                {
                    builder.SetIsOriginAllowed((host) => true)
                    .AllowAnyOrigin()
                    .AllowAnyHeader()
                    .AllowAnyMethod();
                });
        });
        Asp.Api.Azul.Infrastructure.Dependencies.ConfigureServices.Configure(services);
        RegisterBusiness(services);
        RegisterRepositories(services);
        RegistrarServiciosKafka(services);

        services.AddMemoryCache();

        var apisSection = Configuration.GetSection("MicroService");
        services.Configure<List<MicroServiceConfig>>(Configuration.GetSection("MicroService"));


        var container = new ContainerBuilder();
        container.Populate(services);

        return new AutofacServiceProvider(container.Build());
    }

    public void Configure(IApplicationBuilder app, IWebHostEnvironment env, ILoggerFactory loggerFactory)
    {


        var pathBase = Configuration["PATH_BASE"];

        if (!string.IsNullOrEmpty(pathBase))
        {
            loggerFactory.CreateLogger<Startup>().LogDebug("Using PATH BASE '{pathBase}'", pathBase);
            app.UsePathBase(pathBase);
        }

        if (env.IsDevelopment())
        {
            app.UseDeveloperExceptionPage();
        }

        app.UseSwagger().UseSwaggerUI(c =>
        {
            c.SwaggerEndpoint($"{(!string.IsNullOrEmpty(pathBase) ? pathBase : string.Empty)}/swagger/v1/swagger.json", "Asp.Api.Eyu v1");

            c.OAuthClientId("Asp Api EYU");
            c.OAuthClientSecret(string.Empty);
            c.OAuthRealm(string.Empty);
            c.OAuthAppName("Api EYU Swagger UI");
        });

        app.UseRouting();
        app.UseCors("CorsPolicy");
        app.UseStaticFiles();
        app.UseAuthentication();
        app.UseAuthorization();
        app.UseEndpoints(endpoints =>
        {
            endpoints.MapDefaultControllerRoute();
            endpoints.MapControllers();
        });

      
    }

    

    


    private void RegisterRepositories(IServiceCollection services)
    {
        services.AddTransient<HttpClient>();
        services.AddHttpClient("Contingency", client =>
        {
            client.Timeout = TimeSpan.FromSeconds(30);
        });
        services.AddScoped<TokenSettings>();
        services.AddTransient<IJwtIssuerService, JwtIssuerService>();

        services.AddTransient<ICuentaRepository, CuentaRepository>();
        services.AddTransient<ILogsRepository, LogsRepository>();
        services.AddTransient<IPagoRepository, PagoRepository>();
        services.AddTransient<ISaldoPbluRepository, SaldoPbluRepository>();
        services.AddTransient<IUdnRepository, UdnRepository>();
        services.AddTransient<IViewConciliacionHistRepository, ViewConciliacionHistRepository>();
        services.AddTransient<IAbonoRepository, AbonoRepository>();
        services.AddTransient<IPbluRepository, PbluRepository>();
        services.AddTransient<IHistorialSaldoPbluRepository, HistorialSaldoPbluRepository>();
        services.AddTransient<ICertificadoRepository, CertificadoRepository>();
        services.AddTransient<IParametrosApiRepository, ParametrosApiRepository>();
        services.AddTransient<ICatalogoRepository, CatalogoRepository>();
        services.AddTransient<IPersonaRepository, PersonaRepository>();
        services.AddTransient<IContribuyenteRepository, ContribuyenteRepository>();



        services.AddTransient<IAccessRepository, AccessRepository>();
        services.AddTransient<IViewPbluCrtRepository, ViewPbluCrtRepository>();
        services.AddTransient<INacionalidadRepository,NacionalidadRepository>();
        services.AddTransient<IOcupacionRepository,OcupacionRepository>(); 
        services.AddTransient<IExpedienteRepository,ExpedienteRepository>();
        services.AddTransient<IPaisRepository, PaisRepository>();
        services.AddTransient<IGiroRepository, GiroRepository>();


        services.AddTransient<IViewPbluUdnRepository, ViewPbluUdnRepository>();
        services.AddTransient<IAspLogsRepository, AspLogsRepository>();

        services.AddTransient<IKafkaPldService, KafkaPldService>();

    }

    private void RegisterBusiness(IServiceCollection services)
    {
        services.AddScoped<IUserResolver,UserResolver>();   
        services.AddSingleton<IHttpContextAccessor, HttpContextAccessor>();
        services.AddSingleton<IEncriptionService, EncriptionService>();
        services.AddHostedService<EncriptionBackgroundService>();
        services.AddSingleton<IValidarDomicilioService, ValidarDomicilioService>();


        services.AddTransient<IAspApiClient, AspApiClient>();
        services.AddTransient<ILogsBusiness, LogBusiness>();
        services.AddTransient<IPagoBusiness, PagoBusiness>();
        services.AddTransient<ISaldoBusiness, SaldoBusiness>();
        services.AddTransient<IAbonoBusiness, AbonoBusiness>();
        services.AddTransient<ILoginBusiness, LoginBusiness>();
        services.AddTransient<ICuentaBusiness,CuentaBusiness>();
        services.AddTransient<IAspLogservice, AspLogService>();
        services.AddScoped<IRetornoBusiness, RetornoBusiness>();
        services.AddScoped<IRetornoRepository, RetornoRepository>();
        services.AddScoped<ITraspasosCoreService, TraspasosCoreService>();
    }

    private void RegistrarServiciosKafka(IServiceCollection services)
    {
        services.AddTransient<IInicializadorTopicos, InicializadorTopicos>();

        services.AddSingleton<KafkaProducerService>();

        services.AddHostedService<SpeiOutPendienteHandlerKafka>();
        services.AddHostedService<SpeiOutNotificationHandlerKafka>();
        services.AddHostedService<SpeiOutRechazadoHandlerKafka>();
        services.AddHostedService<SpeiOutReintentoHandlerKafka>();
        services.AddHostedService<SpeiRetornoEstadoHandlerKafka>();
        services.AddHostedService<SpeiInNotificationHandlerKafka>();
        services.AddHostedService<SpeiRetornoAzulApiHandlerKafka>();
        services.AddHostedService<PrevFraude_SolicitudValidadaHandler>();


    }
}
