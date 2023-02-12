package enset.presentation;


import FrameworkIOC.metier.Context;
import FrameworkIOC.metier.ContextAnn;
import enset.metier.IMetier;

public class PreSpringAnn {
    public static void main(String[] args) {
        Context context = new ContextAnn("enset.ext","enset.metier");
        IMetier metier = (IMetier) context.getBean("metier");
        System.out.println(metier.calcul());
}
}
