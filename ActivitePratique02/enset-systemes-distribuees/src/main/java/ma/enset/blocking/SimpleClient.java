package ma.enset.blocking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("localhost",1234);
        InputStream is=socket.getInputStream();
        OutputStream os=socket.getOutputStream();
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter a number:");
        int nb=scanner.nextInt();
        os.write(nb);
        System.out.println("Waiting for result...");
        int result=is.read();
        System.out.println("Result is: "+result);
        socket.close();
    }
}
