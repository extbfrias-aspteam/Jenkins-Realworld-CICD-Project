using Microsoft.Extensions.Logging;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Infrastructure.Utils
{
    public class LoggerHelper
    {
        private readonly ILogger<LoggerHelper> _logger;
        private static readonly AsyncLocal<ConcurrentDictionary<string, string>> _context = new();

        public LoggerHelper(ILogger<LoggerHelper> logger)
        {
            _logger = logger;
        }

        private string GetOrCreateRequestId()
        {
            if (_context.Value == null)
            {
                _context.Value = new ConcurrentDictionary<string, string>();
            }

            if (!_context.Value.TryGetValue("RequestId", out string requestId))
            {
                requestId = Guid.NewGuid().ToString();
                _context.Value["RequestId"] = requestId;
            }

            return requestId;
        }

        public void LogInformation(string message,
            [CallerMemberName] string memberName = "",
            [CallerFilePath] string filePath = "",
            [CallerLineNumber] int lineNumber = 0)
        {
            var callerInfo = $"{System.IO.Path.GetFileName(filePath)}:{lineNumber}";
            var timestamp = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.ffffff");
            var requestId = GetOrCreateRequestId();

            _logger.LogInformation($"[{timestamp}] [ID: {requestId}] {message} ({callerInfo})");
        }

        public void LogError(string message, Exception ex,
            [CallerMemberName] string memberName = "",
            [CallerFilePath] string filePath = "",
            [CallerLineNumber] int lineNumber = 0)
        {
            var callerInfo = $"{System.IO.Path.GetFileName(filePath)}:{lineNumber}";
            var timestamp = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss.ffffff");
            var requestId = GetOrCreateRequestId();

            _logger.LogError(ex, $"[{timestamp}] [ID: {requestId}] {message} ({callerInfo})");
        }
    }
}
