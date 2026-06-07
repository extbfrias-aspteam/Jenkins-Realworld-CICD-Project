using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class ASPGiro
    {
        [DbColumn("desc_actividad")]
        public string? DescActividad { get; set; }
        [DbColumn("gir_id")]
        public int GiroId { get; set; }
        [DbColumn("desc_giro")]
        public string? DescGiro { get; set; }
        [DbColumn("act_id")]
        public int? ActId { get; set; }
        [DbColumn("clave_cnbv")]
        public string? ClaveCnbv { get; set; }
        [DbColumn("clave_fnd")]
        public string? ClaveFnd { get; set; }
    }
}
