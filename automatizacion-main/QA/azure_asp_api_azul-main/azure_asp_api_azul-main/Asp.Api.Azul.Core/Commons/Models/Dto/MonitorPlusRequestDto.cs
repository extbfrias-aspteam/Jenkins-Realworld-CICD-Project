using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Core.Commons.Models.Dto
{
    public class MonitorPlusRequestDto
    {
        [JsonPropertyName("Type")]
        public string Type { get; set; }

        [JsonPropertyName("PbluId")]
        public string PbluId { get; set; }

        [JsonPropertyName("CveRastreo")]
        public string CveRastreo { get; set; }

        [JsonPropertyName("Amount")]
        public decimal? Amount { get; set; }

        [JsonPropertyName("Transaction")]
        public TransactionDto Transaction { get; set; }
    }

    public class TransactionDto
    {
        [JsonPropertyName("header")]
        public TransactionHeaderDto Header { get; set; }

        [JsonPropertyName("Fields")]
        public TransactionFieldsDto Fields { get; set; }
    }

    public class TransactionHeaderDto
    {
        [JsonPropertyName("rtind")]
        public string? Rtind { get; set; }

        [JsonPropertyName("date")]
        public string? Date { get; set; }

        [JsonPropertyName("time")]
        public string? Time { get; set; }

        [JsonPropertyName("event")]
        public string? Event { get; set; }

        [JsonPropertyName("user")]
        public string? User { get; set; }

        [JsonPropertyName("nicknamenode")]
        public string? NicknameNode { get; set; }

        [JsonPropertyName("nicknamemodule")]
        public string? NicknameModule { get; set; }
    }

    public class TransactionFieldsDto
    {
        [JsonPropertyName("CodigoEntidad")]
        public string? CodigoEntidad { get; set; }

        [JsonPropertyName("IdCliente")]
        public string? IdCliente { get; set; }

        [JsonPropertyName("UsuarioBancaDigital")]
        public string? UsuarioBancaDigital { get; set; }

        [JsonPropertyName("CodigoClienteTitular")]
        public string? CodigoClienteTitular { get; set; }

        [JsonPropertyName("TipoPersona")]
        public string? TipoPersona { get; set; }

        [JsonPropertyName("OrigenDeTransaccion")]
        public string? OrigenDeTransaccion { get; set; }

        [JsonPropertyName("AccionDentroSesion")]
        public string? AccionDentroSesion { get; set; }

        [JsonPropertyName("CodigoTransaccion")]
        public string? CodigoTransaccion { get; set; }

        public string? CodigoRespuestaHost { get; set; }

        [JsonPropertyName("FechaTrx")]
        public string? FechaTrx { get; set; }

        [JsonPropertyName("HoraTrx")]
        public string? HoraTrx { get; set; }

        public string? MonedaTrx { get; set; }

        [JsonPropertyName("MontoTotalTrx")]
        public string? MontoTotalTrx { get; set; }

        public string? MontoTotalTrxUS { get; set; }
        public string? TipoCambio { get; set; }
        public string? LocalInternacional { get; set; }
        public string? TipoTransaccion { get; set; }

        [JsonPropertyName("NumeroReferencia")]
        public string? NumeroReferencia { get; set; }

        public string? ClaveTransferenciaInterbancaria { get; set; }
        public string? IDRastreoTransferencias { get; set; }

        [JsonPropertyName("Reversa")]
        public string? Reversa { get; set; }

        public string? DatosRetoMFA { get; set; }
        public string? EnviaPreguntasReto { get; set; }
        public string? RespuestasCorrectas { get; set; }
        public string? MFARetoSolicitado { get; set; }
        public string? MFATipoReto { get; set; }
        public string? MFAAprobado { get; set; }
        public string? MFAPrioridad { get; set; }

        [JsonPropertyName("CodigoProductoTitular")]
        public string? CodigoProductoTitular { get; set; }

        public string? CodigoSubproductoTitular { get; set; }

        [JsonPropertyName("NumerodeCuentaTitular")]
        public string? NumerodeCuentaTitular { get; set; }

        [JsonPropertyName("FechaAperturaCuentaTitular")]
        public string? FechaAperturaCuentaTitular { get; set; }

        public string? TipoVIPCuentaTitular { get; set; }

        [JsonPropertyName("FechaVinculacionCliente")]
        public string? FechaVinculacionCliente { get; set; }

        [JsonPropertyName("EstatusCuentaProductoTitular")]
        public string? EstatusCuentaProductoTitular { get; set; }

        public string? SignoValorSaldoCuentaTitular { get; set; }

        [JsonPropertyName("SaldoDisponibleCuentaTitular")]
        public string? SaldoDisponibleCuentaTitular { get; set; }

        [JsonPropertyName("EmpleadoCuentaTitular")]
        public string? EmpleadoCuentaTitular { get; set; }

        [JsonPropertyName("SucursalAperturaCuentaTitular")]
        public string? SucursalAperturaCuentaTitular { get; set; }

        [JsonPropertyName("NombreCompletoClienteCuentaTitular")]
        public string? NombreCompletoClienteCuentaTitular { get; set; }

        public string? EjecutivoCuentaTitular { get; set; }

        [JsonPropertyName("TelefonoLaboralCuentaTitular")]
        public string? TelefonoLaboralCuentaTitular { get; set; }

        [JsonPropertyName("TelefonoCelularCuentaTitular")]
        public string? TelefonoCelularCuentaTitular { get; set; }

        [JsonPropertyName("TelefonoResidenciaCuentaTitular")]
        public string? TelefonoResidenciaCuentaTitular { get; set; }

        [JsonPropertyName("SegmentoClienteCuentaTitular")]
        public string? SegmentoClienteCuentaTitular { get; set; }

        [JsonPropertyName("CorreoElectronicoCuentaTitular")]
        public string? CorreoElectronicoCuentaTitular { get; set; }

        [JsonPropertyName("DominioEMailCuentaTitular")]
        public string? DominioEMailCuentaTitular { get; set; }

        public string? FechaAperturaCuentaDestino { get; set; }
        public string? EjecutivoCuentaDestino { get; set; }
        public string? SucursalAperturaCuentaDestino { get; set; }
        public string? TelefonoCuentaDestino { get; set; }
        public string? EmailCuentaDestino { get; set; }
        public string? DominioEmailDestino { get; set; }
        public string? CodigoPaisDestino { get; set; }
        public string? CodClienteDestino { get; set; }
        public string? EmpleadoCtaDestino { get; set; }
        public string? BancoDestino { get; set; }

        [JsonPropertyName("TipoProductoDestino")]
        public string? TipoProductoDestino { get; set; }

        public string? NumeroCuentaDestino { get; set; }

        [JsonPropertyName("NombreClienteTitularDestino")]
        public string NombreClienteTitularDestino { get; set; } = "X";

        public string? ProveedorServicio { get; set; }

        [JsonPropertyName("CategoriaPagoServicio")]
        public string? CategoriaPagoServicio { get; set; }

        [JsonPropertyName("ReferenciaPagoServicio1")]
        public string? ReferenciaPagoServicio1 { get; set; }

        [JsonPropertyName("ReferenciaPagoServicio2")]
        public string? ReferenciaPagoServicio2 { get; set; }

        [JsonPropertyName("ReferenciaPagoServicio3")]
        public string? ReferenciaPagoServicio3 { get; set; }

        [JsonPropertyName("Origendealerta")]
        public string? Origendealerta { get; set; }

        public string? IdentTrxFraude { get; set; }
        public string? IndicadordeFraude { get; set; }
        public string? IndicadorRealTime { get; set; }

        public string? IPConexion { get; set; }

        [JsonPropertyName("Correlativo")]
        public string? Correlativo { get; set; }

        [JsonPropertyName("SesionID")]
        public string? SesionID { get; set; }

        public string? PaymentSource { get; set; }
        public string? Channel { get; set; }
        public string? IntegrationType { get; set; }
        public string? AuthenticatedService { get; set; }
        public string? AuthenticationMethod { get; set; }
        public string? ApiKeyPresent { get; set; }
        public string? UserInteraction { get; set; }
        public string? IdTransaccion { get; set; }


        [JsonPropertyName("fingerPrint")]
        public FingerPrintDto FingerPrint { get; set; } = new();
    }

    public class FingerPrintDto
    {

        [JsonPropertyName("Browser")]
        public FingerPrintBrowserDto Browser { get; set; } = new();

        [JsonPropertyName("General")]
        public FingerPrintGeneralDto General { get; set; } = new();

        [JsonPropertyName("Personalization")]
        public FingerPrintPersonalizationDto Personalization { get; set; } = new();

        [JsonPropertyName("Alterations")]
        public FingerPrintAlterationsDto Alterations { get; set; } = new();

        [JsonPropertyName("Identifiers")]
        public FingerPrintIdentifiersDto Identifiers { get; set; } = new();

        [JsonPropertyName("Network")]
        public FingerPrintNetworkDto Network { get; set; } = new();

        [JsonPropertyName("Site")]
        public FingerPrintSiteDto Site { get; set; } = new();

        [JsonPropertyName("Geoip")]
        public FingerPrintGeoipDto Geoip { get; set; } = new();
    }

    public class FingerPrintGeneralDto
    {
        [JsonPropertyName("fingerprintVersion")]
        public string FingerprintVersion { get; set; } = "3.1.0";

        [JsonPropertyName("language")]
        public string Language { get; set; } = "es-ES";

        [JsonPropertyName("colorDepth")]
        public string ColorDepth { get; set; } = "24";

        [JsonPropertyName("deviceMemory")]
        public string DeviceMemory { get; set; } = "8";

        [JsonPropertyName("hardwareConcurrency")]
        public string HardwareConcurrency { get; set; } = "4";

        [JsonPropertyName("resolution")]
        public string Resolution { get; set; } = "1366x768";

        [JsonPropertyName("availableResolution")]
        public string AvailableResolution { get; set; } = "1366x728";

        [JsonPropertyName("timezoneOffset")]
        public string TimezoneOffset { get; set; } = "300";

        [JsonPropertyName("sessionStorage")]
        public string SessionStorage { get; set; } = "0";

        [JsonPropertyName("cookieEnabled")]
        public string CookieEnabled { get; set; } = "1";

        [JsonPropertyName("localStorage")]
        public string LocalStorage { get; set; } = "1";

        [JsonPropertyName("indexedDb")]
        public string IndexedDb { get; set; } = "0";

        [JsonPropertyName("cpuClass")]
        public string CpuClass { get; set; } = "1";

        [JsonPropertyName("openDatabase")]
        public string OpenDatabase { get; set; } = "0";

        [JsonPropertyName("navigatorPlatform")]
        public string NavigatorPlatform { get; set; } = "Win32";

        [JsonPropertyName("vendorWebGL")]
        public string VendorWebGL { get; set; } = "1";

        [JsonPropertyName("rendererVideo")]
        public string RendererVideo { get; set; } = "ANGLE (Intel, Intel(R) HD Graphics 4000 (0x00000166) Direct3D11 vs_5_0 ps_5_0, D3D11)";

        [JsonPropertyName("timeZone")]
        public string TimeZone { get; set; } = "GMT-0500 (hora est%C3%A1ndar oriental)";

        [JsonPropertyName("zone")]
        public string Zone { get; set; } = "America/Panama";

        [JsonPropertyName("utc")] // En minúsculas para coincidir con el JSON
        public string Utc { get; set; } = "-5";

        [JsonPropertyName("ram")]
        public string Ram { get; set; } = "8";

        [JsonPropertyName("processorCount")]
        public string ProcessorCount { get; set; } = "4";

        [JsonPropertyName("videoInput")]
        public string VideoInput { get; set; } = "1";

        [JsonPropertyName("audio")]
        public string Audio { get; set; } = "1";

        [JsonPropertyName("canvas")]
        public string Canvas { get; set; } = "1565439318";
    }

    public class FingerPrintPersonalizationDto
    {
        [JsonPropertyName("numberPlugins")]
        public string NumberPlugins { get; set; } = "5";

        [JsonPropertyName("numberFonts")]
        public string NumberFonts { get; set; } = "48";
    }

    public class FingerPrintAlterationsDto
    {
        [JsonPropertyName("adblock")]
        public string Adblock { get; set; } = "0";

        [JsonPropertyName("hasLiedLanguages")]
        public string HasLiedLanguages { get; set; } = "0";

        [JsonPropertyName("hasLiedResolution")]
        public string HasLiedResolution { get; set; } = "0";

        [JsonPropertyName("hasLiedOs")]
        public string HasLiedOs { get; set; } = "0";

        [JsonPropertyName("hasLiedBrowser")]
        public string HasLiedBrowser { get; set; } = "0";

        [JsonPropertyName("touchSupport")]
        public string TouchSupport { get; set; } = "0";
    }

    public class FingerPrintIdentifiersDto
    {
        [JsonPropertyName("hash")]
        public string Hash { get; set; } = "2C4CF65733783512.9D174085C186E036.53";

        [JsonPropertyName("cookie")]
        public string Cookie { get; set; } = "13f72aaeea4e5adccedc5c72778571b8";

        [JsonPropertyName("localStorageValue")]
        public string LocalStorageValue { get; set; } = "669c07f4efccf2a08cb5c8a831ac4927";

        [JsonPropertyName("anonimity1")]
        public string Anonimity1 { get; set; } = "4efd577a8f895b2b14e4bac7925723bfcc8eb123c0c46aa4c426c947d0e22264";

        [JsonPropertyName("anonimity2")]
        public string Anonimity2 { get; set; } = "e26d3a9d5be99ee04102be139eb75da4c0be4cf4c978d1de867a8f002595b0dd";

        [JsonPropertyName("anonimity3")]
        public string Anonimity3 { get; set; } = "67860ed17d8cc7e971486782f9d881c08f6104ccbe5a8b611115636ed7c28ff4";

        [JsonPropertyName("anonimity5")]
        public string Anonimity5 { get; set; } = "1";
    }

    public class FingerPrintNetworkDto
    {
        public string localIp { get; set; } = "1";
        public string publicIp { get; set; } = "1";
    }

    public class FingerPrintSiteDto
    {
        public string host { get; set; } = "10.7.57.19";
        public string hostName { get; set; } = "10.7.57.19";
        public string href { get; set; } = "https://10.7.57.19/blinea/ACH_SGB.bca_soltrans_local_trx";
        public string origin { get; set; } = "https://10.7.57.19/blinea/ACH_SGB.Bca_SolTrans_Local_2";
        public string pathname { get; set; } = "/blinea/ACH_SGB.bca_soltrans_local_trx";
        public string port { get; set; } = "1";
        public string protocol { get; set; } = "https:";
    }

    public class FingerPrintGeoipDto
    {
        [JsonPropertyName("as")]
        public string As { get; set; } = "AS8048 CANTV Servicios, Venezuela";

        [JsonPropertyName("currency")]
        public string Currency { get; set; } = "VEF";

        [JsonPropertyName("isp")]
        public string Isp { get; set; } = "CANTV Servicios, Venezuela";

        [JsonPropertyName("country")]
        public string Country { get; set; } = "Venezuela";

        [JsonPropertyName("region")]
        public string Region { get; set; } = "A";

        [JsonPropertyName("lon")]
        public string Lon { get; set; } = "-66.8738";

        [JsonPropertyName("mobile")]
        public string Mobile { get; set; } = "0";

        [JsonPropertyName("proxy")]
        public string Proxy { get; set; } = "0";

        [JsonPropertyName("org")]
        public string Org { get; set; } = "CANTV Servicios, Venezuela";

        [JsonPropertyName("district")]
        public string District { get; set; } = "1";

        [JsonPropertyName("continent")]
        public string Continent { get; set; } = "South America";

        [JsonPropertyName("regionName")]
        public string RegionName { get; set; } = "Distrito Federal";

        [JsonPropertyName("countryCode3")]
        public string CountryCode3 { get; set; } = "VEN";

        [JsonPropertyName("reverse")]
        public string Reverse { get; set; } = "190-72-90-98.lms-01-p68.cantv.net";

        [JsonPropertyName("asname")]
        public string Asname { get; set; } = "CANTV Servicios, Venezuela";

        [JsonPropertyName("offset")]
        public string Offset { get; set; } = "-14400";

        [JsonPropertyName("timezone")]
        public string Timezone { get; set; } = "America/Caracas";

        [JsonPropertyName("city")]
        public string City { get; set; } = "Caracas";

        [JsonPropertyName("status")]
        public string Status { get; set; } = "success";

        [JsonPropertyName("continentCode")]
        public string ContinentCode { get; set; } = "SA";

        [JsonPropertyName("query")]
        public string Query { get; set; } = "190.72.90.98";

        [JsonPropertyName("hosting")]
        public string Hosting { get; set; } = "0";

        [JsonPropertyName("currentTime")]
        public string CurrentTime { get; set; } = "2025-11-13T13:54:14-04:00";

        [JsonPropertyName("zip")]
        public string Zip { get; set; } = "1";

        [JsonPropertyName("lat")]
        public string Lat { get; set; } = "10.4873";

        [JsonPropertyName("countryCode")]
        public string CountryCode { get; set; } = "VE";

        [JsonPropertyName("callingCode")]
        public string CallingCode { get; set; } = "58";
    }

    public class FingerPrintBrowserDto
    {
        public string userAgent { get; set; } = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
        public string browserName { get; set; } = "Chrome";
        public string browserVersion { get; set; } = "120.0.0.0";
        public string browserMajor { get; set; } = "120";
        public string browserEngineName { get; set; } = "WebKit";
        public string browserEngineVersion { get; set; } = "537.36";
        public string osName { get; set; } = "Windows";
        public string osVersion { get; set; } = "10";
        public string deviceVendor { get; set; } = "1";
        public string deviceModel { get; set; } = "1";
        public string deviceType { get; set; } = "1";
        public string cpuArchitecture { get; set; } = "amd64";
        public string isPrivateMode { get; set; } = "1";
    }
}
