package FrameworkIOC.metier;

import FrameworkIOC.dao.Autowired;
import FrameworkIOC.dao.Bean;
import FrameworkIOC.dao.Property;
import FrameworkIOC.dao.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class ContextAnn extends Context{
    ArrayList<String> packages = new ArrayList<>();
    public ContextAnn(String... packages) {
        for (String pack : packages) {
            this.packages.add(pack);
        }
        loadAll();
    }

    public void loadAll() {
        for (String pack : packages) {
            //Search all classes in the package
            ClassesLoader classesLoader = new ClassesLoader();
            ArrayList<Class> classes = classesLoader.findAllClassesUsingClassLoader(pack);
            for (Class className : classes) {
                //Search all annotations in the class
                if (className.isAnnotationPresent(Service.class)) {
                    Service service = (Service) className.getAnnotation(Service.class);
                    Bean bean = new Bean();
                    if (!service.value().equals(""))bean.setId(service.value());
                    Boolean isDone = false;
                    //find Autowired annotation
                    Method[] methods = className.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Autowired.class)) {
                            Property property = new Property();
                            //if the method is a setter
                            String name = method.getName().substring(3);
                            property.setName(name);
                            property.setRef(name.toLowerCase());
                            bean.setProperty(property);
                            isDone = true;
                        }
                    }
                    //if there is no Autowired annotation
                    if (!isDone) {
                        Field[] fields = className.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(Autowired.class)) {
                                Property property = new Property();
                                property.setName(field.getName());
                                property.setRef(field.getName().toLowerCase());
                                bean.setProperty(property);
                                isDone = true;
                            }
                        }
                    }
                    //Constructor
                    if (!isDone) {
                        Constructor[] constructors = className.getConstructors();
                        for (Constructor constructor : constructors) {
                            if (constructor.isAnnotationPresent(Autowired.class)) {
                                Property property = new Property();
                                property.setName(constructor.getName());
                                property.setRef(constructor.getName().toLowerCase());
                                bean.setProperty(property);
                                isDone = true;
                            }
                        }
                    }
                    bean.setClassName(className.getName());
                    beanList.add(bean);
                }
            }
        }
    }


}
