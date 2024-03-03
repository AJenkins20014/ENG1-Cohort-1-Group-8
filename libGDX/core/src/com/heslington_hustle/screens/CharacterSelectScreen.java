package com.heslington_hustle.screens;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;

public class CharacterSelectScreen implements Screen{
	HeslingtonHustle game;
	
	public CharacterSelectScreen(HeslingtonHustle game) {
		this.game = game;
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		// Temporary (instantly loads map)
		this.dispose();
		game.setScreen(new Map(game));
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
