package marvin;

public class Filter {

	    
	private static final float ALPHA = 0.125f;

	// exponential weighted moving average
	public static float avgEWMA(float estimate, int sample){
		
	    return (1-ALPHA)*estimate + ALPHA*sample;
	}
	
}
