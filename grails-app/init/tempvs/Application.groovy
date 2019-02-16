package tempvs

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import com.rabbitmq.client.ConnectionFactory;

@EnableEurekaClient
class Application extends GrailsAutoConfiguration {

    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.build()
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
