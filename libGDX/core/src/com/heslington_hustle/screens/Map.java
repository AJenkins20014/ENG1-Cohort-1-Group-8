package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.EnergyBar;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.Player;
import com.heslington_hustle.objects.Building;
import com.heslington_hustle.objects.Object;

public class Map implements Screen{
	
	private HeslingtonHustle game;
	public Texture background;
	public Object[] objects = new Object[6];
	
	public Map (HeslingtonHustle game) {
		this.game = game;
		game.player = new Player(game, new Texture("PlaceholderCharacter.png"));
		game.energyBar = new EnergyBar(new Texture("PlaceholderCharacter.png"), 100f);
		game.day = 1;
		this.background = new Texture("PlaceholderCharacter.png");
		
		// Add objects to array
		initialiseObjects();
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(70/255f, 130/255f, 50/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
				
		
		game.batch.begin();
		
		// Draw objects
		for(int i = 0; i < objects.length; i++) {
			if(objects[i] != null) {
				game.batch.draw(objects[i].sprite, objects[i].x, objects[i].y);
			}
		}
		
		// Draw player
		game.batch.draw(game.player.sprite, game.player.x, game.player.y);
		
		// Check for whether the player has touched an object
		checkPlayerObjectCollision();

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
	
	private void initialiseObjects() {
		objects[0] = new Building(game, "CS Building", new Texture("PlaceholderBuilding.png"), 100, 100, "E: Study", game.minigames[0]);
		// etc...
	}
	
	private void checkPlayerObjectCollision() {
		for(int i = 0; i < objects.length; i++) {
			if(objects[i] != null) {
				if(game.player.x + game.player.sprite.getWidth()/2 > (objects[i].x - objects[i].sprite.getWidth()/2) && game.player.x - game.player.sprite.getWidth()/2 < (objects[i].x + objects[i].sprite.getWidth()/2) && 
						game.player.y + game.player.sprite.getHeight()/2 > (objects[i].y - objects[i].sprite.getHeight()/2) && game.player.y - game.player.sprite.getHeight()/2 < (objects[i].y + objects[i].sprite.getHeight()/2)){
					// Draw tooltips
					game.font.getData().setScale(0.5f); // Set font size
					game.font.draw(game.batch, objects[i].tooltip, objects[i].x, objects[i].y + objects[i].sprite.getHeight()*1.5f, objects[i].sprite.getWidth(), Align.center, false);
					game.font.draw(game.batch, objects[i].name, objects[i].x, objects[i].y - objects[i].sprite.getHeight()/2, objects[i].sprite.getWidth(), Align.center, false);
					
					// Check if interact key (E) is pressed
					if(Gdx.input.isKeyPressed(Keys.E)) {
						if(objects[i] instanceof Building) {
							((Building) objects[i]).interact();
						}
						// else if...
						
					}
				}
			}
		}
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
