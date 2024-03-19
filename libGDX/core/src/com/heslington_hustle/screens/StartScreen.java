package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;

public class StartScreen implements Screen {

	private HeslingtonHustle game;
	
	public StartScreen (HeslingtonHustle game) {
		this.game = game;
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
		
		
		// Start drawing
		game.batch.begin();

		// Draw logo
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Heslington", 470, 330, 140, Align.right, false);
		game.font.draw(game.batch, "Hustle", 470, 275, 140, Align.right, false);
		
		// Draw high scores - TODO: Add high scores for other minigames
		game.font.getData().setScale(0.4f); // Set font size
		game.font.setColor(new Color(232/255f, 193/255f, 112/255f, 1));
		game.font.draw(game.batch, "High Score: " + Integer.toString(game.prefs.getInteger("highScore", 0)), 470, 190, 140, Align.right, false);
		game.font.draw(game.batch, "BugFixer High Score: " + Integer.toString(game.prefs.getInteger("bugFixerHighScore", 0)), 470, 160, 140, Align.right, false);
		game.font.draw(game.batch, "BookStacker High Score: " + Integer.toString(game.prefs.getInteger("bookStackerHighScore", 0)), 470, 130, 140, Align.right, false);
		game.font.draw(game.batch, "SwiftSwimmer High Score: " + Integer.toString(game.prefs.getInteger("swiftSwimmerHighScore", 0)), 470, 100, 140, Align.right, false);
		game.font.draw(game.batch, "DrunkDancer High Score: " + Integer.toString(game.prefs.getInteger("drunkDancerHighScore", 0)), 470, 70, 140, Align.right, false);
		game.font.draw(game.batch, "Squash High Score: " + Integer.toString(game.prefs.getInteger("squashHighScore", 0)), 470, 40, 140, Align.right, false);
		
		// Draw buttons
		game.font.getData().setScale(0.8f); // Set font size
		game.font.setColor(new Color(1, 1, 1, 1));
		
		// Play button
		game.layout.setText(game.font, "Play", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 10 + game.layout.width && mousePos.x > 10 && mousePos.y < 220 && mousePos.y > 220 - game.layout.height) {
			game.layout.setText(game.font, "Play", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Play button clicked
				game.menuClick.play(game.volume);
				loadCharacterSelect();
			}
		}
		game.font.draw(game.batch, game.layout, 10, 220);
		
		// Options button
		game.layout.setText(game.font, "Options", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 10 + game.layout.width && mousePos.x > 10 && mousePos.y < 160 && mousePos.y > 160 - game.layout.height) {
			game.layout.setText(game.font, "Options", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Options button clicked
				game.menuClick.play(game.volume);
				loadOptions();
			}
		}
		game.font.draw(game.batch, game.layout, 10, 160);
		
		// About button
		game.layout.setText(game.font, "About", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 10 + game.layout.width && mousePos.x > 10 && mousePos.y < 100 && mousePos.y > 100 - game.layout.height) {
			game.layout.setText(game.font, "About", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// About button clicked
				game.menuClick.play(game.volume);
				Gdx.net.openURI("https://nicholaslambert03.github.io/ENG1Website.io/"); // Opens link to website
				resize(0, 0); // Minimises game window
			}
		}
		game.font.draw(game.batch, game.layout, 10, 100);
		
		// Exit button
		game.layout.setText(game.font, "Exit", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		if(mousePos.x < 10 + game.layout.width && mousePos.x > 10 && mousePos.y < 40 && mousePos.y > 40 - game.layout.height) {
			game.layout.setText(game.font, "Exit", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Exit button clicked
				game.menuClick.play(game.volume);
				exit();
			}
		}
		game.font.draw(game.batch, game.layout, 10, 40);
		
		// Stop drawing
		game.batch.end();

	}
	
	private void loadCharacterSelect() {
		game.setScreen(new CharacterSelectScreen(game));
	}
	
	private void loadOptions() {
		game.setScreen(new OptionsScreen(game));
	}
	
	private void exit() {
		Gdx.app.exit();
	}

	@Override
	public void dispose() {
		
	}
	
	@Override
	public void show() {
		// Called when this screen becomes displayed
		game.menuMusic.play();
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
}
