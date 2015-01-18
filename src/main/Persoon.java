package main;

public class Persoon extends Thread {
	
	protected Museum museum;
	
	public Persoon(String name, Museum museum) {
		super(name);
		this.museum = museum;
	}
}
