package club.tempvs.rest

import club.tempvs.user.Profile
import club.tempvs.user.User
import club.tempvs.user.UserService
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

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String URL = 'url'
    private static final String TIMEZONE = 'Europe/Minsk'

    def json = Mock JSON
    def user = Mock User
    def userService = Mock UserService
    def profile = Mock Profile
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
        given:
        String body = "body"
        String role = "role"
        List<String> roles = [role, role]

        when:
        def result = restCaller.call(URL, HttpMethod.POST, json)

        then:
        1 * restTemplate.exchange("http://${URL}", HttpMethod.POST, _ as HttpEntity, byte[].class) >> responseEntity
        1 * responseEntity.statusCode >> HttpStatus.OK
        1 * responseEntity.body >> body
        1 * responseEntity.headers >> httpHeaders
        0 * _

        and:
        result instanceof RestResponse
    }

    void "Test call() for GET"() {
        given:
        String body = "body"
        String role = "role"
        List<String> roles = [role, role]

        when:
        def result = restCaller.call(URL, HttpMethod.GET, json)

        then:
        1 * restTemplate.exchange("http://${URL}", HttpMethod.GET, _ as HttpEntity, byte[].class) >> responseEntity
        1 * responseEntity.statusCode >> HttpStatus.OK
        1 * responseEntity.body >> body
        1 * responseEntity.headers >> httpHeaders
        0 * _

        and:
        result instanceof RestResponse
    }
}
