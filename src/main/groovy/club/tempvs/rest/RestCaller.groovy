package club.tempvs.rest

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import sun.net.www.protocol.http.HttpURLConnection

import java.nio.charset.StandardCharsets

@GrailsCompileStatic
class RestCaller {

    private static final String JSON_CONTENT_TYPE = 'application/json; charset=UTF-8'

    ConnectionFactory connectionFactory

    RestResponse doPost(String url, JSON payload = "", Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod("POST")
        connection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE)
        connection.setDoOutput(Boolean.TRUE)

        headers.each { String headerName, String headerValue ->
            connection.setRequestProperty(headerName, headerValue)
        }

        String jsonString = payload.toString()

        byte[] out = jsonString.getBytes(StandardCharsets.UTF_8)

        connection.setFixedLengthStreamingMode(out.length)
        connection.connect()

        OutputStream outputStream

        try {
            outputStream = connection.outputStream
            outputStream.write(out)
        } finally {
            outputStream?.close()
        }

        new RestResponse(statusCode: connection.responseCode)
    }

    RestResponse doGet(String url, Map<String, String> headers = [:], Map params = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod("GET")
        new RestResponse(statusCode: connection.responseCode)
    }
}
