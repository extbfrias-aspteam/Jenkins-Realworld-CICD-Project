package topico;

import java.security.GeneralSecurityException;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.CloudEventsFunction;
import com.google.gson.Gson;

import io.cloudevents.CloudEvent;
import topico.dto.PubSubStorageDto;
import topico.dto.PubSubStorageSendDto;

public class PubSubNubariumLeerApp implements CloudEventsFunction {
	private static final Logger logger = Logger.getLogger(PubSubNubariumLeerApp.class.getName());


	private static final String PATH = System.getenv("HOST_SERVICIO_NUBARIUM"); 
	
	RestTemplate restTemplate = new RestTemplate();

	@Override
	public void accept(CloudEvent event) throws GeneralSecurityException {
		// Get cloud event data as JSON string
		String cloudEventData = new String(event.getData().toBytes());

//		// Decode JSON event data to the Pub/Sub MessagePublishedData type
		Gson gson = new Gson();
		PubSubStorageDto data = gson.fromJson(cloudEventData, PubSubStorageDto.class);
//		// Get the message from the data
//		Message message = data.getMessage();
//		// Get the base64-encoded data from the message & decode it
//		String encodedData = message.getData();
//		String decodedData = new String(Base64.getDecoder().decode(encodedData));
		// Log the message
		logger.info(data.toString());
		logger.info("Linked storage... " + data.getSelfLink());
		logger.info("Pub/Sub message: " + cloudEventData);
		enviarInfoNubariumValidation(data);
	}

	public void enviarInfoNubariumValidation(PubSubStorageDto documento) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		PubSubStorageSendDto body = new PubSubStorageSendDto();
		body.setSelfLink(documento.getSelfLink());
		body.setEsINE(false);
		if ((documento.getName().contains("INE_FRONTAL") || documento.getName().contains("INE_REVERSO"))
				|| (documento.getName().contains("INEFRONTAL") || documento.getName().contains("INEREVERSO"))
				|| (documento.getName().contains("FRONTALINE") || documento.getName().contains("REVERSOINE"))
				|| (documento.getName().contains("FRONTAL_INE") || documento.getName().contains("REVERSO_INE"))) {
			body.setEsINE(true);
		}
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = "";
		try {
			jsonStr = Obj.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		restTemplate.postForObject(PATH + "/AppServicioNubarium", jsonStr,
				String.class);
	}
}
