package club.tempvs.rest

import javax.servlet.http.HttpServletRequest

import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PATCH
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

import grails.converters.JSON
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import org.grails.web.util.WebUtils

@Slf4j
@CompileStatic
class RestCaller {

    private static final String MOZILLA_USER_AGENT_VALUE = 'Mozilla/5.0'
    private static final String USER_AGENT_HEADER = 'User-Agent'
    private static final String USER_INFO_HEADER = 'User-Info'
    private static final String AUTHORIZATION_HEADER = 'Authorization'
    private static final String CONTENT_TYPE_HEADER = 'Content-Type'

    RestTemplate restTemplate

    @Value('${security.token}')
    private final String securityToken

    RestResponse call(String url, HttpMethod httpMethod, JSON payload = null) {
        String encodedToken = securityToken.encodeAsMD5() as String

        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.set(USER_AGENT_HEADER, MOZILLA_USER_AGENT_VALUE)
        httpHeaders.set(USER_INFO_HEADER, userInfoJson)
        httpHeaders.set(AUTHORIZATION_HEADER, encodedToken)

        if (httpMethod in [POST, PATCH]) {
            httpHeaders.set(CONTENT_TYPE_HEADER, APPLICATION_JSON_VALUE)
        }

        HttpEntity<String> httpEntity = new HttpEntity(payload?.toString(), httpHeaders)

        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange("http://${url}".toString(), httpMethod, httpEntity, byte[].class)
            return new RestResponse(responseEntity.statusCode, responseEntity.body, responseEntity.headers)
        } catch(ResourceAccessException e) {
            log.error e.message
            return null
        } catch (HttpStatusCodeException e) {
            return new RestResponse(e.statusCode, e.responseBodyAsByteArray)
        }
    }

    private String getUserInfoJson() {
        HttpServletRequest request = WebUtils.retrieveGrailsWebRequest().getCurrentRequest()
        return request.getHeader(USER_INFO_HEADER)
    }
}
