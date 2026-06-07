using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Core.Commons.Enums;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Helpers;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Repositorys.ContribuyenteRepository;
using Asp.Api.Azul.Repositorys.CuentaRepository;
using Asp.Api.Azul.Repositorys.ExpedienteRepository;
using Asp.Api.Azul.Repositorys.GiroRepository;
using Asp.Api.Azul.Repositorys.NacionalidadRepository;
using Asp.Api.Azul.Repositorys.OcupacionRepository;
using Asp.Api.Azul.Repositorys.PaisRepository;
using Asp.Api.Azul.Repositorys.PbluRepository;
using Asp.Api.Azul.Repositorys.PersonaRepository;
using Asp.Api.Azul.Repositorys.UdnRepository;
using Asp.Api.Azul.Repositorys.ViewPbluUdnRepository;
using Asp.Api.Azul.Services;
using Asp.Cifrado.Services;
using System.Data;
using System.Globalization;
using System.Text.Json;

namespace Asp.Api.Azul.Business.Cuenta
{
    public class CuentaBusiness : ICuentaBusiness
    {
        #region Dependency

        private readonly IPersonaRepository _personaRepository;
        private readonly IContribuyenteRepository _contribuyenteRepository;
        private readonly IExpedienteRepository _expedienteRepository;
        private readonly IUdnRepository _udnRepository;
        private readonly IPbluRepository _pbluRepository;
        private readonly ICuentaRepository _cuentaRepository;
        private readonly INacionalidadRepository _nacionalidadRepository;
        private readonly IPaisRepository _paisRepository;
        private readonly IGiroRepository _giroRepository;
        private readonly IOcupacionRepository _ocupacionRepository;
        private readonly IAspApiClient _aspApiClient;
        private readonly IViewPbluUdnRepository _viewPbluUdnRepository;
        private readonly ILogsBusiness _logsBusiness;

        public CuentaBusiness(IPersonaRepository personaRepository
            , IContribuyenteRepository contribuyenteRepository
            , IExpedienteRepository expedienteRepository
            , IUdnRepository udnRepository
            , IPbluRepository pbluRepository
            , ICuentaRepository cuentaRepository
            , INacionalidadRepository nacionalidadRepository
            , IPaisRepository paisRepository
            , IGiroRepository giroRepository
            , IOcupacionRepository ocupacionRepository
            , IAspApiClient aspApiClient
            , IViewPbluUdnRepository viewPbluUdnRepository
            , ILogsBusiness logsBusiness)
        {
            _personaRepository = personaRepository;
            _contribuyenteRepository = contribuyenteRepository;
            _expedienteRepository = expedienteRepository;
            _udnRepository = udnRepository;
            _pbluRepository = pbluRepository;
            _cuentaRepository = cuentaRepository;
            _nacionalidadRepository = nacionalidadRepository;
            _paisRepository = paisRepository;
            _giroRepository = giroRepository;
            _ocupacionRepository = ocupacionRepository;
            _aspApiClient = aspApiClient;
            _viewPbluUdnRepository = viewPbluUdnRepository;
            _logsBusiness = logsBusiness;
        }

        #endregion

        #region Crear persona moral


        public async Task<DtoClabe> CrearCuentaPersonaMoral(DtoCtaExpedientePMoral cuentaPm, int idPblu, string nombreUsuario, string timestamp)
        {
            await ValidarBloqueoAltaReferencia(idPblu, false, true, timestamp);

            bool IsActivePbluDigitalizacion = await _cuentaRepository.IsActiveDigitalizacionPblu(idPblu);
            GenerarLog(timestamp, "INICIA CrearCuentaPersonaMoral", $"RFC: {cuentaPm.PersonaMoral.Rfc} - CURP: {cuentaPm.RepresentanteLegal.Persona.curp}");
            bool nuevoContribuyente = false;
            int idPersonaPersonaMoral = 0;
            int idPersonaRepresentanteLegal = 0;
            GenerarLog(timestamp, "_udnRepository.GetById(cuentaPm.UdnId)", "Consulta a base de datos, tabla UDN");
            Udn udn = await _udnRepository.GetById(cuentaPm.UdnId);
            if (udn == null || udn.Pblu != idPblu)
                throw new ErrorUdnNoAsociada("La Udn asociada a la cuenta no pertenece al Pblu " + idPblu);

            GenerarLog(timestamp, "_pbluRepository.GetById(idPblu)", "Consulta a base de datos, tabla PBLU");
            Pblu pblu = await _pbluRepository.GetById(idPblu);
            if (pblu == null)
                throw new ErrorIdPbluInexistente("No se encontró el pblu");

            GenerarLog(timestamp, "_cuentaRepository.GenerarCuentaClabe(pblu.IdPblu,udn.IdUdn)", "Inicio de generación cuenta clabe");
            string cuentaClabe = await _cuentaRepository.GenerarCuentaClabe(pblu.IdPblu,udn.IdUdn);
            GenerarLog(timestamp, "_cuentaRepository.GenerarCuentaClabe(pblu.IdPblu,udn.IdUdn)", $"Finalización de generación cuenta clabe, resultado= {cuentaClabe}");
            DtoAspCtaExpediente dtoAspCtaExpediente = await toDtoAspCtaExpediente(cuentaPm, udn, pblu, timestamp, cuentaClabe);

            GenerarLog(timestamp, "ValidateSolicitante", "INICIO validación");
            ValidateSolicitante(dtoAspCtaExpediente.solicitante, cuentaPm.NivelCuenta, "PM");
            GenerarLog(timestamp, "ValidateSolicitante", "FIN validación");
            GenerarLog(timestamp, "ValidateRepresentanteLegal", "INICIO validación");
            ValidateRepresentanteLegal(dtoAspCtaExpediente.repLegal);
            GenerarLog(timestamp, "ValidateRepresentanteLegal", "FIN validación");


            GenerarLog(timestamp, "_personaRepository.ExistePersona", "Consulta a base de datos (Representante legal)");
            //Registramos al representante legal en la tabla persona
            idPersonaRepresentanteLegal = await _personaRepository.ExistePersona(cuentaPm.RepresentanteLegal.Persona.rfc, cuentaPm.RepresentanteLegal.Persona.curp);
            if (idPersonaRepresentanteLegal == 0)
            {
                GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se creara el objeto del nuevo representante legal");
                nuevoContribuyente = true;
                var personaRepresentanteLegal = new Persona
                {
                    nombre = cuentaPm.RepresentanteLegal.Persona.nombre,
                    apellidoPaterno = cuentaPm.RepresentanteLegal.Persona.apellidoPaterno,
                    apellidoMaterno = cuentaPm.RepresentanteLegal.Persona.apellidoMaterno,

                    curp = cuentaPm.RepresentanteLegal.Persona.curp,
                    rfc = cuentaPm.RepresentanteLegal.Persona.rfc,

                    aspCodigoPostal = cuentaPm.RepresentanteLegal.Domicilio.codPostal,
                    calle = cuentaPm.RepresentanteLegal.Domicilio.callePrincipal,
                    celular = cuentaPm.RepresentanteLegal.Persona.celular,
                    correo = cuentaPm.RepresentanteLegal.Persona.correo,
                    fechaCreacion = DateTime.Now,
                    fechaNacimiento = cuentaPm.RepresentanteLegal.Persona.fechaNacimiento,
                    idNacionalidad = cuentaPm.RepresentanteLegal.Persona.idNacionalidad,
                    idOcupacion = cuentaPm.RepresentanteLegal.Persona.idOcupacion,
                    sexo = cuentaPm.RepresentanteLegal.Persona.sexo,

                    idGiro = cuentaPm.RepresentanteLegal.Persona.idOcupacion,
                    colonia = cuentaPm.RepresentanteLegal.Domicilio.colonia,
                    calle2 = cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria2,
                    calle3 = cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria3,
                    numInt = cuentaPm.RepresentanteLegal.Domicilio.numInterior,
                    numExt = cuentaPm.RepresentanteLegal.Domicilio.numExterior,
                    serieFiel = cuentaPm.RepresentanteLegal.Persona.serieFirmaElect,
                    geolocalizacion = cuentaPm.RepresentanteLegal.Persona.geolocalizacion,
                    idPais = 117,
                    idEntidad = cuentaPm.RepresentanteLegal.Persona.entidadNacimiento,
                };
                idPersonaRepresentanteLegal = await _personaRepository.Insert(personaRepresentanteLegal);
                GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se insertó en base de datos el nuevo representante legal");
            }

            //Registramos la persona moral en la tabla persona
            string nombre = "";
            string apellidoPaterno = "";
            string apellidoMaterno = null;
            //Obtenemos el nombre, como en blu
            ObtenerNombre(cuentaPm, out nombre, out apellidoPaterno, out apellidoMaterno);
            GenerarLog(timestamp, "_personaRepository.ExistePersona", "Consulta a base de datos (Persona moral)");
            idPersonaPersonaMoral = await _personaRepository.ExistePersona(cuentaPm.PersonaMoral.Rfc, cuentaPm.PersonaMoral.Rfc);
            if (idPersonaPersonaMoral == 0)
            {

                GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se creara el objeto de la nueva persona moral");
                nuevoContribuyente = true;
                var personaPersonaMoral = new Persona
                {
                    nombre = nombre,
                    apellidoPaterno = apellidoPaterno,
                    apellidoMaterno = apellidoMaterno,
                    tipoPersona = "M",
                    razonSocial = cuentaPm.PersonaMoral.RazonSocial,
                    curp = cuentaPm.PersonaMoral.Rfc,
                    rfc = cuentaPm.PersonaMoral.Rfc,

                    aspCodigoPostal = cuentaPm.PersonaMoral.Domicilio.codPostal,
                    calle = cuentaPm.PersonaMoral.Domicilio.callePrincipal,
                    celular = cuentaPm.PersonaMoral.Telefono,

                    fechaCreacion = DateTime.Now,
                    fechaNacimiento = cuentaPm.PersonaMoral.FechaCreacion,
                    idNacionalidad = Convert.ToInt32(cuentaPm.PersonaMoral.Nacionalidad),
                    montoMaximoAhorro = cuentaPm.PersonaMoral.Perfil.montoMax,
                    ingresoMensual = cuentaPm.PersonaMoral.Perfil.ingresosMensuales,

                    colonia = cuentaPm.PersonaMoral.Domicilio.colonia,
                    calle2 = cuentaPm.PersonaMoral.Domicilio.calleSecundaria2,
                    calle3 = cuentaPm.PersonaMoral.Domicilio.calleSecundaria3,
                    numInt = cuentaPm.PersonaMoral.Domicilio.numInterior,
                    numExt = cuentaPm.PersonaMoral.Domicilio.numExterior,

                    geolocalizacion = cuentaPm.PersonaMoral.Geolocalizacion,
                    idPais = 117,
                    serieFiel = cuentaPm.PersonaMoral.SerieFirmaElect,
                };
                idPersonaPersonaMoral = await _personaRepository.Insert(personaPersonaMoral);

            }

            if (nuevoContribuyente)
            {
                GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se crea el objeto del nuevo contribuyente");
                //Registramos en la tabla contribuyente
                var contribuyente = new Contribuyente
                {
                    PersonaMoral = idPersonaPersonaMoral,
                    Representante = idPersonaRepresentanteLegal,
                    Activo = true,
                    FechaCreacion = DateTime.Now,
                    FechaActualizacion = DateTime.Now,
                    UsuarioCreacion = "processCuenta"
                };
                GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se insertó en base de datos el nuevo contribuyente");
                await _contribuyenteRepository.Insert(contribuyente);
            }

            List<Expediente> expedientes = new List<Expediente>();

            //Guardamos los expedientes del representante legal
            foreach (var representanteLegalComprobante in cuentaPm.RepresentanteLegal.Comprobantes)
            {
                var expediente = new Expediente
                {
                    Extension = representanteLegalComprobante.extension,
                    NumIdentificacion = representanteLegalComprobante.numIdentificacion,
                    Tipo = tiposDocumento.FirstOrDefault(x => x.Item1 == representanteLegalComprobante.idTipoIdentificacion)?.Item2,
                    IdPersona = idPersonaPersonaMoral,
                    RepLegal = "N"
                };
                var idExpediente = await _expedienteRepository.Insert(expediente);
                expediente.Id = idExpediente;
                expedientes.Add(expediente);
            }

            List<DtoAspCtaExpediente> items = new List<DtoAspCtaExpediente>();

            foreach (var expediente in expedientes)
            {
                await _expedienteRepository.UpdateClabe(expediente.Id, dtoAspCtaExpediente.cuentaReferencia);
            }
            items.Add(dtoAspCtaExpediente);

            var listCtas = await _aspApiClient.EnviarAltaRefCtaExpedienteFinal(JsonSerializer.Serialize(items), idPblu, items?.FirstOrDefault()?.cuentaReferencia ?? string.Empty, timestamp);

            foreach (var cta in listCtas)
            {
                if (cta.resultado.ToUpper() == "ERROR")
                {

                    GenerarLog(timestamp, "CrearCuentaPersonaMoral", $"No se genero la cuenta: {cta.observaciones}");
                    throw new ErrorPeticionMalformada("No se genero la cuenta: " + cta.observaciones);
                }
                else
                {
                    Entities.DataBase.Cuenta cuenta = new Entities.DataBase.Cuenta
                    {
                        Pblu = idPblu,
                        Uuid = cuentaPm.Uuid.Length > 60 ? cuentaPm.Uuid.Substring(0, 60) : cuentaPm.Uuid,
                        Clabe = cta.cuenta_referencia,
                        Activo = CambioStatusCuenta(0, IsActivePbluDigitalizacion,2),
                        FechaCreacion = DateTime.Now,
                        Estado = "OK",
                        Udn = cuentaPm.UdnId,
                        UsuarioCreacion = nombreUsuario,
                        IdPersona = idPersonaPersonaMoral,
                        BluBlackList = false,
                        MontoPermitido = false,
                        BluMontoLimite = 300000,
                        Nivel = cuentaPm.NivelCuenta
                    };
                    GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se insertara la cuenta en la tabla de cuenta");
                    await _cuentaRepository.Insert(cuenta);

                    var _clabe = await _udnRepository.GetClabe(cuentaPm.UdnId);
                    if (_clabe.Equals(""))
                    {
                        GenerarLog(timestamp, "CrearCuentaPersonaMoral", "Se actualiza la cuenta CLABE");
                        await _udnRepository.UpdateClabe(cuentaPm.UdnId, cta.cuenta_referencia);
                    }
                }
            }

            foreach (var expediente in expedientes)
            {
                await _expedienteRepository.UpdateClabe(expediente.Id, dtoAspCtaExpediente.cuentaReferencia);
            }

            DtoClabe clabeNueva = new DtoClabe
            {
                Clabe = listCtas.FirstOrDefault().cuenta_referencia
            };

            GenerarLog(timestamp, "FINALIZA CrearCuentaPersonaMoral", $"RFC: {cuentaPm.PersonaMoral.Rfc} - CURP: {cuentaPm.RepresentanteLegal.Persona.curp}");
            return clabeNueva;
        }


