using System.Numerics;
using System.Reflection;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using System.Text;
using Asp.Cifrado.Entities.Business;
using Asp.Cifrado.Repositorys.CertificadoRepository;
using Asp.Cifrado.Repositorys.ParametrosApiRepository;
using Microsoft.Extensions.Configuration;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.OpenSsl;
using Org.BouncyCastle.Security;
using Org.BouncyCastle.Utilities;
using static System.Net.Mime.MediaTypeNames;

namespace Asp.Cifrado.Services;

public class EncriptionService : IEncriptionService
{
	private readonly ICertificadoRepository _certificadoRepository;
	private readonly IParametrosApiRepository _parametrosApiRepository;
    private readonly IConfiguration _configuration;

    /// <summary>
    /// 1 : IdCertificado
    /// 2 : IdPblu
    /// 3 : AsymmetricKeyParameter
    /// 4 : RSA
    /// </summary>
    private readonly List<Tuple<int, int, AsymmetricKeyParameter, RSA?>> publicKeyTuple = new List<Tuple<int, int, AsymmetricKeyParameter, RSA?>>();
	private readonly List<Tuple<int, int, AsymmetricKeyParameter, RSA?>> publicKeyTuplePortal = new List<Tuple<int, int, AsymmetricKeyParameter, RSA?>>();
    private RsaPrivateCrtKeyParameters? privateKey;
    private RsaPrivateCrtKeyParameters? privateKeySIES;
    private  bool hasExecuted = false;

    public EncriptionService(ICertificadoRepository certificadoRepository, IParametrosApiRepository parametrosApiRepository, IConfiguration configuration)
	{
		_certificadoRepository = certificadoRepository;
		_parametrosApiRepository = parametrosApiRepository;
        _configuration = configuration;


    }

	public async Task Init()
	{
        if (hasExecuted)
        {
            return;
        }
        try
		{
			var certificados = await _certificadoRepository.GetAll();
            var entryAssembly = Assembly.GetEntryAssembly();
            var path = entryAssembly != null ? Path.GetDirectoryName(entryAssembly.Location) : string.Empty;

            foreach (var certificado in certificados)
			{
				try
				{
                    if (!string.IsNullOrEmpty(certificado.RutaPortal))
                    {

                        var ruta = Path.Combine(path ?? string.Empty, certificado.RutaPortal ?? string.Empty);



                        PemReader PrPublic = new PemReader(File.OpenText(ruta));
                        Org.BouncyCastle.X509.X509Certificate Keys = (Org.BouncyCastle.X509.X509Certificate)PrPublic.ReadObject();
                        var _publicKey = Keys.GetPublicKey();

                        var _x509 = new X509Certificate2(ruta);
                        var _pubK = _x509.GetRSAPublicKey();

                        publicKeyTuplePortal.Add(new Tuple<int, int, AsymmetricKeyParameter, RSA?>(certificado.IdCertificado, certificado.IdPblu, _publicKey, _pubK));
                        Console.WriteLine($"certificado.Ruta_portal no es NULL y vale {certificado.RutaPortal}");
                        Console.WriteLine($"pblu= {certificado.IdPblu}");

                    }

                    var certificadoRuta = Path.Combine(path?? string.Empty, certificado.Ruta);
            
                    PemReader prPublic = new PemReader(File.OpenText(certificadoRuta));
					Org.BouncyCastle.X509.X509Certificate keys = (Org.BouncyCastle.X509.X509Certificate)prPublic.ReadObject();
					var publicKey = keys.GetPublicKey();

                    var x509 = new X509Certificate2(certificadoRuta);
                    var pubK = x509.GetRSAPublicKey();

                    publicKeyTuple.Add(new Tuple<int, int, AsymmetricKeyParameter, RSA?>(certificado.IdCertificado, certificado.IdPblu, publicKey, pubK));
                }
				catch (Exception e)
				{
					
                    Console.WriteLine($"No se pudo leer el certificado {certificado.Ruta}");
                    Console.WriteLine(e.Message);
                }
			}
            var  parametro = await _parametrosApiRepository.GetById(1);
            var privateKeyRutaParcial = parametro.Valor;

            var parametro2 = await _parametrosApiRepository.GetById(2);
            var fraseLlave = parametro2.Valor;
            var privateKeyRuta = Path.Combine(path??string.Empty, privateKeyRutaParcial);
         

            var privateKeyContent = File.ReadAllText(privateKeyRuta);
			PemReader pr = new PemReader(new StringReader(privateKeyContent), new PasswordFinder(fraseLlave));

			RsaPrivateCrtKeyParameters KeyPair = (RsaPrivateCrtKeyParameters)pr.ReadObject();

			privateKey = KeyPair;



            //Leemos la llave privada para la firma de SIES

            string privateKeyRutaParcialSIES = _configuration.GetValue<string>("SIES:PrivateKey")??"";
            string pathPhrase = _configuration.GetValue<string>("SIES:Phrase")??"";
          
            var privateKeySIESRuta = Path.Combine(path ?? string.Empty, privateKeyRutaParcialSIES);

            var privateKeySIESContent = File.ReadAllText(privateKeySIESRuta);
            var passwordSIES = File.ReadAllText(pathPhrase??"");


            PemReader prSIES = new PemReader(new StringReader(privateKeySIESContent), new PasswordFinder(passwordSIES));

            RsaPrivateCrtKeyParameters KeyPairSIES = (RsaPrivateCrtKeyParameters)prSIES.ReadObject();

            privateKeySIES = KeyPairSIES;
            hasExecuted = true;

        }
        catch (Exception e)
		{
			
		}
	}

