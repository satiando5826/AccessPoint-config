import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Point;
import java.io.File;
import java.io.Serializable;


public class AP implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float freq;
	public int pt;
	public float curK;
	public float posxf,posyf;
	public int posx,posy;
	public int channel;
	
	 
	public AP(){
		
		
	}
	public AP(AP another){
		this.freq = another.freq;
		this.curK = another.curK;
		this.posx = another.posx;
		this.posy = another.posy;
		this.pt = another.pt;
		
	}
	public AP(int x ,int y,float freq_){
	
		//set up var
		freq = freq_*1000000000;
		float Lamda = 300000000/freq;
		curK = (float) (-20*Math.log10((4*Math.PI)/Lamda));
		posx = (int) x;
		posy = (int) y;
		posxf = x;
		posyf = y;
		pt = 1;
	}
	public Point getPos(){
		
		return new Point(posx,posy);
		
	}

	public void calK(float freq_){
		
		freq = freq_*1000000000;
		float Lamda = 300000000/freq;
		curK = (float) (-20*Math.log10((4*Math.PI)/Lamda));
		
	}
	public void setFreq(float freq_){
		freq = freq_*1000000000;
		float Lamda = 300000000/freq;
		curK = (float) (-20*Math.log10((4*Math.PI)/Lamda));
		
		
	}
	
	public void setPT(int pt_) {
		pt = pt_;
	}
	
	public void setChannel(int channel_) {
		channel = channel_;
	}

	/*public boolean isThereInterfere(Point AnotherApPos) { 
	    return AreaPos.contains(AnotherApPos); 
	}*/
	 

	
}
