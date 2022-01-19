package sim;

import java.awt.Color;
import java.util.LinkedList;

import acm.graphics.GOval;
import acm.program.GraphicsProgram;

public class Planet extends Thread {
	
	// Static constants
	private static final double density = 5e22;
	private static final int scale = 5000000;
	private static final int deltaTime = 2;
	private static final int physicsTime = 400;
	private static final double G = 6.67430e-11;
	
	// Instance Variables
	private GraphicsProgram GProgram;
	private String name;
	private double mass;
	private double x, y;
	private double vx, vy;
	
	public boolean debug = false;
	
	private int width, height;
	private double radius;
	private GOval shape;
	private LinkedList<Planet> others = new LinkedList<Planet>();
	
	/**
	 * Constructor Method
	 * 
	 * @param GProgram - GraphicsProgram to draw to
	 * @param name - name of the planet
	 * @param mass - mass of the planet
	 * @param color - color of the planet
	 * @param x - initial x pos
	 * @param y - initial y pos
	 * @param vx - initial x vel
	 * @param vy - initial y vel
	 */
	public Planet(GraphicsProgram GProgram, String name, double mass, Color color, double x, double y, double vx, double vy) {
		// Populate instance variables
		this.GProgram = GProgram;
		this.name = name;
		this.mass = mass;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		width = GProgram.getWidth();
		height = GProgram.getHeight();
		
		// Create shape
		radius = mass / density;
		shape = new GOval(radius, radius);
		shape.setFillColor(color);
		shape.setFilled(true);
		GProgram.add(shape);
		draw();
	}
	
	/**
	 * Adder Method: another planet
	 * 
	 * @param p - other planet to be added
	 */
	public void addOther(Planet p) {
		others.add(p);
	}
	
	/**
	 * Draw Method: draws the planet
	 */
	public void draw() {
		int X = (int) (width / 2 + x / scale - radius / 2);
		int Y = (int) (height / 2 - y / scale - radius / 2);
		shape.setLocation(X, Y);
	}
	
	/**
	 * Getter Method: x pos
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter Method: y pos
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Getter Method: mass
	 */
	public double getMass() {
		return mass;
	}
	
	/**
	 * Getter Method: distance between this and another planet
	 * 
	 * @param p - the other planet
	 */
	private double getDist(Planet p) {
		return Math.sqrt(Math.pow(getX() - p.getX(), 2) + Math.pow(getY() - p.getY(), 2));
	}
	
	/**
	 * Getter Method: theta between this and another planet
	 * 
	 * @param p - the other planet
	 * 
	 * @return theta - (rad) in (-pi, pi]
	 */
	public double getTheta(Planet p) {
		return Math.atan2(p.getY() - getY(), p.getX() - getX());
	}
	
	/**
	 * Debug Method: override toString()
	 * 
	 * @return name, x, y, vx, vy
	 */
	@Override
	public String toString() {
		return String.format("Name: %s\tX: %f\tY: %f\tVx: %f\tVy: %f", name, x, y, vx, vy);
	}
	
	/**
	 * Physics Loop: gravity simulation
	 */
	public void run() {
		
		// Physics variables
		double r, theta;
		double Fg;
		double FnetX = 0., FnetY = 0.;
		double ax, ay;
		
		while (true) {
			
			// Calculate Fnet (loop through Fg of <others>)
			for (Planet p : others) {
				r = getDist(p);
				theta = getTheta(p);
				Fg = G * getMass() * p.getMass() / Math.pow(r, 2);
				FnetX = Fg * Math.cos(theta);
				FnetY = Fg * Math.sin(theta);
			}
		
			// Calculate accel
			ax = FnetX / mass;
			ay = FnetY / mass;
			
			// Calculate vel
			vx += ax * physicsTime;
			vy += ay * physicsTime;
		
			// Calculate pos
			x += vx * physicsTime;
			y += vy * physicsTime;
		
			// Redraw
			draw();
			if (debug) System.out.println(toString());
			
			// Sleep
			GProgram.pause(deltaTime);
		
		}
	}
	
}
