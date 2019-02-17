package club.tempvs.rest

import groovy.transform.CompileStatic
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

/**
 * An object that represents a response from RESTful webservice.
 */
@CompileStatic
class RestResponse {

    private static final String CONTENT_TYPE_HEADER = 'Content-Type'

    HttpStatus statusCode
    byte[] responseBody
    HttpHeaders headers

    RestResponse(HttpStatus statusCode, byte[] responseBody, HttpHeaders headers = null) {
        this.statusCode = statusCode
        this.responseBody = responseBody
        this.headers = headers
    }

    boolean isImage() {
        this.headers?.get(CONTENT_TYPE_HEADER)?.contains 'image/jpeg'
    }
}
