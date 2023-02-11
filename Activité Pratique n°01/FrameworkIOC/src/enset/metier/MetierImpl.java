package enset.metier;

import enset.dao.IDao;

public class MetierImpl implements IMetier {


    private IDao dao; //Couplage faible
    public MetierImpl(IDao dao) {
        this.dao = dao;
    }
    public MetierImpl() {
    }
    @Override
    public double calcul() {
        double temperature = dao.getData();
        return temperature * 54;
    }

    // Injection dans la variable dao un objet d'une classe qui implémente l'interface IDao
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
