package by.training.webapp.Client;

import by.training.webapp.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ClientListener {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private String nickname;
    protected String type;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;

    public BufferedReader getIn(){return this.in;}

    public ClientListener(Socket socket) {
        this.socket = socket;
    }

    public void start() throws IOException{
        try {
            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        } catch (IOException e) {
            ClientListener.this.downService();
        }

    }

    public void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }
    public void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }
}