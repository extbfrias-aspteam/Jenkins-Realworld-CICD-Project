using Asp.Api.Azul.Core.Commons.Models.Dto;
using Asp.Api.Azul.Entities.Business;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoDatosOriginalesPago
    {

        public required int IdPblu { get; set; }
        public required bool IsPortal {  get; set; }
        public required OrdenPagoDto OrdenPago { get; set; }
        public required string JsonPago { get; set; }
        public required string Firma {  get; set; }
        public required string Llave { get; set; }
    }
}
