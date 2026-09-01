package net.cero.seguridad.utilidades;

import lombok.extern.log4j.Log4j2;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

@Log4j2
public class Encrypted {

	public static void main(String[] args){
		//getSecurePassword("071a4400", "0024374400")
		
		generaPasswordNuevoCoDi();
		

	}
	
	private static void generaPasswordNuevoCoDi() {
		String folio = "";
		String cuenta = "";
		String passwordPlano = "";
		String password = "";
		
		
		//////////////////////////////////////////
		cuenta = "0020014100";
		folio = "26bb86bdf9";
		
		passwordPlano = folio.substring(folio.length()-4);
		passwordPlano = passwordPlano + cuenta.substring(cuenta.length()-4);
		
		password = getSecurePassword(passwordPlano, cuenta);
		
		log.info("Cuenta: " + cuenta);
		log.info("Password: " + password);
		log.info("Pass: " + passwordPlano);
		
		////////////////////////////////////////////////////////////////////
		cuenta = "0024376300";
		folio = "26bb86d12a";
		
		passwordPlano = folio.substring(folio.length()-4);
		passwordPlano = passwordPlano + cuenta.substring(cuenta.length()-4);
		
		password = getSecurePassword(passwordPlano, cuenta);
		
		log.info("Cuenta: " + cuenta);
		log.info("Password: " + password);
		log.info("Pass: " + passwordPlano);
		
		////////////////////////////////////////////////////////////////////
		cuenta = "0024376200";
		folio = "26bb8b468e";

		passwordPlano = folio.substring(folio.length()-4);
		passwordPlano = passwordPlano + cuenta.substring(cuenta.length()-4);

		password = getSecurePassword(passwordPlano, cuenta);

		log.info("Cuenta: " + cuenta);
		log.info("Password: " + password);
		log.info("Pass: " + passwordPlano);

		////////////////////////////////////////////////////////////////////
		cuenta = "0024376100";
		folio = "26bb86e9fe";

		passwordPlano = folio.substring(folio.length()-4);
		passwordPlano = passwordPlano + cuenta.substring(cuenta.length()-4);

		password = getSecurePassword(passwordPlano, cuenta);

		log.info("Cuenta: " + cuenta);
		log.info("Password: " + password);
		log.info("Pass: " + passwordPlano);

		////////////////////////////////////////////////////////////////////
		cuenta = "0024374700";
		folio = "26bb86f4d6";

		passwordPlano = folio.substring(folio.length()-4);
		passwordPlano = passwordPlano + cuenta.substring(cuenta.length()-4);

		password = getSecurePassword(passwordPlano, cuenta);

		log.info("Cuenta: " + cuenta);
		log.info("Password: " + password);
		log.info("Pass: " + passwordPlano);

		////////////////////////////////////////////////////////////////////
		cuenta = "0024375900";
		folio = "26bb870212";

		passwordPlano = folio.substring(folio.length()-4);
		passwordPlano = passwordPlano + cuenta.substring(cuenta.length()-4);

		password = getSecurePassword(passwordPlano, cuenta);

		log.info("Cuenta: " + cuenta);
		log.info("Password: " + password);
		log.info("Pass: " + passwordPlano);
	}
	
	public static String getSecurePassword(String pswd, String cta)
    {
        String generatedPassword = null;
        try {
            // Crear instancia SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //agregarle la "Salt"
            md.update(getSalt(pswd, cta));
            //Obtener el hash del password
            byte[] bytes = md.digest(pswd.getBytes());
            //Pasar arreglo de Bytes a hexadecimal
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Obtener el hash completo y formateado
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e){
        	e.printStackTrace();
        }
        return generatedPassword;
    }


    private static byte[] getSalt(String preHash, String usuario) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        //Secure Random, garantiza que ser� un n�mero totalmente al azar
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        String secureHash= preHash + usuario; 
        try {
			sr.setSeed(secureHash.getBytes("us-ascii"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //crea un byte[] para la salt
        byte[] salt = new byte[16];
        //de las multiples posibilidades que genero agarramos el byte proximo.
        sr.nextBytes(secureHash.getBytes());
        //return salt
        return salt;
    }
}

