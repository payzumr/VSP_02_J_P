package starter;

import java.util.Properties;

import org.omg.CORBA.*;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.*;

import ggt.KoordinatorHelper;
import ggt.StarterHelper;

public class Starter {

	static public ORB orb;

	public static void main(String args[]) {
		if (args.length == 0) {
			System.out.println("Starter Namen eingeben...");
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

				// NamingContext besorgen
				NamingContextExt nc = NamingContextExtHelper.narrow(orb
						.resolve_initial_references("NameService"));

				
				// Objektreferenz mit Namen "koordinator" besorgen
				org.omg.CORBA.Object obj = nc.resolve_str(args[1]);

				// Referenz fuer den Servant besorgen
				ggt.Koordinator koord = KoordinatorHelper.narrow(obj);
				
				// Servant erzeugen
				StarterImpl starter = new StarterImpl(args[0],koord,rootPoa);

				// Referenz fuer den Servant besorgen
				org.omg.CORBA.Object ref = rootPoa.servant_to_reference(starter);

				// Downcast Corba-Objekt -> starter
				ggt.Starter href = StarterHelper.narrow(ref);

				//starter bei koordinator anmelden
				koord.activateStarter(href, args[0]);

				// binde die Object Reference an einen Namen
				String name = args[0];
				NameComponent path[] = nc.to_name(name);
				nc.rebind(path, href);
				System.out.println("Starter laeuft ...");

				// Orb starten und auf Clients warten
				orb.run();
				//entfernen der StarterImpl wenn ORB Shutdown
				koord.deleteStarter(href);
			} catch (Exception e) {
				System.err.println("Fehler: " + e);
				e.printStackTrace(System.out);
			}
			System.out.println("Starter Exit");
		}
		

	}
	public static ORB getORB(){
		return orb;
	}

}
