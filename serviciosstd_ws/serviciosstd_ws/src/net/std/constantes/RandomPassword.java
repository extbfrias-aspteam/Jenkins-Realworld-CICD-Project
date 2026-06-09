package net.std.constantes;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.spring.config.Apps;

@SuppressWarnings("unused")
public class RandomPassword implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(RandomPassword.class);

	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC = "0123456789";
	private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
	private static final String TIME_CHARS = new SimpleDateFormat("yyMMddhhmmssMs").format(Calendar.getInstance().getTime());

	private static final SecureRandom random = new SecureRandom();
	private static final char[] dic = (TIME_CHARS + ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL_CHARS ).toCharArray();

	private static final String _FORMATO_FECHA_ = "yyyy-MM-dd";

	private static Apps apps = null;

	public static String generatePassword(int len) {
		String pass = null;
		try{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i++) {
				sb.append(dic[random.nextInt(dic.length)]);
			}
			pass = sb.toString();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return pass;
	}
}

