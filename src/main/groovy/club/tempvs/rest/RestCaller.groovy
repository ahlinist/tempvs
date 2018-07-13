package club.tempvs.rest

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.util.logging.Slf4j
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

@Slf4j
@GrailsCompileStatic
class RestCaller {

    private static final String TOKEN = 'token'
    private static final String CONTENT_TYPE = 'Content-Type'
    private static final String JSON_CONTENT_TYPE = 'application/json; charset=UTF-8'

    RestHelper restHelper

    RestResponse doGet(String url, String token = null) {
        RestTemplate restTemplate = restHelper.newTemplate()
        HttpEntity<String> entity = restHelper.newHttpEntity([(TOKEN): token])
        execute(restTemplate, url, HttpMethod.GET, entity)
    }

    RestResponse doPost(String url, String token = null, JSON payload = new JSON()) {
        RestTemplate restTemplate = restHelper.newTemplate()
        Map<String, String> headers = [(TOKEN): token, (CONTENT_TYPE): JSON_CONTENT_TYPE]
        HttpEntity<String> entity = restHelper.newHttpEntity(headers, payload?.toString())
        execute(restTemplate, url, HttpMethod.POST, entity)
    }

    RestResponse doDelete(String url, String token = null) {
        RestTemplate restTemplate = restHelper.newTemplate()
        HttpEntity<String> entity = restHelper.newHttpEntity([(TOKEN): token])
        execute(restTemplate, url, HttpMethod.DELETE, entity)
    }

    private RestResponse execute(RestTemplate restTemplate, String url, HttpMethod httpMethod, HttpEntity<String> httpEntity) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class)
            return restHelper.newRestResponse(responseEntity)
        } catch(ResourceAccessException e) {
            log.error e.message
            return null
        } catch (HttpClientErrorException e) {
            return restHelper.newRestResponse(e)
        }
    }
}
