using Asp.Api.Azul.Business.Logs;
using Asp.Api.Azul.Entities.DataBase;
using Asp.Api.Azul.Models.Entities;
using Asp.Api.Azul.Repositorys.HistorialSaldoPbluRepository;
using Asp.Api.Azul.Repositorys.PbluRepository;
using Asp.Api.Azul.Repositorys.UdnRepository;
using Asp.Api.Azul.Repositorys.ViewConciliacionHistRepository;

namespace Asp.Api.Azul.Business.Saldo
{
	public class SaldoBusiness : ISaldoBusiness
	{

		private readonly IViewConciliacionHistRepository _viewConciliacionHistRepository;
		private readonly IUdnRepository _udnRepository;
		private readonly IPbluRepository _pbluRepository;
		private readonly IHistorialSaldoPbluRepository _historialSaldoPbluRepository;
		private readonly ILogsBusiness _logsBusiness;

		public SaldoBusiness(IViewConciliacionHistRepository viewConciliacionHistRepository, IUdnRepository udnRepository, IPbluRepository pbluRepository, IHistorialSaldoPbluRepository historialSaldoPbluRepository, ILogsBusiness logsBusiness)
		{
			_viewConciliacionHistRepository = viewConciliacionHistRepository;
			_udnRepository = udnRepository;
			_pbluRepository = pbluRepository;
			_historialSaldoPbluRepository = historialSaldoPbluRepository;
			_logsBusiness = logsBusiness;
		}
        
    public async Task<string> GetSaldoUdnByUdnEs(string udn)
    {
        var saldo = await _viewConciliacionHistRepository.GetByUdn(udn);
        if (saldo == null)
            throw new ErrorUdnNoAsociada($"No se encontro resultados para la udn {udn}");

        return saldo;
    }
		public async Task<bool> VerificarUdnByPblu(int id_pblu, int id_udn)
		{
			try
			{
				return await _udnRepository.VerificaUdnByIdPblu(id_pblu, id_udn);
			}
			catch (Exception ex)
			{
                Console.WriteLine($"Ocurrio un error al consultar la existencia de la UDN {id_udn} con el pblu {id_pblu}");
				throw;
			}
		}
        public async Task<string> GetSaldoUdnByClabe(string clabe)
        {
            /*var udn = _udnRepository.GetByClabe(clabe);
            if (udn == null)
                throw new ErrorUdnNoExiste($"No se encontró la udn con clabe: {clabe}");*/
            var saldo = await _viewConciliacionHistRepository.GetBy(clabe);
            if (saldo == null)
                throw new ErrorUdnNoAsociada($"No se encontro resultados para la clabe {clabe}");

            return saldo;
        }
        public async Task<ConciliacionUdnPblu> GetSaldoDiaActual(int idPblu)
		{
			
			ConciliacionUdnPblu result = new ConciliacionUdnPblu();
			List<UdnSaldo> listUdnSaldo = new List<UdnSaldo>();
			List<ViewConciliacionHist> listUdn = await _viewConciliacionHistRepository.GetBy(idPblu);
			decimal saldoActual = 0;
			SaldoControl abonos = new SaldoControl();
			SaldoControl cargos = new SaldoControl();
			foreach (var conciliacionUdn in listUdn)
			{
				UdnSaldo udnSaldo = new UdnSaldo
				{
					IdUdn = conciliacionUdn.IdUdn ?? 0,
					Abonos = new SaldoControl
					{
						Cant = conciliacionUdn.TotalAbonosMonto ?? 0L,
						Monto = conciliacionUdn.MontoAbono ?? 0m
					},
					Cargos = new SaldoControl
					{
						Cant = conciliacionUdn.TotalCargosMonto ?? 0L,
						Monto = conciliacionUdn.MontoCargo ?? 0m
					},
					SaldoTotal = conciliacionUdn.Saldo ?? 0m,
					SaldoInicial = conciliacionUdn.SaldoInicial ?? 0m,
					Descripcion = conciliacionUdn.Descripcion ?? string.Empty
				};

				saldoActual += conciliacionUdn.Saldo ?? 0m;

				abonos.Cant += conciliacionUdn.TotalAbonosMonto ?? 0L;
				abonos.Monto += conciliacionUdn.MontoAbono ?? 0m;

				cargos.Cant += conciliacionUdn.TotalCargosMonto ?? 0L;
				cargos.Monto += conciliacionUdn.MontoCargo ?? 0m;
				listUdnSaldo.Add(udnSaldo);
			}

			result.SaldoActual = saldoActual;
			result.Abonos = abonos;
			result.Cargos = cargos;
			result.UdnList = listUdnSaldo;

			return result;
		}

