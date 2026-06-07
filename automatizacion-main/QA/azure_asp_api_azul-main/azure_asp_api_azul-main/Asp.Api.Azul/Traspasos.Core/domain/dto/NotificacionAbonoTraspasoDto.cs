using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Traspasos.Core.domain.enums;
using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Traspasos.Core.domain.dtos
{
    public record NotificacionAbonoTraspasoDto
    {
        [JsonPropertyName("idPbluDestino")]
        public required int IdPbluDestino { get; set; }
        [JsonPropertyName("noNotificarAbono")]
        public required bool NoNotificarAbono { get; set; }
        [JsonPropertyName("abono")]
        public required DtoAbonoTraspaso Abono { get; set; }
        [JsonPropertyName("requestOri")]
        public required RequestOrigen RequestOri { get; set; }
    }
}
