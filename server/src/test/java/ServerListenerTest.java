import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ServerListenerTest {
    ServerSocket server;
    @Test
    public void run() throws IOException { // mock server socket
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String SimulatedInput = "agent"+ System.getProperty("line.separator")+"pasha"+System.getProperty("line.separator");
        ByteArrayInputStream in = new ByteArrayInputStream(SimulatedInput.getBytes());
        when(socket.getInputStream()).thenReturn(in);
        when(socket.getOutputStream()).thenReturn(out);
        server = new ServerSocket(8080);
        ServerListener that = new ServerListener(socket);
        Server.serverList.add(that);
        assertThat(that.type, is("agent"));
    }

    @Test
    public void send() throws IOException {
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String SimulatedInput = "agent"+ System.getProperty("line.separator")+"pasha"+System.getProperty("line.separator");
        ByteArrayInputStream in = new ByteArrayInputStream(SimulatedInput.getBytes());
        when(socket.getInputStream()).thenReturn(in);
        when(socket.getOutputStream()).thenReturn(out);
        server = new ServerSocket(8080);
        ServerListener that = new ServerListener(socket);
        Server.serverList.add(that);
        that.send("hello");
        String message = out.toString();
        assertEquals(message.intern(), "hello\n");
    }

    @Test
    public void downService() throws IOException {
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String SimulatedInput = "agent"+ System.getProperty("line.separator")+"pasha"+System.getProperty("line.separator");
        ByteArrayInputStream in = new ByteArrayInputStream(SimulatedInput.getBytes());
        when(socket.getInputStream()).thenReturn(in);
        when(socket.getOutputStream()).thenReturn(out);
        server = new ServerSocket(8080);
        ServerListener that = new ServerListener(socket);
        Server.serverList.add(that);
        System.out.println(Server.serverList.toString());
        assertTrue(Server.serverList.contains(that));
        that.downService();
        System.out.println(Server.serverList.toString());
        assertFalse(Server.serverList.contains(that));
    }

}