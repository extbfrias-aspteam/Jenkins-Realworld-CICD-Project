namespace Asp.Api.Azul.Core.Commons.Constants
{
    public static class GeneralConstants
    {
        public struct CacheKeys
        {
            public const string PBLU_LIST = "cache_key_pblu_list";
        }

        public struct CacheTimes
        {
            public static readonly TimeSpan ONE_HOUR = TimeSpan.FromHours(1);
        }

        public struct MicroServicesCode
        {
            public const string CONTINGENCY = "CONTINGENCY";
        }

        public struct KafkaTopics
        {
            public const string CONTINGENCY_ACTIVE = "asp_contingency_active";
        }
        public struct MonitorPlus
        {
            public const string USE_MONITORPLUS = "use_monitorplus";
            public const string REFRESH_TOKEN_EXPIRATION_HOURS = "REFRESH_TOKEN_EXPIRATION_HOURS";
        }
    }
}