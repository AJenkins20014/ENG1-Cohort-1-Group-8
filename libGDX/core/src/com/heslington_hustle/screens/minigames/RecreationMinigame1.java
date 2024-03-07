package com.heslington_hustle.screens.minigames;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.MinigameScreen;

public class RecreationMinigame1 extends MinigameScreen implements Screen {
	
	private float energyGained;
	private float maxEnergyGained;

	public RecreationMinigame1(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 80f; // From best possible performance
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
	}
	
	@Override
	public void render(float delta) {
		
	}
	
	@Override
	public void dispose() {
		
	}

}
