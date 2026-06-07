namespace Asp.Api.Azul.Utilities.GestionTokens
{
    public interface IUserResolver
    {
        string GetToken();
        BlueUser GetUser();
    }
}