using Asp.Api.Azul.Traspasos.Core.domain.dtos;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoPagoValidado
    {
        public string CveRastreo { get; set; }
        public string PagoJson { get; set; }
        public int IdTipoPago { get; set; }
        public DtoTraspasoAsp TraspasoAsp { get; set; }
        public string Proveedor { get; set; }
        public bool toAsp { get; set; }
        public string CloudDestino { get; set; }
        public string CloudOrigen { get; set; }


        public string AbonoEiyu { get; set; }
        public NotificacionAbonoTraspasoDto? TraspasosCore { get; set; }
        public DtoCambioEstado CambioEstadoTraspaso { get; set; }

    }
}
