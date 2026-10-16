package net.cero.seguridad.utilidades;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecuredPassword {
	public static String getSecurePassword(String preHash, String usuario) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(getSalt(preHash, usuario));
            byte[] bytes = md.digest(preHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
        }
        return generatedPassword;
    }

    private static byte[] getSalt(String preHash, String usuario)
            throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        String secureHash = preHash + usuario; // codigo PRE HASH + USUARIO
        try {
            sr.setSeed(secureHash.getBytes("us-ascii"));
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
