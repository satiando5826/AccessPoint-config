import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Spot implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Point pos;

public ArrayList<Float> values = new ArrayList<Float>();
public float value;
public float Maxval;
public Color sc;
Spot(Point _pos,float _val){
	pos = _pos;
	value = _val;
}
public Point getPos(){
	return pos;
	
}
public Color getColor(){
	
	return sc;
}
public void setColor(Color _sc){
	
	sc = _sc;
	
}
public float getVal(){
	
	return value;
}
public float getMaxVal(){
	float Max = -999;
	for(float temp:values){
		if(temp>Max)
			Max = temp;
		
	}
	return Max;
	
}
public void setMaxval(){
	float Max = -999;
	for(float temp:values){
		if(temp>Max)
			Max = temp;
		
	}
	Maxval=Max;
	
}
public void addVal(float _val){
	values.add(_val);
	
}






//public void 


}
