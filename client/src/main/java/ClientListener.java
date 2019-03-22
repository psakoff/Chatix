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
    private String type;
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;

    public ClientListener(String addr, int port) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname();
            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            ClientListener.this.downService();
        }

    }



    private void pressNickname() {
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
            out.write(type + "\n");
            out.flush();
            out.write(nickname + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void downService() {
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
                    if (str.equals("/disconnect")) {
                        ClientListener.this.downService(); // харакири
                        break;
                    }
                    System.out.println(str);
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
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    userWord = inputUser.readLine();
                    if (userWord.equals("/disconnect")) {
                        out.write("/disconnect" + "\n");
                        ClientListener.this.downService();
                        break;
                    } else {
                        out.write("(" + dtime + ") " + nickname + ": " + userWord + "\n");
                    }
                    out.flush();
                } catch (IOException e) {
                    ClientListener.this.downService();

                }

            }
        }
    }
}