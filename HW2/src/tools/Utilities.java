package tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Utilities {
	
	public final static int LIST_PRODUCT_MARKET = 0;
	public final static int LIST_PRODUCT_TRADER = 1;
	public final static int ITEM_ADDED_SALE = 2;
	public final static int ITEM_WISHED_RECEIVED = 3;
	public final static int ITEM_ALREADY_EXISTS = 4;
	public final static int ITEM_WISHED_AVAILABLE = 5;
	public final static int ITEM_WISHED_ALREADY_EXISTS = 6;
	public final static int WISH_REGISTERED = 7;
	public final static int WISH_CAN_BE_SERVED = 8;
	public final static int PRODUCT_BOUGHT = 9;
	public final static int PRODUCT_SOLD = 10;
	public final static int ALL_PRODUCTS_FROM_MARKET = 11;
        public final static int WISH_ALREADY_REGISTERED = 12;
        public final static int BALANCE_INSUFFICIENT = 13;
        public final static int ITEM_NOT_FOUND = 14;
        
        public final static int port = 1099;
        
     /**
     * Returns local ip address, code by jguru
     * @return
     * @throws UnknownHostException
     */
    public static InetAddress getCurrentEnvironmentNetworkIp() throws UnknownHostException { 
        Enumeration<NetworkInterface> netInterfaces = null;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            //log.error("Somehow we have a socket error...");
        }

        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                InetAddress addr = address.nextElement();
                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress() && !(addr.getHostAddress().indexOf(":") > -1)) {
                    return addr;
                }
            }
        }
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return InetAddress.getLocalHost();
        }
    }

}
