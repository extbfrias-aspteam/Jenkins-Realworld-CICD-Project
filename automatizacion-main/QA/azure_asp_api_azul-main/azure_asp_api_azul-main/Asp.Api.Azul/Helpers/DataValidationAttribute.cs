using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;
using Asp.Api.Azul.Repositorys.CuentaRepository;

namespace Asp.Api.Azul.Helpers
{
    [System.AttributeUsage(System.AttributeTargets.Property)]
    public class DataValidationAttribute : System.Attribute
    {
        private bool _campoObligatorio;
        private bool _curpValidation;
        private bool _rfcValidation;
        private bool _geolocalizacionValidation;
        private static readonly Regex RfcFisicaRegex = new Regex(@"^[A-ZÑ&]{4}\d{6}[A-Z\d]{3}$", RegexOptions.Compiled | RegexOptions.IgnoreCase);
        private static readonly Regex RfcMoralRegex = new Regex(@"^[A-ZÑ&]{3}\d{6}[A-Z\d]{3}$", RegexOptions.Compiled | RegexOptions.IgnoreCase);
        private static readonly Regex GeolocalizacionRegex = new Regex(@"^(-?(?:[1-8]?\d(?:\.\d+)?|90(?:\.0+)?)),\s*(-?(?:1[0-7]\d|[1-9]?\d)(?:\.\d+)?|180(?:\.0+)?)$", RegexOptions.Compiled);

        public DataValidationAttribute(bool curpValidation = false, bool rfcValidation = false, bool geolocalizacionValidation = false)
        {
            this._curpValidation = curpValidation;
            this._rfcValidation = rfcValidation;
            this._geolocalizacionValidation = geolocalizacionValidation;
        }

        public string validate(object? value, string tipoPersona,bool campoObligatorio)
        {
            _campoObligatorio = campoObligatorio; 

            if (_curpValidation && tipoPersona.Equals("PF")){
                return validateCurp(value?.ToString());
            }
            if (_rfcValidation) {
                return validateRFC(value?.ToString(), tipoPersona);
            }
            if (_geolocalizacionValidation){
                return validateGeolocalizacion(value?.ToString()); 
            }
            return string.Empty;
        }

        private string validateCurp(string? curp)
        {
            Regex CurpRegex = new Regex(@"^[A-Z]{1}[AEIOUX]{1}[A-Z]{2}\d{6}[HM]{1}(AS|BC|BS|CC|CL|CM|CS|CH|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[A-Z\d]{1}\d{1}$", RegexOptions.Compiled | RegexOptions.IgnoreCase);

            if (string.IsNullOrWhiteSpace(curp))
            {
                return "El campo curpCuenta es obligatorio para este tipo de cuenta.";
            }

            curp = curp.Trim();

            if (curp.Length != 18)
            {
                return "El campo curpCuenta no tiene la longitud correcta.";
            }

            if (!CurpRegex.IsMatch(curp))
            {
                return "El campo curpCuenta no tiene el formato correcto.";
            }
            return string.Empty;
        }

        private string validateRFC(string? rfc, string tipoPersona)
        {
            if (string.IsNullOrWhiteSpace(rfc))
            {
                return "El campo rfcCuenta es obligatorio para este tipo de cuenta.";
            }
            rfc = rfc.Trim();
            int expectedLength = tipoPersona.Equals("PF") ? 13 : 12;
            Regex expectedRegex = tipoPersona.Equals("PF") ? RfcFisicaRegex : RfcMoralRegex;

            if (rfc.Length != expectedLength)
            {
                return "El campo rfcCuenta no tiene la longitud correcta.";
            }
            if (!expectedRegex.IsMatch(rfc))
            {
                return "El campo rfcCuenta no tiene el formato correcto.";
            }
            return string.Empty;
        }
        
        public string validateGeolocalizacion(string? geolocalizacion)
        {
           

            //Validamos Campo Obligatorio Bandera Encendida
            if (_campoObligatorio)
            {
                if (string.IsNullOrWhiteSpace(geolocalizacion))
                {
                    return "El campo de geolocalización es obligatorio.";
                }
                geolocalizacion = geolocalizacion.Trim();

                if (!GeolocalizacionRegex.IsMatch(geolocalizacion))
                {
                    return "El campo de geolocalización no tiene el formato correcto (latitud, longitud).";
                }
            }
            return string.Empty;
        }
    }
}
