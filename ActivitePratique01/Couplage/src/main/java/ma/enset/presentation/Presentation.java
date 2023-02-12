package ma.enset.presentation;

import ma.enset.dao.DaoImpl;
import ma.enset.dao.DaoImpl2;
import ma.enset.metier.MetierImpl;

public class Presentation {
    //Factory class
    public static void main(String[] args) {
        //DaoImpl dao = new DaoImpl();
        /*
        * Injection des dépendances
        * par injection statique
        * */

        DaoImpl2 dao2 = new DaoImpl2();
        //MetierImpl metier = new MetierImpl(dao);
        //metier.setDao(dao2); // Injection de dépendance

        //System.out.println(metier.calcul());
    }
}
