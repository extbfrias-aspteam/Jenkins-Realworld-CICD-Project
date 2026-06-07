using Asp.Api.Azul.Core.Commons.Models.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Repositorys
{
    public interface ILoggerRepository
    {
        Task InsertLogMonitorPlus(MonitorPlusLog log);
    }
    
}
