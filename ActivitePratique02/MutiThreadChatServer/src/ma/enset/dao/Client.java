package ma.enset.dao;


public class Client {
    private String id;
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
