using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services
{
    public interface IConfigurationService
    {
        Task<T> Get<T>(string key);
        Task<T> GetWithCache<T>(string key, TimeSpan timeExpires);
    }
}
