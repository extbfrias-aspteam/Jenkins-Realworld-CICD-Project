package net.cero.promesi;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mx.Res.RespuestaCommons;
import com.mx.beans.ErrorDTO;
import com.mx.constantes.Constantes;

public class RestCall2<E, S extends RespuestaCommons> {
	
	  private static final int EXITO = 200;
	 
	  private static final Logger LOG = LogManager.getLogger(RestCall2.class);
	  private E entrada;
	  private S salida;
	  private String url;
	  private JavaType dtoType;
	  private Class<S> clase;
	  private AuthHeadersRequest auth;
	  
	  
	  public RestCall2() {
		
	  }
	  
	  public RestCall2(final String url,final E entrada , final Class<S> s,AuthHeadersRequest authRequest) {
		    this.url = url;
		    this.entrada = entrada;
		    this.clase=s;
		    this.auth = authRequest;
		    
	  }
	  
	  private void generate(){
		  this.dtoType = TypeFactory.defaultInstance().constructFromCanonical(clase.getCanonicalName());
		    try {
		    	this.salida = clase.newInstance();
		    } catch (InstantiationException|IllegalAccessException e1) {
		    	LOG.info(e1);		    	
		    }
	  }
	  
	  public S call(){
		  this.generate();
		  
		try{
			
			ResteasyClientBuilder clientBuilder = new ResteasyClientBuilder();
			ResteasyClient client = clientBuilder.build();
			final ObjectMapper mapper = new ObjectMapper(); 
			String tramaFinal = "";
				 
		    tramaFinal =  mapper.writeValueAsString(entrada);
		     
		    ResteasyWebTarget target = client.target(this.url);
		    LOG.info("Auth: "+this.auth.getUsername());
		    client.register(this.auth);
		    Response response = target.request().post(Entity.entity(tramaFinal, MediaType.APPLICATION_JSON));
		     
		    if( this.validaRespuesta(response)){
				String data= response.readEntity(String.class);
			   	response.close();  
			   	if(data==null || data.isEmpty() ) return salida;
			   	return mapper.readValue(data, this.dtoType);
		    }
		    response.close();
		    
		} catch (Exception e) {
			LOG.error("RestCall2 "+e.toString());
			ErrorDTO error = new ErrorDTO();
			error.setMessage("Error de tipo de dato");
			salida.setError(error);	
		}
		    return salida;  
	  }
	  
	  
	  private boolean validaRespuesta(final Response response) throws IOException {
		  boolean resultado = false;
		  if (response.getStatus() != EXITO) {
			  resultado = false;
	      } else {
	    	  resultado = true;
	    	  LOG.info("Petición::" + this.url + "::Exito : HTTP CODE  " + response.getStatus()+" ::");
	    	  if(Constantes.COD_STATUS_ERROR.equalsIgnoreCase(response.getHeaderString(Constantes.HEADER_STATUS))){
	    		  LOG.info(response.getHeaderString(Constantes.HEADER_STATUS)+"::"+response.getHeaderString(Constantes.HEADER_MESSAGE));
	    		  resultado = false;
	            }
	      }
		  return resultado;
		    
		}
	  
	  
	  public E getEntrada() {
		return entrada;
	}

	public void setEntrada(E entrada) {
		this.entrada = entrada;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Class<S> getClase() {
		return clase;
	}

	public void setClase(Class<S> s) {
		this.clase = s;
	}

	public AuthHeadersRequest getAuth() {
		return auth;
	}

	public void setAuth(AuthHeadersRequest auth) {
		this.auth = auth;
	}
	

}
