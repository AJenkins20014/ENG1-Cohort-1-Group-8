package com.heslington_hustle.screens.minigames.DrunkDancer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class QuickTimeEvent {
	public HeslingtonHustle game;
	private DrunkDancer drunkDancer;
	public int input;
	public float x;
	public float y;
	private float speed;
	private Texture sprite;
	
	public QuickTimeEvent(HeslingtonHustle game, DrunkDancer drunkDancer, int input, float speed) {
		this.game = game;
		this.drunkDancer = drunkDancer;
		this.input = input;
		this.speed = speed;
		this.y = 360;
		
		if(input == Keys.A) {
			this.x = 145f;
			this.sprite = new Texture("DrunkDancerMinigame/LeftArrow.png");
		}
		else if(input == Keys.W) {
			this.x = 240f;
			this.sprite = new Texture("DrunkDancerMinigame/UpArrow.png");
		}
		else if(input == Keys.S) {
			this.x = 318f;
			this.sprite = new Texture("DrunkDancerMinigame/DownArrow.png");
		}
		else if(input == Keys.D) {
			this.x = 416f;
			this.sprite = new Texture("DrunkDancerMinigame/RightArrow.png");
		}
	}
	
	public void update() {
		move();
		drawSprite();
	}
	
	private void move() {
		y -= speed*Gdx.graphics.getDeltaTime();
	}
	
	private void drawSprite() {
		game.batch.draw(sprite, x, y);
	}
	
	public void destroy() {
		drunkDancer.QTEs.remove(this);
	}
}
