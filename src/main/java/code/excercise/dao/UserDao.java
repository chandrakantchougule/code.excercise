package code.excercise.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import code.excercise.domain.User;
import code.excercise.util.HibernateUtil;

public class UserDao {

	
	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	public User getUser(int userId) {
		System.out.println("getUser " + userId); 
		User user = null;
	        Session session = null;

	        try {
	            session = sessionFactory.openSession();
	            session.beginTransaction();
	            user = (User) session.get(User.class, userId);
	            
	        }
	        catch (Exception exception) {
	           if (session != null) {
	               session.getTransaction().rollback();
	           }
	        }
	        finally {
	            if (session != null) {
	               session.close();
	            }
	        }

	        return user;
	}

	
	public User addUser(User user) {
		   Session session = null;

	        try {
	            session = sessionFactory.openSession();
	            session.beginTransaction();
	            session.persist(user);
	            session.getTransaction().commit();
	        }
	        catch (Exception exception) {
	           if (session != null) {
	               session.getTransaction().rollback();
	           }
	        }
	        finally {
	            if (session != null) {
	               session.close();
	            }
	        }

	        return user;
	}

}
