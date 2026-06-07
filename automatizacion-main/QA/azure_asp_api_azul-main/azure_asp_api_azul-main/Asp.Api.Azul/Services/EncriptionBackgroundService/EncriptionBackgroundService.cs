using Asp.Cifrado.Services;

namespace Asp.Api.Azul.Services.EncriptionBackgroundService
{
    public class EncriptionBackgroundService: BackgroundService
    {
        private readonly IEncriptionService _encriptionService;

        public EncriptionBackgroundService(IEncriptionService encriptionService)
        {
            _encriptionService = encriptionService;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            await _encriptionService.Init();
            
        }
    }
}
