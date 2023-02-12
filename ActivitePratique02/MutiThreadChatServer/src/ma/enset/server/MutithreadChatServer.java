package ma.enset.server;


import ma.enset.dao.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MutithreadChatServer extends Thread{
    static List<Conversation> conversations = new ArrayList<>();
    static protected List<Client> clients=new ArrayList<>();

    public static void main(String[] args) {
        new MutithreadChatServer().start();
        //Code interface graphique
    }
    @Override
    public void run() {
        //Code serveur
        System.out.println("Starting server at port 1234");
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (true) {
                System.out.println("Waiting for client...");
                Socket socket = serverSocket.accept();
                Conversation conversation = new Conversation(socket);
                conversations.add(conversation);
                conversation.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
