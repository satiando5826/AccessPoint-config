import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
	static ImageView im1;
	static ImageView im2;
    public static void main(String[] args) throws MalformedURLException, IOException {
        JFrame frame = new JFrame();
        Box box = new Box(BoxLayout.Y_AXIS);
 
       JSlider Zoomslider = new JSlider(SwingConstants.HORIZONTAL,50,200,100);
		 Zoomslider.setMajorTickSpacing(25);
		 Zoomslider.setPaintTicks(true);
		 Zoomslider.setSnapToTicks(true);
		Zoomslider.addChangeListener(new ChangeListener() {
			 
	         @Override
			public void stateChanged(ChangeEvent e) {
	        	im1.setScale(2.*Zoomslider.getValue()/Zoomslider.getMaximum());
	        	im2.setScale(2.*Zoomslider.getValue()/Zoomslider.getMaximum());
	        	
	         }
	});
		box.add(Zoomslider);
        BufferedImage image = ImageIO.read(new URL("https://upload.wikimedia.org/wikipedia/commons/6/6f/HP_logo_630x630.png"));
        //AffineTransform xfrm1 = AffineTransform.getScaleInstance(1, 1);
        AffineTransform xfrm1 = new AffineTransform();
       // xfrm1.scale(0.5,0.5);
        im1 = new ImageView(image,xfrm1);
        box.add(im1);
      //  AffineTransform xfrm2 = AffineTransform.getShearInstance(0.0, 0.0);
        AffineTransform xfrm2 = new AffineTransform();
        //xfrm2.scale(1, 1);
        im2 = new ImageView(image,xfrm2);
        box.add(im2);
        JScrollPane scroll = new JScrollPane(box);
        frame.add(scroll);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

@SuppressWarnings("serial")
class ImageView extends JComponent {
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        try {
            paintXfrm = g2d.getTransform();
            paintXfrm.invert();
           // g2d.translate(getWidth() / 2, getHeight() / 2);
          //  g2d.transform(xfrm);
           // g2d.translate(image.getWidth() * -0.5, image.getHeight() * -0.5);
          //  paintXfrm.concatenate(g2d.getTransform());
            Dimension dim = getPreferredSize();
            g2d.drawImage(image, 0, 0,dim.width,dim.height, null);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {

 			int
            w = (int)(zoomS *  image.getWidth()),
            h = (int)(zoomS * image.getHeight());
 			return new Dimension(w, h);
	
}
    public void setScale(double scale){
    	zoomS = scale;
    	 revalidate();
         repaint();
    }
    public Point noZoomPos(Point inP){
    	
    	int x =  (int) (inP.x/zoomS);
		int y = (int) (inP.y/zoomS);
		return new Point(x,y);
    	
    }



    ImageView(final BufferedImage image, final AffineTransform xfrm) {
        this.canvas = image.createGraphics();
        canvas.setColor(Color.BLACK);
        canvas.setStroke(new BasicStroke(3.0f));
        this.image = image;
        this.xfrm = xfrm;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                  //  mouseDownCoord = e.getPoint();
                	mouseDownCoord = noZoomPos(e.getPoint());
                    paintXfrm.inverseTransform(mouseDownCoord, mouseDownCoord);
                } catch (NoninvertibleTransformException ex) {
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseDownCoord = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
               // Point p = e.getPoint();
            	Point p = noZoomPos(e.getPoint());
                try {
                    paintXfrm.inverseTransform(p, p);
                    if (mouseDownCoord != null) {
                        canvas.drawLine(mouseDownCoord.x, mouseDownCoord.y, p.x, p.y);
                        for (Component sibling: getParent().getComponents()) {
                            sibling.repaint();
                        }
                    }
                    mouseDownCoord = p;
                } catch (NoninvertibleTransformException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    private double zoomS=1;
    private Graphics2D canvas;
    private BufferedImage image;
    private AffineTransform xfrm;
    private AffineTransform paintXfrm;
    private Point mouseDownCoord;
    
}