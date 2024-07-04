package org.jpos;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jpos.models.User;
import org.jpos.q2.QBeanSupport;
import org.jpos.util.NameRegistrar;

public class InitDB extends QBeanSupport implements Runnable {
    @Override
    public void startService(){
        new Thread(this).start();
    }

    Configuration cfg = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class);

    @Override
    public void run(){
        SessionFactory sessionFactory = cfg.buildSessionFactory();
        NameRegistrar.register("SessionFactory",sessionFactory);
    }


}
