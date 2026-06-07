namespace Asp.Api.Azul.Kafka.Dtos.SpeiRetornos
{
    public record SpeiRetornoEiyuToAspDto
    {
        public string IdRetiro { get; set; }

        public string Monto { get; set; }

        public string Concepto { get; set; }

        public string CuentaDestino { get; set; }

        public string CuentaOrigen { get; set; }

        public string Producto { get; set; }

        public string ClaveMovimiento { get; set; }

        public string RefNum { get; set; }

        public string CveRastreo { get; set; }

        public string NombreOrdenante { get; set; }
        public string DevolucionId { get; set; }
        public string NombreBeneficiario { get; set; }
        public string FechaOperacion { get; set; }
        public string Folio { get; set; }
        public string FolioPaquete { get; set; }
        public int IdPblu { get; set; }

    }
}
