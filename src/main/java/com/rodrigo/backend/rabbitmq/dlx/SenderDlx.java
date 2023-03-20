package com.rodrigo.backend.rabbitmq.dlx;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

public class SenderDlx {

    private static final String NAME_EXCHANGE = "mainExchange";

    public static void main(String[] args) {
        //Criação da conexão
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("123");
        connectionFactory.setPort(5672);


        Connection connection;
        try {
            connection = connectionFactory.newConnection();
            System.out.println(connection.hashCode());

            //criar um novo canal
            Channel channel = connection.createChannel();
            AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
            System.out.println("Canal: " + channel);

            //declarar a exchange (nome
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");

            //criar a mensagem
            String message = "Hello! This is a test!";
            String rountingKey = "bkConsumer.test1";


            //enviar a mensagem
            channel.basicPublish(NAME_EXCHANGE, rountingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[x] Sending the message: '" + message + "'");

            System.out.println("[x] Done");
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
