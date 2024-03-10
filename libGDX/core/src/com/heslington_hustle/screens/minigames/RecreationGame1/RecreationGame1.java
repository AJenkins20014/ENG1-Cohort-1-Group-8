package com.heslington_hustle.screens.minigames.RecreationGame1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.MinigameScreen;

public class RecreationGame1 extends MinigameScreen implements Screen {
	private boolean minimised;
	private float energyGained;
	private float maxEnergyGained;

	public RecreationGame1(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 60f; // From best possible performance
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void startGame() {
		// Code to restart the game
		energyGained = 20f; // From worst possible performance
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
				
		// Display game screen
		game.setScreen(this);
		
		
		// Temporary - TODO: REMOVE
		endGame();
	}
	
	private void endGame() {
		game.energyBar.addEnergy(energyGained);
		
		/*
		// Check minigame high score
		if(game.prefs.getInteger("thisMinigameHighScore", 0) < score) {
			game.prefs.putInteger("thisMinigameHighScore", score);
			game.prefs.flush();
		}
		*/
		
		if(energyGained > maxEnergyGained) {
			energyGained = maxEnergyGained;
		}
		
		// Reset window back to borderless
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
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
			game.addPopUpText(new PopUpText("You feel extremely well rested!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		
	}
	
	@Override
	public void render(float delta) {
		if(minimised) return;
		
		// Clear the screen
		Gdx.gl.glClearColor(16/255f, 20/255f, 31/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
		
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
		
		
		game.batch.begin();
		
		
		// CODE TO RENDER SPRITES GOES HERE
		
		
		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}
		
		game.batch.end();
		
				
		if(game.paused) return;
		// Anything that shouldn't happen while the game is paused should go here
	}
	
	@Override
	public void resize(int width, int height) {
		if(width == 0 && height == 0) {
			minimised = true;
			game.togglePause();
		}
		else {
			minimised = false;
		}
	}
	
	@Override
	public void dispose() {
		
	}

}