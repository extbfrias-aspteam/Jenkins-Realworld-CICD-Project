namespace Asp.Cifrado.Entities.DataBase
{
    public class Certificado
    {
        public int IdCertificado { get; set; }
        public int IdPblu { get; set; }
        public string NumeroSerie { get; set; }
        public string Ruta { get; set; }
        public bool Activo { get; set; }
        public DateTime? FechaCreacion { get; set; }
        public string? UsuarioCreacion { get; set; }
        public string? Tipo { get; set; }
        public string? RutaPortal { get; set; }
    }
}
