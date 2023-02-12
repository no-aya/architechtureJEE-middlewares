package ma.enset.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MutithreadBlockingServer extends Thread{
    int clientNumber = 0;
    public static void main(String[] args) {
        new  MutithreadBlockingServer().start();
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
                new Conversation(socket,clientNumber).start();
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
                    String response = "Size = "+request.length();
                    System.out.println("Client "+clientNumber+" sent: "+new String(request));
                    pw.println(response);
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }


        }
    }
}

