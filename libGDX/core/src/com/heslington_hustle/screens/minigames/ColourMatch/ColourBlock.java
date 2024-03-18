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
	
	public ColourBlock(HeslingtonHustle game,Colour colour,float x,float y) {
		this.game = game;
		this.colour = colour;
		this.x = x;
		this.y = y;
		switch(this.colour) {
			case RED: this.sprite = new Texture("ColourMatchMinigame/red.png");
			case GREEN:this.sprite = new Texture("ColourMatchMinigame/green.png");
			case BLUE:this.sprite = new Texture("ColourMatchMinigame/blue.png");
			case YELLOW:this.sprite = new Texture("ColourMatchMinigame/yellow.png");
		}
		drawSprite();
	}
	public void drawSprite() {
		// TODO Auto-generated method stub
		game.batch.begin();
		game.batch.draw(sprite,x,y);
		game.batch.end();
	}

}
	
