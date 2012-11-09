package chord;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides TCP connectivy. Acts as a server and client
 * @author Barbarossa Team
 */
public class ServerRbl implements Runnable
{
    private int destinationPort;
    private InetAddress destinationAddr;
    private ServerSocket ss;
    private Node node;
    private String addr;
    private String procid;
    private String rmiaddress;
    private String robj;
   /**
    * Constructor
    * @param node
    * @throws RemoteException
    */
    public ServerRbl(Node node) throws RemoteException
    {
        super();
        this.node = node;
 
    }//constructor

    ServerRbl(ServerSocket ss, Node node)
    {
        this.node = node;
        this.ss = ss;
    }

    ServerRbl(int tcpPort, InetAddress clientAddr, Node node) {
        this.destinationPort = tcpPort;
        this.destinationAddr = clientAddr;
        this.node = node;
      
    }
    ServerRbl(int tcpPort, InetAddress clientAddr) {
        this.destinationPort = tcpPort;
        this.destinationAddr = clientAddr;

    }
   
    public void run()
    {
        if (destinationAddr != null)
        {
            try {
                
                Socket s = new Socket();
                s.connect(new InetSocketAddress(destinationAddr, destinationPort), 10000);
               
                ObjectOutputStream responseObj = new ObjectOutputStream(s.getOutputStream());
                robj = new String(s.getLocalAddress().toString()+"\n"+new Integer(node.getProccessId()).toString());
               
                responseObj.writeObject(robj);
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerRbl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                

    }//run()


public String HearResponce(ServerSocket ss){
    String addr;
    String procid;
    String rmiaddress;
    String robj;
            InputStream inStream = null;
            try
            {
                ss.setSoTimeout(1000);
                Socket incoming = ss.accept();

                inStream = incoming.getInputStream();
                //Scanner in = new Scanner(inStream);
                ObjectInputStream in = new ObjectInputStream(inStream);
                try {
                   String response = new String((String) in.readObject());

                    //Tokenizing gia na paroume ip kai proccessid
                    StringTokenizer st = new StringTokenizer(response);
                    addr = st.nextToken("\n");
                    procid = st.nextToken("\n");
                    String temp = new String ("rmi:/"+ addr +"/Chord-"+ procid);
                    node.window.getChordActivityText().append("got responce:"+temp+"\n");
                    return temp;

                    //FindSuccessor(0, node);

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ServerRbl.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (RemoteException ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
               }
                inStream.close();
            } catch (SocketTimeoutException ste) {
                node.window.getChordActivityText().append("No one is here! Creating my Chord\n");
                //System.err.println("No one is here! Creating my Chord");
                return null;
            } catch (IOException ex) {
                Logger.getLogger(ServerRbl.class.getName()).log(Level.SEVERE, null, ex);
            }
            node.window.getChordActivityText().append("got no responce");
            return null;
        }//HearResponce


     public synchronized String getAddr() {
        return addr;
    }

    public synchronized void setAddr(String addr) {
        this.addr = addr;
    }
       public synchronized String getClientRmiaddress() {
        return rmiaddress;
    }

    public synchronized void setClientRmiaddress(String rmiaddress) {
        this.rmiaddress = rmiaddress;
    }
    public synchronized String getProcid() {
        return procid;
    }

    public synchronized void setProcid(String procid) {
        this.procid = procid;
    }

    public synchronized String getRobj() {
        return robj;
    }
}
