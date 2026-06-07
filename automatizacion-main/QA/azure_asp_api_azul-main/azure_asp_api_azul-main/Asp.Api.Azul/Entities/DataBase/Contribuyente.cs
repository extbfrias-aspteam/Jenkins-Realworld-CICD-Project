using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    [DbTable("contribuyente")]
    public class Contribuyente
    {
        [DbColumn("id_contribuyente", pk:true)]
        public int IdContribuyente { get; set; }
        [DbColumn("representante")]
        public int Representante { get; set; }
        [DbColumn("p_moral")]
        public int PersonaMoral { get; set; }
        [DbColumn("activo")]
        public bool Activo { get; set; }
        [DbColumn("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }
        [DbColumn("fecha_actualizacion")]
        public DateTime? FechaActualizacion { get; set; }
        [DbColumn("usuario_creacion",100)]
        public string? UsuarioCreacion { get; set; }
    }
}
