package main;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Museum {
	
	private static final int NR_BURGERS = 100;
	
	Lock lock;
	
	private int nrOfBurgers = 0;
	private int nrOfBeroemdheden = 0;
	private int BeroemdhedenCount = 0;
	
	private boolean beroemdhedenInRij = false, burgersInRij = false, closedToBurgers = false, closedToBeroemdheden = false;
	
	private Burger[] binnenBurger = new Burger[NR_BURGERS];
	private Beroemdheid binnenBeroemdheid;
	
	private Condition beroemdheidOpenPlek, burgerOpenPlek, invitation, finished, newBurger, newBeroemdheid, readyToEnterBurger, readyToEnterBeroemdheid;
	
	
	public Museum() {
		lock = new ReentrantLock();
		
		beroemdheidOpenPlek = lock.newCondition();
		burgerOpenPlek = lock.newCondition();
		invitation = lock.newCondition();
		finished = lock.newCondition();
		newBurger = lock.newCondition();
		newBeroemdheid = lock.newCondition();
		readyToEnterBurger = lock.newCondition();
		readyToEnterBeroemdheid = lock.newCondition();
	}
	
	public void visitBurger() throws InterruptedException {
		lock.lock();
		try {
			while(!closedToBurgers) {
				readyToEnterBurger.await();
			}
			
			
			
		} finally {
			lock.unlock();
		}
	}
	
	public void visitBeroemdheid() {
		
	}
	
	private boolean noBeroemdheden(){
		return nrOfBeroemdheden == 0;
	}
	
}
