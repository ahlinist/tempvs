package club.tempvs.rest

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.util.logging.Slf4j
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import sun.net.www.protocol.http.HttpURLConnection

import java.nio.charset.StandardCharsets

@Slf4j
@GrailsCompileStatic
class RestCaller {

    private static final String JSON_CONTENT_TYPE = 'application/json; charset=UTF-8'

    ConnectionFactory connectionFactory

    RestResponse doGet(String url, Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(HttpMethod.GET.name())
        setHeaders(connection, headers)
        parseResponse(connection)
    }

    RestResponse doPost(String url, Map<String, String> headers = [:], JSON payload = new JSON()) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(HttpMethod.POST.name())
        setHeaders(connection, headers)
        sendRequest(connection, payload)
        parseResponse(connection)
    }

    RestResponse doDelete(String url, Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(HttpMethod.DELETE.name())
        setHeaders(connection, headers)
        parseResponse(connection)
    }

    private HttpURLConnection sendRequest(HttpURLConnection connection, JSON payload = new JSON()) {
        connection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE)
        connection.setDoOutput(Boolean.TRUE)

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
    }

    private void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        headers.each { String headerName, String headerValue ->
            connection.setRequestProperty(headerName, headerValue)
        }
    }

    private RestResponse parseResponse(HttpURLConnection connection) {
        Integer statusCode
        InputStream inputStream

        try {
            statusCode = connection.responseCode
            inputStream = (statusCode == HttpStatus.OK.value()) ? connection.inputStream : connection.errorStream

            return new RestResponse(statusCode: statusCode, responseBody: inputStream.text)
        } catch (ConnectException e) {
            log.error "'${connection.getURL()}' URL is not available"
            return null
        } finally {
            inputStream?.close()
        }
    }
}
