package server;

import java.util.Properties;

import org.omg.CORBA.*;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;

import ggt.KoordinatorHelper;

public class Koordinator {

	static public ORB orb;

	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("Koordinator Namen eingeben...");
		} else {

			try {
				// ORB Eigenschaften setzen
				Properties props = new Properties();
				props.put("org.omg.CORBA.ORBInitialPort", "1050");
				props.put("org.omg.CORBA.ORBInitialHost", "localhost");
				orb = ORB.init(args, props);

				// Referenz von rootPOA holen und POA Manager aktivieren
				POA rootPoa = POAHelper.narrow(orb
						.resolve_initial_references("RootPOA"));
				rootPoa.the_POAManager().activate();

				// Servant erzeugen
				KoordinatorImpl koord = new KoordinatorImpl(rootPoa);

				// Referenz fuer den Servant besorgen
				org.omg.CORBA.Object ref = rootPoa.servant_to_reference(koord);

				// Downcast Corba-Objekt -> koordinator
				ggt.Koordinator href = KoordinatorHelper.narrow(ref);

				// Referenz zum Namensdiesnt (root naming context) holen
				org.omg.CORBA.Object objRef = orb
						.resolve_initial_references("NameService");

				// Verwendung von NamingContextExt, ist Teil der Interoperable
				// Naming Service (INS) Spezifikation.
				NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

				// binde die Object Reference an einen Namen
				String name = args[0];
				NameComponent path[] = ncRef.to_name(name);
				ncRef.rebind(path, href);
				System.out.println("Koordinator laeuft ...");

				// Orb starten und auf Clients warten
				orb.run();
			} catch (Exception e) {
				System.err.println("Fehler: " + e);
				e.printStackTrace(System.out);
			}
			System.out.println("Koordinator Exit");
		}
		

	}
	public static ORB getORB(){
		return orb;
	}

}