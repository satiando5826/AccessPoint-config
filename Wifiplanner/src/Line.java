import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

public class Line implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point p1 ;
	 private Point p2 ;
	 private float dist;
	 private float PAF;
	 private Color c;
	public Line(Point _p1,Point _p2){
		p1 = _p1;
		p2 = _p2;
		dist = (float) Point.distance(p1.x, p1.y, p2.x, p2.y);
		
		
	}
	public Line(){
		
		
		
	}
	public void setP1(Point Spoint){
		p1.x = Spoint.x;
		p1.y = Spoint.y;
		
	}
	public void setP2(Point Epoint ){
		p2.x = Epoint.x;
		p2.y = Epoint.y;
		
	}
	public void HideLine(){
		p1.setLocation(-1, -1);
		p2.setLocation(-1, -1);
	}
	public int getP1X(){
		
		return p1.x;
	}
	public int getP1Y(){
		
		return p1.y;
	}
	public int getP2X(){
		
		return p2.x;
	}
	public int getP2Y(){
		
		return p2.y;
	}
	public float getDistance(){
		dist = (float) Point.distance(p1.x, p1.y, p2.x, p2.y);
		return dist;
		
	} 
	public void setWPAF(float paf){
		PAF = paf;
	
	}
	public float getPAF(){
		
		
		return PAF;
	}
	public void setColor(int index){
		if(index==0)
			c = (Color.BLUE);
		else if(index ==1)
			c = (Color.GREEN);
		else if(index ==2)
			c = (Color.gray);
		else if(index ==3)
			c = (Color.MAGENTA);
		else if(index ==4)
			c = (Color.yellow);
		else if(index ==5)
			c = (Color.PINK);
		else if(index ==6)
			c = (Color.RED);
	}
	public Color getColor(){
		
		return c;
	}

}
