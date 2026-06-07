using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Models.Entities
{
    public class DtoCtaExpedientePersona
    {
        
        public int idPaisNac { get; set; }

        public string correo { get; set; }
        public string telefono { get; set; }
        public string nombre { get; set; }
        public int idOcupacion { get; set; }
        public string celular { get; set; }
        public string sexo { get; set; }
        public int entidadNacimiento { get; set; }
        public string rfc { get; set; }
        public int idNacionalidad { get; set; }
        public string curp { get; set; }
        public string apellidoMaterno { get; set; }
        public string apellidoPaterno { get; set; }
        public string fechaNacimiento { get; set; }

       
        public string serieFirmaElect { get; set; }

       
        public string geolocalizacion { get; set; }

        
        public int tipoIdentificacionOf { get; set; }
        public string numIdentificacionOf { get; set; }
    }
}