        #endregion

        #region Actualiza Cuenta Expediente Persona Moral

        public async Task<DtoClabe> ActualizaCuentaExpedienteMoral(DtoCtaExpedienteActualizaPMoral cuentaPm, int idPblu, string nombreUsuario)
        {
            await ValidarBloqueoAltaReferencia(idPblu, false, true);
            DateTime currentTime = DateTime.Now;
            string timestamp = currentTime.ToString("yyyyMMddHHmmss");
            DtoClabe clabeNueva = new DtoClabe();

            var cuenta = await _cuentaRepository.GetByClabe(cuentaPm.clabe);

            if (cuenta != null)
            {
                Contribuyente? contribuyente = await _contribuyenteRepository.GetByPMoral(cuenta.IdPersona ?? 0);

                // TODO - Validar que otros campos pueden ser actualizados
                contribuyente.FechaActualizacion = DateTime.Now;

                Persona? representanteLegal = await _personaRepository.GetById(contribuyente.Representante);

                representanteLegal.tipoPersona = null;
                representanteLegal.nombre = cuentaPm.RepresentanteLegal.Persona.nombre;
                representanteLegal.apellidoPaterno = cuentaPm.RepresentanteLegal.Persona.apellidoPaterno;
                representanteLegal.apellidoMaterno = cuentaPm.RepresentanteLegal.Persona.apellidoMaterno;

                representanteLegal.curp = cuentaPm.RepresentanteLegal.Persona.curp;
                representanteLegal.rfc = cuentaPm.RepresentanteLegal.Persona.rfc;

                representanteLegal.aspCodigoPostal = cuentaPm.RepresentanteLegal.Domicilio.codPostal;
                representanteLegal.calle = cuentaPm.RepresentanteLegal.Domicilio.callePrincipal;
                representanteLegal.celular = cuentaPm.RepresentanteLegal.Persona.celular;
                representanteLegal.correo = cuentaPm.RepresentanteLegal.Persona.correo;
                representanteLegal.fechaCreacion = DateTime.Now;
                representanteLegal.fechaNacimiento = cuentaPm.RepresentanteLegal.Persona.fechaNacimiento;
                representanteLegal.idNacionalidad = cuentaPm.RepresentanteLegal.Persona.idNacionalidad;
                representanteLegal.idOcupacion = cuentaPm.RepresentanteLegal.Persona.idOcupacion;
                representanteLegal.sexo = cuentaPm.RepresentanteLegal.Persona.sexo;

                representanteLegal.idGiro = cuentaPm.RepresentanteLegal.Persona.idOcupacion;
                representanteLegal.colonia = cuentaPm.RepresentanteLegal.Domicilio.colonia;
                representanteLegal.calle2 = cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria2;
                representanteLegal.calle3 = cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria3;
                representanteLegal.numInt = cuentaPm.RepresentanteLegal.Domicilio.numInterior;
                representanteLegal.numExt = cuentaPm.RepresentanteLegal.Domicilio.numExterior;
                representanteLegal.serieFiel = cuentaPm.RepresentanteLegal.Persona.serieFirmaElect;
                representanteLegal.geolocalizacion = cuentaPm.RepresentanteLegal.Persona.geolocalizacion;
                representanteLegal.idPais = 117;
                representanteLegal.idEntidad = cuentaPm.RepresentanteLegal.Persona.entidadNacimiento;

                Persona? personaMoral = await _personaRepository.GetById(contribuyente.PersonaMoral);
                //Registramos la persona moral en la tabla persona
                string nombre = "";
                string apellidoPaterno = "";
                string apellidoMaterno = null;
                //Obtenemos el nombre, como en blu
                ObtenerNombre(cuentaPm, out nombre, out apellidoPaterno, out apellidoMaterno);

                personaMoral.nombre = nombre;
                personaMoral.apellidoPaterno = apellidoPaterno;
                personaMoral.apellidoMaterno = apellidoMaterno;
                personaMoral.tipoPersona = "M";
                personaMoral.curp = cuentaPm.PersonaMoral.Rfc;
                personaMoral.rfc = cuentaPm.PersonaMoral.Rfc;

                personaMoral.aspCodigoPostal = cuentaPm.PersonaMoral.Domicilio.codPostal;
                personaMoral.calle = cuentaPm.PersonaMoral.Domicilio.callePrincipal;
                personaMoral.celular = cuentaPm.PersonaMoral.Telefono;

                personaMoral.fechaCreacion = DateTime.Now;
                personaMoral.fechaNacimiento = cuentaPm.PersonaMoral.FechaCreacion;
                personaMoral.idNacionalidad = Convert.ToInt32(cuentaPm.PersonaMoral.Nacionalidad);
                personaMoral.montoMaximoAhorro = cuentaPm.PersonaMoral.Perfil.montoMax;
                personaMoral.ingresoMensual = cuentaPm.PersonaMoral.Perfil.ingresosMensuales;

                personaMoral.colonia = cuentaPm.PersonaMoral.Domicilio.colonia;
                personaMoral.calle2 = cuentaPm.PersonaMoral.Domicilio.calleSecundaria2;
                personaMoral.calle3 = cuentaPm.PersonaMoral.Domicilio.calleSecundaria3;
                personaMoral.numInt = cuentaPm.PersonaMoral.Domicilio.numInterior;
                personaMoral.numExt = cuentaPm.PersonaMoral.Domicilio.numExterior;

                personaMoral.geolocalizacion = cuentaPm.PersonaMoral.Geolocalizacion;
                personaMoral.idPais = 117;

                List<Expediente> expedientes = cuentaPm.RepresentanteLegal.Comprobantes.Select(x => new Expediente
                {
                    Extension = x.extension,
                    NumIdentificacion = x.numIdentificacion,
                    Tipo = tiposDocumento.FirstOrDefault(y => y.Item1 == x.idTipoIdentificacion)?.Item2,
                    IdPersona = personaMoral.idPersona,
                    RepLegal = "N"
                }).ToList();

                Udn? udn = await _udnRepository.GetById(cuentaPm.UdnId);
                if (udn == null)
                    throw new ErrorUdnNoExiste("No se encontró la udn");
                Pblu? pblu = await _pbluRepository.GetById(idPblu);
                if (pblu == null)
                    throw new ErrorIdPbluInexistente("No se encontró el pblu");
                string cuentaClabe = await _cuentaRepository.GenerarCuentaClabe(pblu.IdPblu, udn.IdUdn);
                DtoAspCtaExpediente dtoAspCtaExpediente = await toDtoAspCtaExpediente(cuentaPm, udn, pblu, timestamp, cuentaClabe );

                ValidateSolicitante(dtoAspCtaExpediente.solicitante, cuentaPm.NivelCuenta, "PM");
                ValidateRepresentanteLegal(dtoAspCtaExpediente.repLegal);

                List<DtoAspCtaExpediente> items = new List<DtoAspCtaExpediente>();

                items.Add(dtoAspCtaExpediente);

                List<DtoCtaRefFinalResp> listCtas = await _aspApiClient.EnviarAltaRefCtaExpedienteFinal(JsonSerializer.Serialize(items), idPblu, items?.FirstOrDefault()?.cuentaReferencia ?? string.Empty);

                foreach (var cta in listCtas)
                {
                    if (cta.resultado.Equals("ERROR"))
                    {
                        cuenta.Estado = "ERROR";
                        await _cuentaRepository.ActualizaEstado(cuenta);
                        expedientes.ForEach(async exp =>
                        {
                            exp.IdPersona = personaMoral.idPersona;
                            exp.Estado = 0;
                            exp.FechaCreacion = DateTime.Now;
                            exp.RepLegal = "N";
                            await _expedienteRepository.Insert(exp);
                           await _expedienteRepository.UpdateClabe(exp.Id, dtoAspCtaExpediente.cuentaReferencia);
                        });
                        throw new ErrorPeticionMalformada("No se actualizó la cuenta");
                    }
                    cuenta.Estado = "OK";
                    await _cuentaRepository.ActualizaEstado(cuenta);
                    await _personaRepository.Update(personaMoral);
                    await _personaRepository.Update(representanteLegal);
                    await _contribuyenteRepository.Update(contribuyente);
                    expedientes.ForEach(async exp =>
                    {
                        exp.IdPersona = personaMoral.idPersona;
                        exp.Estado = 0;
                        exp.FechaCreacion = DateTime.Now;
                        exp.RepLegal = "N";
                        await _expedienteRepository.Insert(exp);
                        await _expedienteRepository.UpdateClabe(exp.Id, dtoAspCtaExpediente.cuentaReferencia);
                    });
                }

                clabeNueva = new DtoClabe
                {
                    Clabe = listCtas?.FirstOrDefault()?.cuenta_referencia
                };

            }
            else
            {
                throw new ErrorCuentaInexistente("La cuenta no está registrada");
            }
            return clabeNueva;
        }

