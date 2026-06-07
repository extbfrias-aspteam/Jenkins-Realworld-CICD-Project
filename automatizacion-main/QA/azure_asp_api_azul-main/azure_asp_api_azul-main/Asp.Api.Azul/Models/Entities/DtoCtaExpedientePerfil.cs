namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedientePerfil
    {
        public decimal ingresosMensuales { get; set; }
        public decimal montoMax { get; set; }
        public int cantOpMensual { get; set; } = 10;
    }
}
