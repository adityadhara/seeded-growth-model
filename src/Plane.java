import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import datastructures.ParticleType;
import datastructures.Vector;

public class Plane {
	// basic parameters
	private final int WIDTH;
	private final int HEIGHT;
	private final int POPULATION;
	private final int PARTICLE_SIZE;
	private final int MAX_TEMP; //"temperature" is the randomness component of molecules
	private final Color COLOR_DEFAULT;
	private final Color COLOR_FIXED;
	private final Random GENERATOR;
	private final int ROSTER_SIZE = 15;

	// main data
	private int cycle;
	private Particle[] points;
	private Particle[][][] roster;
	
	// initiation functions
	public Plane(int setWidth, int setHeight, int setPopulation,
			int setParticleSize, int setMaxTemp, Color setColorDefault,
			Color setColorFixed) {
		WIDTH = setWidth;
		HEIGHT = setHeight;
		POPULATION = setPopulation;
		PARTICLE_SIZE = setParticleSize;
		MAX_TEMP = setMaxTemp;
		COLOR_DEFAULT = setColorDefault;
		COLOR_FIXED = setColorFixed;
		
		GENERATOR = new Random();

		cycle = 0;
		points = new Particle[POPULATION];
		roster = new Particle[ROSTER_SIZE][ROSTER_SIZE][];
		
		initialize();
	}

	public void initialize() {
		Particle.resetNumFixed();
		cycle = 0;
		
		int fixedSeed = GENERATOR.nextInt(POPULATION);
		for (int i = 0; i < POPULATION; ++i) {
			Particle p = new Particle(
					GENERATOR.nextInt(WIDTH), GENERATOR.nextInt(HEIGHT),
					GENERATOR.nextDouble() * 2 - 1, GENERATOR.nextDouble() * 2 - 1,
					PARTICLE_SIZE, ParticleType.WATER);
			p.getVel().normalize();
			
			points[i] = p;
		}
		points[fixedSeed].setIsFixed(true);
	}
	
	//getters and setters
	public int getWidth() { return WIDTH; } //end accessor getWidth
	public int getHeight() { return HEIGHT; } //end accessor getHeight
	public int getPopulation() { return POPULATION; } //end accessor getPopulation
	public int getParticleSize() { return PARTICLE_SIZE; } //end accessor getParticleSize
	public Color getColorDefault() { return COLOR_DEFAULT; } //end accessor getColorDefault
	public Color getColorFixed() { return COLOR_FIXED; } //end accessor getColorFixed
	public int getCycle() { return cycle; } //end accessor getCycle
	

	// motion related functions
	public void MakeMotion() {
		if (isDone()) return; // kill if done
		setTotalMoves();
		cycle++;
	}

