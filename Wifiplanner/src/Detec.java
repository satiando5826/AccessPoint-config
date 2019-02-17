import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Point;
import java.io.File;
import java.io.Serializable;


public class Detec implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float posxf,posyf;
	public int posx,posy;
	
	 
	public Detec(){
		
		
	}
	public Detec(Detec another){
		this.posx = another.posx;
		this.posy = another.posy;
		
	}
	
	public Point getPos(){
		
		return new Point(posx,posy);
		
	}
	public Detec(int x ,int y){
		posx = (int) x;
		posy = (int) y;
		posxf = x;
		posyf = y;
	}

	

	/*public boolean isThereInterfere(Point AnotherApPos) { 
	    return AreaPos.contains(AnotherApPos); 
	}*/
	 

	
}
