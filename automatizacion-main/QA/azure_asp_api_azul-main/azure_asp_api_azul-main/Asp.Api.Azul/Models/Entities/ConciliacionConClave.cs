using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Models.Entities
{
	public class ConciliacionConClave
	{
		[JsonPropertyName("clave_rastreo")]
		public string ClaveRastreo { get; set; }
	}
}
