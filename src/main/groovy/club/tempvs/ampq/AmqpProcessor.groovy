package club.tempvs.ampq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.QueueingConsumer
import groovy.transform.CompileStatic

@CompileStatic
class AmqpProcessor {

    private static final long DELIVERY_TIMEOUT = 30000L

    ConnectionFactory amqpConnectionFactory

    void send(String queue, String payload) {
        Thread.start {
            Connection connection
            Channel channel

            try {
                connection = amqpConnectionFactory.newConnection()
                channel = connection.createChannel()
                channel.queueDeclare(queue, false, false, false, null)
                channel.basicPublish("", queue, null, payload.getBytes())
            } finally {
                channel?.close()
                connection?.close()
            }
        }
    }

    String receive(String queue, Closure action) {
        Connection connection
        Channel channel

        try {
            connection = amqpConnectionFactory.newConnection()
            channel = connection.createChannel()
            channel.queueDeclare(queue, false, false, false, null)

            QueueingConsumer consumer = new QueueingConsumer(channel)
            channel.basicConsume(queue, true, consumer)

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery(DELIVERY_TIMEOUT)

                if (delivery != null) {
                    String body = new String(delivery.getBody())
                    action(body)
                } else {
                    throw new RuntimeException("The " + queue + " queue is empty")
                }
            }
        } catch (RuntimeException e) {
            //do nothing, just skip until the next execution
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            channel?.close()
            connection?.close()
        }
    }
}
