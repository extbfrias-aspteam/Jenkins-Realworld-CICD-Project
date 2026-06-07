namespace Asp.Api.Azul.Models.Entities
{
    public class ResponseDto
    {
        public int Codigo { get; set; } // 0 = Éxito, != 0 Error
        public string Mensaje { get; set; }
        public string Data { get; set; }
    }
}
