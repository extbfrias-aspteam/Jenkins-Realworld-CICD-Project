using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Models.Request;
using Asp.Api.Azul.Repositorys.AccessRepository;
using Asp.Api.Azul.Repositorys.ViewPbluCrtRepository;
using BCrypt.Net;

namespace Asp.Api.Azul.Business.Authentication
{
    public class LoginBusiness : ILoginBusiness
    {
        private IAccessRepository _accessRepository;
        private IViewPbluCrtRepository _viewPblueCrtRepository;

        public LoginBusiness(IAccessRepository accessRepository, IViewPbluCrtRepository viewPblueCrtRepository)
        {
            _accessRepository = accessRepository;
            _viewPblueCrtRepository = viewPblueCrtRepository;
        }

        public async Task<DtoLogin?> Authenticate(LoginRequest request)
        {
            try
            {
                var user = await _accessRepository.FindByUser(request.Username ?? string.Empty);
                if (user != null)
                {
                    user.IntentosAcceso = user.IntentosAcceso == null ? 0 : user.IntentosAcceso;
                    if (user.IntentosAcceso < 3)
                    {
                        if (BCrypt.Net.BCrypt.Verify(request.Password, user.Password))
                        {
                            var viewPblueCrt = await _viewPblueCrtRepository.GetCertificadoActivo(user.IdPblue);
                            if (viewPblueCrt != null)
                            {
                                return new DtoLogin
                                {
                                    Ctr = viewPblueCrt.IdCertificado,
                                    Idp = viewPblueCrt.IdPblu,
                                    Tdp = viewPblueCrt.IdTipoPart
                                };
                            }
                        }
                    }
                    // TODO -> Aqui se debe enviar un correo de que el usuario llego al limite de intentos para acceder

                }
                return null;                
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }
}
