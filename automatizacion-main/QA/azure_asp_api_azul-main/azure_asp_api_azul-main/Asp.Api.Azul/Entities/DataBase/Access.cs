namespace Asp.Api.Azul.Entities.DataBase
{
    public class Access
    {
        public int IdPblue { get; set; }
        public int? IdPerfil { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public int? IntentosAcceso { get; set; }
        public DateTime? FechaCreacion { get; set; }
        public string? UsuarioCreacion { get; set; }
    }
}
