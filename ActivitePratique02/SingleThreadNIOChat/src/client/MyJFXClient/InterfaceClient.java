package client.MyJFXClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class InterfaceClient implements Initializable {
    @FXML
    private Button btn_send;
    @FXML
    private TextField txt_message;
    @FXML
    private VBox vbox_message;
    @FXML
    private ScrollPane conversation_main;

    private Client client;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
            try {
                client = new Client(new Socket("localhost", 1234));
                System.out.println("Client connected to server");
            } catch (Exception e) {
                e.printStackTrace();
            }
            vbox_message.heightProperty().addListener((observable, oldValue, newValue) ->
                    conversation_main.setVvalue((Double) newValue));
            client.receiveMessage(vbox_message);


            btn_send.setOnAction(event -> {
                String message = txt_message.getText();
                if (!message.isEmpty()) {
                    client.sendMessageToServer(message);
                    if (message.contains("=>")) {
                        String[] split = message.split("=>",2);
                        message = split[1] + " (to" + split[0]+")";
                    }
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    TextFlow textFlow = new TextFlow();
                    textFlow.setStyle("-fx-color: #000000FF; " +
                            "-fx-background-radius: 20px; " +
                            "-fx-background-color: rgb(189,190,190);");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    Text text = new Text(message);
                    text.setFill(Color.color(0.934,0.945,0.966));
                    vbox_message.getChildren().add(hBox);
                    hBox.getChildren().add(textFlow);
                    textFlow.getChildren().add(text);
                    txt_message.clear();
                }
            });
    }
    public static void addTitle(String messageToServer, VBox vbox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        TextFlow textFlow = new TextFlow();
        textFlow.setStyle("-fx-color: rgb(0,0,0); " +
                "-fx-font-weight: bolder;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        Text text = new Text(messageToServer);
        textFlow.getChildren().add(text);
        hBox.getChildren().add(textFlow);
        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }
    public static void addLabel(String messageToServer, VBox vbox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        if (!messageToServer.contains("> ")){
            TextFlow textFlow = new TextFlow();
            textFlow.setStyle("-fx-color: rgb(259,242,255); " +
                    "-fx-background-radius: 20px; " +
                    "-fx-background-color: rgb(15,125,242);");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            Text text = new Text(messageToServer);
            text.setFill(Color.color(0.934,0.945,0.966));
            textFlow.getChildren().add(text);
            hBox.getChildren().add(textFlow);
            return;
        }else {
            String[] words = messageToServer.split("> ",2);
            TextFlow textFlow = new TextFlow();
            textFlow.setStyle("-fx-color: rgb(259,242,255); " +
                    "-fx-background-radius: 20px; " +
                    "-fx-background-color: rgb(15,125,242);");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            Text user = new Text(words[0]);
            user.setStyle("-fx-font-style: Italic; " +
                    "-fx-font-size: 10px; " +
                    "-fx-fill: rgb(147,147,147);");
            Text text = new Text(words[1]);
            hBox.getChildren().add(user);
            text.setFill(Color.color(0.934,0.945,0.966));
            textFlow.getChildren().add(text);
            hBox.getChildren().add(textFlow);
        }


        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }
}
