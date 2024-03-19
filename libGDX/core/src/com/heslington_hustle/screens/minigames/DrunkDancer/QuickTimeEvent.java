/**
 * Represents a QuickTimeEvent in the Drunk Dancer minigame.
 * QuickTimeEvents are displayed on the screen and require player input.
 */
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
	
	/**
     * Constructs a QuickTimeEvent with the specified game instance, DrunkDancer instance, input key, and speed.
     * @param game The main game instance.
     * @param drunkDancer The DrunkDancer instance.
     * @param input The input key code associated with the event.
     * @param speed The speed at which the event moves downward.
     */
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
	
	/**
     * Updates the position of the QuickTimeEvent.
     * Called every frame in the DrunkDancer render method.
     */
	public void update() {
		move();
		drawSprite();
	}
	
	/**
     * Moves the QuickTimeEvent downward.
     */
	private void move() {
		y -= speed*Gdx.graphics.getDeltaTime();
	}
	
	/**
     * Draws the QuickTimeEvent sprite.
     */
	private void drawSprite() {
		game.batch.draw(sprite, x, y);
	}
	
	/**
     * Removes the QuickTimeEvent from the parent DrunkDancer's list of active events.
     */
	public void destroy() {
		drunkDancer.QTEs.remove(this);
	}
}
