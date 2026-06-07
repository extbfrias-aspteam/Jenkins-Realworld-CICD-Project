namespace Asp.Api.Azul.Core.Commons.Interfaces.Infrastructure.Services
{
    public interface ICacheService
    {
        T? Get<T>(string key);
        void Set<T>(string key, T value, TimeSpan? absoluteExpiration = null);
        void Remove(string key);
        bool Exists(string key);
    }
}