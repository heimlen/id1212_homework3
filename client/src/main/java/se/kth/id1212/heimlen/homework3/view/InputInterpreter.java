package se.kth.id1212.heimlen.homework3.view;

import se.kth.id1212.heimlen.homework3.exceptions.BadFormattedInputException;
import se.kth.id1212.heimlen.homework3.exceptions.UnknownCommandException;
import se.kth.id1212.heimlen.homework3.model.OutputObserver;
import se.kth.id1212.heimlen.homework3.model.ServerConnection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * This class read and interpret user commands. This interpreter will run in a separate thread to allow multiple
 * users to simultaneously give commands to the interpreter. These threads will be picked from the existing thread pool in JDK.
 */
public class InputInterpreter implements Runnable {
    private static final String PROMPT = "> ";
    private final Scanner clientInput = new Scanner(System.in);
    private boolean acceptingClientCommands = false;
    private ServerConnection server;
    private final ThreadSafeStdOut msgOut = new ThreadSafeStdOut();

    /**
     * Starts the interpreter. The interpreter will be waiting for user input when this method
     * returns. Calling <code>start</code> on an interpreter that is already started has no effect.
     */
    public void start() {
        if (acceptingClientCommands) {
            return;
        }
        System.out.println(welcomeMsg());
        acceptingClientCommands = true;
        server = new ServerConnection();
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(acceptingClientCommands) {
            try {
                UserInput userInput = new UserInput(readNextLine());
                switch(userInput.getUserCommand()) {
                    case QUIT :
                        acceptingClientCommands = false;
                        server.disconnect();
                        break;
                    case CONNECT :
                        server.addOutputHandler(new OutputHandler());
                        server.connectToServer(userInput.getFirstParam(),
                                Integer.parseInt(userInput.getSecondParam()));
                        break;
                    case GUESS :
                        server.sendClientInput(userInput.getFirstParam());
                        break;
                }
            } catch (UnknownCommandException | BadFormattedInputException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String readNextLine() {
        msgOut.print(PROMPT);
        return clientInput.nextLine();
    }

    private class OutputHandler implements OutputObserver {
        @Override
        public void printToTerminal(String output) {
            msgOut.println(output);
            msgOut.print(PROMPT);

        }

        @Override
        public void connected(InetSocketAddress address) {
            printToTerminal("Connected to " + address.getHostName() + ":" + address.getPort());
        }

        @Override
        public void disconnected() {
            printToTerminal("Game is closing, thanks for playing!");
        }
    }

    private String welcomeMsg() {
        return "Welcome to the hangman game!\n" +
                "please start off by connecting to a server, this is done by writing connect \"ip-adress\" \"port\"\n" +
                "you can then start guessing for the word by typing guess \"letter/word you want to guess\"\n" +
                "you can at any time quit the game by typing quit!";
    }
}
