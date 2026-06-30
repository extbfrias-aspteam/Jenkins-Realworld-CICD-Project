package functions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import functions.dto.ResponseTokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class FunctionValidaToken implements HttpFunction {

	private static final Logger log = LogManager.getLogger(FunctionValidaToken.class);
	private ObjectMapper objetMapper = new ObjectMapper();
	private static final String HEADER = "Authorization";
	private static final String PREFIX = "Bearer ";
	private static final String SECRET = System.getenv("SECRET_KEY");//"8c67879a0fdfbdbc4182bde93a91cd3f9e4c39b61889fe65de718038d34b9a46";

	@Override
	public void service(final HttpRequest request, final HttpResponse response)
			throws IOException, GeneralSecurityException {
		log.info("parametros recibidos son: " + request.getReader());
		// verificar documento en caso de ser ine validar
		ResponseTokenDTO validacion = new ResponseTokenDTO(false, "", null);
		try {
			Claims infoINE = validateToken(request);
			System.out.println("A " + infoINE.getAudience());
			System.out.println("C " + infoINE.getIssuer());
			System.out.println("D " + infoINE.getExpiration());
			validacion = new ResponseTokenDTO(true, "Token valido", obtenerIdPblu(infoINE));
		}catch(SignatureException se) {
			se.printStackTrace();
			validacion = new ResponseTokenDTO(false, "Error Token no valido", null);
		} catch (MalformedJwtException me) {
			me.printStackTrace();
			validacion = new ResponseTokenDTO(false, "Error Token no valido", null);
		} catch(NullPointerException ne) {
			ne.printStackTrace();
			validacion = new ResponseTokenDTO(false, "Error Token no valido", null);
		} catch(Exception ex) {
			ex.printStackTrace();
			validacion = new ResponseTokenDTO(false, "Error Token no valido", null);
		}
		final BufferedWriter writer = response.getWriter();
		log.info("Deja el hilo corriendo....");
		writer.write(objetMapper.writeValueAsString(validacion));
	}

	private Claims validateToken(HttpRequest request) throws IOException, Exception  {
		System.out.println("request.getFirstHeader(HEADER).get()>> " + request.getFirstHeader(HEADER).get());
		String jwtToken = request.getFirstHeader(HEADER).get().replace(PREFIX, "");
		System.out.println("JwtToken :: " + jwtToken);
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	private Integer obtenerIdPblu(Claims claims) {
		Object idPblu = claims.get("idPblu");
		if (idPblu instanceof Integer integer) {
			return integer;
		}
		if (idPblu instanceof Long longValue) {
			return longValue.intValue();
		}
		if (idPblu instanceof Double doubleValue) {
			return doubleValue.intValue();
		}
		if (idPblu instanceof String stringValue && !stringValue.isBlank()) {
			return Integer.parseInt(stringValue);
		}
		return null;
	}
}
