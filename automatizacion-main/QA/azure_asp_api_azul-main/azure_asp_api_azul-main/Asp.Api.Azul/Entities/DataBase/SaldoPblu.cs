namespace Asp.Api.Azul.Entities.DataBase
{
	public class SaldoPblu
	{
		public int IdPblu { get; set; }
		public decimal Saldo { get; set; }
		public DateTime? FechaCreacion { get; set; }
		public string? UsuarioCreacion { get; set; }
	}
}
