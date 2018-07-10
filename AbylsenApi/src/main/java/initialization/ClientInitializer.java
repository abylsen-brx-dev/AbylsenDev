package initialization;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import interfaces.IInitializer;
import model.Client;
import model.LngLat;

public class ClientInitializer implements IInitializer {

	@Override
	public void startInit(Session session) {
		createIfNotExists(session, "GAMING1", "Rue Saint Exupéry 17, 4460 Bierset",
				createIfNotExists(session, 50.638687f, 5.4279303f));
		createIfNotExists(session, "Orange Business Service", "Avenue du Bourget 3, 1130 Bruxelles",
				createIfNotExists(session, 50.8767413f, 4.4139008f));
		createIfNotExists(session, "idweaver", "Drève Richelle 159, 1410 Waterloo",
				createIfNotExists(session, 50.709436f, 4.407755f));
	}

	private Client createIfNotExists(Session session, String name, String address, LngLat position) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Client.class);

		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("address", address));
		criteria.add(Restrictions.eq("position", position));

		Client c = new Client();

		c.setName(name);
		c.setAddress(address);
		c.setPosition(position);

		if (criteria.uniqueResult() == null)
			session.save(c);

		return c;
	}

	private LngLat createIfNotExists(Session session, float lng, float lat) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(LngLat.class);

		criteria.add(Restrictions.eq("lng", lng));
		criteria.add(Restrictions.eq("lat", lat));

		LngLat p = new LngLat();

		p.setLng(lng);
		p.setLat(lat);

		if (criteria.uniqueResult() == null)
			session.save(p);

		return p;
	}
}
