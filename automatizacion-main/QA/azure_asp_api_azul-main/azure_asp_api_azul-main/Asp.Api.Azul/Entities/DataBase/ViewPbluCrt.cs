namespace Asp.Api.Azul.Entities.DataBase
{
    public class ViewPbluCrt
    {
        public int IdPblu { get; set; }
        public int IdTipoPart { get; set; }
        public string? Descripcion { get; set; }
        public int IdCertificado { get; set; }
        public bool Activo { get; set; }
        public string? Username { get; set; }
    }
}
