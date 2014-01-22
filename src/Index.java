import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import controlP5.*;

import codeanticode.glgraphics.GLConstants;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.*;

public class Index extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ControlP5 cp5;

	PFont font;

	String climateData[], crimeData[], latLonData[], climateChunk[],
			crimeChunk[], latLonChunk[], popData[], popChunk[];

	ArrayList<Crime> crimeArray;
	Climate climateArray[];
	Population popArray[];
	StateLatLon latLonArray[];

	UnfoldingMap map;
	List<Marker> stateMarkers;
	List<Feature> states;

	Location locationArray[];
	SimplePointMarker simplePointArray[];
	ScreenPosition screenPositionArray[];

	CheckBox crimeTypeBox;

	ListBox crimeBox, yearBox;

	Slider tempSlider;

	int col1, col2, col3, col4, col5, col6, col7, col8, col9;

	int tempColor0, tempColor1, tempColor2, tempColor3, tempColor4, tempColor5,
			tempColor6, tempColor7;

	String selectedCrime = "Please select a crime";
	String selectedYear = "Please select a year";

	String pointedState;
	float temp = 0;
	int population = 0;

	int totalViolent, totalProperty, totalMurder, totalRape, totalRobbery,
			totalAssault, totalBurglary, totalTheft, totalVehicleTheft;

	int selectionNum = 10;

	int globalBaseValue;

	public void setup() {
		size(1300, 700, GLConstants.GLGRAPHICS);
		smooth();

		col1 = color(255, 247, 236);
		col2 = color(254, 232, 200);
		col3 = color(253, 212, 158);
		col4 = color(253, 212, 158);
		col5 = color(252, 141, 89);
		col6 = color(239, 101, 72);
		col7 = color(215, 48, 31);
		col8 = color(179, 0, 0);
		col9 = color(127, 0, 0);

		tempColor0 = color(247, 252, 253);
		tempColor1 = color(224, 236, 244);
		tempColor2 = color(191, 211, 230);
		tempColor3 = color(158, 188, 218);
		tempColor4 = color(140, 150, 198);
		tempColor5 = color(140, 107, 177);
		tempColor6 = color(136, 65, 157);
		tempColor7 = color(110, 1, 107);

		font = loadFont("Nilland-48.vlw");

		cp5 = new ControlP5(this);

		map = new UnfoldingMap(this, 0, 0, 900, height - 25);

		// Show map around the location in the given zoom level.
		map.zoomAndPanTo(new Location(38.951252f, -102.26932f), 4);
		map.setBackgroundColor(240);

		// Add mouse and keyboard interactions
		MapUtils.createDefaultEventDispatcher(this, map);

		states = GeoJSONReader.loadData(this, "states-20m.json");
		stateMarkers = MapUtils.createSimpleMarkers(states);

		map.addMarkers(stateMarkers);

		font = loadFont("Nilland-48.vlw");

		climateData = loadStrings("climate_by_states.csv");
		crimeData = loadStrings("CrimeStatebyState.csv");
		popData = loadStrings("pop.csv");
		latLonData = loadStrings("state_latlon.csv");

		climateArray = new Climate[climateData.length];
		crimeArray = new ArrayList<Crime>();
		// crimeArray = new Crime[crimeData.length - 1];
		popArray = new Population[popData.length - 1];
		latLonArray = new StateLatLon[latLonData.length];

		locationArray = new Location[latLonData.length];
		simplePointArray = new SimplePointMarker[latLonData.length];
		screenPositionArray = new ScreenPosition[latLonData.length];

		for (int i = 0; i < climateData.length; i++) {

			climateChunk = split(climateData[i], ",");
			climateArray[i] = new Climate(this, climateChunk[0],
					Float.valueOf(climateChunk[2]));

			// println("State: " + climateArray[i].getState());
			// println("Average temperature: " + climateArray[i].getTempC());
			// println();
		}

		for (int i = 1; i < crimeData.length - 1; i++) {
			crimeChunk = split(crimeData[i], ",");
			if (Integer.parseInt(crimeChunk[3]) >= 2000)
				crimeArray.add(new Crime(this, crimeChunk[0], crimeChunk[1],
						crimeChunk[2], Integer.parseInt(crimeChunk[3]), Integer
								.parseInt(crimeChunk[4])));
		}

		for (int i = 0; i < crimeArray.size(); i++) {
			// Crime crime = crimeArray.get(i);
			// println("State: " + crime.getState());
			// println("Type of Crime: " + crime.getCrimeType());
			// println("Crime: " + crime.getCrime());
			// println("Year: " + crime.getYear());
			// println("Count: " + crime.getCount());
			// println();
		}

		for (int i = 1; i < popData.length; i++) {

			popChunk = split(popData[i], ",");
			popArray[i - 1] = new Population(this, popChunk[4],
					Integer.parseInt(popChunk[6]),
					Integer.parseInt(popChunk[7]),
					Integer.parseInt(popChunk[8]),
					Integer.parseInt(popChunk[9]),
					Integer.parseInt(popChunk[10]),
					Integer.parseInt(popChunk[11]));

		}

		for (int i = 0; i < popArray.length; i++) {
			popArray[i].setAverage(popArray[i].averagePopulation());
		}

		for (int i = 0; i < latLonData.length; i++) {
			latLonChunk = split(latLonData[i], ",");
			latLonArray[i] = new StateLatLon(this, latLonChunk[0],
					Double.parseDouble(latLonChunk[1]),
					Double.parseDouble(latLonChunk[2]));
		}

		// for (int i = 0; i < latLonArray.length; i++) {
		// println("State: " + latLonArray[0].getState());
		// println("Latitude: " + latLonArray[1].getLat());
		// println("Longitude: " + latLonArray[2].getLon());
		// println();
		// }

		for (int i = 0; i < locationArray.length; i++) {
			locationArray[i] = new Location(latLonArray[i].getLat(),
					latLonArray[i].getLon());
			simplePointArray[i] = new SimplePointMarker(locationArray[i]);
		}

		// map.addMarkers(simplePointArray);

		// for (int i = 0; i < popArray.length; i++) {
		// println("State: " + popArray[i].getState());
		// println("00: " + popArray[i].getP0());
		// println("01: " + popArray[i].getP1());
		// println("02: " + popArray[i].getP2());
		// println("03: " + popArray[i].getP3());
		// println("04: " + popArray[i].getP4());
		// println("05: " + popArray[i].getP5());
		// println();
		// }

		crimeTypeBox = cp5.addCheckBox("Type of Crime", 1100, 120)
				.setSize(15, 15).addItem("Violent Crime", 0)
				.addItem("Property Crime", 1);

		crimeBox = cp5.addListBox("Crime", 1050, 220, 180, 120);
		crimeBox.addItem("Murder and nonnegligent Manslaughter", 0);
		crimeBox.addItem("Forcible rape", 1);
		crimeBox.addItem("Robbery", 2);
		crimeBox.addItem("Aggravated assault", 3);
		crimeBox.addItem("Burglary", 4);
		crimeBox.addItem("Larceny-theft", 5);
		crimeBox.addItem("Motor vehicle theft", 6);

		// yearBox = cp5.addListBox("Year", 1050, 420, 180, 120);
		// yearBox.addItem("2000", 0);
		// yearBox.addItem("2001", 1);
		// yearBox.addItem("2002", 2);
		// yearBox.addItem("2003", 3);
		// yearBox.addItem("2004", 4);
		// yearBox.addItem("2005", 5);
		//
		// tempSlider = cp5.addSlider("A.A. Temperature").setPosition(950, 620)
		// .setSize(150, 15).setRange(-10, 30);
	}

	public void draw() {
		background(50);
		drawLegends();
		writeText();
		map.draw();
		drawTemperature();
		if (mousePressed)
			drawPopUp();
//		correlation(corCrimeArray, corClimateArray);
//		for (int a = 0; a<50; a++){
//		println("l: "+corCrimeArray[a]);}
	}

	public void writeText() {
		int xPos = 950;
		fill(255);
		smooth();
		textFont(font, 18);
		textAlign(LEFT);
		text("Crime, Climate and Population\n2000 - 2005", 950, 50);

		textFont(font, 12);
		text("Total Crime Count: ", 950, 130);
		textFont(font, 9);
		text("*calculates total number\nof that type of crime", 950, 140);

		textFont(font, 12);
		text("Crime: ", 950, 190);
		text(selectedCrime, 1050, 190);
		
		textFont(font, 14);
		text("Total Crime Count", xPos, 370);
		text("Annual Average Temperature Scale", xPos, 520);

		textFont(font, 12);
		textAlign(RIGHT);
		text("Author: T. Heng and A. Rodrigues", width - 20, height - 10);
		textAlign(LEFT);
		text(String.valueOf(globalBaseValue), xPos, 390);
		text(String.valueOf(globalBaseValue * 2), xPos + 30, 445);
		text(String.valueOf(globalBaseValue * 3), xPos + 30 * 2, 390);
		text(String.valueOf(globalBaseValue * 4), xPos + 30 * 3, 445);
		text(String.valueOf(globalBaseValue * 5), xPos + 30 * 4, 390);
		text(String.valueOf(globalBaseValue * 6), xPos + 30 * 5, 445);
		text(String.valueOf(globalBaseValue * 7), xPos + 30 * 6, 390);
		text(String.valueOf(globalBaseValue * 8), xPos + 30 * 7, 445);
		text(String.valueOf(globalBaseValue * 9), xPos + 30 * 8, 390);

		text("<= 0", xPos, 540);
		text("(0, 3]", xPos + 30, 595);
		text("(3, 6]", xPos + 30 * 2, 540);
		text("(6, 9]", xPos + 30 * 3, 595);
		text("(9, 12]", xPos + 30 * 4, 540);
		text("(12, 15]", xPos + 30 * 5, 595);
		text("(15, 18]", xPos + 30 * 6, 540);
		text(">= 18", xPos + 30 * 7, 595);
		 

		fill(col1);
		stroke(50);
		strokeWeight(1);
		rect(xPos, 400, 30, 30);
		fill(col2);
		rect(xPos + 30, 400, 30, 30);
		fill(col3);
		rect(xPos + 30 * 2, 400, 30, 30);
		fill(col4);
		rect(xPos + 30 * 3, 400, 30, 30);
		fill(col5);
		rect(xPos + 30 * 4, 400, 30, 30);
		fill(col6);
		rect(xPos + 30 * 5, 400, 30, 30);
		fill(col7);
		rect(xPos + 30 * 6, 400, 30, 30);
		fill(col8);
		rect(xPos + 30 * 7, 400, 30, 30);
		fill(col9);
		rect(xPos + 30 * 8, 400, 30, 30);

		fill(tempColor0);
		stroke(200);
		strokeWeight(1);
		ellipseMode(LEFT);
		ellipse(xPos, 550, 25, 25);
		fill(tempColor1);
		ellipse(xPos + 30, 550, 25, 25);
		fill(tempColor2);
		ellipse(xPos + 30 * 2, 550, 25, 25);
		fill(tempColor3);
		ellipse(xPos + 30 * 3, 550, 25, 25);
		fill(tempColor4);
		ellipse(xPos + 30 * 4, 550, 25, 25);
		fill(tempColor5);
		ellipse(xPos + 30 * 5, 550, 25, 25);
		fill(tempColor6);
		ellipse(xPos + 30 * 6, 550, 25, 25);
		fill(tempColor7);
		ellipse(xPos + 30 * 7, 550, 25, 25);
		
		
	}

	public int totalViolentCrime(String state) {

		int total = 0;

		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrimeType().equalsIgnoreCase("Violent Crime")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalPropertyCrime(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrimeType().equalsIgnoreCase("Property Crime")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalMurderCrime(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase(
					"Murder and nonnegligent Manslaughter")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalRape(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase("Forcible rape")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalRobbery(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase("Robbery")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalAggravatedAssault(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase("Aggravated assault")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalBurglary(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase("Burglary")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalTheft(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase("Larceny-theft")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public int totalVehicleTheft(String state) {

		int total = 0;
		for (int i = 0; i < crimeArray.size(); i++) {
			Crime crime = (Crime) crimeArray.get(i);
			if (crime.getCrime().equalsIgnoreCase("Motor vehicle theft")) {
				if (crime.getState().equalsIgnoreCase(state))
					total = total + crime.getCount();
			}
		}
		return total;
	}

	public void controlEvent(ControlEvent theEvent) {
		if (theEvent.isFrom(crimeTypeBox)) {
			if (crimeTypeBox.getArrayValue()[0] == 1
					&& crimeTypeBox.getArrayValue()[1] == 0) {

				selectionNum = 7;

				crimeBox.clear();
				crimeBox.addItem("Murder and nonnegligent Manslaughter", 0);
				crimeBox.addItem("Forcible rape", 1);

				shadeViolentStates();

			} else if (crimeTypeBox.getArrayValue()[1] == 1
					&& crimeTypeBox.getArrayValue()[0] == 0) {

				selectionNum = 8;

				crimeBox.clear();
				crimeBox.addItem("Robbery", 2);
				crimeBox.addItem("Aggravated assault", 3);
				crimeBox.addItem("Burglary", 4);
				crimeBox.addItem("Larceny-theft", 5);
				crimeBox.addItem("Motor vehicle theft", 6);
				shadePropertyStates();
			} else if (crimeTypeBox.getArrayValue()[0] == 0
					&& crimeTypeBox.getArrayValue()[1] == 0) {
				crimeBox.clear();
				crimeBox.addItem("Murder and nonnegligent Manslaughter", 0);
				crimeBox.addItem("Forcible rape", 1);
				crimeBox.addItem("Robbery", 2);
				crimeBox.addItem("Aggravated assault", 3);
				crimeBox.addItem("Burglary", 4);
				crimeBox.addItem("Larceny-theft", 5);
				crimeBox.addItem("Motor vehicle theft", 6);
				shadeNone();
			} else {

				selectionNum = 9;

				crimeBox.clear();
				crimeBox.addItem("Murder and nonnegligent Manslaughter", 0);
				crimeBox.addItem("Forcible rape", 1);
				crimeBox.addItem("Robbery", 2);
				crimeBox.addItem("Aggravated assault", 3);
				crimeBox.addItem("Burglary", 4);
				crimeBox.addItem("Larceny-theft", 5);
				crimeBox.addItem("Motor vehicle theft", 6);
				shadeViolentProperty();
			}
		}

		if (theEvent.isGroup() && theEvent.name().equals("Crime")) {
			if (theEvent.group().value() == 0) {
				selectionNum = 0;
				selectedCrime = "Murder and nonnegligent Manslaughter";
				shadeMurder();

			} else if (theEvent.group().value() == 1) {
				selectionNum = 1;
				selectedCrime = "Forcible rape";
				shadeRape();

			} else if (theEvent.group().value() == 2) {
				selectionNum = 2;
				selectedCrime = "Robbery";
				shadeRobbery();

			} else if (theEvent.group().value() == 3) {
				selectionNum = 3;
				selectedCrime = "Aggravated assault";
				shadeAssault();

			} else if (theEvent.group().value() == 4) {
				selectionNum = 4;
				selectedCrime = "Burglary";
				shadeBurglary();

			} else if (theEvent.group().value() == 5) {
				selectionNum = 5;
				selectedCrime = "Larceny-theft";
				shadeTheft();

			} else if (theEvent.group().value() == 6) {
				selectionNum = 6;
				selectedCrime = "Motor vehicle theft";
				shadeVehicleTheft();

			}
		}

		// if (theEvent.isGroup() && theEvent.name().equals("Year")) {
		// if (theEvent.group().value() == 0) {
		// selectedYear = String.valueOf(2000);
		// } else if (theEvent.group().value() == 1) {
		// selectedYear = String.valueOf(2001);
		// } else if (theEvent.group().value() == 2) {
		// selectedYear = String.valueOf(2002);
		// } else if (theEvent.group().value() == 3) {
		// selectedYear = String.valueOf(2003);
		// } else if (theEvent.group().value() == 4) {
		// selectedYear = String.valueOf(2004);
		// } else if (theEvent.group().value() == 5) {
		// selectedYear = String.valueOf(2005);
		// }
		// }
	}

	public void shadeViolentProperty() {
		int mainColor = 0;
		int baseValue = round(7009180 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalViolentCrime(marker.getProperties().get("NAME")
//					.toString()));
//			println(totalPropertyCrime(marker.getProperties().get("NAME")
//					.toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalPropertyCrime(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 2
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue) {
						mainColor = col2;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 3
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 2) {
						mainColor = col3;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 4
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 3) {
						mainColor = col4;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 5
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 4) {
						mainColor = col5;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 6
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 5) {
						mainColor = col6;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 7
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 6) {
						mainColor = col7;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 8
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 7) {
						mainColor = col8;
					} else if ((totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
							.getState())) <= baseValue * 9
							&& (totalPropertyCrime(crime.getState()) + totalViolentCrime(crime
									.getState())) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeViolentStates() {
		int mainColor = 0;
		int baseValue = round(1216678 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalViolentCrime(marker.getProperties().get("NAME")
//					.toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalViolentCrime(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 2
							&& totalViolentCrime(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 3
							&& totalViolentCrime(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 4
							&& totalViolentCrime(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 5
							&& totalViolentCrime(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 6
							&& totalViolentCrime(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 7
							&& totalViolentCrime(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 8
							&& totalViolentCrime(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 9
							&& totalViolentCrime(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}
		println();

	}

	public void shadePropertyStates() {
		int mainColor = 0;
		int baseValue = round(7009180 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalPropertyCrime(marker.getProperties().get("NAME")
//					.toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalPropertyCrime(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 2
							&& totalPropertyCrime(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 3
							&& totalPropertyCrime(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 4
							&& totalPropertyCrime(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalViolentCrime(crime.getState()) <= baseValue * 5
							&& totalViolentCrime(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 6
							&& totalPropertyCrime(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 7
							&& totalPropertyCrime(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 8
							&& totalPropertyCrime(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalPropertyCrime(crime.getState()) <= baseValue * 9
							&& totalPropertyCrime(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}
		}

	}

	public void shadeMurder() {
		int mainColor = 0;
		int baseValue = round(13982 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalMurderCrime(marker.getProperties().get("NAME")
//					.toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalMurderCrime(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 2
							&& totalMurderCrime(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 3
							&& totalMurderCrime(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 4
							&& totalMurderCrime(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 5
							&& totalMurderCrime(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 6
							&& totalMurderCrime(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 7
							&& totalMurderCrime(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 8
							&& totalMurderCrime(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalMurderCrime(crime.getState()) <= baseValue * 9
							&& totalMurderCrime(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeRape() {
		int mainColor = 0;
		int baseValue = round(58945 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalRape(marker.getProperties().get("NAME").toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalRape(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalRape(crime.getState()) <= baseValue * 2
							&& totalRape(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalRape(crime.getState()) <= baseValue * 3
							&& totalRape(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalRape(crime.getState()) <= baseValue * 4
							&& totalRape(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalRape(crime.getState()) <= baseValue * 5
							&& totalRape(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalRape(crime.getState()) <= baseValue * 6
							&& totalRape(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalRape(crime.getState()) <= baseValue * 7
							&& totalRape(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalRape(crime.getState()) <= baseValue * 8
							&& totalRape(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalRape(crime.getState()) <= baseValue * 9
							&& totalRape(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeRobbery() {
		int mainColor = 0;
		int baseValue = round(378922 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalRobbery(marker.getProperties().get("NAME").toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalRobbery(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalRobbery(crime.getState()) <= baseValue * 2
							&& totalRobbery(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalRobbery(crime.getState()) <= baseValue * 3
							&& totalRobbery(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalRobbery(crime.getState()) <= baseValue * 4
							&& totalRobbery(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalRobbery(crime.getState()) <= baseValue * 5
							&& totalRobbery(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalRobbery(crime.getState()) <= baseValue * 6
							&& totalRobbery(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalRobbery(crime.getState()) <= baseValue * 7
							&& totalRobbery(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalRobbery(crime.getState()) <= baseValue * 8
							&& totalRobbery(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalRobbery(crime.getState()) <= baseValue * 9
							&& totalRobbery(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeAssault() {
		int mainColor = 0;
		int baseValue = round(764829 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalAggravatedAssault(marker.getProperties().get("NAME")
//					.toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalAggravatedAssault(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 2
							&& totalAggravatedAssault(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 3
							&& totalAggravatedAssault(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 4
							&& totalAggravatedAssault(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 5
							&& totalAggravatedAssault(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 6
							&& totalAggravatedAssault(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 7
							&& totalAggravatedAssault(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 8
							&& totalAggravatedAssault(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalAggravatedAssault(crime.getState()) <= baseValue * 9
							&& totalAggravatedAssault(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeBurglary() {
		int mainColor = 0;
		int baseValue = round(1431419 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalBurglary(marker.getProperties().get("NAME").toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalBurglary(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalBurglary(crime.getState()) <= baseValue * 2
							&& totalBurglary(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalBurglary(crime.getState()) <= baseValue * 3
							&& totalBurglary(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalBurglary(crime.getState()) <= baseValue * 4
							&& totalBurglary(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalBurglary(crime.getState()) <= baseValue * 5
							&& totalBurglary(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalBurglary(crime.getState()) <= baseValue * 6
							&& totalBurglary(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalBurglary(crime.getState()) <= baseValue * 7
							&& totalBurglary(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalBurglary(crime.getState()) <= baseValue * 8
							&& totalBurglary(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalBurglary(crime.getState()) <= baseValue * 9
							&& totalBurglary(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeTheft() {
		int mainColor = 0;
		int baseValue = round(4217856 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalTheft(marker.getProperties().get("NAME").toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalTheft(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalTheft(crime.getState()) <= baseValue * 2
							&& totalTheft(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalTheft(crime.getState()) <= baseValue * 3
							&& totalTheft(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalTheft(crime.getState()) <= baseValue * 4
							&& totalTheft(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalTheft(crime.getState()) <= baseValue * 5
							&& totalTheft(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalTheft(crime.getState()) <= baseValue * 6
							&& totalTheft(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalTheft(crime.getState()) <= baseValue * 7
							&& totalTheft(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalTheft(crime.getState()) <= baseValue * 8
							&& totalTheft(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalTheft(crime.getState()) <= baseValue * 9
							&& totalTheft(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeVehicleTheft() {
		int mainColor = 0;
		int baseValue = round(1359905 / 9);
		globalBaseValue = baseValue;

		for (Marker marker : stateMarkers) {
			// print(marker.getProperties().get("NAME").toString());
			// print(": ");
//			println(totalVehicleTheft(marker.getProperties().get("NAME")
//					.toString()));

			for (int i = 0; i < crimeArray.size(); i++) {

				Crime crime = (Crime) crimeArray.get(i);

				if (marker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(crime.getState())) {

					if (totalVehicleTheft(crime.getState()) <= baseValue) {
						mainColor = col1;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 2
							&& totalVehicleTheft(crime.getState()) > baseValue) {
						mainColor = col2;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 3
							&& totalVehicleTheft(crime.getState()) > baseValue * 2) {
						mainColor = col3;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 4
							&& totalVehicleTheft(crime.getState()) > baseValue * 3) {
						mainColor = col4;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 5
							&& totalVehicleTheft(crime.getState()) > baseValue * 4) {
						mainColor = col5;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 6
							&& totalVehicleTheft(crime.getState()) > baseValue * 5) {
						mainColor = col6;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 7
							&& totalVehicleTheft(crime.getState()) > baseValue * 6) {
						mainColor = col7;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 8
							&& totalVehicleTheft(crime.getState()) > baseValue * 7) {
						mainColor = col8;
					} else if (totalVehicleTheft(crime.getState()) <= baseValue * 9
							&& totalVehicleTheft(crime.getState()) > baseValue * 8) {
						mainColor = col9;
					}

					marker.setColor(color(mainColor));
				}

			}

		}

	}

	public void shadeNone() {
		int mainColor = 255;

		for (Marker marker : stateMarkers) {
			marker.setColor(mainColor);
		}
	}

	public void mousePressed() {
		Marker hitMarker = map.getFirstHitMarker(mouseX, mouseY);
		if (hitMarker != null) {

			pointedState = hitMarker.getProperties().get("NAME").toString();

			for (int i = 0; i < climateArray.length; i++) {
				if (hitMarker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(climateArray[i].getState())) {
					temp = climateArray[i].getTempC();
					totalViolent = totalViolentCrime(pointedState);
					totalProperty = totalPropertyCrime(pointedState);
					totalMurder = totalMurderCrime(pointedState);
					totalRape = totalRape(pointedState);
					totalRobbery = totalRobbery(pointedState);
					totalAssault = totalAggravatedAssault(pointedState);
					totalBurglary = totalBurglary(pointedState);
					totalTheft = totalTheft(pointedState);
					totalVehicleTheft = totalVehicleTheft(pointedState);
				}
			}

			for (int i = 0; i < popArray.length; i++) {
				if (hitMarker.getProperties().get("NAME").toString()
						.equalsIgnoreCase(popArray[i].getState())) {
					population = popArray[i].getAverage();
					// if (selectedYear.equalsIgnoreCase("2000"))
					// population = popArray[i].getP0();
					// else if (selectedYear.equalsIgnoreCase("2001"))
					// population = popArray[i].getP1();
					// else if (selectedYear.equalsIgnoreCase("2002"))
					// population = popArray[i].getP2();
					// else if (selectedYear.equalsIgnoreCase("2003"))
					// population = popArray[i].getP3();
					// else if (selectedYear.equalsIgnoreCase("2004"))
					// population = popArray[i].getP4();
					// else if (selectedYear.equalsIgnoreCase("2005"))
					// population = popArray[i].getP5();
				}
			}

			hitMarker.setSelected(true);
		}
		// else {
		// for (Marker marker : map.getMarkers()) {
		// marker.setSelected(false);
		// }
		// }
	}

	public void mouseReleased() {
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}
	}

	public void drawPopUp() {
		fill(50);
		smooth();
		noStroke();
		rect(mouseX + 10, mouseY + 10, 300, 150);
		fill(255);
		textFont(font, 14);
		text("State: " + pointedState, mouseX + 20, mouseY + 30);
		// text("Year: " + selectedYear, mouseX + 20, mouseY + 45);
		text("Annual Average Temperature: " + temp + " C", mouseX + 20,
				mouseY + 60);
		text("Population: " + population, mouseX + 20, mouseY + 75);

		if (selectionNum == 7)
			text("Total Violent Crimes: " + totalViolent, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 8)
			text("Total Property Crimes: " + totalProperty, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 9)
			text("Total Violent + Property Crimes: \n" + totalViolent
					+ totalProperty, mouseX + 20, mouseY + 105);

		else if (selectionNum == 0)
			text("Total Murder Cases: " + totalMurder, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 1)
			text("Total Forcible Rape Cases: " + totalRape, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 2)
			text("Total Robbery Cases: " + totalRobbery, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 3)
			text("Total Aggravated Assault Cases: " + totalAssault,
					mouseX + 20, mouseY + 105);
		else if (selectionNum == 4)
			text("Total Burglary Cases: " + totalBurglary, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 5)
			text("Total Larceny-Theft Cases: " + totalTheft, mouseX + 20,
					mouseY + 105);
		else if (selectionNum == 6)
			text("Total Motor Vehicle Theft Cases: " + totalVehicleTheft,
					mouseX + 20, mouseY + 105);
	}

	public void drawLegends() {
		fill(50);
		smooth();
		noStroke();
		rect(0, height - 25, 200, 20);
		fill(255);
		textFont(font, 14);
		text("Click and hold on a state to see more information.", 10,
				height - 10);
	}

	void drawTemperature() {
		for (int i = 0; i < climateArray.length; i++) {

			for (int j = 0; j < latLonArray.length; j++) {
				if (climateArray[i].getState().equalsIgnoreCase(
						latLonArray[j].getState())) {
					screenPositionArray[j] = simplePointArray[j]
							.getScreenPosition(map);
					if ((screenPositionArray[j].x >= 0 && screenPositionArray[j].x <= 900)
							&& (screenPositionArray[j].y >= 0 && screenPositionArray[j].y <= height - 25)) {
						if (climateArray[i].getTempC() <= 0) {

							fill(tempColor0);
							strokeWeight(1);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 0
								&& climateArray[i].getTempC() <= 3) {
							fill(tempColor1);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 3
								&& climateArray[i].getTempC() <= 6) {
							fill(tempColor2);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 6
								&& climateArray[i].getTempC() <= 9) {
							fill(tempColor3);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 9
								&& climateArray[i].getTempC() <= 12) {
							fill(tempColor4);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 12
								&& climateArray[i].getTempC() <= 15) {
							fill(tempColor5);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 15
								&& climateArray[i].getTempC() <= 18) {
							fill(tempColor6);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						} else if (climateArray[i].getTempC() > 18) {
							fill(tempColor7);
							strokeWeight(2);
							smooth();
							stroke(200);
							ellipse(screenPositionArray[j].x,
									screenPositionArray[j].y, 10, 10);
						}
					}
				}

			}
		}
	}
}
