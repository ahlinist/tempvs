package club.tempvs.ampq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import groovy.transform.CompileStatic

@CompileStatic
class AmqpSender {

    private static final String CLOUDAMQP_URL = System.getenv('CLOUDAMQP_URL')

    void send(String queue, String payload) {
        Thread.start {
            ConnectionFactory factory = new ConnectionFactory()
            factory.setUri(CLOUDAMQP_URL)
            factory.setConnectionTimeout(30000)
            Connection connection
            Channel channel

            try {
                connection = factory.newConnection()
                channel = connection.createChannel()
                channel.queueDeclare(queue, false, false, false, null)
                channel.basicPublish("", queue, null, payload.getBytes())
            } finally {
                channel?.close()
                connection?.close()
            }
        }
    }
}
