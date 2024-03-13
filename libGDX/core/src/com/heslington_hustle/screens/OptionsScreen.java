package com.heslington_hustle.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;

public class OptionsScreen implements Screen {

	private HeslingtonHustle game;
	
	private int maxScreenWidth;
	private int maxScreenHeight;
	private int resolutionIndex;
	private int windowModeIndex; // 0 = fullscreen, 1 = borderless, 2 = windowed
	private int framerateIndex;
	
	private ArrayList<Integer> resolutionWidths = new ArrayList<>();
	private ArrayList<Integer> resolutionHeights = new ArrayList<>();
	
	private ArrayList<Integer> framerates = new ArrayList<>();

	public OptionsScreen(HeslingtonHustle game) {
		this.game = game;
		this.maxScreenWidth = 1920;
		this.maxScreenHeight = 1080;

		// Add standard display settings to the array
		resolutionWidths.add(320);
		resolutionHeights.add(180);
		resolutionWidths.add(640);
		resolutionHeights.add(360);
		resolutionWidths.add(1280);
		resolutionHeights.add(720);
		resolutionWidths.add(1920);
		resolutionHeights.add(1080);
		
		// Add standard FPS limit values to the array
		framerates.add(30);
		framerates.add(60);
		framerates.add(75);
		framerates.add(120);
		framerates.add(144);
		framerates.add(240);
		
		// Get the current monitor's supported display settings, and add any other supported display settings to the array
		DisplayMode[] displayModes = Gdx.graphics.getDisplayModes();
		for(int i = 0; i < displayModes.length; i++) {
			if(displayModes[i].width > maxScreenWidth && displayModes[i].width%640f == 0 && displayModes[i].height > maxScreenHeight && displayModes[i].height%360f == 0) {
				maxScreenWidth = displayModes[i].width;
				maxScreenHeight = displayModes[i].height;
				resolutionWidths.add(displayModes[i].width);
				resolutionHeights.add(displayModes[i].height);
			}
			if(!framerates.contains(displayModes[i].refreshRate) && displayModes[i].refreshRate > framerates.get(framerates.size()-1)) {
				framerates.add(displayModes[i].refreshRate);
			}
		}
		
		for(int i = 0; i < framerates.size(); i++) {
			if(game.prefs.getInteger("framerate", 60) == framerates.get(i)) {
				framerateIndex = i;
			}
		}
		
		findResolution();
		
		if(game.prefs.getBoolean("fullscreen", true)) {
			windowModeIndex = 0;
		}
		else if(game.prefs.getBoolean("borderless", false)) {
			windowModeIndex = 1;
		}
		else {
			windowModeIndex = 2;
		}
	}
	
