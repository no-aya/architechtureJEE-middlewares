package FrameworkIOC.dao;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class Beans{
    @XmlElement(name = "bean")
    public List<Bean> beanList = null;

    public List<Bean> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<Bean> beanList) {
        this.beanList = beanList;
    }
}