	private void setTotalMoves() {
		
		for (int iii = 0; iii<points.length; iii++) {
			//randomize movement due to molecular vibrations
			double molXVel = 2 * GENERATOR.nextDouble() - 1;
			double molYVel = 2 * GENERATOR.nextDouble() - 1;
			points[iii].setMolVel(molXVel, molYVel);
			points[iii].getMolVel().normalize();
			
		}

		for (int numMoves = 0; numMoves < MAX_TEMP; numMoves++) {

			//fix any particles outside of the border
			for (int iii=0; iii<points.length; iii++) {
				if (points[iii].getPos().getX() < 0) points[iii].setPos(0, points[iii].getPos().getY());
				if (points[iii].getPos().getX() > WIDTH) points[iii].setPos(WIDTH, points[iii].getPos().getY());
				if (points[iii].getPos().getY() < 0) points[iii].setPos(points[iii].getPos().getX(), 0);
				if (points[iii].getPos().getY() > HEIGHT) points[iii].setPos(points[iii].getPos().getX(), HEIGHT);
			}
			
			rosterize();
			
			//assess each point in roster by row and column
			for (int iii = 0; iii < ROSTER_SIZE+1; iii++) {
				for (int jjj = 0; jjj < ROSTER_SIZE+1; jjj++) {
					for (Particle a: roster[iii][jjj]) {
						//no movement for fixed a's
						if (a.getIsFixed()) continue;
						
						//determine 
						double xMove = (a.getVel().getX() + a.getMolVel().getX())/2;
						double yMove = (a.getVel().getY() + a.getMolVel().getY())/2;
						
						//edge particles
						if ((iii==0) || (iii >= ROSTER_SIZE-1) || (jjj == 0) || (jjj >= ROSTER_SIZE-1)) {
							if (a.getPos().getX() + xMove < 0) {
								xMove = -1*(a.getPos().getX() + xMove);
								a.setVel(-1 * a.getVel().getX(), a.getVel().getY());
								a.setMolVel(-1 * a.getMolVel().getX(), a.getMolVel().getY());
							} //end if
							if (a.getPos().getX() + xMove > WIDTH) {
								xMove = -1*(a.getPos().getX() + xMove - WIDTH);
								a.setVel(-1 * a.getVel().getX(), a.getVel().getY());
								a.setMolVel(-1 * a.getMolVel().getX(), a.getMolVel().getY());
							} //end if
							if (a.getPos().getY() + yMove < 0) {
								yMove = -1*(a.getPos().getY() + yMove);
								a.setVel(a.getVel().getX(), -1 * a.getVel().getY());
								a.setMolVel(a.getMolVel().getX(), -1 * a.getMolVel().getY());
							} //end if
							if (a.getPos().getY() + yMove > HEIGHT) {
								yMove = -1*(a.getPos().getY() + yMove - HEIGHT);
								a.setVel(a.getVel().getX(), -1 * a.getVel().getY());
								a.setMolVel(a.getMolVel().getX(), -1 * a.getMolVel().getY());
							} //end if
						} //end if; edge particles comparisons
						
						//to make particles become crystallized/bounce
						//full one-to-one comparisons with all points from roster cells nearby
						for (int ii=Math.max(iii-2, 0); ii<Math.min(iii+2,ROSTER_SIZE+1); ii++) {
							for (int jj=Math.max(jjj-2, 0); jj<Math.min(jjj+2, ROSTER_SIZE+1); jj++) {
								for (Particle b : roster[ii][jj]) {
									if(a.getIsFixed()) continue; //in case in prev loop, a got fixed
									
									double bxMove = (b.getVel().getX() + b.getMolVel().getX())/2;
									double byMove = (b.getVel().getY() + b.getMolVel().getY())/2;
									
									if (b.getIsFixed()) {
										//crystallization
										if((int)Math.sqrt(Math.pow(a.getPos().getX()+xMove-b.getPos().getX(), 2)+Math.pow(a.getPos().getY()+yMove-b.getPos().getY(), 2)) < 2*PARTICLE_SIZE) {
											a.setIsFixed(true);
											continue;
										} // end if; compare and set if fixed
									} else {
										//bounce 
										if((int)Math.sqrt(Math.pow(a.getPos().getX()+xMove-b.getPos().getX()-bxMove, 2)+Math.pow(a.getPos().getY()+yMove-b.getPos().getY()-byMove, 2)) < 2*PARTICLE_SIZE) {
											//trajectory is sum of velocities;
											xMove += bxMove;
											yMove += byMove;
											
											//switch velocities and molecular vibrations
											Vector v = a.getVel();
											Vector temp = a.getMolVel();
											a.setVel(b.getVel());
											a.setMolVel(b.getMolVel());
											b.setVel(v);
											b.setMolVel(temp);
										} // end if; compare and bounce particles
									} //end if
								} // end for; comparing a to each particle b in roaster[ii][jj]
							} // end for jj
						} //end for ii
						
						//Now that xMove and yMove are moved,
						a.moveBy(xMove, yMove);
					} //end for each particle in roster[iii][jjj]
				} //end for jjj
			} //end for iii
		} //end for total moves due to temperature
			
	} // end setTotalMoves
	
	public void rosterize() {
		double sizeXPart = WIDTH/ROSTER_SIZE;
		double sizeYPart = HEIGHT/ROSTER_SIZE;
		
		roster = new Particle[ROSTER_SIZE+1][ROSTER_SIZE+1][];
		for (int iii = 0; iii<=ROSTER_SIZE; ++iii) {
			for (int jjj = 0; jjj<=ROSTER_SIZE; ++jjj) {
				ArrayList<Particle> tempList = new ArrayList<Particle>();
				for (Particle p: points) {
					if (((int)(p.getPos().getX()/sizeXPart) == iii) && ((int)(p.getPos().getY()/sizeYPart) == jjj)) tempList.add(p);
				}
				Particle[] tempArray = new Particle[tempList.size()];
				tempArray = tempList.toArray(tempArray);
				roster[iii][jjj] = tempArray;
			} //end y-dim for loop
		} //end x-dim for loop
	} //end rosterize
	
	public boolean isDone() {
		return Particle.getNumFixed() == POPULATION;
	} //end isDone
	
	public Particle[] getPoints() { return points; } //end accessor getPoints

} // end class field
