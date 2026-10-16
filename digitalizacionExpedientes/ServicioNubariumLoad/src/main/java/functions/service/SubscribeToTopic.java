package functions.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

/**
 * Banco ASP Project: eiyu Class: SubscribeToTopic.java
 *
 * Description:
 *
 * @author Herwin TR @company ICORPTTI @created Oct 6, 2023 @since JDK17
 *
 * @version Control de cambios: @version 1.0 Oct 6, 2023 Herwin: Creacion de la
 * clase
 *
 * @category
 *
 */
public class SubscribeToTopic {
	private static final Logger log = LogManager.getLogger(SubscribeToTopic.class);

	public void publisherExample(String projectId, String topicId, String mensaje)
			throws IOException, ExecutionException, InterruptedException {
		TopicName topicName = TopicName.of(projectId, topicId);
		log.info("Enviando mensaje: [" + projectId + "] [" + topicId + "] [" + mensaje + "]");
		Publisher publisher = null;
		try {
			// Create a publisher instance with default settings bound to the topic
			publisher = Publisher.newBuilder(topicName).build();
			log.info("Mensaje: [" + mensaje + "]");
			ByteString data = ByteString.copyFromUtf8(mensaje);
			PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
			log.info("pubsubMessage: [" + pubsubMessage + "]");
			// Once published, returns a server-assigned message id (unique within the
			// topic)
			ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
			String messageId = messageIdFuture.get();
			System.out.println("Published message ID: " + messageId);
		}catch(IOException e) {
			System.out.println("Error al publicar mensaje: ");
			e.printStackTrace();
		}finally {
			
			if (publisher != null) {
				// When finished with the publisher, shutdown to free up resources.
				publisher.shutdown();
				publisher.awaitTermination(1, TimeUnit.MINUTES);
			}
		}
	}
}
