package KeyManager;

import common.FileLocation;
/**
 *
 * @author Barbarossa Team
 */
public class Entry implements java.io.Serializable {

    int hash;
    FileLocation location;
    /**
     * Constructor
     * @param hash
     * @param location
     */
    public Entry(int hash, FileLocation location){
        this.hash = hash;
        this.location = location;
    }
    /**
     * Constructor
     * @param e
     */
    public Entry(java.util.Map.Entry<Integer,FileLocation> e){
        this.hash = e.getKey();
        this.location =e.getValue();
    }
    //Getters Setters
    
    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public FileLocation getLocation() {
        return location;
    }

    public void setLocation(FileLocation location) {
        this.location = location;
    }

    @Override
    public String toString(){
        return hash+":"+location;
    }
}
