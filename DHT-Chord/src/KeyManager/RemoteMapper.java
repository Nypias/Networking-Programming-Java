package KeyManager;

import common.FileLocation;

/**
 *
 * @author Barbarossa Team
 */
public interface RemoteMapper extends java.rmi.Remote {
   
    public void addEntry(Entry e)throws java.rmi.RemoteException;
    public void addMap(java.util.TreeMap<Integer,FileLocation> toAdd)throws java.rmi.RemoteException;
    public void removeKey(Integer k)throws java.rmi.RemoteException;
    public FileLocation lookup(int key)throws java.rmi.RemoteException;
    

}
