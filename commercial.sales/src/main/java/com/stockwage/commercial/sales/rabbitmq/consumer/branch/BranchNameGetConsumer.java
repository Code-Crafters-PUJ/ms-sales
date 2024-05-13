package com.stockwage.commercial.sales.rabbitmq.consumer.branch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class BranchNameGetConsumer {
    
    @RabbitListener(queues = "branch-name-get-queue")
    public String processBranchProductUpdate(@Payload String branchName) {
        System.out.println("Branch name received: " + branchName);
        return branchName;
    }

}