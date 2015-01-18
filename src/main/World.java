package main;

public class World {
	
	public static final int NR_BURGERS = 10;
	private static final int NR_BEROEMDHEDEN = 5;

	public static void main(String[] args) {
		Museum museum = new Museum();
		Thread[] burger;
		Thread[] beroemdheid;
		Thread toegangRegelaar;
		
		burger = new Thread[NR_BURGERS];
		for (int i = 0; i < NR_BURGERS; i++) {
			burger[i] = new Burger("burger"+i, museum);
			burger[i].start();
		}
		
		beroemdheid = new Thread[NR_BEROEMDHEDEN];
		for (int i = 0; i < NR_BEROEMDHEDEN; i++) {
			beroemdheid[i] = new Beroemdheid("beroemdheid"+i, museum);
			beroemdheid[i].start();
		}
		
		toegangRegelaar = new ToegangsRegelaar(museum);
		toegangRegelaar.start();
		
	}
}
