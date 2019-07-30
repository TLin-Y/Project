/*
s * COMP6231 A1
 * Tianlin Yang 40010303
 * Gaoshuo Cui 40085020
 */

package server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Logger;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import functions.City;
import functions.Constants;
import logTool.allLogger;
import remoteObject.EventSystemImplementation;
import remoteObject.EventSystemInterface;
import remoteObject.EventSystemInterfaceHelper;

public class TOR_Server {

	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {
		EventSystemImplementation stub;
		try {
			setupLogging();
			// create & initialize the ORB;
			// get reference to rootPOA & activate POAManager
			ORB orb = ORB.init(args, null);
			POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootpoa.the_POAManager().activate();
			stub = new EventSystemImplementation("TOR", orb);
			// get object reference from the servant
			Object ref = rootpoa.servant_to_reference(stub);
			EventSystemInterface href = EventSystemInterfaceHelper.narrow(ref);

			Object objRef = orb.resolve_initial_references("NameService");
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			NameComponent path[] = ncRef.to_name(City.TOR.toString());
			ncRef.rebind(path, href);

		// start the city's UDP server for inter-city communication
		// the UDP server is started on a new thread
			new Thread(() -> {
				((EventSystemImplementation) stub).UDPServer();
			}).start();
			System.out.println("Toronto server started!");
			for (;;) {
				orb.run();
			}

		} catch (Exception e) {
			// TODO - catch only the specific exception
			e.printStackTrace();
		}
	}
	
	private static void setupLogging() throws IOException {
		File files = new File(Constants.SERVER_LOG_DIRECTORY);
        if (!files.exists()) 
            files.mkdirs(); 
        files = new File(Constants.SERVER_LOG_DIRECTORY+"TOR_Server.log");
        if(!files.exists())
        	files.createNewFile();
        allLogger.setup(files.getAbsolutePath());
	}

}
