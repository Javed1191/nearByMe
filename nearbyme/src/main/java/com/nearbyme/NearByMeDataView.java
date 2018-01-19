package com.nearbyme;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import classes.Place;
import utils.Camera;
import utils.PaintUtils;
import utils.RadarLines;


/**
 * 
 * Currently the markers are plotted with reference to line parallel to the earth surface.
 * We are working to include the elevation and height factors.
 * 
 * */


public class NearByMeDataView {

	RelativeLayout[] locationMarkerView;
	ImageView[] subjectImageView;
	LayoutParams[] layoutParams;
	LayoutParams[] subjectImageViewParams;
	LayoutParams[] subjectTextViewParams;
	TextView[] locationTextView;

		
	int[] nextXofText ;
	ArrayList<Integer> 	nextYofText = new ArrayList<Integer>();

	double[] bearings;
	float angleToShift;
	float yPosition;
	Location currentLocation = new Location("provider");
	Location destinedLocation = new Location("provider");
	
	/** is the view Inited? */
	boolean isInit = false;
	boolean isDrawing = true;
	boolean isFirstEntry;
	public Context _context;
	/** width and height of the view*/
	int width, height;
	android.hardware.Camera camera;

	float yawPrevious;
	float yaw = 0;
	float pitch = 0;
	float roll = 0;

	DisplayMetrics displayMetrics;
	NearByMeRadarView radarPoints;

	RadarLines lrl = new RadarLines();
	RadarLines rrl = new RadarLines();
	float rx = 50, ry = 50;
	public float addX = 0, addY = 0;
	public float degreetopixelWidth;
	public float degreetopixelHeight;
	public float pixelstodp;
	public float bearing;

	public int[][] coordinateArray;// = new int[20][2];
	public int locationBlockWidth;
	public int locationBlockHeight;

	public float deltaX;
	public float deltaY;
	
	ARView arView;

	int dWidth=0,dHeight=0;
	
	public NearByMeDataView(Context ctx) {
		this._context = ctx;
		this.arView = (ARView) ctx;		
		coordinateArray = new int[this.arView.listPlaces.size()][2];

		// Dialog design
		DisplayMetrics displaymetrics = new DisplayMetrics();
		Activity activity = (Activity) _context;
		activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		dWidth = displaymetrics.widthPixels;
		dHeight = displaymetrics.heightPixels;
	}


	public boolean isInited() {
		return isInit;
	}

