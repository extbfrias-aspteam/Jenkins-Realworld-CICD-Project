package net.cero.ahorro.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import net.cero.data.DepositoAhorroReq;
import net.cero.seguridad.utilidades.HeaderWS;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Log4j2
public class TestGeneral {
	public static void main(String[] args) {			
		log.info(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
		imprimeBodyCorreoNotificacionCallCenter("Laura Angelica Elías Troy Covarrubias","0024018700","123456","Cuenta Fácil");
		log.info(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
	}
	
	private static void imprimeBodyCorreoCuentaSimplificada() {
		String body = "";
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\">"
				+ "<head>" + "<meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\">"
				+ "<meta name=Generator content=\"Microsoft Word 12 (filtered medium)\">" + "<style>" + "<!--"
				+ " @font-face" + "	{font-family:\"Cambria Math\";" + "	panose-1:2 4 5 3 5 4 6 3 2 4;}" + "@font-face"
				+ "	{font-family:Calibri;" + "	panose-1:2 15 5 2 2 2 4 3 2 4;}" + "@font-face"
				+ "	{font-family:Tahoma;" + "	panose-1:2 11 6 4 3 5 4 4 2 4;}" + "@font-face"
				+ "	{font-family:\"Century Gothic\";" + "	panose-1:2 11 5 2 2 2 2 2 2 4;}" + "@font-face"
				+ "	{font-family:\"Ubuntu Light\";" + "	panose-1:2 11 3 4 3 6 2 3 2 4;}"
				+ " p.MsoNormal, li.MsoNormal, div.MsoNormal" + "	{margin:0cm;" + "	margin-bottom:.0001pt;"
				+ "	font-size:11.0pt;" + "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}"
				+ "a:link, span.MsoHyperlink" + "	{mso-style-priority:99;" + "	color:#0563C1;"
				+ "	text-decoration:underline;}" + "a:visited, span.MsoHyperlinkFollowed" + "	{mso-style-priority:99;"
				+ "	color:#954F72;" + "	text-decoration:underline;}" + "p.MsoAcetate, li.MsoAcetate, div.MsoAcetate"
				+ "	{mso-style-priority:99;" + "	mso-style-link:\"Texto de globo Car\";" + "	margin:0cm;"
				+ "	margin-bottom:.0001pt;" + "	font-size:8.0pt;" + "	font-family:\"Tahoma\",\"sans-serif\";"
				+ "	color:#3B547B;}" + "span.EstiloCorreo17" + "	{mso-style-type:personal-compose;"
				+ "	font-family:\"Ubuntu Light\",\"sans-serif\";" + "	color:#3B547B;}" + "span.TextodegloboCar"
				+ "	{mso-style-name:\"Texto de globo Car\";" + "	mso-style-priority:99;"
				+ "	mso-style-link:\"Texto de globo\";" + "	font-family:\"Tahoma\",\"sans-serif\";" + "	color:#3B547B;}"
				+ ".MsoChpDefault" + "	{mso-style-type:export-only;}" + "@page Section1" + "	{size:612.0pt 792.0pt;"
				+ "	margin:70.85pt 3.0cm 70.85pt 3.0cm;}" + "div.Section1" + "	{page:Section1;}" + "-->" + "</style>"
				+ "<!--[if gte mso 9]><xml>" + "<o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" />"
				+ "</xml><![endif]--><!--[if gte mso 9]><xml>" + " <o:shapelayout v:ext=\"edit\">"
				+ "  <o:idmap v:ext=\"edit\" data=\"1\" />" + " </o:shapelayout></xml><![endif]-->" + "</head>"
				+ "<body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\">" + "<div class=Section1>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "<img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br />"
				+ "</span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado(a) <b>"
				+ "AQUI VA EL NOMBRE DEL CLIENTE" + "</b>:<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "&#161;Gracias por elegir ASP Integra Opciones&#33; Te compartimos tu tarjeta de ahorro que acabas de crear con la terminaci&#243;n de n&#250;mero de cuenta: ******"
				+ "AQUI VA LA TERMINACION DE LA CUENTA" + "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Los dep&#243;sitos a tu cuenta de ahorro podr&#225;s realizarlos a trav&#233;s de SPEI, usando tu CLABE INTERBANCARIA, "
				+ "en tu sucursal m&#225;s cercana o a trav&#233;s de la red de Ventanilla F&#225;cil.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Si deseas retirar efectivo solo acude a tu sucursal m&#225;s cercana o Ventanilla F&#225;cil con tu identificaci&#243;n oficial vigente."
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "Recuerda que puedes realizar todas tus compras a trav&#233;s de CoDi sin cargo adicional. Desc&#225;rgala "
				+ "<a href=\"https://play.google.com/store/apps/details?id=com.codi.aspintegraopciones.aspcodi\" target=\"_blank\">aqu&#237;</a> y s&#250;mate a la nueva forma de pagar en M&#233;xico."
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><b><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Atentamente<o:p></o:p></span></b></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt;text-indent:0.0pt'><span lang=ESstyle='font-family:\"Century Gothic\",\"sans-serif\"'>Opciones Empresariales del Noreste, S.A. de C.V. S.F.P.<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "En ASP Integra Opciones, los datos que proporciones en la solicitud est&#225;n protegidos. Consulta el Aviso de Privacidad en "
				+ "<a href=\"https://aspintegraopciones.com/aviso-de-privacidad\" target=\"_blank\">https://aspintegraopciones.com/aviso-de-privacidad</a> "
				+ " o en tu Sucursal m&#225;s cercana." + "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"; font-size: 10px;'>"
				+ "Consulta los costos y comisiones de nuestros productos en "
				+ "<a href=\"https://aspintegraopciones.com/comisiones\" target=\"_blank\">https://aspintegraopciones.com/comisiones</a> "
				+ "<o:p></o:p></span></p>"

				+ "<p class=MsoNormal><span lang=ES><o:p>&nbsp;</o:p></span></p>"
				+ "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "<p class=MsoNormal><o:p>&nbsp;</o:p></p>" + "</div>"
				+ "</body>" + "</html>";
		
			log.info(body);
	}
	
	private static void imprimeBodyCorreoNotificacionCallCenter(String nombreCliente, String numeroCuenta, String numeroSolicitante, String producto) {
		String body = "";
		
		SimpleDateFormat formato = new SimpleDateFormat("dd 'de' MMMM 'a las' HH:mm a");
		Date fechaActual = Calendar.getInstance().getTime();
		String fecha = formato.format(fechaActual);
		log.info("Fecha :: " + fecha);
		body = "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\"><head><meta http-equiv=Content-Type content=\"text/html; charset=iso-8859-1\">"
				+ "<meta name=Generator content=\"Microsoft Word 12 (filtered medium)\"><style><!-- @font-face	{font-family:\"Cambria Math\";	panose-1:2 4 5 3 5 4 6 3 2 4;}@font-face	{font-family:Calibri;	panose-1:2 15 5 2 2 2 4 3 2 4;}@font-face	{font-family:Tahoma;	panose-1:2 11 6 4 3 5 4 4 2 4;}@font-face	{font-family:\"Century Gothic\";	"
				+ "panose-1:2 11 5 2 2 2 2 2 2 4;}@font-face	{font-family:\"Ubuntu Light\";	panose-1:2 11 3 4 3 6 2 3 2 4;} p.MsoNormal, li.MsoNormal, div.MsoNormal	{margin:0cm;	margin-bottom:.0001pt;	font-size:11.0pt;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}a:link, span.MsoHyperlink	{mso-style-priority:99;	color:#0563C1;	"
				+ "text-decoration:underline;}a:visited, span.MsoHyperlinkFollowed	{mso-style-priority:99;	color:#954F72;	text-decoration:underline;}p.MsoAcetate, li.MsoAcetate, div.MsoAcetate	{mso-style-priority:99;	mso-style-link:\"Texto de globo Car\";	margin:0cm;	margin-bottom:.0001pt;	font-size:8.0pt;	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}span.EstiloCorreo17	"
				+ "{mso-style-type:personal-compose;	font-family:\"Ubuntu Light\",\"sans-serif\";	color:#3B547B;}span.TextodegloboCar	{mso-style-name:\"Texto de globo Car\";	mso-style-priority:99;	mso-style-link:\"Texto de globo\";	font-family:\"Tahoma\",\"sans-serif\";	color:#3B547B;}.MsoChpDefault	"
				+ "{mso-style-type:export-only;}@page Section1	{size:612.0pt 792.0pt;	margin:70.85pt 3.0cm 70.85pt 3.0cm;}div.Section1	{page:Section1;}--></style><!--[if gte mso 9]><xml><o:shapedefaults v:ext=\"edit\" spidmax=\"2050\" /></xml><![endif]--><!--[if gte mso 9]>\r\n"
				+ "<xml>"
				+ "<o:shapelayout v:ext=\"edit\">  <o:idmap v:ext=\"edit\" data=\"1\" />" 
				+ "</o:shapelayout></xml><![endif]--></head><body lang=ES-MX link=\"#0563C1\" vlink=\"#954F72\"><div class=Section1><p class=MsoNormal style='margin-left:0.0pt'><span style='font-family:\"Century Gothic\",\"sans-serif\"'>"
				+ "<img alt=\"ASP Integra Opciones\"src=\"http://aspintegraopciones.com/frontend/img/logo_asp.png\" /><br /><br /></span><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'><o:p></o:p></span></p>"
				+ "<p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Estimado Ejecutivo, El d&#237;a " + fecha + " se realiz&#243; el tr&#229;mite de apertura del Cliente: " + nombreCliente + " Cuenta: " + numeroCuenta + " Producto: " + producto + "; favor de realizar la llamada de Validaci&#243;n de los datos.<o:p></o:p></span></p>"
				+ "<br /><p class=MsoNormal style='margin-left:0.0pt'><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>Favor de revisar su seguimiento en la Plataforma PROCREA y actualizar. El n&#250;mero de solicitante es " + numeroSolicitante + "<o:p></o:p></span></p>"
				+ "<p class=MsoNormal><span lang=ES style='font-family:\"Century Gothic\",\"sans-serif\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<o:p></o:p></span></p>\r\n"
				+ "	</div></body></html>";
		
			log.info(body);
	}
	
	private static void testAcentos() {
		String descripcion = "INSTALACIÓN DE HERRERÍA EN VENTANAS";
		descripcion=descripcion.replace("Ñ", "\\u00D1");
        descripcion=descripcion.replace("Á", "\\u00c1");
        descripcion=descripcion.replace("É", "\\u00C9");
        descripcion=descripcion.replace("Í", "\\u00CD");
        descripcion=descripcion.replace("Ó", "\\u00D3");
        descripcion=descripcion.replace("Ú", "\\u00DA");
        
		log.info("descripcion :: " + descripcion);
	}
}
