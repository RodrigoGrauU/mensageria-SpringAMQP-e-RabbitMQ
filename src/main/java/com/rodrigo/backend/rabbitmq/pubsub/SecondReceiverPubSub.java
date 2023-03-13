package com.rodrigo.backend.rabbitmq.pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SecondReceiverPubSub {
    private static final String NAME_QUEUE = "broadcast";

    private static final String NAME_EXCHANGE = "fanoutExchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        //Criação da conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);

        Connection connection =  connectionFactory.newConnection();

        //criar um novo canal
        Channel channel = connection.createChannel();
        System.out.println("Canal criado: " + channel);

        //declaracao da fila
        //nome da fila, exclusiva, autodelete, durable, map(args)
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //declaração da exchange -> só pode ser feito com fila existente
        channel.exchangeDeclare(NAME_EXCHANGE, "fanout");
        channel.queueBind(NAME_QUEUE, NAME_EXCHANGE, "");

        //receber mensagem
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message: '" + message + "'");
        };

        //enviar a mensagem
        boolean autoAck = true; //ack is false
        channel.basicConsume(NAME_QUEUE, autoAck, deliverCallback, ConsumeTag -> {});
    }
}
