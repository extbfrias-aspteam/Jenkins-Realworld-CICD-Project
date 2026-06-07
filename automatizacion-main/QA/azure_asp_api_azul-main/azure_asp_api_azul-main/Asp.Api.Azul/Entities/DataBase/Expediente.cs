using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class Expediente
    {
        [DbColumn("id")]
        public int Id { get; set; }
        [DbColumn("tipo")]
        public string? Tipo { get; set; }
        [DbColumn("archivo")]
        public string? Archivo { get; set; }
        [DbColumn("num_ideentificacion",50)]
        public string? NumIdentificacion { get; set; }
        [DbColumn("extension")]
        public string? Extension { get; set; }
        [DbColumn("id_persona")]
        public int? IdPersona { get; set; }
        [DbColumn("estado")]
        public int? Estado { get; set; }
        [DbColumn("clabe")]
        public string? Clabe { get; set; }
        [DbColumn("fecha_creacion")]
        public DateTime? FechaCreacion { get; set; }
        [DbColumn("rep_legal")]
        public string? RepLegal { get; set; }
    }
}
