package uk.co.kpaxindustries.games.jjata;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends SurfaceView implements OnTouchListener,SurfaceHolder.Callback
{

	static final String TAG = "DrawView";

	private Path mPath;
	private Bitmap mBitmap;
	private Bitmap mScaledBG;
	private Bitmap mScaledLetter;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private Canvas mCanvas;

	private Point point;
	private List<Point> mPoints = new ArrayList<Point>();

	private Context mContext;
	private DrawThread mDrawThread;

	public DrawView(Context ctx, AttributeSet attributeSet) {
		super(ctx,attributeSet);

		point = new Point();
		mPaint = new Paint();
		mContext = ctx;
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		mPaint.setColor(Color.RED);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(30);
		mPaint.setDither(true);

		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//Scale Background
		Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		float hscale = (float)background.getHeight()/(float)getHeight();
		float wscale = (float)background.getWidth()/(float)getWidth();
		int newWidth = Math.round(background.getWidth()/wscale);
		int newHeight = Math.round(background.getHeight()/hscale);
		mScaledBG = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);

		// Scale Letter
		background = BitmapFactory.decodeResource(getResources(), R.drawable.a);
		hscale = (float)background.getHeight()/(float)getHeight();
		wscale = (float)background.getWidth()/(float)getWidth();
		newWidth = Math.round(background.getWidth()/wscale);
		newHeight = Math.round(background.getHeight()/hscale);
		mScaledLetter = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);

		mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

		mDrawThread = new DrawThread(holder, mContext, this);
		mDrawThread.setRunning(true);
		mDrawThread.start();
		//Log.d(TAG,"Surface Created");
	}

	void doDraw(Canvas canvas)
	{
		// Draw Background First
		canvas.drawBitmap(mScaledBG, 0,0, null);
		canvas.drawBitmap(mScaledLetter, 0,0, null);

		canvas.drawBitmap(mBitmap, 0, 0 , mBitmapPaint);

		canvas.drawPath(mPath, mPaint);
	}

	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		
		// Log the Point
		point.set((int)mX, (int)mY);
		mPoints.add(point);
	}
	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
		}
		
		// Log the Point
		point.set((int)mX, (int)mY);
		mPoints.add(point);
	}
	private void touch_up() {
		mPath.lineTo(mX, mY);
		// commit the path to our offscreen
		mCanvas.drawPath(mPath, mPaint);
		// kill this so we don't double draw
		mPath.reset();
		
		// Log the Point
		point.set((int)mX, (int)mY);
		mPoints.add(point);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch(event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG,"Touching");
			touch_start(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			break;
		case MotionEvent.ACTION_UP:
			Log.d(TAG,"Not Touching");
			touch_up();
		}
		Log.d(TAG,"X:" + String.valueOf(event.getX(event.getActionIndex())) + ",Y:" + String.valueOf(event.getY(event.getActionIndex())));
		return true;
	}



	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mDrawThread.setRunning(false);
		boolean retry = true;
		while(retry)
		{
			try
			{
				mDrawThread.join();
				retry = false;
			} catch(Exception e)
			{
				Log.v(TAG, e.getMessage());
			}
		}
	}






}
