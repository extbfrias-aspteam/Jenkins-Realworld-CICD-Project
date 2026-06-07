using Asp.Cifrado.Entities.DataBase;

namespace Asp.Cifrado.Repositorys.CertificadoRepository
{
	public interface ICertificadoRepository
	{
		Task<List<Certificado>> GetAll();
	}
}