	/// <summary>
	/// Encripta un dato de texto plano, usando un certificado
	/// </summary>
	/// <param name="dataToEncript">Dato que se encriptara</param>
	/// <param name="idPblu">Id del pblu para obtener el certificado (enviar 0 para usar el certificado de ASP)</param>
	/// <returns></returns>
	public EncriptionResult Encript(string dataToEncript, int idPblu = 0, string IDENTIFICADOR="")
	{
		
		var publicKey = publicKeyTuple.FirstOrDefault(x => (idPblu == 0 && x.Item1 == 4) || (idPblu != 0 && x.Item2 == idPblu));

        if (publicKey == null) throw new InvalidOperationException("No se encontró el certificado público.");

        var keyAes = GenerateKey(128);

        var firma = Firmar(dataToEncript);
        var key = RsaEncrypt(keyAes, publicKey.Item3);
        var jsonCifrado = AesEncrypt(dataToEncript, keyAes);
        DateTime currentTime = DateTime.Now;
        string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.fff");
        Console.WriteLine($"{current_time_formatt} INFO [Encript - {IDENTIFICADOR}] -> FIN del descifrado");
        return new EncriptionResult
		{
			Firma = firma,
			JsonCifrado = jsonCifrado,
			Key = key
		};
	}

	/// <summary>
	/// Desencripta una cadena de texto, usando una private key
	/// </summary>
	/// <param name="dataToDecript"></param>
	/// <param name="key"></param>
	/// <returns></returns>
	public string Decript(string dataToDecript, string key, string timestamp="")
	{
		dataToDecript = RepairBase64String(dataToDecript);
		key = RepairBase64String(key);
        IBufferedCipher cipherRSA = CipherUtilities.GetCipher("RSA/ECB/PKCS1Padding");
        cipherRSA.Init(false, privateKey);
        byte[] msgDecrypt = cipherRSA.DoFinal(Convert.FromBase64String(key));

        if (msgDecrypt.Length != 16)
        {
            msgDecrypt = Arrays.CopyOf(msgDecrypt, 16);
        }

        IBufferedCipher cipher = CipherUtilities.GetCipher("AES/ECB/PKCS5Padding");
        cipher.Init(false, new KeyParameter(msgDecrypt));
        byte[] bPago = cipher.DoFinal(Convert.FromBase64String(dataToDecript));
        var pago = Encoding.UTF8.GetString(bPago);
        //print
        DateTime currentTime = DateTime.Now;
        string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.fff");
        Console.WriteLine($"{current_time_formatt} INFO [Decript - {timestamp}] -> FIN del descifrado");
        return pago; 
	}

