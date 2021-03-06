package server;

import ggt.Process;
import ggt.Starter;
import ggt.KoordinatorPackage.EStarterNoSuchElement;

import java.util.List;

public class TerminatorThread extends Thread {
	private int termTime;
	private List<Process> ring;
	private KoordinatorImpl koord;
	private volatile boolean running = true;
	private int sequenz = 1;

	public TerminatorThread(int termTime, List<Process> ring,
			KoordinatorImpl koord) {
		super();
		this.termTime = termTime;
		this.ring = ring;
		this.koord = koord;
	}

	//Die run Methode wartet die uebergebene Zeit und startet dann den Terminierungsalgo um zu 
	//prüfen ob die Processe fertig sind mit Ihren Berechnungen
	@Override
	public void run() {
		
		try {
			sleep(termTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		while (running) {
			try {

				int randomPlace = (int) Math.round(Math.random()
						* (ring.size() - 1));

				if (!koord.terminated) {
					ring.get(randomPlace).sendMarker("Koordinator", sequenz);
					sequenz++;

				} else {
					running = false;

				}
				sleep(termTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void setRunning(boolean status){
		this.running = status;
	}
}
