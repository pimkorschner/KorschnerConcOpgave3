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
	
	private int nrOfBurgers = 0;
	private int nrOfBeroemdheden = 0;
	private int beroemdhedenCount = 0;
	private int burgerCount = 0;
	private int burgersBinnen = 0;
	private int beroemdBinnen = 0;
	
	private boolean beroemdhedenInRij = false, burgersInRij = false, closedToBurgers = false, closedToBeroemdheden = false, burgerInvite = false, beroemdheidInvite = false, finBurger = false, finBeroemd = false;
	private boolean burgerInList = false;
	
//	private Burger[] binnenBurger = new Burger[World.NR_BURGERS];
	public List<Persoon> list = Collections.synchronizedList(new ArrayList<Persoon>());
	private Burger binnenBurger;
	private Beroemdheid binnenBeroemdheid;
	
	private Condition newLine, beroemdheidOpenPlek, burgerOpenPlek, burgerInvitation, beroemdInvitation, finishedBurger, finishedBeroemdheid, newBurger, newBeroemdheid, readyToEnterBurger, readyToEnterBeroemdheid;
	
	public Museum() {
		lock = new ReentrantLock();
		
		beroemdheidOpenPlek = lock.newCondition();
		burgerOpenPlek = lock.newCondition();
		burgerInvitation = lock.newCondition();
		beroemdInvitation = lock.newCondition();
		finishedBurger = lock.newCondition();
		finishedBeroemdheid = lock.newCondition();
		newBurger = lock.newCondition();
		newBeroemdheid = lock.newCondition();
		readyToEnterBurger = lock.newCondition();
		readyToEnterBeroemdheid = lock.newCondition();
		
		newLine = lock.newCondition();
	}
	
	public void visitBurger() throws InterruptedException {
		lock.lock();
		try {
			System.out.println(Thread.currentThread().getName() + " zit in visitBurger()");
			//deze om de toegangsregelaar wakker te maken.
			while(noBurgerLineAvailable()) {
				burgerOpenPlek.await();
			}
			burgerCount++;
			newBurger.signal();
//			System.out.println(Thread.currentThread().getName() + " staat in de burger rij");
				
//			while(!burgerInvite) {
//				burgerInvitation.await();
//			}
//			burgerInvite = false;
			System.out.println(Thread.currentThread().getName() + " is in line");
			
			burgerCount--;
			burgerOpenPlek.signal();
			
			burgersBinnen++;
			
//			binnenBurger = (Burger) Thread.currentThread();
//			list.add((Burger) Thread.currentThread());
			putIfAbsent((Burger)Thread.currentThread());
			burgerInList = true;
			readyToEnterBurger.signal();
			//hier dus het museum binnen
			
			
			while(!finBurger) {
				finishedBurger.await();
			}
			finBurger = false;
			
			burgersBinnen--;
			System.out.println(Thread.currentThread().getName() + " is klaar!");
		} finally {
			lock.unlock();
		}
	}
	
	public void visitBeroemdheid() throws InterruptedException {
		lock.lock();
		try {
			/*
			 * Kijken of beroemdheden binnen mogen of dat er al 3 binnen zijn geweest en het tijd is voor burgers
			 * daarna in de rij wachten
			 */
			while(!closedToBeroemdheden) {
				readyToEnterBeroemdheid.await();
			}
			
			/*
			 * When there's no celeb in the building enter the museum
			 */
			
			/*
			 * Toegangsregelaar.visit() dus een readyForVisit voor beroemdheden
			 */
			
		} finally {
			lock.unlock();
		}
	}
	
	public Persoon showIn() throws InterruptedException {
		lock.lock();
		try {
			System.out.println("toegangsregelaar zit in permitAccess");
			//toegangsregelaar wakker maken
			while(noBurgerLineAvailable()) {
				newBurger.await();
			}
			
//			burgerInvite = true;
//			burgerInvitation.signalAll();
			
			while (!burgerInList)
				readyToEnterBurger.await();
			
			Persoon juisteBurger = list.get(list.size()-1);
			burgerInList = false;
			
			return juisteBurger;
		} finally {
			lock.unlock();
		}
	}
	
	public void showOut(Persoon persoon) {
		lock.lock();
		try {
			list.remove(persoon);
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
	
	public boolean putIfAbsent(Persoon p) {
		synchronized (list) {
			boolean absent = !list.contains(p);
			if(absent) {
				list.add(p);
			}
			return absent;
		}
	}
	
}
