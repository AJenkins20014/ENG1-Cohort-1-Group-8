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
		this.speed = 200;
		this.x = game.camera.viewportWidth/2;
		this.y = game.camera.viewportHeight/2;
	}
	
	public void moveUp() {
		if(y < game.camera.viewportHeight - sprite.getHeight()) {
			y += speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 1) {
			// If player was on bottom left, scroll camera to top left
			y = 0 + sprite.getHeight();
			game.map.screen = 3;
		}
		else if(game.map.screen == 2) {
			// If player was on bottom right, scroll camera to top right
			y = 0 + sprite.getHeight();
			game.map.screen = 4;
		}
	}
	
	public void moveLeft() {
		if(x > 0) {
			x -= speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 2) {
			// If player was on bottom right, scroll camera to bottom left
			x = game.camera.viewportWidth - sprite.getWidth();
			game.map.screen = 1;
		}
		else if(game.map.screen == 4) {
			// If player was on top right, scroll camera to top left
			x = game.camera.viewportWidth - sprite.getWidth();
			game.map.screen = 3;
		}
	}
	
	public void moveDown() {
		if(y > 0) {
			y -= speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 3) {
			// If player was on top left, scroll camera to bottom left
			y = game.camera.viewportHeight - sprite.getHeight();
			game.map.screen = 1;
		}
		else if(game.map.screen == 4) {
			// If player was on top right, scroll camera to bottom right
			y = game.camera.viewportHeight - sprite.getHeight();
			game.map.screen = 2;
		}
	}
	
	public void moveRight() {
		if(x < game.camera.viewportWidth - sprite.getWidth()) {
			x += speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 1) {
			// If player was on bottom left, scroll camera to bottom right
			x = 0 + sprite.getWidth();
			game.map.screen = 2;
		}
		else if(game.map.screen == 3) {
			// If player was on top left, scroll camera to top right
			x = 0 + sprite.getWidth();
			game.map.screen = 4;
		}
	}
}
