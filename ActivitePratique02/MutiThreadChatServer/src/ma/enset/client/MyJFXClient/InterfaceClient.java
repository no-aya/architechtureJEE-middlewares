package ma.enset.client.MyJFXClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
                //client.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    conversation_main.setVvalue((Double) newValue);
                }
            });
            client.receiveMessage(vbox_message);
            btn_send.setOnAction(event -> {
                String message = txt_message.getText();
                if (!message.isEmpty()) {
                    client.sendMessageToServer(message);

                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    TextFlow textFlow = new TextFlow();
                    textFlow.setStyle("-fx-color: rgb(259,242,255); " +
                            "-fx-background-radius: 20px; " +
                            "-fx-background-color: rgb(15,125,242);");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    Text text = new Text(message);
                    text.setFill(Color.color(0.934,0.945,0.966));
                    vbox_message.getChildren().add(hBox);

                    client.sendMessageToServer(message);
                    txt_message.clear();
                }
            });
    }
    public static void addLabel(String messageToServer, VBox vbox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        TextFlow textFlow = new TextFlow();
        textFlow.setStyle("-fx-color: rgb(259,242,255); " +
                "-fx-background-radius: 20px; " +
                "-fx-background-color: rgb(15,125,242);");
        textFlow.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(messageToServer);
        text.setFill(Color.color(0.934,0.945,0.966));
        textFlow.getChildren().add(text);
        hBox.getChildren().add(textFlow);

        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }
}
