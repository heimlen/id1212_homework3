package se.kth.id1212.heimlen.homework3.startup;

import se.kth.id1212.heimlen.homework3.FileSystem;
import se.kth.id1212.heimlen.homework3.controller.Controller;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Starts the filesystem and binds it in the RMI registry.
 * */
public class Server {
    private String filesystemName = FileSystem.FILESYSTEM_NAME_IN_REGISTRY;

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startRegistry();
            System.out.println("Filesystem server started.");
            Naming.rebind(Controller.FILESYSTEM_NAME_IN_REGISTRY, new Controller());
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Could not connect to file system.");
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
}
