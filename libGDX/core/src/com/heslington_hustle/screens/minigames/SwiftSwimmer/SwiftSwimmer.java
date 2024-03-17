package com.heslington_hustle.screens.minigames.SwiftSwimmer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class SwiftSwimmer extends MinigameScreen implements Screen {
	private float clock; 
	private boolean minimised;
	private float energyGained;
	private float maxEnergyGained;
	private int score;
	public Swimmer swimmer;

	public SwiftSwimmer(HeslingtonHustle game, float difficultyScalar) {
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
		
		
		// Display tutorial - TODO: create a tutorial in InformationScreen and rename the string in the constructor below to fit
	    game.setScreen(new InformationScreen(game,"swiftSwimmerTutorial", this));
	    
	    //Declares a swimmer
		swimmer = new Swimmer(game);
		
		// Temporary - TODO: REMOVE
	}
	
	private void endGame() {
		game.energyBar.addEnergy(energyGained);
		score = swimmer.laps*75;
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
		
		// Display final score
		game.setScreen(new InformationScreen(game, "recreationGameScore", game.map, score, energyGained));
		
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
		//Increment delta time
		clock += Gdx.graphics.getDeltaTime();
		//Move Swimmer
		swimmer.Swim();
		game.batch.begin();
		//Move Swimmer
		if(clock > 15) {
			endGame();
		}
		
		
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
