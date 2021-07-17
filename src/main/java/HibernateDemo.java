import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import xyz.kiandev.HibernateDemo.entity.User;
import xyz.kiandev.HibernateDemo.util.HibernateUtil;

public class HibernateDemo {
	private static final SessionFactory factory = HibernateUtil.getSessionFactory();

	public static void main(String[] args) {
		HibernateDemo hibernateDemo = new HibernateDemo();
		
		// Add user
		Long user1 = hibernateDemo.addUser("kian", "123");
		Long user2 = hibernateDemo.addUser("bao", "123");
		// Print all user
		hibernateDemo.printAll();
		
		// Find user has username contain "b"
		System.out.println("List user has username contain 'b'");
		hibernateDemo.findUser("b");
		
		// Update password
		hibernateDemo.updatePassword(user1, "234");
		hibernateDemo.printAll();
		
		// Delete user
		hibernateDemo.deleteUser(user2);
		hibernateDemo.printAll();
	}

	public Long addUser(String username, String password) {
		Session session = factory.openSession();
		Transaction transaction = null;
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		Long createdUserId = null;

		try {
			transaction = session.beginTransaction();
			createdUserId = (Long) session.save(user);
			transaction.commit();
			System.out.println("Add user id=" + createdUserId + " success!");

		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return createdUserId;
	}

	public void updatePassword(Long userId, String newPassword) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = (User) session.get(User.class, userId);
			user.setPassword(newPassword);
			session.update(user);
			transaction.commit();
			System.out.println("Update password for user id=" + userId + " success!");
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void deleteUser(Long userId) {
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = (User) session.get(User.class, userId);
			session.delete(user);
			transaction.commit();
			System.out.println("Delete user id=" + userId + " success!");
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public List<User> getAllUser() {
		Session session = factory.openSession();
		Transaction transaction = null;
		List<User> users = null;
		try {
			transaction = session.beginTransaction();
			users = session.createQuery("FROM user").getResultList();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return users;
	}
	
	public void findUser(String keyword) {
		Session session = factory.openSession();
		Transaction transaction = null;
		List<User> users = new ArrayList<User>();
		try {
			transaction = session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			Root<User> root = criteriaQuery.from(User.class);
			criteriaQuery.select(root).where(builder.like(root.get("username"), "%"+keyword+"%"));
			users = session.createQuery(criteriaQuery).getResultList();
			transaction.commit();
			users.forEach(user -> System.out.println(user.toString()));
			System.out.println();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void printAll() {
		getAllUser().forEach(user -> System.out.println(user.toString()));
		System.out.println();
	}

}
