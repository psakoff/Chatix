package by.training.webapp.controller;

import by.training.webapp.Client.Client;
import by.training.webapp.Client.ClientListener;
import by.training.webapp.Client.Message;
import by.training.webapp.Client.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate template;
    @Autowired
    static Client client = new Client();
    ObjectMapper objectMapper = new ObjectMapper();
    ArrayList<User> users = new ArrayList<>();
        private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = client.clientListener.getIn().readLine();
//                    if (str.equals("/disconnect")) {
//
//                    }
                    Message temp = objectMapper.readValue(str,Message.class);
                    System.out.println(temp);
                    User target = null;
                    for(User user : users){
                        if(user.getName().equals(temp.getSendTo())){
                            target = user;
                            break;
                        }
                    }
                    template.convertAndSendToUser(target.getId(),"/queue",
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + temp.getUser() + ": " + temp.getValue());
                }
            } catch (IOException e) {
                client.clientListener.downService();
            }
        }
    }

    @Autowired
    public  WebSocketController(final SimpMessagingTemplate template) {
        this.template = template;
        client.start();
        new ReadMsg().start();
    }


    @MessageMapping("/send/message")
    public void onReceiveMessage(@Nullable final String message, Principal principal) throws IOException {
        Message temp = objectMapper.readValue(message,Message.class);
        if (temp.getValue().equals("/register")){
            User user = new User(temp.getUser(),principal.getName());
            users.add(user);
//            this.template.convertAndSendToUser(user.getId(),"/queue",
//                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + temp.getUser() + ": " + temp.getValue());
            System.out.println(user.getId() + " " + user.getName());

        }
        client.clientListener.send(message);
//        this.template.convertAndSendToUser(,"/chat",
//            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + temp.getUser() + ": " + temp.getValue());
//
    }
    //
}
