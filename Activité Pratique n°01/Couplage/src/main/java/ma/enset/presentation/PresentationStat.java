package ma.enset.presentation;
import ma.enset.dao.IDaoImpl;
import ma.enset.metier.IMetierImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PresentationStat {
    public static void main(String[] args){
        IDaoImpl dao = new IDaoImpl();
        IMetierImpl metier = new IMetierImpl();
        metier.setDao(dao);
        Long days=0L;
        try {
            days=metier.calcul(new SimpleDateFormat("yyyy-MM-dd").parse("2001-01-01"));
        }catch (ParseException e){
            e.printStackTrace();
        }
        System.out.println("le nombre de jours est : "+days);
    }
}
