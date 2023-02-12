package enset.presentation;

import FrameworkIOC.metier.ContextAnn;
import enset.metier.IMetier;

public class PreSpringAnn {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        ContextAnn context = new ContextAnn("enset.metier","enset.dao");
        IMetier metier = (IMetier) context.getBean("metier");
        System.out.println(metier.calcul());
}
}
