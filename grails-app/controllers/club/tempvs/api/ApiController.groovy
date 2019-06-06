package club.tempvs.api

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import com.netflix.discovery.EurekaClient
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import org.springframework.http.HttpMethod
import org.springframework.security.access.annotation.Secured

@Secured('permitAll')
@GrailsCompileStatic
class ApiController {

    RestCaller restCaller
    EurekaClient eurekaClient

    def call(String service, String uri) {
        HttpMethod httpMethod = HttpMethod.valueOf(request.method)
        String serviceUrl = eurekaClient.getApplication(service)?.instances?.find()?.homePageUrl
        String getParameters = (httpMethod == HttpMethod.GET) ? "?page=${params.page}&size=${params.size}&q=${params.q}&userId=${params.userId}" : ""
        String url = "${serviceUrl}/api/" + uri + getParameters
        RestResponse restResponse = restCaller.call(url, httpMethod, request.JSON as JSON)
        Integer status = restResponse?.statusCode?.value()

        restResponse?.headers?.each { String key, List values ->
            values.each { value ->
                response.setHeader(key, (String) value)
            }
        }

        if (status in [200, 400]) {
            if (restResponse.image) {
                response.with {
                    setHeader('Content-length', restResponse?.responseBody?.length?.toString())
                    contentType = 'image/jpg' // or the appropriate image content type
                    outputStream << restResponse?.responseBody
                    outputStream.flush()
                }
            } else {
                render(status: status, text: new String(restResponse?.responseBody ?: "".bytes))
            }
        } else {
            response.sendError(500)
        }
    }
}
