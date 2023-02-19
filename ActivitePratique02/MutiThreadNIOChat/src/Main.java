import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new SocketServer("localhost",1234).start();
    }
}