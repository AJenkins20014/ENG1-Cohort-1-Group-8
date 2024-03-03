package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.Player;

public class Map implements Screen{
	
	private HeslingtonHustle game;
	public Player player;
	
	public Map (HeslingtonHustle game) {
		this.game = game;
		player = new Player(new Texture("placeholderCharacter.png"));
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0.65f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		game.batch.begin();
		
		game.batch.draw(player.sprite, player.x, player.y);
		
		game.batch.end();
		
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			player.moveUp();
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			player.moveLeft();
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			player.moveDown();
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			player.moveRight();
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
