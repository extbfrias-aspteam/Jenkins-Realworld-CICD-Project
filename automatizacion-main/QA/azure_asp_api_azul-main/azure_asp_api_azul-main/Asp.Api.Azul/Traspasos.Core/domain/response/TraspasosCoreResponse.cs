using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Traspasos.Core.domain.response
{
    public class TraspasosCoreResponse
    {

        [JsonPropertyName("success")]
        public required bool Success { get; set; }

        [JsonPropertyName("message")]
        public required string Message { get; set; }

        [JsonPropertyName("timestamp")]
        public DateTime Timestamp { get; set; } = DateTime.UtcNow;

        [JsonPropertyName("transactionId")]
        public string? TransactionId { get; set; }

    }
}