        #endregion

        #region Crear persona fisica

        public async Task<DtoClabe> CrearCuentaPersonaFisica(DtoCtaExpediente cuentaObj, int idPblu, string nombreUsuario)
        {

            await ValidarBloqueoAltaReferencia(idPblu, true, false);
            var udn = await _udnRepository.GetById(cuentaObj.udnId);

            bool IsActivePbluDigitalizacion = await _cuentaRepository.IsActiveDigitalizacionPblu(idPblu);

            

            int personaId = 0;

            if (udn == null || udn.Pblu != idPblu)
                throw new ErrorUdnNoAsociada("La Udn asociada a la cuenta no pertene al Pblu " + idPblu);

            // LA cuenta_clabe_padre se toma de la tabla pblu porque no existe la tabla udn_prefijo_pblu_cta
            Pblu? udnPrefijo = await _pbluRepository.GetById(idPblu);

            if (udnPrefijo == null)
                throw new ErrorUdnNoExiste("La udn no existe");

            string cuentaClabe = await _cuentaRepository.GenerarCuentaClabe(udnPrefijo.IdPblu, udn.IdUdn);
            DtoAspCtaExpediente item = await toAspCtaPFisica(cuentaObj, udnPrefijo.ClabePblu, idPblu, cuentaObj.udnId, cuentaClabe);

            ValidateSolicitante(item.solicitante, cuentaObj.nivel_cuenta, "PF");

            Persona? personaExiste = await _personaRepository.GetByRfc(cuentaObj.persona.rfc);
            personaId = personaExiste != null ? personaExiste.idPersona : 0;
            if (personaId == 0)
            {
                Persona? personaCurpExiste = await _personaRepository.GetByCurp(cuentaObj.persona.curp);
                if (personaCurpExiste != null)
                {
                    throw new ErrorUdnNoExiste("Curp registrado para otro rfc");
                }
                personaId = await _personaRepository.Insert(new Persona
                {
                    nombre = cuentaObj.persona.nombre,
                    apellidoPaterno = cuentaObj.persona.apellidoPaterno,
                    apellidoMaterno = string.IsNullOrEmpty(cuentaObj.persona.apellidoMaterno) ? string.Empty : cuentaObj.persona.apellidoMaterno,
                    tipoPersona = "F",
                    curp = cuentaObj.persona.curp,
                    rfc = cuentaObj.persona.rfc,
                    telefono = cuentaObj.persona.telefono,
                    aspCodigoPostal = cuentaObj.domicilio.codPostal,
                    calle = cuentaObj.domicilio.callePrincipal,
                    celular = cuentaObj.persona.celular,
                    correo = cuentaObj.persona.correo,
                    fechaCreacion = DateTime.Now,
                    fechaNacimiento = cuentaObj.persona.fechaNacimiento,
                    idNacionalidad = cuentaObj.persona.idNacionalidad,
                    idOcupacion = cuentaObj.persona.idOcupacion,
                    sexo = cuentaObj.persona.sexo[0].ToString(), //Cambiar a char
                    montoMaximoAhorro = cuentaObj.perfil.montoMax,
                    ingresoMensual = cuentaObj.perfil.ingresosMensuales,
                    idGiro = cuentaObj.persona.idOcupacion,
                    colonia = cuentaObj.domicilio.colonia,
                    calle2 = cuentaObj.domicilio.calleSecundaria2,
                    calle3 = cuentaObj.domicilio.calleSecundaria3,
                    numeroInterior = int.TryParse(cuentaObj.domicilio.numInterior, out int nInt) ? nInt : 0,
                    numeroExterior = int.TryParse(cuentaObj.domicilio.numExterior, out int nExt) ? nExt : 0,
                    serieFiel = cuentaObj.persona.serieFirmaElect,
                    geolocalizacion = cuentaObj.persona.geolocalizacion,
                    idPais = 117,
                    idEntidad = cuentaObj.persona.entidadNacimiento
                });
            }
            else
            {
                if (string.IsNullOrWhiteSpace(personaExiste?.curp))
                {
                    Persona? personaCurpExiste =  await _personaRepository.GetByCurp(cuentaObj.persona.curp);
                    if (personaCurpExiste == null)
                    {
                        await _personaRepository.UpdateCurp(cuentaObj.persona.rfc, cuentaObj.persona.curp);
                    }
                    else
                    {
                        throw new ErrorUdnNoExiste("Curp registrado para otro rfc");
                    }
                }
                else
                {
                    item.solicitante.curpCuenta = personaExiste.curp;
                }
            }

            List<DtoAspCtaExpediente> items = new List<DtoAspCtaExpediente>
            {
                item
            };

            var jsonEnviar = JsonSerializer.Serialize(items);

            List<DtoCtaRefFinalResp> listCtas = await _aspApiClient.EnviarAltaRefCtaExpedienteFinal(jsonEnviar, idPblu, items?.FirstOrDefault()?.cuentaReferencia ?? string.Empty);

            List<Expediente> expedientes = cuentaObj.comprobantes?.Select(x => new Expediente
            {
                Extension = x.extension,
                NumIdentificacion = x.numIdentificacion,
                Tipo = tiposDocumento.FirstOrDefault(y => y.Item1 == x.idTipoIdentificacion)?.Item2,
                IdPersona = personaId
            }).ToList() ?? new List<Expediente>();

            foreach (var cta in listCtas)
            {
                if (cta.resultado.ToUpper() == "ERROR")
                {
                    throw new ErrorPeticionMalformada("No se genero la cuenta: " + cta.observaciones);
                }
                else
                {
                    Entities.DataBase.Cuenta cuenta = new Entities.DataBase.Cuenta()
                    {
                        Pblu = idPblu,
                        Uuid = cuentaObj.uuid.Length > 60 ? cuentaObj.uuid.Substring(0, 60) : cuentaObj.uuid,
                        Clabe = cta.cuenta_referencia,
                        Activo = CambioStatusCuenta(cuentaObj.nivel_cuenta, IsActivePbluDigitalizacion,1),
                        FechaCreacion = DateTime.Now,
                        Estado = "OK",
                        NoNotificarAbono = false,
                        Udn = cuentaObj.udnId,
                        UsuarioCreacion = nombreUsuario,
                        IdPersona = personaId,
                        BluBlackList = false,
                        MontoPermitido = false,
                        BluMontoLimite = 300000,
                        Nivel = cuentaObj.nivel_cuenta,

                    };
                    await _cuentaRepository.Insert(cuenta);

                    var _clabe = await _udnRepository.GetClabe(cuentaObj.udnId);
                   
                    if (_clabe.Equals(""))
                    {
                        await _udnRepository.UpdateClabe(cuentaObj.udnId, cta.cuenta_referencia);
                    }
                }
            }

            try
            {
                foreach (var expediente in expedientes)
                {
                    expediente.IdPersona = personaId;
                    expediente.Estado = 0;
                    expediente.FechaCreacion = DateTime.Now;
                    expediente.RepLegal = "N";
                    await _expedienteRepository.Insert(expediente);
                    expediente.Clabe = item.cuentaReferencia;
                    await _expedienteRepository.UpdateClabe(expediente.Id, item.cuentaReferencia);
                }
            }
            catch (Exception e)
            {
                await _logsBusiness.RegistraError(e, idPblu, LogLevel.Error, "");
            }

            DtoClabe clabeNueva = new DtoClabe
            {
                Clabe = listCtas?.FirstOrDefault()?.cuenta_referencia
            };

            return clabeNueva;
        }

