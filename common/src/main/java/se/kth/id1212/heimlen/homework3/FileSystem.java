package se.kth.id1212.heimlen.homework3;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import se.kth.id1212.heimlen.homework3.dto.CredentialDTO;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
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
     * Creates an account with the specified credentials
     * @param credentialDTO the specified credentials for the account
     */
    long registerUser(CredentialDTO credentialDTO, Listener outputHandler) throws RemoteException, MySQLIntegrityConstraintViolationException, Exception, DuplicateUsernameException;

    /**
     * Unregisters an account given credentials
     */
    void unregisterUser(CredentialDTO credentialDTO) throws RemoteException;

    /**
     * List files in the filesystem
     */
    void listFiles(long userId) throws RemoteException;

    /**
     * Login user
     */
    long login(CredentialDTO credentialDTO) throws Exception;

    /**
     * Logout user
     */
    void logout(long id) throws RemoteException, IllegalAccessException;

    void upload(String localFilename, long userId, long size, Boolean isPublicAccess, Boolean isWritePermission, Boolean isReadPermission) throws RemoteException, IllegalAccessException;
}
