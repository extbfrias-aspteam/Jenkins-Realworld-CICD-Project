using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
	public class ErrorResponse
	{
		[JsonPropertyName("id_log")]
		public int IdLog { get; set; }
		[JsonPropertyName("id_error")]
		public int IdError { get; set; }
		[JsonPropertyName("cve_rastreo")]
		public string CveRastreo { get; set; }
		[JsonPropertyName("mensaje")]
		public string Mensaje { get; set; }
	}
}
