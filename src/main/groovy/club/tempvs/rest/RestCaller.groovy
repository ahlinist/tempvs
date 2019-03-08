package club.tempvs.rest

import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PATCH
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

import club.tempvs.user.Profile
import club.tempvs.user.User
import club.tempvs.user.UserService
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
import org.springframework.context.i18n.LocaleContextHolder

@Slf4j
@CompileStatic
class RestCaller {

    private static final String MOZILLA_USER_AGENT_VALUE = 'Mozilla/5.0'
    private static final String USER_AGENT_HEADER = 'User-Agent'
    private static final String USER_INFO_HEADER = 'User-Info'
    private static final String AUTHORIZATION_HEADER = 'Authorization'
    private static final String CONTENT_TYPE_HEADER = 'Content-Type'

    UserService userService
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
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, byte[].class)
            return new RestResponse(responseEntity.statusCode, responseEntity.body, responseEntity.headers)
        } catch(ResourceAccessException e) {
            log.error e.message
            return null
        } catch (HttpStatusCodeException e) {
            return new RestResponse(e.statusCode, e.responseBodyAsByteArray)
        }
    }

    private String getUserInfoJson() {
        Profile currentProfile = userService.currentProfile

        JSON userInfoJson

        if (currentProfile) {
            User user = currentProfile.user
            Profile userProfile = user.userProfile

            userInfoJson = [
                    userId: user.id as String,
                    profileId: currentProfile?.id as String,
                    userProfileId: userProfile?.id,
                    userName: userProfile.toString(),
                    timezone: user.timeZone,
                    lang: LocaleContextHolder.locale.language,
                    roles: userService.roles
            ] as JSON
        }

        return userInfoJson?.toString()
    }
}
