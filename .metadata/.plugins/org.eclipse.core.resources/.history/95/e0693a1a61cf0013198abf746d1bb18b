package client;

import ggt.*;

import java.util.*;

import monitor.Monitor;
import monitor.MonitorHelper;

import org.omg.CORBA.*;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import ggt.KoordinatorHelper;
import ggt.KoordinatorPackage.EInvalidAmount;
import ggt.KoordinatorPackage.EStarterNoSuchElement;

public class Client {
//args <Nameservice><operation><koordname><Monitorname><minProcesses><maxProcesses><minDelay><maxDelay><timeout><ggt>
	public static void main(String args[]) {

		try {

			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialPort", "1050");
			props.put("org.omg.CORBA.ORBInitialHost", "localhost");
			ORB orb = ORB.init(args, props);

			// NamingContext besorgen
			NamingContextExt nc = NamingContextExtHelper.narrow(orb
					.resolve_initial_references(args[0]));

			// Objektreferenz mit Namen "koordinator" besorgen
			org.omg.CORBA.Object obj = nc.resolve_str(args[2]);
			
			// Down-Cast
			Koordinator koordinator = KoordinatorHelper.narrow(obj);
			
			switch (args[1]) {
			case "starterliste"	:
				try{
					
					Starter[] starters = koordinator.getStarterListe();
					for(int i = 0; i < starters.length; i++){
						System.out.println("Starter " + i + ": " + starters[i].name());
				}
				}catch(EStarterNoSuchElement e){
					System.out.println(e.s);
				}

				break;
				 
			case "start"	:
				// Monitor Objektreferezn
				org.omg.CORBA.Object obj2 = nc.resolve_str(args[3]);
				Monitor monitor = MonitorHelper.narrow(obj2);
				koordinator.registerMonitor(monitor);
				try{
				koordinator.startCalculation(	Integer.parseInt(args[4]),
												Integer.parseInt(args[5]), 
												Integer.parseInt(args[6]), 
												Integer.parseInt(args[7]), 
												Integer.parseInt(args[8]), 
												Integer.parseInt(args[9]));
				}catch(EInvalidAmount e){
					System.out.println(e.s);
				}
				System.out.println("Kalkulation gestartet");	
				break;
			case "ende"		:
				System.out.println("client exit");				
				koordinator.exit();
				break;
				
			default			:
				System.out.println("ungueltige operation");
			}
			
		}
			
			
		 catch (Exception ex) {
			System.err.println(ex);
			System.exit(1);
		}
	}
}

