package com.heslington_hustle.screens;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;

public class MinigameScreen implements Screen {

	public HeslingtonHustle game;
	public float energyChange;
	public float difficultyScalar;
	public boolean exam;
	
	// Parent class of minigames
	public MinigameScreen(HeslingtonHustle game, float difficultyScalar){
		this.game = game;
		this.difficultyScalar = difficultyScalar;
		this.energyChange = 100; // Default - should be modified in each individual minigame
	}
	
	public void startGame() {
		// Should be overrided in specific minigames
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
