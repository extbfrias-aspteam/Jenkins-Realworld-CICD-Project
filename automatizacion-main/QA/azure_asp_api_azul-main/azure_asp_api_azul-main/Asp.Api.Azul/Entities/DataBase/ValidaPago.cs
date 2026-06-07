namespace Asp.Api.Azul.Entities.DataBase
{
    public class ValidaPago
    {
        public bool Success { get; set; }
        public string Mensaje { get; set; }
        
        public string Proveedor { get; set; }

        public string NombreWithRFC { get; set; }
        public int IdUdn { get; set; }
        public int IdMensaje { get; set; }
        /*        public string NombreOrd { get; set; }
                public string ApellidoPaternoOrd { get; set; }
                public string ApellidoMaternoOrd { get; set; }
                public string RfcOrd { get; set; }*/
    }
}
