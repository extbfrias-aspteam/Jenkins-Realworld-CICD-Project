using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace Asp.Api.Azul.Services.JwtService
{
    public interface IJwtIssuerService
    {
        string GenerateTokenTraspasosCore();
    }

    public class JwtIssuerService : IJwtIssuerService
    {
        private readonly IConfiguration _configuration;

        public JwtIssuerService(IConfiguration configuration)
        {
            _configuration = configuration;
        }


        public string GenerateTokenTraspasosCore()
        {
            var secretKey = _configuration["ApiTraspasosCore:SecretKey"] ?? throw new InvalidOperationException("JWT secret key not found.");
            var issuer = _configuration["ApiTraspasosCore:Issuer"] ?? "Asp.Api.Azul";
            var audience = _configuration["ApiTraspasosCore:Audience"] ?? "Traspasos.Core";

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secretKey));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);


            var claims = new[]
            {
                new Claim("scope", "envio_traspasos"),
                new Claim("origin", "Asp.Api.Azul"),
                new Claim("purpose", "Traspasos.Core"),
                new Claim("jti", Guid.NewGuid().ToString())
            };


            var token = new JwtSecurityToken(
                issuer: issuer,
                audience: audience,
                claims: claims,
                notBefore: DateTime.UtcNow,
                expires: DateTime.UtcNow.AddMinutes(1),
                signingCredentials: creds
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
