import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;


public class seededGrowth {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Plane f1 = new Plane(600, 600, 3600, 3, 5, Color.BLACK, Color.RED);
		OperatingPlane container = new OperatingPlane(f1.getWidth(), f1.getHeight(), f1);
		
		JFrame Window = new JFrame("Seeded Growth");
		Window.setContentPane(container);
		/*for (point p: container.ALpoints) {
			System.out.println(p.x + ", " + p.y + " (" + p.isFixed + ")");
		}
		System.out.println();*/
		int ysize = f1.getHeight() + 22 + 21;
		Window.setSize(new Dimension(f1.getWidth(), ysize));
		Window.setLocation(100, 100);
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Window.setVisible(true);
		
		/*System.out.println("Cycles done: " + cycles);
		for (point p: container.ALpoints) {
			System.out.println(p.x + ", " + p.y + " (" + p.isFixed + ")");
		}*/
	}

}
