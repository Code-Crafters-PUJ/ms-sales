package com.stockwage.commercial.sales.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue createUpdateProductQueue(){
        return new Queue("create-update-product-queue", true);
    }

    //create-update-branch-product-queue
    @Bean
    public Queue createUpdateBranchProductQueue(){
        return new Queue("create-update-branch-product-queue", true);
    }

    @Bean
    public Queue deleteProductQueue() {
        return new Queue("delete-product-queue", true);
    }

}
