/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverSide;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author aniket
 */
public interface Cohart extends Remote {

    /*
     * 
     * @throws RemoteException 
     */
    public void assignTask() throws RemoteException, IOException;

    /*
     * 
     * @throws RemoteException 
     */
    public String voteCohart() throws RemoteException, IOException;

    /*
     * 
     * @throws RemoteException 
     */
    public void commitCohart(String command) throws RemoteException, IOException;

    /*
     * 
     * @throws RemoteException 
     */
    public void rollbackCohart(String command) throws RemoteException, IOException;

    /*
     * 
     * @throws RemoteException 
     */
    public void writeLogCohart(String command) throws RemoteException, IOException;

    /*
     * 
     * @throws RemoteException 
     */
    public void readLogCohart() throws RemoteException;
}
