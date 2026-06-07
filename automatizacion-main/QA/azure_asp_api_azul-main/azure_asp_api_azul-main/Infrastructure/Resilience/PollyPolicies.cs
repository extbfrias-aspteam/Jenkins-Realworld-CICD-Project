using Npgsql;
using Polly.Retry;
using Polly.Timeout;
using Polly;
using System.Net.Sockets;
using Polly.Extensions.Http;

namespace Asp.Api.Azul.Infrastructure.Resilience
{
    public class PollyPolicies
    {
        private readonly ILogger<PollyPolicies> _logger;

        public PollyPolicies(ILogger<PollyPolicies> logger)
        {
            _logger = logger;
        }

        public IAsyncPolicy<HttpResponseMessage> GetTimeoutPolicy()
        {
            return Policy.TimeoutAsync<HttpResponseMessage>(TimeSpan.FromSeconds(30));
        }

        public IAsyncPolicy<HttpResponseMessage> GetRetryPolicy()
        {
            return HttpPolicyExtensions
                .HandleTransientHttpError()
                .Or<TaskCanceledException>()
                .Or<TimeoutRejectedException>()
                .OrResult(msg => (int)msg.StatusCode == 429)
                .WaitAndRetryAsync(
                    4,
                    retryAttempt => TimeSpan.FromSeconds(Math.Pow(2, retryAttempt)),
                    onRetry: (outcome, timespan, retryCount, context) =>
                    {
                        context.TryGetValue("CorrelationId", out var cid);
                        _logger.LogWarning(
                            "Intento {RetryCount} fallido para notificar. Reintentando en {Delay}s...",
                            retryCount,
                            timespan.TotalSeconds
                        );
                    });
        }

        public AsyncRetryPolicy<T> GetRetryPolicyForGeneric<T>(int retryCount = 4, Func<int, TimeSpan>? sleepDurationProvider = null, Action<DelegateResult<T>, TimeSpan, int, Context>? onRetry = null)
        {
            return Policy<T>
                .Handle<NpgsqlException>()
                .Or<IOException>()
                .Or<SocketException>()
                .Or<OperationCanceledException>()
                .WaitAndRetryAsync(
                    retryCount: retryCount,
                    sleepDurationProvider: sleepDurationProvider ?? (attempt => TimeSpan.FromSeconds(Math.Pow(1.8, attempt))),
                    onRetry: onRetry ?? ((delegateResult, timeSpan, retryAttempt, context) =>
                    {
                        var ex = delegateResult.Exception;
                        _logger.LogWarning(
                             "Intento {RetryCount} fallido. Reintentando en {Delay}s. Excepción: {ExceptionType}, Mensaje: {ExceptionMessage}.",
                             retryAttempt,
                             timeSpan.TotalSeconds,
                             ex?.GetType().Name,
                             ex?.Message
                        );
                    })
                );
        }

        public AsyncRetryPolicy GetRetryPolicyForTask(int retryCount = 4, Func<int, TimeSpan>? sleepDurationProvider = null, Action<Exception, TimeSpan, int, Context>? onRetry = null)
        {
            return Policy
                .Handle<NpgsqlException>()
                .Or<IOException>()
                .Or<SocketException>()
                .Or<OperationCanceledException>()
                .WaitAndRetryAsync(
                    retryCount: retryCount,
                    sleepDurationProvider: sleepDurationProvider ?? (attempt => TimeSpan.FromSeconds(Math.Pow(1.8, attempt))),
                    onRetry: onRetry ?? ((exception, timeSpan, retryAttempt, context) =>
                    {
                        _logger.LogWarning(
                            "Intento {RetryCount} fallido . Reintentando en {Delay}s. Excepción: {ExceptionType}, Mensaje: {ExceptionMessage}.",
                            retryAttempt,
                            timeSpan.TotalSeconds,
                            exception.GetType().Name,
                            exception.Message
                        );
                    })
                );
        }
    }
}