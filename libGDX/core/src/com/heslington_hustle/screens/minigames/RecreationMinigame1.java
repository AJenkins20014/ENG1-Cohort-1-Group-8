package com.heslington_hustle.screens.minigames;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.MinigameScreen;

public class RecreationMinigame1 extends MinigameScreen implements Screen {
	
	private float energyGained;
	private float maxEnergyGained;

	public RecreationMinigame1(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 60f; // From best possible performance
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void startGame() {
		// Code to restart the game
		energyGained = 20f; // From worst possible performance
		
		// Display game screen
		game.setScreen(this);
		
		// Temporary - TODO REMOVE
		endGame();
	}
	
	private void endGame() {
		game.energyBar.addEnergy(energyGained);
		game.setScreen(game.map);
		
		// TODO - Change accordingly
		if(energyGained < 30f) {
			game.addPopUpText(new PopUpText("You don't feel very well rested...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(energyGained < 40f) {
			game.addPopUpText(new PopUpText("You feel slightly well rested", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(energyGained < 50f) {
			game.addPopUpText(new PopUpText("You feel well rested", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(energyGained < 60f) {
			game.addPopUpText(new PopUpText("You feel very well rested", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else {
			game.addPopUpText(new PopUpText("You feel extremely well rested", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		
	}
	
	@Override
	public void render(float delta) {
		
	}
	
	@Override
	public void dispose() {
		
	}

}
