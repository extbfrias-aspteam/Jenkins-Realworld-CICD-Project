using Asp.Api.Azul.Helpers;
using System.Text.Json.Serialization;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class AspOcupacion
    {
        [DbColumn("desc_ocupacion")]
        public string descOcupacion { get; set; }
        [DbColumn("ocu_id")]
        public int ocuId { get; set; }
    }
}
