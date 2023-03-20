package com.rodrigo.backend.rabbitmq.pubconfirmation;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

public class SecondSenderPubConfirmation {

    private static final String NAME_EXCHANGE = "fanoutExchange";

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
            System.out.println(selectOk);

            //declarar a exchange (nome
            channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

            //criar a mensagem
            String message = "This is my ";
            int setOfMessages = 10;
            int outMessages = 0;
            String bodyMessage;

            //enviar a mensagem - não há o nome da exchange (primeiro parâmetro) pois não está sendo utilizado nesse primeiro momento)
            for (int i = 0; i < setOfMessages; i++, outMessages++) {
                bodyMessage = message + i;
                channel.basicPublish(NAME_EXCHANGE, "", null, bodyMessage.getBytes(StandardCharsets.UTF_8));
                System.out.println("[x] Sending the message: '" + bodyMessage + "'");

                if(outMessages == setOfMessages) {
                    //wait for 5 sec.
                    channel.waitForConfirmsOrDie(5000);
                    System.out.println("[v] message confirmed ");
                    outMessages = 0;
                }
            }

            if(outMessages != 0) {
                channel.waitForConfirmsOrDie(5000);
                System.out.println("[v] message confirmed ");
            }

            System.out.println("[x] Done");
            connection.close();
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
