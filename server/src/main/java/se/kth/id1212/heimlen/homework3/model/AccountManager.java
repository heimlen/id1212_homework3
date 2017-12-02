package se.kth.id1212.heimlen.homework3.model;

import se.kth.id1212.heimlen.homework3.Listener;
import se.kth.id1212.heimlen.homework3.dto.CredentialDTO;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.integration.FileSystemDAO;

import javax.security.auth.login.LoginException;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class managing each account.
 */
public class AccountManager {
    private Listener outputHandler;
    private Account account;
    private FileSystemDAO fileSystemDAO = new FileSystemDAO();
    private SocketChannel socketChannel;

    public long login(CredentialDTO credentials) throws LoginException, RemoteException {
    account = fileSystemDAO.login(new Account(credentials));
    long accountId = account.getId();
    printToTerminal("You are now logged in and your id is " + accountId);
    return accountId;
    }

    public void addListener(Listener outputHandler) {
        this.outputHandler = outputHandler;
    }

    public void printToTerminal(String msg) throws RemoteException {
        outputHandler.printToTerminal(msg);
    }

    public long registerAccount(CredentialDTO credentials) throws RemoteException, DuplicateUsernameException {
        account = fileSystemDAO.registerAccount(new Account(credentials));
        long accountId = account.getId();
        printToTerminal("You are now logged in and your id is " + accountId);
        return accountId;
    }

    public void unregisterAccount(CredentialDTO credentials) throws RemoteException {
        fileSystemDAO.unregisterAccount(new Account(credentials));
        printToTerminal("Thank you for using our service.");
    }

    public Account getAccount() {
        return account;
    }
}
