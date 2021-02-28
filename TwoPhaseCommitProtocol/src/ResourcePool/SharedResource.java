/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResourcePool;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author aniket
 */
public interface SharedResource extends Remote {

    public void writeLogCoordinator(String command) throws RemoteException, IOException;

    public boolean readLogCoordinator() throws RemoteException, IOException;
}
