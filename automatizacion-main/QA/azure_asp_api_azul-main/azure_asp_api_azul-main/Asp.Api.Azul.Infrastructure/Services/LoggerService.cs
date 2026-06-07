using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Repositorys;
using Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services;
using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Infrastructure.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Infrastructure.Services
{
    public class LoggerService : ILoggerService
    {
        private readonly ILoggerRepository _loggerRepository;
        private readonly LoggerHelper _logger;
        public LoggerService(ILoggerRepository loggerRepository, LoggerHelper logger)
        {
            _loggerRepository = loggerRepository;
            _logger = logger;
        }
        public async Task RegistraLogMonitorPlus(MonitorPlusLog log)
        {
            try
            {
                await _loggerRepository.InsertLogMonitorPlus(log);
            }
            catch (Exception ex)
            {
                _logger.LogError($"Error al registrar en RegistraLogMonitorPlus el log con clave de rastreo:{log.ClaveRastreo} ", ex);
            }
        }
    }
}
