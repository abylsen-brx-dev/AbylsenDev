package initialization;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

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

			System.out.println("Start " + i.getClass().getName());

			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			
			try{
				i.startInit(session);
				System.out.println("==> Finish without errors");
			}
			catch(Exception e) {
				System.out.println("!!!!! Errors : " + e.getMessage());
			}
			
			tx.commit();
			session.close();
		}
	}
}
