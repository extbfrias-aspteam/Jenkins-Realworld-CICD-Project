using Asp.Api.Azul.Models.Entities;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Text;

namespace Asp.Api.Azul.Services.JwtService
{
    public class TokenSettings
    {
        private readonly JwtSettings _jwtSettings;
        private readonly IConfiguration _configuration;

        public TokenSettings(IOptions<JwtSettings> jwtSettings, IConfiguration configuration)
        {
            _jwtSettings = jwtSettings.Value;
            _configuration = configuration;
        }

        public string GenerarToken()
        {

            var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtSettings.SecretKey));
            var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: "EIYU Services",
                audience: "Eiyu Crypto Validator",
                signingCredentials: creds
            );

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

    }
}
