

import datastructures.ParticleType;
import datastructures.Vector;

public class Particle {
	//position parameters
	private Vector pos;
	private Vector vel;
	private Vector molVel;
	
	//other properties
	private final int SIZE;
	private boolean isFixed = false;
	private final ParticleType TYPE;
	
	private static int numFixed = 0;
	
	public Particle(
			double setPX, double setPY,
			double setVX, double setVY,
			int setSize, ParticleType setType) {
		pos = new Vector(setPX, setPY);
		vel = new Vector(setVX, setVY);
		SIZE = setSize;
		TYPE = setType;
		
		//set defaults
		molVel = new Vector(0, 0);		
	} // end constructor Particle

	public Vector getPos() { return pos; } // end accessor getPos
	public void setPos(Vector v) { pos = v; } // end mutator setPos
	public void setPos(double x, double y) { pos.set(x, y); } // end mutator setPos

	public Vector getVel() { return vel; } // end accessor getVel
	public void setVel(Vector v) { vel = v; } // end mutator setVel
	public void setVel(double x, double y) { vel.set(x, y); } // end mutator setVel

	public Vector getMolVel() { return molVel; } // end accessor getMolVel
	public void setMolVel(Vector v) { molVel = v; } // end mutator setMolVel
	public void setMolVel(double x, double y) { molVel.set(x, y); } // end mutator setMolVel
	
	public int getSize() { return SIZE; } // end accessor getSize
	public ParticleType getType() { return TYPE; } // end accessor getType
	
	public boolean getIsFixed() { return isFixed; } // end accessor getIsFixed
	public void setIsFixed(boolean newIsFixed) {
		if (isFixed == newIsFixed) return; //to maintain numFixed
		isFixed = newIsFixed;
		if (newIsFixed) {
			++numFixed;
		} else {
			--numFixed;
		}
	} // end mutator setIsFixed
	
	public static int getNumFixed() { return numFixed; } //end class accessor getNumFixed
	public static void resetNumFixed() { numFixed = 0; } //end class mutator resetNumFixed
	
	public void moveBy(Vector v) {
		pos.add(v);
	} // end method moveBy
	
	public void moveBy(double dx, double dy) {
		pos.add(dx, dy);
	} // end method moveBy
	
	@Override
	public String toString() {
		return pos.toString();
	} // end method toString
	
	
} // end class Particle
