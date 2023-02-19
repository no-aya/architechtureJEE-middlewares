package client.MyJFXClient;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    public Client(Socket socket) {
        this.socket = socket;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error Creating Client...");
            e.printStackTrace();
            close(socket,reader,writer);
        }
    }
    public void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (socket!=null) socket.close();
            if (bufferedReader!=null) bufferedReader.close();
            if (bufferedWriter!=null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessageToServer(String message){
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("Error Sending Message...");
            e.printStackTrace();
            close(socket,reader,writer);
        }
    }
    public void receiveMessage(VBox vbox_message){
        new Thread(()->{
            while (socket.isConnected()){
                try {
                    String messageFromServer = reader.readLine();
                    if (messageFromServer.startsWith("Welcome")){
                        //TODO: Add Welcome Label
                        InterfaceClient.addTitle(messageFromServer, vbox_message);
                        continue;
                    }
                    System.out.println("Message From Server: "+messageFromServer);
                    InterfaceClient.addLabel(messageFromServer, vbox_message);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error Receiving Message...");
                    close(socket,reader,writer);
                    break;
                }

            }
        }).start();
    }
}
