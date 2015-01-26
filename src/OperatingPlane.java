import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class OperatingPlane extends JPanel implements MouseListener, ActionListener {

	private static final int ANIMATION_SPEED = 50;
	
	//anim stuff
	private Timer timer;
	private int WIDTH;
	private int HEIGHT;
	private static final long serialVersionUID = 1L;
	
	//data
	private Plane field;
	
	public OperatingPlane(int setWidth, int setHeight, Plane setField) {
		WIDTH = setWidth;
		HEIGHT = setHeight;
		
		field = setField;
		
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		timer = new Timer(ANIMATION_SPEED, this);
	}
	
	//graphics stuff
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Particle p: field.getPoints()) {
			if (p.getIsFixed()) {
				g.setColor(field.getColorFixed());
			} else {
				g.setColor(field.getColorDefault());
			}
			g.drawOval((int)p.getPos().getX()-p.getSize(), (int)p.getPos().getY()-p.getSize(), 2 * p.getSize(), 2 * p.getSize());
		}
		
		g.setColor(Color.WHITE);
		g.fillRect(0, HEIGHT, WIDTH, 21);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, WIDTH-1, HEIGHT-1);
		
		g.setColor(field.getColorFixed());
		g.drawString("" + field.getCycle(), 10, 16 + HEIGHT);
			
		g.setColor(Color.BLACK);
		if (field.isDone()) g.drawString("Done!", 20+8*(int)Math.ceil(Math.log10(field.getCycle())), 16 + HEIGHT);
	}
	
	public void mousePressed(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }

    public void mouseClicked(MouseEvent evt) {
    	if(!field.isDone()) {
    		if(timer.isRunning()) {
    			timer.stop();
    		} else {
    			timer.start();
    		}
    	} else {
    		field.initialize();
    		repaint();
    	}
    }
    
    public void actionPerformed(ActionEvent evt) {
    	field.MakeMotion();
    	repaint();
    	if (field.isDone()) timer.stop();
    }
}
