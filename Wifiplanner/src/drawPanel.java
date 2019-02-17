import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class drawPanel extends JPanel implements Serializable {
	public int detectedWallnum;
	public int detectedAP;
	public int detectedDetec;
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  transient BufferedImage  BGimgIcon;//BG uploaded img
	public transient BufferedImage BGg;
	 public transient BufferedImage combined;
	// private int paneWidth,paneHeight;
	// private MouseHandler mouseHandler = new MouseHandler();
	
	 public ArrayList<Spot> test	 = new ArrayList<Spot>() ;

	 //-----------------------scale condition var
	 public Line ScaleLine;
	 public float gridDist; 
	 public float gridDistP;
	 public boolean drawingScale = false;
	 public boolean scale = false;
	 //---------------------wall condition var
	 public Line temp ;
	 public  ArrayList<Line> WList=new ArrayList<Line>();
	 public boolean Walldrawing = false;
	 public float curPAF;
	 public float curFreq;
	 public int selectedWall=0;
	 //---------------------------- calculator's vars
	 public JLabel txt7;
	 public float curY=2;
	 public float showCur=0;
	 public int currentMode = 0;//mode of menu
	 //----------------------------------auto Mode vars--------------------
	 private float[] exDists = new float[5];
	 private float[] exVals = new float[5];
	 
	//grid width and height
	 public int gw=20;
	 public int cGW=gw;
	
	 public int npx,npy;//current position of mouse
	 int size;//arraysize
	 public JPanel ChangingPanel;
	 float[] freqf = {2.4f,5.0f};
	 public   JComboBox<String>  changeFreq;
	 JSlider selectPt ;
	//AP var----------------------------------------------
	 public  ArrayList<AP> APs=new ArrayList<AP>();
	 public  ArrayList<Detec> Detecs=new ArrayList<Detec>();
	 public  ArrayList<AP> APshow=new ArrayList<AP>();
	 public ArrayList<Detec> Detecshow = new ArrayList<Detec>();
	 public ArrayList<Spot> Spots	 = new ArrayList<Spot>() ;
	 public ArrayList<Spot> SampleSpots	 = new ArrayList<Spot>() ;
	//-----------Zoom vars
	 double Zoomscale;
	 double ZoomInc;
	public drawPanel() {
		Zoomscale = 1.0;
		
		ShowApOptions();
	
	     ScaleLine = new Line(new Point(-1,-1),new Point(-1,-1));

		
	
		
	}
	public void setPath(File path) throws IOException{
		BGimgIcon = ImageIO.read(path);
		//paneWidth = BGimgIcon.getWidth();
		//paneHeight = BGimgIcon.getHeight();
		//setPreferredSize(new Dimension(758,571));
		 System.out.println("setpath");
		
	}
	
	public void setPAF(float paf,int index){
		
		curPAF = paf;
		selectedWall = index;
		
	}
	public void setY(float y){
		curY = y;
		reCal();
		repaint();
		
	}
	public void setGW(int _gw){
		
		gw = _gw;
		
		repaint();
		
	}
	public void setExample(float[] exDi,float[] exVa){
		
		exDists = exDi;
		exVals = exVa;
		
	}
	public void calMMSE(){
		
		
		
	}
	public void setFreq(float freq){
		curFreq = freq;
		
	}
	public void setScale(){
		
		scale = !scale;//change mode to scale or not scale when Scale button has been clicked
		 setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}
	public void changeModeTo(int desireMode){
		currentMode = desireMode;
		if(desireMode == 0){
		
	       setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	       }
		else {
			 setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			
			
		}
			
		
	}
	public void setZoomScale(double zoomsize)
    {
        Zoomscale = zoomsize;
        revalidate();
        repaint();
    }
	 @Override
	 public Dimension getPreferredSize() {
		 		if(BGimgIcon==null)
		 			return new Dimension(758, 571);
		 			//return new Dimension(800, 600);
		 		else{
		 			
		 			int
		            w = (int)(Zoomscale *  BGimgIcon.getWidth()),
		            h = (int)(Zoomscale * BGimgIcon.getHeight());
		 			return new Dimension(w, h);
		 			//return new Dimension(BGimgIcon.getWidth(), BGimgIcon.getHeight());
		 			
		 		}
	    }
	 @Override
	public void paintComponent(Graphics g) {
		 	super.paintComponent(g);
		 	
		 
		 
		 	

		 	//---------------------draw on BGg------------------------------
		 	if(BGimgIcon!=null){
		 		//set up
		 		 	BGg =	  new BufferedImage(BGimgIcon.getWidth(), BGimgIcon.getHeight(),
		                    BufferedImage.TYPE_INT_ARGB);
		 		Graphics2D g2 = BGg.createGraphics();
		 		if(test!=null){
		 			for(int x=0;x<test.size();x++){
		 				g2.setColor(new Color(255,0,0));
		 				g2.fillOval(test.get(x).getPos().x,test.get(x).getPos().y, 8,8);
		 				
		 			}
		 			
		 		}
		 		if(Spots!=null){
		 			for(int k =0;k<Spots.size();k++){
		 				g2.setColor(Spots.get(k).getColor());
		 				g2.fillRect(Spots.get(k).getPos().x, Spots.get(k).getPos().y,cGW, cGW);
		 			
		 			}
		 		}
		 		//test sample
		 		if(SampleSpots!=null){
		 			for(int k =0;k<SampleSpots.size();k++){
		 				g2.setColor(SampleSpots.get(k).getColor());
		 				g2.fillRect(SampleSpots.get(k).getPos().x, SampleSpots.get(k).getPos().y,cGW, cGW);
		 			
		 			}
		 			
		 		}
		 	
		 		//drawAP----------------
		 		if(APs!=null){
		 		for(int i=0; i< APs.size();i++){//for every AP
	        		
		 			g2.setColor(Color.BLUE);
		 			g2.fillOval((APs.get(i).posx-4),(APs.get(i).posy-4),gw,gw);
		 			g2.drawOval(APs.get(i).posx-4,APs.get(i).posy-4,gw,gw);	        			
	        		}
		 		}
		 		
		 		//drawDetec
		 		if(Detecs!=null) {
		 			for(int i = 0; i< Detecs.size();i++) {//
		 				g2.setColor(Color.BLACK);
			 			g2.fillRect((Detecs.get(i).posx-4),(Detecs.get(i).posy-4),gw,gw);
			 			g2.setColor(Color.WHITE);
			 			g2.drawRect(Detecs.get(i).posx-4,Detecs.get(i).posy-4,gw,gw);	        			
		 			}
		 		}
		 	
			 	  Graphics2D g2d = (Graphics2D) g2;
			 	
			        g2d.setColor(Color.blue);
			        g2d.setRenderingHint(
			            RenderingHints.KEY_ANTIALIASING,
			            RenderingHints.VALUE_ANTIALIAS_ON);
			        g2d.setStroke(new BasicStroke(3,
			            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			        //------------------
			        Graphics2D gWall = (Graphics2D) g2;
			        gWall.setColor(Color.blue);
			 		gWall.setRenderingHint(
				            RenderingHints.KEY_ANTIALIASING,
				            RenderingHints.VALUE_ANTIALIAS_ON);
			 		gWall.setStroke(new BasicStroke(6,
				            BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
			        if(drawingScale)//draw only when it's scaling mode
			        { gWall.drawLine(ScaleLine.getP1X(), ScaleLine.getP1Y(),ScaleLine.getP2X(), ScaleLine.getP2Y());}
			        if(Walldrawing){
			        	
			        	gWall.drawLine(temp.getP1X(), temp.getP1Y(),temp.getP2X(),temp.getP2Y());
			        }
			    	
			 		//drawWall---------------
				 	   if(WList != null){
				 		 
				 		
				        	for(int i=0; i< WList.size(); i++){
				        		//System.out.println(WList.get(i).getPAF());
				        		gWall.setColor(WList.get(i).getColor());
				        		gWall.drawLine(WList.get(i).getP1X(),WList.get(i).getP1Y(),WList.get(i).getP2X(),WList.get(i).getP2Y());
				        		
				        	}
				        	
				        	
				        }
			 		

		 		Dimension dim = getPreferredSize();
		 	  g.drawImage(BGimgIcon,0,0,dim.width,dim.height, null);
		 		g.drawImage(BGg,0,0,dim.width,dim.height, null);
		 	  
		 		// System.out.println(BGimgIcon.getWidth()+","+BGimgIcon.getHeight());
		 	
		 	    //save combined IMG-------------------------------------------------
		        int w = Math.max(BGimgIcon.getWidth(), BGg.getWidth());
		        int h = Math.max(BGimgIcon.getHeight(), BGg.getHeight());
		        	combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		        	Graphics cg = combined.getGraphics();
		        	cg.drawImage(BGimgIcon, 0, 0, null);
		        	cg.drawImage(BGg, 0, 0, null);
		 		
		 		
		 		
		        }
		        //-------------------------------------------------------------------------
		    	  
		     //   g.drawImage(BGimgIcon,0,0,null);
		    for (int i=0;i<this.getWidth();i+=gw) {
	            
	        	  for (int j=0;j<this.getHeight();j+=gw) {
	        		 
	        	  g.drawOval(i,j,1,1);
	        	 
	        	  }
	          }
	
	        	
        	
	        g.drawOval(npx-2, npy-2, 4, 4);
	   	
	        
	    
	        	
		  }
	
	


	public float spl(float d,float _curK,int _pt){
		
		 float PL = (float) (_pt + _curK - 10*curY*Math.log10(d));
		 return PL;
		
	}
	public ArrayList<Float> ipm(int APx ,int APy,int posx,int  posy){
		ArrayList<Float> PAFS = new ArrayList<Float>();
		boolean intersected = false;
		for(int i=0;i<WList.size();i++){
			if(posx != APx || posy != APy){//do not compare itself
				intersected = Line2D.linesIntersect(APx,APy,posx,posy,
					 WList.get(i).getP1X(),WList.get(i).getP1Y(),WList.get(i).getP2X(),WList.get(i).getP2Y());
			}
			if(intersected){
				float tempPAF = WList.get(i).getPAF();
				PAFS.add(tempPAF);
				
			}
		}
		return PAFS;
	}
	public float findMaxTempVal(float[] _tampVals){
		float temp = -999;
		for(int i=0;i<_tampVals.length;i++){
			if(_tampVals[i]>temp)
				temp = _tampVals[i];
				
		}
		return temp;
		
	}
	public void reCal(){
		 //create Image for Spots---------------
		  

		
		Spots.clear();
		
			 //int count = 0;
			 
		 for (int i=0;i<BGimgIcon.getWidth();i+=cGW) {
	            
        	  for (int j=0;j<BGimgIcon.getHeight();j+=cGW) {
        		  for(int k=0; k< APshow.size();k++){//check all APs
        		  if(i!=APshow.get(k).posx || j!=APshow.get(k).posy){
        		  
        		  float tempDistP =(float) Point.distance(i, j,APshow.get(k).posx ,APshow.get(k).posy);//distance from ij to AP in pixel
        		  if(tempDistP<=gridDistP){//if it's less than 70m( in pixel unit) gridDistP is defined in Testrun1
        			  
        			  float dist = (tempDistP*gridDist)/gw;//convert Pixel to Meter
        			//  System.out.println(APs.get(k).freq);
        			  float tempVal = spl(dist,APshow.get(k).curK,APshow.get(k).pt);//calculate spl
        			  ArrayList<Float> PAFS = new ArrayList<Float>();
        			  PAFS = ipm(APshow.get(k).posx,APshow.get(k).posy,i,j);//find how many walls in the line of sign
        			  
        			  for(int m=0; m<PAFS.size();m++){
        				  tempVal= tempVal + PAFS.get(m);// Path loss + obstacles
        				 
        			  }
        			  if(tempVal >= -90){//if val > -100  add it to Spots
        		
        			 Spot tempSpot = new Spot(new Point(i,j),tempVal);
        			 int checkDup = findDupSpot(new Point(i,j));//check duplicate spot
        			 if(checkDup != -1){//if found
        				 if(tempVal > Spots.get(checkDup).value){//if new spot is greater than old spot
        					 Spots.get(checkDup).value = tempVal;//update new value to old spot
        					// Color c = findColor(tempSpot.value);
        					// Spots.get(checkDup).setColor(c);
        				 }
        			 }else{
        				// Color c = findColor(tempSpot.value);
        				// tempSpot.setColor(c);
        				 Spots.add(tempSpot);//add to arrayList
        				 
        			 }
        			 
        			  }
        			  
        		  }
        	  }
        		
        	  }
        	  }
          }
	 
		// }//check all APs
		 setColor();//set color
		
		
	}
	
	public void SampleRecal(){

		 //create Image for Spots---------------
		  
			for(int h=0;h<SampleSpots.size();h++){
				
				SampleSpots.get(h).value = -999;
			}
		
		//SampleSpots.clear();
		
			 //int count = 0;
			 
		 for (int i=0;i<SampleSpots.size();i++) {
		
       		  for(int k=0; k< APshow.size();k++){//check all APs
       			
       		  float tempDistP =(float) Point.distance(SampleSpots.get(i).getPos().x,SampleSpots.get(i).getPos().y,APshow.get(k).posx ,APshow.get(k).posy);//distance from ij to AP in pixel  
       		  float dist = (tempDistP*gridDist)/gw;//convert Pixel to Meter
       			//  System.out.println(APs.get(k).freq);
       		  float tempVal = spl(dist,APshow.get(k).curK,APshow.get(k).pt);//calculate spl
       		  ArrayList<Float> PAFS = new ArrayList<Float>();
       		  PAFS = ipm(APshow.get(k).posx,APshow.get(k).posy,SampleSpots.get(i).getPos().x,SampleSpots.get(i).getPos().y);//find how many walls in the line of sign
       			  
       			  for(int m=0; m<PAFS.size();m++){
       				  tempVal= tempVal + PAFS.get(m);// Path loss + obstacles
       				 
       			  }
       			  	System.out.println(tempVal);
       			if(tempVal > SampleSpots.get(i).value)
       					SampleSpots.get(i).value = tempVal;
       			//System.out.println(SampleSpots.get(i).value);
       			//}
       			//System.out.println("22222222");
       		
       	  }
       	  }
         
	 
		// }//check all APs
		 setColorTestSpots();//set color
		
		
	
		
		
		
		
	}
	public void setColor(){
		int i=0;
		int min1 = (int) findMinSpots(Spots);
		 int max1 = (int) findMaxSpots(Spots);
		//set Color------------------------------------------
		 for(int k =0;k<Spots.size();k++){
    		
    		Color c = findColor(Spots.get(k).value,min1,max1);
//    		System.out.println(i);
    		Spots.get(k).setColor(c);
    			
       
    			        		
    	}
	}
		 public void setColorTestSpots(){
			 int min1 = (int) findMinSpots(SampleSpots);
			 int max1 = (int) findMaxSpots(SampleSpots);
				//set Color------------------------------------------
				 for(int k =0;k<SampleSpots.size();k++){
		    		
		    		Color c = findColor(SampleSpots.get(k).value,min1,max1);
		    		SampleSpots.get(k).setColor(c);	    			        		
		    	}
			}
	public int findDupSpot(Point _loc){
		int found=-1;
		
		loop:
		for(int i=0;i<Spots.size();i++){
			//System.out.println(Spots.get(i).getPos()+"   xxxxxxxxxxxxx    "  +_loc);
			if(Spots.get(i).getPos().equals(_loc)){
				//System.out.println("999999999999999999999999999999999999999999");
				found = i;
				break loop;
			}
		
		}
		return found;
		
	}
	public Color findColor(float value,int min1,int max1){
		
		Color c;
		//System.out.println("max = "+max1);
		
		//min1 *= -1;
		if(value <= min1 && value>= -60 ){
			min1 *= -1;
			value *= -1;
			int val = (int) (((value-min1)/(60-min1))*203);//dark green to light green

			 c = new Color(0,51+val,0,150);
			
			//return c;
			
		}else if(value < -60 &&value >= -65 ){// light green to lighter green
			int val = (int) ((((-value)-60)/(65-60))*128); 
			//System.out.println(val);
			//System.out.println(value);
			 c = new Color(val,255,0,150);
			//return c;
			
		}else if(value < -65 && value >= -72){//lighter green to pure yellow
			int val = (int) ((((-value)-65)/(72-65))*126); 
			//System.out.println(val);
			//System.out.println(value);
			 c = new Color(128+val,255,0,150);
			//return c;
			
		}else if(value < -72 && value >= -76){//pure yellow to light yellow
			int val = (int) ((((-value)-72)/(76-72))*50); 
			//System.out.println(val);
			//System.out.println(value);
			 c = new Color(255,255,0+val,150);
			//return c;
			
			
		}else if(value < -76 && value >= -85){//lighter yellow to strong orange
			
			int val = (int) ((((-value)-76)/(85-76))*128); 
			//System.out.println(val);
			//System.out.println(value);
			int val2 = (val*51)/128;
			 c = new Color(255,255-val,51-val2,150);
			//return c;
		}else if(value < -85 && value >=max1){
			max1 *= -1;
			int val = (int) ((((-value)-85)/(max1-85))*128); 
			//System.out.println(val);
			//System.out.println(value);
			 c = new Color(255,val,0,150);
			//return c;
			
		}else{
			System.out.println("FUCKFJKWFHWIDJIWDJIWDJ");
			System.out.println(value+" max = "+max1+ " min = "+min1);
			c = new Color(255,255,255);
		}
		
		
		return c;
		
		
		
	}
	
		
	
		
		
		
	/*public Color findColor(float value){
		//int min1 = (int) findMinSpots();
		//min1 *= -1;
		if(value < 0 && value>= -40 ){
		//	min1 *= -1;
			value *= -1;
			int val = (int) (((value-0)/(60-0))*204);//dark green to light green

			Color c = new Color(0,51+val,0,150);
	
			return c;
			
		}else if(value < -40 &&value >= -45 ){// light green to lighter green
			int val = (int) ((((-value)-40)/(45-40))*128); 
			//System.out.println(val);
			//System.out.println(value);
			Color c = new Color(val,255,0,150);
			return c;
			
		}else if(value < -45 && value >= -50){//lighter green to pure yellow
			int val = (int) ((((-value)-45)/(50-45))*126); 
			//System.out.println(val);
			//System.out.println(value);
			Color c = new Color(128+val,255,0,150);
			return c;
			
		}else if(value < -50 && value >= -60){//pure yellow to light yellow
			int val = (int) ((((-value)-50)/(60-50))*50); 
			//System.out.println(val);
			//System.out.println(value);
			Color c = new Color(255,255,0+val,150);
			return c;
			
			
		}else if(value < -60 && value >= -70){//lighter yellow to strong orange
			
			int val = (int) ((((-value)-60)/(70-60))*128); 
			//System.out.println(val);
			//System.out.println(value);
			int val2 = (val*51)/128;
			Color c = new Color(255,255-val,51-val2,150);
			return c;
		}else{
			int val = (int) ((((-value)-70)/(100-70))*127); 
			//System.out.println(val);
			//System.out.println(value);
			Color c = new Color(255,128-val,0,150);
			return c;
			
		}
		
		
		
	}*/
	public float findMinSpots(ArrayList<Spot> Tspots){
		float temp = -200;
		for(int i=0;i<Tspots.size();i++){
			if(Tspots.get(i).value>temp){
				temp = Tspots.get(i).value;
			}
		}
		return temp;
	}
	public float findMaxSpots(ArrayList<Spot> Tspots){
		float temp = 200;
		for(int i=0;i<Tspots.size();i++){
			if(Tspots.get(i).value<temp){
				temp = Tspots.get(i).value;
			}
		}
		return temp;
	}
	public boolean isthereWall(int apx,int apy){
		boolean checker = false;
		loop:
		for(int i=0;i<WList.size();i++){
			double check = Line2D.ptSegDist(WList.get(i).getP1X(),WList.get(i).getP1Y(),
					WList.get(i).getP2X(),WList.get(i).getP2Y(),apx, apy);
			if(check <= 5){
				checker = true;
				detectedWallnum =i;
				break loop;
			}
			
		}
		return checker;
	}
	public boolean isthereAP(int apx,int apy){
		boolean checker = false;
		loop:
			 for(int i=0;i<APshow.size();i++){
				 if(APshow.get(i).getPos().equals(new Point(apx,apy)))
				 {
					 checker = true;
					 detectedAP = i;
					 break loop;
				 } 
			 }
		return checker;
	}
	public boolean isthereDetec(int apx,int apy) {
		boolean checker = false;
		loop:
			for(int i=0;i<Detecshow.size();i++){
				 if(Detecshow.get(i).getPos().equals(new Point(apx,apy)))
				 {
					 checker = true;
					 detectedDetec = i;
					 break loop;
				 } 
			 }
		return checker;	
	}
	public void ShowApOptions(){
		
		ChangingPanel = new JPanel();
      String[] freqs = {"2.4 GHz","5.0GHz"};
      changeFreq  = new JComboBox<String>(freqs);
      changeFreq.setSelectedIndex(0);
      changeFreq.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			float selectedFreq = freqf[changeFreq.getSelectedIndex()]; 
			APshow.get(detectedAP).setFreq(selectedFreq);
			APs.get(detectedAP).setFreq(selectedFreq);
			System.out.println(selectedFreq);
		
		}
	});
      ChangingPanel.add(changeFreq);
      
      selectPt = new JSlider(JSlider.HORIZONTAL, 0, 10, 1);
      selectPt.setMinorTickSpacing(1);
      selectPt.setMajorTickSpacing(2);
       selectPt.setPaintTicks(true);
      selectPt.setPaintLabels(true);
      selectPt.addChangeListener(new ChangeListener() {
			 
	         @Override
			public void stateChanged(ChangeEvent e) {
	            int value = selectPt.getValue();
	            APshow.get(detectedAP).pt = value;
	            APs.get(detectedAP).pt = value;
	         }
	});
      ChangingPanel.add(selectPt);
  

		JButton okbtn = new JButton("OK");
  		okbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//System.out.println("OKOKOKO");
				reCal();
				repaint();
				ChangingPanel.setVisible(false);
			}
		});
  		
  		ChangingPanel.add(okbtn);
  		ChangingPanel.setVisible(false);
  		add(ChangingPanel);
	
}
	
	public void reset(){
		gw = 11;		
	    WList = new ArrayList<Line>();
		APs = new ArrayList<AP>();
		Detecs = new ArrayList<Detec>();
		Spots = new ArrayList<Spot>();
		APshow = new ArrayList<AP>();
		Detecshow = new ArrayList<Detec>();
		gridDist = 0;
		gridDistP = 0;	
		curY = 2;
		
		
	}
	
}
