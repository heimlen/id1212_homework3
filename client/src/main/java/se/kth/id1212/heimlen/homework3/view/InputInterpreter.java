package se.kth.id1212.heimlen.homework3.view;

import se.kth.id1212.heimlen.homework3.constants.Constants;
import se.kth.id1212.heimlen.homework3.dto.CredentialDTO;
import se.kth.id1212.heimlen.homework3.dto.FileDTO;
import se.kth.id1212.heimlen.homework3.FileSystem;
import se.kth.id1212.heimlen.homework3.net.FileTransferHandler;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * This class read and interpret user commands. This interpreter will run in a separate thread to allow multiple
 * users to simultaneously give commands to the interpreter. These threads will be picked from the existing thread pool in JDK.
 */
public class InputInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner clientInput = new Scanner(System.in);
    private SocketChannel socketChannel;
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
                        userId = fileSystem.registerUser(getCredentials(userInput), outputHandler);
                        //userId = fileSystem.registerUser(userInput.getFirstParam(), userInput.getSecondParam(), outputHandler);
                        //outputHandler.printToTerminal("Successfully registered account.");
                        break;
                    case UNREGISTER :
                        fileSystem.unregisterUser(getCredentials(userInput));
                        //fileSystem.unregisterUser(userInput.getFirstParam(), userInput.getSecondParam());
                        break;
                    case LS :
                        fileSystem.listFiles(userId);
                        break;
                    case LOGIN :
                        userId = fileSystem.login(getCredentials(userInput));
                        //createServerSocket(userId);
                        break;
                    case LOGOUT :
                        userId = 0;
                        break;
                    case UPLOAD :
                        if(userId == 0) {
                            throw new LoginException("You have to register and log in prior to uploading files!");
                        }
                        upload(userInput.getFirstParam(), userInput.getSecondParam(), userInput.getThirdParam(), userInput.getFourthParam());
                        break;
                    case DOWNLOAD :
                        if(userId == 0) {
                            throw new LoginException("You have to register and log in prior to downloading files!");
                        }
                    //    fileSystem.download();
                        break;
                }
            } catch (Exception e) {
                outputHandler.errorToTerminal(e.getMessage(), e);
            }
        }
    }

    private CredentialDTO getCredentials(UserInput userInput) {
        return new CredentialDTO(userInput.getFirstParam(), userInput.getSecondParam());
    }

    private String readNextLine() {
        msgOut.print(PROMPT);
        return clientInput.nextLine();
    }

    private void upload(String localFilename, String isPublicAccess, String isPublicWritePermission, String isPublicReadPermission) throws IOException, IllegalAccessException {
        Path path = FileTransferHandler.getClientPath(localFilename);
        long size;
        Boolean publicAccess = false;
        Boolean publicWritePermission = false;
        Boolean publicReadPermission = false;

        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            size = attributes.size();
        } catch (IOException e) {
            throw new IOException("File does not exist, create the file first");
        }
        if(isPublicAccess.equalsIgnoreCase("public")) {
            publicAccess = true;
            if(isPublicWritePermission.equalsIgnoreCase("yes")) {
                publicWritePermission = true;
            } if(isPublicReadPermission.equalsIgnoreCase("yes")) {
                publicReadPermission = true;
            }
        }
        fileSystem.upload(localFilename, userId, size, publicAccess, publicWritePermission, publicReadPermission);
        //.sendFile();
    }

    /*private void createServerSocket(long userId) throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(Constants.SOCKET_ADDRESS, Constants.SOCKET_PORT));

        ObjectOutputStream output = new ObjectOutputStream(socketChannel.socket().getOutputStream());

        output.writeObject(new SocketIdentifierDTO(userId));
        output.flush();
        output.reset();
    }*/


    public class OutputHandler extends UnicastRemoteObject implements se.kth.id1212.heimlen.homework3.Listener {

        OutputHandler() throws RemoteException {
        }

        @Override
        public void printToTerminal(String output) throws RemoteException {
            msgOut.println(output);
            msgOut.print(PROMPT);

        }

        @Override
        public void errorToTerminal(String error, Exception e) {
            msgOut.print("ERROR: \n" + error + "\n");
        }

        @Override
        public void disconnected() throws RemoteException {
            printToTerminal("Filesystem is closing.");
        }
    }
}
