package FrameworkIOC.metier;

import FrameworkIOC.dao.Bean;
import FrameworkIOC.dao.Beans;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Context implements IContext {

    List<Bean> beanList = new ArrayList<>();
    protected Context() {
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

    @Override
    public Object beanFactory(Bean bean) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class className = Class.forName(bean.getClassName());
        Object object = className.newInstance();
        if (bean.getProperty()!=null) {

            String name = bean.getProperty().getName();
            String ref =bean.getProperty().getRef();
            Boolean isDone = false;


            //Injection par Setter
            if (!isDone){
                try {
                    bySetter(className, object,name , ref );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            //injection par constructeur
            if (!isDone){
                try {
                    byContructor(className, object, name, ref);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            //Injection par attribut (Field)
            if (!isDone){
                try {
                    byField(className, object, name, ref);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return object;
    }
    @Override
    public void byField(Class className, Object object, String name, String ref) {
        Field[] fields = className.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)||field.getName().equals(name.toLowerCase())) {
                try {
                    Object objectTemp=getBean(ref);
                    field.setAccessible(true);
                    field.set(object, objectTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Override
    public void byContructor(Class className, Object object, String name, String ref) {
        Constructor[] constructors = className.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getParameterCount()==1) {
                try {
                    Object objectTemp=getBean(ref);
                    object = constructor.newInstance(objectTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    @Override
    public void bySetter(Class className, Object object, String name, String ref) {
        Method[] methods = className.getMethods();
        for (Method method : methods) {
            if (method.getName().equals("set"+name)) {
                try {
                    Object objectTemp=getBean(ref);
                    method.invoke(object, objectTemp);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
