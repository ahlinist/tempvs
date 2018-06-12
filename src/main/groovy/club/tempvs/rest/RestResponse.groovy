package club.tempvs.rest

import grails.converters.JSON

/**
 * An object that represents a response from RESTful webservice.
 */
class RestResponse {
    Integer statusCode
    String responseBody
}
