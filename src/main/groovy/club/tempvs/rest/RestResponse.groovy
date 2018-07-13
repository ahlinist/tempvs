package club.tempvs.rest

import org.springframework.http.HttpStatus

/**
 * An object that represents a response from RESTful webservice.
 */
class RestResponse {
    HttpStatus statusCode
    String responseBody
}
