package com.heslington_hustle.screens.minigames.SwiftSwimmer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class Swimmer {
	private HeslingtonHustle game;
	public int laps;
	public float x;
	public float y;
	public Texture sprite;
	public int speed;
	
	
	public Swimmer(HeslingtonHustle game) {
		laps = 0;
		this.game = game;
		this.sprite = new Texture("SwiftSwimmerMinigame/swimmer.png");
		this.speed = -50; //Starts in change boundary at side
		this.x = 0;
		this.y = 200;
	}
	
	public void Swim() {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
			boundaryChecks();
			this.x += this.speed;
		}
		drawSprite();
		
	}
	public void drawSprite() {
		// TODO Auto-generated method stub
		game.batch.begin();
		game.batch.draw(sprite,x,y);
		game.batch.end();
	}
	private void boundaryChecks() {
		if(this.x > 550|| this.x <50){
			speed = -speed;
			laps += 1;	
		}
		
	}

}