	private void findResolution() {
		for(int i = 0; i < resolutionWidths.size(); i++) {
			for(int j = 0; j < resolutionHeights.size(); j++) {
				if(HeslingtonHustle.windowWidth == resolutionWidths.get(i) && HeslingtonHustle.windowHeight == resolutionHeights.get(j)) {
					resolutionIndex = i;
				}
			}
		}
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
		
		// Draw menu title
		game.font.getData().setScale(1.2f); // Set font size
		game.font.setColor(new Color(222/255f, 158/255f, 65/255f, 1));
		game.font.draw(game.batch, "Options", 470, 330, 140, Align.right, false);
		
		// Draw buttons
		game.font.getData().setScale(0.5f); // Set font size
		game.font.setColor(new Color(1, 1, 1, 1));
				
		// Resolution settings
		game.layout.setText(game.font, "Resolution:   " + Integer.toString(HeslingtonHustle.windowWidth) + " x " + Integer.toString(HeslingtonHustle.windowHeight), new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		game.font.draw(game.batch, game.layout, 10, 250);
		
		if(resolutionIndex > 0) {
			if(mousePos.x < 190 && mousePos.x > 170 && mousePos.y < 250 && mousePos.y > 250 - game.layout.height) {
				game.layout.setText(game.font, "<", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 170, 250);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Lower resolution
					game.menuClick.play(game.volume);
					game.batch.end();
					HeslingtonHustle.windowWidth = resolutionWidths.get(resolutionIndex-1);
					HeslingtonHustle.windowHeight = resolutionHeights.get(resolutionIndex-1);
					Gdx.graphics.setWindowedMode(resolutionWidths.get(resolutionIndex-1), resolutionHeights.get(resolutionIndex-1));
					Gdx.graphics.setUndecorated(false);
					windowModeIndex = 2;
					resolutionIndex--;
					game.prefs.putBoolean("fullscreen", false);
					game.prefs.putBoolean("borderless", false);
					return;
				}
			}
			else {
				game.layout.setText(game.font, "<", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 170, 250);
			}
		}
		
		if(resolutionIndex < resolutionWidths.size()-1) {
			if(mousePos.x < 395 && mousePos.x > 365 && mousePos.y < 250 && mousePos.y > 250 - game.layout.height) {
				game.layout.setText(game.font, ">", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 365, 250);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Raise resolution
					game.menuClick.play(game.volume);
					game.batch.end();
					HeslingtonHustle.windowWidth = resolutionWidths.get(resolutionIndex+1);
					HeslingtonHustle.windowHeight = resolutionHeights.get(resolutionIndex+1);
					Gdx.graphics.setUndecorated(false);
					Gdx.graphics.setWindowedMode(resolutionWidths.get(resolutionIndex+1), resolutionHeights.get(resolutionIndex+1));
					windowModeIndex = 2;
					resolutionIndex++;
					game.prefs.putBoolean("fullscreen", false);
					game.prefs.putBoolean("borderless", false);
					return;
				}
			}
			else {
				game.layout.setText(game.font, ">", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 365, 250);
			}
		}
		
		
		// Fullscreen settings
		if(windowModeIndex == 0) {
			game.layout.setText(game.font, "Window mode: Fullscreen", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
			game.font.draw(game.batch, game.layout, 10, 200);
			if(mousePos.x < game.layout.width && mousePos.x > 10 && mousePos.y < 200 && mousePos.y > 200 - game.layout.height) {
				game.layout.setText(game.font, "Window mode: Fullscreen", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 200);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Change to borderless mode
					game.menuClick.play(game.volume);
					game.batch.end();
					Gdx.graphics.setUndecorated(true);
					Gdx.graphics.setWindowedMode(resolutionWidths.get(resolutionIndex), resolutionHeights.get(resolutionIndex));
					findResolution();
					windowModeIndex = 1;
					game.prefs.putBoolean("fullscreen", false);
					game.prefs.putBoolean("borderless", true);
					return;
				}
			}
			else {
				game.layout.setText(game.font, "Window mode: Fullscreen", new Color(1, 1,1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 200);
			}
		}
		else if(windowModeIndex == 1){
			game.layout.setText(game.font, "Window mode: Borderless", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
			game.font.draw(game.batch, game.layout, 10, 200);
			if(mousePos.x < game.layout.width && mousePos.x > 10 && mousePos.y < 200 && mousePos.y > 200 - game.layout.height) {
				game.layout.setText(game.font, "Window mode: Borderless", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 200);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Change to windowed mode
					game.menuClick.play(game.volume);
					game.batch.end();
					Gdx.graphics.setUndecorated(false);
					Gdx.graphics.setWindowedMode(resolutionWidths.get(resolutionIndex), resolutionHeights.get(resolutionIndex));
					findResolution();
					windowModeIndex = 2;
					game.prefs.putBoolean("fullscreen", false);
					game.prefs.putBoolean("borderless", false);
					return;
				}
			}
			else {
				game.layout.setText(game.font, "Window mode: Borderless", new Color(1, 1,1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 200);
			}
		}
		else if(windowModeIndex == 2){
			game.layout.setText(game.font, "Window mode: Windowed", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
			game.font.draw(game.batch, game.layout, 10, 200);
			if(mousePos.x < game.layout.width && mousePos.x > 10 && mousePos.y < 200 && mousePos.y > 200 - game.layout.height) {
				game.layout.setText(game.font, "Window mode: Windowed", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 200);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Change to fullscreen mode
					game.menuClick.play(game.volume);
					game.batch.end();
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
					HeslingtonHustle.windowWidth = Gdx.graphics.getDisplayMode().width;
					HeslingtonHustle.windowHeight = Gdx.graphics.getDisplayMode().height;
					findResolution();
					windowModeIndex = 0;
					game.prefs.putBoolean("fullscreen", true);
					game.prefs.putBoolean("borderless", false);
					return;
				}
			}
			else {
				game.layout.setText(game.font, "Window mode: Windowed", new Color(1, 1,1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 200);
			}
		}
		
		// Framerate settings
		game.layout.setText(game.font, "Framerate:   " + Integer.toString(framerates.get(framerateIndex)), new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		game.font.draw(game.batch, game.layout, 10, 150);
		if(framerateIndex > 0) {
			if(mousePos.x < 185 && mousePos.x > 165 && mousePos.y < 150 && mousePos.y > 150 - game.layout.height) {
				game.layout.setText(game.font, "<", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 165, 150);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Lower framerate
					game.menuClick.play(game.volume);
					game.batch.end();
					Gdx.graphics.setForegroundFPS(framerates.get(framerateIndex-1));
					game.prefs.putInteger("framerate", framerates.get(framerateIndex-1));
					framerateIndex--;
					return;
				}
			}
			else {
				game.layout.setText(game.font, "<", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 165, 150);
			}
		}
		
		if(framerateIndex < framerates.size()-1) {
			if(mousePos.x < 280 && mousePos.x > 250 && mousePos.y < 150 && mousePos.y > 150 - game.layout.height) {
				game.layout.setText(game.font, ">", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 250, 150);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Raise framerate
					game.menuClick.play(game.volume);
					game.batch.end();
					Gdx.graphics.setForegroundFPS(framerates.get(framerateIndex+1));
					game.prefs.putInteger("framerate", framerates.get(framerateIndex+1));
					framerateIndex++;
					return;
				}
			}
			else {
				game.layout.setText(game.font, ">", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 250, 150);
			}
		}
		
		// Volume settings
		game.layout.setText(game.font, "Volume:   " + Integer.toString(Math.round(game.volume*10)), new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
		game.font.draw(game.batch, game.layout, 10, 100);
		if(game.volume > 0) {
			if(mousePos.x < 135 && mousePos.x > 115 && mousePos.y < 100 && mousePos.y > 100 - game.layout.height) {
				game.layout.setText(game.font, "<", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 115, 100);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Lower volume
					game.menuClick.play(game.volume);
					game.batch.end();
					if(game.volume-0.1f < 0) {
						game.volume = 0f;
					}
					else {
						game.volume -= 0.1f;
					}
					game.prefs.putFloat("volume", game.volume);
					return;
				}
			}
			else {
				game.layout.setText(game.font, "<", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 115, 100);
			}
		}
				
		if(game.volume < 1) {
			if(mousePos.x < 210 && mousePos.x > 180 && mousePos.y < 100 && mousePos.y > 100 - game.layout.height) {
				game.layout.setText(game.font, ">", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 180, 100);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Raise volume
					game.menuClick.play(game.volume);
					game.batch.end();
					game.volume += 0.1f;
					game.prefs.putFloat("volume", game.volume);
					return;
				}
			}
			else {
				game.layout.setText(game.font, ">", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 180, 100);
			}
		}
		
		// Back and apply settings
		game.layout.setText(game.font, "Back", new Color(232/255f, 193/255f, 112/255f, 1), 100, Align.bottomLeft, false);
		game.font.draw(game.batch, game.layout, 10, 50);
		if(game.volume > 0) {
			if(mousePos.x < 10 + game.layout.width && mousePos.x > 10 && mousePos.y < 50 && mousePos.y > 50 - game.layout.height) {
				game.font.draw(game.batch, game.layout, 10, 50);
				if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
					// Go back to start screen and save setting to prefs
					game.menuClick.play(game.volume);
					game.prefs.flush();
					if(windowModeIndex == 1) game.isBorderless = true;
					else game.isBorderless = false;
					game.setScreen(new StartScreen(game));
				}
			}
			else {
				game.layout.setText(game.font, "Back", new Color(1, 1, 1, 1), 100, Align.bottomLeft, false);
				game.font.draw(game.batch, game.layout, 10, 50);
			}
		}

		// Stop drawing
		game.batch.end();
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

	@Override
	public void dispose() {
		
	}

}
