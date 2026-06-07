using Asp.Api.Azul.Infrastructure.Resilience;
using Asp.Cifrado.Entities.DataBase;
using Npgsql;

namespace Asp.Cifrado.Repositorys.CertificadoRepository
{
    public class CertificadoRepository : ICertificadoRepository
	{
		private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public CertificadoRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
		{
			_configuration = configuration;
			_resilientExecutor = resilientExecutor;
		}
		public async Task<List<Certificado>> GetAll()
		{
			return await _resilientExecutor.ExecuteAsync(async () =>
			{
                List<Certificado> response = new List<Certificado>();
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_certificado, id_pblu, numero_serie, ruta, activo, fecha_creacion, usuario_creacion, tipo,ruta_portal FROM certificado;";
                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        var reader = await command.ExecuteReaderAsync();
                        while (await reader.ReadAsync())
                        {
                            response.Add(new Certificado
                            {
                                IdCertificado = reader.GetInt32(0),
                                IdPblu = reader.GetInt32(1),
                                NumeroSerie = reader.GetString(2),
                                Ruta = reader.GetString(3),
                                Activo = reader.GetBoolean(4),
                                FechaCreacion = reader.GetValue(5) as DateTime?,
                                UsuarioCreacion = reader.GetValue(6)?.ToString(),
                                Tipo = reader.GetValue(7)?.ToString(),
                                RutaPortal = reader.GetValue(8)?.ToString() ?? null
                            });
                        }
                    }

                    return response;
                }
            });
		}
	}
}