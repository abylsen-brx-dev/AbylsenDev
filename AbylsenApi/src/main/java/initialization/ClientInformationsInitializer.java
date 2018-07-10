package initialization;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import interfaces.IInitializer;
import model.ClientInformation;

public class ClientInformationsInitializer implements IInitializer {

	@Override
	public void startInit(Session session) {
		createIfNotExists(session, "devApiKey123456", "devSecretKey", 0, "devClient");
	}
	
	private ClientInformation createIfNotExists(Session session, String apiKey, String secretKey, int level, String name) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(ClientInformation.class);
		
		criteria.add(Restrictions.eq("apikey", apiKey));
		criteria.add(Restrictions.eq("secretKey", secretKey));
		criteria.add(Restrictions.eq("level", level));
		criteria.add(Restrictions.eq("name", name));
		
		ClientInformation c = new ClientInformation();

		c.setApikey(apiKey);
		c.setSecretKey(secretKey);
		c.setLevel(level);
		c.setName(name);

		if(criteria.uniqueResult() == null)
			session.save(c);
		
		return c;
	}
}
