package com.heslington_hustle.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class StartScreen implements Screen {

	HeslingtonHustle game;
	
	Texture gameLogo;
	
	private float gameLogoWidth;
	private float gameLogoHeight;
	
	private float playButtonWidth;
	private float playButtonHeight;
	private float optionsButtonWidth;
	private float optionsButtonHeight;
	private float aboutButtonWidth;
	private float aboutButtonHeight;
	private float exitButtonWidth;
	private float exitButtonHeight;
	
	private float gameLogoX;
	private float gameLogoY;
	
	private float menuButtonX;
	private float playButtonY;
	private float optionsButtonY;
	private float aboutButtonY;
	private float exitButtonY;
	
	Texture playButtonActive;
	Texture playButtonInactive;
	Texture optionsButtonActive;
	Texture optionsButtonInactive;
	Texture aboutButtonActive;
	Texture aboutButtonInactive;
	Texture exitButtonActive;
	Texture exitButtonInactive;
	
	
	public StartScreen (HeslingtonHustle game) {
		this.game = game;
		
		gameLogo = new Texture("Logo.png");
		
		playButtonActive = new Texture("playActive.png");
		playButtonInactive = new Texture("playInactive.png");
		optionsButtonActive = new Texture("optionsActive.png");
		optionsButtonInactive = new Texture("optionsInactive.png");
		aboutButtonActive = new Texture("aboutActive.png");
		aboutButtonInactive = new Texture("aboutInactive.png");
		exitButtonActive = new Texture("exitActive.png");
		exitButtonInactive = new Texture("exitInactive.png");
		
		
		gameLogoWidth = gameLogo.getWidth()*HeslingtonHustle.pixelArtScalar;
		gameLogoHeight = gameLogo.getHeight()*HeslingtonHustle.pixelArtScalar;
		
		playButtonWidth = playButtonActive.getWidth()*HeslingtonHustle.pixelArtScalar;
		playButtonHeight = playButtonActive.getHeight()*HeslingtonHustle.pixelArtScalar;
		optionsButtonWidth = optionsButtonActive.getWidth()*HeslingtonHustle.pixelArtScalar;
		optionsButtonHeight = optionsButtonActive.getHeight()*HeslingtonHustle.pixelArtScalar;
		aboutButtonWidth = aboutButtonActive.getWidth()*HeslingtonHustle.pixelArtScalar;
		aboutButtonHeight = aboutButtonActive.getHeight()*HeslingtonHustle.pixelArtScalar;
		exitButtonWidth = exitButtonActive.getWidth()*HeslingtonHustle.pixelArtScalar;
		exitButtonHeight = exitButtonActive.getHeight()*HeslingtonHustle.pixelArtScalar;
		
		gameLogoX = 200*HeslingtonHustle.pixelArtScalar;
		gameLogoY = 220*HeslingtonHustle.pixelArtScalar;
		
		menuButtonX = 10*HeslingtonHustle.pixelArtScalar;
		playButtonY = 190*HeslingtonHustle.pixelArtScalar;
		optionsButtonY = 130*HeslingtonHustle.pixelArtScalar;
		aboutButtonY = 70*HeslingtonHustle.pixelArtScalar;
		exitButtonY = 10*HeslingtonHustle.pixelArtScalar;
	}
	
	@Override
	public void show() {
	
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		game.batch.begin();
		
		game.batch.draw(gameLogo, gameLogoX, gameLogoY, gameLogoWidth, gameLogoHeight);
		
		if(Gdx.input.getX() < menuButtonX + playButtonWidth && Gdx.input.getX() > menuButtonX && HeslingtonHustle.windowHeight - Gdx.input.getY() < playButtonY + playButtonHeight && HeslingtonHustle.windowHeight - Gdx.input.getY() > playButtonY) {
			game.batch.draw(playButtonActive, menuButtonX, playButtonY, playButtonWidth, playButtonHeight);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				// Play button clicked
				startGame();
			}
		}
		else {
			game.batch.draw(playButtonInactive, menuButtonX, playButtonY, playButtonWidth, playButtonHeight);
		}
		
		if(Gdx.input.getX() < menuButtonX + optionsButtonWidth && Gdx.input.getX() > menuButtonX && HeslingtonHustle.windowHeight - Gdx.input.getY() < optionsButtonY + optionsButtonHeight && HeslingtonHustle.windowHeight - Gdx.input.getY() > optionsButtonY) {
			game.batch.draw(optionsButtonActive, menuButtonX, optionsButtonY, optionsButtonWidth, optionsButtonHeight);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				// Options button clicked
			}
		}
		else {
			game.batch.draw(optionsButtonInactive, menuButtonX, optionsButtonY, optionsButtonWidth, optionsButtonHeight);
		}
		
		if(Gdx.input.getX() < menuButtonX + aboutButtonWidth && Gdx.input.getX() > menuButtonX && HeslingtonHustle.windowHeight - Gdx.input.getY() < aboutButtonY + aboutButtonHeight && HeslingtonHustle.windowHeight - Gdx.input.getY() > aboutButtonY) {
			game.batch.draw(aboutButtonActive, menuButtonX, aboutButtonY, aboutButtonWidth, aboutButtonHeight);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				// About button clicked
			}
		}
		else {
			game.batch.draw(aboutButtonInactive, menuButtonX, aboutButtonY, aboutButtonWidth, aboutButtonHeight);
		}
		
		if(Gdx.input.getX() < menuButtonX + exitButtonWidth && Gdx.input.getX() > menuButtonX && HeslingtonHustle.windowHeight - Gdx.input.getY() < exitButtonY + exitButtonHeight && HeslingtonHustle.windowHeight - Gdx.input.getY() > exitButtonY) {
			game.batch.draw(exitButtonActive, menuButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				// exit button clicked
				exit();
			}
		}
		else {
			game.batch.draw(exitButtonInactive, menuButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
		}
		
		
		game.batch.end();

	}
	
	private void startGame() {
		//this.dispose();
		game.setScreen(new CharacterSelectScreen(game));
	}
	
	private void exit() {
		Gdx.app.exit();
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
