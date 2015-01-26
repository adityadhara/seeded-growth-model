package datastructures;
public class Vector {
	private double x;
	private double y;
	
	public Vector(double newX, double newY) {
		x = newX;
		y = newY;
	}
	
	public void set(double newX, double newY) {
		x = newX;
		y = newY;
	} // end method set

	public double getX() { return x; } // end accessor getX
	public double getY() { return y; } // end accessor getY
	
	public void add(Vector v) {
		x += v.getX();
		y += v.getY();
	} // end method add
	
	public void add(double dx, double dy) {
		x += dx;
		y += dy;
	} // end method add
	
	public void multiply(double k) {
		x *= k;
		y *= k;
	} // end method multiply
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public double magnitude() {
		return Math.sqrt(x*x + y*y);
	} // end method magnitude
	
	public double distance(Vector v) {
		return Math.sqrt(
				Math.pow(x - v.getX(), 2) +
				Math.pow(y - v.getY(), 2));
	} // end method distance
	
	public void normalize() {
		double mag = magnitude();
		x /= mag;
		y /= mag;
	}

} // end class Vector