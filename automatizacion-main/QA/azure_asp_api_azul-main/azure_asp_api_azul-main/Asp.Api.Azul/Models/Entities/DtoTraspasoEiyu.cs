namespace Asp.Api.Azul.Models.Entities
{
    public class DtoTraspasoEiyu
    {
        public required int IdPbluDestino { get; set; }
        public required bool NoNotificarAbono { get; set; }
        public required DtoAbonoTraspaso Abono { get; set; } 
        public required DtoCambioEstado CambioEstado { get; set; }
        
    }
}
