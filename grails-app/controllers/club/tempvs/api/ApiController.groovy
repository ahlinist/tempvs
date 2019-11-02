package club.tempvs.api

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import org.springframework.http.HttpMethod

@GrailsCompileStatic
class ApiController {

    RestCaller restCaller

    def call(String service, String uri) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.method)
        String getParams = (httpMethod == HttpMethod.GET) ? '?' + params.collect { "${it.key}=${it.value}" }.join('&') : ""
        String url = "${service}/api/" + uri + getParams
        //TODO: pass all headers downstream
        RestResponse restResponse = restCaller.call(url, httpMethod, request.cookies, request.JSON as JSON)
        Integer status = restResponse?.statusCode?.value()

        restResponse?.headers?.each { String key, List values ->
            values.each { value ->
                response.addHeader(key, (String) value)
            }
        }

        render(status: status, text: new String(restResponse?.responseBody ?: "".bytes))
    }
}
