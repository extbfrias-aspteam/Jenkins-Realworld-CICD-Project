using Asp.Api.Azul.Helpers;

namespace Asp.Api.Azul.Entities.DataBase
{
    public class ViewPbluUdn
    {
        [DbColumn("id_pblu")]
        public int idPblu { get; set; }

        [DbColumn("id_udn")]
        public int idUdn { get; set; }

        [DbColumn("udn_descripcion")]
        public string? udnDescripcion { get; set; }

        [DbColumn("udn_saldo_min")]
        public decimal udnSaldoMin { get; set; }

        [DbColumn("udn_monto_limite")]
        public decimal? udnMontoLimite { get; set; }

        [DbColumn("udn_notificacion_activa")]
        public bool udnNotificacionActiva { get; set; }

        [DbColumn("udn_fecha_creacion")]
        public DateTime? udnFechaCreacion { get; set; }

        [DbColumn("activo")]
        public bool activo { get; set; }

        [DbColumn("clabe")]
        public string? clabe { get; set; }
    }
}
