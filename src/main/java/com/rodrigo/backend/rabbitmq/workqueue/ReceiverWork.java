package com.rodrigo.backend.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ReceiverWork {

    private static final String NAME_QUEUE = "Work";

    private static void doWork(String task) throws InterruptedException {
        for (char ch:task.toCharArray()){
            if(ch == '.') Thread.sleep(1000);
        }
    }

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

        //declarar a fila que será usada
        //nome da fila, exclusiva, autodelete, durable, map(args)
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //receber mensagem
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message: '" + message + "'");

            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("[x] Done");
            }
        };

        //enviar a mensagem
        boolean autoAck = true; //ack is false
        channel.basicConsume(NAME_QUEUE, autoAck, deliverCallback, ConsumeTag -> {});
    }
}
