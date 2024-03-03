package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.Building;
import com.heslington_hustle.game.EnergyBar;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.Minigame;
import com.heslington_hustle.game.Player;

public class Map implements Screen{
	
	private HeslingtonHustle game;
	public Texture background;
	public Object[] objects = new Object[6];
	
	public Map (HeslingtonHustle game) {
		this.game = game;
		game.player = new Player(new Texture("placeholderCharacter.png"));
		game.energyBar = new EnergyBar(new Texture("placeholderCharacter.png"), 100f);
		game.day = 1;
		this.background = new Texture("placeholderCharacter.png");
		
		// Add objects to array
		objects[0] = new Building(game, "test building", new Texture("placeholderCharacter.png"), 30, 30, game.minigames[0]);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0.65f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.begin();
		
		game.batch.draw(game.player.sprite, game.player.x, game.player.y);
		
		game.batch.end();
		
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			game.player.moveUp();
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			game.player.moveLeft();
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			game.player.moveDown();
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			game.player.moveRight();
		}
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
