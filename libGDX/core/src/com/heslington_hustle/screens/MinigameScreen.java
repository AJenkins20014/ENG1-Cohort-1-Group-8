package com.heslington_hustle.screens;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;

public class MinigameScreen implements Screen {

	public HeslingtonHustle game;
	public float energyChange;
	public float difficultyScalar;
	
	public MinigameScreen(HeslingtonHustle game, float difficultyScalar){
		this.game = game;
		this.difficultyScalar = difficultyScalar;
		this.energyChange = 100; // Default - should be modified in each individual minigame
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
