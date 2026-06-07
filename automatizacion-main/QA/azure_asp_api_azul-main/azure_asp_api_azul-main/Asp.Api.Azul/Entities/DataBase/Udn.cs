namespace Asp.Api.Azul.Entities.DataBase
{
	public class Udn
	{
        public int IdUdn { get; set; }
        public string Descripcion { get; set; }
        public decimal Saldo { get; set; }
        public bool Activo { get; set; }
        public DateTime? FechaCreacion { get; set; }
        public string? UsuarioCreacion { get; set; }
		public int Pblu { get; set; }
		public decimal SaldoMin { get; set; }
        public bool NotificacionActiva { get; set; }
		public string? PrefijoClabe { get; set; }
		public int? ContadorClabe { get; set; }
        public string? Clabe { get; set; }
        public decimal? MontoLimite { get; set; }
        public bool? Bloqueado { get; set; }
    }
}
