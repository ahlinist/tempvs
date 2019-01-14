package tempvs

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@EnableDiscoveryClient
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        builder.build()
    }
}