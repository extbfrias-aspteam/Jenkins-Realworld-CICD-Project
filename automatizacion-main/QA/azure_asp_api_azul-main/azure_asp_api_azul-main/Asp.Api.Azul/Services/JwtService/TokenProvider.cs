namespace Asp.Api.Azul.Services.JwtService
{
    public interface ITokenProvider
    {
        string GetToken();
    }
    public class TokenProvider : ITokenProvider
    {
        private readonly IServiceScopeFactory _scopeFactory;
        private string _jwt;

        public TokenProvider(IServiceScopeFactory scopeFactory)
        {
            _scopeFactory = scopeFactory;
            // _jwt = GenerateToken();
        }

        //private string GenerateToken()
        //{
        //    using var scope = _scopeFactory.CreateScope();
        //    var tokenHelper = scope.ServiceProvider.GetRequiredService<TokenHelper>();
        //    return tokenHelper.GenerarToken();
        //}


        public string GetToken() => _jwt;
    }
}
