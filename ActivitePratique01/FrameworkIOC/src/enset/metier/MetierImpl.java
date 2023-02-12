package enset.metier;

import FrameworkIOC.dao.Autowired;
import FrameworkIOC.dao.Service;
import enset.dao.IDao;

@Service("metier")
public class MetierImpl implements IMetier {


    public IDao dao; //Couplage faible
    @Autowired
    public void setDao(IDao dao) {
        this.dao = dao;
    }

    //Injection par constructeur
    /*public MetierImpl(IDao dao) {
        this.dao = dao;
    }*/
    public MetierImpl() {
    }
    @Override
    public double calcul() {
        double temperature = dao.getData();
        return temperature * 54;
    }

    // Injection dans la variable dao un objet d'une classe qui implémente l'interface IDao

}
