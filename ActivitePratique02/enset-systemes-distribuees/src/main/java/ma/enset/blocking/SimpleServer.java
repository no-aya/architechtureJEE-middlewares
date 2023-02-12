package ma.enset.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {

        //Démarrage du server
        ServerSocket ss=new ServerSocket(1234);
        System.out.println("Waiting for client...");

        Socket socket=ss.accept();  //Instruction bloquante

        //Chargement des tubes de communication
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        System.out.println("Waiting for number...");
        int nb=is.read();
        int result=nb*nb;
        System.out.println("Sending result...");
        os.write(result);

        socket.close();

    }
}