        #endregion

        #region Actualiza Cuenta Expediente

        public async Task<DtoClabe> ActualizaCuentaExpediente(DtoCtaExpedienteActualiza ctaEje, int idPblu, string nombreUsuario)
        {

            await ValidarBloqueoAltaReferencia(idPblu, true, false);
            DtoClabe clabeNueva;

            var cuenta = await _cuentaRepository.GetByClabe(ctaEje.clabe);

            if (cuenta != null)
            {
                var udn = await _udnRepository.GetById(ctaEje.udnId);

                if (udn == null || udn.Pblu != idPblu)
                    throw new ErrorUdnNoAsociada("La Udn asociada a la cuenta no pertene al Pblu " + idPblu);

                cuenta.Uuid = ctaEje.uuid;
                cuenta.FehaActualizacion = DateTime.Now;
                cuenta.UsuarioActualizacion = nombreUsuario;
                cuenta.Nivel = ctaEje.nivelCuenta;

                //cuenta.IdPersona Representa el objeto Persona 

                Persona? persona = await _personaRepository.GetById(cuenta.IdPersona ?? 0);

                // begin:: Actualiza datos de persona

                persona.nombre = ctaEje.persona.nombre;
                persona.apellidoPaterno = ctaEje.persona.apellidoPaterno;
                persona.tipoPersona = "F";

                persona.curp = ctaEje.persona.curp;
                persona.rfc = ctaEje.persona.rfc;

                if (!string.IsNullOrEmpty(ctaEje.persona.apellidoMaterno))
                    persona.apellidoMaterno = ctaEje.persona.apellidoMaterno;

                persona.telefono = ctaEje.persona.telefono;
                persona.aspCodigoPostal = ctaEje.domicilio.codPostal;
                persona.calle = ctaEje.domicilio.callePrincipal;
                persona.celular = ctaEje.persona.celular;
                persona.correo = ctaEje.persona.correo;
                persona.fechaCreacion = DateTime.Now;
                persona.fechaNacimiento = ctaEje.persona.fechaNacimiento;
                persona.idNacionalidad = ctaEje.persona.idNacionalidad;
                persona.idOcupacion = ctaEje.persona.idOcupacion;
                persona.sexo = ctaEje.persona.sexo;
                persona.montoMaximoAhorro = ctaEje.perfil.montoMax;
                persona.ingresoMensual = ctaEje.perfil.ingresosMensuales;
                persona.idGiro = ctaEje.persona.idOcupacion;
                persona.colonia = ctaEje.domicilio.colonia;
                persona.calle2 = ctaEje.domicilio.calleSecundaria2;
                persona.calle3 = ctaEje.domicilio.calleSecundaria3;

                if (!string.IsNullOrEmpty(ctaEje.domicilio.numInterior))
                    persona.numeroInterior = Convert.ToInt32(ctaEje.domicilio.numInterior);

                if (!string.IsNullOrEmpty(ctaEje.domicilio.numExterior))
                    persona.numeroExterior = Convert.ToInt32(ctaEje.domicilio.numExterior);

                persona.serieFiel = ctaEje.persona.serieFirmaElect;
                persona.geolocalizacion = ctaEje.persona.geolocalizacion;
                persona.idPais = 117;
                persona.idEntidad = ctaEje.persona.entidadNacimiento;

                // end:: Actualiza datos persona

                ViewPbluUdn? view = await _viewPbluUdnRepository.GetUdnById(ctaEje.udnId);

                Pblu? pblu = await _pbluRepository.GetById(view.idPblu);

                //DtoAspCtaExpediente item = toAspCtaPFisica(cuentaObj, pblu.ClabePblu, idPblu, cuentaObj.udnId);

                DtoAspCtaExpediente result = new DtoAspCtaExpediente
                {
                    cuentaReferencia = ctaEje.clabe,
                    cuentaConcentradora = pblu.ClabePblu,
                    consecutivo = null,
                    control = null,
                    correoReferencia = ctaEje.persona.correo,
                    error = null,
                    fecha = DateTime.Now.ToString("ddMMyyyy"),
                    nombreReferencia = $"{ctaEje.persona.nombre} {ctaEje.persona.apellidoPaterno} {ctaEje.persona.apellidoMaterno}",
                    observaciones = null,
                    procesado = null,
                    rfcReferencia = ctaEje.persona.rfc,
                    curpReferencia = ctaEje.persona.curp,
                    telefonoReferencia = ctaEje.persona.telefono,
                    tipoCuenta = "REFERENCIADA",
                    accion = "AGREGAR"
                };

                var repLegal = new DtoAspRepLegal();
                repLegal.nombreCuenta = "Dummy";
                repLegal.ocupacionIdCuenta = ctaEje.persona.idOcupacion > 0 ? ctaEje.persona.idOcupacion : 1;

                if (ctaEje.persona.idOcupacion > 0)
                {
                    AspOcupacion? ocupacion = await _ocupacionRepository.getOptionalOcupacionById(ctaEje.persona.idOcupacion);
                    repLegal.ocupacion_cuenta = ocupacion == null ? "Sin Datos" : ocupacion.descOcupacion;
                }
                else
                {
                    repLegal.ocupacion_cuenta = "Sin Datos";
                }

                result.repLegal = repLegal;

                DtoAspSolicitante solicitante = new DtoAspSolicitante();

                solicitante.curpCuenta = ctaEje.persona.curp;
                solicitante.callePrincipalCuenta = ctaEje.domicilio.callePrincipal;
                solicitante.calleSecundariaCuenta = string.IsNullOrEmpty(ctaEje.domicilio.calleSecundaria2) ? "Sin valor" : ctaEje.domicilio.calleSecundaria2;
                solicitante.calleSecundaria2Cuenta = string.IsNullOrEmpty(ctaEje.domicilio.calleSecundaria3) ? "Sin valor" : ctaEje.domicilio.calleSecundaria3;
                solicitante.serieFirmaElectCuenta = ctaEje.persona.serieFirmaElect;
                solicitante.celularCuenta = ctaEje.persona.celular;
                solicitante.ciudadCuenta = ctaEje.domicilio.ciudad;

                //solicitante.coloniaIdcuenta = ctaEje.domicilio.colonia;
                solicitante.coloniaIdCuenta = ctaEje.domicilio.codPostal;
                solicitante.colonia_cuenta = ctaEje.domicilio.colonia;
                solicitante.cp_cuenta = ctaEje.domicilio.codPostal;
                solicitante.correoCuenta = ctaEje.persona.correo;
                solicitante.denominacionCuenta = 1;

                solicitante.nivelCuenta = $"CTA_N{ctaEje.nivelCuenta}";
                solicitante.entidadNacIdCuenta = ctaEje.persona.entidadNacimiento;
                solicitante.fechaNacCuenta = ctaEje.persona.fechaNacimiento;
                solicitante.generoCuenta = ctaEje.persona.sexo[0].ToString();
                solicitante.geolocalizacionCuenta = "LAT , LON";
                solicitante.ingresosCuenta = ctaEje.perfil.ingresosMensuales.ToString();
                solicitante.montoMaxAhoCuenta = ctaEje.perfil.montoMax.ToString();

                solicitante.nacionalidadIdCuenta = ctaEje.persona.idNacionalidad;
                solicitante.noExteriorCuenta = ctaEje.domicilio.numExterior;
                solicitante.noInteriorCuenta = ctaEje.domicilio.numInterior;
                solicitante.nombreCuenta = ctaEje.persona.nombre;
                solicitante.tipoIdentIdCuenta = ctaEje.persona.tipoIdentificacionOf;
                solicitante.numIdentCuenta = ctaEje.persona.numIdentificacionOf.ToString();

                if (ctaEje.persona.idOcupacion > 0)
                {
                    AspOcupacion? ocupacion = await _ocupacionRepository.getOptionalOcupacionById(ctaEje.persona.idOcupacion);
                    solicitante.ocupacion_cuenta = ocupacion == null ? "Sin Datos" : ocupacion.descOcupacion;
                }
                else
                {
                    solicitante.ocupacion_cuenta = "Sin Datos";
                }

                solicitante.ocupacionIdCuenta = ctaEje.persona.idOcupacion;

                ASPNacionalidad? nacionalida = await _nacionalidadRepository.findByPaisId(ctaEje.persona.idNacionalidad);
                if (nacionalida == null)
                    throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

                solicitante.paisNacIdCuenta = nacionalida.PaisId ?? default;

                ASPPais? pais = await _paisRepository.GetById(nacionalida.PaisId ?? default);
                if (pais == null)
                    throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

                solicitante.pais_nac_cuenta = pais.DescPais;
                solicitante.nacionalidad_cuenta = nacionalida.DescNacionalidad;
                solicitante.prApellidoCuenta = ctaEje.persona.apellidoMaterno; ///Validar
                solicitante.rfcCuenta = ctaEje.persona.rfc;
                solicitante.sgApellidoCuenta = ctaEje.persona.apellidoPaterno;
                solicitante.serieFirmaElectCuenta = ctaEje.persona.serieFirmaElect;
                solicitante.telefonoCuenta = ctaEje.persona.telefono;
                solicitante.tipoPersonaCuenta = "F";
                solicitante.unidadNegocioCuenta = ctaEje.udnId;

                if (ctaEje.comprobantes.Any())
                {
                    foreach (var comp in ctaEje.comprobantes)
                    {
                        if (comp.idTipoIdentificacion == 1)
                        {
                            solicitante.tipoIdentIdCuenta = comp.idTipoIdentificacion;

                            if (comp.numIdentificacion.Trim().Equals(""))
                            {
                                solicitante.numIdentCuenta = "1111111111";
                                continue;
                            }
                            solicitante.numIdentCuenta = comp.numIdentificacion; continue;
                        }

                        if (comp.idTipoIdentificacion >= 2 && comp.idTipoIdentificacion < 100)
                        {
                            solicitante.tipoIdentIdCuenta = comp.idTipoIdentificacion; continue;

                        }
                        if (comp.idTipoIdentificacion >= 100 && comp.idTipoIdentificacion < 200)
                        {
                            continue;
                        }
                        if (comp.idTipoIdentificacion >= 200 && comp.idTipoIdentificacion < 300)
                        {
                            continue;
                        }
                        if (comp.idTipoIdentificacion < 300 || comp.idTipoIdentificacion < 400) ;
                    }
                }

                ValidateSolicitante(solicitante, ctaEje.nivelCuenta, "PF");
                result.solicitante = solicitante;

                List<DtoAspCtaExpediente> items = new List<DtoAspCtaExpediente>();
                items.Add(result);

                var jsonEnviar = JsonSerializer.Serialize(items);

                List<DtoCtaRefFinalResp> listCtas = await _aspApiClient.EnviarAltaRefCtaExpedienteFinal(jsonEnviar, idPblu, items?.FirstOrDefault()?.cuentaReferencia ?? string.Empty);

                List<Expediente> expedientes = ctaEje.comprobantes.Select(x => new Expediente
                {
                    Extension = x.extension,
                    NumIdentificacion = x.numIdentificacion,
                    Tipo = tiposDocumento.FirstOrDefault(y => y.Item1 == x.idTipoIdentificacion)?.Item2,
                    IdPersona = persona.idPersona
                }).ToList();

                foreach (var cta in listCtas)
                {
                    if (cta.resultado.Equals("ERROR"))
                    {
                        cuenta.Estado = "ERROR";
                       await _cuentaRepository.ActualizaEstado(cuenta);
                        expedientes.ForEach(async exp =>
                        {
                            exp.IdPersona = persona.idPersona;
                            exp.Estado = 0;
                            exp.FechaCreacion = DateTime.Now;
                            exp.RepLegal = "N";
                           await _expedienteRepository.Insert(exp);
                           await _expedienteRepository.UpdateClabe(exp.Id, result.cuentaReferencia);
                        });
                        throw new ErrorPeticionMalformada("No se actualizó la cuenta");
                    }
                    cuenta.Estado = "OK";
                    await _cuentaRepository.ActualizaEstado(cuenta);
                   await _personaRepository.Update(persona);
                    expedientes.ForEach(async exp =>
                    {
                        exp.IdPersona = persona.idPersona;
                        exp.Estado = 0;
                        exp.FechaCreacion = DateTime.Now;
                        exp.RepLegal = "N";
                        await _expedienteRepository.Insert(exp);
                        await _expedienteRepository.UpdateClabe(exp.Id, result.cuentaReferencia);
                    });
                }

                clabeNueva = new DtoClabe
                {
                    Clabe = listCtas?.FirstOrDefault()?.cuenta_referencia
                };
            }
            else
            {
                throw new ErrorCuentaInexistente("La cuenta no está registrada");
            }

            return clabeNueva;
        }

