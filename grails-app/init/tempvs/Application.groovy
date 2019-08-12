package tempvs

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate
import com.rabbitmq.client.ConnectionFactory;

@EnableScheduling
@EnableEurekaClient
class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        new RestTemplate()
    }

    @Bean
    ConnectionFactory amqpConnectionFactory(
            @Value('${amqp.url}') String amqpUrl,
            @Value('${amqp.timeout}') int amqpTimeout) {
        ConnectionFactory factory = new ConnectionFactory()
        factory.setUri(amqpUrl)
        factory.setConnectionTimeout(amqpTimeout)
        return factory
    }
}
