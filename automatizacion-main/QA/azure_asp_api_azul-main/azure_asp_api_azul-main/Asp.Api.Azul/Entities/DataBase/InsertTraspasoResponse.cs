namespace Asp.Api.Azul.Entities.DataBase
{
    public class InsertTraspasoResponse
    {
        public required string Mensaje { get; set; }
        public required int IdPbluDestino { get; set; }
        public required string CloudDestino { get; set; }
    }
}