    public bool VerificarFirma(string jsonDescifrado, string firma, int idPblu = 0, bool isPortal = false, string IDENTIFICADOR = "")
    {
        Tuple<int, int, AsymmetricKeyParameter, RSA?>? publicKey = null;
        bool firmaValida = false;
        byte[]? signatureBytes = null;
        var selectedPublicKeyList = isPortal ? publicKeyTuplePortal : publicKeyTuple;

        publicKey = selectedPublicKeyList.FirstOrDefault(x => (idPblu == 0 && x.Item1 == 4) || (idPblu != 0 && x.Item2 == idPblu));

        if (publicKey == null || selectedPublicKeyList.Count == 0)
        {
            publicKey = publicKeyTuple.FirstOrDefault(x => (idPblu == 0 && x.Item1 == 4) || (idPblu != 0 && x.Item2 == idPblu));
        }
        if (publicKey!= null && publicKey.Item4 != null)
        {

            signatureBytes = Convert.FromBase64String(firma);
            firmaValida = publicKey.Item4.VerifyData(Encoding.UTF8.GetBytes(jsonDescifrado), signatureBytes, HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1);
            DateTime currentTime = DateTime.Now;
            string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.fff");
            Console.WriteLine($"{current_time_formatt} INFO [VerificarFirma - {IDENTIFICADOR}] -> FIN del descifrado");
            return firmaValida;
        }

        throw new InvalidOperationException("No se encontró el certificado público.");

    }

    public bool VerificarFirmaAlquimia(string jsonDescifrado, string firma, int idPblu = 0)
    {
        var firmaBytes = Convert.FromBase64String(firma);
        var firmaDecode = Encoding.UTF8.GetString(firmaBytes);
        var firmaValida = jsonDescifrado == firmaDecode;

        return firmaValida;
    }

    private static byte[] GenerateKey(int size)
    {
        using (Aes aesAlgorithm = Aes.Create())
        {
            aesAlgorithm.KeySize = size;
            aesAlgorithm.GenerateKey();
            return aesAlgorithm.Key;
        }
    }

    private string Firmar(string json)
    {
        RSAParameters rsaParams = DotNetUtilities.ToRSAParameters(privateKey);
        using RSA csp = RSA.Create(2048);
        csp.ImportParameters(rsaParams);
        byte[] data = csp.SignData(Encoding.UTF8.GetBytes(json), HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1);
        return Convert.ToBase64String(data);
    }

    public string FirmarSIES(string cadena, string IDENTIFICADOR = "")
    {
        RSAParameters rsaParams = DotNetUtilities.ToRSAParameters(privateKeySIES);

        RSACryptoServiceProvider csp = new RSACryptoServiceProvider();
        csp.ImportParameters(rsaParams);
        byte[] data = csp.SignData(Encoding.UTF8.GetBytes(cadena), SHA512.Create());
        //print
        DateTime currentTime = DateTime.Now;
        string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.fff");
        Console.WriteLine($"{current_time_formatt} INFO [FirmarSIES - {IDENTIFICADOR}] -> FIN del descifrado");
        return Convert.ToBase64String(data);
    }
   /* public string FirmarSIES(string cadena, string IDENTIFICADOR = "")
    {
        RSAParameters rsaParams = DotNetUtilities.ToRSAParameters(privateKeySIES);

       // using RSA csp = RSA.Create(2048);
        using RSA csp = RSA.Create(4096);
        csp.ImportParameters(rsaParams);
   
        byte[] data = csp.SignData(Encoding.UTF8.GetBytes(cadena), HashAlgorithmName.SHA256, RSASignaturePadding.Pkcs1);
      
        DateTime currentTime = DateTime.Now;
        string current_time_formatt = currentTime.ToString("dd/MM/yyyy hh:mm:ss.fff");
        Console.WriteLine($"{current_time_formatt} INFO [FirmarSIES - {IDENTIFICADOR}] -> FIN del descifrado");
        return Convert.ToBase64String(data);
    }*/

    private static string RsaEncrypt(byte[] aes, AsymmetricKeyParameter publicKey)
    {
        IBufferedCipher cipher = CipherUtilities.GetCipher("RSA/ECB/PKCS1Padding");
        cipher.Init(true, publicKey);
        var aesCifrada = cipher.DoFinal(aes);
        return Convert.ToBase64String(aesCifrada);
    }

    private static string AesEncrypt(string json, byte[] keyAes)
    {
        IBufferedCipher cipher = CipherUtilities.GetCipher("AES/ECB/PKCS5Padding");
        cipher.Init(true, new KeyParameter(keyAes));
        var jsonCifrado = cipher.DoFinal(Encoding.UTF8.GetBytes(json));
        return Convert.ToBase64String(jsonCifrado);
    }


    private static string RepairBase64String(string base64String)
    {
        if (base64String.Contains('\\')) 
        {
            base64String = base64String.Replace("\\", ""); 
        }

        return base64String;
    }
}

public class PasswordFinder : IPasswordFinder
{
    private readonly string password;

    public PasswordFinder(string password)
    {
        this.password = password;
    }


    public char[] GetPassword()
    {
        return password.ToCharArray();
    }
}