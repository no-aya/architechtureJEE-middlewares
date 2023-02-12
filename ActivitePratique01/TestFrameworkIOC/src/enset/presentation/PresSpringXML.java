package enset.presentation;


import FrameworkIOC.metier.Context;
import FrameworkIOC.metier.ContextXML;
import enset.metier.IMetier;

public class PresSpringXML {
    public static void main(String[] args)  {
        Context context = new ContextXML("config.xml");
        IMetier metier=(IMetier) context.getBean("metier");
        System.out.println(metier.calcul());

    }
}
