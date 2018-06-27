package club.tempvs.rest

import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import sun.net.www.protocol.http.HttpURLConnection

import java.nio.charset.StandardCharsets

@GrailsCompileStatic
class RestCaller {

    private static final String JSON_CONTENT_TYPE = 'application/json; charset=UTF-8'

    ConnectionFactory connectionFactory

    RestResponse doGet(String url, Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(HttpMethod.GET.name())
        new RestResponse(statusCode: connection.responseCode)
    }

    RestResponse doPost(String url, JSON payload = new JSON(), Map<String, String> headers = [:]) {
        processRequestWithBody(HttpMethod.POST.name(), url, payload, headers)
    }

    RestResponse doDelete(String url, JSON payload = new JSON(), Map<String, String> headers = [:]) {
        processRequestWithBody(HttpMethod.DELETE.name(), url, payload, headers)
    }

    private RestResponse processRequestWithBody(String method, String url, JSON payload = new JSON(), Map<String, String> headers = [:]) {
        HttpURLConnection connection = connectionFactory.getInstance(url)
        connection.setRequestMethod(method)
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

        RestResponse restResponse = new RestResponse()
        Integer statusCode = connection.responseCode
        restResponse.statusCode = statusCode

        if (statusCode == HttpStatus.OK.value()) {
            restResponse.responseBody = parseResponse(connection)
        }

        restResponse
    }

    private String parseResponse(HttpURLConnection connection) {
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