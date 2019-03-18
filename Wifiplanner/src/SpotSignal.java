import java.awt.Point;
import java.util.ArrayList;


public class SpotSignal {
	public ArrayList<Float> vals;
	public ArrayList<Integer> channels;
	
	public SpotSignal(){		
	}
	
	public SpotSignal(ArrayList<Float> _vals,ArrayList<Integer> _channels) {
		vals = _vals;
		channels = _channels;
	}
	
	public SpotSignal(ArrayList<Float> _vals) {
		vals = _vals;
		channels = new ArrayList<Integer> ();
	}
}
