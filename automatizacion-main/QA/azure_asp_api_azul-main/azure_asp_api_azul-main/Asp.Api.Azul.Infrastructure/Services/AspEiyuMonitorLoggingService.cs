using System.Runtime.CompilerServices;
using System.Text.Json;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.AspEiyuMonitor;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Logging;

namespace Asp.Api.Azul.Infrastructure.Services
{
    public class AspEiyuMonitorLoggingService : IAspEiyuMonitorLoggingService
    {
        private readonly IKafkaProducerService _kafkaProducerService;
        private readonly ILogger<AspEiyuMonitorLoggingService> _logger;
        private string serverKafka;
        private string topic;
        private bool logger_active;

        public AspEiyuMonitorLoggingService(
            IKafkaProducerService kafkaProducerService,
            ILogger<AspEiyuMonitorLoggingService> logger,
            IConfiguration configuration)
        {
            _kafkaProducerService = kafkaProducerService;
            _logger = logger;

            serverKafka = configuration.GetSection("AspEiyuMonitor:LoggingKafkaServer").Value
                ?? throw new Exception("AspEiyuMonitor:LoggingKafkaServer not found");

            topic = configuration.GetSection("AspEiyuMonitor:LoggingTopic").Value
                ?? throw new Exception("AspEiyuMonitor:LoggingTopic not found");

            string logger_activeStr = configuration.GetSection("AspEiyuMonitor:LoggingActive").Value
                ?? throw new Exception("AspEiyuMonitor:LoggingActive not found");

            logger_active = bool.TryParse(logger_activeStr, out var parsedValue) && parsedValue;
        }

        public async Task SendInfoGeneric(string message, dynamic metaData = null, [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(string.Empty, string.Empty, AspLoggingLevel.INFO, message, metaData, string.Empty, callerMethod, className);
        }

        public async Task SendErrorGeneric(string message, string stackTrace, dynamic metaData = null, [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(string.Empty, string.Empty, AspLoggingLevel.ERROR, message, metaData, stackTrace, callerMethod, className);
        }

        public async Task SendInfo(string idPblu, string trackingKey, string message, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(idPblu, trackingKey, AspLoggingLevel.INFO, message, metaData, stackTrace, callerMethod, className);
        }

        public async Task SendError(string idPblu, string trackingKey, string message, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(idPblu, trackingKey, AspLoggingLevel.ERROR, message, metaData, stackTrace, callerMethod, className);
        }

        public async Task SendWarning(string idPblu, string trackingKey, string message, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(idPblu, trackingKey, AspLoggingLevel.WARNINGN, message, metaData, stackTrace, callerMethod, className);
        }

        public async Task SendRequestInfo(string idPblu, string trackingKey, string message, RequestDetailDto requestDetail, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(idPblu, trackingKey, AspLoggingLevel.INFO, message, metaData, stackTrace, callerMethod, className, requestDetail);
        }

        public async Task SendRequestError(string idPblu, string trackingKey, string message, RequestDetailDto requestDetail, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(idPblu, trackingKey, AspLoggingLevel.ERROR, message, metaData, stackTrace, callerMethod, className, requestDetail);
        }

        public async Task SendRequestWarning(string idPblu, string trackingKey, string message, RequestDetailDto requestDetail, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "")
        {
            var className = Path.GetFileNameWithoutExtension(callerFilePath);

            await Send(idPblu, trackingKey, AspLoggingLevel.WARNINGN, message, metaData, stackTrace, callerMethod, className, requestDetail);
        
        }

        private async Task Send(string idPblu, string trackingKey, string logLevel, string message, dynamic metaData = null, string stackTrace = "", string callerMethod = "", string callerFilePath = "", RequestDetailDto? requestDetail = null)
        {
            try
            {
                MessageLogDto messageDto = new MessageLogDto
                {
                    Date = DateTime.Now,
                    IdPblu = idPblu,
                    TrackingKey = trackingKey,
                    ApplicationCode = "APIAZUL",
                    ClassName = $"{callerFilePath}.cs",
                    MethodName = callerMethod,
                    LogLevel = logLevel,
                    Message = message,
                    StackTrace = stackTrace,
                };

                if (requestDetail != null)
                {
                    messageDto.RequestDetail = requestDetail;
                }

                if (metaData != null)
                {
                    messageDto.LogMetadata = JsonSerializer.Serialize(metaData);
                }

                string messageDtoStr = JsonSerializer.Serialize(messageDto);

                if (logger_active)
                {
                    _logger.LogInformation(messageDtoStr);
                }

                await _kafkaProducerService.SendMessageAsync(serverKafka, topic, messageDtoStr);
            }
            catch (Exception ex)
            {
                _logger.LogError(ex.StackTrace);
            }
        }
                
        private struct AspLoggingLevel
        {
            public const string INFO = "INFO";
            public const string ERROR = "ERROR";
            public const string WARNINGN = "WARNING";
        }
    }
}