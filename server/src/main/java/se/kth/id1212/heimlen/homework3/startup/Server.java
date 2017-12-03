package se.kth.id1212.heimlen.homework3.startup;

import se.kth.id1212.heimlen.homework3.FileSystem;
import se.kth.id1212.heimlen.homework3.controller.Controller;
import se.kth.id1212.heimlen.homework3.net.SocketListener;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Starts the filesystem-server and binds it in the RMI registry.
 * */
public class Server {
    private String filesystemName = FileSystem.FILESYSTEM_NAME_IN_REGISTRY;

    public static void main(String[] args) {
        // Set logging to a more reasonable level
        Logger log = Logger.getLogger("org.hibernate");
        log.setLevel(Level.SEVERE);

        try {
            Server server = new Server();
            Controller controller = new Controller();
            server.startRegistry();
            System.out.println("Filesystem server started.");
            Naming.rebind(Controller.FILESYSTEM_NAME_IN_REGISTRY, controller);
            server.startSocketListener(controller);
        } catch (RemoteException e) {
            System.out.println("Could not connect to file system.");
        } catch(MalformedURLException e) {
            System.out.println("malformed url exception");
        }
    }

    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }

    private void startSocketListener(Controller controller) {
        new SocketListener(controller);
    }
}
