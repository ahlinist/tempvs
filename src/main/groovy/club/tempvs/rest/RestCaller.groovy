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
        parseResponse(connection)
    }

    RestResponse doPost(String url, JSON payload = new JSON(), Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(HttpMethod.POST.name())
        sendRequest(connection, payload, headers)
        parseResponse(connection)
    }

    RestResponse doDelete(String url, JSON payload = new JSON(), Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(HttpMethod.DELETE.name())
        sendRequest(connection, payload, headers)
        parseResponse(connection)
    }

    private HttpURLConnection sendRequest(HttpURLConnection connection, JSON payload = new JSON(), Map<String, String> headers = [:]) {
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
    }

    private RestResponse parseResponse(HttpURLConnection connection) {
        Integer statusCode
        String responseBody

        try {
            statusCode = connection.responseCode

            if (statusCode == HttpStatus.OK.value()) {
                responseBody = readResponseBody(connection)
            }
        } catch (ConnectException e) {
            log.error "'${connection.getURL()}' URL is not available"
            return null
        }

        return new RestResponse(statusCode: statusCode, responseBody: responseBody)
    }

    private String readResponseBody(HttpURLConnection connection) {
        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
        BufferedReader reader = new BufferedReader(inputStreamReader)

        String line
        StringBuffer buffer = new StringBuffer()

        while ((line = reader.readLine()) != null) {
            buffer.append(line)
        }

        buffer.toString()
    }
}
