package views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ScaleView extends View {

	Paint paint = new Paint();
	Paint textpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private int width = 0;
	private int height = 0;
	private int seekbarpoints = 110;
	
	public ScaleView(Context context) {
		super(context);
	}
	
	public ScaleView(Context context, AttributeSet foo) {
		super(context, foo);
        
        paint.setColor(Color.WHITE); //Set the colour to red
        paint.setStyle(Paint.Style.STROKE); //set the style
        paint.setStrokeWidth(5); //Stoke width
        
        textpaint.setColor(Color.rgb(225, 225, 225));// text color RGB
        textpaint.setTextSize(30);// text size
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    width = MeasureSpec.getSize(widthMeasureSpec);
	    height = MeasureSpec.getSize(heightMeasureSpec);
	    setMeasuredDimension(width, 100);	    
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
        float point = 50; //initiate the point variable

        //Start a for loop that will loop seekbarpoints number of times.
        /*for (float i = 0.5f; i < seekbarpoints; i+=0.5f){

            if(i == 0.5f)
					canvas.drawText(Float.toString(i), point, 95, textpaint);
				else
					canvas.drawText(Float.toString(i), point - 20, 95, textpaint);

            if(i==0.5f || i==1.5f || i==2.5f || i==3.5f || i==4.5f || i==5.5f) {
					//short line
					canvas.drawLine(point, 60, point, 30, paint);
					point = point  + seekbarpoints;

            }else  if( i==1.0f || i==2.0f || i==3.0f || i==4.0f || i==5.0f) {
					//long line

					canvas.drawLine(point, 60, point, 0, paint);
					point = point  + seekbarpoints;
            }
        }*/
        
        float indwidth = width / 10;
        for (float i = indwidth, j = 0.5f; i <= width; i += indwidth, j += 0.5) {
        	if(j==0.5f || j==1.5f || j==2.5f || j==3.5f || j==4.5f || j==5.5f) 
        		canvas.drawLine(i - (indwidth / 2), 60, i - (indwidth / 2), 30, paint);
    		else
    			canvas.drawLine(i - (indwidth / 2), 60, i - (indwidth / 2), 0, paint);
        	canvas.drawText(Float.toString(j), i - (indwidth / 1.5f), 95, textpaint);
		}
	}
}
