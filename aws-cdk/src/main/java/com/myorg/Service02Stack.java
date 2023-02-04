package com.myorg;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.ecs.AwsLogDriverProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.CpuUtilizationScalingProps;
import software.amazon.awscdk.services.ecs.LogDriver;
import software.amazon.awscdk.services.ecs.ScalableTaskCount;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.events.targets.SnsTopic;
import software.amazon.awscdk.services.logs.LogGroup;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;
import software.constructs.Construct;

public class Service02Stack extends Stack {

	public Service02Stack(final Construct scope, final String id, Cluster cluster, SnsTopic productEventsTopic, Table productEventsDdb) {
		this(scope, id, null, cluster, productEventsTopic, productEventsDdb);
	}

	public Service02Stack(final Construct scope, final String id, final StackProps props, Cluster cluster, SnsTopic productEventsTopic, Table productEventsDdb) {
		super(scope, id, props);

        //Cria a fila para ouvir os eventos que apresentaram problemas
        Queue productEventsQueueDLQ = Queue.Builder
        		.create(this, "productEventsDLQ")
        		.queueName("product-events-dlq")
        		.build();
        
        DeadLetterQueue deadLetterQueue = DeadLetterQueue
        		.builder()
        		.queue(productEventsQueueDLQ)
        		.maxReceiveCount(3)
        		.build();
        
        //Cria a fila para ouvir os eventos de produtos
        Queue productEventsQueue = Queue
        		.Builder
        		.create(this, "ProductEvents")
        		.queueName("product-events")
        		.deadLetterQueue(deadLetterQueue)
        		.build();
        
        SqsSubscription sqsSubscription = SqsSubscription.Builder.create(productEventsQueue).build();
        productEventsTopic.getTopic().addSubscription(sqsSubscription);
        
		Map<String, String> envVariables = new HashMap<String, String>();
        envVariables.put("AWS_REGION", "us-east-1");
        envVariables.put("AWS_SQS_QUEUE_PRODUCT_EVENTS_NAME", productEventsQueue.getQueueName());
        
		ApplicationLoadBalancedFargateService service02 = ApplicationLoadBalancedFargateService.Builder.create(this, "ALB02")
				.serviceName("service-02")
				.cluster(cluster)
				.cpu(512)
				.memoryLimitMiB(1024)
				.desiredCount(2)
				.listenerPort(9090)
				.assignPublicIp(true)
				.taskImageOptions(
						ApplicationLoadBalancedTaskImageOptions.builder()
						.containerName("aws-project02")
						.image(ContainerImage.fromRegistry("antoniocesarlopes/aws-project02:0.0.1"))
						.containerPort(8080)
						.logDriver(LogDriver.awsLogs(AwsLogDriverProps.builder()
								.logGroup(LogGroup.Builder.create(this, "Service02LogGroup")
										.logGroupName("Service02")
										.removalPolicy(RemovalPolicy.DESTROY)
										.build())
								.streamPrefix("Service02")
								.build()))
						.environment(envVariables)
						.build())
				.publicLoadBalancer(true)
				.build();

		service02.getTargetGroup().configureHealthCheck(new HealthCheck.Builder()
				.path("/actuator/health")
				.port("9090")
				.healthyHttpCodes("200")
				.build());

		ScalableTaskCount scalableTaskCount = service02.getService().autoScaleTaskCount(EnableScalingProps.builder()
				.minCapacity(2)
				.maxCapacity(4)
				.build());

		scalableTaskCount.scaleOnCpuUtilization("Service02AutoScaling", CpuUtilizationScalingProps.builder()
				.targetUtilizationPercent(50)
				.scaleInCooldown(Duration.seconds(60))
				.scaleOutCooldown(Duration.seconds(60))
				.build());
		
		productEventsQueue.grantConsumeMessages(service02.getTaskDefinition().getTaskRole());
		productEventsDdb.grantReadWriteData(service02.getTaskDefinition().getTaskRole());
	}

}
