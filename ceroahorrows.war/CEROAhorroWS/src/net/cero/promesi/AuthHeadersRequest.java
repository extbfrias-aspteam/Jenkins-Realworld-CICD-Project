package net.cero.promesi;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

public class AuthHeadersRequest implements ClientRequestFilter{

	
	 private final String username;

	    public AuthHeadersRequest(String username) {
	        this.username = username;
	    }
	    
	
	    public String getUsername() {
			return username;
		}


	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
	        requestContext.getHeaders().add("Authorization", username);
	}

}
