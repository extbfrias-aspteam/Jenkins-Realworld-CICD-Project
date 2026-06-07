using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;

namespace Asp.Api.Azul.Business.Authentication
{
    public interface ILoginBusiness
    {
        Task<DtoLogin?> Authenticate(LoginRequest request);
    }
}