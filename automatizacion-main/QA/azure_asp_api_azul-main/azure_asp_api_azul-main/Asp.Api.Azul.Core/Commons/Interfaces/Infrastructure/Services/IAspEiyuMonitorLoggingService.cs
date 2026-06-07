using System.Runtime.CompilerServices;
using Asp.Api.Azul.Core.Commons.Models.AspEiyuMonitor;

namespace Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services
{
    public interface IAspEiyuMonitorLoggingService
    {
        Task SendInfoGeneric(string message, dynamic metaData = null, [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendErrorGeneric(string message, string stackTrace, dynamic metaData = null, [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendInfo(string idPblu, string trackingKey, string message, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendError(string idPblu, string trackingKey, string message, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendWarning(string idPblu, string trackingKey, string message, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendRequestInfo(string idPblu, string trackingKey, string message, RequestDetailDto requestDetail, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendRequestError(string idPblu, string trackingKey, string message, RequestDetailDto requestDetail, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
        Task SendRequestWarning(string idPblu, string trackingKey, string message, RequestDetailDto requestDetail, dynamic metaData = null, string stackTrace = "", [CallerMemberName] string callerMethod = "", [CallerFilePath] string callerFilePath = "");
    }
}