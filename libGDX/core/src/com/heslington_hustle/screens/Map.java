package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.objects.Building;
import com.heslington_hustle.objects.Object;

public class Map implements Screen{
	
	private HeslingtonHustle game;
	public Object[] objects = new Object[6];

	public Map(HeslingtonHustle game) {
		this.game = game;
		game.map = this;
		
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
		
		// Draw UI elements
		drawUI();
		
		// Check for whether the player has touched an object
		checkPlayerObjectCollision();
		
		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}
		
		game.batch.end();
		
		if(!game.paused) {
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
	}
	
	private void initialiseObjects() {
		objects[0] = new Building(game, "CS Building", new Texture("Objects/PlaceholderBuilding.png"), 100, 100, "E: Study", game.minigames[0], 25f);
		// etc...
	}
	
	private void drawUI() {
		// Draw energy bar
		game.batch.draw(new Texture("UI/EnergyBar1.png"), 10, 308);
		TextureRegion region = new TextureRegion(new Texture("UI/EnergyBar2.png"), (int)Math.round(284f*(game.energyBar.energy/100f)), new Texture("UI/EnergyBar2.png").getHeight());
		game.batch.draw(region, 10, 308);
		game.batch.draw(new Texture("UI/EnergyBar3.png"), 10, 308);
		
		game.font.getData().setScale(0.25f); // Set font size
		game.font.draw(game.batch, Integer.toString((int)Math.round(game.energyBar.energy)), -17, 328, 100, Align.center, false);
	}
	
	private void checkPlayerObjectCollision() {
		for(int i = 0; i < objects.length; i++) {
			if(objects[i] != null) {
				if(game.player.x + game.player.sprite.getWidth()/2 > objects[i].x && game.player.x - game.player.sprite.getWidth() < (objects[i].x + objects[i].sprite.getWidth()/2) && 
						game.player.y > (objects[i].y - objects[i].sprite.getHeight()/2) && game.player.y - game.player.sprite.getHeight()/2 < (objects[i].y + objects[i].sprite.getHeight()/2)){
					// Draw tooltips
					if(objects[i] instanceof Building) {
						game.font.getData().setScale(0.3f); // Set font size
						game.font.draw(game.batch, objects[i].tooltip, objects[i].x, objects[i].y + objects[i].sprite.getHeight() + 50, objects[i].sprite.getWidth(), Align.center, false);
						
						game.font.getData().setScale(0.5f); // Set font size
						game.font.setColor(new Color(164/255f, 221/255f, 219/255f, 1));
						
						Texture energyIcon = new Texture("UI/EnergyIconEmpty.png");
						if(game.energyBar.energy >= ((Building) objects[i]).requiredEnergy){
							energyIcon = new Texture("UI/EnergyIcon.png");
						}
						
						game.batch.draw(energyIcon, objects[i].x + objects[i].sprite.getWidth()/6, objects[i].y + objects[i].sprite.getHeight() + 5);
						game.font.draw(game.batch, Integer.toString((int)Math.round(((Building) objects[i]).requiredEnergy)),
								objects[i].x + objects[i].sprite.getWidth()/6 + energyIcon.getWidth() + 10, objects[i].y + objects[i].sprite.getHeight() + energyIcon.getHeight(),
								20, Align.left, false);
						
						game.font.getData().setScale(0.3f); // Set font size
						game.font.setColor(new Color(1, 1, 1, 1));
						game.font.draw(game.batch, objects[i].name, objects[i].x, objects[i].y - 10, objects[i].sprite.getWidth(), Align.center, false);
					}
					else {
						game.font.getData().setScale(0.5f); // Set font size
						game.font.draw(game.batch, objects[i].tooltip, objects[i].x, objects[i].y + objects[i].sprite.getHeight()*1.5f, objects[i].sprite.getWidth(), Align.center, false);
						game.font.draw(game.batch, objects[i].name, objects[i].x, objects[i].y - objects[i].sprite.getHeight()/2, objects[i].sprite.getWidth(), Align.center, false);
					}
					
					// Check if interact key (E) is pressed
					if(Gdx.input.isKeyJustPressed(Keys.E)) {
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
