# Mutithread Chat Server with Blocking I/O
## Description
On cherche à développer un serveur de chat qui permet à plusieurs clients de communiquer entre eux. Ceci grâce à plusieurs threads qui gèrent les connexions des clients.

Nous allons aussi créer différents types de clients qui vont communiquer avec le serveur de chat : client telnet, client JavaFX et client Python.

## Partie Serveur
On définit la classe `MultithreadChatServer` qui va gérer les connexions des clients. On utilise un thread pour chaque client.

Cette classe hérite de la classe `Thread`.

### Sous classe Conversation
On définit la classe `Conversation`. 

Cette classe hérite de la classe `Thread`. Elle contient un attribut `socket` de type `Socket` qui représente la socket du client. Elle contient aussi un attribut `clientNumber` de type `int` qui représente l'identifiant du client.

Lorsqu'on crée une instance de cette classe, on lui passe en paramètre la socket du client et son identifiant. On définit aussi la méthode `run` qui instancie des I/O streams pour la socket. Donc chaque client aura son propre thread qui gère les I/O.

```java
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
                    System.out.println("Client "+clientNumber+" : "+request +" from "+IP);
                    broadcast(request,this);
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }


        }
    }
```
Ceci nous servira ultérieurement pour la gestion des messages.

### Attributs
On définit les attributs suivants :
- `private int clientNumber` : le nombre de clients connectés
- `private List<Conversation> conversations` : la liste des conversations en cours

Avec la méthode `main` pour lancer le serveur.

```java
    public static void main(String[] args) {
        MultithreadChatServer server = new MultithreadChatServer();
        server.start();
    }
```

### Méthodes
On définit les méthodes suivantes :
- `public void run()` : méthode qui lance le serveur
- `public void broadcast(String message, Conversation conversation)` : méthode qui permet d'envoyer un message à tous les clients connectés

#### Méthode run()
Cette méthode va créer une socket sur le port 1234. On va ensuite créer un thread pour chaque client qui se connecte. On va aussi incrémenter le nombre de clients connectés.

```java
@Override
public void run() {
    System.out.println("Starting server at port 1234");
    try {
        ServerSocket serverSocket = new ServerSocket(1234);
        while(true){
            System.out.println("Waiting for client...");
            Socket socket = serverSocket.accept();
            ++clientNumber;
            Conversation conversation= new Conversation(socket,clientNumber);
            conversations.add(conversation);
            conversation.start();
        }
    }catch (IOException e){
        throw new RuntimeException(e);
    }
}
```

#### Méthode broadcast()
Cette méthode va envoyer un message à tous les clients connectés sauve celui qui l'a envoyé.
On définit aussi un protocole pour la communication entre le client et le serveur. On utilise le caractère ` : ` pour séparer le nom du client de son message. Pour envoyer un message à un ou plusieurs clients en particulier, on utilise le caractère ` => ` pour séparer le nom du client de son message.

```java
    public void broadcast(String message, Conversation from){
        try {
            if(message.contains("=>")){
                String[] privateMessage = message.split("=>",2);
                String[] receiver = privateMessage[0].split(",");
                String messageToSend = privateMessage[1];
                for(Conversation conversation:conversations){
                    for(String s:receiver){
                        if(conversation.clientNumber==Integer.parseInt(s)){
                            Socket socket=conversation.socket;
                            OutputStream os = socket.getOutputStream();
                            PrintWriter pw = new PrintWriter(os,true);
                            pw.println(from.clientNumber+" : "+privateMessage[1]);
                        }
                    }
                }
            }else {
                for(Conversation conversation:conversations){
                    if(conversation==from) continue;
                    Socket socket=conversation.socket;
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os,true);
                    pw.println(from.clientNumber+" : "+message);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
```

L'éxecution donne ceci :
![image](https://user-images.githubusercontent.com/106016869/219961904-48d8eea9-7038-4f7f-9022-d8d83f39e860.png)

## Partie Client

### Client Telnet
On définit la classe `MyTelnetClient` qui se connecte au serveur de chat. 

On définit aussi la méthode `main` pour lancer le client.

```java
public class MyTelnetClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            InputStream is = socket.getInputStream();
            InputStreamReader isr= new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os,true);

            new Thread(()->{
                try {
                    String response;
                    while((response= br.readLine())!=null){
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            Scanner scanner = new Scanner(System.in);
            while(true){
                String request = scanner.nextLine();
                pw.println(request);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
```

L'exécution donne ceci :

![image](https://user-images.githubusercontent.com/106016869/219961952-807bbbb3-e935-423e-a2ec-653afb4a178f.png)

### Client JavaFX
On crée une interface graphique pour le client. On définit la classe `MyJFXClient.InterfaceClient` qui controle l'interface `interafceClient.xml`.

Voici le désign de l'interface :

![image](https://user-images.githubusercontent.com/106016869/219961981-de24874f-f073-4a8c-b9d0-d5854c6d1b47.png)


Et voici le code de la classe `InterfaceClient` :

```java
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

        String[] words = messageToServer.split(": ",2);

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

        Platform.runLater(() -> vbox.getChildren().add(hBox));
    }
}
```
A chaque fois qu'un message est reçu ou envoyé, on l'affiche dans la `vbox_message` avec la méthode `addLabel`. 
Si le message est entrant, on affiche le nom de l'utilisateur qui a envoyé le message et le message lui-même dans une box blue à gauche.
Si le message est sortant, on affiche le message dans une box grise à droite.

Le message d'accueil est affiché avec la méthode `addTitle`.

L'appel à ce client se fait dans la méthode `main` de la classe `MyJFXClient.Main` :

```java
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("InterfaceClient.fxml"));
        primaryStage.setTitle("Client JavaFX");
        Scene scene = new Scene(root,478,396);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
```
On exécute et on obtient ceci

![Screenshot 2023-02-19 174442](https://user-images.githubusercontent.com/106016869/219962216-e872b7e5-36b9-4b72-a3a7-4337ebadcff2.png)


### Client Python
On crée un client en python simple qui se connecte au serveur Java. 

On définit le script `client.py`.
Ce script import les 2 modules `threading` et `socket`.

On définit aussi 2 méthodes :
- `client_receive` qui permet de recevoir les messages du serveur
- `client_send` qui permet d'envoyer des messages au serveur


```python
import threading
import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('localhost', 1234))

def client_receive():
    while True:
        try: 
            message = client.recv(1024).decode('utf-8')[:-1]
            print(message)
        except:
            print('Error!')
            client.close()
            break
def client_send():
    while True:
        msg = input()
        client.send(msg.encode('utf-8')+b'\n')

receive_thread = threading.Thread(target=client_receive)
send_thread = threading.Thread(target=client_send)

receive_thread.start()
send_thread.start()
```
On execute le script `client.py` dans un terminal avec la commande `python client.py`.

![image](https://user-images.githubusercontent.com/106016869/219962270-51204d55-c825-4d7b-bdcb-46d34867bf7e.png)

## Demonstration

https://user-images.githubusercontent.com/106016869/219962679-6d9fb48f-9e2d-4b23-b12b-1be48e4fa85c.mp4



