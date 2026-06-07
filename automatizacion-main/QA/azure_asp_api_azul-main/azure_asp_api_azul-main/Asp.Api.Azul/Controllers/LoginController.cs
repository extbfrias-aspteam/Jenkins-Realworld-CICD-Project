using Asp.Api.Azul.Business.Authentication;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Net.Http;
using System.Security.Claims;
using System.Text;

namespace Asp.Api.Azul.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LoginController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private ILoginBusiness _loginBusiness;
        private readonly IAspLogservice _aspLogservice;

        public LoginController(IConfiguration configuration, ILoginBusiness loginBusiness, IAspLogservice aspLogservice)
        {
            _configuration = configuration;
            _loginBusiness = loginBusiness;
            _aspLogservice = aspLogservice;
        }

        [AllowAnonymous]
        [HttpPost]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            try
            {

                var loginResponse = await _loginBusiness.Authenticate(request);

                if (loginResponse == null) return BadRequest();

                var token = GenerateJsonWebToken(request.Username, loginResponse);

                Response.Headers.Add("authorization",token);
                //_aspLogservice.RegistraLoginLog("/api/v1/cuentas/eyu_login", "POST", request, "Ok", new {}, new { }, new {},0);

                return Ok();
            }
            catch (Exception e)
            {
               await _aspLogservice.RegistraError("/api/v1/cuentas/eyu_login", "POST", request, new {},e, 0);
                return BadRequest();
            }
        }

        private string GenerateJsonWebToken(string username, DtoLogin login)
        {
            var key = _configuration["Jwt:Key"];
            var issuer = _configuration["Jwt:Issuer"];

            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new List<Claim>
            {
                new Claim("tdp",login.Tdp.ToString()),
                new Claim("crt",login.Ctr.ToString()),
                new Claim("idp",login.Idp.ToString()),
                new Claim("pdt",login.Pdt),
                new Claim("username", username)
            };

            var token = new JwtSecurityToken(issuer: issuer
                , audience: issuer
                , claims: claims
                , expires: DateTime.Now.AddDays(1)
                , signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
