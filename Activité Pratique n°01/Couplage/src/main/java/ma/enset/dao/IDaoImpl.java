package ma.enset.dao;

import java.util.Date;

public class IDaoImpl implements IDao{
    @Override
    public Date getDate() {
        return new Date();
    }
}
