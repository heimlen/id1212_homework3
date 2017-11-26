package se.kth.id1212.heimlen.homework3.view;

/**
 * Defines all commands that can be invoked by the client in the hangman game.
 */
public enum UserCommand {
        /**
         * Start the hangman game
         */
        START,
        /**
         * Make a guess in the hangman game
         */
        GUESS,
        /**
         * Establish a connection to the server. The first parameter is IP address (or host name), the second is port number.
         */
        CONNECT,
        /**
         * quit the game.
         */
        QUIT,
    }
