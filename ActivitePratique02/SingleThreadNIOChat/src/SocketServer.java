import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class SocketServer {
    private int clientNumber = 0;
    private Selector selector;
    private InetSocketAddress address;
    private List<SocketChannel> sessions;

    public SocketServer(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.sessions = new ArrayList<>();
    }

    public void start() {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server started at " + address);

            while (true) {
                //blocking wait for events
                this.selector.select();
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) continue;
                    if (key.isAcceptable()) accept(key);
                    else if (key.isReadable()) read(key);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        socketChannel.register(this.selector, SelectionKey.OP_READ);
        this.sessions.add(socketChannel);

        clientNumber++;
        System.out.println("Client connected from " + socketChannel.getRemoteAddress());

        broadcast("Client " + socketChannel.getRemoteAddress()  + " joined chat!"+System.lineSeparator(), socketChannel);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.write(ByteBuffer.wrap("Welcome client ".getBytes()));

        socketChannel.write(ByteBuffer.wrap(String.valueOf(socketChannel.getRemoteAddress()).getBytes()));
        socketChannel.write(ByteBuffer.wrap(System.lineSeparator().getBytes()));
        //serverSocketChannel.register(this.selector, SelectionKey.OP_WRITE);


    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = 0;
        read = socketChannel.read(buffer);
        if (read == -1) {
            this.sessions.remove(socketChannel);

            broadcast("Client " + socketChannel.getRemoteAddress() + " left chat!"+System.lineSeparator(), socketChannel);
            try {
                socketChannel.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            key.cancel();
            return;
        }
        String request = new String(buffer.array(), 0, read);

        broadcast(request, socketChannel);
        try {
            System.out.println("Received from " + socketChannel.getRemoteAddress() + " : " + request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcast(String message, SocketChannel sender) throws IOException {
        if (!message.contains("joined")) message = "Client " + sender.getRemoteAddress() + " > " + message;
        if (message.contains("=>")) {
            String[] parts = message.split("=>",2);
            String[] receivers = parts[0].split(",");
            for (SocketChannel session : this.sessions) {
                for (String receiver : receivers) {
                    if (session.getRemoteAddress().toString().equals("/"+receiver)) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        buffer.put(parts[1].getBytes());
                        buffer.flip();
                        try {
                            session.write(buffer);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

        }else {
            for (SocketChannel session : this.sessions) {
                if (session == sender) continue;
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put(message.getBytes());
                buffer.flip();
                try {
                    session.write(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
