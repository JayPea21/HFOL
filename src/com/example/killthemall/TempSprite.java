package com.example.killthemall;

import java.util.List;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class TempSprite {
	private float x;
	private float y;
	private Bitmap bmp;
	private int life = 15;
	private List<TempSprite> temps;
	
	public TempSprite(List<TempSprite> temps, GameView gameView, float x, float y, Bitmap bmp) {
			//if we did "this.x = x" then the sprite would draw below and to the right from where we clicked, but this way it is drawn from the center of where we clicked
		this.x = Math.min(Math.max(x-bmp.getWidth()/2, 0), gameView.getWidth() - bmp.getWidth());
		this.y = Math.min(Math.max(y-bmp.getWidth()/2, 0), gameView.getWidth() - bmp.getWidth());
		this.bmp = bmp;
		this.temps = temps;
	}
	
	public void onDraw(Canvas canvas) {
		update();
		canvas.drawBitmap(bmp, x, y, null);
	}
	
	private void update() {
		if (--life < 1) {
			temps.remove(this);
		}
	}

}
