package se.kth.id1212.heimlen.homework3;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.sun.org.apache.regexp.internal.RE;
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
     * Download a file from the filesystem
     * @param filename the name of the file
     * @param userId the id of the user that wants to download the file
     * @return the file
     */
    FileDTO downloadFile(String filename, long userId) throws RemoteException, IllegalAccessException;

    /**
     * Login user
     */
    long login(CredentialDTO credentialDTO) throws Exception;

    /**
     * Upload a file to the filesystem
     * @param localFilename the name of the file on the client
     * @param userId the id representing the user/account that wants to upload
     * @param size the size of the file
     * @param isPublicAccess is it supposed to have public access?
     * @param isPublicWritePermission does it have public write permission?
     * @param isPublicReadPermission does it have public read permission?
     * @throws RemoteException if something fails with the RMI
     * @throws IllegalAccessException if a user tries to upload a file without being logged in
     */
    void upload(String localFilename, long userId, long size, Boolean isPublicAccess, Boolean isPublicWritePermission, Boolean isPublicReadPermission) throws RemoteException, IllegalAccessException;
}
