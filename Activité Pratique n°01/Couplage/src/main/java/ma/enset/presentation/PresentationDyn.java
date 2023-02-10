package ma.enset.presentation;

import ma.enset.dao.IDao;
import ma.enset.metier.IMetier;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Scanner;

public class PresentationDyn {
    public static void main(String[] args) throws Exception {
        //Lecture du fichier conf.txt
        Scanner scanner = new Scanner(new File("src/main/conf.txt"));


        String daoClassName = scanner.nextLine();
        Class cDao = Class.forName(daoClassName);
        IDao dao = (IDao) cDao.newInstance(); //Instanciation dynamique des classes

        String metierClassName = scanner.nextLine();
        Class cMetier =Class.forName(metierClassName);
        IMetier metier =(IMetier) cMetier.newInstance();
        //Maintenant on a besoin de la méthode Calcul
        Method method =cMetier.getMethod("setDao",IDao.class);
        method.invoke(metier,dao);

        System.out.println(dao.getDate());
        System.out.println(metier.calcul(new Date()));
    }
}
