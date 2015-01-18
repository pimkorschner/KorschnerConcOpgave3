package main;

public class Burger extends Persoon {
	
	public Burger(String name, Museum museum) {
		super(name, museum);
	}
	
	public void run() {
		while(true) {
			try {
				live();
				museum.visitBurger();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
