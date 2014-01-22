import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Crime {
	
	String state, crimeType, crime;
	int year, count;
	
	PApplet parent;
	
	//Constructor
	Crime (PApplet p, String state, String crimeType, String crime, int year, int count) {
		parent = p;
		
		this.state = state;
		this.crimeType = crimeType;
		this.crime = crime;
		this.year = year;
		this.count = count;
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

	
	//Setters and Getters
	public String getState() {
		return state;
	}

	public String getCrimeType() {
		return crimeType;
	}

	public String getCrime() {
		return crime;
	}

	public int getYear() {
		return year;
	}

	public int getCount() {
		return count;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setCrimeType(String crimeType) {
		this.crimeType = crimeType;
	}

	public void setCrime(String crime) {
		this.crime = crime;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
