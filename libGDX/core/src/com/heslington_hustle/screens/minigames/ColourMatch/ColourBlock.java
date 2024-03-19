/**
 * The ColourBlock class represents a colored block in the Colour Match minigame.
 * Each block has a specific color and position on the screen.
 */
package com.heslington_hustle.screens.minigames.ColourMatch;

import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.minigames.ColourMatch.ColourMatch.Colour;

public class ColourBlock {
	private HeslingtonHustle game;
	public Colour colour;
	public float x;
	public float y;
	public Texture sprite;
	
	/**
     * Constructs a new ColourBlock object with the specified game instance, color, x-coordinate, and y-coordinate.
     * @param game The HeslingtonHustle game instance.
     * @param colour The color of the block.
     * @param x The x-coordinate of the block's position.
     * @param y The y-coordinate of the block's position.
     */
	public ColourBlock(HeslingtonHustle game,Colour colour,float x,float y) {
		this.game = game;
		this.colour = colour;
		this.x = x;
		this.y = y;
		//Assigns texture based on colour property
		switch(this.colour) {
			case RED: this.sprite = new Texture("ColourMatchMinigame/red.png");break;
			case GREEN:this.sprite = new Texture("ColourMatchMinigame/green.png");break;
			case BLUE:this.sprite = new Texture("ColourMatchMinigame/blue.png");break;
			case YELLOW:this.sprite = new Texture("ColourMatchMinigame/yellow.png");break;
		}
	}
	
	/**
     * Draws the block's sprite on the screen.
     */
	public void drawSprite() {
		game.batch.begin();
		game.batch.draw(sprite,x,y);
		game.batch.end();
	}
	
	/**
     * Disposes of the block's sprite texture to release resources.
     */
	public void kill() {
		this.sprite.dispose();
	}
	
	/**
     * Checks if the specified mouse coordinates are within the bounds of the block.
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     * @return True if the mouse is within the bounds of the block, otherwise false.
     */
	public Boolean inBounds(float mouseX, float mouseY) {
		//Checks if mouse clicks on itself
		if(mouseX>=this.x && mouseX <=this.x+100 && mouseY>=this.y && mouseY <=this.y+100) {
			return true;
		}
		return false;
	}
}
	