	public void init(int widthInit, int heightInit, android.hardware.Camera camera, DisplayMetrics displayMetrics, RelativeLayout rel) {
		
		this.displayMetrics = displayMetrics;
		try {
			
			locationMarkerView = new RelativeLayout[this.arView.listPlaces.size()];
			layoutParams = new LayoutParams[this.arView.listPlaces.size()];
			subjectImageViewParams = new LayoutParams[this.arView.listPlaces.size()];
			subjectTextViewParams = new LayoutParams[this.arView.listPlaces.size()];
			subjectImageView = new ImageView[this.arView.listPlaces.size()];
			locationTextView = new TextView[this.arView.listPlaces.size()];
			nextXofText = new int[this.arView.listPlaces.size()];
			
			for(int i=0;i<this.arView.listPlaces.size();i++){
				layoutParams[i] = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				subjectTextViewParams[i] = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

				subjectImageView[i] = new ImageView(_context);
				locationMarkerView[i] = new RelativeLayout(_context);
				locationTextView[i] = new TextView(_context);
				locationTextView[i].setText(checkTextToDisplay(this.arView.listPlaces.get(i).distance) + " km");
				locationTextView[i].setTextColor(Color.WHITE);
				locationTextView[i].setTextSize(10f);
				
				subjectImageView[i].setBackgroundResource(R.drawable.icon_marker);
				locationMarkerView[i].setId(i);
				subjectImageView[i].setId(i);
				locationTextView[i].setId(i);
				
				subjectImageViewParams[i] = new  LayoutParams(65, 80);
				subjectImageViewParams[i].topMargin = 5;
				subjectImageViewParams[i].addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
				subjectImageViewParams[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
				
				layoutParams[i].setMargins(displayMetrics.widthPixels/2, displayMetrics.heightPixels/2, 0, 0);
				
				locationMarkerView[i] = new RelativeLayout(_context);
				//locationMarkerView[i].setBackgroundColor(Color.CYAN);
				//locationMarkerView[i].setBackgroundResource(R.drawable.thoughtbubble);
				subjectTextViewParams[i].addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
				subjectTextViewParams[i].addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
				subjectTextViewParams[i].topMargin = 15;
				locationMarkerView[i].setLayoutParams(layoutParams[i]);
				subjectImageView[i].setLayoutParams(subjectImageViewParams[i]);
				locationTextView[i].setLayoutParams(subjectTextViewParams[i]);
				//locationTextView[i].setBackgroundColor(Color.BLUE);

				locationMarkerView[i].addView(subjectImageView[i]);
				locationMarkerView[i].addView(locationTextView[i]);
				
				/*FrameLayout.LayoutParams linearParams = new FrameLayout.LayoutParams(
				        new FrameLayout.LayoutParams(
				        		FrameLayout.LayoutParams.MATCH_PARENT,
				        		FrameLayout.LayoutParams.WRAP_CONTENT));*/
								
				int top = getMarkerHeight(Float.parseFloat(this.arView.listPlaces.get(i).distance));Log.e("", top + "<<Top");
				/*linearParams.setMargins(0, top, 0, 0);
				rel.setLayoutParams(linearParams);
				rel.requestLayout();*/
				locationMarkerView[i].setY(top);
				
				rel.addView(locationMarkerView[i]);

				subjectImageView[i].setClickable(false);
				locationTextView[i].setClickable(false);

				subjectImageView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v.getId() != -1) {

							LayoutParams params = (LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int index = 0;
							for (LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin,
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
								}
								index++;
							}

							if (matchIDs.size() > 1) {

							}
							showDialog(arView.listPlaces.get(v.getId()).getShort_atm_name(),arView.listPlaces.get(v.getId()).getDistance(),
									arView.listPlaces.get(v.getId()).getAddress_1()+" "+arView.listPlaces.get(v.getId()).getAddress_2()+" "+
									arView.listPlaces.get(v.getId()).getAddress_3(),
									arView.listPlaces.get(v.getId()).getLat(),
									arView.listPlaces.get(v.getId()).getLog());


							locationMarkerView[v.getId()].bringToFront();

						}
						
					}
				});


