package ma.enset.dao;


public class Client {
    private String id;
    // TODO:
    //  Pour s'assurer qu'il n'y aura pas de collision d'adresse on peut les stocker dans une BD
    private String name;

    //Generate ID
    public Client() {
    }

    public Client(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
