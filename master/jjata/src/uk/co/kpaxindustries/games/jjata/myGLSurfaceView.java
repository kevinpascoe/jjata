package uk.co.kpaxindustries.games.jjata;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class myGLSurfaceView extends GLSurfaceView {

	public myGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setRenderer(new myRenderer());
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
	}
	
	public myGLSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		setRenderer(new myRenderer());
		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

}
