import FrameworkIOC.metier.Context;
import FrameworkIOC.metier.ContextXML;

public class Test {
    public static void main(String[] args) {
        Context context = new ContextXML("config.xml");
    }
}
