namespace Asp.Api.Azul.Repositorys.SaldoPbluRepository
{
    public interface ISaldoPbluRepository
    {

        Task<bool> AumentaSaldo(int idPblu, decimal monto);
        Task<bool> DisminuyeSaldo(int idPblu, decimal monto);


    }
}