        public async Task<bool> ActivateCuenta(string clabe,int idPblu)
        {
            var cuenta = await _cuentaRepository.GetByClabe(clabe);
            if (cuenta != null)
            {
                if (cuenta.Pblu != idPblu) throw new Exception("Esta cuenta no existe.");
                if (!cuenta.Activo)
                {
                    cuenta.Activo = true;
                    return await _cuentaRepository.ActivarCuentaClabe(cuenta.Clabe); // Método de actualización que retorna un bool
                }

            }
            return false;
        }


        public async Task<bool> ExisteActivateCuenta(string clabe)
        {
            var cuenta = await _cuentaRepository.GetByClabe(clabe);
            return cuenta != null;
        }
        #endregion

        #region Verifica si existe una cuenta por medio de la clabe y el id pblu

        public async Task<bool> VerificaClabeByIdPblu(int idPblu, string clabe)
        {
            try
            {
                return await _cuentaRepository.VerificarCuentaByPblu(idPblu, clabe);    
            }catch(Exception ex)
            {
                Console.WriteLine($"Error al consultar la cuenta {clabe} del id pblu {idPblu}");
                throw;
            }
        }
        #endregion

        #region Private Methods
        private void GenerarLog(string timestamp, string metodo, string text)
        {
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.ffffff");
            Console.WriteLine($"{current_time_formatt} INFO [{metodo} - {timestamp}] -> {text}");

        }
        private List<Tuple<int, string>> tiposDocumento = new List<Tuple<int, string>>
        {
            new Tuple<int, string>(101, "CURP"),
            new Tuple<int, string>(301, "RFC"),
            new Tuple<int, string>(302, "ACT_CON"),
            new Tuple<int, string>(201, "COMP_DOM"),
            new Tuple<int, string>(303, "POD_NOT"),
            new Tuple<int, string>(3, "CAR_MIL"),
            new Tuple<int, string>(4, "CED_PRO"),
            new Tuple<int, string>(8, "DOC_MIG"),
            new Tuple<int, string>(7, "INA"),
            new Tuple<int, string>(1, "INE"),
            new Tuple<int, string>(6, "LIC_CON"),
            new Tuple<int, string>(5, "OTRO"),
            new Tuple<int, string>(2, "PAS")
        };
        private static int[] ponderacion = new int[] { 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7 };

       

       

        private void ObtenerNombre(DtoCtaExpedientePMoral cuentaPm, out string nombre, out string apellidoPaterno, out string apellidoMaterno)
        {
            nombre = "";
            apellidoPaterno = "";
            apellidoMaterno = "";
            if (cuentaPm.PersonaMoral.RazonSocial.Length <= 44)
            {
                nombre = cuentaPm.PersonaMoral.RazonSocial;
                apellidoPaterno = cuentaPm.PersonaMoral.RazonSocial;
            }
            else if (cuentaPm.PersonaMoral.RazonSocial.Length >= 45 &&
                      cuentaPm.PersonaMoral.RazonSocial.Length <= 89)
            {
                nombre = cuentaPm.PersonaMoral.RazonSocial.Substring(0, 45);
                apellidoPaterno = cuentaPm.PersonaMoral.RazonSocial.Substring(45, cuentaPm.PersonaMoral.RazonSocial.Length-45);
            }
            else if (cuentaPm.PersonaMoral.RazonSocial.Length > 90 &&
                     cuentaPm.PersonaMoral.RazonSocial.Length <= 134)
            {
                nombre = cuentaPm.PersonaMoral.RazonSocial.Substring(0, 45);
                apellidoPaterno = cuentaPm.PersonaMoral.RazonSocial.Substring(45,45);
                apellidoMaterno = cuentaPm.PersonaMoral.RazonSocial.Substring(90, cuentaPm.PersonaMoral.RazonSocial.Length-90);
            }
            else if (cuentaPm.PersonaMoral.RazonSocial.Length > 134)
            {
                nombre = cuentaPm.PersonaMoral.RazonSocial.Substring(0, 45);
                apellidoPaterno = cuentaPm.PersonaMoral.RazonSocial.Substring(45,45);
                apellidoMaterno = cuentaPm.PersonaMoral.RazonSocial.Substring(90,45);
            }
        }

