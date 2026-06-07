using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
	public class Logs
	{
		[DbColumn("id_log")]
		public int IdLog { get; set; }
		[DbColumn("id_pblu")]
		public int IdPblu { get; set; }
		[DbColumn("id_error")]
		public int IdError { get; set; }
		[DbColumn("application",50)]
		public string? Application { get; set; }
		[DbColumn("cve_rastreo",4000)]
		public string CveRastreo { get; set; }
		[DbColumn("fh_log")]
		public DateTime? FechaLog { get; set; }
		[DbColumn("log_level",20)]
		public string LogLevel { get; set; }
		[DbColumn("fuente",1000)]
		public string Fuente { get; set; }
		
	}
}
