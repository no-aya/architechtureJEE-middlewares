package enset.dao;



public class DaoImpl implements IDao{

    @Override
    public double getData() {
        /*
        * Se connecter à la base de données pour récupérer la température
         */
        System.out.println("Version base de données");
        return Math.random() * 100;

    }
}
