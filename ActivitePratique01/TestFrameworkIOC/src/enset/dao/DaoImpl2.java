package enset.dao;


import FrameworkIOC.dao.Service;


public class DaoImpl2 implements IDao{
    @Override
    public double getData() {
        System.out.println("Version capteur");
        double temperature = 80;
        return temperature;
    }
}
