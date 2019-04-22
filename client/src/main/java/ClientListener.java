import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;



class ClientListener {


    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inputUser;
    private String addr;
    private int port;
    private String nickname;
    protected String type;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    private ObjectMapper objectMapper = new ObjectMapper();
    public ClientListener(Socket socket) {
        this.socket = socket;
    }

    protected void start() throws IOException{
        try {
            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.pressNickname();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientListener.this.downService();
        }

    }

    protected void pressNickname() {
        System.out.print("agent or client? input agent/client ");
        try {
            type = inputUser.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("input your name: ");
        try {
            nickname = inputUser.readLine();
            System.out.println("Hello " +type + " " + nickname + "\n");
            Message message = new Message(nickname,"console","/register",type);
            out.write(objectMapper.writeValueAsString(message)+"\n");
            System.out.println(objectMapper.writeValueAsString(message));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    protected void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    // нить чтения
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine();
                    Message message = objectMapper.readValue(str,Message.class);
                    if (message.getValue().equals("/disconnect")) {
                        System.out.println(objectMapper.writeValueAsString(message));
                        System.out.println("DISCONNECTED");
                        ClientListener.this.downService(); // харакири
                        break;
                    }
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    System.out.println("(" + dtime + ") " + message.getUser() + ": " + message.getValue());
                }
            } catch (IOException e) {
                ClientListener.this.downService();
            }
        }
    }

    // нить отправляющая сообщения
    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    userWord = inputUser.readLine();
                    Message message = new Message(nickname,"console",userWord,type);
                    if (userWord.equals("/disconnect")) {
                        out.write( objectMapper.writeValueAsString(message) +"\n");
                    } else {
                        out.write((objectMapper.writeValueAsString(message)) +"\n");
                    }
                    out.flush();
                } catch (IOException e) {
                    ClientListener.this.downService();

                }

            }
        }
    }
}