				locationTextView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if ((v.getId() != -1)) {

							LayoutParams params = (LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int index = 0;
							for (LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin,
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
								}
								index++;
							}

							if (matchIDs.size() > 1) {

							}
							showDialog(arView.listPlaces.get(v.getId()).getShort_atm_name(),arView.listPlaces.get(v.getId()).getDistance(),
									arView.listPlaces.get(v.getId()).getAddress_1()+" "+arView.listPlaces.get(v.getId()).getAddress_2()+" "+
											arView.listPlaces.get(v.getId()).getAddress_3(),
									arView.listPlaces.get(v.getId()).getLat(),
									arView.listPlaces.get(v.getId()).getLog());

							locationMarkerView[v.getId()].bringToFront();

						}
						
					}
				});

				locationMarkerView[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (v.getId() != -1) {

							LayoutParams params = (LayoutParams) locationMarkerView[v.getId()].getLayoutParams();
							Rect rect = new Rect(params.leftMargin, params.topMargin, params.leftMargin + params.width, params.topMargin + params.height);
							ArrayList<Integer> matchIDs = new ArrayList<Integer>();
							Rect compRect = new Rect();
							int index = 0;
							for (LayoutParams layoutparams : layoutParams) {
								compRect.set(layoutparams.leftMargin, layoutparams.topMargin,
										layoutparams.leftMargin + layoutparams.width, layoutparams.topMargin + layoutparams.height);
								if (compRect.intersect(rect)) {
									matchIDs.add(index);
								}
								index++;
							}

							if (matchIDs.size() > 1) {

							}
							showDialog(arView.listPlaces.get(v.getId()).getShort_atm_name(),arView.listPlaces.get(v.getId()).getDistance(),
									arView.listPlaces.get(v.getId()).getAddress_1()+" "+arView.listPlaces.get(v.getId()).getAddress_2()+" "+
									arView.listPlaces.get(v.getId()).getAddress_3(),
									arView.listPlaces.get(v.getId()).getLat(),
									arView.listPlaces.get(v.getId()).getLog());

							locationMarkerView[v.getId()].bringToFront();

						}
						
					}
				});
			}


			this.degreetopixelWidth = this.displayMetrics.widthPixels / camera.getParameters().getHorizontalViewAngle();
			this.degreetopixelHeight = this.displayMetrics.heightPixels / camera.getParameters().getVerticalViewAngle();
			
			//this.degreetopixelWidth = this.displayMetrics.heightPixels / camera.getParameters().getVerticalViewAngle();
			//this.degreetopixelHeight = this.displayMetrics.widthPixels / camera.getParameters().getHorizontalViewAngle();
			
			System.out.println("this.displayMetrics.widthPixels "+ this.displayMetrics.widthPixels);
			System.out.println("camera.getParameters().getHorizontalViewAngle() "+ camera.getParameters().getHorizontalViewAngle());
			System.out.println("this.degreetopixelWidth "+ this.degreetopixelWidth);

			bearings = new double[this.arView.listPlaces.size()];
			//currentLocation.setLatitude(19.0649303);//19.9754168);//NearByMeMainActivity.getCurrentLocation.getLatitude()
			//currentLocation.setLongitude(72.8354298);//(73.7787641);//NearByMeMainActivity.getCurrentLocation.getLongitude()

			currentLocation.setLatitude(NearByMeMainActivity.getCurrentLocation.getLatitude());//19.9754168);//NearByMeMainActivity.getCurrentLocation.getLatitude()
			currentLocation.setLongitude(NearByMeMainActivity.getCurrentLocation.getLongitude());//(73.7787641);//NearByMeMainActivity.getCurrentLocation.getLongitude()

			//GetCurrentLocation currentLocation1 = new GetCurrentLocation(_context);

			//Location currentLocation = new Location("CurrentLocation");
			//currentLocation.setLatitude(11.1642254);
			//currentLocation.setLongitude(75.8795392);

			if(bearing < 0)
				bearing  = 360 + bearing;

			for(int i = 0; i <this.arView.listPlaces.size();i++){
				destinedLocation.setLatitude(Double.parseDouble(this.arView.listPlaces.get(i).lat));
				destinedLocation.setLongitude(Double.parseDouble(this.arView.listPlaces.get(i).log));
				bearing = currentLocation.bearingTo(destinedLocation);

				if(bearing < 0){
					bearing  = 360 + bearing;
				}
				bearings[i] = bearing;

			}
			
			radarPoints = new NearByMeRadarView(this, bearings, arView.listPlaces);
			this.camera = camera;
			width = widthInit;
			height = heightInit;
			
			lrl.set(0, -NearByMeRadarView.RADIUS);
			lrl.rotate(Camera.DEFAULT_VIEW_ANGLE / 2);
			lrl.add(rx + NearByMeRadarView.RADIUS, ry + NearByMeRadarView.RADIUS);
			rrl.set(0, -NearByMeRadarView.RADIUS);
			rrl.rotate(-Camera.DEFAULT_VIEW_ANGLE / 2);
			rrl.add(rx + NearByMeRadarView.RADIUS, ry + NearByMeRadarView.RADIUS);
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("", "INIT error " + ex.getMessage());
		}
		
		/*
		 * initialization is done, so dont call init() again.
		 * */
		isInit = true;
	}

	public void draw(PaintUtils dw, float yaw, float pitch, float roll) {


		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;

		// Draw Radar
		String	dirTxt = "";
		bearing = (int) this.yaw; 
		int range = (int) (this.yaw / (360f / 16f));
		if (range == 15 || range == 0) dirTxt = "N"; 
		else if (range == 1 || range == 2) dirTxt = "NE"; 
		else if (range == 3 || range == 4) dirTxt = "E"; 
		else if (range == 5 || range == 6) dirTxt = "SE";
		else if (range == 7 || range == 8) dirTxt= "S"; 
		else if (range == 9 || range == 10) dirTxt = "SW"; 
		else if (range == 11 || range == 12) dirTxt = "W"; 
		else if (range == 13 || range == 14) dirTxt = "NW";


		radarPoints.view = this;

		dw.paintObj(radarPoints, rx+ PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
		dw.setFill(false);
		dw.setStrokeWidth(2.0f);
		dw.setColor(Color.argb(255,220,255,255));
		dw.paintLine( lrl.x + 10, lrl.y + 8, rx+ NearByMeRadarView.RADIUS, ry+ NearByMeRadarView.RADIUS); //right
		dw.paintLine( rrl.x - 10, rrl.y + 8, rx+ NearByMeRadarView.RADIUS, ry+ NearByMeRadarView.RADIUS); //left
		//dw.setColor(Color.rgb(255,255,255));
		//dw.setFontSize(12);
		//radarText(dw, "" + bearing + ((char) 176) + " " + dirTxt, rx + NearByMeRadarView.RADIUS, ry - 5, true, false, -1);

		//cView.paintObj(radarPoints, rx+PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw, dw);
		drawTextBlock(dw);
	}

	void drawPOI(PaintUtils dw, float yaw){
		if(isDrawing){
			dw.paintObj(radarPoints, rx+PaintUtils.XPADDING, ry+PaintUtils.YPADDING, -this.yaw, 1, this.yaw);
			isDrawing = false;
		}
	}

	void radarText(PaintUtils dw, String txt, float x, float y, boolean bg, boolean isLocationBlock, int count) {

		float padw = 4, padh = 2;
		float w = dw.getTextWidth(txt) + padw * 2;
		float h;
		if(isLocationBlock){
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2+10;
		}else{
			h = dw.getTextAsc() + dw.getTextDesc() + padh * 2;
		}
		if (bg) {

			if(isLocationBlock){
				layoutParams[count].setMargins((int)(x - w / 2 - 10), (int)(y - h / 2 - 10), 0, 0);
				layoutParams[count].height = 120;
				layoutParams[count].width = 120;
				locationMarkerView[count].setLayoutParams(layoutParams[count]);

			}else{
				dw.setColor(Color.rgb(0, 0, 0));
				dw.setFill(true);
				dw.paintRect((x - w / 2) + PaintUtils.XPADDING , (y - h / 2) + PaintUtils.YPADDING, w, h);
				pixelstodp = (padw + x - w / 2)/((displayMetrics.density)/160);
				dw.setColor(Color.rgb(255, 255, 255));
				dw.setFill(false);
				dw.paintText((padw + x -w/2)+PaintUtils.XPADDING, ((padh + dw.getTextAsc() + y - h / 2)) + PaintUtils.YPADDING,txt);
			}
		}

	}

	String checkTextToDisplay(String str){

		if(str.length()>4){
			str = str.substring(0, 4);
		}
		return str;

	}

	void drawTextBlock(PaintUtils dw){
		//System.out.println("drawTextBlock " + bearings.length + " pitch " + this.pitch);
		for(int i = 0; i<bearings.length;i++){
			if(bearings[i]<0){
				
				if(this.pitch != 90){
					yPosition = (this.pitch - 90) * this.degreetopixelHeight+200;
				}else{
					yPosition = (float)this.height/2;
				}

				bearings[i] = 360 - bearings[i];
				angleToShift = (float)bearings[i] - this.yaw;
				nextXofText[i] = (int)(angleToShift*degreetopixelWidth);
				yawPrevious = this.yaw;
				isDrawing = true;
				radarText(dw, this.arView.listPlaces.get(i).distance, nextXofText[i], yPosition, true, true, i);
				coordinateArray[i][0] =  nextXofText[i];
				coordinateArray[i][1] =   (int)yPosition;

			}else{
				if (this.yaw > 360) {
					this.yaw -= 360;
	            }
				Log.e("", yaw + "<yaw");			
				angleToShift = (float)bearings[i] - this.yaw;// - this.yaw;
				
				if(this.pitch != 90){
					yPosition = (this.pitch - 90) * this.degreetopixelHeight+200;
				}else{
					yPosition = (float)this.height/2;
				}

				nextXofText[i] =  (int)((displayMetrics.widthPixels/2)+(angleToShift*degreetopixelWidth));
				yawPrevious = this.yaw; //newly added
				if(Math.abs(coordinateArray[i][0] - nextXofText[i]) > 50){
					radarText(dw, this.arView.listPlaces.get(i).distance, (nextXofText[i]), yPosition, true, true, i);
					coordinateArray[i][0] =  (int)((displayMetrics.widthPixels/2)+(angleToShift*degreetopixelWidth));
					coordinateArray[i][1] =  (int)yPosition;
					
					isDrawing = true;
				}else{
					radarText(dw, this.arView.listPlaces.get(i).distance,coordinateArray[i][0],yPosition, true, true, i);
					isDrawing = false;
				}
			}
		}
	}
	
	private int getMarkerHeight(float distance)
	{
		if(distance > 0 && distance <= 0.3f) return (int) (displayMetrics.heightPixels * 0.60);
		else if(distance > 0.3f && distance <= 0.6f)return (int) (displayMetrics.heightPixels * 0.57);
		else if(distance > 0.6f && distance <= 0.9f)return (int) (displayMetrics.heightPixels * 0.54);
		else if(distance > 0.9f && distance <= 1.2f)return (int) (displayMetrics.heightPixels * 0.51);
		else if(distance > 1.2f && distance <= 1.5f)return (int) (displayMetrics.heightPixels * 0.48);
		else if(distance > 1.5f && distance <= 1.8f)return (int) (displayMetrics.heightPixels * 0.45);
		else if(distance > 1.8f && distance <= 2.1f)return (int) (displayMetrics.heightPixels * 0.42);
		else if(distance > 2.1f && distance <= 2.4f)return (int) (displayMetrics.heightPixels * 0.39);
		else if(distance > 2.4f && distance <= 2.7f)return (int) (displayMetrics.heightPixels * 0.36);
		else if(distance > 2.7f && distance <= 3.0f)return (int) (displayMetrics.heightPixels * 0.33);
		else if(distance > 3.0f && distance <= 3.3f)return (int) (displayMetrics.heightPixels * 0.30);
		else if(distance > 3.3f && distance <= 3.6f)return (int) (displayMetrics.heightPixels * 0.27);
		else if(distance > 3.6f && distance <= 3.9f)return (int) (displayMetrics.heightPixels * 0.24);
		else if(distance > 3.9f && distance <= 4.2f)return (int) (displayMetrics.heightPixels * 0.21);
		else if(distance > 4.2f && distance <= 4.5f)return (int) (displayMetrics.heightPixels * 0.18);
		else if(distance > 4.5f && distance <= 4.8f)return (int) (displayMetrics.heightPixels * 0.14);
		else if(distance > 4.8f && distance <= 5.1f)return (int) (displayMetrics.heightPixels * 0.11);
		else return (int) (displayMetrics.heightPixels * 0.27);
	}
	
	public void setDistance(int progress)
	{
		float distanceTemp = 0;
		switch (progress) {
			case 0: distanceTemp = 0.5f; break;
			case 1: distanceTemp = 1.0f; break;
			case 2: distanceTemp = 1.5f; break;
			case 3: distanceTemp = 2.0f; break;
			case 4: distanceTemp = 2.5f; break;
			case 5: distanceTemp = 3.0f; break;
			case 6: distanceTemp = 3.5f; break;
			case 7: distanceTemp = 4.0f; break;
			case 8: distanceTemp = 4.5f; break;
			case 9: distanceTemp = 5.0f; break;					

			default:
				break;
		}
		
		ArrayList<Place> temp = new ArrayList<Place>();
		
		for (int i = 0; i < locationMarkerView.length; i++) {
			if(Float.parseFloat(checkTextToDisplay(this.arView.listPlaces.get(i).distance)) <= distanceTemp)
			{
				locationMarkerView[i].setVisibility(RelativeLayout.VISIBLE);
				
				//int actualY = getMarkerHeight(Float.parseFloat(this.arView.listPlaces.get(i).distance));
			
				//locationMarkerView[i].setY(newPos);
				
				temp.add(this.arView.listPlaces.get(i));
			}
			else
				locationMarkerView[i].setVisibility(RelativeLayout.GONE);
		}

		//Toast.makeText(_context, temp.size() + "<< Temp distanceTemp>> " + distanceTemp + " progress >> " + progress, Toast.LENGTH_LONG).show();
		/*bearings = new double[temp.size()];
		if(bearing < 0)
			bearing  = 360 + bearing;

		for(int i = 0; i <temp.size();i++){
			destinedLocation.setLatitude(Double.parseDouble(temp.get(i).lat));
			destinedLocation.setLongitude(Double.parseDouble(temp.get(i).log));
			bearing = currentLocation.bearingTo(destinedLocation);

			if(bearing < 0){
				bearing  = 360 + bearing;
			}
			bearings[i] = bearing;

		}*/
		
		radarPoints = new NearByMeRadarView(this, bearings, temp);
	}

	public void showDialog(String strName, String strDistance, String strAddress, final String strLat, final String strLong)
	{
		final AlertDialog builder = new AlertDialog.Builder(_context).create();

		builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View builder_view = inflater.inflate(R.layout.activity_near_by_me_location_detail_dialog, null);
		builder.setView(builder_view);
		//bid_now.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		// builder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


		TextView tv_confirm = (TextView) builder_view.findViewById(R.id.tv_confirm);
		TextView tv_cancel = (TextView) builder_view.findViewById(R.id.tv_cancel);
		final TextView tv_name = (TextView) builder_view.findViewById(R.id.tv_name);
		final TextView tv_address = (TextView) builder_view.findViewById(R.id.tv_address);
		final TextView tv_distance = (TextView) builder_view.findViewById(R.id.tv_distance);

		final LinearLayout lay_direction = (LinearLayout) builder_view.findViewById(R.id.lay_direction);


		tv_name.setText(strName);
		tv_address.setText(strAddress);

		strDistance = String.format("%.2f",Double.parseDouble(strDistance));
		tv_distance.setText("Distance: "+strDistance + " km");

		lay_direction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showRout(strLat,strLong);
			}
		});

		tv_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				try {
					builder.cancel();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					//dialog.dismiss();
					builder.cancel();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		builder.show();
		Window window = builder.getWindow();
		window.setLayout(dWidth/2, LinearLayout.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);

	}

	private void showRout(String strDesLat, String strDesLong)
	{
		String strStartLat="",strStartLong="";
		strStartLat = String.valueOf(NearByMeMainActivity.getCurrentLocation.getLatitude());
		strStartLong = String.valueOf(NearByMeMainActivity.getCurrentLocation.getLongitude());

		String strUrl = "http://maps.google.com/maps?saddr="+strStartLat+","+strStartLong+"&daddr="+strDesLat+","+strDesLong;

		Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(strUrl));
		_context.startActivity(intent);
	}
}