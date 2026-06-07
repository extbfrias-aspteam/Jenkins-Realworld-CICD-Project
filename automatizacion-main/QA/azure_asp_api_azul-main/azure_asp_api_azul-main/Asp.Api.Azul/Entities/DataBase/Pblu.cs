namespace Asp.Api.Azul.Entities.DataBase
{
	public class Pblu
	{
		public int IdPblu { get; set; }
		public int? PrefijoPblu { get; set; }
		public int IdTipoPart { get; set; }
		public string ClabePblu { get; set; }
		public string Descripcion { get; set; }
		public bool Activo { get; set; }
		public DateTime? FechaCreacion { get; set; }
		public string? UsuarioCreacion { get; set; }
		public int? ContadorUdn { get; set; }
		public string? Rfc { get; set; }
		public string? Domicilio { get; set; }
		public string? Localidad { get; set; }
		public string? Colonia { get; set; }
		public string? CodigoPostal { get; set; }
		public string? Telefono { get; set; }
		public string? Email { get; set; }
		public DateTime? FechaInicioMonto { get; set; }
		public DateTime? FechaFinMonto { get; set; }
		public decimal? BluLimite { get; set; }
		public decimal? LimiteOperativo { get; set; }
        public bool? Bloqueado { get; set; }
    }
}
