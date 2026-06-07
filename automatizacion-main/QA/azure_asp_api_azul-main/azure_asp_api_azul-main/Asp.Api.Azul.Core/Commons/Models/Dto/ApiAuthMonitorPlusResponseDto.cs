using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace Asp.Api.Azul.Core.Commons.Models.Dto
{
    public class ApiAuthMonitorPlusResponseDto
    {
        [JsonPropertyName("data")]
        public AuthDataMonitorPlusDto Data { get; set; }

        [JsonPropertyName("success")]
        public bool Success { get; set; }

        [JsonPropertyName("message")]
        public string Message { get; set; }

        [JsonPropertyName("code")]
        public int Code { get; set; }
    }
    
    public class AuthDataMonitorPlusDto
    {
        [JsonPropertyName("success")]
        public bool Success { get; set; }

        [JsonPropertyName("token")]
        public string Token { get; set; }
    }
}
