import processing.core.PApplet;


public class StateLatLon {
	
	String state;
	
	double lat, lon;
	
	PApplet parent;
	
	StateLatLon(PApplet p, String state, double lat, double lon) {
		parent = p;
		
		this.state = state;
		this.lat = lat;
		this.lon = lon;
	}

	public String getState() {
		return state;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	

}
