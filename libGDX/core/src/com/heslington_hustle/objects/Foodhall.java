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
			if(game.timesEatenToday == 0) {
				game.energyBar.addEnergy(energyChange);
				game.time += timeSpent;
				game.addPopUpText(new PopUpText("You feel well fed", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
			}
			// If already eaten once today, gain a bit less energy
			else if(game.timesEatenToday == 1) {
				game.energyBar.addEnergy(energyChange-15f);
				game.time += timeSpent;
				game.addPopUpText(new PopUpText("You feel full", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
			}
			// If already eaten twice today, gain a lot less energy
			else if(game.timesEatenToday == 2) {
				game.energyBar.addEnergy(energyChange-30f);
				game.time += timeSpent;
				game.addPopUpText(new PopUpText("You feel bloated...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
			}
			// If already eaten 3 of more times today, lose energy instead
			else {
				game.energyBar.addEnergy(-20f);
				game.time += timeSpent;
				game.addPopUpText(new PopUpText("You start to feel sick...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
			}
			game.timesEatenToday++;
		}
	}

}
