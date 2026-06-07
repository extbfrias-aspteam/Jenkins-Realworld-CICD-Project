using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class UdnPrefijoPbluCta
    {
        [DbColumn("id_udn")]
        public int idUdn { get; set; }
        [DbColumn("id_pblu")]
        public int idPblu { get; set; }
        [DbColumn("prefijo_udn")]
        public string prefijoUdn { get; set; }
        [DbColumn("prefijo_pblu")]
        public string prefijoPblu { get; set; }
        [DbColumn("cuenta_clabe_padre")]
        public string cuentaClabePadre { get; set; }
    }
}
