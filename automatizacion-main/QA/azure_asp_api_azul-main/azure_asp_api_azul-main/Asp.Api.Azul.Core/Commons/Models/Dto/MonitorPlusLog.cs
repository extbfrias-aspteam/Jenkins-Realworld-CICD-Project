using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Core.Commons.Models.Dto
{
    public class MonitorPlusLog
    {
        public required string Endpoint { get; set; }
        public required string HttpMethod { get; set; }
        public required string RequestBody { get; set; }
        public required string ResponseBody { get; set; }
        public required int IdPblu { get; set; }
        public required int StatusCode { get; set; }
        public required string HeadersRequest { get; set; }
        public required string ClaveRastreo { get; set; }
        public required string ErrorMessage { get; set; }
        public required string StackTrace { get; set; }
        public required string InfoAdicional { get; set; }
    }
}
