namespace Asp.Api.Azul.Core.Commons.Models.Dto
{
    public class ApiResponse<T>
    {
        public T? Data { get; set; }
        public bool Success { get; set; }
        public required string Message { get; set; }
        public int Code { get; set; }
    }
}