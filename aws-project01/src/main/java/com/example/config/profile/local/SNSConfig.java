package com.example.config.profile.local;

import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.Topic;

@Configuration
@Profile("local")
public class SNSConfig {
	
	private Logger LOG = Logger.getLogger(SNSConfig.class.getName());

	private AmazonSNS snsClient;
	private String productEventsTopic;
	
	public SNSConfig() {
		super();
		this.snsClient = AmazonSNSClient
				.builder()
				.withEndpointConfiguration(new AwsClientBuilder
						.EndpointConfiguration("http://localhost:4566", Regions.US_EAST_1.getName()))
				.withCredentials(new DefaultAWSCredentialsProviderChain()).build();
		
		CreateTopicRequest createTopicRequest = new CreateTopicRequest("product-events");
		this.productEventsTopic = this.snsClient.createTopic(createTopicRequest).getTopicArn();
		
		LOG.info("SNS topic ARN ::: " + this.productEventsTopic);
	}

	@Bean
	//Permitir que a aplicação publique eventos
	public AmazonSNS snsClient() {
		return snsClient;
	}

	@Bean(name = "productEventsTopic")
	//Para qual topico publicar o evento
	public Topic snsProductEventsTopic() {
		return new Topic().withTopicArn(productEventsTopic);
	}
}
