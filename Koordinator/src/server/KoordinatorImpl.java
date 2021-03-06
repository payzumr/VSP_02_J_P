package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import monitor.Monitor;
import ggt.Koordinator;
import ggt.KoordinatorHelper;
import ggt.KoordinatorPOA;
import ggt.Process;
import ggt.Starter;
import ggt.KoordinatorPackage.EAlreadyExists;
import ggt.KoordinatorPackage.EInvalidAmount;
import ggt.KoordinatorPackage.EStarterNoSuchElement;
import ggt.KoordinatorPackage.exAlreadyExists;
import ggt.StarterPackage.EInvalidCount;
import ggt.StarterPackage.exInvalidCount;

public class KoordinatorImpl extends KoordinatorPOA {

	private Map<Starter, String> starters = new HashMap<Starter, String>();
	private List<Process> processes = new ArrayList<Process>();
	private List<Process> ringProcesses = new ArrayList<Process>();
	Monitor monitor;
	private Koordinator koord;
	private POA poa;

	private int aktuelleSeqN;
	private int terminatedProcessCounter;
	boolean terminated;
	ggt.Process lastProcess;
	private TerminatorThread tt;

	public KoordinatorImpl(POA poa) {
		this.poa = poa;
	}

	//New Process add to List
	@Override
	public void registerProcess(Process process) {
		processes.add(process);

	}

	//Diese Methode prüft die Terminierung der Porcesse. Wenn alle Processe terminiert sind setz er einen
	//Boolean auf true damit der TerminierungsThread bescheid weiss und alle Processe beenden kann
	@Override
	public synchronized void terminationCcheck(Process terminator, int seqN, boolean status) {
		if ((aktuelleSeqN == seqN) && status) {
			terminatedProcessCounter++;
			if (terminatedProcessCounter == ringProcesses.size()) {
				lastProcess = terminator;
				terminated = true;
				tt.setRunning(false);
				//Aufruf der Exit Methode der Starter					
				Starter[] tmp = null;
				try {
					tmp = koord.getStarterListe();
				} catch (EStarterNoSuchElement e) {
					e.printStackTrace();
				}
				for (int i = 0; i < tmp.length; i++) {
					tmp[i].processExit();
				}
				monitor.ergebnis(lastProcess.name(), lastProcess.mi());
			}
		} else if (aktuelleSeqN < seqN) {
			if (status) {
				terminatedProcessCounter = 1;
			} else {
				terminatedProcessCounter = 0;
			}
			aktuelleSeqN = seqN;
		}
	}
	//Diese Methode fuegt neue Starter hinzu
	@Override
	public synchronized void activateStarter(Starter starter, String starterName) throws EAlreadyExists {
		if(starters.containsKey(starter)){
			throw new EAlreadyExists("Error: Starter named " + starterName + " allready exists.");
		}else{
			starters.put(starter, starterName);
		}
		
	}

	@Override
	public synchronized void deleteStarter(Starter starter) {
		starters.remove(starter);
	}
	@Override
	public synchronized void registerMonitor(Monitor monitor) {
		this.monitor = monitor;
	}


