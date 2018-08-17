package edu.usu.cs.mas.test;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import edu.usu.cs.mas.managedisaster.entity.TestTableEntity;

public class HibernateTest {

	private SessionFactory sessionFactory;
	private Session session;
	
	public void setUp() throws Exception {
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
			e.printStackTrace();
		}
	}
	
	public void openSession() {
		session = sessionFactory.openSession();
	}
	
	public void closeSession() {
		session.close();
	}
	
	public void save() {
		session.beginTransaction();
		TestTableEntity entity1 = new TestTableEntity("Entity1");
		TestTableEntity entity2 = new TestTableEntity("Entity2");
		session.save( entity1 );
		session.save( entity2 );
		session.getTransaction().commit();
	}
	
	public void select() {
		@SuppressWarnings("unchecked")
		TypedQuery<TestTableEntity> query = session.createQuery("FROM TestTableEntity");
		List<TestTableEntity> result = query.getResultList();
		for(TestTableEntity entity : result) {
			System.out.println(entity);
		}
	}
	
	public void select(long id) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<TestTableEntity> query = builder.createQuery(TestTableEntity.class);
    Root<TestTableEntity> root = query.from( TestTableEntity.class );
    query.select(root)
    			.where(
    					builder.equal(root.get("id"), id));
    Query<TestTableEntity> q = session.createQuery(query);
    List<TestTableEntity> entities  = q.getResultList();
    for(TestTableEntity entity : entities) {
			System.out.println(entity);
		}
	}
	
	public void delete() {
		@SuppressWarnings("unchecked")
		TypedQuery<TestTableEntity> query = session.createQuery("DELETE FROM TestTableEntity");
		query.executeUpdate();
	}
	
	public static void main(String[] args) {
		HibernateTest hibernateTest = new HibernateTest();
		try {
			hibernateTest.setUp();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		hibernateTest.openSession();
//		hibernateTest.save();
//		hibernateTest.save();
//		hibernateTest.select();
		hibernateTest.delete();
		hibernateTest.closeSession();
	}
	
	
}
