package chord;

import common.GUI.MainWindow;
import common.Hasher;
import java.net.ConnectException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Barbarossa Team
 */

public class FingerTable implements Runnable
{
    private ChordInterface[] fingertable;
    private ChordInterface[] compressedfinger;
    private Set<ChordInterface> set; //Set to hold each Node once
    private Node n;
    MainWindow window;
    Thread updater;

    /**
     * Constructor
     * @param n
     * @param window
     */

    public  FingerTable(Node n, MainWindow window)
    {
        
            this.n = n;
            this.window = window;
            fingertable = new ChordInterface[32]; //m=32 bits of int
            set = new HashSet<ChordInterface>();
             for (int i = 1; i <= fingertable.length; i++) {
                fingertable[i - 1] = n;
                set.add(fingertable[i - 1]); //Krata 1 fora to kathe node
            } //for
            
            updater = new Thread(this);
            updater.start();
        

    }//constructor
    /**
     * Compresses the Finger Table
     */
    private synchronized void compress()
    {
        compressedfinger = new ChordInterface[set.size()];
        compressedfinger[0]=fingertable[0];
        int cfi = 1;
        for (int i = 1; i < 32; i++)
        {
            if(fingertable[i]!=fingertable[i-1])
            {
                compressedfinger[cfi]=fingertable[i];
                cfi++;
            }
        }

    }
    /**
     * Returs a String representation of FingerTable
     * @return
     */
    public  void printFingerTable()
    {
            String s = new String();
            s+="Compressed Finger Table: \n";
            Iterator it = set.iterator();
            while (it.hasNext())
            {
                // Get element
                ChordInterface cint = (ChordInterface)it.next();
                try
                {
                    s += cint.getNodeKey()+"\n";
                }
                catch (RemoteException ex) {
                   System.err.println("print finger table remote exception");
                }
            }
            s+="=======================================\n";
            for(int i=1; i<=fingertable.length;i++)
            {
                try {
                    int temp =(int)Math.pow((double)2,(double)(i - 1));
                    int sumtemp = n.getNodeKey() + temp;
                    s += i+"NK: " + /*n.getNodeKey() + " + " + temp + */" = "+sumtemp+" || " + fingertable[i-1].getNodeKey() + "\n";
                } catch (RemoteException ex) {
                    System.err.println("print fingertable remote exception");
                }

            }

        window.getFingerTableText().setText(s);
    }
     /**
     * Fix the Finger table periodically
     */
    public synchronized void fixfingers()
    {   
          set.clear();
          for(int i=1; i<=fingertable.length;i++)
            {
                try
                {
                    int sumtemp = n.getNodeKey() + (int)Math.pow((double)2,(double)(i - 1));
                    fingertable[i-1] = n.FindSuccessor(sumtemp);
                    set.add(fingertable[i-1]); //Hold each node once
                }

                catch (RemoteException ex) {
                   System.err.println("fix fingers remote exception");
                }
            }
         //  compress();
     }

    /**
     * Checks for closest preceding Node in Finger Table
     * @param k
     * @return
     */
    public ChordInterface checkprecNode(int k)// throws Exception
    {
        for(int i=fingertable.length-1;i>=0;i--)
        {
            try {
                if (Hasher.isBetween(n.getNodeKey(), fingertable[i].getNodeKey(), k)) {
                    return fingertable[i];
                }
            } catch (RemoteException ex) {
               System.err.println("checkprecNode remote exception");
            }
        }//for
        return n;
    }//checkprecNode

     public ChordInterface[] getFingertable() {
        return fingertable;
     }

    public void setFingertable(ChordInterface[] fingertable) {
        this.fingertable = fingertable;
    }

    public void reset(){

            for (int i = 1; i <= fingertable.length; i++) {
                fingertable[i - 1] = n;
                set.add(fingertable[i - 1]); //Krata 1 fora to kathe node
            } //for
           
        fixfingers();
    }

    public void run() {

            while (true) {
                try {
                    n.getSuccessors(0).getNodeKey();//check only
                    fixfingers();
                    Thread.sleep((long) 2000);
                } catch (InterruptedException ex) { System.err.println("fingerTable,run interrupted exception"); }
                  catch (RemoteException ex) {
                  System.err.println("fingerTable,run remote exception");
                  try {Thread.sleep(500);}catch (InterruptedException ex1) {  }
                   }//catch interruptred exception
                  catch(Exception e){System.err.println("fingerTable,run: "+e.getClass());}
      
            }//while(true)
    }//run


}
