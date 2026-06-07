using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.ViewPbluUdnRepository
{
    public class ViewPbluUdnRepository : IViewPbluUdnRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public ViewPbluUdnRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<ViewPbluUdn?> GetUdnById(int id)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = "SELECT id_pblu, id_udn, udn_descripcion, udn_saldo_min, udn_notificacion_activa, udn_fecha_creacion, activo, clabe, udn_monto_limite FROM view_pblu_udn where id_udn = @id_udn limit 1";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_udn", id);

                        var reader = await command.ExecuteReaderAsync();

                        while (await reader.ReadAsync())
                        {
                            var view = new ViewPbluUdn
                            {
                                idPblu = reader.GetInt32(0),
                                idUdn = reader.GetInt32(1),
                                udnDescripcion = reader.GetValue(2)?.ToString(),
                                udnSaldoMin = reader.GetDecimal(3),
                                udnNotificacionActiva = reader.GetBoolean(4),
                                udnFechaCreacion = reader.GetValue(5) as DateTime?,
                                activo = reader.GetBoolean(6),
                                clabe = reader.GetValue(7)?.ToString(),
                                udnMontoLimite = reader.GetValue(8) as decimal?
                            };
                            return view;
                        }
                    }
                }
                return null;
            });
        }
    }
}