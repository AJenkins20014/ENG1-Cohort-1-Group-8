package com.heslington_hustle.game;

import com.badlogic.gdx.graphics.Texture;

public class Building extends Object {

	public Minigame minigame;
	public float requiredEnergy;
	
	public Building(HeslingtonHustle game, String name, Texture sprite, float x, float y, Minigame minigame) {
		super(game, name, sprite, x, y);
		this.minigame = minigame;
	}
	
	public void interact() {
		
	}

}
