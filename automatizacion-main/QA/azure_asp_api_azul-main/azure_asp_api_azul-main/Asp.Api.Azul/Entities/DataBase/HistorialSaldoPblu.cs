using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
	public class HistorialSaldoPblu
	{
		[DbColumn("id_udn")]
		public int IdUdn { get; set; }
		[DbColumn("fecha_creacion")]
		public DateTime? FechaCreacion { get; set; }
		[DbColumn("fecha_operativa")]
		public DateTime FechaOperativa { get; set; }
		[DbColumn("saldo_inicial")]
		public decimal? SaldoInicial { get; set; }
		[DbColumn("saldo_final")]
		public decimal? SaldoFinal { get; set; }
		[DbColumn("usuario_creacion",100)]
		public string? UsuarioCreacion { get; set; }
	}
}
