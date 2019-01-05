package club.tempvs.message

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.util.logging.Slf4j
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    private static final String MESSAGE_SERVICE_URL = System.getenv('MESSAGE_SERVICE_URL')
    private static final String MESSAGE_SECURITY_TOKEN = System.getenv('MESSAGE_SECURITY_TOKEN').encodeAsMD5() as String
    private static final String COUNT_HEADER = 'X-Total-Count'
    private static final String PROFILE_HEADER = 'Profile'

    static defaultAction = 'conversation'

    RestCaller restCaller

    def conversation(Long id) {
        if (id != null) {
            [conversationId: id]
        }
    }

    def api(String uri) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.method)
        String getParameters = (httpMethod == HttpMethod.GET && params.page && params.size)
                ? "?page=${params.page}&size=${params.size}" : ""
        String url = "${MESSAGE_SERVICE_URL}/api/" + uri + getParameters
        RestResponse restResponse = restCaller.call(url, httpMethod, MESSAGE_SECURITY_TOKEN, request.JSON as JSON)
        HttpHeaders httpHeaders = restResponse?.headers
        response.setHeader(PROFILE_HEADER, httpHeaders?.getFirst(PROFILE_HEADER))
        response.setHeader(COUNT_HEADER, httpHeaders?.getFirst(COUNT_HEADER))
        Integer status = restResponse?.statusCode?.value()

        if (status in [200, 400]) {
            render(status: status, text: restResponse?.responseBody)
        } else {
            response.sendError(500)
        }
    }
}
