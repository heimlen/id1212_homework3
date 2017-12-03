package se.kth.id1212.heimlen.homework3.integration;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.model.Account;
import se.kth.id1212.heimlen.homework3.model.File;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Implementation of the fileSystem.
 */
public class FileSystemDAO {
    private static SessionFactory sessionFactory;


    public FileSystemDAO() {
        try {
            initSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes a <code>SessionFactory</code>.
     */
    public static void initSessionFactory() {
        if (sessionFactory == null)
            sessionFactory = new Configuration().configure().buildSessionFactory();
    }


    public Account registerAccount(Account account) throws RemoteException, DuplicateUsernameException {
        Transaction tx = null;
        Account fetchedAccount = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            session.save(account);
            Query query = session.createQuery("select account from Account account where username= :username and password= :password");
            query.setParameter("username", account.getUsername());
            query.setParameter("password", account.getPassword());
            fetchedAccount = (Account) query.getSingleResult();
            tx.commit();
            return fetchedAccount;
        } catch (Exception e) {
            throw new DuplicateUsernameException("This username already exists, please try another one.");
        } finally {
            session.close();
        }

    }

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

    @SuppressWarnings("unchecked")
    public List<File> listFiles(Account account) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("Select file from File file where file.owner= :account or file.publicAccess = true");
            query.setParameter("account", account);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public void upload(Account account, File uploadedFile) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            uploadedFile.setOwner(account);
            session.save(uploadedFile);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateFile(File uploadedFile) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();

            File file = getFileByName(uploadedFile.getName());
            file.setPublicAccess(uploadedFile.isPublicAccess());
            file.setReadPermission(uploadedFile.isReadPermission());
            file.setWritePermission(uploadedFile.isPublicWrite());
            file.setSize(uploadedFile.getSize());

            session.update(file);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public File getFileByName(String filename) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("Select file from File file where file.name=:filename");
            query.setParameter("filename", filename);

            return (File) query.getSingleResult();
        } finally {
            session.close();
        }
    }

    public void updateFileSize(File uploadedFile) {
        Transaction tx = null;
        Session session = sessionFactory.openSession();
        try {
            tx = session.beginTransaction();

            File file = getFileByName(uploadedFile.getName());
            file.setSize(uploadedFile.getSize());

            session.update(file);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

}
