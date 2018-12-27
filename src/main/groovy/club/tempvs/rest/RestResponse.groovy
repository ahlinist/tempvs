package club.tempvs.rest

import groovy.transform.CompileStatic
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

/**
 * An object that represents a response from RESTful webservice.
 */
@CompileStatic
class RestResponse {
    HttpStatus statusCode
    String responseBody
    HttpHeaders headers

    RestResponse(HttpStatus statusCode, String responseBody, HttpHeaders headers = null) {
        this.statusCode = statusCode
        this.responseBody = responseBody
        this.headers = headers
    }
}
