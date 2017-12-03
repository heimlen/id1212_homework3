package se.kth.id1212.heimlen.homework3.model;

import se.kth.id1212.heimlen.homework3.Listener;
import se.kth.id1212.heimlen.homework3.dto.CredentialDTO;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.integration.FileSystemDAO;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;

/**
 * Class managing each account.
 */
public class AccountManager {
    private Listener outputHandler;
    private Account account;
    private FileSystemDAO fileSystemDAO = new FileSystemDAO();
    private SocketChannel socketChannel;

    public void addListener(Listener outputHandler) {
        this.outputHandler = outputHandler;
    }

    public void printToTerminal(String msg) throws RemoteException {
        outputHandler.printToTerminal(msg);
    }

    /**
     * Registers an account with the provided credentials.
     * @param credentials the account credentials.
     * @return the id that the account is associated with.
     * @throws RemoteException if RMI somehow fails.
     * @throws DuplicateUsernameException if the username specified in credentials is already in use in the filesystem.
     */
    public long registerAccount(CredentialDTO credentials) throws RemoteException, DuplicateUsernameException {
        account = fileSystemDAO.registerAccount(new Account(credentials));
        long accountId = account.getId();
        printToTerminal("You are now logged in and your id is " + accountId);
        return accountId;
    }

    /**
     * Unregisters an account
     * @param credentials the account credentials
     * @throws RemoteException if RMI somehow fails.
     */

    public void unregisterAccount(CredentialDTO credentials) throws RemoteException {
        fileSystemDAO.unregisterAccount(new Account(credentials));
        printToTerminal("Thank you for using our service.");
    }

    /**
     * Getter
     * @return the account object.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Saves the provided socket to the account, for uploading files with this account.
     * @param socketChannel The socketchannel used to upload the accounts files.
     */
    public void attachSocketToAccount(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    /**
     * Getter
     * @return the socketchannel.
     */
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}