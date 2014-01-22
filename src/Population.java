import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Population {
	
	String state;
	int p0, p1, p2, p3, p4, p5, average;
	
	PApplet parent;
	
	//Constructor
	Population (PApplet p, String state, int p0, int p1, int p2, int p3, int p4, int p5) {
		parent = p;
		
		this.state = state;
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		this.p5 = p5;

	}
	
//	public void drawNode(){
//		
//		parent.fill(6, 144, 183);
//		parent.noStroke();
//		parent.rect(parent.width/2, parent.height/2, 100, 50);
//		parent.fill(255);
//		parent.textFont(font, 14);
//		parent.textAlign(PConstants.CENTER);
//		parent.text(crimeType, parent.width/2, parent.height/2);
//		
//		
//	}
	
	public int averagePopulation() {
		int average;
		average = (p0 + p1 + p2 + p3 + p4 + p5) / 5;
		return average;
	}

	//Getters and Setters
	
	public int getAverage() {
		return average;
	}

	public void setAverage(int average) {
		this.average = average;
	}
	public String getState() {
		return state;
	}
	public int getP0() {
		return p0;
	}
	public int getP1() {
		return p1;
	}
	public int getP2() {
		return p2;
	}
	public int getP3() {
		return p3;
	}
	public int getP4() {
		return p4;
	}
	public int getP5() {
		return p5;
	}
}