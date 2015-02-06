package marvin;

public class KillBoss implements Step {

	@Override
	public void run(Configuration configuration) {
		configuration.getMovementPrimitives().fullSpeed();
		
	}

	@Override
	public String getName() {
		return "KillBoss";
	}

}
