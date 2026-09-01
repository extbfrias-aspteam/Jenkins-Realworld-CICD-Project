//package com.asp.eiyu.api.admdocument.gtw.dto;
//
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Flow.Publisher;
//import java.util.logging.Level;
//
//import com.google.protobuf.ByteString;
//import com.google.pubsub.v1.ProjectTopicName;
//import com.google.pubsub.v1.PubsubMessage;
///**
// * Banco ASP Project: eiyu Class: PublishMessage.java
// *
// * Description:
// *
// * @author Herwin TR @company ICORPTTI @created Oct 6, 2023 @since JDK17
// *
// * @version Control de cambios: @version 1.0 Oct 6, 2023 Herwin: Creacion de la
// * clase
// *
// * @category
// *
// */
//public class PublishMessage {
//
//	public void publicarMensaje(String topicName) {
//		System.out.println("Publishing message to topic: " + topicName);
//
//		// Create the PubsubMessage object
//		// (This is different than the PubsubMessage POJO used in Pub/Sub-triggered
//		// functions)
//		ByteString byteStr = ByteString.copyFrom(maybeMessage.get(), StandardCharsets.UTF_8);
//		PubsubMessage pubsubApiMessage = PubsubMessage.newBuilder().setData(byteStr).build();
//
//		Publisher publisher = Publisher.newBuilder(ProjectTopicName.of(PROJECT_ID, topicName)).build();
//
//		// Attempt to publish the message
//		String responseMessage;
//		try {
//			publisher.publish(pubsubApiMessage).get();
//			responseMessage = "Message published.";
//		} catch (InterruptedException | ExecutionException e) {
//			logger.log(Level.SEVERE, "Error publishing Pub/Sub message: " + e.getMessage(), e);
//			responseMessage = "Error publishing Pub/Sub message; see logs for more info.";
//		}
//
//		responseWriter.write(responseMessage);
//	}
//
//}
