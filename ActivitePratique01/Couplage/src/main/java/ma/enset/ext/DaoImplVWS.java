package ma.enset.ext;

import ma.enset.dao.IDao;

public class DaoImplVWS implements IDao {
    @Override
    public double getData() {
        System.out.println("Version VWS");
        double temperature = 20;
        return temperature;
    }
}
