package main;

public class ToegangsRegelaar extends Thread {
	
	private Museum museum;
	
	public ToegangsRegelaar(Museum museum) {
		museum = this.museum;
	}
	
	public void run() {
		while(true) {
			
			
		}
	}
	
	private void laatBinnen() {
		try{
			Thread.sleep((int)(Math.random() * 1000));
		} catch(InterruptedException e) {}
	}

}
