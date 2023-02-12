package ma.enset.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MutithreadChatServer extends Thread{

    private List<Conversation> conversations=new ArrayList<>();

    int clientNumber = 0;
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
            while(true){
                System.out.println("Waiting for client...");
                Socket socket = serverSocket.accept();
                ++clientNumber;
                Conversation conversation= new Conversation(socket,clientNumber);
                conversations.add(conversation);
                conversation.start();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }
    class Conversation extends Thread{
        private Socket socket;
        int clientNumber;

        public Conversation(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
        }

        @Override
        public void run() {
            //Code conversation
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr= new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os,true);
                String IP = socket.getRemoteSocketAddress().toString();
                System.out.println("Client "+clientNumber+" connected from "+IP);
                pw.println("Welcome client "+clientNumber);


                String request;
                while((request = br.readLine())!=null){
                    System.out.println("Client "+clientNumber+" : "+request +" from "+IP);
                    broadcast(request,this);
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }


        }
    }
    public void broadcast(String message, Conversation from){
        try {
        for(Conversation conversation:conversations){
            if(conversation==from) continue;
            Socket socket=conversation.socket;
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os,true);
            pw.println(message);
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
}

