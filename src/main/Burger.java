package main;

public class Burger extends Thread {
	
	private Museum museum;
	
	public Burger(String name, Museum museum) {
		super(name);
		museum = this.museum;
	}
	
	public void run() {
		while(true) {
			live();
		}
	}
	
	private void live() {
		try {
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public String getBurgerName() {
		return getName();
	}
}
