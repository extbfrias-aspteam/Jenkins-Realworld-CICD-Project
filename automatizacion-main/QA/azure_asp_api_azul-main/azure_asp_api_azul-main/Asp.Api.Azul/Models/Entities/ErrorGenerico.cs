using Asp.Api.Azul.Models.Entities;
using System.Runtime.ConstrainedExecution;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace Asp.Api.Azul.Models.Entities
{
    public class ErrorGenerico : Exception
    {
        public string CveRastreo { get; set; } = "default";
        public int Pblu { get; set; }
        public int IdError { get; set; }

        public ErrorGenerico(int pblu, string mensaje, string cveRastreo) : base(mensaje)
        {
            Pblu = pblu;
            CveRastreo = cveRastreo;
        }

        public ErrorGenerico(int pblu, string mensaje) : base(mensaje)
        {
            Pblu = pblu;
        }

        public ErrorGenerico(string mensaje) : base(mensaje)
        {

        }
    }
    public class CertificadoDobleError : ErrorGenerico
    {
        public CertificadoDobleError(string mensaje) : base(mensaje)
        {
            this.IdError = 16;
        }
        public CertificadoDobleError(int id, string mensaje) : base(id, mensaje)
        {
            this.IdError = 16;
        }
    }
    public class ErrorAlGuardarCertificado : ErrorGenerico
    {

        public ErrorAlGuardarCertificado(string mensaje) : base(mensaje)
        {
            this.IdError = 20;
        }
    }
    public class ErrorBlackList : ErrorGenerico
    {
        public ErrorBlackList(string mensaje) : base(mensaje)
        {
            this.IdError = 202;
        }
    }
    public class ErrorCierreDiaSinInicio : ErrorGenerico
    {
        public ErrorCierreDiaSinInicio(string mensaje) : base(mensaje)
        {
            this.IdError = -1;
        }
    }
    public class ErrorCifrado : ErrorGenerico
    {
        public ErrorCifrado(string mensaje) : base(mensaje)
        {
            this.IdError = 29;
        }
    }
    public class ErrorClabeSinUDN : ErrorGenerico
    {
        public ErrorClabeSinUDN(string mensaje) : base(mensaje)
        {
            this.IdError = 4;
        }
    }
    public class ErrorClaveRastreoNoExiste : ErrorGenerico
    {
        public ErrorClaveRastreoNoExiste(string mensaje) : base(mensaje)
        {
            this.IdError = 26;
        }
    }
    public class ErrorComunicacionPSPSEI : ErrorGenerico
    {
        public ErrorComunicacionPSPSEI(string mensaje) : base(mensaje)
        {
            this.IdError = 21;
        }
        public ErrorComunicacionPSPSEI(string mensaje, int idError) : base(mensaje)
        {
            this.IdError = idError;
        }
    }
    public class ErrorConsultaCuenta : ErrorGenerico
    {
        public ErrorConsultaCuenta(string mensaje) : base(mensaje)
        {
            this.IdError = 28;
        }
    }
    public class ErrorCuentaDestino : ErrorGenerico
    {
        public ErrorCuentaDestino(string mensaje) : base(mensaje)
        {
            this.IdError = 119;
        }
    }
    public class ErrorCuentaMalformada : ErrorGenerico
    {
        public string Cuenta { get; set; }
        public ErrorCuentaMalformada(string mensaje) : base(mensaje)
        {
            this.IdError = 5;
        }
        public ErrorCuentaMalformada(int pblu, string mensaje) : base(pblu, mensaje)
        {
        }
        public ErrorCuentaMalformada(int pblu, string mensaje, string cveRastreo) : base(pblu, mensaje, cveRastreo)
        {
        }
        public ErrorCuentaMalformada(int pblu, string mensaje, string cveRastreo, string cuenta) : base(pblu, mensaje, cveRastreo)
        {
            this.Cuenta = cuenta;
        }
    }
    public class ErrorCuentaTDD : ErrorGenerico
    {
        public ErrorCuentaTDD(string mensaje) : base(mensaje)
        {
            this.IdError = 9;
        }
    }
    public class ErrorCuentaYaExiste : ErrorGenerico
    {
        public ErrorCuentaYaExiste(string mensaje) : base(mensaje)
        {
            this.IdError = 22;
        }
    }
    public class ErrorDePrefijo : ErrorGenerico
    {
        public ErrorDePrefijo(string mensaje) : base(mensaje)
        {
            this.IdError = 18;
        }
    }
    public class ErrorDocumento : ErrorGenerico
    {
        public ErrorDocumento(string mensaje) : base(mensaje)
        {
            this.IdError = 33;
        }
    }
    public class ErrorDomicilio : ErrorGenerico
    {
        public ErrorDomicilio(string mensaje) : base(mensaje)
        {
            this.IdError = 0;
        }
    }
    public class ErrorEnBD : ErrorGenerico
    {
        public ErrorEnBD(string mensaje) : base(mensaje)
        {
            this.IdError = 6;
        }
        public ErrorEnBD(int pblu, string mensaje, string cveRastreo) : base(pblu, mensaje, cveRastreo)
        {
            this.IdError = 6;
        }
    }
    public class ErrorEnProcesoDeConciliacion : ErrorGenerico
    {
        public ErrorEnProcesoDeConciliacion(string mensaje) : base(mensaje)
        {
            this.IdError = 19;
        }
        public ErrorEnProcesoDeConciliacion(string mensaje, int idPblu) : base(idPblu, mensaje)
        {
            this.IdError = 19;
        }
    }
    public class ErrorFirma : ErrorGenerico
    {
        public ErrorFirma(string mensaje) : base(mensaje)
        {
            this.IdError = 2;
        }
    }
    public class ErrorFormatoFecha : ErrorGenerico
    {
        public ErrorFormatoFecha(string mensaje) : base(mensaje)
        {
            this.IdError = -1;
        }
    }
    public class ErrorHttpBlu : ErrorGenerico
    {
        public ErrorHttpBlu(string mensaje) : base(mensaje)
        {
        }
    }
    public class ErrorIdEstadoNoExiste : ErrorGenerico
    {
        public ErrorIdEstadoNoExiste(string mensaje) : base(mensaje)
        {
            this.IdError = 27;
        }
    }
    public class ErrorIdPbluInexistente : ErrorGenerico
    {
        public ErrorIdPbluInexistente(string mensaje) : base(mensaje)
        {
        }
    }
    public class ErrorInicioDiaSinCierre : ErrorGenerico
    {
        public ErrorInicioDiaSinCierre(string mensaje) : base(mensaje)
        {
            this.IdError = -1;
        }
    }
    public class ErrorIpNoAlcanzable : ErrorGenerico
    {
        public ErrorIpNoAlcanzable(string mensaje) : base(mensaje)
        {
        }
    }
    public class ErrorIPNula : ErrorGenerico
    {
        public ErrorIPNula(string mensaje) : base(mensaje)
        {
            this.IdError = 15;
        }
        public ErrorIPNula(string mensaje, int idPblu) : base(idPblu, mensaje)
        {
            this.IdError = 15;
        }
    }
    public class ErrorLogin : ErrorGenerico
    {
        public ErrorLogin(string mensaje) : base(mensaje)
        {
            this.IdError = 0;
        }
    }
    public class ErrorMonto : ErrorGenerico
    {
        public ErrorMonto(string mensaje) : base(mensaje)
        {
            this.IdError = 13;
        }
    }
    public class ErrorMontoLimite : ErrorGenerico
    {
        public ErrorMontoLimite(string mensaje) : base(mensaje)

        {
            this.IdError = 201;
        }
    }
    public class ErrorNacionalidad : ErrorGenerico
    {
        public ErrorNacionalidad(string mensaje) : base(mensaje)
        {
            this.IdError = 0;
        }
    }
    public class ErrorNoSeEncontroResultado : ErrorGenerico
    {
        public ErrorNoSeEncontroResultado(string mensaje) : base(mensaje)
        {
            this.IdError = 32;
        }
    }
    public class ErrorPaddingBlu : ErrorGenerico
    {
        public const long serialVersionUID = -1685886855516993371L;
        public ErrorPaddingBlu(string mensaje) : base(mensaje)
        {
            this.IdError = 34;
        }
    }
    public class ErrorPagoExistente : ErrorGenerico
    {
        public ErrorPagoExistente(string mensaje) : base(mensaje)
        {
            this.IdError = 6;
        }
        public ErrorPagoExistente(int pblu, string mensaje, string cveRastreo) : base(pblu, mensaje, cveRastreo)
        {
            this.IdError = 6;
        }
    }
    public class ErrorPagoNoLocalizadoParaDevolver : ErrorGenerico
    {
        public ErrorPagoNoLocalizadoParaDevolver(string mensaje) : base(mensaje)
        {
            this.IdError = 17;
        }
    }
    public class ErrorPagoSinRefNum : ErrorGenerico
    {
        public ErrorPagoSinRefNum(string mensaje) : base(mensaje)
        {
            this.IdError = 326;
        }
    }
    public class ErrorPeticionMalformada : ErrorGenerico
    {
        public ErrorPeticionMalformada(string mensaje) : base(mensaje)
        {
            this.IdError = 3;
        }
    }
    public class ErrorRespuestaInvalida : ErrorGenerico
    {
        public ErrorRespuestaInvalida(string mensaje) : base(mensaje)
        {
            this.IdError = 31;
        }
    }
    public class ErrorSaldoInsuficiente : ErrorGenerico
    {
        public ErrorSaldoInsuficiente(string mensaje) : base(mensaje)
        {
            this.IdError = 14;
        }
    }
    public class ErrorTokenOtpInvalido : ErrorGenerico
    {
        public ErrorTokenOtpInvalido(string mensaje) : base(mensaje)
        {
            this.IdError = 25;
        }
    }
    public class ErrorUdnNoAsociada : ErrorGenerico
    {
        public ErrorUdnNoAsociada(string mensaje) : base(mensaje)
        {
            this.IdError = 24;
        }
    }
    public class ErrorUdnNoExiste : ErrorGenerico
    {
        public ErrorUdnNoExiste(string mensaje) : base(mensaje)
        {
            this.IdError = 23;
        }
    }
    public class ErrorTrace
    {
        public static string GetStackTrace(Exception ex)
        {
            string sResult = "";
            if (ex != null)
            {
                try
                {
                    StringWriter writer = new StringWriter();
                    writer.Write(ex.StackTrace.ToString());
                    sResult = writer.ToString();
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                    sResult = ex.Message;
                }
            }
            return sResult;
        }
        public static string GetStackTrunkedTrace(Exception ex)
        {
            string sResult = "";
            if (ex != null)
            {
                try
                {
                    StringWriter writer = new StringWriter();
                    writer.Write(ex.StackTrace.ToString());
                    sResult = writer.ToString();
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                    sResult = ex.Message;
                }
            }
            return sResult;
        }
    }

    public class ErrorCuentaInexistente : ErrorGenerico
    {
        public ErrorCuentaInexistente(string mensaje) : base(mensaje)
        {
        }

    }
}
