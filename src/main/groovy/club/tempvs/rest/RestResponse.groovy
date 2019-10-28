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
    byte[] responseBody
    HttpHeaders headers

    RestResponse(HttpStatus statusCode, byte[] responseBody, HttpHeaders headers = null) {
        this.statusCode = statusCode
        this.responseBody = responseBody
        this.headers = headers
    }
}
