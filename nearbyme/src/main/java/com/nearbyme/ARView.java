package com.nearbyme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


import classes.Place;
import utils.Compatibility;
import utils.PaintUtils;

public class ARView extends AppCompatActivity implements SensorEventListener{

	private static Context _context;
	WakeLock mWakeLock;
	CameraView cameraView;
	RadarMarkerView radarMarkerView;
	public RelativeLayout upperLayerLayout;
	static PaintUtils paintScreen;
	static NearByMeDataView nearByMeDataView;
	boolean isInited = false;
	public static float azimuth;
	public static float pitch;
	public static float roll;
	public double latitudeprevious;
	public double longitude;
	String locationContext;
	String	provider;
	DisplayMetrics displayMetrics;
	Camera camera;
	public int screenWidth;
	public int screenHeight;

	private float RTmp[] = new float[9];
	private float Rot[] = new float[9];
	private float I[] = new float[9];
	private float grav[] = new float[3];
	private float mag[] = new float[3];
	private float results[] = new float[3];
	private SensorManager sensorMgr;
	private List<Sensor> sensors;
	private Sensor sensorGrav, sensorMag;

	static final float ALPHA = 0.25f;
	protected float[] gravSensorVals;
	protected float[] magSensorVals;

	public ArrayList<Place> listPlaces;

	//CompassView compassView;
	public static double bearing;
	static int ORIENTATION_CAP = 0;
	int seekbarpoints,seekbarmax;

