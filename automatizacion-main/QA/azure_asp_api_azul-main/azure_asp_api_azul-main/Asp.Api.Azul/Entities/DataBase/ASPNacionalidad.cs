using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class ASPNacionalidad
    {
        [DbColumn("id_nacionalidad")]
        public int IdNacionalidad { get; set; }
        [DbColumn("desc_nacionalidad")]
        public string? DescNacionalidad { get; set; }
        [DbColumn("pais_id")]
        public int? PaisId { get; set; }
    }
}
