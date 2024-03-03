package com.heslington_hustle.objects;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class Building extends Object {

	public Screen minigameScreen;
	public float requiredEnergy;
	
	public Building(HeslingtonHustle game, String name, Texture sprite, float x, float y, String tooltip, Screen minigameScreen) {
		super(game, name, sprite, x, y, tooltip);
		this.minigameScreen = minigameScreen;
	}
	
	public void interact() {
		game.setScreen(minigameScreen);
	}

}
