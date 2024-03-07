package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;

public class Foodhall extends Object {
	public Float energyChange;
	public int timeSpent;
	
	public Foodhall(HeslingtonHustle game, String name, Texture sprite, float x, float y, int screen, String tooltip, Float energyChange, int timeSpent) {
		super(game, name, sprite, x, y, screen, tooltip);
		this.energyChange = energyChange;
		this.timeSpent = timeSpent;
	}
	
	public void eat() {
		// Check if player has enough time
		if (game.time + timeSpent > 24){
			game.addPopUpText(new PopUpText("It's too late to do this now!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		// Add energy and time
		else {
			game.energyBar.addEnergy(energyChange);
			game.time += timeSpent;
		}
	}

}