	@Override
	public synchronized void startCalculation(int minProcesses, int maxProcesses, int minDelay, int maxDelay, int timeout, int startGGT) throws EInvalidAmount {
		
		//Alle Werte prüfen
		if((maxProcesses <= 0) || (minProcesses <= 0) || (minDelay <= 0) || (maxDelay <= 0) || (timeout <= 0) || (startGGT <= 0) ){
			throw new EInvalidAmount("Error: Only positive Amounts are available");
		}else if((minProcesses > maxProcesses) || (minDelay > maxDelay)){
			throw new EInvalidAmount("Error: Min value is greater then max value");
		}
		
		//Anzeigen der Uebergebenen Werte
		System.out.printf("Starte Berechnung mit folgenden Werten: \n Min/Max Prozesse: %d / %d \n Min/Max Delay: %d ms / %d ms \n Terminierungsabfragenzeit: % dms \n Gewuenschter GGT: %d \n",
				minProcesses, maxProcesses, minDelay, maxDelay, timeout, startGGT);

		// Alle Variablen initialisieren
		Map<Integer, Process> newList = new HashMap<Integer, Process>();
		ringProcesses.clear();
		aktuelleSeqN = 1;
		terminatedProcessCounter = 0;
		terminated = false;

		try {
			koord = KoordinatorHelper.narrow(poa.servant_to_reference(this));
		} catch (ServantNotActive | WrongPolicy e1) {
			e1.printStackTrace();
		}
		
		//Jetzt werden alle Starter aufgefordert eine Random Menge zw. minProcesses und maxProcesses zu erstellen
		for (Starter e : starters.keySet()) {
			int numOfProcess = (int) Math.round(Math.random() * (maxProcesses - minProcesses)) + minProcesses;
			try {
				e.createProcess(numOfProcess);
			} catch (EInvalidCount ex) {
				System.out.println(ex.s);
			}
		}
		
		//Die Processe werden dann zufällig zu einem Ring zusammengebaut
		int length = processes.size();
		for (int j = 0; j < length; j++) {
			int randomPlace = (int) Math.round(Math.random() * (processes.size() - 1));
			ringProcesses.add(processes.get(randomPlace));
			processes.remove(randomPlace);
		}
		int[] startZahlen = new int[ringProcesses.size()];
		
		//Jetzt bekommen alle Processe die Infos welche Nachbarn sie besitzen und welchen zufälligen Startwert sie haben
		//Zusätzlich geben wir noch die Corba Referenz zu dem Monitor mit damit der Process direkt zugriff für die Ausgaben hat
		for (int i = 0; i < ringProcesses.size(); i++) {

			int startGGTRandom = startGGT * ((int) Math.round(Math.random() * 100) + 1) * ((int) Math.round(Math.random() * 100) + 1);
			startZahlen[i] = startGGTRandom;
			int delay = (int) Math.round(Math.random() * minDelay) + maxDelay;

			if (i == 0) {
				ringProcesses.get(i).init(ringProcesses.get(i + 1), ringProcesses.get(ringProcesses.size() - 1), startGGTRandom, delay, monitor, koord);
			} else if (i == ringProcesses.size() - 1) {
				ringProcesses.get(i).init(ringProcesses.get(0), ringProcesses.get(i - 1), startGGTRandom, delay, monitor, koord);
			} else {
				ringProcesses.get(i).init(ringProcesses.get(i + 1), ringProcesses.get(i - 1), startGGTRandom, delay, monitor, koord);
			}
			newList.put(startGGTRandom, ringProcesses.get(i));
		}
		
		//String Array bauen fuer den Monitor
		String[] showRing = new String[ringProcesses.size()];
		for (int i = 0; i < ringProcesses.size(); i++) {
			showRing[i] = ringProcesses.get(i).name();
		}
		//Monitor den Ring und die Startzahlen mitteilen
		monitor.ring(showRing);
		monitor.startzahlen(startZahlen);

		// 3 Prozesse mit der kleinsten Zahl starten
		List<Integer> sortList = new ArrayList<Integer>(newList.keySet());
		Collections.sort(sortList);
		newList.get(sortList.get(0)).startCalulation();
		newList.get(sortList.get(1)).startCalulation();
		newList.get(sortList.get(2)).startCalulation();

		//Terminierungstread starten. Dieser startet alle timeout millisekunden den TerminationAlgo
		tt = new TerminatorThread(timeout, ringProcesses, this);
		tt.start();

	}
	//Starter Liste dem Client bei Anfrage liefern
	@Override
	public synchronized Starter[] getStarterListe() throws EStarterNoSuchElement{
		Starter[] tmp;
		if(starters.isEmpty()){
			throw new EStarterNoSuchElement("No Starter registert");
		}else{
			tmp = new Starter[starters.size()];
			int i = 0;
			for (Starter e : starters.keySet()) {
				tmp[i] = e;
				i++;
			}
		}

		return tmp;
	}
    //Koordinator beenden Erst werden die Starter beendet und dann beendet sich der Koordinator waehrend sich die Starter
	//beenden warten der Koordinator eine gewissen zeit
	@Override
	public synchronized void exit() {
		System.out.println("Koordinator beendet...");
		tt.setRunning(false);
		try{
			if (!starters.isEmpty()) {
				for (Starter e : getStarterListe()) {
					e.exit();
				}
			}
			
		}catch( EStarterNoSuchElement e){
			System.out.println(e.s);
		}
		
		sleep();
	}

	static void sleep() {
		try {
			new Thread() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					server.Koordinator.getORB().shutdown(true);
					System.exit(0);
				}
			}.start();
		} catch (Exception e) {
			System.out.println("exit failed");
		}
	}

}
