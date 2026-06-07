using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    [DbTable("pago")]
    public class PagoLog
    {
        [DbColumn("id", pk: true)]
        public long Id { get; set; }
        [DbColumn("fecha")]
        public DateTime? Fecha { get; set; }
        [DbColumn("endpoint")]
        public string? EndPoint { get; set; }
        [DbColumn("http_method")]
        public string? HttpMethod { get; set; }
        [DbColumn("request")]
        public string? Request { get; set; }
        [DbColumn("response")]
        public string? Response { get; set; }
        [DbColumn("id_pblu")]
        public int IdPblu { get; set; }
        [DbColumn("headers_request")]
        public string? HeadersRequest { get; set; }
        [DbColumn("headers_response")]
        public string? HeadersResponse { get; set; }
        [DbColumn("clave_rastreo")]
        public string? ClaveRastreo { get; set; }
    }
}
