import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

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


public class TestrunAJ {

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

     int currentMode = 0;
     
     int npx,npy;
	//drawingPanel vars
	//please delete it-----------
     int numArea;
     public ArrayList<Spot> temptest	 = new ArrayList<Spot>() ;
	 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TestrunAJ window = new TestrunAJ();
					
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
			
		
	}

	/**
	 * Create the application.
	 */
	public TestrunAJ() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setEnabled(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		int xsize = (int) tk.getScreenSize().getWidth();
		int ysize = (int) tk.getScreenSize().getHeight();
		//frame.setSize(xsize, ysize-40);
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
								System.out.println(parts[0]+","+parts[1]+","+parts[2]);
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
							 System.out.println(Drawingpanel.SampleSpots.get(j).value);
							 
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
				int result = fileChooser.showSaveDialog(null);
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
                     Drawingpanel.Spots = loaded.Spots_saved;
                     Drawingpanel.gridDist = loaded.gridDist;
                     Drawingpanel.gridDistP = loaded.gridDistP;
                     Drawingpanel.gw = loaded.gw_saved;
                     Drawingpanel.cGW = loaded.cGW_saved;
                     Drawingpanel.curY = loaded.currentY_saved;
                    // System.out.println( loaded.APshow_saved.get(0).getPos());
                    // Drawingpanel.APshow = loaded.APshow_saved;
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
				         savedObj saveOb = new savedObj(selectedFile,Drawingpanel.WList,Drawingpanel.APs,Drawingpanel.Spots,Drawingpanel.gridDist,Drawingpanel.gridDistP,Drawingpanel.gw,Drawingpanel.curY,Drawingpanel.cGW);
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
				   int result = file.showSaveDialog(null);
					
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
				 
				Drawingpanel.changeModeTo(4);
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
					System.out.println(index+"num");
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
			 }else if(Drawingpanel.currentMode ==  1){// if it's wall mode
				
				 if(Drawingpanel.Walldrawing){
					 Drawingpanel.temp.setWPAF(Drawingpanel.curPAF);//set current PAF to the wall
					 Drawingpanel.temp.setColor(Drawingpanel.selectedWall);
					 Drawingpanel.WList.add(Drawingpanel.temp);
					 Drawingpanel.reCal();
					// Drawingpanel.SampleRecal();
					 Drawingpanel.temp = new Line();
				 }else{
					
					 Drawingpanel.temp = new Line(new Point(npx,npy),new Point(npx,npy));
					 
					 				 
				 }
				 	 
				 Drawingpanel.Walldrawing = !Drawingpanel.Walldrawing;
				
				
					 
				 
				 
			 }else if(Drawingpanel.currentMode == 2){
				
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
				 
				 
				 
				 
			 }else if(Drawingpanel.currentMode ==4){//delete mode
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
}
