using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Entities.Business
{
    public class OrdenPagoSIES
    {
        [JsonPropertyName("cveEntidad")]
        public string CveEntidad { get; set; }
        [JsonPropertyName("idEmpresa")]
        public string IdEmpresa { get; set; }
        [JsonPropertyName("folio")]
        public string Folio { get; set; }
        [JsonPropertyName("nombreOrdenante")]
        public string NombreOrdenante { get; set; }
        [JsonPropertyName("idTipoCuentaOrdenante")]
        public string IdTipoCuentaOrdenante { get; set; }
        [JsonPropertyName("cuentaOrdenante")]
        public string CuentaOrdenante { get; set; }
        [JsonPropertyName("rfcOrdenante")]
        public string RfcOrdenante { get; set; }
        [JsonPropertyName("nombreBeneficiario")]
        public string NombreBeneficiario { get; set; }
        [JsonPropertyName("idTipoCuentaBeneficiario")]
        public string IdTipoCuentaBeneficiario { get; set; }
        [JsonPropertyName("cuentaBeneficiario")]
        public string CuentaBeneficiario { get; set; }
        [JsonPropertyName("rfcBeneficiario")]
        public string RfcBeneficiario { get; set; }
        [JsonPropertyName("idAreaEmite")]
        public string IdAreaEmite { get; set; }
        [JsonPropertyName("conceptoPago")]
        public string ConceptoPago { get; set; }
        [JsonPropertyName("monto")]
        public string Monto { get; set; }
        [JsonPropertyName("iva")]
        public string Iva { get; set; }
        [JsonPropertyName("referenciaNumerica")]
        public string ReferenciaNumerica { get; set; }
        [JsonPropertyName("referenciaCobranza")]
        public string ReferenciaCobranza { get; set; }
        [JsonPropertyName("referenciaCobranza1")]
        public string ReferenciaCobranza1 { get; set; }

        [JsonPropertyName("idTipoPago")]
        public string IdTipoPago { get; set; }
        [JsonPropertyName("topologia")]
        public string Topologia { get; set; }
        [JsonPropertyName("prioridad")]
        public string Prioridad { get; set; }
        [JsonPropertyName("devolucion")]
        public string Devolucion { get; set; }
        [JsonPropertyName("cancelacion")]
        public string Cancelacion { get; set; }
        [JsonPropertyName("respCancelacion")]
        public string RespCancelacion { get; set; }
        [JsonPropertyName("envio")]
        public string Envio { get; set; }
        [JsonPropertyName("respEnvio")]
        public string RespEnvio { get; set; }
        [JsonPropertyName("reenvio")]
        public string Reenvio { get; set; }
        [JsonPropertyName("respReenvio")]
        public string RespReenvio { get; set; }
        [JsonPropertyName("cerrada")]
        public string Cerrada { get; set; }
        [JsonPropertyName("respBanxico")]
        public string RespBanxico { get; set; }
        [JsonPropertyName("clavePago")]
        public string ClavePago { get; set; }
        [JsonPropertyName("idTipoOperacion")]
        public string IdTipoOperacion { get; set; }
        [JsonPropertyName("fechaCaptura")]
        public string FechaCaptura { get; set; }
        [JsonPropertyName("nombreBeneficiario2")]
        public string NombreBeneficiario2 { get; set; }
        [JsonPropertyName("rfcBeneficiario2")]
        public string RfcBeneficiario2 { get; set; }
        [JsonPropertyName("cuentaBeneficiario2")]
        public string CuentaBeneficiario2 { get; set; }
        [JsonPropertyName("conceptoPago2")]
        public string ConceptoPago2 { get; set; }
        [JsonPropertyName("idTipoCuentaBeneficiario2")]
        public string IdTipoCuentaBeneficiario2 { get; set; }
        [JsonPropertyName("cveRastreo")]
        public string CveRastreo { get; set; }
        [JsonPropertyName("idDevolucion")]
        public string IdDevolucion { get; set; }
        [JsonPropertyName("idInstitucionOrd")]
        public string IdInstitucionOrd { get; set; }
        [JsonPropertyName("idInstitucionBen")]
        public string IdInstitucionBen { get; set; }
        [JsonPropertyName("idTipoSiac")]
        public string IdTipoSiac { get; set; }
        [JsonPropertyName("cveRastreo2")]
        public string CveRastreo2 { get; set; }
        [JsonPropertyName("infAdicional")]
        public string InfAdicional { get; set; }
        [JsonPropertyName("nomArchivo")]
        public string NomArchivo { get; set; }
        [JsonPropertyName("envioBanxico")]
        public string EnvioBanxico { get; set; }
        [JsonPropertyName("verificado")]
        public string Verificado { get; set; }
        [JsonPropertyName("cancelacionBanxico")]
        public string CancelacionBanxico { get; set; }
        [JsonPropertyName("respCancelacionBanxico")]
        public string RespCancelacionBanxico { get; set; }
        [JsonPropertyName("reenvioBanxico")]
        public string ReenvioBanxico { get; set; }
        [JsonPropertyName("respReenvioBanxico")]
        public string RespReenvioBanxico { get; set; }
        [JsonPropertyName("detenido")]
        public string Detenido { get; set; }
        [JsonPropertyName("usuario")]
        public string Usuario { get; set; }
        [JsonPropertyName("reparar")]
        public string Reparar { get; set; }
        [JsonPropertyName("folioPaquete")]
        public string FolioPaquete { get; set; }
        [JsonPropertyName("firma")]
        public string Firma { get; set; }
        [JsonPropertyName("status")]
        public string Status { get; set; }
        [JsonPropertyName("folioBanxico")]
        public string FolioBanxico { get; set; }
        [JsonPropertyName("folioServidor")]
        public string FolioServidor { get; set; }
        [JsonPropertyName("prioridadEnvio")]
        public string PrioridadEnvio { get; set; }
        [JsonPropertyName("saldoSpei")]
        public string SaldoSpei { get; set; }
        [JsonPropertyName("idFolioMonex")]
        public string IdFolioMonex { get; set; }
        [JsonPropertyName("bConfirmacion")]
        public string BConfirmacion { get; set; }
        [JsonPropertyName("idMultiempresa")]
        public string IdMultiempresa { get; set; }
        [JsonPropertyName("folioSolicitud")]
        public string FolioSolicitud { get; set; }
        [JsonPropertyName("folioPaqueteDevExt")]
        public string FolioPaqueteDevExt { get; set; }
        [JsonPropertyName("montoOriginalDevExt")]
        public string MontoOriginalDevExt { get; set; }
        [JsonPropertyName("montoInteresDevExt")]
        public string MontoInteresDevExt { get; set; }
        [JsonPropertyName("fechaOperacion")]
        public string FechaOperacion { get; set; }
        [JsonPropertyName("fechaOperacionDevExt")]
        public string FechaOperacionDevExt { get; set; }
        [JsonPropertyName("firmaCoreBancario")]
        public string FirmaCoreBancario { get; set; }
        [JsonPropertyName("token1")]
        public string Token1 { get; set; }
        [JsonPropertyName("token2")]
        public string Token2 { get; set; }
        [JsonPropertyName("ArchivoHibernate")]
        public string ArchivoHibernate { get; set; }
        [JsonPropertyName("uetrSwift")]
        public string UetrSwift { get; set; }
        [JsonPropertyName("texto1Swift")]
        public string Texto1Swift { get; set; }
        [JsonPropertyName("texto2Swift")]
        public string Texto2Swift { get; set; }
        [JsonPropertyName("dvBeneficiario")]
        public string DvBeneficiario { get; set; }
        [JsonPropertyName("dvOrdenante")]
        public string DvOrdenante { get; set; }
        [JsonPropertyName("pagoComision")]
        public string PagoComision { get; set; }
        [JsonPropertyName("folioCobroSpei")]
        public string FolioCobroSpei { get; set; }
        [JsonPropertyName("fhLimite")]
        public string FhLimite { get; set; }
        [JsonPropertyName("montoComision")]
        public string MontoComision { get; set; }
        [JsonPropertyName("aliasNumCelOrdenante")]
        public string AliasNumCelOrdenante { get; set; }
        [JsonPropertyName("aliasNumCelBeneficiario")]
        public string AliasNumCelBeneficiario { get; set; }
        [JsonPropertyName("aliasNumSerieCertificado")]
        public string AliasNumSerieCertificado { get; set; }
        [JsonPropertyName("instancia")]
        public string Instancia { get; set; }
        [JsonPropertyName("nombreOrdenanteIndirecto")]
        public string NombreOrdenanteIndirecto { get; set; }
        [JsonPropertyName("idTipoCuentaOrdIndirecto")]
        public string IdTipoCuentaOrdIndirecto { get; set; }
        [JsonPropertyName("cuentaOrdenanteIndirecto")]
        public string CuentaOrdenanteIndirecto { get; set; }
        [JsonPropertyName("rfcOrdenanteIndirecto")]
        public string RfcOrdenanteIndirecto { get; set; }
        [JsonPropertyName("identificadorRemesa")]
        public string IdentificadorRemesa { get; set; }
        [JsonPropertyName("pais")]
        public string Pais { get; set; }
        [JsonPropertyName("divisa")]
        public string Divisa { get; set; }
        [JsonPropertyName("nombreBeneficiarioRemesa")]
        public string NombreBeneficiarioRemesa { get; set; }
        [JsonPropertyName("nombreProveedorRemesaExt")]
        public string NombreProveedorRemesaExt { get; set; }
        [JsonPropertyName("nombreProveedorRemesaNal")]
        public string NombreProveedorRemesaNal { get; set; }
        [JsonPropertyName("nombreCteIndRecDis")]
        public string NombreCteIndRecDis { get; set; }
        [JsonPropertyName("tipoCambio")]
        public string TipoCambio { get; set; }
    }
}
