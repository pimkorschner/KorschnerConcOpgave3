package main;

public class Beroemdheid extends Persoon {
	
	public Beroemdheid(String name, Museum museum) {
		super(name, museum);
	}
	
	public void run() {
		while(true){ 
			try {
				live();
				museum.visitBeroemdheid();
				bezoekMuseum();
				museum.showOutBeroemdheid();
				lovendWoordje();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private void live() {
		try {
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public void bezoekMuseum() {
		try {
			System.out.println(getName() + " bezoekt het museum");
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public void lovendWoordje() {
		try {
			System.out.println(getName() + " laat een lovend woordje achter");
			Thread.sleep((int)(Math.random() * 10000));
		} catch (InterruptedException e) {}
	}
	
	public String getBeroemdheidName() {
		return getName();
	}
	
}
