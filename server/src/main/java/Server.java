import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {

    public static final int PORT = 8080;
    public static LinkedList<ServerListener> serverList = new LinkedList<>();
    public static LinkedList<ServerListener> agentList = new LinkedList<>();
    public static HashMap <String,ServerListener> serverMap = new HashMap<String,ServerListener>();

    public static History history;

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        history = new History();
        System.out.println("Server is running");
        try {
            while (true) {

                Socket socket = server.accept();
                try {
                    serverList.add(new ServerListener(socket));
                } catch (IOException e) {

                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}