        private async Task<DtoAspCtaExpediente> toDtoAspCtaExpediente(DtoCtaExpedientePMoral cuentaPm, Udn udn, Pblu pblu,string timestamp, string cuentaClabe)
        {
            bool campoObligatorio = _cuentaRepository.GetCamposObligatorios();
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "INICIO Generación de instancias DTO y llenado de objetos");

            var dtoAspCtaExpediente = new DtoAspCtaExpediente
            {
                cuentaConcentradora = pblu.ClabePblu,
                consecutivo = null,
                control = null,
                correoReferencia = cuentaPm.RepresentanteLegal.Persona.correo,
                //cuentaReferencia = GenerarClabe(pblu.PrefijoPblu ?? 0, udn.IdUdn, udn.PrefijoClabe ?? "0", timestamp),
                cuentaReferencia = cuentaClabe,
                error = null,
                fecha = DateTime.Today.ToString("yyyyMMdd"),
                nombreReferencia = cuentaPm.PersonaMoral.RazonSocial.Trim(),
                observaciones = null,
                procesado = null,
                rfcReferencia = cuentaPm.PersonaMoral.Rfc,
                curpReferencia = null,
                telefonoReferencia = cuentaPm.PersonaMoral.Telefono,
                tipoCuenta = "REFERENCIADA",
                accion = "AGREGAR"
            };

            var solicitante = new DtoAspSolicitante();
            solicitante.callePrincipalCuenta = cuentaPm.PersonaMoral.Domicilio.callePrincipal;
            if (string.IsNullOrEmpty(cuentaPm.PersonaMoral.Domicilio.calleSecundaria2) ||
                cuentaPm.PersonaMoral.Domicilio.calleSecundaria2.Length <= 1 ||
                cuentaPm.PersonaMoral.Domicilio.calleSecundaria2.Trim().Length <= 1)
            {
                solicitante.calleSecundariaCuenta = "Sin valor";
            }
            else
            {
                solicitante.calleSecundariaCuenta = cuentaPm.PersonaMoral.Domicilio.calleSecundaria2;
            }

            if (string.IsNullOrEmpty(cuentaPm.PersonaMoral.Domicilio.calleSecundaria3) ||
                cuentaPm.PersonaMoral.Domicilio.calleSecundaria3.Length <= 1 ||
                cuentaPm.PersonaMoral.Domicilio.calleSecundaria3.Trim().Length <= 1)
            {
                solicitante.calleSecundaria2Cuenta = "Sin valor";
            }
            else
            {
                solicitante.calleSecundaria2Cuenta = cuentaPm.PersonaMoral.Domicilio.calleSecundaria3;
            }

            solicitante.celularCuenta = cuentaPm.PersonaMoral.Telefono;
            solicitante.ciudadCuenta = cuentaPm.PersonaMoral.Domicilio.ciudad;
            solicitante.cp_cuenta = cuentaPm.PersonaMoral.Domicilio.codPostal;

            solicitante.coloniaIdCuenta = cuentaPm.PersonaMoral.Domicilio.codPostal;
            solicitante.colonia_cuenta = cuentaPm.PersonaMoral.Domicilio.colonia;

            if (string.IsNullOrEmpty(cuentaPm.PersonaMoral.Email))
            {
                solicitante.correoCuenta = cuentaPm.RepresentanteLegal.Persona.correo;
            }
            else
            {
                solicitante.correoCuenta = cuentaPm.PersonaMoral.Email;
            }

            solicitante.denominacionCuenta = cuentaPm.PersonaMoral.Denominacion;
            solicitante.nivelCuenta = $"CTA_N{cuentaPm.NivelCuenta}";
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Inicia consulta de base de datos -> _nacionalidadRepository.findByPaisId");
            var nacionalidadSolicitante = await _nacionalidadRepository.findByPaisId(Convert.ToInt32(cuentaPm.PersonaMoral.Nacionalidad));
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Termina consulta de base de datos -> _nacionalidadRepository.findByPaisId");
            if (nacionalidadSolicitante == null)
                throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Inicia consulta de base de datos -> _paisRepository.GetById");
            var paisSolicitante = await _paisRepository.GetById(nacionalidadSolicitante.PaisId ?? default);
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Termina consulta de base de datos -> _paisRepository.GetById");
            if (paisSolicitante == null)
                throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

            solicitante.paisNacIdCuenta = nacionalidadSolicitante.PaisId ?? default;
            solicitante.pais_nac_cuenta = paisSolicitante.DescPais;
            solicitante.nacionalidad_cuenta = nacionalidadSolicitante.DescNacionalidad;
            solicitante.nacionalidadIdCuenta = paisSolicitante.PaisId;

            solicitante.entidadNacIdCuenta = cuentaPm.PersonaMoral.Entidad;

            var fechaNacimiento = DateTime.ParseExact(cuentaPm.PersonaMoral.FechaCreacion, "yyyyMMdd", CultureInfo.InvariantCulture);
            solicitante.fechaNacCuenta = fechaNacimiento.ToString("ddMMyyyy");
            solicitante.generoCuenta = null;
            solicitante.geolocalizacionCuenta = string.IsNullOrEmpty(cuentaPm.PersonaMoral.Geolocalizacion) && !campoObligatorio ? "Sin Datos" : cuentaPm.PersonaMoral.Geolocalizacion;
            solicitante.ingresosCuenta = cuentaPm.PersonaMoral.Perfil.ingresosMensuales.ToString();
            solicitante.montoMaxAhoCuenta = cuentaPm.PersonaMoral.Perfil.montoMax.ToString();
            solicitante.noExteriorCuenta = cuentaPm.PersonaMoral.Domicilio.numExterior;
            solicitante.noInteriorCuenta = cuentaPm.PersonaMoral.Domicilio.numInterior;
            solicitante.nombreCuenta = cuentaPm.PersonaMoral.RazonSocial;

            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Inicia consulta de base de datos ->  _giroRepository.GetById");
            var giroSolicitante = await _giroRepository.GetById(Convert.ToInt32(cuentaPm.PersonaMoral.Giro));
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Termina consulta de base de datos ->  _giroRepository.GetById");

            if (giroSolicitante == null)
                throw new ErrorGenerico("Giro no encontrada para el ID");

            solicitante.ocupacionIdCuenta = giroSolicitante.GiroId;
            solicitante.ocupacion_cuenta = giroSolicitante.DescGiro;

            solicitante.prApellidoCuenta = null;
            solicitante.rfcCuenta = cuentaPm.PersonaMoral.Rfc;
            solicitante.sgApellidoCuenta = null;
            solicitante.serieFirmaElectCuenta = cuentaPm.PersonaMoral.SerieFirmaElect;
            solicitante.telefonoCuenta = cuentaPm.PersonaMoral.Telefono;

            solicitante.tipoPersonaCuenta = "M";
            solicitante.unidadNegocioCuenta = cuentaPm.UdnId;

            dtoAspCtaExpediente.solicitante = solicitante;

            DtoAspRepLegal repLegal = new DtoAspRepLegal();
            repLegal.callePrincipalCuenta = cuentaPm.RepresentanteLegal.Domicilio.callePrincipal;
            if (string.IsNullOrEmpty(cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria2) ||
                cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria2.Length <= 1 ||
                cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria2.Trim().Length <= 1)
            {
                repLegal.calleSecundariaCuenta = "Sin valor";
            }
            else
            {
                repLegal.calleSecundariaCuenta = cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria2;
            }
            if (string.IsNullOrEmpty(cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria3) ||
                cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria3.Length <= 1 ||
                cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria3.Trim().Length <= 1)
            {
                repLegal.calleSecundaria2Cuenta = "Sin valor";
            }
            else
            {
                repLegal.calleSecundaria2Cuenta = cuentaPm.RepresentanteLegal.Domicilio.calleSecundaria3;
            }

            if (string.IsNullOrEmpty(cuentaPm.RepresentanteLegal.Persona.celular))
            {
                repLegal.celularCuenta = cuentaPm.RepresentanteLegal.Persona.telefono;
            }
            else
            {
                repLegal.celularCuenta = cuentaPm.RepresentanteLegal.Persona.celular;
            }

            repLegal.ciudadCuenta = cuentaPm.RepresentanteLegal.Domicilio.ciudad;
            repLegal.cp_cuenta = cuentaPm.RepresentanteLegal.Domicilio.codPostal;

            repLegal.coloniaIdCuenta = cuentaPm.RepresentanteLegal.Domicilio.codPostal;
            repLegal.colonia_cuenta = cuentaPm.RepresentanteLegal.Domicilio.colonia;

            repLegal.correoCuenta = cuentaPm.RepresentanteLegal.Persona.correo;
            repLegal.curpCuenta = cuentaPm.RepresentanteLegal.Persona.curp;
            repLegal.entidadNacIdCuenta = cuentaPm.RepresentanteLegal.Persona.entidadNacimiento;
            var fechaNacimiento2 = DateTime.ParseExact(cuentaPm.RepresentanteLegal.Persona.fechaNacimiento, "yyyyMMdd", CultureInfo.InvariantCulture);
            repLegal.fechaNacCuenta = fechaNacimiento2.ToString("ddMMyyyy");
            repLegal.generoCuenta = cuentaPm.RepresentanteLegal.Persona.sexo;

            repLegal.nacionalidadIdCuenta = cuentaPm.RepresentanteLegal.Persona.idNacionalidad;

