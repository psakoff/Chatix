import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientListenerTest {
    @Test
    public void pressNickname() throws IOException {
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String SimulatedInput = "agent"+ System.getProperty("line.separator")+"pasha"+System.getProperty("line.separator")+ "/disconnect" +System.getProperty("line.separator");
        ByteArrayInputStream UserInput = new ByteArrayInputStream(SimulatedInput.getBytes());
        when(socket.getInputStream()).thenReturn(UserInput);
        when(socket.getOutputStream()).thenReturn(out);
        ClientListener client = new ClientListener(socket);
        System.setIn(new ByteArrayInputStream(SimulatedInput.getBytes()));
        client.start();
        assertThat(client.type, is("agent"));
    }
}