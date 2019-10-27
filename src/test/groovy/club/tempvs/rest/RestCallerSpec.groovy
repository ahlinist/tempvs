package club.tempvs.rest

import grails.converters.JSON
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
class RestCallerSpec extends Specification {

    private static final String URL = 'url'

    def json = Mock JSON
    def restTemplate = Mock RestTemplate
    def responseEntity = Mock ResponseEntity
    def httpHeaders = Mock HttpHeaders

    RestCaller restCaller

    def setup() {
        restCaller = new RestCaller()
        restCaller.restTemplate = restTemplate
    }

    def cleanup() {
    }

    void "Test call() for POST"() {
        when:
        def result = restCaller.call(URL, HttpMethod.POST, json)

        then:
        1 * restTemplate.exchange("http://${URL}", HttpMethod.POST, _ as HttpEntity, byte[].class) >> responseEntity
        1 * responseEntity.statusCode >> HttpStatus.OK
        1 * responseEntity.body
        1 * responseEntity.headers >> httpHeaders
        0 * _

        and:
        result instanceof RestResponse
    }

    void "Test call() for GET"() {
        when:
        def result = restCaller.call(URL, HttpMethod.GET, json)

        then:
        1 * restTemplate.exchange("http://${URL}", HttpMethod.GET, _ as HttpEntity, byte[].class) >> responseEntity
        1 * responseEntity.statusCode >> HttpStatus.OK
        1 * responseEntity.body
        1 * responseEntity.headers >> httpHeaders
        0 * _

        and:
        result instanceof RestResponse
    }
}