            repLegal.noExteriorCuenta = cuentaPm.RepresentanteLegal.Domicilio.numExterior;
            repLegal.noInteriorCuenta = cuentaPm.RepresentanteLegal.Domicilio.numInterior;
            repLegal.nombreCuenta = cuentaPm.RepresentanteLegal.Persona.nombre;

            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Inicia consulta de base de datos ->  _ocupacionRepository.getOptionalOcupacionById");
            AspOcupacion ocupacion = await _ocupacionRepository.getOptionalOcupacionById(cuentaPm.RepresentanteLegal.Persona.idOcupacion);
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Termina consulta de base de datos ->  _ocupacionRepository.getOptionalOcupacionById");

            if (ocupacion == null)
                throw new ErrorGenerico("Ocupacion no encontrada para el ID");

            repLegal.ocupacionIdCuenta = ocupacion.ocuId;
            repLegal.ocupacion_cuenta = ocupacion.descOcupacion;

            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Inicia consulta de base de datos -> _nacionalidadRepository.findByPaisId");
            ASPNacionalidad nacionalidadRepLegal = await _nacionalidadRepository.findByPaisId(cuentaPm.RepresentanteLegal.Persona.idNacionalidad);
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Termina consulta de base de datos -> _nacionalidadRepository.findByPaisId");

            if (nacionalidadRepLegal == null)
                throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Inicia consulta de base de datos -> _paisRepository.GetById");
            var paisRepLegal = await _paisRepository.GetById(nacionalidadRepLegal.PaisId ?? default);
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "Termina consulta de base de datos -> _paisRepository.GetById");
            if (paisRepLegal == null)
                throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");
            repLegal.paisNacIdCuenta = nacionalidadRepLegal.PaisId ?? default;
            repLegal.pais_nac_cuenta = paisRepLegal.DescPais;
            repLegal.nacionalidad_cuenta = nacionalidadRepLegal.DescNacionalidad;

            repLegal.prApellidoCuenta = cuentaPm.RepresentanteLegal.Persona.apellidoPaterno;
            repLegal.rfcCuenta = cuentaPm.RepresentanteLegal.Persona.rfc;
            repLegal.serieFirmaElectCuenta = cuentaPm.RepresentanteLegal.Persona.serieFirmaElect;
            repLegal.sgApellidoCuenta = cuentaPm.RepresentanteLegal.Persona.apellidoMaterno;
            repLegal.telefonoCuenta = cuentaPm.RepresentanteLegal.Persona.telefono;

            repLegal.tipoPersonaCuenta = "F";
            repLegal.curpCuenta = cuentaPm.RepresentanteLegal.Persona.curp;

            if (cuentaPm.RepresentanteLegal.Comprobantes.Count < 1)
            {
                //Bandera Prendida
                if(campoObligatorio)
                {
                    //Pregunta si tiene Datos en el TipoIdentificacion
                    if (cuentaPm.RepresentanteLegal.Persona.tipoIdentificacionOf > 0)
                    {
                        repLegal.tipoIdentIdCuenta = cuentaPm.RepresentanteLegal.Persona.tipoIdentificacionOf;
                    }
                    //Error Si viene Vacio 
                    else
                    {
                        throw new ErrorGenerico("Tipo de identificación oficial obligatoria");
                    }
                }
                else if(cuentaPm.RepresentanteLegal.Persona.tipoIdentificacionOf != null)
                {
                    repLegal.tipoIdentIdCuenta = cuentaPm.RepresentanteLegal.Persona.tipoIdentificacionOf;
                }

               

                if (string.IsNullOrEmpty(cuentaPm.RepresentanteLegal.Persona.numIdentificacionOf))
                {
                    repLegal.numIdentCuenta = "1111111111";
                }
                else
                {
                    repLegal.numIdentCuenta = cuentaPm.RepresentanteLegal.Persona.numIdentificacionOf;
                }
            }
            else
            {
                foreach (var comp in cuentaPm.RepresentanteLegal.Comprobantes)
                {
                    if (comp.idTipoIdentificacion == 1)
                    {
                        repLegal.tipoIdentIdCuenta = comp.idTipoIdentificacion;
                        if (comp.numIdentificacion.Trim() == "")
                        {
                            repLegal.numIdentCuenta = "1111111111";
                            continue;
                        }

                        repLegal.numIdentCuenta = comp.numIdentificacion;
                        continue;
                    }

                    if (comp.idTipoIdentificacion >= 2 && comp.idTipoIdentificacion < 100)
                    {
                        repLegal.tipoIdentIdCuenta = comp.idTipoIdentificacion;
                        continue;
                    }
                    if (comp.idTipoIdentificacion >= 200 && comp.idTipoIdentificacion < 300)
                        continue;
                    if (comp.idTipoIdentificacion >= 300 && comp.idTipoIdentificacion < 400)
                        continue;
                    if (comp.idTipoIdentificacion < 1)
                    {
                        repLegal.tipoIdentIdCuenta = 1;
                        repLegal.numIdentCuenta = cuentaPm.RepresentanteLegal.Persona.curp;
                    }
                }
            }

