package initialization;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

import Logging.LoggerManager;
import interfaces.IInitializer;
import util.HibernateUtil;

public class InitializationEngine {
	private ArrayList<IInitializer> initializers;
	
	public InitializationEngine() {
		initializers = new ArrayList<IInitializer>();
		
		initializers.add(new ClientInformationsInitializer());
		initializers.add(new PersonInitializer());
	}
	
	public void start() {
		Session session;
		Transaction tx;
		for(IInitializer i : initializers) {
			if(i == null)
				continue;

			LoggerManager.getInstance().logDebug("[InitializationEngine.start] Start " + i.getClass().getName());

			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			
			try{
				i.startInit(session);
				LoggerManager.getInstance().logDebug("[InitializationEngine.start] ==> Finish without errors");
			}
			catch(Exception e) {
				LoggerManager.getInstance().logError("[InitializationEngine.start] !!!!! ERROR !!!!!", e);
			}
			
			tx.commit();
			session.close();
		}
	}
}