	Toolbar toolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_by_me_arview);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		FrameLayout fmLyt = (FrameLayout) findViewById(R.id.arViewContainer);
				
		Bundle b = getIntent().getExtras();
		if(b!=null && b.containsKey("placeArrayList"))
		{
			listPlaces = (ArrayList<Place>) b.getSerializable("placeArrayList");
			//Toast.makeText(this, listPlaces.size() + "", Toast.LENGTH_LONG).show();
		}
		
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		screenHeight = displayMetrics.heightPixels;
		screenWidth = displayMetrics.widthPixels;
		
		RelativeLayout upperLayerLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams upperLayerLayoutParams = new RelativeLayout.LayoutParams(screenWidth + 200, RelativeLayout.LayoutParams.MATCH_PARENT);
		upperLayerLayout.setLayoutParams(upperLayerLayoutParams);
		upperLayerLayout.setBackgroundColor(Color.TRANSPARENT);

		_context = this;
		cameraView = new CameraView(this);
		radarMarkerView = new RadarMarkerView(this, displayMetrics, upperLayerLayout);
		//compassView = new CompassView(this);
				
		fmLyt.addView(cameraView);
		//fmLyt.addView(compassView);
		fmLyt.addView(radarMarkerView);
		fmLyt.addView(upperLayerLayout, upperLayerLayoutParams);
		
		if(!isInited){
			nearByMeDataView = new NearByMeDataView(ARView.this);
			paintScreen = new PaintUtils(this);
			isInited = true;
		}

		upperLayerLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(_context, "RELATIVE LAYOUT CLICKED", Toast.LENGTH_SHORT).show();
			}
		});

		cameraView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {

				for (int i = 0; i < nearByMeDataView.coordinateArray.length; i++) {
					if((int)event.getX() < nearByMeDataView.coordinateArray[i][0] &&  ((int)event.getX()+100) > nearByMeDataView.coordinateArray[i][0]){
						if((int)event.getY() <= nearByMeDataView.coordinateArray[i][1] && ((int)event.getY()+100) > nearByMeDataView.coordinateArray[i][1]){
							Toast.makeText(_context, "match Found its "+ nearByMeDataView.arView.listPlaces.get(i).distance, Toast.LENGTH_SHORT).show();
							return false;
						}
					}
				}
				return true;
			}
		});
		
		//Get the width of the main view.     
		int scale_width = screenWidth;

        //set the seekbar maximum (Must be a even number, having a remainder will cause undersirable results)
        //this variable will also determine the number of points on the scale.
        seekbarmax = 9;
        seekbarpoints = (scale_width / seekbarmax); //this will determine how many points on the scale there should be on the seekbar
        
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(seekbarmax);
		seekBar.setProgress(9);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				//Log.e("", "fromUser >>" + fromUser);
				nearByMeDataView.setDistance(progress);
			}
		});
		
		 /*if (ContextCompat.checkSelfPermission(getContext(),
				 Manifest.CAMERA) != PackageManager.PERMISSION_GRANTED) {

			if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)
					getContext(), Manifest.permission.CAMERA)) {


			} else {
				ActivityCompat.requestPermissions((Activity) getContext(), 
						new String[]{Manifest.permission.CAMERA},
						MY_PERMISSIONS_REQUEST_CAMERA);
			}

		}*/

         //Set the seekbar widgets background to the above drawable.
        // seekBar.setProgressDrawable(ruler());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				break;

		}
		return super.onOptionsItemSelected(item);
	}
	
	public static Context getContext() {
		return _context;
	}

	public int convertToPix(int val){
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, _context.getResources().getDisplayMetrics());
		return (int)px;

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		super.onPause();
		this.mWakeLock.release();

		sensorMgr.unregisterListener(this, sensorGrav);
		sensorMgr.unregisterListener(this, sensorMag);
		sensorMgr = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.mWakeLock.acquire();


		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

		sensors = sensorMgr.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensors.size() > 0) {
			sensorGrav = sensors.get(0);
		}

		sensors = sensorMgr.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		if (sensors.size() > 0) {
			sensorMag = sensors.get(0);
		}

		sensorMgr.registerListener(this, sensorGrav, SensorManager.SENSOR_DELAY_NORMAL);
		sensorMgr.registerListener(this, sensorMag, SensorManager.SENSOR_DELAY_NORMAL);
		
		int orientation = this.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
		    //code for portrait mode
			ORIENTATION_CAP = 90;
		} else {
		    //code for landscape mode
			ORIENTATION_CAP = 0;
		}
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent evt) {


		if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			gravSensorVals = lowPass(evt.values.clone(), gravSensorVals);
			grav[0] = evt.values[0];
			grav[1] = evt.values[1];
			grav[2] = evt.values[2];

		} else if (evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			magSensorVals = lowPass(evt.values.clone(), magSensorVals);
			mag[0] = evt.values[0];
			mag[1] = evt.values[1];
			mag[2] = evt.values[2];

		}

		if (gravSensorVals != null && magSensorVals != null) {
			SensorManager.getRotationMatrix(RTmp, I, gravSensorVals, magSensorVals);

			int rotation = Compatibility.getRotation(this);

			if (rotation == 1) {
				SensorManager.remapCoordinateSystem(RTmp, SensorManager.AXIS_X, SensorManager.AXIS_MINUS_Z, Rot);
			} else {
				SensorManager.remapCoordinateSystem(RTmp, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_Z, Rot);
			}

			SensorManager.getOrientation(Rot, results);

			ARView.azimuth = (float)(((results[0]*180)/Math.PI)+180 + ORIENTATION_CAP);
			ARView.pitch = (float) 87.5;//(float)(((results[1]*180/Math.PI))+90);
			ARView.roll = (float)(((results[2]*180/Math.PI)));

			float rollval = (float)(((results[2]*180/ Math.PI)));
			if(rollval > 140 && rollval < 180)
				ARView.pitch = (float) 87.0;//(float)(((results[2]*180/ Math.PI)));
			else if(rollval > -180 && rollval < -160)
				ARView.pitch = (float) 87.0;
			else
				ARView.pitch = 0;
			
			radarMarkerView.postInvalidate();
			
			//*************************************compass*****************************
            // east degrees of true North
			bearing = results[0];
            // convert from radians to degrees
            bearing = Math.toDegrees(bearing);

           
            // bearing must be in 0-360
            if (bearing < 0) {
                bearing += 360;
            }
            //Log.e("", bearing + "<Bearing results[0]>" + results[0]);
            // update compass view
            //compassView.setBearing((float) bearing - 90);
            //compassView.postInvalidate();
            
            //******************************************end compass*****************
		}
	}

	protected float[] lowPass( float[] input, float[] output ) {
		if ( output == null ) return input;

		for ( int i=0; i<input.length; i++ ) {
			output[i] = output[i] + ALPHA * (input[i] - output[i]);
		}
		return output;
	}	
}

