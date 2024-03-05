package com.heslington_hustle.screens;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;

public class GameOverScreen implements Screen {

	private HeslingtonHustle game;
	private float score;
	private String grade;
	
	public GameOverScreen(HeslingtonHustle game) {
		this.game = game;
	}
	
	private void calculateScore() {
		// Take all scores gathered from studying throughout the 7 day period
		// Add that to the final exam score
		// Calculate and display final score
		// Calculate final grade based on score
	}
	
	private void returnToMenu(){
		// Check in game.prefs("User Data") if score/grade is higher than current high score/grade. If so, update high score/grade
		// game.prefs.flush(); // Save high score/grade
		
		// Return to main menu
		game.setScreen(new StartScreen(game));
	}
	
	@Override
	public void render(float delta) {
		
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
			
	}

}