		public async Task<DtoSaldoUdn> GetSaldoByClabe(int idPblu, string clabe)
		{
			var udn = await _udnRepository.GetByClabe(clabe);
			if (udn == null)
				throw new ErrorUdnNoExiste($"No se encontró la udn con clabe: {clabe}");
			var saldo = await _viewConciliacionHistRepository.GetBy(idPblu, udn.IdUdn);
			if (saldo == null)
				throw new ErrorUdnNoAsociada($"No se encontraron datos de conciliacion para la clabe {clabe}");
			var result = new DtoSaldoUdn
			{
				FechaOperacion = saldo.FechaOperacion ?? DateTime.Now,
				IdUdn = saldo.IdUdn ?? 0,
				MontoAbono = saldo.MontoAbono ?? 0m,
				MontoCargo = saldo.MontoCargo ?? 0m,
				Saldo = saldo.Saldo ?? 0m,
				SaldoInicial = saldo.SaldoInicial ?? 0m,
				TotalAbonosMonto = saldo.TotalAbonosMonto ?? 0L,
				TotalCargosMonto = saldo.TotalCargosMonto ?? 0L
			};
			return result;
		}

		public async Task InicioDia()
		{
			var fechaOperativa = DateTime.Today;
            if (fechaOperativa.DayOfWeek == DayOfWeek.Saturday || fechaOperativa.DayOfWeek == DayOfWeek.Sunday) return;
            try
			{
				var allPblus = await _pbluRepository.GetAll();
				foreach (var pblu in allPblus)
				{
					var udns = await _udnRepository.GetByIdPblu(pblu.IdPblu);
					foreach (var udn in udns)
					{
						try
						{
							var lastHistorial = await _historialSaldoPbluRepository.GetByIdUdnAndLastFechaOperativa(udn.IdUdn);

							var newHistorial = new HistorialSaldoPblu
							{
								IdUdn = udn.IdUdn,
								FechaOperativa = fechaOperativa,
								SaldoInicial = lastHistorial?.SaldoFinal ?? 0m,
								SaldoFinal = 0m,
								FechaCreacion = DateTime.Now
							};
							await _historialSaldoPbluRepository.Insert(newHistorial);
						}
						catch (Exception e)
						{
							var logError = await _logsBusiness.RegistraError(e, udn.Pblu, LogLevel.Error, "InicioDia");
						}
					}
				}
			}
			catch (Exception e)
			{
				var logError = await _logsBusiness.RegistraError(e, 0, LogLevel.Error, "InicioDia");
			}
		}

		public async Task CierreDia(bool anterior = false)
		{
			var fechaOperacion = DateTime.Today;
            if (anterior) fechaOperacion = fechaOperacion.AddDays(-1);
            if (fechaOperacion.DayOfWeek == DayOfWeek.Saturday || fechaOperacion.DayOfWeek == DayOfWeek.Sunday) return;
            try
			{
				var allPblus = await _pbluRepository.GetAll();
				foreach (var pblu in allPblus)
				{
					var udns = await _udnRepository.GetByIdPblu(pblu.IdPblu);
					foreach (var udn in udns)
					{
						try
						{
							var saldoActual = await _viewConciliacionHistRepository.GetByUdnFecha(udn.IdUdn, fechaOperacion);


							if (await _historialSaldoPbluRepository.ExisteByIdUdnAndFechaOperativa(udn.IdUdn, fechaOperacion))
							{
								await _historialSaldoPbluRepository.UpdateSaldoFinal(udn.IdUdn, fechaOperacion,
									saldoActual?.Saldo ?? 0m);
							}
							else
							{
								throw new ErrorCierreDiaSinInicio("Saldo inicial no encontrado");
							}							
						}
						catch (ErrorGenerico eg) when (eg is ErrorCierreDiaSinInicio)
						{
							var logError = await _logsBusiness.RegistraErrorAzul(eg, udn.Pblu, LogLevel.Error, "Cierre Dia UDN "+udn.IdUdn);
						}
						catch (Exception e)
						{
							var logError = await _logsBusiness.RegistraError(e, udn.Pblu, LogLevel.Error, "Cierre Dia UDN " + udn.IdUdn);
						}
					}
				}
			}
			catch (Exception e)
			{
				var logError = await _logsBusiness.RegistraError(e, 0, LogLevel.Error, "Cierre Dia");
			}
		}


