package se.kth.id1212.heimlen.homework3.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import se.kth.id1212.heimlen.homework3.FileSystem;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.integration.FileSystemDAO;
import se.kth.id1212.heimlen.homework3.model.Account;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Implementation of the filesystems remote methods, this is the only server class that can be called remotely.
 */
public class Controller extends UnicastRemoteObject implements FileSystem {
    private final FileSystemDAO fileSystemDB;

    public Controller() throws RemoteException {
    super();
    fileSystemDB = new FileSystemDAO();
    }

    @Override
    public void registerUser(String username, String password) throws RemoteException, DuplicateUsernameException {
        fileSystemDB.registerAccount(new Account(username, password));
    }

    @Override
    public void unregisterUser(String username, String password) throws RemoteException {
        fileSystemDB.unregisterAccount(new Account(username, password));
    }

    @Override
    public List<? extends FileDTO> listFiles() throws RemoteException {
        return null;
    }

    @Override
    public Long login(String username, String password) throws HibernateException, LoginException {
        return fileSystemDB.login(new Account(username,password));
    }


    @Override
    public void logout(String username) {

    }

    @Override
    public void upload(String localFilename, long userId, long size, Boolean isPublicAccess, Boolean isWritePermission, Boolean isReadPermission) {
        fileSystemDB.upload();
    }
}
