package com.heslington_hustle.screens.minigames.SwiftSwimmer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class Swimmer {
	private HeslingtonHustle game;
	public float x;
	public float y;
	public Texture sprite;
	public int speed;
	
	
	public Swimmer(HeslingtonHustle game) {
		this.sprite = new Texture("SwiftSwimmerMinigame/swimmer.png");
		this.speed = 50;
		this.x = 50;
		this.y = 50;
	}
	
	public void Swim() {
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
			this.x += this.speed;
		}
		drawSprite();
		
	}
	public void drawSprite() {
		// TODO Auto-generated method stub
		System.out.println(game.batch);
		game.batch.begin();
		game.batch.draw(sprite,x,y);
		game.batch.end();
	}


}

