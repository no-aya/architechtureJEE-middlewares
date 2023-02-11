package FrameworkIOC.metier;

import FrameworkIOC.dao.Bean;
import FrameworkIOC.dao.Beans;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Context implements IContext {
    String path;
    List<Bean> beanList = new ArrayList<>();
    public Context(String path) {
        this.path = path;
        unmarshallXML();
    }
    @Override
    public void unmarshallXML() {
        try {
            JAXBContext context = JAXBContext.newInstance(Beans.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Beans beans = (Beans) unmarshaller.unmarshal(Context.class.getResourceAsStream("/"+path));
            for (Bean bean : beans.getBeanList()) {
                beanList.add(bean);
            }
        }catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Object getBean(String id) {
        for (Bean bean : beanList) {
            if (bean.getId().equals(id)) {
                try {
                    return beanFactory(bean);
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
    private Object beanFactory(Bean bean) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class className = Class.forName(bean.getClassName());
        Object object = className.newInstance();
        if (bean.getProperty()!=null) {
           //Injection par setter
            Method[] methods = className.getMethods();
            for (Method method : methods) {
                if (method.getName().equals("set"+bean.getProperty().getName())) {
                    try {
                        //System.out.println(bean.getProperty().getName());
                        Object objectTemp=getBean(bean.getProperty().getRef());
                        //System.out.println(object2);
                        method.invoke(object, objectTemp);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return object;
    }

}
