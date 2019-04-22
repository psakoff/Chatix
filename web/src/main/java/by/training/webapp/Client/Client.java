package by.training.webapp.Client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;
@Component
public class Client {
    public static ClientListener clientListener;

    public void start(){
        try {
            Socket socket = new Socket("localhost", 8081);
            clientListener = new ClientListener(socket);
            clientListener.start();
        } catch (IOException e) {
            System.err.println("Socket failed");
        }

    }
}
