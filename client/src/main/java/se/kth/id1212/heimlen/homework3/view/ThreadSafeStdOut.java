package se.kth.id1212.heimlen.homework3.view;

/**
 * This class provides a thread safe output, all methods are synchronized.
 */
class ThreadSafeStdOut {
    /**
     * Thread safe mimic of print, prints to <code>System.out</code>, just as built-in equivalent
     *
     * @param output The output to print.
     */
    synchronized void print(String output) {
        System.out.print(output);
    }

    /**
     * Thread safe mimic of println, prints to <code>System.out</code>, just as built-in equivalent
     *
     * @param output The output to print.
     */
    synchronized void println(String output) {
        System.out.println(output);
    }
}
