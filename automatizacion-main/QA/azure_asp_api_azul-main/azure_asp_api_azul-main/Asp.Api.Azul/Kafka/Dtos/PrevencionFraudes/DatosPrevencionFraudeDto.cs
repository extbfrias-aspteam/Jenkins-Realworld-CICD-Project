using Asp.Api.Azul.Entities.Business;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;

namespace Asp.Api.Azul.Kafka.Dtos.PrevencionFraudes
{
    public class DatosPrevencionFraudeDto
    {
        public required int IdPblu {  get; set; }
        public required DatosCuentaPrevFraudeDto DatosCuenta { get; set; }
        public required DtoDatosOriginalesPago DatosOriginales {  get; set; }
    }
}
