package by.training.webapp;

import by.training.webapp.Client.Client;
import by.training.webapp.Client.ClientListener;
import by.training.webapp.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.Socket;

@SpringBootApplication
public class SpringBootWebsocketChatApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebsocketChatApplication.class, args);

}
}
