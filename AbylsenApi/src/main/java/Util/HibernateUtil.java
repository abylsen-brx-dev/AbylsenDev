package Util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	private static final SessionFactory sf;

	static {
		try {
			sf = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Echec création sessionFactory => " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sf;
	}
}
