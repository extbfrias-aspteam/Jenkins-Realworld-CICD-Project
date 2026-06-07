using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Asp.Cifrado.Entities.DataBase
{
    public class ParametrosApi
    {
        public int IdParam { get; set; }
        public int Version { get; set; }
        public string Descripcion { get; set; }
        public bool Activo { get; set; }
        public string Valor { get; set; }
        public DateTime? FechaCreacion { get; set; }
        public string? UsuarioCreacion { get; set; }
    }
}
