package club.tempvs.rest

import grails.converters.JSON
import spock.lang.Specification
import sun.net.www.protocol.http.HttpURLConnection

class RestCallerSpec extends Specification {

    private static final String URL = 'url'
    private static final String CONTENT_TYPE = 'application/json; charset=UTF-8'

    def json = Mock JSON
    def connection = Mock HttpURLConnection
    def connectionFactory = Mock ConnectionFactory
    def outputStream = Mock OutputStream

    RestCaller restCaller

    def setup() {
        restCaller = new RestCaller()
        restCaller.connectionFactory = connectionFactory
    }

    def cleanup() {
    }

    void "Test doGet()"() {
        when:
        def result = restCaller.doGet(URL)

        then:
        1 * connectionFactory.getInstance(URL) >> connection
        1 * connection.setRequestMethod("GET")
        1 * connection.responseCode
        0 * _

        and:
        result instanceof RestResponse
    }

    void "Test doPost()"() {
        when:
        def result = restCaller.doPost(URL, json, [:])

        then:
        1 * connectionFactory.getInstance(URL) >> connection
        1 * connection.setRequestMethod("POST")
        1 * connection.setRequestProperty("Content-Type", CONTENT_TYPE)
        1 * connection.setDoOutput(Boolean.TRUE)
        1 * json.toString() >> ""
        1 * connection.setFixedLengthStreamingMode(_ as Integer)
        1 * connection.connect()
        1 * connection.outputStream >> outputStream
        1 * outputStream.write(_ as byte[])
        1 * outputStream.close()
        1 * connection.responseCode
        0 * _

        and:
        result instanceof RestResponse
    }
}
