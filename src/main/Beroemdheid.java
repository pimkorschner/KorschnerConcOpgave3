package main;

public class Beroemdheid extends Persoon {
	
	public Beroemdheid(String name, Museum museum) {
		super(name, museum);
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
	
	public void bezoekMuseum() {
		try {
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public void lovendWoordje() {
		try {
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public String getBeroemdheidName() {
		return getName();
	}
	
}
