namespace Asp.Api.Azul.Helpers
{
    public class DateTimeFix
    {
        public static DateTime Now()
        {
            return TimeZoneInfo.ConvertTimeFromUtc(DateTime.UtcNow,
                TimeZoneInfo.FindSystemTimeZoneById("Central Standard Time (Mexico)"));
        }
    }
}
