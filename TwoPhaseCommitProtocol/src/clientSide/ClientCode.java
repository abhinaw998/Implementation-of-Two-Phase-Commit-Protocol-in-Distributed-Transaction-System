/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 *
 * @author aniket
 */
public class ClientCode {

    public static void main(String[] args) {
        try {
            /*
             * The remote objects are taken from the registry.
             * The coordinator class stores those remote objects. 
             */
            Coordinator coordinator = new Coordinator();

            /*
             * The coordinator distributes the transactional load
             * across all the cohert 
             */
            coordinator.distributeTask();

            /*
             * The coordinator asks for the vote of each cohert.
             * On the basis of response the coordinator will do the necassary.
             */
            coordinator.phaseOne();

            /*
             * The coordinator sends a global command to all the cohart. 
             */
            coordinator = null;
            coordinator.phaseTwo();

        } catch (NotBoundException | RemoteException e) {
            System.out.println(e);
        } catch (IOException | NullPointerException e) {
            System.out.println("Coordinator Failure!");
        }
    }
}
