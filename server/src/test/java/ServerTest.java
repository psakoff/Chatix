import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class ServerTest {

    @Test
    public void main() throws IOException {
        final int PORT = 8080;
        ServerSocket server = new ServerSocket(PORT);
        assertTrue(server.getLocalPort()== PORT);
    }
}