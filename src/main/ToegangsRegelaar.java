package main;

public class ToegangsRegelaar extends Thread {
	
	private Museum museum;
	private Persoon persoon;
	
	public ToegangsRegelaar(Museum museum) {
		this.museum = museum;
	}
	
	public void run() {
		while(true) {
			try {
				persoon = museum.showIn();
				visit();
				museum.showOut(persoon);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			/*
			 * Check of er een beroemdheid is, zoja, laat die 1 binnen
			 */
//			if(!museum.noBeroemdheden()) {
				/*
				 * Er zijn beroemdheden in de rij, zorg eerst dat iedereen buiten is, laat dan de beroemdheid binnen
				 */
//			} else {
				/*
				 * Geen beroemdheden binnen dus laat de burgers erin
				 */
//			}
			
		}
	}
	
	private void visit() {
		try{
			System.out.println(persoon.getName() + " is aan het bezoeken");
			Thread.sleep((int)(Math.random() * 1000));
		} catch(InterruptedException e) {}
	}

}
