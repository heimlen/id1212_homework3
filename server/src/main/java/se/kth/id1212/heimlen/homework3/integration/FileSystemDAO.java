package se.kth.id1212.heimlen.homework3.integration;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.model.Account;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Implementation of the fileSystem.
 */
public class FileSystemDAO {
    private static SessionFactory sessionFactory;


    public FileSystemDAO() {
        try {
            setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUp() throws Exception {
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
        }
    }

    public void registerAccount(Account account) throws RemoteException, DuplicateUsernameException {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } catch (Exception e) {
            throw new DuplicateUsernameException("This username already exists, please try another one.");
        } finally {
            session.close();
        }

    }

    //TODO ask Leif if unregistering an user should remove all that users file as well?
    /**
     * Used to unregister an account and thereby remove it and all of its files from the database.
     * @param account the account to remove
     */
    public void unregisterAccount(Account account) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("delete from Account where username= :username and password= :password");
            query.setParameter("username", account.getUsername());
            query.setParameter("password", account.getPassword());
            query.executeUpdate();
            tx.commit();
            session.close();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    //TODO add the functionality that only private files owned by the account is shown.

    /**
     * Lists all the files that the file system currently contains that the provided <code>Account</code> has access to.
     * @param account the logged in and registered account.
     * @return
     */
    public List<File> listFiles(Account account) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Query query = session.createQuery("select file from File file where owner= :id or publicAccess= :pa");
            query.setParameter("id", account.getId());
            query.setParameter("pa", true);
        }
    }

    public long login(Account account) throws LoginException, HibernateException {
        Transaction tx = null;
        Account fetchedAccount = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("select account from Account account where username= :username and password= :password");
            query.setParameter("username", account.getUsername());
            query.setParameter("password", account.getPassword());
            fetchedAccount = (Account) query.getSingleResult();
            tx.commit();
            return fetchedAccount.getId();
        } catch (NoResultException e) {
            throw new LoginException("Wrong username or password");
        }/* catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }*/
    }

    public void upload(String localFilename, long userId, long size, Boolean isPublicAccess, Boolean isWritePermission, Boolean isReadPermission) {
        Transaction tx = null;


    }



}
