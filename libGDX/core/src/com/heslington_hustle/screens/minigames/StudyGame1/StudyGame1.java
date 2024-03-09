package com.heslington_hustle.screens.minigames.StudyGame1;

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

public class StudyGame1 extends MinigameScreen implements Screen {
	private boolean minimised;
	private float studyPointsGained;
	private float maxStudyPointsGained;
	
	public StudyGame1(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		this.studyPointsGained = 15f; // From worst possible performance
		this.maxStudyPointsGained = 100f; // From best possible performance
	}
	
	@Override
	public void startGame() {
		// Code to restart the game
		studyPointsGained = 15f;
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		// Display game screen
		game.setScreen(this);
		
		
		//TEMPORARY - TODO: REMOVE
		endGame();
	}
	
	private void endGame() {
		// Calculate final score
		//studyPointsGained += score;
		
		/*
		// Check minigame high score
		if(game.prefs.getInteger("thisMinigameHighScore", 0) < score) {
			game.prefs.putInteger("thisMinigameHighScore", score);
			game.prefs.flush();
		}
		*/
		
		if(studyPointsGained > maxStudyPointsGained) {
			studyPointsGained = maxStudyPointsGained;
		}
		
		// Reset window back to borderless
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		// Add studyPoints score to total score for this minigame
		if(game.studyPoints.containsKey("thisMinigame")) {
			game.studyPoints.put("thisMinigame", (game.studyPoints.get("thisMinigame") + studyPointsGained));
		}
		else {
			game.studyPoints.put("thisMinigame", studyPointsGained);
		}
		
		// Update amount of times studied today
		game.timesStudied[game.day-1] += 1;
		
		
		game.setScreen(game.map);
		
		// TODO - Change accordingly
		if(studyPointsGained < 30f) {
			game.addPopUpText(new PopUpText("You don't feel very productive...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(studyPointsGained < 50f) {
			game.addPopUpText(new PopUpText("You feel slightly productive", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(studyPointsGained < 70f) {
			game.addPopUpText(new PopUpText("You feel productive", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(studyPointsGained < 90f) {
			game.addPopUpText(new PopUpText("You feel very productive", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else {
			game.addPopUpText(new PopUpText("You feel extremely productive!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
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
