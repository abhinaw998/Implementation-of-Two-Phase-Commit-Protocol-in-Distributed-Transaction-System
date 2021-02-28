/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResourcePool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author aniket
 */
public class CoordinatorLog extends UnicastRemoteObject implements SharedResource {

    private final String FILE_NAME = "Resource/Coordinator/coordinator.txt";
    private final File fptr;

    public CoordinatorLog() throws RemoteException, IOException {
        fptr = new File(FILE_NAME);
        fptr.createNewFile();
    }

    @Override
    public void writeLogCoordinator(String command) throws RemoteException, IOException {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(command + "\n");
        }
    }

    @Override
    public boolean readLogCoordinator() throws RemoteException, IOException {
        return fptr.length() != 0;
    }

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.bind("coordinatorLogFileObject", new CoordinatorLog());
        } catch (AlreadyBoundException | RemoteException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
