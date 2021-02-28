/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import ResourcePool.SharedResource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aniket
 */
public class CohartClass extends UnicastRemoteObject implements Cohart {

    private final String cohartID;
    private final String logFile;
    private static final String[] RESPONSE = new String[]{"abort", "commit", "abort", "commit", "abort",};
    private ScheduledFuture check;

    public CohartClass() throws RemoteException, IOException {
        cohartID = UUID.randomUUID().toString();
        logFile = "Resource/Cohart/" + cohartID + ".txt";
        new File(logFile).createNewFile();
    }

    @Override
    public void assignTask() throws RemoteException, IOException {
        this.writeLogCohart("Task Assigned " + " [" + new Date().toString() + "]");
    }

    @Override
    public String voteCohart() throws RemoteException, IOException {
        String response = RESPONSE[(int) (Math.random() * 5)];
        this.writeLogCohart("Phase One Vote " + response + " [" + new Date().toString() + "]");
        this.checkCoordinatorFailure();
        return response;
    }

    @Override
    public void commitCohart(String command) throws RemoteException, IOException {
        /*
         * Cohart will commit all the changes and write it to its log file
         */
        this.writeLogCohart(command);

    }

    @Override
    public void rollbackCohart(String command) throws RemoteException, IOException {
        /*
         * Cohart will commit all the changes and write it to its log file
         */
        this.writeLogCohart(command);

    }

    @Override
    public void readLogCohart() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeLogCohart(String command) throws RemoteException, IOException {
        try (FileWriter fw = new FileWriter(logFile, true)) {
            fw.write(command + "\n");
        }
    }

    public void checkCoordinatorFailure() {
        this.check = Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                Registry registry = LocateRegistry.getRegistry(1099);
                SharedResource remoteCoordinatorLog = (SharedResource) registry.lookup("coordinatorLogFileObject");
                if (!remoteCoordinatorLog.readLogCoordinator()) {
                    /*
                     * Phase two has not initiated
                     * Its been 30 seconds since the phase one.
                     * So now the cohart is rolling back
                     */
                    this.rollbackCohart("Coordinator Failure abort [" + new Date().toString() + "]");
                    remoteCoordinatorLog.writeLogCoordinator("Cohart abort due to coordinator failure [" + new Date().toString() + "]");
                    System.out.println("Cohart aborted due to failure!");
                }
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(CohartClass.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                System.out.println(e);
            }
        }, 5, TimeUnit.SECONDS);

    }

}
