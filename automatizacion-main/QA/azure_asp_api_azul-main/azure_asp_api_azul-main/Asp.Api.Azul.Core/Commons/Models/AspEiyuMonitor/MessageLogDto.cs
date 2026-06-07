namespace Asp.Api.Azul.Core.Commons.Models.AspEiyuMonitor
{
    public class MessageLogDto
    {
        public DateTime Date { get; set; }
        public string IdPblu { get; set; }
        public string TrackingKey { get; set; }
        public string ApplicationCode { get; set; }
        public string ClassName { get; set; }
        public string MethodName { get; set; }
        public string LogLevel { get; set; }
        public string Message { get; set; }
        public string StackTrace { get; set; }
        public string LogMetadata { get; set; }
        public RequestDetailDto? RequestDetail { get; set; }
    }

    public class RequestDetailDto
    {
        public string Endpoint { get; set; }
        public string HttpMethod { get; set; }
        public string RequestBody { get; set; }
        public string ResponseBody { get; set; }
        public int StatusCode { get; set; }
        public string HeadersRequest { get; set; }
    }
}