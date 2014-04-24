package com.example.killthemall;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Picture;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView {
	
	private Bitmap bmp;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	//private Sprite sprite; //for only one sprite
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private List<TempSprite> temps = new ArrayList<TempSprite>();
	private long lastClick;
	/*private int x = 0;
	private int xspeed = 1;*/		//removed once we added sprite logic
	private Bitmap bmpBlood;
	private Bitmap bmpBG;
	public int score;
	public int combo;
	Paint scoreFont = new Paint();
	private ProgressDialog dialog;

	
	public GameView(Context context) {
		super(context);
		bmpBG = BitmapFactory.decodeResource(getResources(), R.drawable.snow_bg2);
		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				createSprites();
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});
		/*Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sprite_1);
		sprite = new Sprite(this, bmp); */  //for only one sprite
		bmpBlood = BitmapFactory.decodeResource(getResources(), R.drawable.blue_sparkle_md);
	}
	
	private void createSprites() {
		sprites.add(createSprite(R.drawable.sprite_1_big));
		sprites.add(createSprite(R.drawable.sprite_1_big));
		sprites.add(createSprite(R.drawable.sprite_1_big));
		sprites.add(createSprite(R.drawable.sprite_1_big));

		sprites.add(createSprite(R.drawable.sprite_2_big));
		sprites.add(createSprite(R.drawable.sprite_2_big));
		sprites.add(createSprite(R.drawable.sprite_2_big));
		sprites.add(createSprite(R.drawable.sprite_2_big));
		
		sprites.add(createSprite(R.drawable.sprite_3_big));
		sprites.add(createSprite(R.drawable.sprite_3_big));
		sprites.add(createSprite(R.drawable.sprite_3_big));
		sprites.add(createSprite(R.drawable.sprite_3_big));
	}
	
	private Sprite createSprite(int resource) {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
		return new Sprite(this, bmp);
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		//bmpBG = BitmapFactory.decodeResource(getResources(), R.drawable.snow_bg);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bmpBG, 0, 0, new Paint());
		/*if (x< getWidth()-bmp.getWidth()) { 	//icon stops at the border
			x++;
		} */
		/*if (x == getWidth()-bmp.getWidth()) { 	//icon bounces against borders
			xspeed = -1;						//removed once we added sprite logic
		}
		if (x == 0) {
			xspeed = 1;
		}
		x = x + xspeed;
		canvas.drawBitmap(bmp, x, 10, null); */
		for (int i = temps.size() - 1; i > 0; i--) {	//bloodstain. written first so it overwrites the sprite
			temps.get(i).onDraw(canvas);
		}
		for (Sprite sprite : sprites) {		//sprites
			sprite.onDraw(canvas);
		}

		scoreFont.setColor(Color.MAGENTA);	//score
		scoreFont.setTextSize(50);
		scoreFont.setStrokeWidth(4);
		//canvas.drawText(Integer.toString(score), 50,100, scoreFont);
		canvas.drawText("Score: "+score, 50,100, scoreFont);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	//called every time you touch the screen
		if (System.currentTimeMillis() - lastClick > 300) {		//give it a delay for the same reason as the break
			lastClick = System.currentTimeMillis();		
			synchronized (getHolder()) {	//synchronized allows this thread to be run the same time as it's pair in GameLoopThread
				float x=event.getX();
				float y=event.getY();
				for (int i = sprites.size()-1; i >= 0; i--) {
					Sprite sprite = sprites.get(i);
					if(sprite.isCollition(x, y)) {
						score ++;
						sprites.remove(sprite);		//kill the sprite touched
						temps.add(new TempSprite(temps, this, x, y, bmpBlood));		//add bloodstain
						//break;  		//the break insures that we only kill the top one touched and stop killing others in that same spot
						combo ++;
						if(score >= 4) {
							CharSequence tit = "Sarah-Kate Magee...";
							CharSequence mes = "Will You Marry Me?";
							dialog = ProgressDialog.show(getContext(), tit, mes);
							//dialog = findViewById(R.layout.loading_layout);
							dialog.show();
						}
					}
				}
			}
		}
		return true;
	}
	
	
}
