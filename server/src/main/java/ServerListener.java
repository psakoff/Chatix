import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


class ServerListener extends Thread {

    private Socket socket;
    protected BufferedReader in;
    protected BufferedWriter out;
    private String name;
    private String type;
    private ServerListener client;

    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;



    public ServerListener(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //Server.history.printHistory(out);
        start();
    }
    @Override
    public void run() {
        String word;
        this.client = null;

        try {
            initialize();
            try {
                while (true) {
                    word = in.readLine();

                    if(word.endsWith("/disconnect")) {
                        this.downService();
                        time = new Date();
                        dt1 = new SimpleDateFormat("HH:mm:ss");
                        dtime = dt1.format(time);
                        System.out.println("("+ dtime + ")"+ name + type + "has disconnected from chat");
                        break;
                    }
                    if (this.client!=null)
                    {client.send(word);
                    this.send(word);
                    System.out.println(" in room with " + client.name + " and " + this.name + "Echoing: " + word);
                    }
                    if(Server.serverList.contains(this)) {
                        if (word.endsWith("/connect")) {
                            CreateRoom(ServerListener.this);
                        }
                        System.out.println("Echoing: " + word);
                            //Server.history.addHistoryEl(word);
                            //for (ServerListener client : Server.serverList) {
                                //client.send(word);
                            // }
                    }
                }
            } catch (NullPointerException ignored) {} catch (IOException e1) {
            e1.printStackTrace();
        }


    } catch (IOException e) {
            this.downService();
        }
    }


    protected void initialize() throws IOException {
        String temp;
        temp = in.readLine();
        type = temp;
        temp = in.readLine();
        name = temp;
        time = new Date();
        dt1 = new SimpleDateFormat("HH:mm:ss");
        dtime = dt1.format(time);
        System.out.println("("+ dtime + ")"+ name + type + "has connected to chat");
        if (type.charAt(0) == 'a') {
            Server.agentList.add(this);
//            try {
//                out.write(type + " " + name + "\n");
//                out.flush();
//            } catch (IOException ignored) {}
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }


    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerListener vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
    private void CreateRoom(ServerListener client) throws IOException {
        ServerListener target = Server.agentList.isEmpty() ? null  : Server.agentList.getFirst();
        if(target!=null){
            target.send("client created room with you");
            client.send("Connected with agent");
            Server.agentList.removeFirst();
            Server.serverList.remove(target);
            Server.serverList.remove(client);
            target.client = client;
            System.out.println(client.name + " has created room with agent "+ target.name);
            while (true) {
                String msg;
                msg = in.readLine();
                if(msg.endsWith("/leave")) {
                    Server.agentList.addLast(target);
                    Server.serverList.add(target);
                    Server.serverList.add(client);
                    target.client = null;
                    time = new Date();
                    dt1 = new SimpleDateFormat("HH:mm:ss");
                    dtime = dt1.format(time);
                    System.out.println("("+ dtime + ")"+ client.name + " has leaved room with agent "+ target.name);
                    return;
                }
                client.send(msg);
                target.send(msg);
                System.out.println(" in room with " + client.name + " and " + target.name + "Echoing: " + msg);
            }


            }else {
            client.send("No agents available. Try again later");
            return;
        }
        }
    }


