package se.kth.id1212.heimlen.homework3.view;

/**
 * Defines all commands that can be invoked by the client in the hangman game.
 */
public enum UserCommand {
        /**
         * Registers a new user
         */
        REGISTER,
        /**
         * Unregisters a user
         */
        UNREGISTER,
        /**
         * Logs a user in
         */
        LOGIN,
        /**
         * Logs a user out
         */
        LOGOUT,
        /**
         * Downloads a file
         */
        DOWNLOAD,
        /**
         * Uploads a file
         */
        UPLOAD,
        /**
         * List available directories.
         * */
        LS,
        /**
         * Show the help menu
         */
        HELP,
        /**
         * quit the application.
         */
        QUIT,

}
