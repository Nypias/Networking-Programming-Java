package bankjpa;

import java.rmi.Naming;

public class Server {
	static final String USAGE = "java bankrmi.Server [rmi-URL of a bank]" ;
	static final String BANK = "Nordea";

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String bankname = (args.length > 0)? args[0] : BANK;
		if (bankname.equalsIgnoreCase("-h")) {
			System.out.println(USAGE);
			System.exit(1);
		}
		try {
			Bank bankobj = new BankImpl();
			// Register the newly created bank at rmiregistry.
			Naming.rebind(bankname, bankobj);
			System.out.println(bankobj + " is ready.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
