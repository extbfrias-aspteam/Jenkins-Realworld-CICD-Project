namespace Asp.Api.Azul.Models.Entities
{
    public class DtoLogin
    {
        public int Tdp { get; set; }
        public int Ctr { get; set; }
        public int Idp { get; set; }
        public string Pdt
        {
            get
            {
                return Tdp == 2 ? "ROLE_PSPEI" : Tdp == 1 ? "ROLE_PBLU" : "ROLE_BLU";
            }
        }
    }
}
