package ma.enset.server;

import ma.enset.dao.Client;
import ma.enset.dao.Message;

import java.io.*;
import java.net.Socket;

import static ma.enset.server.MutithreadChatServer.clients;


class Conversation extends Thread{
    private Socket socket;
    private Client client=new Client();


    public Conversation(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //Code conversation
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            String IP = socket.getRemoteSocketAddress().toString();


            System.out.println("Client " + client.getId() + " connected from " + IP);
            //Get userName

            String name;
            while (true){
                name = br.readLine();
                for (Client client : clients) if (client.getName().equals(name)) name=null;
                if (name!=null && !name.isEmpty()) break;
                pw.println("Name already exist or empty\n " +
                        "Please enter another name :");
            }
            client=new Client(IP,name);
            clients.add(client);
            pw.println("Welcome " + client.getName());


            String request;
            while ((request = br.readLine()) != null) {
                System.out.println("Client " + client.getId() + " : " + request + " from " + IP);
                Message message = Message.parse(request);

                message.setSender(client.getId());
                System.out.println(message);
                broadcast(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void broadcast(Message message){
        try {
            for (Conversation conversation : MutithreadChatServer.conversations) {

               if (conversation.client.getId().equals(message.getSender())) continue;
                Socket socket = conversation.socket;
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                System.out.println("Sending to "+conversation.client.getName());
                pw.println(client.getName()+" : "+message.getContent());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}