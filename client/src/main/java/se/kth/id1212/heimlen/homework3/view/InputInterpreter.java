package se.kth.id1212.heimlen.homework3.view;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.jpa.event.spi.jpa.Listener;
import se.kth.id1212.heimlen.homework3.dto.AccountDTO;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;
import se.kth.id1212.heimlen.homework3.exceptions.BadFormattedInputException;
import se.kth.id1212.heimlen.homework3.exceptions.DuplicateUsernameException;
import se.kth.id1212.heimlen.homework3.exceptions.UnknownCommandException;
import se.kth.id1212.heimlen.homework3.FileSystem;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;

/**
 * This class read and interpret user commands. This interpreter will run in a separate thread to allow multiple
 * users to simultaneously give commands to the interpreter. These threads will be picked from the existing thread pool in JDK.
 */
public class InputInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner clientInput = new Scanner(System.in);
    private boolean acceptingClientCommands = false;
    private FileSystem fileSystem;
    private final ThreadSafeStdOut msgOut = new ThreadSafeStdOut();
    private long userId;
    private OutputHandler outputHandler;

    /**
     * Starts the interpreter. The interpreter will be waiting for user input when this method
     * returns. Calling <code>start</code> on an interpreter that is already started has no effect.
     */
    public void start(FileSystem fileSystem) throws RemoteException {
        this.fileSystem = fileSystem;
        this.outputHandler = new OutputHandler();
        if(acceptingClientCommands) {
            return;
        }
        acceptingClientCommands = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        FileDTO fileInstance = null;
        while(acceptingClientCommands) {
            try {
                UserInput userInput = new UserInput(readNextLine());
                switch(userInput.getUserCommand()) {
                    case HELP :
                        System.out.println("These are the available commands \n");
                        for (UserCommand userCommand : UserCommand.values()) {
                            System.out.println(userCommand.toString().toLowerCase());
                        }
                        break;
                    case QUIT :
                        acceptingClientCommands = false;
                        outputHandler.disconnected();
                        break;
                    case REGISTER :
                        fileSystem.registerUser(userInput.getFirstParam(), userInput.getSecondParam());
                        outputHandler.printToTerminal("Successfully registered account.");
                        break;
                    case UNREGISTER :
                        fileSystem.unregisterUser(userInput.getFirstParam(), userInput.getSecondParam());
                        break;
                    case LS :
                        List<? extends FileDTO> files = fileSystem.listFiles();
                        System.out.println("Name  |  Size  |  Owner  |  read-permission  |  write-permission");
                        for(FileDTO file : files) {
                            System.out.println(file.getName() + " " + file.getSize() + " " + file.getOwner().getUsername() + " " + file.isReadPermission() + " " + file.isWritePermission());
                        }
                        break;
                    case LOGIN :
                        userId = fileSystem.login(userInput.getFirstParam(), userInput.getSecondParam());
                    case LOGOUT :
                        userId = 0;
                    case UPLOAD :
                        upload();
                    case DOWNLOAD :
                    //    fileSystem.download();
                }
            } catch (Exception e) {
                outputHandler.errorToTerminal(e.getMessage(), e);
            }
        }
    }

    private String readNextLine() {
        msgOut.print(PROMPT);
        return clientInput.nextLine();
    }

    private void upload(String localFilename, Boolean isPublicAccess, Boolean isWritePermission, Boolean isReadPermission) throws IOException {
        Path path = Paths.get("clientDirectory/%s", localFilename);
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            long size = attributes.size();
            fileSystem.upload(localFilename, userId, size, isPublicAccess, isWritePermission, isReadPermission);
        } catch (IOException e) {
            throw new IOException("File does not exist, create the file first");
        }
    }


    private class OutputHandler extends UnicastRemoteObject {

        protected OutputHandler() throws RemoteException {
        }

        public void printToTerminal(String output) throws RemoteException {
            msgOut.println(output);
            msgOut.print(PROMPT);

        }

        public void errorToTerminal(String error, Exception e) {
            msgOut.print("ERROR: \n" + error + "\n");
        }

        public void disconnected() throws RemoteException {
            printToTerminal("Filesystem is closing.");
        }
    }
}
