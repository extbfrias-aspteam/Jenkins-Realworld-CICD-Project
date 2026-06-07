using System.IdentityModel.Tokens.Jwt;

namespace Asp.Api.Azul.Utilities.GestionTokens
{
    public class UserResolver : IUserResolver
    {
        private readonly string _token;
        private BlueUser _blueUser;

        public UserResolver(IHttpContextAccessor context)
        {
            _blueUser = new BlueUser();
            _token = string.Empty;

            if (context.HttpContext != null && context.HttpContext.User != null && context.HttpContext.User.Claims.Any())
            {
                _token = context.HttpContext.Request.Headers["Authorization"].ToString().Replace("Bearer ", "");

                if (!string.IsNullOrEmpty(_token))
                {
                    var jsonToken = new JwtSecurityTokenHandler().ReadJwtToken(_token) as JwtSecurityToken;

                    var tdp = jsonToken.Claims?.FirstOrDefault(claim => claim.Type == "tdp")?.Value;
                    var crt = jsonToken.Claims?.FirstOrDefault(claim => claim.Type == "crt")?.Value;
                    var idp = jsonToken.Claims?.FirstOrDefault(claim => claim.Type == "idp")?.Value;
                    var pdt = jsonToken.Claims?.FirstOrDefault(claim => claim.Type == "pdt")?.Value;
                    var username = jsonToken.Claims?.FirstOrDefault(claim => claim.Type == "username")?.Value;   

                    _blueUser.IdTipoParticipante = Convert.ToInt32(tdp);
                    _blueUser.IdPblue = Convert.ToInt32(idp);
                    _blueUser.IdCertificado = Convert.ToInt32(crt);
                    _blueUser.Role = pdt;
                    _blueUser.Username = username;
                }
            }
        }

        public BlueUser GetUser()
        {
            return _blueUser;
        }

        public string GetToken()
        {
            return _token;
        }
    }
}
