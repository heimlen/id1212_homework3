package se.kth.id1212.heimlen.homework3.view;

import se.kth.id1212.heimlen.homework3.exceptions.BadFormattedInputException;
import se.kth.id1212.heimlen.homework3.exceptions.UnknownCommandException;

import javax.jws.soap.SOAPBinding;
import java.util.Arrays;

/**
 * This class represents one line of user input which much start with a valid user-command and then possibly take another parameter.
 * */
public class UserInput {
    private final String enteredInput;
    private static final String PARAM_DELIMITER = " ";
    private String userCmd;
    private String[] parameters;
    private UserCommand userCommand;
    /**
     * Constructor taking a line of user input and parsing it, since this is a type of DTO everything is done in the constructor.
     * @param userInput the command that the user wants to run
     */
    UserInput(String userInput) throws UnknownCommandException, BadFormattedInputException {
        this.enteredInput = userInput;
        parameters = new String[3];
        splitInput(enteredInput);
        parseCommand(userCmd);
    }

    private void splitInput(String enteredInput) {
        if(enteredInput == null) {
            userCmd = null;
            parameters = null;
            return;
        }
        String[] splitInput = enteredInput.split(PARAM_DELIMITER);
        userCmd = splitInput[0].toUpperCase();
        parameters = Arrays.copyOfRange(splitInput,1,splitInput.length);
    }

    private void parseCommand(String userCmd) throws UnknownCommandException, BadFormattedInputException {
        if(userCmd == null) {
            throw new  BadFormattedInputException("This is not a correctly typed input, please enter input as COMMAND parameter instead");
        }

        switch(userCmd) {
            case "LOGIN" :
                userCommand = UserCommand.LOGIN;
                break;
            case "LOGOUT" :
                userCommand = UserCommand.LOGOUT;
                break;
            case "REGISTER" :
                userCommand = UserCommand.REGISTER;
                break;
            case "UNREGISTER" :
                userCommand = UserCommand.UNREGISTER;
                break;
            case "LS" :
                userCommand = UserCommand.LS;
                break;
            case "DOWNLOAD" :
                userCommand = UserCommand.DOWNLOAD;
                break;
            case "UPLOAD" :
                userCommand = UserCommand.UPLOAD;
                break;
            case "HELP" :
                userCommand = UserCommand.HELP;
                break;
            case "QUIT" :
                userCommand = UserCommand.QUIT;
                break;
            default:
                throw new UnknownCommandException("the entered command " + userCmd + " is unknown, please enter a known command");
        }
    }

    UserCommand getUserCommand() {
        return userCommand;
    }

    String getFirstParam() {
        return parameters[0];
    }

    String getSecondParam() {
        return parameters[1];
    }
}
