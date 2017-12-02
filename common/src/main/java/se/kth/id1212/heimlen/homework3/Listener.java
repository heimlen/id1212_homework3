package se.kth.id1212.heimlen.homework3;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Message handling.
 */
public interface Listener extends Remote {
    /**
     * @param msg <code>String</code> to print.
     */
    void printToTerminal(String msg) throws RemoteException;

    /**
     * @param msg <code>String</code> with a custom error message.
     * @param e   <code>Exception</code> which contains information of what went wrong.
     */
    void errorToTerminal(String msg, Exception e) throws RemoteException;

    /**
     * Information about a disconnection.
     */
    void disconnected() throws RemoteException;
}
