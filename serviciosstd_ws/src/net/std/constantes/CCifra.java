package net.std.constantes;

import java.nio.charset.Charset;

public class CCifra 
{
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	public static String limpiaCaracteres(String src)
	{
		StringBuilder sb = new StringBuilder();
		int ascii[]= {162,164,165,167,169,170,171,172,174,176,177,178,179,180,181,182,184,185,186,187,188,189,190,198,208,216,222,223,230,231,248,254};
		int ascci_inv[]= {192,193,194,195,196,197, 200,201,202,203, 204,205,206,207, 209, 210,211,212,213,214, 217,218,219,220, 224,225,226,227,228,229, 232,233,234,235, 236,237,238,239, 241, 242,243,244,245,246, 249,250,251,252, 253,255  };
		int ascci_val[]= {65,65,65,65,65,65, 69,69,69,69, 73,73,73,73, 78, 79,79,79,79,79, 85,85,85,85, 97,97,97,97,97,97, 101,101,101,101, 105,105,105,105, 110, 111,111,111,111,111, 117,117,117,117, 121, 121 };
		String original;
		byte[] ptext;
		int ascval;

		original= src;
		try {

			for (int i = 0; i < original.length(); i++) {
				ascval= (int) original.charAt(i);
				if (ascval > 256) {
					sb.append((char)38);   //equivale &				
				}
				else {
					sb.append((char)ascval); //equivale a:					
				}
			}
			original= sb.toString();

			for (int i = 0; i < ascii.length; i++) {
				original= original.replace((char)ascii[i], (char)32);
			}  
			for (int i = 0; i < ascci_inv.length; i++) {
				original= original.replace((char)ascci_inv[i], (char)ascci_val[i]);			
			}
			ptext= original.getBytes(Charset.forName("US-ASCII"));
			original= new String(ptext, Charset.forName("UTF-8"));

		} 
		catch (Exception e) {
			original= src;
		}
		return original;
	}
	
	/**
	 * 
	 * @param byEntrada
	 * @return
	 */
	public static short BCD4toShort(byte[] byEntrada)
	{
		byte a,b;		
		short c;
		a= (byte)((byEntrada[1] & 0xFF) << 4);
		b= (byte)(byEntrada[0] & 0xFF);
		c= (short)(b | a);
		return c;
	}
	
	/**
	 * 
	 * @param val
	 * @return
	 */
	public static byte[] shortToArrayBCD4(short val)
	{
		byte byShort[] = new byte[2];

		byShort[1]= (byte)((val >> 4) & 0xFF);
		byShort[0]= (byte)(val & 0xFF);
		return byShort;
	}
	
	/**
	 * 
	 * @param digest
	 * @return
	 */
	public static String toHexadecimal(byte[] digest) {
		String hash = "";
		for(byte aux : digest) {
			int b = aux & 0xff; // Hace un cast del byte a hexadecimal
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

	/**
	 * 
	 * @param entrada
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String encodeBCDtoStr (String entrada)	
	{
		StringBuilder encode= new StringBuilder();
		byte byShort[] = new byte[2];
		String stringBuilder_Retorno;
		String a;
		String b;
		char ch;
		short c;

		try {
			for (int i = 0; i < entrada.length(); i++) {
				ch= (char) entrada.charAt(i);
				c= (short)ch;
				byShort= shortToArrayBCD4(c);
				a= toHexadecimal(byShort);
				encode.append(a);
			}
			stringBuilder_Retorno= encode.toString() + "=";
		} catch (Exception e) {
			stringBuilder_Retorno= null;
		}
		return stringBuilder_Retorno;
	}
	
	
	/**
	 * Decodifica el mensaje codificado >> 4,0
	 * @param encoded
	 * @return
	 */
	public static String decodeBCDtoStr(String data)
	{
		StringBuilder sbl;
		StringBuilder sbh;
		StringBuilder decode;
		String strRetorno;
		char buf[] = new char[4];
		byte byShort[] = new byte[2];
		short chr;    	

		
		if (data== null || "".equals(data)) {
			return data;
		}
		
		strRetorno= "";
		data= data.replace("=", "");
		if ((data.length() % 4)!= 0) {
			return strRetorno;
		}

		decode= new StringBuilder();
		for (int i = 0 ; i < data.length() ; i+=4) {
			sbl = new StringBuilder();
			sbh = new StringBuilder();
			data.getChars(i, i+4, buf, 0);
			sbl.append(buf[0]).append(buf[1]);
			sbh.append(buf[2]).append(buf[3]);
			byShort[0]= (byte)Integer.parseInt(sbl.toString(), 16);  // hex to int
			byShort[1]= (byte)Integer.parseInt(sbh.toString(), 16); 
			chr= BCD4toShort(byShort);			
			decode.append((char)chr);
			strRetorno= decode.toString();
		}
		strRetorno= limpiaCaracteres(strRetorno);
		return strRetorno;
	}
}
