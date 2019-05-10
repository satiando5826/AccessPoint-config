import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.math3.analysis.function.Min;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import javafx.collections.SetChangeListener;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Collections;

import java.awt.event.MouseMotionAdapter;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import java.util.Random;

public class DOrun {

	private JFrame frame;
	   int GBx;
       int GBy;
	ControlPanel conPanel;
	drawPanel Drawingpanel;
	JScrollPane scroll;
	JPanel RightPane;
	JPanel Btmpanel;
	JLabel zoomAmount;
	JSlider Zoomslider;
	public   BufferedImage  LoadedImg;
	float[] freqf = {2.4f,5.0f};
	float[] PAFs = {-10f,-15.4f,-2.89f,-16f,-5f,-18f};
	float selectedFreq;
	File selectedFile;
	float currentPAF;
	float currentY;
	boolean tested = false;

     int currentMode = 0;
     
     int npx,npy;
	//drawingPanel vars
	//please delete it-----------
     int numArea;
     public ArrayList<Spot> temptest	 = new ArrayList<Spot>() ;
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws FileNotFoundException  {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DOrun window = new DOrun();
					
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		// Creating a File object that represents the disk file. 
        PrintStream o = new PrintStream(new File("A.txt")); 
  
        // Store current System.out before assigning a new value 
        PrintStream console = System.out; 
  
        // Assign o to output stream 
        System.setOut(o); 
//        System.out.println("This will be written to the text file"); 
  
//        // Use stored value for output stream 
//        System.setOut(console); 
//        System.out.println("This will be written on the console!"); 	
		
	}

