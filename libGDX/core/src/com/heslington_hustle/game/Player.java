package com.heslington_hustle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Player {
	
	public Texture sprite;
	public float speed;
	public float x;
	public float y;
	
	public Player (Texture sprite) {
		this.sprite = sprite;
		this.speed = 400;
		this.x = HeslingtonHustle.windowWidth/2;
		this.y = HeslingtonHustle.windowHeight/2;
	}
	
	public void moveUp() {
		if(y < HeslingtonHustle.windowHeight - sprite.getHeight()) {
			y += speed*Gdx.graphics.getDeltaTime();
		}
	}
	
	public void moveLeft() {
		if(x > 0) {
			x -= speed*Gdx.graphics.getDeltaTime();
		}
	}
	
	public void moveDown() {
		if(y > 0) {
			y -= speed*Gdx.graphics.getDeltaTime();
		}
	}
	
	public void moveRight() {
		if( x < HeslingtonHustle.windowWidth - sprite.getWidth()) {
			x += speed*Gdx.graphics.getDeltaTime();
		}
	}
}
