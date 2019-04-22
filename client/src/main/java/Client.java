import java.io.IOException;
import java.net.Socket;

public class Client {
    private static ClientListener clientListener;


    public static void main(String[] args) throws IOException {
        try {
            Socket socket = new Socket("localhost", 8081);
            clientListener = new ClientListener(socket);
            clientListener.start();
        } catch (IOException e) {
            System.err.println("Socket failed");
        }

    }
}