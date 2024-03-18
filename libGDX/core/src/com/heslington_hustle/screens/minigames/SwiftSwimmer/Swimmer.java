package com.heslington_hustle.screens.minigames.SwiftSwimmer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.heslington_hustle.game.HeslingtonHustle;

public class Swimmer {
	private HeslingtonHustle game;
	public int laps;
	public float x;
	public float y;
	public int speed;
	private TextureRegion currentFrame;
	private int frame;
	private int clickCount;
	
	public Swimmer(HeslingtonHustle game) {
		laps = 0;
		this.game = game;
		this.speed = -50; //Starts in change boundary at side
		this.x = 0;
		this.y = 200;
		this.frame = 0;
		this.clickCount = 0;
	}
	
	public void Swim() {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
			boundaryChecks();
			this.x += this.speed;
			SwiftSwimmer.splash.play(game.volume);
			clickCount++;
			if(frame == 0) frame = 1;
			else frame = 0;
		}
		drawSprite();
		
	}
	public void drawSprite() {
		game.batch.begin();
		currentFrame = SwiftSwimmer.swimAnimation.getKeyFrame(frame, true);
		if(speed < 0 && clickCount != 0){
			game.batch.draw(currentFrame, x+15, y, -currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		}
		else {
			game.batch.draw(currentFrame, x+15, y, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
		}
		game.batch.end();
	}
	private void boundaryChecks() {
		if(this.x > 550 || this.x < 50){
			speed = -speed;
			laps += 1;	
		}
		
	}

}

