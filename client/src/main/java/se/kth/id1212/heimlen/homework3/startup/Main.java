package se.kth.id1212.heimlen.homework3.startup;

import se.kth.id1212.heimlen.homework3.FileSystem;
import se.kth.id1212.heimlen.homework3.view.InputInterpreter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by heimlen on 2017-11-26.
 */
public class Main {

    public static void main(String[] args) {
        try {
            FileSystem fileSystem = (FileSystem) Naming.lookup(FileSystem.FILESYSTEM_NAME_IN_REGISTRY);
            new InputInterpreter().start(fileSystem);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

}
