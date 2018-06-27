package interfaces;

import org.hibernate.Session;

public interface IInitializer {

	public void startInit(Session session);
}