	/**
	 * Create the application.
	 */
	public DOrun() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setEnabled(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xsize = (int) tk.getScreenSize().getWidth();
		int ysize = (int) tk.getScreenSize().getHeight();
		frame.pack();
		frame.setSize(xsize, ysize-40);
		frame.getContentPane().setLayout(new BorderLayout());
		//----------------setup var for start
		initialize();
		Drawingpanel.setPAF(PAFs[conPanel.wallOption.getSelectedIndex()],0);
		Drawingpanel.setLayout(null);
		selectedFreq = freqf[conPanel.selectFreq.getSelectedIndex()]; 
		Drawingpanel.setGW(conPanel.gridSlider.getValue());
		
		
		
		
		Drawingpanel.setFreq(selectedFreq);
		
		Btmpanel = new JPanel();
		
		frame.getContentPane().add(Btmpanel, BorderLayout.SOUTH);
		
		JButton Zout = new JButton("-");
		Zout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Zoomslider.setValue(Zoomslider.getValue() -25);
				Drawingpanel.setZoomScale(2.*Zoomslider.getValue()/Zoomslider.getMaximum());
			}
		});
		Btmpanel.add(Zout);
		
		 Zoomslider = new JSlider(SwingConstants.HORIZONTAL,50,200,100);
		 Zoomslider.setMajorTickSpacing(25);
		 Zoomslider.setPaintTicks(true);
		 Zoomslider.setSnapToTicks(true);
		Zoomslider.addChangeListener(new ChangeListener() {
			 
	         @Override
			public void stateChanged(ChangeEvent e) {
	        	 Drawingpanel.setZoomScale(2.*Zoomslider.getValue()/Zoomslider.getMaximum());
	        	// System.out.println(2.*Zoomslider.getValue()/Zoomslider.getMaximum());
	            zoomAmount.setText(Zoomslider.getValue() + "%");
	            
	         }
	});
		Btmpanel.add(Zoomslider);
		
		JButton Zin = new JButton("+");
		Zin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Zoomslider.setValue(Zoomslider.getValue() +25);
				Drawingpanel.setZoomScale(2.*Zoomslider.getValue()/Zoomslider.getMaximum());
			}
		});
		
		Btmpanel.add(Zin);
		 zoomAmount = new JLabel(Zoomslider.getValue() + "%");
		 Btmpanel.add(zoomAmount);
		 
		 JButton uptxt = new JButton("Uptxt");
		 uptxt.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 	/*	//cut text from allessid.txt  and find avg   allessid.txt ----> allpostest2.txt
		 		JFileChooser fileChooser = new JFileChooser();
		 		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));		
		 		int result = fileChooser.showSaveDialog(null);
		 		 if(result == JFileChooser.APPROVE_OPTION)
                 {
					 File file = fileChooser.getSelectedFile();
					 ArrayList<Integer> allVal = new ArrayList<Integer>();
					// ArrayList<String> headText = new ArrayList<String>();
					// ArrayList<Integer> numbers = new ArrayList<Integer>();
					 try
                     {
						 @SuppressWarnings("resource")
						Writer wr = new FileWriter("allPosTest2.txt");
						 
						 for (String line : Files.readAllLines(Paths.get(file.toString()))) {
							 if(line.contains("="))
							 {
							     String parts[] = line.split("\\=");
							     //System.out.print(parts[2]);
							     String parts2[] = parts[2].split("\\s+");
							     allVal.add(Integer.parseInt(parts2[0]));
							     wr.write(parts2[0]);
							     wr.write(System.lineSeparator());
							     
							 }
							 if(line.contains("_")){
							
									double sum = 0;//find avg
									if(!allVal.isEmpty()){
										 for (Integer temp : allVal) {
										        sum += temp;
										    }
										
										 sum = sum/allVal.size();
										 int rounded = (int) Math.round(sum);
										 wr.write("avg = : "+String.valueOf(rounded));
										// System.out.println(rounded);
										 wr.write(System.lineSeparator());
										 allVal.clear();
									}
									
						 wr.write(line);
								 wr.write(System.lineSeparator());
								 
							 }
						 }
						 
						wr.close();
						 
                     }
                     catch(Exception io)
                     {
                    	// System.out.println(io);
                        // System.exit(1);
                     } 
					 
                 }
		 		
		 		*/
		 		
		 		
		 	/*	
		 		// import position to drawingpanel   ----->onlyfor sample pos (realPos30.txt)

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));			
				int result = fileChooser.showSaveDialog(null);
				 if(result == JFileChooser.APPROVE_OPTION)
                 {
					 File file = fileChooser.getSelectedFile();
					 ArrayList<Integer> numbers = new ArrayList<Integer>();
					 try
                     {
						 
						 for (String line : Files.readAllLines(Paths.get(file.toString()))) {
						     for (String part : line.split("\\s+")) {
						         Integer i = Integer.valueOf(part);
						         numbers.add(i);
						         //System.out.println(i);
						     }
						 }
						 for(int k = 2; k<numbers.size();k+=4){
							 Spot temp = new Spot(new Point(numbers.get(k),numbers.get(k+1)),0);
							 Drawingpanel.test.add(temp);
							 
						 }
						 
                     }
                     catch(Exception io)
                     {
                    	 System.out.println(io);
                        // System.exit(1);
                     } 
					 
                 }
				*/
		 		
		 		
		 	/*//sample data-------------------------allpostest2---->AP'data + values
		 		JFileChooser fileChooser = new JFileChooser();
		 		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));		
		 		int result = fileChooser.showSaveDialog(null);
		 		 if(result == JFileChooser.APPROVE_OPTION)
                 {
					 File file = fileChooser.getSelectedFile();
					 ArrayList<Integer> allVal = new ArrayList<Integer>();
					// ArrayList<String> headText = new ArrayList<String>();
					// ArrayList<Integer> numbers = new ArrayList<Integer>();
					 try
                     {
						 @SuppressWarnings("resource")
						Writer wr = new FileWriter("521ALL.txt");
						 boolean found = false;
						 for (String line : Files.readAllLines(Paths.get(file.toString()))) {
							 if(line.contains("_"))
							 {
							     String parts[] = line.split("\\_");
							     //System.out.print(parts[2]);
							     String parts2[] = parts[2].split("\\s+");
							     System.out.println(parts2[2]);
							     if(parts2[0].equals("521")){
							    	// allVal.add(Integer.parseInt(parts2[0]));
							    	 found = true;
							    	 System.out.println(parts2[1]+" "+parts2[2]+" ");
							    	 wr.write(parts2[1]+" "+parts2[2]+" ");
								   //  wr.write(System.lineSeparator());
							     
							     }
							    
							     
							 }if(found)
							 	{
								 
							 if(line.contains("avg")){
								 String parts[] = line.split("\\s+");
								 System.out.println(parts[3]);
								 wr.write(parts[3]);
								 wr.write(System.lineSeparator());
								 found = false;
							 			}
							 	}
						 }
						 
						wr.close();
						 
                     }
                     catch(Exception io)
                     {
                    	// System.out.println(io);
                        // System.exit(1);
                     } 
					 
                 }*/

		 		// import position to drawingpanelwith values

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setDialogTitle("Up Text");
				int result = fileChooser.showSaveDialog(null);
				 if(result == JFileChooser.APPROVE_OPTION)
                 {
					 File file = fileChooser.getSelectedFile();
					// ArrayList<Integer> numbers = new ArrayList<Integer>();
					 try
                     {
						 
						 for (String line : Files.readAllLines(Paths.get(file.toString()))) {
							// System.out.println(line);
							 	String parts[] = line.split("\\s+");
							 
							 	//if(Integer.parseInt(parts[2]) < -90)
							 		//parts[2] = "-90";
//								System.out.println(parts[0]+","+parts[1]+","+parts[2]);
								Point Ptemp = new Point(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
						    	 Spot temp = new Spot(Ptemp,Integer.parseInt(parts[2]));
						    	 int checkDup = Drawingpanel.findDupSpot(Ptemp);
						    	 if(checkDup != -1){
						    		 if(Integer.parseInt(parts[2]) > Drawingpanel.Spots.get(checkDup).value){
						    			 Drawingpanel.Spots.get(checkDup).value = Integer.parseInt(parts[2]);
						    			 
						    		 }
						    		 
						    		 
						    	 }else{
						    		 
						    		 Drawingpanel.Spots.add(temp);
						    	 }
						    	
						         //System.out.println(i);
						    
						 }
						
						 
                     }
                     catch(Exception io)
                     {
                    	 System.out.println(io);
                        // System.exit(1);
                     } 
					 Drawingpanel.setColor();
					 System.out.println("sizezweswdaszwdawd =    "+Drawingpanel.Spots.size());
                 }
		 	}
		 });
		 Btmpanel.add(uptxt);
		 
		 JButton importPos = new JButton("ImportPosnCal");
		 importPos.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		
		 		// import position to drawingpanel   ----->onlyfor sample pos (realPos30.txt)

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showDialog(conPanel, "Import Posncal");
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));			
				int result = fileChooser.showSaveDialog(null);
				 if(result == JFileChooser.APPROVE_OPTION)
                 {
					 File file = fileChooser.getSelectedFile();
					// ArrayList<Integer> numbers = new ArrayList<Integer>();
					 try
                     {
						 
						 for (String line : Files.readAllLines(Paths.get(file.toString()))) {
							 String test = isItinThisArray(line);
						     if(test.equals("NO")){	
						    	 String parts[] = line.split("\\s+");
						    	 Spot temp = new Spot(new Point(Integer.parseInt(parts[0]),Integer.parseInt(parts[1])),-999);
								 Drawingpanel.SampleSpots.add(temp);
							 }else{
								 String part[] = line.split("\\s+");
									 AP tempAP = new AP(Integer.parseInt(part[0]),Integer.parseInt(part[1]),(float) 2.4);
									 Drawingpanel.APshow.add(tempAP);
									 Drawingpanel.APs.add(tempAP);
								 	}	 
								 
							 
						 }
						/* for(int k = 2; k<numbers.size();k+=4){
							 Spot temp = new Spot(new Point(numbers.get(k),numbers.get(k+1)),-999);
							 Drawingpanel.SampleSpots.add(temp);
							 //System.out.println(Drawingpanel.SampleSpots.get(0).getPos());
						 }*/
						// for(int l=0;l<Drawingpanel.APshow.size();l++){
							 
							// System.out.println(Drawingpanel.APs.get(l).getPos());
						// }
						 for(int j=0;j<Drawingpanel.SampleSpots.size();j++){
							// System.out.println(Drawingpanel.SampleSpots.get(j).value);
							 
							// System.out.println(Drawingpanel.SampleSpots.get(j).getPos());
						 }
						 Drawingpanel.SampleRecal();
						for(int j=0;j<Drawingpanel.SampleSpots.size();j++){
//							 System.out.println(Drawingpanel.SampleSpots.get(j).value);
							 
							 //System.out.println(Drawingpanel.SampleSpots.get(j).getPos());
						 }
						 
						 Drawingpanel.repaint();
						 System.out.println("sizeawdawdawdawd = = = "  + Drawingpanel.SampleSpots.size());
                     }
                     catch(Exception io)
                     {
                    	 System.out.println(io);
                        // System.exit(1);
                     } 
					 
                 }
				
		 		
		 	}
		 });
		 Btmpanel.add(importPos);
		 
		 JButton saveCalculated = new JButton("Savecal(samplePoints)");
		 saveCalculated.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		
		 		
		 		try {
					Writer wr = new FileWriter("521calcucalted.txt");
					for(int j=0;j<Drawingpanel.SampleSpots.size();j++){
						wr.write(Integer.toString(Drawingpanel.SampleSpots.get(j).getPos().x)+" "+Integer.toString(Drawingpanel.SampleSpots.get(j).getPos().y)+" "+Drawingpanel.SampleSpots.get(j).value);
						wr.write(System.lineSeparator());
					}
					wr.close();
					//temptest.clear();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 		
		 		
		 	}
		 });
		 Btmpanel.add(saveCalculated);

		 
		 JButton saveallPos = new JButton("SaveAllPos");
		 saveallPos.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent arg0) {
		 		try {
					Writer wr = new FileWriter("AllPosCalculated.txt");
					for(int j=0;j<Drawingpanel.Spots.size();j++){
						wr.write(Integer.toString(Drawingpanel.Spots.get(j).getPos().x)+" "+Integer.toString(Drawingpanel.Spots.get(j).getPos().y)+" "+Drawingpanel.Spots.get(j).value);
						wr.write(System.lineSeparator());
					}
					wr.close();
				
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 		
		 	}
		 });
		 Btmpanel.add(saveallPos);
		 
		 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		//frame.setEnabled(true);
		//frame.setResizable(true);
		//frame.setBounds(0,0,1024,700);
	//	frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		RightPane = new JPanel();
		Drawingpanel = new drawPanel();
		
	
		conPanel = new ControlPanel();
		conPanel.btnDelete.setLocation(168, 32);
		 
		
		
		
		
		//MENUS---------------------------------------------------------------------------
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoad = new JMenuItem("Load");
		
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));			
				int result = fileChooser.showDialog(conPanel, "Load");
				 if(result == JFileChooser.APPROVE_OPTION)
                 {
					 
					 try
                     {
						
                     File file = fileChooser.getSelectedFile();
                     FileInputStream input = new FileInputStream(file);
                     @SuppressWarnings("resource")
					ObjectInputStream inputObject= new ObjectInputStream(input);
                     savedObj loaded = (savedObj) inputObject.readObject();
                     Drawingpanel.setPath(loaded.selectedFile_S);
                     scroll.setViewportView(Drawingpanel);
                     Drawingpanel.WList = loaded.WList_saved;
                     Drawingpanel.APs = loaded.APs_saved;
                     Drawingpanel.Detecs = loaded.Detecs_saved;
                     Drawingpanel.Spots = loaded.Spots_saved;
                     Drawingpanel.gridDist = loaded.gridDist;
                     Drawingpanel.gridDistP = loaded.gridDistP;
                     Drawingpanel.gw = loaded.gw_saved;
                     Drawingpanel.cGW = loaded.cGW_saved;
                     Drawingpanel.curY = loaded.currentY_saved;
                    // System.out.println( loaded.APshow_saved.get(0).getPos());
                    // Drawingpanel.APshow = loaded.APshow_saved;
                     int count = Drawingpanel.APs.size();
                     String[] numAPs = new String[count];
					 numAPs[0]="all";
					 for(int h = 1; h< numAPs.length;h++){
						 numAPs[h] = String.valueOf(h);
					 }
                     DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) conPanel.numAP.getModel();
					 model.removeAllElements();
					 for(String temp : numAPs){
						 model.addElement(temp);
						 
					 }
                     AP tempAP = new AP(npx,npy,Drawingpanel.curFreq);
                     conPanel.numAP.setModel(model);
                     //
                  // Drawingpanel.APshow = loaded.APshow_saved;
                     String[] numChannel = new String[4];
					 numChannel[0]="all";
					 numChannel[1]="1";
					 numChannel[2]="6";
					 numChannel[3]="11";
                     DefaultComboBoxModel<String> modelCh = (DefaultComboBoxModel<String>) conPanel.numChannel.getModel();
					 modelCh.removeAllElements();
					 for(String temp : numChannel){
						 modelCh.addElement(temp);
						 
					 }
                     conPanel.numChannel.setModel(modelCh);
                     Drawingpanel.reCal();
                     Drawingpanel.repaint();     
                     //
                     //detec
                     int countD = Drawingpanel.Detecs.size();
                     String[] numDetecs = new String[countD];
					 numDetecs[0]="all";
					 for(int h = 1; h< numDetecs.length;h++){
						 numDetecs[h] = String.valueOf(h);
					 }
//                     DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) conPanel.numDetec.getModel();
					 model.removeAllElements();
					 for(String temp : numDetecs){
						 model.addElement(temp); 
					 }
					 Detec tempDetec = new Detec(npx,npy);
                     conPanel.numDetec.setModel(model);
                     Drawingpanel.reCal();
                     Drawingpanel.repaint();           
                   //  System.out.println(loaded.APs_saved.get(0).getPos());
                     }
                     catch(Exception io)
                     {
                    	 System.out.println(io.getMessage());
                    	 System.out.println("Bughere");
                    	
                        // System.exit(1);
                     } 
					 
                 }
			}
		});
		mnFile.add(mntmLoad);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int result = fileChooser.showSaveDialog(null);
				
				  if(result == JFileChooser.APPROVE_OPTION)
                  {
					  
					  try
				      {
						  File file = fileChooser.getSelectedFile();
						  FileOutputStream opf = new FileOutputStream(file);				     
				         ObjectOutputStream out = new ObjectOutputStream(opf);
				         savedObj saveOb = new savedObj(selectedFile,Drawingpanel.WList,Drawingpanel.APs,Drawingpanel.Detecs,Drawingpanel.Spots,Drawingpanel.gridDist,Drawingpanel.gridDistP,Drawingpanel.gw,Drawingpanel.curY,Drawingpanel.cGW);
				         out.writeObject(saveOb);
				         System.out.println("wr done!!");
				         out.close();
				         opf.close();
				        // System.out.printf("Serialized data is saved in /tmp/employee.ser");
				      }catch(IOException i)
				      {
				          i.printStackTrace();
				      }
					  
					  
                  }
				
			}
		});
		mnFile.add(mntmSave);
		
		JMenuItem saveImage = new JMenuItem("saveImg");
		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				int returnVal = fc.showSaveDialog(frame); //parent component to JFileChooser
				if (returnVal == JFileChooser.APPROVE_OPTION) { //OK button pressed by user
				        File file = fc.getSelectedFile(); //get File selected by user
				       try {
				    	   
				    	   ImageIO.write(Drawingpanel.combined, "png", file);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} //use its name

				       // ...
				        //your writing code goes here
				}
			}
		});
		mnFile.add(saveImage);
		
		
		
		//right pane -----------------------------------------------------------------------------------------
		RightPane.setLayout(new GridLayout(7, 2));
		
		GradientLabel color1 = new GradientLabel("",new Color(0,51,0,150),new Color(0,255,0,150));
		RightPane.add(color1);
		
		JLabel txt1 = new JLabel(" >= -60 dbm");
		RightPane.add(txt1);
		
		GradientLabel color2 = new GradientLabel("",new Color(0,255,0,150),new Color(128,255,0,150));
		RightPane.add(color2);
		
		JLabel txt2 = new JLabel(" >= -65 dbm");
		RightPane.add(txt2);
		
		GradientLabel color3 = new GradientLabel("",new Color(128,255,0,150),new Color(255,255,0,150));
		RightPane.add(color3);
		
		JLabel txt3 = new JLabel(" >= -72 dbm");
		RightPane.add(txt3);
		
		GradientLabel color4 = new GradientLabel("",new Color(255,255,0,150),new Color(255,255,50,150));
		RightPane.add(color4);
		
		JLabel txt4 = new JLabel(" >= -76 dbm");
		RightPane.add(txt4);
		
		GradientLabel color5 = new GradientLabel("",new Color(255,255,51,150),new Color(255,128,0,150));
		RightPane.add(color5);
		
		JLabel txt5 = new JLabel(" >= -85 dbm");
		RightPane.add(txt5);
		
		GradientLabel color6 = new GradientLabel("",new Color(255,128,0,150),new Color(255,0,0,150));
		RightPane.add(color6);
		
		JLabel txt6 = new JLabel(" >= -100 dbm");
		RightPane.add(txt6);
		
		JLabel showDbmRight = new JLabel();
		showDbmRight.setOpaque(true);
		
		RightPane.add(showDbmRight);
		
		//right pane -----------------------------------------------------------------------------------------
		
		scroll = new JScrollPane(Drawingpanel);
		
		//conPanel zone-------------------------------------------------------------------------------------
		conPanel.btnUpload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Drawingpanel.reset();
				 JFileChooser file = new JFileChooser();
				 file.setCurrentDirectory(new File(System.getProperty("user.dir")));
					//filter the files 
				 
				 FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
					file.addChoosableFileFilter(filter);
				   int result = file.showDialog(conPanel, "Upload");;
					
				 //if the user click on save in Jfilechooser 
				 if(result == JFileChooser.APPROVE_OPTION)  {
					 
					 //set pic to Jl ebel on Jscroll
					  selectedFile = file.getSelectedFile();
					// String path = selectedFile.getAbsolutePath();
					 try {
						Drawingpanel.setPath(selectedFile);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					
					
					 
				 }
				//if the user click on cancle in Jfilechooser
		          else if(result == JFileChooser.CANCEL_OPTION){
		              System.out.println("No File Select");
		          }
				
				 scroll.setViewportView(Drawingpanel);
				 Drawingpanel.repaint();
				
			}
		});
		
		conPanel.btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 //Drawingpanel.repaint();
				Drawingpanel.setScale();	
				
			//	 System.out.println(Drawingpanel.scale);
			}
		});
		
		
		
		conPanel.btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				Drawingpanel.changeModeTo(6);
				System.out.println(Drawingpanel.currentMode);
			}
		});
		
		conPanel.btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 
				Drawingpanel.setPAF(-Float.parseFloat(conPanel.customPAF.getText()),6);
				System.out.println(Drawingpanel.curPAF);
			}
		});
		
		conPanel.btnGeneticAlgo.addActionListener (new ActionListener()  { 
			@Override
			public void actionPerformed(ActionEvent e) {
				 //Drawingpanel.repaint();
				System.out.println("Population size   "+conPanel.popSize.getText());
				System.out.println("Powerlevel Max    "+conPanel.powMax.getText());
				System.out.println("Round    "+conPanel.roundMax.getText());
				System.out.println("Mutation    "+conPanel.mutationRate.getText());
				System.out.println("Parent use rate    "+conPanel.parentUseRate.getText());
				System.out.println("End count    "+conPanel.endCount.getText());
				geneticAlgorithm(true);
				//emergencyRefresh();
			//	 System.out.println(Drawingpanel.scale);
			}
		});
		
