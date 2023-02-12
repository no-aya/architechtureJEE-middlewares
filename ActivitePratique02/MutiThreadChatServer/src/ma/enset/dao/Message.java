package ma.enset.dao;

import java.util.ArrayList;
import java.util.Arrays;

public class Message {
    String sender;
    ArrayList<String> receiver;
    String content;

    public Message(){
    }
    public Message(String sender, ArrayList<String> receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
    public static Message parse(String message){
        if (message == null || message.isEmpty()) return null;
        if (!message.contains("=>")) return new Message(null,null,message);
        String[] parts = message.split("=>",2);
        System.out.println(Arrays.toString(parts));
        String[] receivers = parts[0].split(",");
        ArrayList<String> receiver = new ArrayList<>();
        for (String r:receivers) {
            receiver.add(r);
        }
        String content = parts[1];
        return new Message(null,receiver,content);
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ArrayList<String> getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                '}';
    }
}
