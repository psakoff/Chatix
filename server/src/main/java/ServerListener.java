import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

//он хочет все сделать приватным, но я тогда не смогу сделать тесты
@SuppressWarnings("ALL")
class ServerListener extends Thread {
    protected Socket socket;
    protected BufferedReader in;
    protected BufferedWriter out;
    protected String name;
    protected String type;
    protected User contact;
    protected Date time;
    protected String dtime;
    protected SimpleDateFormat dt1;
    ObjectMapper objectMapper = new ObjectMapper();


    protected ServerListener(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        //  Server.history.printHistory(out);
        Server.userList.add(new User("SERVER", "server", "server", null));
        start();
    }

    @Override
    public void run() {
        String word;
        try {
            while (true) {
                word = in.readLine();
                Message message = objectMapper.readValue(word, Message.class);

                time = new Date();
                dt1 = new SimpleDateFormat("HH:mm:ss");
                dtime = dt1.format(time);
                System.out.println("Echoing: (" + dtime + ") " + message.getUser() + ": " + message.getValue());

                    if (message.getValue().equals("/register")) {
                        initialize(message);
                    }
                    User user = getUser(message);
                    if (message.getPlatform().equals("web")) {
                    message.setSendTo(user.getName());
                    this.send(objectMapper.writeValueAsString(message));
                    }
                    if (user.getContact() == null) {
                    if (message.getValue().equals("/disconnect")) {
                        System.out.println(this.type + " " + this.name + " has disconnected");
                        Message disc = new Message("SERVER", "server", "/disconnect", "server");
                        message.setSendTo(user.getName());
                        this.send(objectMapper.writeValueAsString(disc));
                        Server.serverList.remove(this);
                        if (this.type.equals("agent")) {
                            synchronized (Server.agentList) {
                                Server.agentList.remove(this);
                            }
                        }
                    }
                    if (message.getValue().equals("/connect")) {
                        if (user.getType().equals("agent")) {
                            Message temp = new Message("SERVER", "server",
                                    "you are agent and can not create rooms", "server");
                            sendToUser(user, temp);
                        } else {
                            connect(user);
                        }
                    }
                }else{
                    if (message.getValue().equals("/leave")) {
                        if (user.getType().equals("agent")) {
                            Message temp = new Message("SERVER", "server",
                                    "you are agent and can not leave rooms by yourself", "server");
                            sendToUser(user, temp);
                        } else {
                            leave(user);
                        }
                    }else{
                    sendToUser(user,message);
                    sendToUser(user.getContact(),message);
                    }
                    System.out.println(" in room with " + user.getContact() + " and " + user.getName() + " Echoing: (" + dtime + ") "  + message.getUser() + ": " + message.getValue());
                }
            }

//                    if (this.type.equals("agent")){
//                    if (this.client!=null) {
//                        message.setSendTo(this.client.name);
//                        client.send(objectMapper.writeValueAsString(message));
//                        message.setSendTo(this.name);
//                        this.send(objectMapper.writeValueAsString(message));
//                        time = new Date();
//                        dt1 = new SimpleDateFormat("HH:mm:ss");
//                        dtime = dt1.format(time);
//                        System.out.println(" in room with " + client.name + " and " + this.name + " Echoing: (" + dtime + ") "  + message.getUser() + ": " + message.getValue());
//                    }
//
//                    }
//                    if(Server.serverList.contains(this)) {
//                        if (message.getValue().equals("/connect")) {
//                            if (this.type.equals("agent")) {
//                                Message temp = new Message("SERVER","server",
//                                        "you are agent and can not create rooms","server" );
//                                message.setSendTo(this.name);
//                                this.send(objectMapper.writeValueAsString(temp));}
//                            else {
//                               // CreateRoom(ServerListener.this);
//                                User owner = null;
//                                Boolean f = false;
//                                synchronized (Server.userList){
//                                    for (User user : Server.userList) {
//                                        if (user.getName().equals(message.getUser())) {
//                                            owner = user;
//                                            f = true;
//                                            break;
//                                        }
//                                    }
//                                }
//                                if (f==false){
//                                    Message msg = new Message("SERVER","server",
//                                            "No agents available. Try again later","server" );
//                                    msg.setSendTo(client.name);
//                                    client.send(objectMapper.writeValueAsString(msg));
//                                }
//                                if(owner!=null){
//                                    connect(owner);
//                                }
//
//                            }
//                        }
////                        time = new Date();
////                        dt1 = new SimpleDateFormat("HH:mm:ss");
////                        dtime = dt1.format(time);
////                        System.out.println("Echoing: (" + dtime + ") " +  word);
//                            //Server.history.addHistoryEl(word);
//                            //for (ServerListener client : Server.serverList) {
//                                //client.send(word);
//                            // }
//                    }
//            }
        } catch (NullPointerException e) { //TO DO fix exceptions
            e.printStackTrace();
        } catch (IOException e1) {
            //System.out.println(this.type+ " " + this.name +" has disconnected" );
            e1.printStackTrace();
        }
    }