//		conPanel.btnCo_channel.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				testCo_Channel();
//				
//			}
//
//		});

		conPanel.btnCoverage.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Drawingpanel.TestAreas.size()<3) {
					System.out.println("Need Test Area");
				}else testCoverage();
				
			}

		});
		
		conPanel.btnExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 double [] exVals = new double[5];
				 double [] exDists = new double[5];
				 
				exVals[0] = Double.parseDouble(conPanel.exVals1.getText());
				exVals[1] = Double.parseDouble(conPanel.exVals2.getText());
				exVals[2] = Double.parseDouble(conPanel.exVals3.getText());
				exVals[3] = Double.parseDouble(conPanel.exVals4.getText());
				exVals[4] = Double.parseDouble(conPanel.exVals5.getText());
				
				exDists[0] = Double.parseDouble(conPanel.Dists1.getText());
				exDists[1] = Double.parseDouble(conPanel.Dists2.getText());
				exDists[2] = Double.parseDouble(conPanel.Dists3.getText());
				exDists[3] = Double.parseDouble(conPanel.Dists4.getText());
				exDists[4] = Double.parseDouble(conPanel.Dists5.getText());
				
				//double[] x = new double[5];
				double[][] vecX = new double[5][];
			   // double[] y = new double[x.length];
			   // x[0]=10;  x[1]=20; x[2]= 50; x[3]=100; x[4]=300;
			   // y[0]=-70; y[1]=-75; y[2]=-90; y[3]=-110; y[4]=-125;
			    for (int i=0; i<exDists.length; i++) {
			    	exVals[i] = (exVals[i] + 40.0459970202808 - 1)/-10.0;
			    	vecX[i] = new double[]{Math.log10(exDists[i])};
			    }
				
				OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
		        ols.setNoIntercept(true); // let the implementation include a constant in xVector if desired
		        ols.newSampleData(exVals, vecX); // provide the data to the model
		        RealMatrix coef = MatrixUtils.createColumnRealMatrix(ols.estimateRegressionParameters());
		        //System.out.println(coef);
		        currentY = (float) coef.getEntry(0,0);
		        //System.out.println(ks);
		        conPanel.showYLabel.setText(Double.toString(currentY));
		        
	            Drawingpanel.setY(currentY);
				
			}
		});
		
		
		
		
		
		
		conPanel.modeListCombo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Drawingpanel.changeModeTo( conPanel.modeListCombo.getSelectedIndex());
				currentMode = conPanel.modeListCombo.getSelectedIndex();
				//-------------------check mode-----------------
				
			
				if(currentMode == 2){
					
					
					
				}else{
					
					
				}
				if(currentMode != 0){
					conPanel.gridSlider.setVisible(false);
					
					
				}else{
					conPanel.gridSlider.setVisible(true);
					
				}
					
				if(currentMode != 3){
					conPanel.exVals1.setVisible(false);
					conPanel.exVals2.setVisible(false);
					conPanel.exVals3.setVisible(false);
					conPanel.exVals4.setVisible(false);
					conPanel.exVals5.setVisible(false);
					conPanel.Dists1.setVisible(false);
					conPanel.Dists2.setVisible(false);
					conPanel.Dists3.setVisible(false);
					conPanel.Dists4.setVisible(false);
					conPanel.Dists5.setVisible(false);
					
					
				}else{
					conPanel.exVals1.setVisible(true);
					conPanel.exVals2.setVisible(true);
					conPanel.exVals3.setVisible(true);
					conPanel.exVals4.setVisible(true);
					conPanel.exVals5.setVisible(true);
					conPanel.Dists1.setVisible(true);
					conPanel.Dists2.setVisible(true);
					conPanel.Dists3.setVisible(true);
					conPanel.Dists4.setVisible(true);
					conPanel.Dists5.setVisible(true);
					conPanel.btnExecute.setVisible(true);
									
				}
				
				System.out.println(currentMode);
			}
		});
		
		
		conPanel.Yslider.addChangeListener(new ChangeListener() {
			 
	         @Override
			public void stateChanged(ChangeEvent e) {
	            int value = conPanel.Yslider.getValue();
	             currentY =  (float) (value / 10.0);
	             conPanel.showYLabel.setText(Double.toString(currentY));
	             Drawingpanel.setY(currentY);
	            System.out.println(currentY);
	         }
	});
		
		conPanel.gridSlider.addChangeListener(new ChangeListener() {
			 
	         @Override
			public void stateChanged(ChangeEvent e) {
	            int value = conPanel.gridSlider.getValue();
	            // currentY =  (float) (value / 10.0);
	             conPanel.showGridsize.setText(Integer.toString(value));
	             Drawingpanel.setGW(value);
	            System.out.println(value);
	         }
	});
		

		conPanel.wallOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(conPanel.wallOption.getSelectedIndex()==6){
					
					conPanel.customPAF.setVisible(true);
					conPanel.btnSubmit.setVisible(true);
					conPanel.minus.setVisible(true);
				}else{
				currentPAF = PAFs[conPanel.wallOption.getSelectedIndex()];
				conPanel.customPAF.setVisible(false);
				conPanel.btnSubmit.setVisible(false);
				conPanel.minus.setVisible(false);
				}
				Drawingpanel.setPAF(currentPAF,conPanel.wallOption.getSelectedIndex());
				System.out.println(currentPAF);
			}
		});
		
		conPanel.numAP.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent  e) {
				if(e.getStateChange()==1){
					int index = conPanel.numAP.getSelectedIndex();
//					System.out.println("select AP " + index);
//					System.out.println(index+"num");
					if(index!= 0){
					Drawingpanel.APshow.clear();
						//AP temp = new AP(Drawingpanel.APs.get(index).posx,Drawingpanel.APs.get(index).posy,Drawingpanel.APs.get(index).freq);
					AP temp = new AP(Drawingpanel.APs.get(index-1));
					Drawingpanel.APshow.add(temp);
					//System.out.println("wdsalijdoiaefjaliwjisle");
					Drawingpanel.reCal();
					Drawingpanel.repaint();
					
				}else{
					//Drawingpanel.APshow.clear();
					//Drawingpanel.APshow = Drawingpanel.APs;
					Drawingpanel.APshow.clear();
					for(int i=0;i<Drawingpanel.APs.size();i++){
						AP temp = new AP(Drawingpanel.APs.get(i));
						Drawingpanel.APshow.add(temp);
					}
					Drawingpanel.reCal();
					Drawingpanel.repaint();
				}	
				}	
			}
		});
		
		conPanel.numDetec.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent  e) {
				if(e.getStateChange()==1){
					int index = conPanel.numDetec.getSelectedIndex();
//					System.out.println(index+"num");
					if(index!= 0){
					Drawingpanel.Detecshow.clear();
					Detec temp = new Detec(Drawingpanel.Detecs.get(index-1));
					Drawingpanel.Detecshow.add(temp);
					Drawingpanel.reCal();
					Drawingpanel.repaint();
					
				}else{
					Drawingpanel.Detecshow.clear();
					for(int i=0;i<Drawingpanel.Detecs.size();i++){
						Detec temp = new Detec(Drawingpanel.Detecs.get(i));
						Drawingpanel.Detecshow.add(temp);
					}
					Drawingpanel.reCal();
					Drawingpanel.repaint();
				}	
				}	
			}
		});
		
		conPanel.numChannel.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent  e) {
				if(e.getStateChange()==1){
					int index = conPanel.numChannel.getSelectedIndex();
//					System.out.println("select channel "+ index);
//					System.out.println(index+"num");
					if(index!= 0){
					Drawingpanel.APshow.clear();
						//AP temp = new AP(Drawingpanel.APs.get(index).posx,Drawingpanel.APs.get(index).posy,Drawingpanel.APs.get(index).freq);
					for (int i = 0; i < Drawingpanel.APs.size(); i++) {
						if(Drawingpanel.APs.get(i).channel==(index-1)*5+1) {
							AP temp = new AP(Drawingpanel.APs.get(i));
							Drawingpanel.APshow.add(temp);
						}
					}
					//System.out.println("wdsalijdoiaefjaliwjisle");
					Drawingpanel.reCal();
					Drawingpanel.repaint();
					
				}else{
					//Drawingpanel.APshow.clear();
					//Drawingpanel.APshow = Drawingpanel.APs;
					Drawingpanel.APshow.clear();
					for(int i=0;i<Drawingpanel.APs.size();i++){
						AP temp = new AP(Drawingpanel.APs.get(i));
						Drawingpanel.APshow.add(temp);
					}
					Drawingpanel.reCal();
					Drawingpanel.repaint();
				}	
				}	
			}
		});
			
		
		conPanel.selectFreq.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedFreq = freqf[conPanel.selectFreq.getSelectedIndex()]; 
				Drawingpanel.setFreq(selectedFreq);
				System.out.println(selectedFreq);
			}
		});
		
		//conPanel zone-------------------------------------------------------------------------------------
		
		//Drawingpanel Zone---------------------------------------------------------------------------------
		Drawingpanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//scale is if it is scaling mode
				 if(Drawingpanel.scale){
				if(Drawingpanel.drawingScale){//after clicked 1time do this condition
					
					//try to get distances if error change stage to non scale mode
					 try {
						 Component source = (Component) e.getSource();
						float distanceScale =Float.parseFloat( JOptionPane.showInputDialog(source,
						            "How far is it (in meters)", null));
						Drawingpanel.gridDist = (Drawingpanel.gw*distanceScale)/Drawingpanel.ScaleLine.getDistance();//2 points = how much meter?
						Drawingpanel.cGW = (int) (Drawingpanel.gw/Drawingpanel.gridDist);
						Drawingpanel.gridDistP = (70*Drawingpanel.gw)/Drawingpanel.gridDist;//70 m = how much Pixel?
						System.out.println("input = "+distanceScale);
						System.out.println("distanceXY = "+Drawingpanel.ScaleLine.getDistance());
						System.out.println("1grid = "+Drawingpanel.gridDist);
						Drawingpanel.scale = false;//when completed scaling change mode to non scale mode
						Drawingpanel.ScaleLine.HideLine();
						Drawingpanel.reCal();
					} catch (NumberFormatException e1) {
						 Component source = (Component) e.getSource();
						JOptionPane.showMessageDialog(source, "Distance is supposed to be number.");
						//e1.printStackTrace();
						Drawingpanel.ScaleLine.HideLine();
					}
				}else{//first clicking keep first position
					
					Drawingpanel.ScaleLine.setP1(new Point(npx,npy));
					Drawingpanel.ScaleLine.setP2(new Point(npx,npy));
					
				}
				
				Drawingpanel.drawingScale = !Drawingpanel.drawingScale; //when clicking stage of drawing must change
				
			 }else if(Drawingpanel.currentMode == 1){// if it's wall mode draw wall
				
				 if(Drawingpanel.Walldrawing){
					 Drawingpanel.temp.setWPAF(Drawingpanel.curPAF);//set current PAF to the wall
					 Drawingpanel.temp.setColor(Drawingpanel.selectedWall);
					 Drawingpanel.WList.add(Drawingpanel.temp);
					 Drawingpanel.reCal();
					// Drawingpanel.SampleRecal();
					 Drawingpanel.temp = new Line();
					 System.out.print(Drawingpanel.temp);
				 }else{
					
					 Drawingpanel.temp = new Line(new Point(npx,npy),new Point(npx,npy));
					 
					 				 
				 }
				 	 
				 Drawingpanel.Walldrawing = !Drawingpanel.Walldrawing;
				
				
					 
				 
				 
			 }else if(Drawingpanel.currentMode == 2){//AP
		//test--------------------------------------------------------------------------------------	
				
				 
				 if(!Drawingpanel.isthereWall(npx,npy)) {//check if there is a wall in the same npx npy
					 
					 AP tempAP = new AP(npx,npy,Drawingpanel.curFreq);
					 System.out.println(npx+","+npy);
					 
					 Drawingpanel.APs.add(tempAP);
					
					 
					 //update Combo box AP
					 int count = Drawingpanel.APs.size()+1;
					 String[] numAPs = new String[count];
					 numAPs[0]="all";
					 for(int h = 1; h< numAPs.length;h++){
						 
						 numAPs[h] = String.valueOf(h);
					 }
					
					DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) conPanel.numAP.getModel();
					 model.removeAllElements();
					 for(String temp : numAPs){
						 model.addElement(temp);
						 
					 }
					 conPanel.numAP.setModel(model);
					 //conPanel.numAP.setSelectedIndex(0);
					// for(int i =0;i<Drawingpanel.APs.size();i++)
					// System.out.println(Drawingpanel.APs.get(i).getPos());
					 Drawingpanel.reCal();
			 } //check if there is a wall in the same npx npy
			 }else if(Drawingpanel.currentMode == 3){//auto mode
				 
				 
				 
				 
			 }else if(Drawingpanel.currentMode == 4){//Detect
				 if(!Drawingpanel.isthereWall(npx,npy)) {//check if there is a wall in the same npx npy
					 
					 Detec tempDetec = new Detec(npx,npy);
					 System.out.println(npx+","+npy);
					 
					 Drawingpanel.Detecs.add(tempDetec);
					
					 
					 //update Combo box AP
					 int count = Drawingpanel.Detecs.size()+1;
					 String[] numDetecs = new String[count];
					 numDetecs[0]="all";
					 for(int h = 1; h< numDetecs.length;h++){
						 
						 numDetecs[h] = String.valueOf(h);
					 }
					
					DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) conPanel.numDetec.getModel();
					 model.removeAllElements();
					 for(String temp : numDetecs){
						 model.addElement(temp);
						 
					 }
					 conPanel.numDetec.setModel(model);
					 //conPanel.numAP.setSelectedIndex(0);
					// for(int i =0;i<Drawingpanel.APs.size();i++)
					// System.out.println(Drawingpanel.APs.get(i).getPos());
					 Drawingpanel.reCal();
			 }
				 
			 }else if(Drawingpanel.currentMode == 5){//Test Area
//				 if(!Drawingpanel.isthereWall(npx,npy)) {//check if there is a wall in the same npx npy
					 
					 TestArea tempArea = new TestArea(npx,npy);
					 System.out.println(npx+","+npy);
					 
					 Drawingpanel.TestAreas.add(tempArea);
//					System.out.println(Drawingpanel.TestAreas);
//					 if(Drawingpanel.TestAreadrawing){
////						 Drawingpanel.temp.setWPAF(Drawingpanel.curPAF);//set current PAF to the wall
////						 Drawingpanel.temp.setColor(Drawingpanel.selectedWall);
//						 Drawingpanel.TestAreas.add(Drawingpanel.temp);
//						 Drawingpanel.reCal();
//						// Drawingpanel.SampleRecal();
//						 Drawingpanel.temp = new Line();
//						 System.out.print(Drawingpanel.temp);
//					 }else{
//						
//						 Drawingpanel.temp = new Line(new Point(npx,npy),new Point(npx,npy));
//						 
//						 				 
//					 }
//					 	 
//					 Drawingpanel.TestAreadrawing = !Drawingpanel.TestAreadrawing;
					 Drawingpanel.reCal();
//			 }	 
				 
			 }else if(Drawingpanel.currentMode == 6){//delete mode
				// System.out.println(GBx+","+GBy);
				 Point p = snapGrid();
				 if(Drawingpanel.isthereAP(p.x,p.y)){
					 
					 Drawingpanel.APs.remove(Drawingpanel.detectedAP);
					 Drawingpanel.APshow.remove(Drawingpanel.detectedAP);
					 Drawingpanel.reCal();
				 }	 
				if(Drawingpanel.isthereWall(p.x,p.y))
				{
					Drawingpanel.WList.remove(Drawingpanel.detectedWallnum);
					// Drawingpanel.reCal();
					Drawingpanel.SampleRecal();
				}
				if(Drawingpanel.isthereDetec(p.x, p.y)) {
					Drawingpanel.Detecs.remove(Drawingpanel.detectedDetec);
					Drawingpanel.Detecshow.remove(Drawingpanel.detectedDetec);
					Drawingpanel.reCal();
				}
				if(Drawingpanel.isthereTestArea(p.x, p.y)) {
					Drawingpanel.TestAreas.remove(Drawingpanel.detectedDetec);
					Drawingpanel.TestAreas.remove(Drawingpanel.detectedDetec);
					Drawingpanel.reCal();
				}
				
					 
				 
				 
			 }else if(Drawingpanel.currentMode==0 ){
				// System.out.println(GBx+","+GBy);
				 Point p = snapGrid();
				 if(Drawingpanel.isthereAP(p.x,p.y)){
					 
					 Drawingpanel.ChangingPanel.setBounds(Drawingpanel.npx, Drawingpanel.npy, 200, 120);
					 Drawingpanel.ChangingPanel.setVisible(true);
					
					 
				 }
				 
				 
				 
			 }
				 
				//Spot temp = new Spot(new Point(npx,npy),0);
				//Drawingpanel.test.add(temp);
				//temptest.add(temp);
				// System.out.println(npx+","+npy);
			//	 System.out.println(Drawingpanel.test.get(0).getPos());
				 
				 Drawingpanel.repaint();
				
				
			}
		});
		
		
		
		Drawingpanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				//get the nearest snap point
                int x = e.getX(), y = e.getY();
                int mx = x % Drawingpanel.gw, my = y % Drawingpanel.gw;
                GBx = noZoomPosX(e.getX());
                GBy = noZoomPosY(e.getY());
               // System.out.println("testestestet +++   "+GBx);
               // System.out.println("dddddddddddd +++   "+e.getX());
                if (mx<Drawingpanel.gw/2){
                	npx =  (x - mx);
                	Drawingpanel.npx=(npx);
                    npx = noZoomPosX(npx);
                    
                	}
                else {
                	npx = x + (Drawingpanel.gw-mx);
                	Drawingpanel.npx=  npx ;
                    npx= noZoomPosX(npx);                   
                }
                if (my<Drawingpanel.gw/2){
                	npy =  (y - my);
                	Drawingpanel.npy= npy;
                    npy = noZoomPosY(npy);
                  
                }
                else{
                	npy = y + (Drawingpanel.gw-my);
                	Drawingpanel.npy= npy;
                    npy = noZoomPosY(npy);
                   
                }
              //  System.out.println(npx+","+npy);
             //   System.out.println(Drawingpanel.npx+","+Drawingpanel.npy+"   : graphic'pos");
              //if scaling mode do this  
                if (Drawingpanel.drawingScale) {
                	Drawingpanel.ScaleLine.setP2(new Point(npx,npy));
	               // repaint();
	            }
                else if(Drawingpanel.Walldrawing){
                	
                	Drawingpanel.temp.setP2(new Point(npx,npy));
                	// repaint();
                }
                
                //if hover Spots
                for(int i=0;i<Drawingpanel.SampleSpots.size();i++){
                	if(Drawingpanel.SampleSpots.get(i).getPos().equals(new Point(npx,npy))){
                		Drawingpanel.showCur = Drawingpanel.SampleSpots.get(i).value;
                	String showVal = Float.toString(Drawingpanel.showCur);
                	System.out.println(showVal);
                	//txt7.setText(showVal+","+findColor(Spots.get(i).value).getGreen());
                	//Drawingpanel.txt7.setText(showVal);
                	showDbmRight.setText(showVal);
                	showDbmRight.setBackground(Drawingpanel.findColor(Drawingpanel.showCur,(int) Drawingpanel.findMinSpots(Drawingpanel.Spots),(int)Drawingpanel.findMaxSpots(Drawingpanel.Spots)));
                	//Drawingpanel.txt7.setText(npx+","+ npy);
                //	Drawingpanel.txt7.setBounds(npx, npy, 200,30);
            		
            		}
                }
                
                
					 
				/*	 if(Drawingpanel.isthereWall(npx,npy))
					 {
						 Drawingpanel.txt7.setText("detected = "+Drawingpanel.testwall);
						 Drawingpanel.txt7.setBounds(npx, npy, 200,30);
					 }*/
					 
				 
              // System.out.println(npx+","+npy);
             //  System.out.println(Drawingpanel.APs.get(0).getPos());
             //  System.out.println(Drawingpanel.getPreferredSize());
                Drawingpanel.repaint();
                
               
				
			}
		});
		
		
		
		
		//Drawingpanel Zone--------------------------------------------------------------------------------
	
		 frame.getContentPane().add(conPanel,BorderLayout.LINE_START);
		 
		
		 frame.getContentPane().add(scroll,BorderLayout.CENTER);
		
		frame.getContentPane().add(RightPane, BorderLayout.LINE_END);
		
		try { 
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	
		
		
		
	
	}
	public Point snapGrid(){
		
		 int x = GBx, y = GBy;
         int mx = x % Drawingpanel.gw, my = y % Drawingpanel.gw;
         int realX,realY;
         if (mx<Drawingpanel.gw/2){
         	realX =  (x - mx);            
         	}
         else {
        	 realX = x + (Drawingpanel.gw-mx);                
         }
         if (my<Drawingpanel.gw/2){
        	 realY =  (y - my);
         
           
         }
         else{
        	 realY = y + (Drawingpanel.gw-my);
         
            
         }
         
         
		return new Point(realX,realY);
		
		
		
	}
	public int noZoomPosX(int _npx){
		//if(Drawingpanel.Zoomscale > 1){
			int newNpx = (int) (_npx/Drawingpanel.Zoomscale);
			
			return newNpx;
			
		//}else if(Drawingpanel.Zoomscale < 1){
		//	int newNpx = (int) (_npx*Drawingpanel.Zoomscale);
			
		//	return newNpx;
		//}
		
		//return _npx;
		
		
		
	}
	
	public int noZoomPosY(int _npy){
		//if(Drawingpanel.Zoomscale > 1){
			
			int newNpy =  (int) (_npy/Drawingpanel.Zoomscale);
			
			return newNpy;
			
	//	}else if(Drawingpanel.Zoomscale < 1){
	
	//		int newNpy = (int) (_npy*Drawingpanel.Zoomscale);
	//		return newNpy;
	//	}
		
		//return _npy;
		
		
		
	}
	public String isItinThisArray(String line){
		String s;
		if(line.contains("521")){
			s = "521";
	
			
		}else if(line.contains("516")){
			s = "516";
			
		}else if(line.contains("517")){
			
			s = "517";
			
		}else if(line.contains("518")){
			
			s = "518";
		}else if(line.contains("floor5")){
			
			s = "floor5";
			
		}else if (line.contains("Jumbo1")){
			s = "Jumbo1";
			
		}else if(line.contains("Jumbo2")){
			s = "Jumbo2";
			
		}else if(line.contains("Jumbo3")){
			s = "Jumbo3";
			
		}else{
			s = "NO";
			
			
		}
			
		return s;	
		
	}
	//GENETIC ALGORITHM _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
	
	public void geneticAlgorithm(boolean showdetaillog) {	
		
		int popSize = Integer.valueOf(conPanel.popSize.getText());
		int powMax = Integer.valueOf(conPanel.powMax.getText());
		int endCountMax = Integer.valueOf(conPanel.endCount.getText());
		int maxRound = Integer.valueOf(conPanel.roundMax.getText()); //Max round of genetic
		Float mutaterate = Float.valueOf(conPanel.mutationRate.getText());
		Float parentUseRate = Float.valueOf(conPanel.parentUseRate.getText());
		System.out.println("*******Start*******");
		System.out.println("Genetic Algorithm \npopulation size : "+popSize+"\tpowerlevelMax = "+powMax+"\tRound : "+maxRound+"\nMutation rate : "+
		mutaterate+"\tParent Rate : "+parentUseRate);
		ArrayList<Float> fitnessList = new ArrayList<Float>();
		ArrayList<ArrayList<Integer> > population =  new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer> > populationChannel =  new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> populationCalCheck =  new ArrayList<Integer>();
		
	    System.out.println("Random generate first Population");
		for(int i = 0;i<popSize;i++) {
			if (showdetaillog == true) {
				System.out.println();
				System.out.println("gene " + i);
			}
			initRandom(Drawingpanel.APs,population,powMax,populationChannel,populationCalCheck,showdetaillog);
			fitnessList.add(geneFitness(populationCalCheck,i,showdetaillog));
		}
		Float bestFitValue = (float) -99999.9;
		Float bestFitValueCurr = (float) -99999.9;
		int endCount = 0;
		int round=1;
		do {
//			bestFitValue = showBestFitness(fitnessList,population);
			bestFitValue = bestFitValueCurr;
			sortFitness(population,populationChannel,fitnessList,showdetaillog);
			mate(fitnessList,population,populationChannel,parentUseRate,populationCalCheck,showdetaillog);
			mutate(population,populationChannel,mutaterate,powMax,parentUseRate,populationCalCheck,showdetaillog);
//			fitnessList.clear();
//			System.out.println("before addNewFIT");
			if (showdetaillog == true) {
				showPop(population, populationChannel);
			}
			addAllNewFitness(fitnessList,population,populationChannel,populationCalCheck,showdetaillog);
			bestFitValueCurr = showBestFitness(fitnessList,population,populationChannel);
			if(bestFitValue>=bestFitValueCurr) {
				endCount++;
			}else {
				endCount=0;
			}
//			sortFitness(population,populationChannel,fitnessList);
			if (showdetaillog == true) {
				showfitness(fitnessList);
				System.out.println();
			}
			System.out.println("_ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ _");
			System.out.println("round "+round+"\tBestFitness: "+bestFitValueCurr);
			round++;
			if(round>maxRound) {
				break;
			}
			System.out.println("_ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ __ _ _ _ _ _ _");
//		}while(bestFitValue<=-100.00*Drawingpanel.Detecs.size()/5);
		}while(endCount<endCountMax);
		showBestFitness(fitnessList, population,populationChannel);
		emergencyRefresh();
		System.out.println("++++++++++++++++END GeneticAlgorithms++++++++++++++++");
		
//		ArrayList<Integer> gene = ptAPs(Drawingpanel.APs);
	}

	private void showfitness(ArrayList<Float> fitnessList) {
		System.out.println("Fitness list");
		int l = 0;
		for (int i = 0; i <fitnessList.size(); i++) {
			l++;
			if(l>5) {
				System.out.println();
				l = 0;
			}
			System.out.print(i+"/<"+fitnessList.get(i)+">/\t ");
		}
		System.out.println();
	}

	private void addAllNewFitness(ArrayList<Float> fitnessList, ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> populationChannel, ArrayList<Integer> populationCalCheck, boolean showdetaillog) {
		for (int i = 0; i < population.size(); i++) {
			for (int j = 0; j < Drawingpanel.APs.size(); j++) {
				Drawingpanel.APs.get(j).setPT(population.get(i).get(j));
				Drawingpanel.APs.get(j).setChannel(populationChannel.get(i).get(j));
			}
			if (showdetaillog == true) {
				System.out.println("Find fitness population "+i);
			}
				fitnessList.set(i, geneFitness(populationCalCheck, i, showdetaillog));
//				fitnessList.add(geneFitness(populationCalCheck,i));
		}
		System.out.println("addALLnewFIT");
		if (showdetaillog == true) {
			showPop(population, populationChannel);
			showfitness(fitnessList);
		}
	}

	private void mutate(ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> populationChannel, Float mutaterate,int _powMax, Float parentUseRate, ArrayList<Integer> populationCalCheck, boolean showdetaillog) {
		if(showdetaillog == true) {
			System.out.println("***************Mutate Function***************");
		}

		ArrayList<ArrayList<Integer>> newpopulation = new ArrayList<ArrayList<Integer>> ();
		ArrayList<ArrayList<Integer>> newpopulationChannel = new ArrayList<ArrayList<Integer>> ();
		Random rand = new Random();
		int mutateVal = rand.nextInt(_powMax);
		int mutateCH = rand.nextInt(2)*5+1;
		for (int i = 0; i < population.size(); i++) {
			newpopulation.add(population.get(i));
			newpopulationChannel.add(populationChannel.get(i));
		}
		for(int i = (int) (parentUseRate*population.size()); i<population.size(); i++) {
			for(int j=0; j<newpopulation.get(i).size();j++) {
				if(rand.nextInt(10000)<=mutaterate*10000) {
					while(newpopulation.get(i).get(j)==mutateVal) {
						mutateVal = rand.nextInt(_powMax);
					}
					newpopulation.get(i).set(j, mutateVal);
					if (showdetaillog == true) {
						System.out.println(("mutate at pop "+i+" gene "+j+" set to "+newpopulation.get(i).get(j)));
					}
				}
				if(rand.nextInt(10000)<=mutaterate*10000) {
					while(newpopulationChannel.get(i).get(j)==mutateCH) {
						mutateCH = rand.nextInt(2)*5+1;
					}
					newpopulationChannel.get(i).set(j, mutateCH);
					if (showdetaillog == true) {
						System.out.println(("mutate at pop_CH "+i+" gene "+j+" set to "+newpopulationChannel.get(i).get(j)));	
					}
				}
				populationCalCheck.set(i, 0);
			}
		}
		population = newpopulation;
		populationChannel =newpopulationChannel;
//		System.out.println("print population : ");
//		int j = 0;
//		for (int i = 0; i < population.size(); i++) {
//			System.out.print("/< "+i+">/ "+population.get(i)+"\t");
//			j++;
//			if(j>=5) {
//				System.out.println();
//				j=0;
//			}
//		}
		
	}

	private void mate(ArrayList<Float> fitnessList, ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> populationChannel, Float parentUseRate, ArrayList<Integer> populationCalCheck, boolean showdetaillog) {
		if (showdetaillog == true) {
			System.out.println("***************Mate Function***************");
		}
		ArrayList<ArrayList<Integer>> newpopulation = new ArrayList<ArrayList<Integer>> ();
		ArrayList<ArrayList<Integer>> newpopulationChannel = new ArrayList<ArrayList<Integer>> ();
		Random rand = new Random();
		
		int size = population.size();
        int i, j, k, l, m, n, p, q;
        
		while (newpopulation.size() < population.size() * (1.0-parentUseRate)) {
			i = rand.nextInt(size);
			j = rand.nextInt(size);
			k = rand.nextInt(size);
			l = rand.nextInt(size);
//			m = rand.nextInt(size);
//			n = rand.nextInt(size);
//			p = rand.nextInt(size);
//			q = rand.nextInt(size);
            while (j == i)
                j = rand.nextInt(size);
            while (k == i || k == j)
                k = rand.nextInt(size);
            while (l == i || l == j || l == k)
                l = rand.nextInt(size);
//            while (m == i || m == j || m == k || m == l)
//                m = rand.nextInt(size);
//            while (n == i || n == j || n == k || n == l || n == m)
//                n = rand.nextInt(size);
//            while (p == i || p == j || p == k || p == l || p == m || p == n)
//                p = rand.nextInt(size);
//            while (q == i || q == j || q == k || q == l || q == m || q == n || q == p)
//                q = rand.nextInt(size);
            
            if (showdetaillog == true) {
                System.out.println("Candidate :"+i+" "+j+" "+k+" "+l);
//                System.out.println("Candidate :"+i+" "+j+" "+k+" "+l+" "+m+" "+n+" "+p+" "+q+" ");
                System.out.println("c1 ="+population.get(i));
                System.out.println("c2 ="+population.get(j));
                System.out.println("c3 ="+population.get(k));
                System.out.println("c4 ="+population.get(l));
//                System.out.println("c5 ="+population.get(m));
//                System.out.println("c6 ="+population.get(n));
//                System.out.println("c7 ="+population.get(p));
//                System.out.println("c8 ="+population.get(q));
			}
            
            Individual c1 = new Individual(population.get(i),populationChannel.get(i));
            Individual c2 = new Individual(population.get(j),populationChannel.get(j));
            Individual c3 = new Individual(population.get(k),populationChannel.get(k));
            Individual c4 = new Individual(population.get(l),populationChannel.get(l));
//            Individual c5 = new Individual(population.get(m),populationChannel.get(m));
//            Individual c6 = new Individual(population.get(n),populationChannel.get(n));
//            Individual c7 = new Individual(population.get(p),populationChannel.get(p));
//            Individual c8 = new Individual(population.get(q),populationChannel.get(q));
//          
//            ArrayList<Integer> c1 = population.get(i);
//            ArrayList<Integer> c2 = population.get(j);
//            ArrayList<Integer> c3 = population.get(k);
//            ArrayList<Integer> c4 = population.get(l);
//            System.out.println("c1 is gene "+i+" = "+c1.pts);
//            System.out.println("c2 is gene "+j+" = "+c2.pts);
//            System.out.println("c3 is gene "+k+" = "+c3.pts);
//            System.out.println("c4 is gene "+l+" = "+c4.pts);
            
            Float f1 = fitnessList.get(i);
            Float f2 = fitnessList.get(j);
            Float f3 = fitnessList.get(k);
            Float f4 = fitnessList.get(l);
//            Float f5 = fitnessList.get(m);
//            Float f6 = fitnessList.get(n);
//            Float f7 = fitnessList.get(p);
//            Float f8 = fitnessList.get(q);
 
            ArrayList<Integer> w1, w2;
            ArrayList<Integer> chw1, chw2;
//            switch (findBestcandidate(f1,f2,f3,f4)) {
//			case 1: w1 = c1.pts;
//					chw1 = c1.channels;
//					break;
//			case 2: w1 = c2.pts;
//					chw1 = c2.channels;
//					break;
//			case 3: w1 = c3.pts;
//					chw1 = c3.channels;
//					break;
//			case 4: w1 = c4.pts;
//					chw1 = c4.channels;
//					break;
//			default: w1 = null;
//					 chw1 = null;
//					 break;
//			}
//            
//            switch (findBestcandidate(f5,f6,f7,f8)) {
//			case 1: w2 = c5.pts;
//					chw2 = c5.channels;
//					break;
//			case 2: w2 = c6.pts;
//					chw2 = c6.channels;
//					break;
//			case 3: w2 = c7.pts;
//					chw2 = c7.channels;
//					break;
//			case 4: w2 = c8.pts;
//					chw2 = c8.channels;
//					break;
//			default: w2 = null;
//					 chw2 = null;
//					 break;
//			}
            
            if (f1 > f2) {
                w1 = c1.pts;
                chw1 = c1.channels;
//                System.out.println("win1=" + i);
            }
            else {
                w1 = c2.pts;
                chw1 = c2.channels;
//                System.out.println("win1=" + j);
            }
            if (f3 > f4) {
                w2 = c3.pts;
                chw2 = c3.channels;
//                System.out.println("win2=" + k);
            }
            else {
                w2 = c4.pts;
                chw2 = c4.channels;
//                System.out.println("win2=" + l);
            }
//            while(newpopulation.size() < population.size() * (1.0-parentUseRate)) {//cross over
            if (showdetaillog) {
            	System.out.println("cross over");
			}
               	ArrayList<Integer> child1 = new ArrayList<Integer>();
            	ArrayList<Integer> child2 = new ArrayList<Integer>();
            	ArrayList<Integer> child1ch = new ArrayList<Integer>();
            	ArrayList<Integer> child2ch = new ArrayList<Integer>();
            	child1.addAll(w1);
            	child2.addAll(w2);
            	child1ch.addAll(chw1);
            	child2ch.addAll(chw2);
        		int index = rand.nextInt(w1.size());
        		int index2 = rand.nextInt(w1.size());
        		int indexCh = rand.nextInt(chw1.size());
        		int indexCh2 = rand.nextInt(chw1.size());
//        		System.out.println("Candidate : "+i+", "+j+", "+k+", "+l);
//        		System.out.println("crossover index is " + index +", "+index2);
//        		System.out.println("crossover indexChannel is " + indexCh +", "+indexCh2);
        		while(index>=index2) {
        			index = rand.nextInt(w1.size());
        			index2 = rand.nextInt(w1.size());
        		}
        		while(indexCh>=indexCh2) {
        			indexCh = rand.nextInt(chw1.size());
        			indexCh2 = rand.nextInt(chw1.size());
        		}
//        		}
        		for(int j1=index;j1<index2;j1++) {
            		child1.set(j1, w2.get(j1));
            		child2.set(j1, w1.get(j1));
        		}
        		for(int j1=indexCh;j1<indexCh2;j1++) {
            		child1ch.set(j1, chw2.get(j1));
            		child2ch.set(j1, chw1.get(j1));
        		}
        		if (showdetaillog) {
        			System.out.println();
                	System.out.print("w1 "+w1+"  \t\t\t\t");
                	System.out.println("w2 "+w2);
                	System.out.print("chw1 "+chw1+"  \t\t\t\t");
                	System.out.println("chw2 "+chw2);
                	
                	System.out.print("child 1 "+child1+"\t\t");
                	System.out.println("child 2 "+child2);
                	System.out.print("childCh 1 "+child1ch+"\t\t");
                	System.out.println("childCh 2 "+child2ch);
                	System.out.println();
				}

            	newpopulation.add(child1);
            	newpopulation.add(child2);
            	newpopulationChannel.add(child1ch);
            	newpopulationChannel.add(child2ch);
//            }
//            while(newpopulation.size()>population.size() * (1.0-parentUseRate)) {
//    			newpopulation.remove(newpopulation.size()-1);
//    			System.out.println("DELETE excess POP");
//    		}
//            System.out.println("old population     " + population);
//            System.out.println("old pop_channel    "+ populationChannel);
		}
		
//		for(int i = (int) (population.size()*(1.0-parentUseRate));i<population.size()*(1.0-parentUseRate);i++) {
//			population.set(i, newpopulation.get(i));
//		}
//		System.out.println("...MATE POPPulation...");
//		showPop(newpopulation, newpopulationChannel);
		int p0 = 0;
		for(int v= (int) (population.size()*parentUseRate);v<population.size() * (1.0-parentUseRate);v++) {
//			System.out.println("index p "+p);
			population.set(v, newpopulation.get(p0));
			populationChannel.set(v, newpopulationChannel.get(p0));
			populationCalCheck.set(v, 0);
			p0++;
		}
//		System.out.println("new population     "+ newpopulation);
//		System.out.println("new pop_channel    "+ newpopulationChannel);
//		System.out.println("newPop");
//		showPop(newpopulation, newpopulationChannel);
//		System.out.println("Pop");
//		showPop(population, populationChannel);
//		System.out.println("Current population " + population);
//		System.out.println("Current pop_channel" + populationChannel);
//		System.out.println("Sort print population : " + population);
//		showfitness(fitnessList);
	}
	private int findBestcandidate(Float f1, Float f2, Float f3, Float f4) {
		System.out.println("candidate val : "+f1+" "+f2+" "+f3+" "+f4+" ");
		int check = 0;
		if(f1 >= f2 && f1 >= f3 && f1 >= f4) {
			System.out.println(f1 >= f2);
			check = 1;
		} else
		if(f2 >= f1 && f2 >= f3 && f2 >= f4) {
			System.out.println(f2);
			check = 2;
		}else
		if(f3 >= f2 && f3 >= f1 && f3 >= f4) {
			System.out.println(f3);
			check = 3;
		}else
		if(f4 >= f2 && f4 >= f3 && f4 >= f1) {
			check = 4;
			System.out.println(f4);
		}
		System.out.println("FindBestCandidate : " + check);
		return check;
	}

	private void sortFitness(ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> populationChannel, ArrayList<Float> fitnessList, boolean showdetaillog) {//Insertion Sort
		if (showdetaillog == true) {
			System.out.println("Sort Fitness");
		}
		ArrayList<Integer> index = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> tmppop= new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> tmpCH= new ArrayList<ArrayList<Integer>>();
		for(int i = 0;i<population.size();i++) {
			index.add(i);
		}
//		System.out.println("population size: "+ population.size()+" \tindex size: " + index.size());
		for(int j = 1;j<fitnessList.size();j++) {
			int k = j;
			while(fitnessList.get(k)>fitnessList.get(k-1)) {
				Collections.swap(fitnessList, k, k-1);
				Collections.swap(population, k, k-1);
				Collections.swap(populationChannel, k, k-1);
				k--;
				if(k==0) break;
			}
		}
//		System.out.println("index sort: " + index);
//		for(int i = 0;i<population.size();i++) {
//			tmppop.add(population.get(index.get(i)));
//			tmpCH.add(populationChannel.get(index.get(i)));
//		}
//		population = tmppop;
//		populationChannel = tmpCH;
//		System.out.println();
//		System.out.println("***Sorted population: "+population);
		if (showdetaillog) {
			System.out.println("----after sorted----");
			showPop(population,populationChannel);
			showfitness(fitnessList);
		}
//		System.out.println("***Sorted popChannel: "+populationChannel);
//		System.out.println("***Sorted fitness: "+fitnessList);
	}
	
	public void showPop(ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> populationChannel) {
		int inLine = 1;
		System.out.println("***Show population: ");
		for(int i = 0;i<population.size();i++) {
			if(inLine>=3) {
				inLine = 1;
				System.out.println();
			}
			System.out.print("\t{"+i +"} \t[");
			for (int j = 0; j < population.get(i).size(); j++) {
				System.out.print(population.get(i).get(j)+"_"+populationChannel.get(i).get(j)+",");	
			}
			System.out.print("]\t\t");
			inLine++;
		}
		System.out.println();
	}
	
	public Float showBestFitness(ArrayList<Float> fitnessList, ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> populationChannel) {
		System.out.println("***************Show Best Fitness***************");
		Float best = (float) -99999;
		int index = 0;
		for(int i = 0;i<fitnessList.size();i++) {
			if(fitnessList.get(i)>best) {
				best=fitnessList.get(i);
				index = i;
			}
		}
		//System.out.println();
		System.out.println("Best: gene " + index + " -> "+fitnessList.get(index));
		for(int i=0;i<Drawingpanel.APs.size();i++) {
			int pt = population.get(index).get(i);
			Drawingpanel.APs.get(i).setPT(pt);
			Drawingpanel.APs.get(i).setChannel(populationChannel.get(index).get(i));
		}
		//System.out.println(population.get(index));
		//System.out.println(populationChannel.get(index));
		//emergencyRefresh();
		return best;
	}

	public void initRandom(ArrayList<AP> _APs, ArrayList<ArrayList<Integer>> population, int _powMax, ArrayList<ArrayList<Integer>> populationChannel, ArrayList<Integer> populationCalCheck, boolean showdetaillog) {	
		Random rand = new Random();
		int n;
		int cha;
		if (showdetaillog) {
			System.out.print("pt_channel[");			
		}
		for(int i = 0; i<Drawingpanel.APs.size();i++){//init random gene
			n = rand.nextInt(_powMax);
			cha = rand.nextInt(3)*5+1;
			Drawingpanel.APs.get(i).setPT(n);
			Drawingpanel.APs.get(i).setChannel(cha);
			if (showdetaillog) {
				System.out.print(n+"_"+cha+", ");
				System.out.println();
			}
		}
		ArrayList<Integer> pt = ptAPs(Drawingpanel.APs);
		ArrayList<Integer> ch = chAPs(Drawingpanel.APs);
		population.add(pt);
		populationChannel.add(ch);
		populationCalCheck.add(0);
		
//		emergencyRefresh();
	}
	
	public ArrayList<Integer> ptAPs(ArrayList<AP> _APs ) {// make gene form current value of pt from AP
		ArrayList<Integer> ptList = new ArrayList<Integer>();
		for(int i = 0; i<_APs.size();i++){
			ptList.add(_APs.get(i).pt);
//			System.out.println("ptAP" + i +"=" + _APs.get(i).pt + "\t ");
		}
		return ptList;
	}
	
	public ArrayList<Integer> chAPs(ArrayList<AP> _APs ) {
		ArrayList<Integer> chList = new ArrayList<Integer>();
		for(int i = 0; i<_APs.size();i++){
			chList.add(_APs.get(i).channel);
//			System.out.println("ptAP" + i +"=" + _APs.get(i).pt + "\t ");
		}
		return chList;
	}
	
	public void emergencyRefresh() {
		//refresh
		System.out.println("*************** Refresh ***************");
		int count = Drawingpanel.APs.size()+1;
		String[] numAPs = new String[count];
		numAPs[0]="all";
		for(int h = 1; h< numAPs.length;h++){	 
					numAPs[h] = String.valueOf(h);
		}
		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) conPanel.numAP.getModel();
		model.removeAllElements();
		for(String temp : numAPs){
			 model.addElement(temp);
		}
		conPanel.numAP.setModel(model);
		
		String[] numChannel = new String[4];
		numChannel[0]="all";
		numChannel[1]="1";
		numChannel[2]="6";
		numChannel[3]="11";
		DefaultComboBoxModel<String> modelCh = (DefaultComboBoxModel<String>) conPanel.numChannel.getModel();
		modelCh.removeAllElements();
		for(String temp : numChannel){
			 modelCh.addElement(temp);
		}
		
		conPanel.numChannel.setModel(modelCh);
		
		Drawingpanel.reCal();
		Drawingpanel.repaint();
		//refresh
	}
	
	public float geneFitness(ArrayList<Integer> populationCalCheck, int i2, boolean shodetaillog) {// Power level
		if (shodetaillog) {
			System.out.println("find geneFitness");
		}
//		emergencyRefresh();
//		Drawingpanel.reCal();
		Drawingpanel.reCalSpot();
		
		float fs = 0;
		float fp = 0;
	    float total = 0;
	    int lastChannel=0;
	    float tmpVal = -200;
	    float penalty = 0;
	    int mostVal_Index = -1;
	    if (shodetaillog) {
		    System.out.print("gene(power_channel) ");
		    for (int i = 0; i < Drawingpanel.APs.size(); i++) {
		    	System.out.print(Drawingpanel.APs.get(i).pt + "_" + Drawingpanel.APs.get(i).channel+",");
		    }
		    System.out.println();
		}
	    
		for(int i = 0;i<Drawingpanel.Detecs.size();i++) {
			SpotSignal currentSignal = pointSignals(Drawingpanel.Detecs.get(i).getPos());
			mostVal_Index = mostValIndex(currentSignal);
//			tmpVal = getVal(Drawingpanel.Detecs.get(i).getPos(),lastChannel);
			tmpVal = currentSignal.vals.get(mostVal_Index);
			fs += tmpVal;
			float genePenalty = 0;
			if (shodetaillog) {
				System.out.println("\nDetect: "+i+"______________");
				System.out.println("MostValIndex (AP_dB): " + mostVal_Index +"_"+currentSignal.vals.get(mostVal_Index));
				System.out.println("penalty channel "+currentSignal.channels.get(mostVal_Index)+"(AP_dBs_penaltyVal)");
			}
			for (int j = 0; j < currentSignal.channels.size(); j++) {
//				System.out.println("current channel is "+ currentSignal.channels.get(mostValIndex(currentSignal)) + "this vals Channel is " + currentSignal.channels.get(j));
				if(currentSignal.channels.get(j)==currentSignal.channels.get(mostValIndex(currentSignal))) {			
					if(currentSignal.vals.get(mostVal_Index)*2<currentSignal.vals.get(j)) {
						genePenalty=tmpVal+Math.abs(tmpVal-currentSignal.vals.get(j));
						if (shodetaillog) {
							System.out.print("("+j+"_"+currentSignal.vals.get(j)+"_"+genePenalty+")");
						}
						penalty += genePenalty; 
					}
				}
			}
			fp += tmpVal-penalty;
			populationCalCheck.set(i2, 1);
//			System.out.println("val:"+getVal(Drawingpanel.Detecs.get(i).getPos())+" comulative fr:"+fr);	
			if (shodetaillog) {
				System.out.println();
				System.out.println("current total penalty = "+ fp);

			}
		}
//		fs = -30*Drawingpanel.overthreadhold;
		total = fs-fp;
		if (shodetaillog) {
			System.out.println("\nFitness = fs - fp = "+fs+" - "+fp +" = "+ total+"\n");
		}
		return total;
	}
	
	public float getVal(Point _loc,int _lastChannel) {//get the most val
		float val = 0;
//		Point test = new Point(100,100);
		Point cen1 = new Point(_loc.x-(_loc.x%30),_loc.y-(_loc.y%30));
//		Point cen2 = new Point(_loc.x%30,_loc.y+1%30);
//		Point cen3 = new Point(_loc.x+1%30,_loc.y%30);
//		Point cen4 = new Point(_loc.x+1%30,_loc.y+1%30);
//		Point[] p =  {cen1,cen2,cen3,cen4};
//		for(int j=0;j<4;j++) {
			for(int i=0;i<Drawingpanel.Spots.size();i++){
				if(Drawingpanel.Spots.get(i).getPos().equals(cen1)){// Point p
					val += Drawingpanel.Spots.get(i).value;
					_lastChannel = Drawingpanel.Spots.get(i).channel;
//					System.out.println("spot channel "+ _lastChannel);
//					System.out.println("spots values "+Drawingpanel.Spots.get(i).values);
					break;
				}
			
			}
			if(val == 0) {
				return -120;
			}
//		}
		return  val;
	}
	
	public int mostValIndex(SpotSignal _signals) {
		int index=-1;
		float val = -200;
		for (int i = 0; i < _signals.vals.size(); i++) {
			if(_signals.vals.get(i)>val) {
				val= _signals.vals.get(i);
				index = i;
			}
		}
		return index;
	}
	
	public SpotSignal pointSignals(Point _loc) {
		ArrayList<Float> pointVals = new ArrayList<Float>();
		ArrayList<Integer> pointChannels = new ArrayList<Integer>();	
		for(int i = 0;i<Drawingpanel.APs.size();i++) {
//			System.out.println("draw AP "+i);
			float tempDistP =(float) Point.distance(_loc.getX(), _loc.getY(),Drawingpanel.APshow.get(i).posx ,Drawingpanel.APshow.get(i).posy);//distance from ij to AP in pixel
   		  	if(tempDistP<=Drawingpanel.gridDistP){//if it's less than 70m( in pixel unit) gridDistP is defined in Testrun1
   		  		float dist = (tempDistP*Drawingpanel.gridDist)/Drawingpanel.gw;
   		  		float tempVal = Drawingpanel.spl(dist,Drawingpanel.APshow.get(i).curK,Drawingpanel.APshow.get(i).pt);//calculate spl
   		  		int tempChannel = Drawingpanel.APs.get(i).channel;
   		  		ArrayList<Float> PAFS = new ArrayList<Float>();
   		  		PAFS = Drawingpanel.ipm(Drawingpanel.APshow.get(i).posx,Drawingpanel.APshow.get(i).posy,_loc.x,_loc.y);//find how many walls in the line of sign
   		  		for(int m=0; m<PAFS.size();m++){
				  tempVal= tempVal + PAFS.get(m);// Path loss + obstacles 
   		  		}
   		  		pointVals.add(tempVal);
   		  		pointChannels.add(tempChannel);
//   		  		System.out.println("AP:"+i+" val:"+tempVal+" channel:"+tempChannel);
   		  	}
   		 
		}
//		System.out.println(pointVals + "   " + pointChannels);
		SpotSignal signals = new  SpotSignal(pointVals,pointChannels);		
		return signals; 
	}
	
