package ma.enset.metier;
import ma.enset.dao.IDao;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class IMetierImpl implements IMetier{
    //Couplage faible entre IMetierImpl et IDaoImpl
    private IDao dao;
    @Override
    public long calcul(Date date) {
        long dateBeforeInMs = dao.getDate().getTime();
        long dateAfterInMs = date.getTime();
        long timeDiff = Math.abs(dateAfterInMs - dateBeforeInMs);
        return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
    }
    // Injection dans la variable dao un objet d'une classe qui implémente l'interface IDao
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}

