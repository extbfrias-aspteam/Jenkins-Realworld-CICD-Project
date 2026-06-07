namespace Asp.Api.Azul
{
    public class DtoSaldoUdn
    {
        public int IdUdn { get; set; }
        public DateTime FechaOperacion { get; set; }
        public long TotalAbonosMonto { get; set; }
        public decimal MontoAbono { get; set; }
        public long TotalCargosMonto { get; set; }
        public decimal MontoCargo { get; set; }
        public decimal Saldo { get; set; }
        public decimal SaldoInicial { get; set; }
    }
}