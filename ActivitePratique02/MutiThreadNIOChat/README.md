# Non-Blocking I/O Chat Server
On cherche à réaliser un serveur de  en JAVA, similaire au précédent mais cette fois non bloquant.
## Partie Serveur
### Principe
Le principe est le même que pour le serveur bloquant, à savoir que le serveur doit pouvoir gérer plusieurs clients en même temps. Pour cela, nous allons utiliser des threads. Chaque client sera géré par un thread différent.

La différence avec le serveur bloquant est que le serveur ne doit pas bloquer lorsqu'il attend une réponse d'un client. Pour cela, nous allons utiliser des sockets non bloquantes. Nous allons donc utiliser la classe `SocketChannel` au lieu de la classe `Socket`. 

On utilise aussi un `Selector` pour gérer les sockets. Le `Selector` va nous permettre de savoir si une socket est prête à être lue ou écrite.

Les étapes du serveur sont les suivantes :
* On crée un `Selector` et on l'associe à un thread.
* On crée un `ServerSocketChannel` et on l'associe au `Selector`.
* On lance le thread du `Selector`.
* On attend que le `Selector` nous indique qu'une socket est prête à être lue.
* On lit la socket et on traite la requête.
* On écrit la réponse dans la socket.
* On ferme la socket.
* On recommence.

![nio-tutorial15](https://user-images.githubusercontent.com/106016869/219957646-a1a10472-1be1-40e9-866d-aea0912c69fc.png)


### Classe SoketServer 
La classe contient les méthodes permettant de gérer les sockets non bloquantes. Elle contient les méthodes suivantes :
* `public void start()` : Cette méthode permet de lancer le serveur.
* `private void accept(SelectionKey key)` : Cette méthode permet d'accepter une connexion d'un client.
* `private void read(SelectionKey key)` : Cette méthode permet de lire une socket.
* `private void broadcast(String message, SocketChannel sender)` : Cette méthode permet d'envoyer un message à tous les clients, sauf le client qui a envoyé le message. Ona définit un protocole pour savoir qui est l'émetteur du message à travers le symbole `=>`. Par exemple, si le client 1 envoie un message au client 2, le message sera `1=>message`. Le client 2 saura alors que le message vient du client 1.

### Code des méthodes
#### start()
```java
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
```
Ce code permet de lancer le serveur. Il crée un `Selector` et un `ServerSocketChannel`. Il associe le `ServerSocketChannel` au `Selector` et il l'associe à l'événement `OP_ACCEPT`. Il lance le thread du `Selector` et il attend que le `Selector` nous indique qu'une socket est prête à être lue. Lorsque le `Selector` nous indique qu'une socket est prête à être lue, on lit la socket et on traite la requête. On écrit la réponse dans la socket et on ferme la socket. On recommence.

#### accept()
```java
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        socketChannel.register(this.selector, SelectionKey.OP_READ);
        this.sessions.add(socketChannel);
        
        System.out.println("Client connected from " + socketChannel.getRemoteAddress());

        broadcast("Client " + socketChannel.getRemoteAddress()  + " joined chat!"+System.lineSeparator(), socketChannel);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.write(ByteBuffer.wrap("Welcome client ".getBytes()));

        socketChannel.write(ByteBuffer.wrap(String.valueOf(socketChannel.getRemoteAddress()).getBytes()));
        socketChannel.write(ByteBuffer.wrap(System.lineSeparator().getBytes()));


    }
```
Cette méthode permet d'accepter une connexion d'un client. Elle crée un `SocketChannel` et elle l'associe au `Selector` et elle l'associe à l'événement `OP_READ`. Elle ajoute le `SocketChannel` à la liste des sessions. Elle envoie un message de bienvenue au client. Elle envoie aussi un message à tous les clients pour leur dire que le client vient de se connecter.

Note : On utilisera les addresses des sockets pour identifier les clients pour le moment.

#### read()
```java
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
```
Cette méthode permet de lire une socket. Elle lit la socket et elle traite la requête. Elle envoie la réponse à tous les clients. Elle envoie aussi un message à tous les clients pour leur dire que le client vient de se déconnecter.

#### broadcast()
```java
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
```
Cette méthode permet d'envoyer un message à tous les clients, sauf le client qui a envoyé le message. Ona définit un protocole pour savoir qui est l'émetteur du message à travers le symbole `=>`. Par exemple, si le client 1 envoie un message au client 2, le message sera `1=>message`. Le client 2 saura alors que le message vient du client 1.

### Exécution du serveur main()
```java
public class Main {
    public static void main(String[] args) {
        new SocketServer("localhost",1234).start();
    }
}
```
Grâce au constructeur de la classe `SocketServer`, on peut lancer le serveur en spécifiant l'adresse et le port.

Voici le résultat de l'exécution du serveur :

![image](https://user-images.githubusercontent.com/106016869/219957709-31b25940-b585-471b-b26b-41ddca4d425d.png)

## Partie Client
Nous allons reprendre le code du client de la partie précédente. Nous allons juste ajouter la possibilité de se connecter à un serveur.

### Client Telnet 
![image](https://user-images.githubusercontent.com/106016869/219957762-b3856789-7e5b-4296-b64a-6516541151bd.png)

### Client JavaFX
![image](https://user-images.githubusercontent.com/106016869/219957807-0752b91c-9844-41e2-b84e-39363d0834ec.png)

### Client Python
![image](https://user-images.githubusercontent.com/106016869/219957876-b3192720-99e7-4425-b3b8-4d98e31554dc.png)



