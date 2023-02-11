package FrameworkIOC.metier;

import FrameworkIOC.dao.Bean;

public interface IContext {
    Object getBean(String id);
    Object beanFactory(Bean bean) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
    void byField(Class className, Object object, String name, String ref);
    void byContructor(Class className, Object object, String name, String ref);
    void bySetter(Class className, Object object, String name, String ref);
}
