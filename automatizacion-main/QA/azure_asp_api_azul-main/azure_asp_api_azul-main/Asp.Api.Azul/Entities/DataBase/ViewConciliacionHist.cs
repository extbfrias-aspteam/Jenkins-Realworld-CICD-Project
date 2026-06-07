namespace Asp.Api.Azul.Entities.DataBase
{
	public class ViewConciliacionHist
	{
		public int? IdUdn { get; set; }
		public int? Pblu { get; set; }
		public string? Descripcion { get; set; }
		public DateTime? FechaOperacion { get; set; }
		public decimal? SaldoInicial { get; set; }
		public long? TotalAbonosMonto { get; set; }
		public decimal? MontoAbono { get; set; }
		public long? TotalCargosMonto { get; set; }
		public decimal? MontoCargo { get; set; }
		public decimal? Saldo { get; set; }
	}
}
