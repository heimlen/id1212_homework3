package se.kth.id1212.heimlen.homework3;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import se.kth.id1212.heimlen.homework3.dto.AccountDTO;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The remote methods of the filesystem, that can be called using RMI.
 */
public interface FileSystem extends Remote{
    /**
     * The default URI of the filesystem server in the RMI registry.
     */
    static final String FILESYSTEM_NAME_IN_REGISTRY = "filesystem";

    /**
     * Creates an account with the specified username.
     * @param username the username of the account.
     */
    void registerUser(String username, String password) throws RemoteException, MySQLIntegrityConstraintViolationException, Exception, DuplicateUsernameException;

    /**
     * Unregisters an account given a username
     */
    void unregisterUser(String username, String password) throws RemoteException;

    /**
     * List files in the filesystem
     */
    List<? extends FileDTO> listFiles() throws RemoteException;

    /**
     * Login user
     */
    Long login(String username, String password) throws Exception;

    /**
     * Logout user
     */
    public void logout(String username) throws RemoteException;

    void upload(String localFilename, long userId, long size, Boolean isPublicAccess, Boolean isWritePermission, Boolean isReadPermission);
}
