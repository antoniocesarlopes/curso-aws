package com.example.config.profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;

@Configuration
@Profile("!local")
public class SNSConfig {

	@Value("${aws.region}")
	private String awsRegion;
	
	@Value("${sns.topic.product.events.arn}")
	private String productEventsTopic;

	@Bean
	//Permitir que a aplicação publique eventos
	public AmazonSNS snsClient() {
		return AmazonSNSClientBuilder.standard().withRegion(awsRegion)
				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
	}

	@Bean(name = "productEventsTopic")
	//Para qual topico publicar o evento
	public Topic snsProductEventsTopic() {
		return new Topic().withTopicArn(productEventsTopic);
	}
}
