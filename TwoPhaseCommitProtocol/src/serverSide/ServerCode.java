/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author aniket
 */
public class ServerCode extends CohartClass {

    public ServerCode() throws RemoteException, IOException {
        super();
    }

    public static void main(String[] args) throws IOException, AlreadyBoundException {
        try {
            /*
             * A registry is created on the port 1099
             */
            Registry registry = LocateRegistry.createRegistry(1099);

            /* 
             * For the time being two coherts as remote object is added to the registry. 
             */
            registry.bind("cohart0", new ServerCode());
            registry.bind("cohart1", new ServerCode());
 
            /*
             * 
             */
            System.out.println("Server Started");
        } catch (RemoteException e) {
            System.out.println(e);
        }
    }
}
