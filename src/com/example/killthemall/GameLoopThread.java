package com.example.killthemall;
import android.graphics.Canvas;



public class GameLoopThread extends Thread{
	static final long FPS = 10;
	private GameView view;
	private Boolean running = false;
	
	public GameLoopThread(GameView view) {
		this.view = view;
	}
	
	public void setRunning(boolean run) {
		running  = run;
		
	}
	
	@Override
	public void run() {
		long tickPS = 1000 / FPS;
		long startTime;
		long sleepTime;
		while (running){
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				synchronized(view.getHolder()) {	//synchronized allows this thread to be run the same time as it's pair in GameView
					view.onDraw(c);
				}
			} finally {
				if(c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			sleepTime = tickPS-(System.currentTimeMillis()-startTime);
			
			try {
				if(sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
			} catch (Exception e) {}
		} 
	}
	
}