    protected User getUser(Message message) {
        User temp = null;
        Boolean f = false;
        synchronized (Server.userList) {
            for (User user : Server.userList) {
                if (user.getName().equals(message.getUser())) {
                    temp = user;
                    f = true;
                    break;
                }
            }
        }
        if (f == false) {
            return null;
        }
        return temp;
    }

    protected void initialize(Message message) throws IOException {
        type = message.getTypeOfUser();
        name = message.getUser();
        User currentUser = new User(name, type, message.getPlatform(), this);
        synchronized (currentUser) {
            Boolean flag = true;
            for (User user : Server.userList) {
                if (currentUser.getName().equals(user.getName())) {
                    flag = false;
                }
            }
            if (flag == true) {
                Server.userList.add(currentUser);
            }
        }
        time = new Date();
        dt1 = new SimpleDateFormat("HH:mm:ss");
        dtime = dt1.format(time);
        System.out.println("(" + dtime + ")" + name + " " + type + " has connected to chat");
        if (type.charAt(0) == 'a') {
            Server.agentList.add(getUser(message));
//            try {
//                out.write(type + " " + name + "\n");
//                out.flush();
//            } catch (IOException ignored) {}
        }
    }

    protected void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }

    protected void sendToUser(User user, Message message) throws JsonProcessingException {
        message.setSendTo(user.getName());
        user.getServerListener().send(objectMapper.writeValueAsString(message));
    }


    protected void downService() {
        ServerListener temp = null;
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerListener vr : Server.serverList) {
                    if (vr.equals(this)) temp = vr;
                }
                temp.interrupt();
                Server.serverList.remove(temp);
            }
        } catch (IOException ignored) {
        }
    }


    protected void connect(User client) throws JsonProcessingException {
        Boolean f = false;
        User target;
        synchronized (Server.agentList) {
            target  = Server.agentList.isEmpty() ? null : Server.agentList.getFirst();
        }
        if (target == null) {
            Message msg = new Message("SERVER", "server",
                    "No agents available. Try again later", "server");
            client.getServerListener().sendToUser(client, msg);
        }
        if (target != null) {
            client.setContact(target);
            target.setContact(client);
            Message temp = new Message("SERVER","server",
                    "client created room with you","server" );
            sendToUser(target,temp);
            temp.setValue("Connected with agent");
            sendToUser(client,temp);
            synchronized (Server.agentList) {
               Server.agentList.removeFirst();
            }
        }
    }
    protected void leave(User user) throws JsonProcessingException {
        User target = user.getContact();
        user.setContact(null);
        target.setContact(null);
        Message temp = new Message("SERVER","server",
                "client leaved room","server" );
        sendToUser(target,temp);
        temp.setValue("Leaved");
        sendToUser(user,temp);
        Server.agentList.addLast(target);
    }
}








//    protected void CreateRoom(ServerListener client) throws IOException {
//        ServerListener target;
//        synchronized (Server.agentList) {
//            target  = Server.agentList.isEmpty() ? null : Server.agentList.getFirst();
//        }
//        if(target!=null){
//            Message temp = new Message("SERVER","server",
//                    "client created room with you","server" );
//            temp.setSendTo(target.name);
//            target.send(objectMapper.writeValueAsString(temp));
//            temp.setValue("Connected with agent");
//            temp.setSendTo(client.name);
//            client.send(objectMapper.writeValueAsString(temp));
//            synchronized (Server.agentList) {
//                Server.agentList.removeFirst();
//            }
//            synchronized (Server.serverList) {
//                Server.serverList.remove(target);
//                Server.serverList.remove(client);
//            }
//            target.client = client;
//            System.out.println(client.name + " has created room with agent "+ target.name);
//            while (true) {
//                String msg;
//                msg = in.readLine();
//                Message message = objectMapper.readValue(msg,Message.class);
//                if(message.getValue().equals("/leave")) {
//                    time = new Date();
//                    dt1 = new SimpleDateFormat("HH:mm:ss");
//                    dtime = dt1.format(time);
//                    temp.setValue(client.name + " has leaved your room, so you are in the end of queue");
//                    temp.setSendTo(target.name);
//                    target.send(objectMapper.writeValueAsString(temp));
//                    Server.agentList.addLast(target);
//                    Server.serverList.add(target);
//                    Server.serverList.add(client);
//                    target.client = null;
//                    time = new Date();
//                    dt1 = new SimpleDateFormat("HH:mm:ss");
//                    dtime = dt1.format(time);
//                    System.out.println("("+ dtime + ")"+ client.name + " has leaved room with agent "+ target.name);
//                    return;
//                }
////                client.send(msg);
//                temp.setSendTo(target.name);
//                target.send(objectMapper.writeValueAsString(message));
//                temp.setSendTo(client.name);
//                client.send(objectMapper.writeValueAsString(message));
//                System.out.println("in room with " + client.name + " and " + target.name + "Echoing: " + msg);
//            }
//
//
//            }else {
//            Message temp = new Message("SERVER","server",
//                    "No agents available. Try again later","server" );
//            temp.setSendTo(client.name);
//            client.send(objectMapper.writeValueAsString(temp));
//            return;
//        }
//        }
//    }