class CameraView extends SurfaceView implements SurfaceHolder.Callback {

	ARView arView;
	SurfaceHolder holder;
	Camera camera;

	public CameraView(Context context) {
		super(context);
		arView = (ARView) context;

		holder = getHolder();
		holder.addCallback(this);
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		try {
			camera.setDisplayOrientation(ARView.ORIENTATION_CAP);
			Camera.Parameters parameters = camera.getParameters();
			try {
				List<Camera.Size> supportedSizes = null;
				supportedSizes = Compatibility.getSupportedPreviewSizes(parameters);

				Iterator<Camera.Size> itr = supportedSizes.iterator(); 
				while(itr.hasNext()) {
					Camera.Size element = itr.next(); 
					element.width -= w;
					element.height -= h;
				} 
				Collections.sort(supportedSizes, new ResolutionsOrder());
				//parameters.setPreviewSize(w, h);
				parameters.setPreviewSize(w + supportedSizes.get(supportedSizes.size()-1).width, h + supportedSizes.get(supportedSizes.size()-1).height);
				//Log.e("","previews1 " + parameters.getPreviewSize().width + "x" + parameters.getPreviewSize().height + " " + w + "x" + h);
			} catch (Exception ex) {
				//parameters.setPreviewSize(arView.screenWidth , arView.screenHeight);
				//Log.e("", "previews " + ex.getMessage());
			}

			camera.setParameters(parameters);
			camera.startPreview();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (camera != null) {
				try {
					camera.stopPreview();
				} catch (Exception ignore) {
				}
				try {
					camera.release();
				} catch (Exception ignore) {
				}
				camera = null;
			}

			camera = Camera.open();
			arView.camera = camera;
			camera.setPreviewDisplay(holder);
		} catch (Exception ex) { System.out.println(ex.getMessage() + "<< Camera Exc");
			try {
				if (camera != null) {
					try {
						camera.stopPreview();
					} catch (Exception ignore) {
					}
					try {
						camera.release();
					} catch (Exception ignore) {
					}
					camera = null;
				}
			} catch (Exception ignore) {

			}
		}		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (camera != null) {
				try {
					camera.stopPreview();
				} catch (Exception ignore) {
				}
				try {
					camera.release();
				} catch (Exception ignore) {
				}
				camera = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

}

class RadarMarkerView extends View {

	ARView arView;
	DisplayMetrics displayMetrics;
	RelativeLayout upperLayoutView = null;
	public RadarMarkerView(Context context, DisplayMetrics displayMetrics, RelativeLayout rel) {
		super(context);

		arView = (ARView) context;
		this.displayMetrics = displayMetrics;
		upperLayoutView = rel;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		ARView.paintScreen.setWidth(canvas.getWidth());
		ARView.paintScreen.setHeight(canvas.getHeight());
		ARView.paintScreen.setCanvas(canvas);
		//Log.e("", ARView.ORIENTATION_CAP + "");

		//ARView.paintScreen.setBearing((float) ARView.bearing - 90);//portrait
		//ARView.paintScreen.setBearing((float) ARView.bearing -90 -90);//landscape
		int mode = 0;
		if(ARView.ORIENTATION_CAP == 90) 
			mode = 0;
		else mode = 90;
		ARView.paintScreen.setBearing((float) ARView.bearing - 90 - mode);
		
		if (!ARView.nearByMeDataView.isInited()) {
			ARView.nearByMeDataView.init(ARView.paintScreen.getWidth(), ARView.paintScreen.getHeight(), arView.camera, displayMetrics,upperLayoutView);
			//ARView.nearByMeDataView.setDistance(9);
		}

		ARView.nearByMeDataView.draw(ARView.paintScreen, ARView.azimuth, ARView.pitch, ARView.roll);
		

		
	}
}

class ResolutionsOrder implements java.util.Comparator<Camera.Size> {
	public int compare(Camera.Size left, Camera.Size right) {

		return Float.compare(left.width + left.height, right.width + right.height);
	}
}