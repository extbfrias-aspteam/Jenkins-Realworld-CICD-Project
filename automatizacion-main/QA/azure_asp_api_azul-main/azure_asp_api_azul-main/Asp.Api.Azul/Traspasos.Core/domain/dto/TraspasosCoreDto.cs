using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Kafka.Dtos.Traspasos;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Traspasos.Core.domain.enums;

namespace Asp.Api.Azul.Traspasos.Core.domain.dtos
{
    public record TraspasosCoreDto
    {

        public required Pago Pago { get; set; }
        public required DtoAbonoTraspaso Abono { get; set; }
        public required int IdPbluOrigen { get; set; }
        public string? NombreOrdenante { get; set; }
        public required string CuentaBeneficiario { get; set; }
        public required RequestOrigen RequestOri { get; set; }
        public required TraspasoEiyuToAspDto TraspasoAsp { get; set; }
    }
}
