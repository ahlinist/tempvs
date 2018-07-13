package club.tempvs.rest

import groovy.transform.CompileStatic
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@CompileStatic
class RestHelper {

    private static final String USER_AGENT = 'Mozilla/5.0'

    RestTemplate newTemplate() {
        new RestTemplate()
    }

    RestResponse newRestResponse(HttpClientErrorException e) {
        new RestResponse(statusCode: e.statusCode, responseBody: e.responseBodyAsString)
    }

    RestResponse newRestResponse(ResponseEntity<String> responseEntity) {
        new RestResponse(statusCode: responseEntity.statusCode, responseBody: responseEntity.body)
    }

    HttpEntity<String> newHttpEntity(Map<String, String> headers, String body = null) {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.set("User-Agent", USER_AGENT)

        for (Map.Entry<String, String> entry in headers.entrySet()) {
            httpHeaders.set(entry.key, entry.value)
        }

        new HttpEntity<String>(body, httpHeaders)
    }
}
