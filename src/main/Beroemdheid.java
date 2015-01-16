package main;

public class Beroemdheid extends Thread {
	
	private Museum museum;
	
	public Beroemdheid(String name, Museum museum) {
		super(name);
		museum = this.museum;
	}
	
	public void run() {
		while(true){ 
			live();
		}
	}
	
	private void live() {
		try {
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public String getBeroemdheidName() {
		return getName();
	}
	
}
