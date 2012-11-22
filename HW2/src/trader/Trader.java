package trader;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author teo
 */
public interface Trader extends Remote {
    public void wishAvailable() throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String name) throws RemoteException;
    public void sendNotification(Integer typeMessage, Object message) throws RemoteException;
}
