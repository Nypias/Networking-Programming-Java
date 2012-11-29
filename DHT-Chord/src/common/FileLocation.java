package common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author DHT-Chord Team
 */
public class FileLocation implements java.io.Serializable {
    InetAddress IP;
    int port;
    /**
     * Constructor
     * @param IP
     * @param port
     */
    /**
     * Constructor
     * @param IP
     * @param port
     */
    public FileLocation(InetAddress IP, int port) {
        this.IP = IP;
        this.port = port;
    }
    /**
     * Construcor
     * @param port
     * @throws UnknownHostException
     */
    public FileLocation(int port) throws UnknownHostException {
        this.port = port;
        this.IP=chord.Node.getCurrentEnvironmentNetworkIp();
    }

    //Getters Setters
    public InetAddress getIP() {
        return IP;
    }

    public void setIP(InetAddress IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString(){
      String s="";
      s+="Location\tIP:"+IP+" port:"+port;
      return s;
    }

}//FileLocation
