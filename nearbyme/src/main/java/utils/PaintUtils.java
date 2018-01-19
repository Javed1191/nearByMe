package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.nearbyme.NearByMeRadarView;
import com.nearbyme.R;

public class PaintUtils {
	
	public static int XPADDING = 0;
	public static int YPADDING = 50;
	
	Canvas canvas;
	int width, height;
	Typeface typeface2;
	public Paint paint = new Paint();
	Paint bPaint = new Paint();

	public PaintUtils(Context context) {
		paint.setTextSize(16);
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		paint.setStyle(Paint.Style.STROKE);
		
		matrix = new Matrix();matrixbck = new Matrix();
		bitmap = BitmapFactory.decodeResource(context.getResources(),
			      R.drawable.compass_icon);
		bitmapbck = BitmapFactory.decodeResource(context.getResources(),
			      R.drawable.compass_iconbck);

		int w = (int) ((NearByMeRadarView.RADIUS + 10) * 2.5);

		bitmap = Bitmap.createScaledBitmap(bitmap, w, w, true);
		bitmapbck = Bitmap.createScaledBitmap(bitmapbck,w, w, true);
		
		/*bitmap = Bitmap.createScaledBitmap(bitmap,
		        (int) ((NearByMeRadarView.RADIUS + 10) * 2.5), (int) ((NearByMeRadarView.RADIUS +10) * 2.5), true);
		bitmapbck = Bitmap.createScaledBitmap(bitmapbck,
		        (int) ((NearByMeRadarView.RADIUS + 10) * 2.5), (int) ((NearByMeRadarView.RADIUS + 10) * 2.5), true);*/
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setFill(boolean fill) {
		if (fill)
			paint.setStyle(Paint.Style.FILL);
		else
			paint.setStyle(Paint.Style.STROKE);
	}

	public void setColor(int c) {
		paint.setColor(c);
	}

	public void setStrokeWidth(float w) {
		paint.setStrokeWidth(w);
	}

	public void paintLine(float x1, float y1, float x2, float y2) {
		canvas.drawLine(x1+XPADDING, y1+YPADDING, x2+XPADDING, y2+YPADDING, paint);
	}

	public void paintRect(float x, float y, float width, float height) {
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	public void paintCircle(float x, float y, float radius, NearByMeRadarView nearByMeRadarView) {
		canvas.drawCircle(x, y, radius, paint);
	}

	public void paintText(float x, float y, String text) {
		canvas.drawText(text, x, y, paint);
	}

	public void paintObj(NearByMeRadarView obj, float x, float y, float rotation,
                         float scale, float yaw) {
		canvas.save();
		canvas.translate(x + obj.getWidth() / 2, y + obj.getHeight() / 2);
		canvas.rotate(rotation);
		canvas.scale(scale, scale);
		canvas.translate(-(obj.getWidth() / 2), -(obj.getHeight() / 2));
		obj.paint(this, yaw);
		canvas.restore();
	}

	public float getTextWidth(String txt) {
		return paint.measureText(txt);
	}

	public float getTextAsc() {
		return -paint.ascent();
	}

	public float getTextDesc() {
		return paint.descent();
	}

	public void setFontSize(float size) {
		paint.setTextSize(size);
	}
	
	public void setFontStyle(int typeface){
		typeface2 = Typeface.create(Typeface.DEFAULT, typeface);
		paint.setTypeface(typeface2);
	}
	
	private Bitmap bitmap, bitmapbck;
	private Matrix matrix, matrixbck;
	private float bearing;
	public void setBearing(float b) {
		bearing = b;
		
		drawCompass();
	}
	
	public void drawCompass()
	{
		int bitmapX = bitmap.getWidth() / 2;
	    int bitmapY = bitmap.getHeight() / 2;
	    int centerX = PaintUtils.XPADDING + 12;
	    int centerY = PaintUtils.YPADDING + 12;
		 // calculate rotation angle
	    int rotation = (int) (360 - bearing);
	 
	    // reset matrix
	    matrix.reset();
	    matrix.setRotate(rotation, bitmapX, bitmapY);
	    // center bitmap on canvas
	    matrix.postTranslate(centerX, centerY);
	    // draw bitmap
	    canvas.drawBitmap(bitmap, matrix, paint);
	    
	    matrixbck.reset();
	    matrixbck.postTranslate(centerX, centerY);
	    //canvas.drawBitmap(bitmapbck, matrixbck, paint);
	}
}