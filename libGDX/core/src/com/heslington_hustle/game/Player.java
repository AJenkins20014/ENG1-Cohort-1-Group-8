package com.heslington_hustle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Player {
	
	private HeslingtonHustle game;
	
	public Texture sprite;
	public float speed;
	public float x;
	public float y;
	
	public Player (HeslingtonHustle game, Texture sprite) {
		this.game = game;
		this.sprite = sprite;
		this.speed = 400;
		this.x = game.camera.viewportWidth/2;
		this.y = game.camera.viewportHeight/2;
	}
	
	public void moveUp() {
		if(y < game.camera.viewportHeight - sprite.getHeight()) {
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
		if( x < game.camera.viewportWidth - sprite.getWidth()) {
			x += speed*Gdx.graphics.getDeltaTime();
		}
	}
}
