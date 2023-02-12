package enset.ext;

import FrameworkIOC.dao.Service;
import enset.dao.IDao;

@Service("dao")
public class DaoImplVWS implements IDao {
    @Override
    public double getData() {
        System.out.println("Version VWS");
        double temperature = 20;
        return temperature;
    }
}
