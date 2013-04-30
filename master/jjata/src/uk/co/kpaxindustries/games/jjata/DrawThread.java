package uk.co.kpaxindustries.games.jjata;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {

	static final String TAG = "DrawThread";
	boolean mRun;
	Canvas mCanvas;
	SurfaceHolder surfaceHolder;
	Context context;
	DrawView mDrawView;
	
	public DrawThread(SurfaceHolder sholder, Context ctx, DrawView drawview)
	{
		surfaceHolder = sholder;
		context = ctx;
		mRun = false;
		mDrawView = drawview;
	}
	
	void setRunning(boolean bRun)
	{
		mRun = bRun;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while(mRun)
		{
			mCanvas = surfaceHolder.lockCanvas();
			if(mCanvas != null)
			{
				mDrawView.doDraw(mCanvas);
				surfaceHolder.unlockCanvasAndPost(mCanvas);
			}
		}
	}
}