        public async Task CierreDia(DateTime fecha)
        {
            if (fecha.DayOfWeek == DayOfWeek.Saturday || fecha.DayOfWeek == DayOfWeek.Sunday) return;
            var fechaOperacion = fecha;
            try
            {
                var allPblus = await _pbluRepository.GetAll();
                //allPblus = allPblus.Where(x => x.IdPblu > 13 && x.IdPblu != 19 && x.IdPblu != 20 && x.IdPblu != 21 && x.IdPblu != 22).ToList();
                foreach (var pblu in allPblus)
                {
                    var udns = await _udnRepository.GetByIdPblu(pblu.IdPblu);
                    foreach (var udn in udns)
                    {
                        try
                        {
                            var saldoActual = await _viewConciliacionHistRepository.GetByUdnFecha(udn.IdUdn, fechaOperacion);


                            if (await _historialSaldoPbluRepository.ExisteByIdUdnAndFechaOperativa(udn.IdUdn, fechaOperacion))
                            {
                                await _historialSaldoPbluRepository.UpdateSaldoFinal(udn.IdUdn, fechaOperacion,
                                    saldoActual?.Saldo ?? 0m);
								continue;
                            }

                            throw new ErrorCierreDiaSinInicio("Saldo inicial no encontrado");
                        }
                        catch (ErrorGenerico eg) when (eg is ErrorCierreDiaSinInicio)
                        {
                            var logError = await _logsBusiness.RegistraErrorAzul(eg, udn.Pblu, LogLevel.Error, "Cierre Dia UDN " + udn.IdUdn);
                        }
                        catch (Exception e)
                        {
                            var logError = await _logsBusiness.RegistraError(e, udn.Pblu, LogLevel.Error, "Cierre Dia UDN " + udn.IdUdn);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                var logError = await _logsBusiness.RegistraError(e, 0, LogLevel.Error, "Cierre Dia");
            }
        }


        public async Task InicioDia(DateTime fecha)
        {
            if (fecha.DayOfWeek == DayOfWeek.Saturday || fecha.DayOfWeek == DayOfWeek.Sunday) return;
            var fechaOperativa = fecha;
            try
            {
                var allPblus = await _pbluRepository.GetAll();
                //allPblus = allPblus.Where(x => x.IdPblu > 13 && x.IdPblu != 19 && x.IdPblu != 20 && x.IdPblu != 21 && x.IdPblu != 22).ToList();
                foreach (var pblu in allPblus)
                {
                    var udns = await _udnRepository.GetByIdPblu(pblu.IdPblu);
                    foreach (var udn in udns)
                    {
                        try
                        {
                            if (await _historialSaldoPbluRepository.ExisteByIdUdnAndFechaOperativa(udn.IdUdn, fechaOperativa))
                            {
								continue;
                            }


                            var lastHistorial = await _historialSaldoPbluRepository.GetByIdUdnAndLastFechaOperativa(udn.IdUdn);
                      
                            var newHistorial = new HistorialSaldoPblu
                            {
                                IdUdn = udn.IdUdn,
                                FechaOperativa = fechaOperativa,
                                SaldoInicial = lastHistorial?.SaldoFinal ?? 0m,
                                SaldoFinal = 0m,
                                FechaCreacion = DateTime.Now
                            };
                           await _historialSaldoPbluRepository.Insert(newHistorial);
                        }
                        catch (Exception e)
                        {
                            var logError = await _logsBusiness.RegistraError(e, udn.Pblu, LogLevel.Error, "InicioDia");
                        }
                    }
                }
            }
            catch (Exception e)
            {
                var logError = await _logsBusiness.RegistraError(e, 0, LogLevel.Error, "InicioDia");
            }
        }
    }
}
