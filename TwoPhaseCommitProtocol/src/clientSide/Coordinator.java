/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import ResourcePool.SharedResource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import serverSide.Cohart;

/**
 *
 * @author aniket
 */
public class Coordinator {

    private final Cohart[] serverObject = new Cohart[2];
    private String coordinator_response;
    private final SharedResource sharedResource;

    /*
     * 
     * @throws RemoteException
     * @throws NotBoundException 
     */
    public Coordinator() throws RemoteException, NotBoundException, IOException {
        Registry registry = LocateRegistry.getRegistry(1099);
        for (int i = 0; i < 2; i++) {
            serverObject[i] = (Cohart) registry.lookup("cohart" + i);
        }
        sharedResource = (SharedResource) registry.lookup("coordinatorLogFileObject");
        //new File("Resource/Coordinator/coordinator_log.txt").createNewFile();
    }

    /*
     * 
     * @throws RemoteException 
     */
    public void distributeTask() throws RemoteException, IOException {
        for (Cohart serverObject1 : serverObject) {
            serverObject1.assignTask();
        }
    }

    public void phaseOne() throws RemoteException, IOException {
        /*
         * Collect the vote in a list 
         */
        ArrayList<String> responseVote = new ArrayList<>();
        for (Cohart serverObject1 : serverObject) {
            responseVote.add(serverObject1.voteCohart());
        }

        /*
         * Display the response from all the cohart 
         */
        System.out.println("Response from the cohert");
        System.out.println(responseVote.toString());

        /*
         * Global decision 
         */
        coordinator_response = (responseVote.contains("abort")) ? "abort" : "commit";
    }

    public void phaseTwo() throws IOException {
        /*
         * Write the coordinator response to a file.
         * 
         */
        sharedResource.writeLogCoordinator("Global command " + coordinator_response + " [" + new Date().toString() + "]");

        for (Cohart serverObject1 : serverObject) {
            if (this.coordinator_response.equalsIgnoreCase("commit")) {
                serverObject1.commitCohart("Phase Two Response commit [" + new Date().toString() + "]");
            } else {
                serverObject1.rollbackCohart("Phase Two Response abort [" + new Date().toString() + "]");
            }
        }

    }

    public void coordinatorFailure() {
        /*
         * The coordinator gets failed
         * The cohart doesnt't know what to do next
         * All the resources are being held 
         */

    }

    public void writeLogCohart(String command) throws RemoteException, IOException {
        try (FileWriter fw = new FileWriter("Resource/Coordinator/coordinator_log.txt", true)) {
            fw.write(command + "\n");
        }
    }

}
