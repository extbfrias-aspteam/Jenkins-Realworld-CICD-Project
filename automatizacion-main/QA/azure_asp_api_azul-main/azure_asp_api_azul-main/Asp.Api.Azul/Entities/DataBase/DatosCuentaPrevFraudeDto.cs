using System.ComponentModel.DataAnnotations;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class DatosCuentaPrevFraudeDto
    {

        public required string Clabe { get; set; }

        public required DateTime FechaAltaClabe { get; set; }

        public required string TitularNombre { get; set; }

        public required string TitularTelefono { get; set; }

        public required string TitularCorreo { get; set; }


        public DateTime FechaOperacion { get; set; } = DateTime.Now;

        public required decimal Monto { get; set; }
    }
}
