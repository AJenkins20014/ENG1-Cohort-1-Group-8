package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.MinigameScreen;

public class Building extends Object {

	public MinigameScreen minigame;
	public float requiredEnergy;
	public int timeSpent;
	
	public Building(HeslingtonHustle game, String name, Rectangle[] interactRegions, int screen, String tooltip, int tooltipX, int tooltipY, int nameX, int nameY, MinigameScreen minigameScreen, Float requiredEnergy, int timeSpent) {
		super(game, name, interactRegions, screen, tooltip, tooltipX, tooltipY, nameX, nameY);
		this.minigame = minigameScreen;
		this.requiredEnergy = requiredEnergy;
		this.timeSpent = timeSpent;
	}
	
	public void interact() {
		// Check if player has enough time
		if (game.time + timeSpent > 24){
			game.addPopUpText(new PopUpText("It's too late to do this now!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		// Check if player has enough energy
		else if(game.energyBar.energy < requiredEnergy) {
			game.addPopUpText(new PopUpText("Insufficient Energy!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		// Subtract energy, add time and start minigame
		else {
			game.energyBar.addEnergy(-requiredEnergy);
			game.time += timeSpent;
			
			// Set minigame difficulty
			minigame.difficultyScalar = game.timesStudied[game.day-1]*0.25f + 1f;
			minigame.exam = false;
			minigame.startGame();
		}
	}
}