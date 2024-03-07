package com.heslington_hustle.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.MinigameScreen;

public class Building extends Object {

	public MinigameScreen minigame;
	public float requiredEnergy;
	
	public Building(HeslingtonHustle game, String name, Texture sprite, float x, float y, int screen, String tooltip, MinigameScreen minigameScreen, Float requiredEnergy) {
		super(game, name, sprite, x, y, screen, tooltip);
		this.minigame = minigameScreen;
		this.requiredEnergy = requiredEnergy;
	}
	
	public void interact() {
		if(game.energyBar.energy >= requiredEnergy) {
			game.energyBar.addEnergy(-requiredEnergy);
			//minigame.difficultyScalar = ??? // Set minigame difficulty
			minigame.startGame();
		}
		else {
			game.addPopUpText(new PopUpText("Insufficient Energy!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
	}

}
