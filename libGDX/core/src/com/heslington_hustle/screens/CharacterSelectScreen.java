package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.EnergyBar;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.Player;

public class CharacterSelectScreen implements Screen{
	private HeslingtonHustle game;
	private Texture[] avatars = new Texture[4];
	private int avatarIndex;
	
	public CharacterSelectScreen(HeslingtonHustle game) {
		this.game = game;
		
		avatarIndex = 0;
		avatars[0] = new Texture("Avatars/PlaceholderCharacter.png");
		avatars[1] = new Texture("Avatars/PlaceholderCharacter.png");
		avatars[2] = new Texture("Avatars/PlaceholderCharacter.png");
		avatars[3] = new Texture("Avatars/PlaceholderCharacter.png");
	}

	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(32/255f, 46/255f, 55/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
						
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
				
				
		game.batch.begin();
				
		// Draw menu title
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Character", 470, 330, 140, Align.right, false);
		game.font.draw(game.batch, "Select", 470, 275, 140, Align.right, false);
		
		// Draw buttons
		game.font.getData().setScale(1f); // Set font size
		game.layout.setText(game.font, "Start!", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 350 + game.layout.width && mousePos.x > 350 && mousePos.y < 150 && mousePos.y > 150 - game.layout.height) {
			game.layout.setText(game.font, "Start!", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Start game
				newGame();
			}
		}
		game.font.draw(game.batch, game.layout, 350, 150);
		
		game.font.getData().setScale(0.6f); // Set font size
		game.font.setColor(new Color(1, 1, 1, 1));			
		game.layout.setText(game.font, "Select Avatar", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		game.font.draw(game.batch, game.layout, 20, 220);
		game.batch.draw(avatars[avatarIndex], 100, 130);
		if(avatarIndex > 0) {
			if(mousePos.x < 60 && mousePos.x > 30 && mousePos.y < 100 && mousePos.y > 100 - game.layout.height) {
				game.layout.setText(game.font, "<", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 30, 100);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Previous Avatar
					avatarIndex--;
				}
			}
			else {
				game.layout.setText(game.font, "<", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 30, 100);
			}
		}
		
		if(avatarIndex < 3) {
			if(mousePos.x < 200 + new Texture("Avatars/PlaceholderCharacter.png").getWidth()/2 && mousePos.x > 170 + new Texture("Avatars/PlaceholderCharacter.png").getWidth()/2 && mousePos.y < 100 && mousePos.y > 100 - game.layout.height) {
				game.layout.setText(game.font, ">", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 170 + new Texture("Avatars/PlaceholderCharacter.png").getWidth()/2, 100);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Next Avatar
					avatarIndex++;
				}
			}
			else {
				game.layout.setText(game.font, ">", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 170 + new Texture("Avatars/PlaceholderCharacter.png").getWidth()/2, 100);
			}
		}
		
		game.layout.setText(game.font, "Back", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 20 + game.layout.width && mousePos.x > 20 && mousePos.y < 50 && mousePos.y > 50 - game.layout.height) {
			game.layout.setText(game.font, "Back", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Back to main menu
				game.setScreen(new StartScreen(game));
			}
		}
		game.font.draw(game.batch, game.layout, 20, 50);
		
		game.batch.end();
	}
	
	private void newGame() {
		game.player = new Player(game, avatars[avatarIndex]);
		game.energyBar = new EnergyBar(new Texture("Avatars/PlaceholderCharacter.png"), 100f);
		game.day = 1;
		game.time = 8;
		game.setScreen(new Map(game, 1));
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
