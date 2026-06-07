namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpediente
    {
        public DtoCtaExpedientePersona persona { get; set; }
        public DtoCtaExpedienteDomicilio domicilio { get; set; }
        public List<DtoCtaExpedienteComprobantes> comprobantes { get; set; }
        public DtoCtaExpedientePerfil perfil { get; set; }
        public int udnId { get; set; }
        public string uuid { get; set; }
        public int nivel_cuenta { get; set; }
    }
}
