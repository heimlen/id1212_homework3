package se.kth.id1212.heimlen.homework3.controller;

import org.hibernate.HibernateException;
import se.kth.id1212.heimlen.homework3.FileSystem;
import se.kth.id1212.heimlen.homework3.Listener;
import se.kth.id1212.heimlen.homework3.dto.CredentialDTO;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.integration.FileSystemDAO;
import se.kth.id1212.heimlen.homework3.model.Account;
import se.kth.id1212.heimlen.homework3.model.AccountManager;
import se.kth.id1212.heimlen.homework3.model.File;

import javax.persistence.NoResultException;
import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the filesystems remote methods, this is the only server class that can be called remotely.
 */
public class Controller extends UnicastRemoteObject implements FileSystem {
    private final Map<Long, AccountManager> accounts = new ConcurrentHashMap<>();
    private final FileSystemDAO fileSystemDB;
    private final Map<CredentialDTO, Long> authentication = new ConcurrentHashMap<>();

    public Controller() throws RemoteException {
    super();
    fileSystemDB = new FileSystemDAO();
    }

    private AccountManager auth(long userId) throws IllegalAccessException {
        if (!accounts.containsKey(userId))
            throw new IllegalAccessException("You must be logged in to do that!");

        return accounts.get(userId);
    }

    @Override
    public long registerUser(CredentialDTO credentials, Listener outputHandler) throws RemoteException, DuplicateUsernameException {
        AccountManager account = new AccountManager();
        account.addListener(outputHandler);
        long accountId = account.registerAccount(credentials);
        accounts.put(accountId, account);
        authentication.put(credentials,accountId);
        return accountId;
    }

    @Override
    public void unregisterUser(CredentialDTO credentials) throws RemoteException {
        long accountId = authentication.get(credentials);
        AccountManager account = accounts.get(accountId);
        account.unregisterAccount(credentials);
    }

    @Override
    public void listFiles(long userId) throws RemoteException {
        AccountManager account = accounts.get(userId);
        for (File file : fileSystemDB.listFiles(account.getAccount())) {
            StringJoiner msg = new StringJoiner(", ");
            msg.add("Name: " + file.getName());
            msg.add("Size: " + file.getSize() + " Bytes");
            msg.add("Owner: " + file.getOwner().getUsername());
            msg.add("Public: " + file.isPublicAccess());
            msg.add("Read: " + file.isReadPermission());
            msg.add("Writable: " + file.isPublicWrite());
            account.printToTerminal(msg.toString());
        }
    }

    @Override
    public long login(CredentialDTO credentials) throws HibernateException, LoginException, RemoteException {
        if(authentication.get(credentials) == null) {
            throw new LoginException("Wrong username or password");
        }
        long accountId = authentication.get(credentials);
        AccountManager account = accounts.get(accountId);
        account.printToTerminal("You are now logged in and your id is " + accountId);
        return accountId;
    }


    @Override
    public void logout(long id) throws RemoteException, IllegalAccessException {
        AccountManager account = auth(id);
    }

    @Override
    public void upload(String localFilename, long userId, long size, Boolean isPublicAccess, Boolean isWritePermission, Boolean isReadPermission) throws RemoteException, IllegalAccessException {
        AccountManager accountManager = auth(userId);
        Account accountConnectedWithProvidedId = accountManager.getAccount();
        File uploadedFile = new File(localFilename, size, accountConnectedWithProvidedId, isPublicAccess, isWritePermission, isReadPermission);
        try {
            File fileInDatabase = fileSystemDB.getFileByName(localFilename);

            if (accountConnectedWithProvidedId == fileInDatabase.getOwner() ){
                fileSystemDB.updateFile(uploadedFile);
                //TODO physical upload of file -- uploadFile(user, fileDTO);
            } else if (!fileInDatabase.isPublicAccess()) {
                throw new IllegalAccessException("You're not the owner and the file is not public!");
            } else if (!fileInDatabase.isPublicWrite()) {
                throw new IllegalAccessException("You're not the owner of the file and the file is not writable!");
            } else {
                fileSystemDB.updateFileSize(uploadedFile);
                //TODO physical upload of file -- uploadFile(user, fileDTO);

                String alertMsg = String.format("The user \"%s\" has updated your public writable file: \"%s\"",
                        accountConnectedWithProvidedId.getUsername(),
                        fileInDatabase.getName());

                long fileOwnerId = fileInDatabase.getOwner().getId();
                AccountManager fileOwner = accounts.get(fileOwnerId);
                fileOwner.printToTerminal(alertMsg);
            }
        } catch (NoResultException e) {
            // File doesn't exist and we're allowed to do whatever
            fileSystemDB.upload(accountConnectedWithProvidedId, uploadedFile);
            //TODO physical upload of file -- uploadFile(user, fileDTO);
        }
    }

    /*private void alertListeners(String message) throws RemoteException {
        CompletableFuture.runAsync(() -> {
            listeners.forEach(accountManager -> {
                try {

                    accountManager.printToTerminal(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }*/
}
