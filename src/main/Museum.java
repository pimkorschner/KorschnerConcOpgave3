package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
	
	Lock lock;
	
	private int burgersInRij = 0;
	private int beroemdhedenInRij = 0;
	private int beroemdhedenCount = 0;
	private int burgersBinnen = 0;

	private boolean beroemdheidBinnen = false;
	
	private Condition beroemdheidToestaan, rijOpen, museumOpen;  
	
	public Museum() {
		lock = new ReentrantLock();
		
		beroemdheidToestaan = lock.newCondition();
		rijOpen = lock.newCondition();
		museumOpen = lock.newCondition();
	}
	
	public void visitBurger() throws InterruptedException {
		lock.lock();
		try {
			//zodat er geen nieuwe burgers aansluiten als de burgers in de rij naar binnen mogen en er zijn wachtende beroemdheden
			while(rijDicht()) {
				rijOpen.await();
			}
			burgersInRij++;
			System.out.println(Thread.currentThread().getName() + " gaat in de rij staan");
				
			//als het museum dicht is betekent dat dat er een celeb binnen is. Als deze celeb het gebouw verlaat moet hij alle burgers signallen zodat deze check opnieuw uitgevoerd kan worden
			while(museumDicht()) {
				museumOpen.await();
			}
			burgersBinnen++;
			burgersInRij--;
		} finally {
			lock.unlock();
		}
	}
	
	public void visitBeroemdheid() throws InterruptedException {
		lock.lock();
		try {
			beroemdhedenInRij++;
			System.out.println(Thread.currentThread().getName() + " gaat in de rij staan.");
			while(closedToBeroemdheden()) {
				beroemdheidToestaan.await();
			}
			//hier wordt een beroemdheid al binnen gelaten, dus kan er gekeken worden of er al 3 zijn geweest
			if(beroemdhedenCount == 3) {
				beroemdhedenCount = 0;
			}
			beroemdheidBinnen = true;
			beroemdhedenInRij--;
			
			//laat alle burgers weer kijken of ze in de rij kunnen aansluiten i.v.m. 3 beroemdheiden achter elkaar
			rijOpen.signalAll();
			
		} finally {
			lock.unlock();
		}
	}
	
	public void showOutBurger() {
		lock.lock();
		try {
			burgersBinnen--;
			System.out.println(Thread.currentThread().getName() + " verlaat het museum");
			//laat de wachtende beroemdheid opnieuw kijken of het al OK is om naar binnen te gaan
			beroemdheidToestaan.signal();
		} finally {
			lock.unlock();
		}
	}
	
	public void showOutBeroemdheid() {
		lock.lock();
		try {
			//tel de hoeveelheid achtereenvolgende beroemdheden op
			beroemdhedenCount++;
			System.out.println("De beroemdhedenCount is: " + beroemdhedenCount);
			beroemdheidBinnen = false;
			
			System.out.println(Thread.currentThread().getName() + " verlaat het museum");
			
			//laat een andere beroemdheid kijken of hij al naar binnen kan en laat alle burgers kijken of ze naar binnen kunnen
			beroemdheidToestaan.signal();
			museumOpen.signalAll();
			
		} finally {
			lock.unlock();
		}
	}
	
	private boolean rijDicht() {
		return (beroemdhedenInRij > 0 && beroemdhedenCount == 3);
	}
	
	private boolean museumDicht() {
		return ((beroemdhedenInRij > 0 && beroemdhedenCount < 3) || beroemdheidBinnen);
	}
	
	private boolean closedToBeroemdheden() {
		return ((beroemdhedenCount >= 3 && burgersInRij > 0) || burgersBinnen > 0 || beroemdheidBinnen);
	}
	
}
