using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Infrastructure.Resilience
{

    public class ResilientExecutor
    {
        private readonly PollyPolicies _polly;

        public ResilientExecutor(PollyPolicies polly)
        {
            _polly = polly;
        }

        public async Task ExecuteAsync(Func<Task> action)
        {
            var policy = _polly.GetRetryPolicyForTask();
            await policy.ExecuteAsync(action);
        }

        public async Task<T> ExecuteAsync<T>(Func<Task<T>> action)
        {
            var policy = _polly.GetRetryPolicyForGeneric<T>();
            return await policy.ExecuteAsync(action);
        }
    }

}
