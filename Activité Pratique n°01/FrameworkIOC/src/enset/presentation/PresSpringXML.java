package enset.presentation;

import enset.metier.IMetier;
import FrameworkIOC.metier.ContextXML;

public class PresSpringXML {
    public static void main(String[] args) {
        ContextXML contextXML = new ContextXML("config.xml");
        IMetier metier = (IMetier) contextXML.getBean("metier");

        System.out.println(metier.calcul());
    }
}
