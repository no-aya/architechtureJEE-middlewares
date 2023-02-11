package FrameworkIOC.metier;

public interface IContext {
    void unmarshallXML();
    Object getBean(String id);
}
