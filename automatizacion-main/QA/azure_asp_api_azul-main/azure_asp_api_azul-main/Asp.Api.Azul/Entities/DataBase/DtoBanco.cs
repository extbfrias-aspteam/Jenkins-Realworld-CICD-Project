using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class DtoBanco
    {
        [DbColumn("IdBanco")]
        public int IdBanco { get; set; }
        [DbColumn("descripcion")]
        public string Descripcion { get; set; }
    }
}