//	public void testCo_Channel() {
//		System.out.println("///////////// Test Co-Channel Interferences /////////////");
//		write_txt("CoverageTest.log", "Test Coverage");
//		int popSize = Integer.valueOf(conPanel.popSize.getText());
//		int Avg = 0;
//		int inc = 50;
//		String content = null;
//		addTestSpot(15,60);
//		for (int i = 0; i < 10 ; i++) {//per change popsize
//			Drawingpanel.TestAreaVal.clear();
//			for (int k = 0; k < 5; k++) {//add val to find Avg
//				geneticAlgorithm(false);
////				System.out.println(Drawingpanel.TestAreaSpot);
////				System.out.println("TestSpot Size " + Drawingpanel.TestAreaSpot.size());
//				Drawingpanel.reCalTestCovorage(-60);
////				Drawingpanel.repaint();
//			}
//			System.out.println("TestCoverage Area Val: "+ Drawingpanel.TestAreaVal);
//			Avg = 0;
//			for (int j = 0; j < Drawingpanel.TestAreaVal.size(); j++) {//Find Avg
////				write_txt("CoverageTest.log", String.valueOf(Drawingpanel.TestAreaVal.get(j)));
//				write_txt("CoverageTest"+popSize+".log", String.valueOf(popSize)+"            "+String.valueOf(Drawingpanel.TestAreaVal.get(j)));
//				Avg += Drawingpanel.TestAreaVal.get(j);
//			}
//			Avg= Avg/Drawingpanel.TestAreaVal.size();
//			write_txt("CoverageTest.log",String.valueOf(popSize)+"			 	"+Avg);
//			popSize += inc;
//			inc += 50;
//			conPanel.popSize.setText(String.valueOf(popSize));
//		}
//		tested = !tested;
//		}
	
	public void testCoverage() {//test all
		System.out.println("///////////////////// Test  /////////////////////");
		write_txt("TestGA.log", "popSize\t\tCoverage\t\tPenalty");
		int popSize = Integer.valueOf(conPanel.popSize.getText());
		float mutaterate = Float.valueOf(conPanel.mutationRate.getText());
		int Avg = 0;
		int AvgCo = 0;
		int inc = 50;
		int change= 8;
		int example_set = 30;
		addTestSpot(15,60);
		for (int i = 0; i < change ; i++) {//per change popsize
			Drawingpanel.TestAreaVal.clear();
			Drawingpanel.TestAreaCoVal.clear();
			
			for (int k = 0; k < example_set; k++) {//add val to find Avg
//				System.out.println("popsize "+popSize+" , example "+k);
				System.out.println("mutate rate "+mutaterate+" , example "+k);
				
				geneticAlgorithm(false);
//				System.out.println(Drawingpanel.TestAreaSpot);
//				System.out.println("TestSpot Size " + Drawingpanel.TestAreaSpot.size());
				Drawingpanel.reCalTestCovorage(-60);
//				Drawingpanel.repaint();
			}
			System.out.println("TestCoverage Area Val: "+ Drawingpanel.TestAreaVal);
			Avg = 0;
			for (int j = 0; j < Drawingpanel.TestAreaVal.size(); j++) {//Find Avg
//				write_txt("CoverageTest.log", String.valueOf(Drawingpanel.TestAreaVal.get(j)));
				write_txt("TestGA.log"+popSize+".log", String.valueOf(popSize)+"\t\t"+String.valueOf(Drawingpanel.TestAreaVal.get(j)+"\t\t"+String.valueOf(Drawingpanel.TestAreaCoVal.get(j))));
				Avg += Drawingpanel.TestAreaVal.get(j);
				AvgCo += Drawingpanel.TestAreaCoVal.get(j);
			}
			Avg= Avg/Drawingpanel.TestAreaVal.size();
			AvgCo= AvgCo/Drawingpanel.TestAreaCoVal.size();
			write_txt("TestGA.log",String.valueOf(popSize)+"			 	"+Avg+"			 	"+AvgCo);
			popSize += inc;
			mutaterate += 0.005;
			//inc += 50;
//			conPanel.popSize.setText(String.valueOf(popSize));
			conPanel.mutationRate.setText(String.valueOf(mutaterate));
		
		}
		tested = !tested;
	}
	
	
	public void write_txt(String path, String content) {
			
		try (FileWriter writer = new FileWriter(path, true);
	             BufferedWriter bw = new BufferedWriter(writer)) {

	            bw.write(content);
	            bw.newLine();
	            bw.close();
	        } catch (IOException e) {
	            System.err.format("IOException: %s%n", e);
	        }
	}
	
	public void addTestSpot(int min, int max) {
		Drawingpanel.TestAreaSpot.clear();
		int subareaSize= max;
		
//		if (!tested) {
//			subareaSize= max;
//		}else {
//			subareaSize= min;
//		}
			
		Drawingpanel.TestAreaSpot.clear();
		for (int i = 0; i < Drawingpanel.TestAreas.size()/4; i++) {//per area
			System.out.println("Area "+i);
			int minX = Min(Min(Drawingpanel.TestAreas.get(i*4).posx,Drawingpanel.TestAreas.get(i*4+1).posx),Min(Drawingpanel.TestAreas.get(i*4+2).posx,Drawingpanel.TestAreas.get(i*4+3).posx));
			int maxX = Max(Max(Drawingpanel.TestAreas.get(i*4).posx,Drawingpanel.TestAreas.get(i*4+1).posx),Max(Drawingpanel.TestAreas.get(i*4+2).posx,Drawingpanel.TestAreas.get(i*4+3).posx));
			int minY = Min(Min(Drawingpanel.TestAreas.get(i*4).posy,Drawingpanel.TestAreas.get(i*4+1).posy),Min(Drawingpanel.TestAreas.get(i*4+2).posy,Drawingpanel.TestAreas.get(i*4+3).posy));
			int maxY = Max(Max(Drawingpanel.TestAreas.get(i*4).posy,Drawingpanel.TestAreas.get(i*4+1).posy),Max(Drawingpanel.TestAreas.get(i*4+2).posy,Drawingpanel.TestAreas.get(i*4+3).posy));
			System.out.println("minX maxX : minY maxY \t"+minX+" "+maxX+" : "+minY+" "+maxY);
			for (int j = 0; j < (maxX - minX)/subareaSize; j++) {//X sub area
				for (int j2 = 0; j2 < (maxY-minY)/subareaSize; j2++) {//Y sub area
//					System.out.println("add spot"+(((minX/30)*30+(j*subareaSize))+", "+((minY/30)*30+(j2*subareaSize))));
//					Drawingpanel.TestAreaSpot.add(new Point((minX/30)*30+(j*subareaSize),(minY/30)*30+(j2*subareaSize)));
					Drawingpanel.TestAreaSpot.add(new Point(minX+(j*subareaSize), minY+(j2*subareaSize)));
				}
			}
		}
	}
	
		
	private int Min(int posx, int posx2) {
		if (posx < posx2) {
			return posx;
		}else {
			return posx2;
		}
	}
	private int Max(int posx, int posx2) {
		if (posx > posx2) {
			return posx;
		}else {
			return posx2;
		}
	}
	
}
