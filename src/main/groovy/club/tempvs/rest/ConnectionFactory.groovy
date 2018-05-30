package club.tempvs.rest

import grails.compiler.GrailsCompileStatic
import sun.net.www.protocol.http.HttpURLConnection

/**
 * Provides an instance of HttpURLConnection.
 */
@GrailsCompileStatic
class ConnectionFactory {

    private static final String USER_AGENT = 'Mozilla/5.0'
    private static final String ACCEPT_LANGUAGE = 'en-US,en;q=0.5'

    HttpURLConnection getInstance(String url) {
        HttpURLConnection connection = new URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", USER_AGENT)
        connection.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE)
        return connection
    }
}
