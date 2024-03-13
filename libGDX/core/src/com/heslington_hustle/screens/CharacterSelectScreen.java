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
	public Texture[] idleRSheets = new Texture[4];
	public Texture[] idleLSheets = new Texture[4];
	public Texture[] walkRSheets = new Texture[4];
	public Texture[] walkLSheets = new Texture[4];
	private int avatarIndex;
	
	public CharacterSelectScreen(HeslingtonHustle game) {
		this.game = game;
		
		avatarIndex = 0;
		
		// Load avatar textures
		avatars[0] = new Texture("Avatars/Avatar1.png");
		avatars[1] = new Texture("Avatars/Avatar2.png");
		avatars[2] = new Texture("Avatars/Avatar3.png");
		avatars[3] = new Texture("Avatars/Avatar4.png");
		
		idleRSheets[0] = new Texture("Avatars/Avatar1_IdleR.png");
		idleRSheets[1] = new Texture("Avatars/Avatar2_IdleR.png");
		idleRSheets[2] = new Texture("Avatars/Avatar3_IdleR.png");
		idleRSheets[3] = new Texture("Avatars/Avatar4_IdleR.png");
		
		idleLSheets[0] = new Texture("Avatars/Avatar1_IdleL.png");
		idleLSheets[1] = new Texture("Avatars/Avatar2_IdleL.png");
		idleLSheets[2] = new Texture("Avatars/Avatar3_IdleL.png");
		idleLSheets[3] = new Texture("Avatars/Avatar4_IdleL.png");
		
		walkRSheets[0] = new Texture("Avatars/Avatar1_WalkR.png");
		walkRSheets[1] = new Texture("Avatars/Avatar2_WalkR.png");
		walkRSheets[2] = new Texture("Avatars/Avatar3_WalkR.png");
		walkRSheets[3] = new Texture("Avatars/Avatar4_WalkR.png");
		
		walkLSheets[0] = new Texture("Avatars/Avatar1_WalkL.png");
		walkLSheets[1] = new Texture("Avatars/Avatar2_WalkL.png");
		walkLSheets[2] = new Texture("Avatars/Avatar3_WalkL.png");
		walkLSheets[3] = new Texture("Avatars/Avatar4_WalkL.png");
	}

	@Override
	public void show() {
		// Called when this screen becomes displayed
		game.menuMusic.play();
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
				
		
		// Begin drawing on screen
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
				game.menuClick.play(game.volume);
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
					game.menuClick.play(game.volume);
					avatarIndex--;
				}
			}
			else {
				game.layout.setText(game.font, "<", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 30, 100);
			}
		}
		
		if(avatarIndex < 3) {
			if(mousePos.x < 200 + avatars[avatarIndex].getWidth()/2 && mousePos.x > 170 + avatars[avatarIndex].getWidth()/2 && mousePos.y < 100 && mousePos.y > 100 - game.layout.height) {
				game.layout.setText(game.font, ">", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 170 + avatars[avatarIndex].getWidth()/2, 100);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Next Avatar
					game.menuClick.play(game.volume);
					avatarIndex++;
				}
			}
			else {
				game.layout.setText(game.font, ">", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 170 + avatars[avatarIndex].getWidth()/2, 100);
			}
		}
		
		game.layout.setText(game.font, "Back", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 20 + game.layout.width && mousePos.x > 20 && mousePos.y < 50 && mousePos.y > 50 - game.layout.height) {
			game.layout.setText(game.font, "Back", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Back to main menu
				game.menuClick.play(game.volume);
				game.setScreen(new StartScreen(game));
			}
		}
		game.font.draw(game.batch, game.layout, 20, 50);
		
		// Stop drawing on screen
		game.batch.end();
	}
	
	private void newGame() {
		game.player = new Player(game, avatars[avatarIndex], idleRSheets[avatarIndex], idleLSheets[avatarIndex], walkRSheets[avatarIndex], walkLSheets[avatarIndex]);
		game.energyBar = new EnergyBar(100f);
		game.day = 1;
		game.time = 8;
		game.setScreen(new InformationScreen(game, "mainTutorial", new Map(game, 3)));
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
		// Called when this screen stops being displayed
		game.menuMusic.pause();
	}

	@Override
	public void dispose() {
		
	}
}
