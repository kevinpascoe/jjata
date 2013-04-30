package uk.co.kpaxindustries.games.jjata;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;


public class myRenderer implements Renderer {

	@Override
	public void onDrawFrame(GL10 unused) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
 
		
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		// TODO Auto-generated method stub
		GLES20.glViewport(0, 0, width, height);

	}


	@Override
	public void onSurfaceCreated(GL10 arg0,
			javax.microedition.khronos.egl.EGLConfig config) {
		// TODO Auto-generated method stub
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}

}
