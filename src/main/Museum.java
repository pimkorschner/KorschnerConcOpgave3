package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
	
//	private static final int NR_BURGERS = 100;
	
	Lock lock;
	
	private int burgersInRij = 0;
	private int beroemdhedenInRij = 0;
	private int beroemdhedenCount = 0;
	private int burgersBinnen = 0;
//	private int beroemdBinnen = 0;
	
//	private boolean closedToBurgers = false, closedToBeroemdheden = false, burgerInvite = false, beroemdheidInvite = false, finBurger = false, finBeroemd = false;
//	private boolean burgerInList = false;
	private boolean beroemdheidBinnen = false;
	
//	private Burger binnenBurger;
//	private Beroemdheid binnenBeroemdheid;
	
//	private Condition newLine, beroemdheidOpenPlek, burgerOpenPlek, burgerInvitation, beroemdInvitation, finishedBurger, finishedBeroemdheid, newBurger, newBeroemdheid, readyToEnterBurger, readyToEnterBeroemdheid;
	private Condition beroemdheidToestaan, rijOpen, museumOpen;  
	
	public Museum() {
		lock = new ReentrantLock();
		
		beroemdheidToestaan = lock.newCondition();
		rijOpen = lock.newCondition();
		museumOpen = lock.newCondition();

//		beroemdheidOpenPlek = lock.newCondition();
//		burgerOpenPlek = lock.newCondition();
//		burgerInvitation = lock.newCondition();
//		beroemdInvitation = lock.newCondition();
//		finishedBurger = lock.newCondition();
//		finishedBeroemdheid = lock.newCondition();
//		newBurger = lock.newCondition();
//		newBeroemdheid = lock.newCondition();
//		readyToEnterBurger = lock.newCondition();
//		readyToEnterBeroemdheid = lock.newCondition();
		
//		newLine = lock.newCondition();
	}
	
	public void visitBurger() throws InterruptedException {
		lock.lock();
		try {
			//hier in de rij gaan staan. 
			while(rijDicht()) {
				rijOpen.await();
			}
			burgersInRij++;
			System.out.println(Thread.currentThread().getName() + " gaat in de rij staan");
				
			//als het museum dicht is betekent dat dat er een celeb binnen is. Als deze celeb het gebouw verlaat moet hij all
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
			
			//nu kan de count van beroemdheden weer naar 0 gezet worden, want de burgers en beroemdheden hebben al gekeken wie naar binnen kan
			if(beroemdhedenCount == 3) {
				beroemdhedenCount = 0;
			}
			
		} finally {
			lock.unlock();
		}
	}
/*	
	public Persoon showIn() throws InterruptedException {
		lock.lock();
		try {
			//toegangsregelaar wakker maken
//			while(noBurgerLineAvailable()) {
//				newBurger.await();
//			}
			
//			burgerInvite = true;
//			burgerInvitation.signalAll();
			
			while (!burgerInList)
				readyToEnterBurger.await();
			
//			Persoon juisteBurger = list.get(list.size()-1);
			burgerInList = false;
			
//			return juisteBurger;
		} finally {
			lock.unlock();
		}
		return binnenBeroemdheid;
	}
	
	public void showOut(Persoon persoon) {
		lock.lock();
		try {
//			list.remove(persoon);
//			binnenBurger = null;
			finBurger = true;
			finishedBurger.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	public boolean noBeroemdheden(){
		return beroemdhedenCount == 0;
	}
	
	public boolean noBurgers() {
		return burgerCount == 0;
	}
	
	public boolean noBurgerLineAvailable() {
		return burgerCount == World.NR_BURGERS;
	}
	*/
	
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
