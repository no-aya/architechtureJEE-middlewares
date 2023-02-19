package client;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

public class SimpleChatClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socket = setUpNetworking();
    }

    private static SocketChannel setUpNetworking() throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 1234));
        System.out.println("Networking established. Local address: " + socketChannel.getLocalAddress());
        return socketChannel;
    }
}

