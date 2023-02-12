package ma.enset.presentation;

import ma.enset.dao.IDao;
import ma.enset.metier.IMetier;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Presentation2 {
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(new File("src/config.txt"));

            String daoClassName = scanner.nextLine();
            Class daoClass = Class.forName(daoClassName);
            IDao dao=(IDao) daoClass.newInstance();

            String metierClassName = scanner.nextLine();
            Class metierClass = Class.forName(metierClassName);
            IMetier metier=(IMetier) metierClass.newInstance();
            //exception classcastexception

            //System.out.println(dao.getData());

            Method method = metierClass.getMethod("setDao", IDao.class);
            method.invoke(metier, dao);

            System.out.println(metier.calcul());

        } catch (Exception e) {
            throw new Exception(e);
        }


    }
}
