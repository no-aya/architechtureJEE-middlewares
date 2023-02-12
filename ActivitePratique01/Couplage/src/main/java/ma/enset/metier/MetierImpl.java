package ma.enset.metier;

import ma.enset.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier {


    private IDao dao; //Couplage faible
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }
    @Override
    public double calcul() {
        double temperature = dao.getData();
        return temperature * 54;
    }
    // Injection dans la variable dao un objet d'une classe qui implémente l'interface IDao
    @Autowired
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
