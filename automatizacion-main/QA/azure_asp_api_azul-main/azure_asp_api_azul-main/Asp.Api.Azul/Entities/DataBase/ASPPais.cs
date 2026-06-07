using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class ASPPais
    {
        [DbColumn("pais_id")]
        public int PaisId { get; set; }
        [DbColumn("desc_pais")]
        public string? DescPais { get; set; }
    }
}
