package sim;

import java.awt.Color;
import java.util.LinkedList;

import acm.program.GraphicsProgram;

public class Simulation extends GraphicsProgram {
	
	// Static Variables
	private static LinkedList<Planet> planets = new LinkedList<Planet>();
	
	/**
	 * Adder Method: new planet
	 */
	private void addPlanet(String name, double mass, Color color, double x, double y, double vx, double vy) {
		// Create planet
		Planet newPlanet = new Planet(this, name, mass, color, x, y, vx, vy);
		// Populate new planet's <others>
		for (Planet p : planets) {
			newPlanet.addOther(p);
			// Also add new planet to old planet
			p.addOther(newPlanet);
		}
		// Add new planet to list
		planets.add(newPlanet);
	}
	
	/**
	 * Entry point
	 */
	public static void main(String[] args) {
		new Simulation().start();
	}

	/**
	 * Simulation start
	 */
	public void run() {
		
		// Initialize GraphicsProgram
		this.resize(1600, 900);
		
		// Initialize planets
		addPlanet("Earth", 6e24,  Color.GREEN, 0, 0, 0, 0);
		addPlanet("Moon", 7.35e22, Color.GRAY, 3.844e8, 0, 0, 1000);
		//addPlanet("Mars", 8e23, Color.RED, 1e9, 1e4, -300, 500);
		
		// Start simulation
		for (Planet p : planets) {
			p.start();
		}
		
	}
	
}
