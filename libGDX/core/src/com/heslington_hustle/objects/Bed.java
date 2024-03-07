package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class Bed extends Object {
	
	public Bed(HeslingtonHustle game, String name, Texture sprite, float x, float y, int screen, String tooltip) {
		super(game, name, sprite, x, y, screen, tooltip);
	}
	
	public void startNewDay() {
		// Adds less energy the later you sleep
		if(game.time == 24) {
			game.energyBar.addEnergy(40f);
		}
		else if(game.time > 22) {
			game.energyBar.addEnergy(60f);
		}
		else if(game.time > 20) {
			game.energyBar.addEnergy(80f);
		}
		
		game.time = 8; // Set time to 8am
		game.day++; // Cycle day
	}

}
