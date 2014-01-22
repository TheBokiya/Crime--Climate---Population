import processing.core.PApplet;

public class Climate {
	
	String state;
	float tempC;
	
	PApplet parent;
	
	//Constructor
	Climate (PApplet p, String state, float tempC) {
		parent = p;
		
		this.state = state;
		this.tempC = tempC;
	}

	
	//Setters and Getters
	public String getState() {
		return state;
	}

	public float getTempC() {
		return tempC;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setTempC(float tempC) {
		this.tempC = tempC;
	}
	
}
