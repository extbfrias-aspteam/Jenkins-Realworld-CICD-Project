using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    [DbTable("error")]
    public class ErrorLog
    {
        [DbColumn("id", pk: true)]
        public long Id { get; set; }
        [DbColumn("endpoint")]
        public string? EndPoint { get; set; }
        [DbColumn("http_method")]
        public string? HttpMethod { get; set; }
        [DbColumn("request")]
        public string? Request { get; set; }
        [DbColumn("method")]
        public string? Method { get; set; }
        [DbColumn("message")]
        public string? Message { get; set; }
        [DbColumn("stack_trace")]
        public string? StackTrace { get; set; }
        [DbColumn("fecha")]
        public DateTime? Fecha { get; set; }
        [DbColumn("id_pblu")]
        public int? idPblu { get; set; }
        [DbColumn("header_request")]
        public string? HeadersRequest { get; set; }
        [DbColumn("clave_rastreo")]
        public string? ClaveRastreo { get; set; }
    }
}
