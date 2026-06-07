using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Infrastructure.Resilience;
using Npgsql;

namespace Asp.Api.Azul.Repositorys.PersonaRepository
{
    public class PersonaRepository : BaseRepository, IPersonaRepository
    {
        private readonly IConfiguration _configuration;
        private readonly ResilientExecutor _resilientExecutor;

        public PersonaRepository(IConfiguration configuration, ResilientExecutor resilientExecutor)
        {
            _configuration = configuration;
            _resilientExecutor = resilientExecutor;
        }

        public async Task<int> Insert(Persona persona)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = GetQueryInsert(persona);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(persona));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        public async Task<int> Update(Persona persona)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var condition = $"id_persona = {persona.idPersona}";

                    var query = GetQueryUpdate(persona, condition);

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddRange(GetParametersInsert(persona));
                        var res = await command.ExecuteScalarAsync() as int?;
                        return res ?? 0;
                    }
                }
            });
        }

        public async Task UpdateCurp(string rfc, string curp)
        {
            await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "UPDATE persona set curp = @curp WHERE rfc = @rfc;";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("curp", curp);
                        command.Parameters.AddWithValue("rfc", rfc);

                        await command.ExecuteNonQueryAsync();
                    }
                }
            });
        }

        public async Task<int> ExistePersona(string rfc, string curp)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = "SELECT id_persona FROM persona WHERE rfc = @rfc and curp = @curp order by id_persona DESC limit 1";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("rfc", rfc);
                        command.Parameters.AddWithValue("curp", curp);

                        var reader = await command.ExecuteReaderAsync();

                        while (await reader.ReadAsync())
                        {
                            var idPersona = reader.GetValue(0) as int?;
                            return idPersona ?? 0;
                        }

                    }
                    return 0;
                }
            });
        }

        public async Task<Persona?> GetByRfc(string rfc)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_persona, tipo_persona, nombre, apellido_paterno, apellido_materno, razon_social, rfc, curp, lugar_nacimiento, id_nacionalidad, id_estado_civil, telefono, celular, correo, id_ocupacion, fecha_nacimiento, sexo, id_grado_estudios, calle, numero_exterior, numero_interior, id_colonia, id_codigo_postal, referencia_direccion, ingreso_mensual, monto_maximo_ahorro, id_puesto, id_giro, id_destino_fondo, id_localidad, fecha_creacion, asp_codigo_postal, expediente_enviado, colonia, serie_fiel, geolocalizacion, num_ext, num_int, calle2, calle3, id_pais, id_entidad, id_sociedad, cant_op_mensual, ciudad, num_ident, tipo_ident_id FROM persona WHERE rfc = @rfc limit 1";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("rfc", rfc);

                        var reader = await command.ExecuteReaderAsync();

                        var persona = await llenaPersonaData(reader);

                        return persona;
                    }
                }
            });
        }

        public async Task<Persona?> GetByCurp(string curp)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();
                    var query = "SELECT id_persona, tipo_persona, nombre, apellido_paterno, apellido_materno, razon_social, rfc, curp, lugar_nacimiento, id_nacionalidad, id_estado_civil, telefono, celular, correo, id_ocupacion, fecha_nacimiento, sexo, id_grado_estudios, calle, numero_exterior, numero_interior, id_colonia, id_codigo_postal, referencia_direccion, ingreso_mensual, monto_maximo_ahorro, id_puesto, id_giro, id_destino_fondo, id_localidad, fecha_creacion, asp_codigo_postal, expediente_enviado, colonia, serie_fiel, geolocalizacion, num_ext, num_int, calle2, calle3, id_pais, id_entidad, id_sociedad, cant_op_mensual, ciudad, num_ident, tipo_ident_id FROM persona WHERE curp = @curp limit 1";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("curp", curp);

                        var reader = await command.ExecuteReaderAsync();

                        var persona = await llenaPersonaData(reader);

                        return persona;
                    }
                }
            });
        }

        public async Task<Persona?> GetById(int id)
        {
            return await _resilientExecutor.ExecuteAsync(async () =>
            {
                using (var connection = new NpgsqlConnection(_configuration.GetConnectionString("BluDbConnection")))
                {
                    await connection.OpenAsync();

                    var query = "SELECT id_persona, tipo_persona, nombre, apellido_paterno, apellido_materno, razon_social, rfc, curp, lugar_nacimiento, id_nacionalidad, id_estado_civil, telefono, celular, correo, id_ocupacion, fecha_nacimiento, sexo, id_grado_estudios, calle, numero_exterior, numero_interior, id_colonia, id_codigo_postal, referencia_direccion, ingreso_mensual, monto_maximo_ahorro, id_puesto, id_giro, id_destino_fondo, id_localidad, fecha_creacion, asp_codigo_postal, expediente_enviado, colonia, serie_fiel, geolocalizacion, num_ext, num_int, calle2, calle3, id_pais, id_entidad, id_sociedad, cant_op_mensual, ciudad, num_ident, tipo_ident_id FROM persona WHERE id_persona = @id_persona limit 1";

                    using (var command = new NpgsqlCommand(query, connection))
                    {
                        command.Parameters.AddWithValue("id_persona", id);

                        var reader = await command.ExecuteReaderAsync();

                        var persona = await llenaPersonaData(reader);

                        return persona;
                    }
                }
            });
        }

        private async Task<Persona?> llenaPersonaData(NpgsqlDataReader reader)
        {
            while (await reader.ReadAsync())
            {
                var persona = new Persona
                {
                    idPersona = reader.GetInt32(0),
                    tipoPersona = reader.GetValue(1)?.ToString(),
                    nombre = reader.GetValue(2)?.ToString(),
                    apellidoPaterno = reader.GetValue(3)?.ToString(),
                    apellidoMaterno = reader.GetValue(4)?.ToString(),
                    razonSocial = reader.GetValue(5)?.ToString(),
                    rfc = reader.GetValue(6)?.ToString(),
                    curp = reader.GetValue(7)?.ToString(),
                    lugarNacimiento = reader.GetValue(8)?.ToString(),
                    idNacionalidad = reader.GetValue(9) as int?,
                    idEstadoCivil = reader.GetValue(10) as int?,
                    telefono = reader.GetValue(11)?.ToString(),
                    celular = reader.GetValue(12)?.ToString(),
                    correo = reader.GetValue(13)?.ToString(),
                    idOcupacion = reader.GetValue(14) as int?,
                    fechaNacimiento = reader.GetValue(15)?.ToString(),
                    sexo = reader.GetValue(16)?.ToString(),
                    idGradoEstudios = reader.GetValue(17) as int?,
                    calle = reader.GetValue(18)?.ToString(),
                    numeroExterior = reader.GetValue(19) as int?,
                    numeroInterior = reader.GetValue(20) as int?,
                    idColonia = reader.GetValue(21) as int?,
                    idCodigoPostal = reader.GetValue(22)?.ToString(),
                    referenciaDireccion = reader.GetValue(23)?.ToString(),
                    ingresoMensual = reader.GetValue(24) as decimal?,
                    montoMaximoAhorro = reader.GetValue(25) as decimal?,
                    idPuesto = reader.GetValue(26) as int?,
                    idGiro = reader.GetValue(27) as int?,
                    idDestinoFondo = reader.GetValue(28) as int?,
                    id_localidad = reader.GetValue(29) as int?,
                    fechaCreacion = reader.GetValue(30) as DateTime?,
                    aspCodigoPostal = reader.GetValue(31)?.ToString(),
                    expedienteEnviado = reader.GetValue(32) as bool?,
                    colonia = reader.GetValue(33)?.ToString(),
                    serieFiel = reader.GetValue(34)?.ToString(),
                    geolocalizacion = reader.GetValue(35)?.ToString(),
                    numExt = reader.GetValue(36)?.ToString(),
                    numIdent = reader.GetValue(37)?.ToString(),
                    calle2 = reader.GetValue(38)?.ToString(),
                    calle3 = reader.GetValue(39)?.ToString(),
                    idPais = reader.GetValue(40) as int?,
                    idEntidad = reader.GetValue(41) as int?,
                    idSociedad = reader.GetValue(42) as int?,
                    cantOpMensual = reader.GetValue(43) as decimal?,
                    ciudad = reader.GetValue(44)?.ToString(),
                    numInt = reader.GetValue(45)?.ToString(),
                    tipoIdentId = reader.GetValue(46) as int?
                };

                return persona;
            }
            return null;
        }      
    }
}