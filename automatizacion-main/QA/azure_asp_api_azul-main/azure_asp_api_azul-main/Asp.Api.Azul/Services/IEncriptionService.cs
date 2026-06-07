using Asp.Cifrado.Entities.Business;

namespace Asp.Cifrado.Services
{
	public interface IEncriptionService
	{

		Task Init();

		/// <summary>
		/// Encripta un dato de texto plano, usando un certificado
		/// </summary>
		/// <param name="dataToEncript">Dato que se encriptara</param>
		/// <param name="idPblu">Id del pblu para obtener el certificado (enviar 0 para usar el certificado de ASP)</param>
		/// <returns></returns>
		public EncriptionResult Encript(string dataToEncript, int idPblu = 0, string IDENTIFICADOR="");

		/// <summary>
		/// Desencripta una cadena de texto, usando una private key
		/// </summary>
		/// <param name="dataToDecript"></param>
		/// <param name="key"></param>
		/// <returns></returns>
		public string Decript(string dataToDecript, string key,string timestamp="");

		public bool VerificarFirma(string jsonDescifrado, string firma, int idPblu = 0, bool isPortal = false, string IDENTIFICADOR="");
		
        bool VerificarFirmaAlquimia(string jsonDescifrado, string firma, int idPblu = 0);

        string FirmarSIES(string cadena, string IDENTIFICADOR="");
    }
}
