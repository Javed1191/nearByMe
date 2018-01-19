package com.nearbyme;

import java.util.ArrayList;

import android.graphics.Color;
import android.location.Location;

import classes.Place;
import utils.PaintUtils;


public class NearByMeRadarView {
	/** The screen */
	public NearByMeDataView view;
	/** The radar's range */
	float range;
	/** Radius in pixel on screen 90 fit for oppo*/
	public static float RADIUS = 100;
	/** Position on screen originX = -35 , originY = -15*/
	static float originX = 0 , originY = 0;

	/**
	 * You can change the radar color from here.
	 *   */
	static int radarColor = Color.argb(0, 255, 250, 220);
	Location currentLocation = new Location("provider");
	Location destinedLocation = new Location("provider");

	public float[][] coordinateArray;// = new float[latitudes.length][2];

	float angleToShift;
	public float degreetopixel;
	public float bearing;
	public float circleOriginX;
	public float circleOriginY;
	private float mscale;


	public float x = 0;
	public float y = 0;
	public float z = 0;

	float  yaw = 0;
	double[] bearings;
	ARView arView = new ARView();

	ArrayList<Place> list;

	public NearByMeRadarView(NearByMeDataView nearByMeDataView, double[] bearings, ArrayList<Place> list){
		this.view = nearByMeDataView;
		this.bearings = bearings;

		//RADIUS = nearByMeDataView.arView.screenWidth / 8;

		this.list = list;
		coordinateArray = new float[list.size()][2];
		calculateMetrics();
	}

	public void calculateMetrics(){
		circleOriginX = originX + RADIUS;
		circleOriginY = originY + RADIUS;

		range = (float)arView.convertToPix(10) * 50;
		mscale = range / arView.convertToPix((int)RADIUS);
	}

	public void paint(PaintUtils dw, float yaw) { //Log.e("", "Radarvioew paint");

//		circleOriginX = originX + RADIUS;
//		circleOriginY = originY + RADIUS;
		this.yaw = yaw;
//		range = arView.convertToPix(10) * 1000;		/** Draw the radar */
		dw.setFill(true);
		dw.setColor(radarColor);
		dw.paintCircle(originX + RADIUS , originY + RADIUS , RADIUS - 14, this);

		/** put the markers in it */
//		float scale = range / arView.convertToPix((int)RADIUS);
		/**
		 *  Your current location coordinate here.
		 * */
		//currentLocation.setLatitude(19.0649303);//19.9754168);//NearByMeMainActivity.getCurrentLocation.getLatitude()
		//currentLocation.setLongitude(72.8354298);//(73.7787641);//NearByMeMainActivity.getCurrentLocation.getLongitude()

		currentLocation.setLatitude(NearByMeMainActivity.getCurrentLocation.getLatitude());//19.9754168);//NearByMeMainActivity.getCurrentLocation.getLatitude()
		currentLocation.setLongitude(NearByMeMainActivity.getCurrentLocation.getLongitude());//73.7787641);//NearByMeMainActivity.getCurrentLocation.getLongitude()

		//GetCurrentLocation currentLocation1 = new GetCurrentLocation(view._context);
		//currentLocation.setLatitude(11.1642254);
		//currentLocation.setLongitude(75.8795392);

		for(int i = 0; i < list.size();i++){
			destinedLocation.setLatitude(Double.parseDouble(list.get(i).lat));
			destinedLocation.setLongitude(Double.parseDouble(list.get(i).log));
			convLocToVec(currentLocation, destinedLocation);
			float x = this.x / mscale;
			float y = this.z / mscale;

			if (x * x + y * y < RADIUS * RADIUS) {
				dw.setFill(true);
				dw.setColor(Color.rgb(200, 0, 0));
				dw.paintRect(x + RADIUS, y + RADIUS, 5, 5);
			}
		}
	}

	/*public void calculateDistances(PaintUtils dw, float yaw){
		currentLocation.setLatitude(19.474037);
		currentLocation.setLongitude(72.800388);
		for(int i = 0; i < list.size();i++){
			if(bearings[i]<0){
				bearings[i] = 360 - bearings[i];
			}
			if(Math.abs(coordinateArray[i][0] - yaw) > 3){
				angleToShift = (float)bearings[i] - this.yaw;
				coordinateArray[i][0] = this.yaw;
			}else{
				angleToShift = (float)bearings[i] - coordinateArray[i][0] ;
				
			}
			destinedLocation.setLatitude(Double.parseDouble(list.get(i).lat));
			destinedLocation.setLongitude(Double.parseDouble(list.get(i).log));
			float[] z = new float[1];
			z[0] = 0;
			Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), destinedLocation.getLatitude(), destinedLocation.getLongitude(), z);
			bearing = currentLocation.bearingTo(destinedLocation);

			this.x = (float) (circleOriginX + 40 * (Math.cos(angleToShift)));
			this.y = (float) (circleOriginY + 40 * (Math.sin(angleToShift)));


			if (x * x + y * y < RADIUS * RADIUS) {
				dw.setFill(true);
				dw.setColor(Color.rgb(255, 255, 255));
				dw.paintRect(x + RADIUS - 1, y + RADIUS - 1, 2, 2);
			}
		}
	}*/

	/** Width on screen */
	public float getWidth() {
		return RADIUS * 2;
	}

	/** Height on screen */
	public float getHeight() {
		return RADIUS * 2;
	}


	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void convLocToVec(Location source, Location destination) {
		float[] z = new float[1];
		z[0] = 0;
		Location.distanceBetween(source.getLatitude(), source.getLongitude(), destination
				.getLatitude(), source.getLongitude(), z);
		float[] x = new float[1];
		Location.distanceBetween(source.getLatitude(), source.getLongitude(), source
				.getLatitude(), destination.getLongitude(), x);
		if (source.getLatitude() < destination.getLatitude())
			z[0] *= -1;
		if (source.getLongitude() > destination.getLongitude())
			x[0] *= -1;

		set(x[0], (float) 0, z[0]);
	}
}