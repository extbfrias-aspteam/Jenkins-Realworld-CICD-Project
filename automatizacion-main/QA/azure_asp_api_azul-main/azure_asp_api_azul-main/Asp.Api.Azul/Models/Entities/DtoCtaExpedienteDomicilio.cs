using System.ComponentModel.DataAnnotations;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedienteDomicilio
    {
        [StringLength(100, ErrorMessage = "La calle secundaria 2 no puede tener más de 100 caracteres.")]
        public string calleSecundaria2 { get; set; }
        [StringLength(100, ErrorMessage = "La calle secundaria 3 no puede tener más de 100 caracteres.")]
        public string calleSecundaria3 { get; set; }
        [StringLength(100, ErrorMessage = "La calle principal no puede tener más de 100 caracteres.")]
        public string callePrincipal { get; set; }
        public string colonia { get; set; }
        public string codPostal { get; set; }
        public string numExterior { get; set; }
        public string numInterior { get; set; }
        public string ciudad { get; set; }
    }
}