            dtoAspCtaExpediente.repLegal = repLegal;
            GenerarLog(timestamp, "toDtoAspCtaExpediente", "FIN Generación de instancias DTO y llenado de objetos ");
            return dtoAspCtaExpediente;
        }

        private async Task<DtoAspCtaExpediente> toAspCtaPFisica(DtoCtaExpediente dtoCta, string ctaEje, int idPblu, int udnInt, string cuentaClabe)
        {

            try
            {

                Udn? udn = await _udnRepository.GetById(udnInt);
                bool campoObligatorio = _cuentaRepository.GetCamposObligatorios();
                
                if (udn == null)
                    throw new ErrorUdnNoExiste("No se encontró la udn");
                Pblu? pblu = await _pbluRepository.GetById(idPblu);
                if (pblu == null)
                    throw new ErrorIdPbluInexistente("No se encontró el pblu");

                DtoAspCtaExpediente result = new DtoAspCtaExpediente
                {
                    cuentaConcentradora = ctaEje,
                    consecutivo = null,
                    control = null,
                    correoReferencia = dtoCta.persona.correo,
                   // cuentaReferencia = GenerarClabe(pblu.PrefijoPblu ?? 0, udn.IdUdn, udn.PrefijoClabe ?? "0",""),
                    cuentaReferencia = cuentaClabe,
                    error = null,
                    fecha = DateTime.Now.ToString("ddMMyyyy"),
                    nombreReferencia = $"{dtoCta.persona.nombre} {dtoCta.persona.apellidoPaterno} {dtoCta.persona.apellidoMaterno}",
                    observaciones = null,
                    procesado = null,
                    rfcReferencia = dtoCta.persona.rfc,
                    curpReferencia = dtoCta.persona.curp,
                    telefonoReferencia = dtoCta.persona.telefono,

                    tipoCuenta = "REFERENCIADA",
                    accion = "AGREGAR",

                    repLegal = new DtoAspRepLegal
                    {
                        nombreCuenta = "Dummy",
                        ocupacionIdCuenta = dtoCta.persona.idOcupacion > 0 ? dtoCta.persona.idOcupacion : 1
                    }
                };

                DtoAspSolicitante solicitante = new DtoAspSolicitante();

                solicitante.curpCuenta = dtoCta.persona.curp;
                solicitante.callePrincipalCuenta = dtoCta.domicilio.callePrincipal;
                solicitante.calleSecundariaCuenta = string.IsNullOrEmpty(dtoCta.domicilio.calleSecundaria2) ? "Sin valor" : dtoCta.domicilio.calleSecundaria2;
                solicitante.calleSecundaria2Cuenta = string.IsNullOrEmpty(dtoCta.domicilio.calleSecundaria3) ? "Sin valor" : dtoCta.domicilio.calleSecundaria3;
                solicitante.geolocalizacionCuenta  = string.IsNullOrEmpty(dtoCta.persona.geolocalizacion) && !campoObligatorio ? "Sin Datos" : dtoCta.persona.geolocalizacion;
                Console.WriteLine("Geolocalizacion Cuenta " + solicitante.geolocalizacionCuenta);
                solicitante.serieFirmaElectCuenta = dtoCta.persona.serieFirmaElect;
                solicitante.celularCuenta = dtoCta.persona.celular;
                solicitante.ciudadCuenta = dtoCta.domicilio.ciudad;
                //solicitante.coloniaIdcuenta = "1";
                solicitante.coloniaIdCuenta = dtoCta.domicilio.codPostal;
                solicitante.cp_cuenta = dtoCta.domicilio.codPostal;
                solicitante.colonia_cuenta = dtoCta.domicilio.colonia;
                solicitante.correoCuenta = dtoCta.persona.correo;
                solicitante.denominacionCuenta = 1;
                solicitante.nivelCuenta = $"CTA_N{dtoCta.nivel_cuenta}";
                solicitante.entidadNacIdCuenta = dtoCta.persona.entidadNacimiento;
                solicitante.fechaNacCuenta = dtoCta.persona.fechaNacimiento;
                solicitante.generoCuenta = dtoCta.persona.sexo[0].ToString();
                solicitante.ingresosCuenta = dtoCta.perfil.ingresosMensuales.ToString();
                solicitante.montoMaxAhoCuenta = dtoCta.perfil.montoMax.ToString();
                solicitante.nacionalidadIdCuenta = dtoCta.persona.idNacionalidad;
                solicitante.noExteriorCuenta = dtoCta.domicilio.numExterior;
                solicitante.noInteriorCuenta = dtoCta.domicilio.numInterior;
                solicitante.nombreCuenta = dtoCta.persona.nombre;

                AspOcupacion ocupacion = await _ocupacionRepository.getOptionalOcupacionById(dtoCta.persona.idOcupacion);
                solicitante.ocupacion_cuenta = ocupacion == null ? "Sin Datos" : ocupacion.descOcupacion;
                solicitante.ocupacionIdCuenta = dtoCta.persona.idOcupacion;

                ASPNacionalidad? aspNacionalidad = await _nacionalidadRepository.findByPaisId(dtoCta.persona.idNacionalidad);
                if (aspNacionalidad == null)
                    throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

                solicitante.paisNacIdCuenta = aspNacionalidad.PaisId ?? default;

                ASPPais? pais = await _paisRepository.GetById(aspNacionalidad.PaisId ?? default);
                if (pais == null)
                    throw new ErrorNacionalidad("Nacionalidad no encontrada para el pais");

                solicitante.pais_nac_cuenta = pais.DescPais ?? string.Empty;
                solicitante.nacionalidad_cuenta = aspNacionalidad.DescNacionalidad;
                solicitante.prApellidoCuenta = dtoCta.persona.apellidoPaterno; ///Validar
                solicitante.rfcCuenta = dtoCta.persona.rfc;
                solicitante.sgApellidoCuenta = dtoCta.persona.apellidoMaterno;
                solicitante.serieFirmaElectCuenta = dtoCta.persona.serieFirmaElect;
                solicitante.telefonoCuenta = dtoCta.persona.telefono;

                solicitante.tipoPersonaCuenta = "F";
                solicitante.unidadNegocioCuenta = dtoCta.udnId;

                if (dtoCta.comprobantes.Any())
                {
                    foreach (var comp in dtoCta.comprobantes)
                    {
                        if (comp.idTipoIdentificacion == 1)
                        {
                            solicitante.tipoIdentIdCuenta = comp.idTipoIdentificacion;
                            if (comp.numIdentificacion.Trim().Equals(""))
                            {
                                if (dtoCta.persona.numIdentificacionOf != null)
                                {
                                    solicitante.numIdentCuenta = (dtoCta.persona.numIdentificacionOf.ToString()); continue;

                                }
                                solicitante.numIdentCuenta = "1111111111";
                                continue;
                            }
                            solicitante.numIdentCuenta = comp.numIdentificacion; continue;
                        }

                        if (comp.idTipoIdentificacion >= 2 && comp.idTipoIdentificacion < 100)
                        {
                            solicitante.tipoIdentIdCuenta = comp.idTipoIdentificacion; continue;

                        }
                        if (comp.idTipoIdentificacion >= 100 && comp.idTipoIdentificacion < 200)
                        {
                            continue;
                        }
                        if (comp.idTipoIdentificacion >= 200 && comp.idTipoIdentificacion < 300)
                        {
                            continue;
                        }
                        if (comp.idTipoIdentificacion < 300 || comp.idTipoIdentificacion < 400) ;
                    }
                }
                else
                {
                    solicitante.tipoIdentIdCuenta = dtoCta.persona.tipoIdentificacionOf;
                    solicitante.numIdentCuenta = dtoCta.persona.numIdentificacionOf?.ToString() ?? string.Empty;
                }

                result.solicitante = solicitante;

                return result;
            }
            catch (ErrorNacionalidad)
            {
                throw new ErrorDomicilio("No se pudo obtener los datos a partir de la nacionalidad");
            }
        }

        private bool ValidateSolicitante(DtoAspSolicitante solicitante, int nivel, string tipoPersona)
        {
            bool campoObligatorio = _cuentaRepository.GetCamposObligatorios();
            var properties = typeof(DtoAspSolicitante).GetProperties();
            foreach (var property in properties)
            {
                //Listado de las CuentasValidation que tenemos Dadas de Alta
                var attrs =
                    property.GetCustomAttributes(true)
                    .Where(x => x.GetType() == typeof(CuentaValidationAttribute))
                    .Cast<CuentaValidationAttribute>()
                    .ToList();

                // Si solo hay un atributo, se usa sin importar el campoObligatorio
                var attr = attrs.Count == 1 ? attrs.FirstOrDefault() :
                           campoObligatorio ? attrs.FirstOrDefault() : attrs.Skip(1).FirstOrDefault();

                if (attr != null)
                {
                    var attribute = attr as CuentaValidationAttribute;
                    if (attribute != null)
                    {

                        if (!attribute.GetIsObligatorioSolicitante(nivel, tipoPersona, campoObligatorio)) continue;

                        var value = property.GetValue(solicitante);

                        
                        if (value == null)
                        {
                            throw new ErrorCuentaMalformada(
                                $"El campo {property.Name} es obligatorio para este tipo de cuenta");
                        }

                        if (property.PropertyType == typeof(string) && string.IsNullOrEmpty(value.ToString().Trim()))
                        {
                            throw new ErrorCuentaMalformada(
                                $"El campo {property.Name} es obligatorio para este tipo de cuenta");
                        }
                    }
                }

                var generalAttr = property.GetCustomAttributes(true)
                    .FirstOrDefault(x => x.GetType() == typeof(DataValidationAttribute));

                

                if (generalAttr != null)
                {
                    var generalAttribute = generalAttr as DataValidationAttribute;

                   
                    if (generalAttribute != null)
                    {
                        var value = property.GetValue(solicitante);

                        

                        var result = generalAttribute.validate(value, tipoPersona, campoObligatorio);

                        if (!string.IsNullOrEmpty(result.Trim()))
                        {
                            throw new ErrorCuentaMalformada(result);
                        }
                    }
                }

            }

            return true;
        }


        #region Validacion de Documentos Digitalizados 
        /// <summary>
        /// Método para Cambiar el Tipo de Status de actividad de la cuenta, Dependiendo de la documentacion de Digitalizacion
        /// </summary>
        /// <param name="nivel_cuenta">Pasar Solamente el Nivel de Cuenta.</param>
        /// <param name="tipo_persona">Mandar un 1 Si es Persona Fisica y 2 para Persona Moral</param>
        /// <param name="IsActivePbluDigitalizacion">Mediante una consulta del Repository, determina si el participante tiene prendido su bandera de Digitalizacion.</param>
        private bool CambioStatusCuenta(int nivel_cuenta, bool IsActivePbluDigitalizacion,int tipo_persona)
        {

            //Switch para validar Tipo de Persona

            switch (tipo_persona)
            {
              
                case 1: // Persona Física
                    if (!IsActivePbluDigitalizacion) return true;
                    if (nivel_cuenta <= 2) return true;
                    return false;

                //Persona Moral
                case 2:
                    // Si la digitalización no está activa, retorna true
                    if (!IsActivePbluDigitalizacion) return true;
                    //Retorna false cuando sea verdadera.
                    return false;



            }
            //En el caso que no pasaran un dato Correcto esta pasara un false para que nazca inactiva
            return false;
        }

        #endregion



        private bool ValidateRepresentanteLegal(DtoAspRepLegal repLegal)
        {
            bool campoObligatorio = _cuentaRepository.GetCamposObligatorios();
            var properties = typeof(DtoAspRepLegal).GetProperties();
            foreach (var property in properties)
            {
                //Listado de las CuentasValidation que tenemos Dadas de Alta
                var attrs =
                    property.GetCustomAttributes(true)
                    .Where(x => x.GetType() == typeof(CuentaValidationAttribute))
                    .Cast<CuentaValidationAttribute>()
                    .ToList();

                // Si solo hay un atributo, se usa sin importar el campoObligatorio
                var attr = attrs.Count == 1 ? attrs.FirstOrDefault() :
                           campoObligatorio ? attrs.FirstOrDefault() : attrs.Skip(1).FirstOrDefault();

                if (attr != null)
                {
                    var attribute = attr as CuentaValidationAttribute;
                    if (attribute != null)
                    {
                        if ( ( !attribute.GetObligatorioRl() && !attribute.GetObligatorioCampos()) ) continue;

                        var value = property.GetValue(repLegal);
                        if (value == null)
                        {
                            throw new ErrorCuentaMalformada(
                                $"El campo {property.Name} del representante legal es obligatorio para este tipo de cuenta");
                        }

                        if (property.PropertyType == typeof(string) && string.IsNullOrEmpty(value.ToString().Trim()))
                        {
                            throw new ErrorCuentaMalformada(
                                $"El campo {property.Name} del representante legal es obligatorio para este tipo de cuenta");
                        }
                    }
                }

            }

            return true;
        }


        #endregion
        private async Task ValidarBloqueoAltaReferencia(int idPblu,bool esPersonaFisica,bool esPersonaMoral,string timestamp = "")
        {
            GenerarLog(timestamp, "ValidarBloqueoAltaReferencia(idPblu)",
                "Consulta a base de datos, columna bloquear_alta_referencia");

            var bloqueoStr = await _pbluRepository.ObtenerBloqueoAltaReferencia(idPblu);

            if (bloqueoStr == null)
                throw new ErrorUdnNoExiste("Participante no existe");

            var bloqueo = Enum.Parse<TipoBloqueoReferencia>(bloqueoStr, true);

            if (bloqueo == TipoBloqueoReferencia.AMBOS ||
               (bloqueo == TipoBloqueoReferencia.PERSONA_FISICA && esPersonaFisica) ||
               (bloqueo == TipoBloqueoReferencia.PERSONA_MORAL && esPersonaMoral))
            {
                GenerarLog(timestamp, "ValidarBloqueoAltaReferencia",
                    $"Participante pblu {idPblu} bloqueado para generación de cuenta tipo: {bloqueo}");

                throw new ErrorGenerico("Por el momento no se pudo crear la referencia, favor de intentar más tarde");
            }
        }
    }
}
