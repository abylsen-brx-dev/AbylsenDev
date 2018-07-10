package initialization;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import enums.EmployeeEnums;
import interfaces.IInitializer;
import model.Client;
import model.Employee;
import model.Mission;

public class MissionInitializer implements IInitializer {

	@SuppressWarnings("deprecation")
	@Override
	public void startInit(Session session) {
		createIfNotExists(
				session, 
				".NET developper", 
				"blablabla",
				ClientInitializer.createIfNotExists(session, "GAMING1", "Rue Saint Exupéry 17, 4460 Bierset",
						ClientInitializer.createIfNotExists(session, 50.638687, 5.4279303)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee4", "Dev", "employee4.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
		
		createIfNotExists(
				session, 
				"Software Analyst", 
				"blablabla2",
				ClientInitializer.createIfNotExists(session, "GAMING1", "Rue Saint Exupéry 17, 4460 Bierset",
						ClientInitializer.createIfNotExists(session, 50.638687, 5.4279303)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee6", "Dev", "employee6.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
		
		createIfNotExists(
				session, 
				"Assistant RH", 
				"blablabla3",
				ClientInitializer.createIfNotExists(session, "GAMING1", "Rue Saint Exupéry 17, 4460 Bierset",
						ClientInitializer.createIfNotExists(session, 50.638687, 5.4279303)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee9", "Dev", "employee9.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
		
		createIfNotExists(
				session, 
				"Projet Manager", 
				"blablabla4",
				ClientInitializer.createIfNotExists(session, "GAMING1", "Rue Saint Exupéry 17, 4460 Bierset",
						ClientInitializer.createIfNotExists(session, 50.638687, 5.4279303)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee11", "Dev", "employee11.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));

		createIfNotExists(
				session, 
				"Consultant Helper", 
				"blablabla5",
				ClientInitializer.createIfNotExists(session, "Orange Business Service", "Avenue du Bourget 3, 1130 Bruxelles",
						ClientInitializer.createIfNotExists(session, 50.8767413, 4.4139008)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee3", "Dev", "employee3.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));

		createIfNotExists(
				session, 
				"Business Analyst", 
				"blablabla6",
				ClientInitializer.createIfNotExists(session, "Orange Business Service", "Avenue du Bourget 3, 1130 Bruxelles",
						ClientInitializer.createIfNotExists(session, 50.8767413, 4.4139008)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee5", "Dev", "employee5.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
		
		createIfNotExists(
				session, 
				"Java Developper", 
				"blablabla7",
				ClientInitializer.createIfNotExists(session, "idweaver", "Drève Richelle 159, 1410 Waterloo",
						ClientInitializer.createIfNotExists(session, 50.709436, 4.407755)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee7", "Dev", "employee7.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
		
		createIfNotExists(
				session, 
				"Front-end Developper", 
				"blablabla8",
				ClientInitializer.createIfNotExists(session, "idweaver", "Drève Richelle 159, 1410 Waterloo",
						ClientInitializer.createIfNotExists(session, 50.709436, 4.407755)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee8", "Dev", "employee8.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
		
		createIfNotExists(
				session, 
				"IT Engineer", 
				"blablabla9",
				ClientInitializer.createIfNotExists(session, "idweaver", "Drève Richelle 159, 1410 Waterloo",
						ClientInitializer.createIfNotExists(session, 50.709436, 4.407755)),
				PersonInitializer.createEmployeeIfNotExists(session, "Employee10", "Dev", "employee10.dev@dev.com", "devPassword", EmployeeEnums.TYPE_CONSULTANT),
				new Date(2017, 9, 1, 0, 0, 0),
				new Date(2019, 9, 1, 0, 0, 0));
	}
	
	public static Mission createIfNotExists(Session session, String name, String desc, Client client, Employee consultant, Date from, Date to) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Mission.class);

		criteria.add(Restrictions.eq("name", name));
		criteria.add(Restrictions.eq("description", desc));
		criteria.add(Restrictions.eq("client", client));
		criteria.add(Restrictions.eq("consultant", consultant));
		criteria.add(Restrictions.eq("from", from));
		criteria.add(Restrictions.eq("to", to));
		
		Mission m = (Mission)criteria.uniqueResult();
		if(m != null)
			return m;
		
		m = new Mission();
		
		m.setName(name);
		m.setDescription(desc);
		m.setClient(client);
		m.setConsultant(consultant);
		m.setFrom(from);
		m.setTo(to);

		session.save(m);

		return m;
	}
}
