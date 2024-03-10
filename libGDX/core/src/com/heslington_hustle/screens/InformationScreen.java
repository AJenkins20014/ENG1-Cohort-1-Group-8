package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;

public class InformationScreen implements Screen{
	private HeslingtonHustle game;
	public String type;
	private Screen nextScreen;
	
	public InformationScreen(HeslingtonHustle game, String type, Screen nextScreen) {
		this.game = game;
		this.type = type;
		this.nextScreen = nextScreen;
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
		
		if(type == "mainTutorial") {
			loadMainTutorial();
		}
		else if(type == "bugFixerTutorial") {
			loadBugFixerTutorial();
		}
		
		// Continue
		game.font.getData().setScale(0.6f); // Set font size
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.layout.setText(game.font, "Continue!", new Color(1, 1, 1, 1), 100, Align.center, false);
		if(mousePos.x < 250 + game.layout.width && mousePos.x > 250 - game.layout.width && mousePos.y < 40 + game.layout.height && mousePos.y > 40 - game.layout.height) {
			game.layout.setText(game.font, "Continue!", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.center, false);
			if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
				// Button clicked
				game.menuClick.play(game.volume);
				game.setScreen(nextScreen);
			}
		}
		game.font.draw(game.batch, game.layout, 250, 40);
		
		game.batch.end();
	}
	
	public void loadMainTutorial() {
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "You wake up after a long night out. You glance at your phone and realise the date - only 1 week before exams start! "
				+ "You really shouldn't have left it so late again! Oh well, late is better than never, you rush out of the door with determination, time to get to work!", 50, 300, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialMove.png"), 60, 90);
		game.font.draw(game.batch, "Move", 150, 110, 100, Align.left, false);
		
		game.batch.draw(new Texture("UI/TutorialInteract.png"), 350, 105);
		game.font.draw(game.batch, "Interact", 390, 110, 100, Align.left, false);
	}
	
	public void loadBugFixerTutorial() {
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "BugFixer", 250, 330, 100, Align.center, false);
		game.font.getData().setScale(0.4f);
		game.font.setColor(new Color(1, 1, 1, 1));	
		game.font.draw(game.batch, "Blast the bugs and survive to get points!", 50, 260, 500, Align.center, true);
		
		game.font.getData().setScale(0.5f);
		game.batch.draw(new Texture("UI/TutorialMove.png"), 60, 90);
		game.font.draw(game.batch, "Move", 150, 110, 100, Align.left, false);
		
		game.batch.draw(new Texture("UI/TutorialLeftClick.png"), 380, 90);
		game.font.draw(game.batch, "Shoot", 420, 110, 100, Align.left, false);
	}

	@Override
	public void resize(int width, int height) {
		
	}
	
	@Override
	public void show() {
		game.menuMusic.play();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		game.menuMusic.pause();
	}

	@Override
	public void dispose() {
		
	}

}
