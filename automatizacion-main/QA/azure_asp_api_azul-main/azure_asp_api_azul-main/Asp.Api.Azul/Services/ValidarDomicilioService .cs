using Asp.Api.Azul.Models.Entities;
using System.ComponentModel.DataAnnotations;

namespace Asp.Api.Azul.Services
{
    public class ValidarDomicilioService : IValidarDomicilioService
    {
        public void ValidarDomicilio(DtoCtaExpedienteDomicilio domicilio, char tipoPersona)
        {
            var validationResults = new List<ValidationResult>();
            var validationContext = new ValidationContext(domicilio);
            if (!Validator.TryValidateObject(domicilio, validationContext, validationResults, true))
            {
                // Si no es válido, devuelve los errores
                //return BadRequest(validationResults.Select(vr => vr.ErrorMessage));
                string errores = string.Join(' ', validationResults.Select(vr => vr.ErrorMessage));
                if (tipoPersona == 'P')
                {
                    throw new ErrorDomicilio("Error al procesar los datos del domicilio: " + errores);
                }
                else
                {
                    throw new ErrorDomicilio("Error al procesar los datos del domicilio del Representante Legal: " + errores);
                }

            }
        }

    }
}
