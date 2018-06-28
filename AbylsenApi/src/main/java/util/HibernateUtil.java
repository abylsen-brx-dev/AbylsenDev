package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import Logging.LoggerManager;

public class HibernateUtil {

	private static final SessionFactory sf;

	static {
		try {
			sf = new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {
			LoggerManager.getInstance().logError("[HibernateUtil.{static}] Echec création sessionFactory => ", (Exception)e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sf;
	}
}
