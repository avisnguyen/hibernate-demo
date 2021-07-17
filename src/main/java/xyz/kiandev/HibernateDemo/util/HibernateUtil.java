package xyz.kiandev.HibernateDemo.util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	public static SessionFactory getSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			return configuration.buildSessionFactory();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return null;
	}

}
