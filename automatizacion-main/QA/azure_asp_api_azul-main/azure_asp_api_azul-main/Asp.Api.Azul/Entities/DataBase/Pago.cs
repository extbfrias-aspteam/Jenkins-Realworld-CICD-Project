using System.Runtime.InteropServices.JavaScript;
using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
	[DbTable("pago")]
	public class Pago
	{
		[DbColumn("cve_rastreo",30)]
		public string CveRastreo { get; set; }
		[DbColumn("id_cve_rastreo",pk:true)]
		public int IdCveRastreo { get; set; }
		[DbColumn("cuenta_origen",18)]
		public string CuentaOrigen { get; set; }
		[DbColumn("concepto_pago",100)]
		public string? ConceptoPago { get; set; }
		[DbColumn("ref_cob",100)]
		public string? RefCob { get; set; }
		[DbColumn("ref_num",100)]
		public string? RefNum { get; set; }
		[DbColumn("nombre_destino",100)]
		public string? NombreDestino { get; set; }
		[DbColumn("rfc_destino",20)]
		public string? RfcDestino { get; set; }
		[DbColumn("cuenta_destino",100)]
		public string? CuentaDestino { get; set; }
		[DbColumn("fecha_operacion")]
		public DateTime? FechaOperacion { get; set; }
		[DbColumn("fecha_aceptado")]
		public DateTime? FechaAceptado { get; set; }
		[DbColumn("fecha_cancelado")]
		public DateTime? FechaCancelado { get; set; }
		[DbColumn("fecha_creacion")]
		public DateTime? FechaCreacion { get; set; }
		[DbColumn("fecha_liquidado")]
		public DateTime? FechaLiquidado { get; set; }
		[DbColumn("fecha_rechazo")]
		public DateTime? FechaRechazo { get; set; }
		[DbColumn("firma",800)]
		public string Firma { get; set; }
		[DbColumn("id_peticion")]
		public int? IdPeticion { get; set; }
		[DbColumn("iva")]
		public decimal? Iva { get; set; }
		[DbColumn("monto_cargo")]
		public decimal? MontoCargo { get; set; }
		[DbColumn("id_banco_origen")]
		public int IdBancoOrigen { get; set; }
		[DbColumn("id_banco_destino")]
		public int IdBancoDestino { get; set; }
		[DbColumn("id_causa_cancelacion")]
		public int? IdCausaCancelacion { get; set; }
		[DbColumn("id_causa_devolucion")]
		public int? IdCausaDevolucion { get; set; }
		[DbColumn("id_causa_rechazo")]
		public int? IdCausaRechazo { get; set; }
		[DbColumn("id_movimiento")]
		public long IdMovimiento { get; set; }
		[DbColumn("id_tipo_cuanta_destino")]
		public int IdTipoCuentaDestino { get; set; }
		[DbColumn("id_tipo_pago")]
		public int IdTipoPago { get; set; }
		[DbColumn("id_estado_pago")]
		public int IdEstadoPago { get; set; }
		[DbColumn("cve_rastreo_origen",255)]
		public string? CveRastreoOrigen { get; set; }
		[DbColumn("cve_rastreo_pblu",40)]
		public string? CveRastreoPblu { get; set; }
		[DbColumn("uuid",40)]
		public string? Uuid { get; set; }
		[DbColumn("json",1000)]
		public string? Json { get; set; }
		[DbColumn("estado_asp")]
		public int? EstadoAsp { get; set; }
		[DbColumn("estado_cacao")]
		public int? EstadoCacao { get; set; }
		[DbColumn("llave")]
		public int? Llave { get; set; }
		[DbColumn("estado_pblu")]
		public int? EstadoPblu { get; set; }
		[DbColumn("fecha_notificacion_pblu")]
		public DateTime? FechaNotificacionPblu { get; set; }
		[DbColumn("id_error")]
		public int? IdError { get; set; }
		[DbColumn("info_adicional",200)]
		public string? InfoAdicional { get; set; }
		[DbColumn("folio_paquete")]
		public int? FolioPaquete { get; set; }
		[DbColumn("folio")]
		public int? Folio { get; set; }
		[DbColumn("fecha_banxico")]
		public DateTime? FechaBanxico { get; set; }
		[DbColumn("id_pblu")]
		public int? IdPblu { get; set; }
		[DbColumn("id_udn")]
		public int? IdUdn { get; set; }
	}
}
