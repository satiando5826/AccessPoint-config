import java.awt.Point;
import java.util.ArrayList;


public class Individual {
	public ArrayList<Integer> pts;
	public ArrayList<Integer> channels;
	
	public Individual(){		
	}
	
	public Individual(ArrayList<Integer> _pts,ArrayList<Integer> _channels) {
		pts = _pts;
		channels = _channels;
	}
	
	public Individual(ArrayList<Integer> _pts) {
		int pt;
		pts = new ArrayList<Integer>();
//		System.out.println("_pts "+_pts);
		for(int i=0;i<_pts.size();i++) {
			pt = _pts.get(i);
//			System.out.println(pt);
			pts.add(pt);
		}
		channels = new ArrayList<Integer>();
	}
	public Individual(Individual _ind){
		int pt;
		int channel;
		for(int i=0;i<_ind.pts.size();i++) {
			pt = _ind.pts.get(i);
			pts.add(pt);
			channel = _ind.channels.get(i);
			channels.add(channel);
		}
	}
}
