import java.awt.Dimension;
import java.awt.Font;



import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class ControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton btnUpload;
	public JButton btnNewButton;
	public JTextField popSize;
	public JTextField powMax;
	public JTextField powMin;
	public JTextField roundMax;
	public JTextField mutationRate;
	public JTextField parentUseRate;
	public JTextField testType;
	public JTextField testCount;
	public JTextField roundDiff;
	public JTextField endCount;
	public JTextField testSample;
	public JTextArea detailPOP;
	public JTextField exVals1;
	public JTextField exVals2;
	public JTextField exVals3;
	public JTextField exVals4;
	public JTextField exVals5;
	public JTextField Dists1;
	public JTextField Dists2;
	public JTextField Dists3;
	public JTextField Dists4;
	public JTextField Dists5;
	public JTextField customPAF;
	public JLabel minus;
	
	public JButton btnDelete;
	public JButton btnExecute;
	public JButton btnSubmit;
	
	public JButton btnGeneticAlgo;
	public JButton btnCo_channel;
	public JButton btnCoverage;

	
	public JSlider Yslider;
	//final JSlider Kslider;
	//final JLabel showKLabel;
	public  JLabel showYLabel;
	public JLabel showGridsize;
	public JSlider gridSlider;
	
	 JComboBox<String> wallOption;
	String[] choices = { "Cement : -10dB", "Iron door : -15.4dB", "Thin Cement : -2.89dB", "Cement+WBoard : -16dB", "Wood board: -5.0 dB", "F : -18dB", "Customize" }; 
	
	  JComboBox<String> modeListCombo;
	String[] modeList = {"Select Mode","Wall Mode","AP Mode","Auto Mode","Detect Mode","Test Area Mode"}; //{0,1,2,3}
	
	  JComboBox<String> selectFreq;
	String[] freqs = {"2.4 GHz","5.0GHz"};
	
	JComboBox<String> numAP;
	JComboBox<String> numDetec;
	JComboBox<String> numChannel;
	
	

	
	public ControlPanel(){
		//setBounds(0, 0, 260, 571);
	
		setLayout(null);
		
		Icon delete = new ImageIcon("delete.png");
		btnDelete = new JButton(delete);
		btnDelete.setBounds(140, 50, 25, 25);
		
		add(btnDelete);
		
		btnExecute = new JButton("Execute");
		btnExecute.setBounds(104, 530, 80, 25);
		btnExecute.setVisible(false);
		add(btnExecute);
		
		btnUpload = new JButton("Upload");	
		btnUpload.setBounds(0, 0, 89, 23);
		add(btnUpload);
		
		btnNewButton = new JButton("Set Scale");
		
		btnNewButton.setBounds(0, 34, 89, 23);
		add(btnNewButton);
		
		wallOption = new JComboBox<String>(choices);
		wallOption.setBounds(115, 72, 112, 31);
		wallOption.setSelectedIndex(0);
		
		add(wallOption);
		
		modeListCombo = new JComboBox<String>(modeList);
		modeListCombo.setBounds(0, 72, 112, 31);
		modeListCombo.setSelectedIndex(0);
		add(modeListCombo);
		
		btnGeneticAlgo = new JButton("GeneticAlgo");
		btnGeneticAlgo.setBounds(0, 380, 120, 23);
		add(btnGeneticAlgo);
		
//		btnCo_channel = new JButton("Co_channel");
//		btnCo_channel.setBounds(0, 800, 120, 23);
//		add(btnCo_channel);
//		
		btnCoverage = new JButton("TEST");
		btnCoverage.setBounds(0, 600, 120, 23);
		add(btnCoverage);
		
		
		
		//-----------------------------------------------------------------------------gridSlider---------------
		
		gridSlider = new JSlider(SwingConstants.HORIZONTAL,5,25,20);
		gridSlider.setBounds(10, 240, 210, 67);
		gridSlider.setMajorTickSpacing(5);
		gridSlider.setMinorTickSpacing(1);
		gridSlider.setPaintTicks(true);
		gridSlider.setPaintLabels(true);
		gridSlider.setVisible(true);

	   	add(gridSlider);
	   	
		JLabel gridsizeLab = new JLabel("Grid size  = ");
		gridsizeLab.setBounds(85, 220, 70, 14);
		add(gridsizeLab);
		
		showGridsize = new JLabel(Integer.toString(gridSlider.getValue()));
		
		showGridsize.setBounds(168, 220, 46, 14);
		add(showGridsize);
	   	
	   	
		//------------------------------------------------------------------------------------------------
		Yslider = new JSlider(SwingConstants.HORIZONTAL,10,40,20);
		Yslider.setBounds(10, 155, 210, 67);
		Yslider.setMajorTickSpacing(5);
		Yslider.setMinorTickSpacing(1);
		Yslider.setPaintTicks(true);
		Yslider.setPaintLabels(true);
		
		Dictionary<Integer, JLabel> Ylabels = new Hashtable<>();
	      for (int i = 10; i <= 40; i += 5) {
	         String text = String.format("%2.1f", i / 10.0);
	         Ylabels.put(i, new JLabel(text));
	      }
        Yslider.setLabelTable(Ylabels);
	
		
	    Font font = new Font("Serif", Font.ITALIC, 15);
	    Yslider.setFont(font);
		add(Yslider);
		
		JLabel YtextLabel = new JLabel("Y = ");
		YtextLabel.setBounds(126, 140, 46, 14);
		add(YtextLabel);
		
		showYLabel = new JLabel("2.0");
		showYLabel.setBounds(168, 140, 46, 14);
		add(showYLabel);
		//-------------------------------------------------------JSlider-------------------------
		customPAF = new JTextField();
		customPAF.setBounds(100, 110, 50, 25);
		customPAF.setVisible(false);
		add(customPAF);
		
		minus = new JLabel("-");
		minus.setBounds(90, 110, 10, 25);
		minus.setVisible(false);
		add(minus);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(155,110,75,25);
		btnSubmit.setVisible(false);
		add(btnSubmit);
		
		
		selectFreq  = new JComboBox<String>(freqs);
		
		selectFreq.setBounds(104, 1, 89, 20);
		selectFreq.setSelectedIndex(0);
		add(selectFreq);
		
		numAP = new JComboBox<String>();
		numAP.setBounds(104, 35, 50, 20);
		
		add(numAP);
		
		numDetec = new JComboBox<String>();
		numDetec.setBounds(124, 35, 50, 20);
		add(numDetec);
		
		JLabel numchanalb = new JLabel("channel");
		numchanalb.setBounds(5,410, 89, 23);
		add(numchanalb);
		
		numChannel = new JComboBox<String>();
		numChannel.setBounds(100, 410, 89, 23);
		add(numChannel);
		
		JLabel lblSignalLevelExamples = new JLabel("Signal level examples");
		lblSignalLevelExamples.setBounds(97, 340, 123, 14);
		add(lblSignalLevelExamples);
		
		JLabel popsizelbl = new JLabel("Population size ");
		popsizelbl.setBounds(5, 440, 100, 23);
		add(popsizelbl);
		
		popSize = new JTextField();
		popSize.setBounds(100, 440, 89, 20);
		popSize.setText("50");
		add(popSize);
		
		JLabel powMaxlbl =  new JLabel("Power transmission     ");
		powMaxlbl.setBounds(5,460,100,23);
		add(powMaxlbl);
		
		powMax = new JTextField("1");
		powMax.setBounds(140, 460, 40, 20);
		add(powMax);
		
		powMin = new JTextField("-10");
		powMin.setBounds(100, 460, 40, 20);
		add(powMin);
		
		JLabel roundMaxlbl =  new JLabel("Round ");
		roundMaxlbl.setBounds(5,480,89,23);
		add(roundMaxlbl);
		
		roundMax = new JTextField("15");
		roundMax.setBounds(100, 480, 89, 20);
		add(roundMax);
		
		JLabel mutatationRatelbl =  new JLabel("Mutation rate ");
		mutatationRatelbl.setBounds(5,500,89,23);
		add(mutatationRatelbl);
		
		mutationRate = new JTextField("0.02");
		mutationRate.setBounds(100, 500, 89, 20);
		add(mutationRate);
		
		JLabel parentUseRatelbl =  new JLabel("Parent Use rate ");
		parentUseRatelbl.setBounds(5,520,100,23);
		add(parentUseRatelbl);
		
		parentUseRate = new JTextField("0.1");
		parentUseRate.setBounds(100, 520, 89, 20);
		add(parentUseRate);
		
		JLabel endCountlbl =  new JLabel("End count ");
//		endCountlbl.setBounds(5,540,100,23);
		add(endCountlbl);
		
		endCount = new JTextField("10");
//		endCount.setBounds(100, 540, 89, 20);
		add(endCount);
		
		JLabel testTypelb1 =  new JLabel("Select test type");
		testTypelb1.setBounds(5,620,250,23);
		add(testTypelb1);
		
		JLabel testTypelb2 =  new JLabel("0:pop 1:mutate 2:round 3:parentuse  ");
		testTypelb2.setBounds(5,640,250,23);
		add(testTypelb2);
		
		testType = new JTextField("0");
		testType.setBounds(5, 660, 89, 20);
		add(testType);
		
		JLabel testCountlb =  new JLabel("Case");
		testCountlb.setBounds(5,680,50,23);
		add(testCountlb);
		
		testCount = new JTextField("20");
		testCount.setBounds(44, 680, 50, 20);
		add(testCount);
		
		JLabel testSamplelb =  new JLabel("Sample");
		testSamplelb.setBounds(100,680,50,23);
		add(testSamplelb);
		
		testSample = new JTextField("50");
		testSample.setBounds(150, 680, 50, 20);
		add(testSample);
		
		JLabel roundDifflb =  new JLabel("Different");
		roundDifflb.setBounds(5,700,100,23);
		add(roundDifflb);
		
		roundDiff = new JTextField("5");
		roundDiff.setBounds(100, 700, 89, 20);
		add(roundDiff);
		
		JLabel detailPoplb =  new JLabel("Ap detail (AP_power(dBm)_channel)");
		detailPoplb.setBounds(5,740,240,23);
		add(detailPoplb);
		
		detailPOP = new JTextArea("");
		detailPOP.setBounds(0, 760, 240, 80);
		add(detailPOP);
		
				
				
		exVals1 = new JTextField();
		exVals1.setBounds(104, 365, 86, 20);
		exVals1.setVisible(false);
		add(exVals1);
		exVals1.setColumns(10);
		
		exVals2 = new JTextField();
		exVals2.setBounds(104, 402, 86, 20);
		exVals2.setVisible(false);
		add(exVals2);
		exVals2.setColumns(10);
		
		exVals3 = new JTextField();
		exVals3.setBounds(104, 433, 86, 20);
		exVals3.setVisible(false);
		add(exVals3);
		exVals3.setColumns(10);
		
		exVals4 = new JTextField();
		exVals4.setBounds(104, 464, 86, 20);
		exVals4.setVisible(false);
		add(exVals4);
		exVals4.setColumns(10);
		
		exVals5 = new JTextField();
		exVals5.setBounds(104, 495, 86, 20);
		exVals5.setVisible(false);
		add(exVals5);
		exVals5.setColumns(10);
		
		JLabel lblDistances = new JLabel("Distances(m)");
		lblDistances.setBounds(14, 340, 68, 14);
		add(lblDistances);
		
		Dists1 = new JTextField();
		Dists1.setBounds(24, 365, 46, 20);
		Dists1.setVisible(false);
		add(Dists1);
		Dists1.setColumns(10);
		
		Dists2 = new JTextField();
		Dists2.setBounds(24, 402, 46, 20);
		Dists2.setVisible(false);
		add(Dists2);
		Dists2.setColumns(10);
		
		Dists3 = new JTextField();
		Dists3.setBounds(24, 433, 46, 20);
		Dists3.setVisible(false);
		add(Dists3);
		Dists3.setColumns(10);
		
		Dists4 = new JTextField();
		Dists4.setBounds(24, 464, 46, 20);
		Dists4.setVisible(false);
		add(Dists4);
		Dists4.setColumns(10);
		
		Dists5 = new JTextField();
		Dists5.setBounds(24, 495, 46, 20);
		Dists5.setVisible(false);
		add(Dists5);
		Dists5.setColumns(10);
		
	
	}
	 @Override
	 public Dimension getPreferredSize() {
	        return  new Dimension(234,571) ;
		// return  new Dimension(400,571) ;
	    }

}
