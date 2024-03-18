package com.heslington_hustle.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.objects.Bed;
import com.heslington_hustle.objects.Building;
import com.heslington_hustle.objects.Foodhall;
import com.heslington_hustle.objects.Object;

public class Map implements Screen{
	
	private HeslingtonHustle game;
	public Object[] objects = new Object[8];
	
	public int screen; // 1 = bottom left, 2 = bottom right, 3 = top left, 4 = top right
	private Texture screen1, screen2, screen3, screen4;
	private Texture screen1Overlay, screen2Overlay, screen3Overlay, screen4Overlay; // Textures that should be displayed over the player object
	private Texture blackFilter; // Dark filter for when it is late in the game day
	private Texture energyBar1, energyBar2, energyBar3;
	private Texture energyIconFull, energyIconEmpty;
	
	public Map(HeslingtonHustle game, int screen) {
		this.game = game;
		game.map = this;
		this.screen = screen;
		
		// Add objects to array
		initialiseObjects();
		
		// Load textures
		screen1 = new Texture("Map/Screen1.png");
		screen2 = new Texture("Map/Screen2.png");
		screen3 = new Texture("Map/Screen3.png");
		screen4 = new Texture("Map/Screen4.png");
		screen1Overlay = new Texture("Map/Screen1Overlay.png");
		screen2Overlay = new Texture("Map/Screen2Overlay.png");
		screen3Overlay = new Texture("Map/Screen3Overlay.png");
		screen4Overlay = new Texture("Map/Screen4Overlay.png");
		
		blackFilter = new Texture("UI/DarkFilter.png");
		
		energyBar1 = new Texture("UI/EnergyBar1.png");
		energyBar2 = new Texture("UI/EnergyBar2.png");
		energyBar3 = new Texture("UI/EnergyBar3.png");
		
		energyIconFull = new Texture("UI/EnergyIcon.png");
		energyIconEmpty = new Texture("UI/EnergyIconEmpty.png");
	}

	@Override
	public void render(float delta) {
		// Clear the screen
		Gdx.gl.glClearColor(70/255f, 130/255f, 50/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
				
		
		// Begin drawing on screen
		game.batch.begin();
		
		// Draw map background
		if(screen == 1) {
			game.batch.draw(screen1, 0, 0);
		}
		else if(screen == 2) {
			game.batch.draw(screen2, 0, 0);
		}
		else if(screen == 3) {
			game.batch.draw(screen3, 0, 0);
		}
		else if(screen == 4) {
			game.batch.draw(screen4, 0, 0);
		}

		// If the player isn't moving, display the idle animation
		if(!Gdx.input.isKeyPressed(Keys.W) && !Gdx.input.isKeyPressed(Keys.S) && !Gdx.input.isKeyPressed(Keys.A) && !Gdx.input.isKeyPressed(Keys.D)) {
			game.player.idleAnimation();
		}
		else {
			if(game.player.direction == "R") {
				// Player is moving right, display the walk right animation
				TextureRegion currentFrame = game.player.walkRAnimation.getKeyFrame(game.player.clock, true);
				game.batch.draw(currentFrame, game.player.x-16, game.player.y-16);
			}
			else if(game.player.direction == "L") {
				// Player is moving left, display the walk left animation
				TextureRegion currentFrame = game.player.walkLAnimation.getKeyFrame(game.player.clock, true);
				game.batch.draw(currentFrame, game.player.x-16, game.player.y-16);
			}
		}
		
		// Checks if player wants to move - ignored is game is paused
		if(!game.paused) {
			if(Gdx.input.isKeyPressed(Keys.W)) {
				game.player.moveUp();
			}
			else if(Gdx.input.isKeyPressed(Keys.S)) {
				game.player.moveDown();
			}
			if(Gdx.input.isKeyPressed(Keys.A)) {
				game.player.moveLeft();
			}
			else if(Gdx.input.isKeyPressed(Keys.D)) {
				game.player.moveRight();
			}
		}
		
		game.player.clock += Gdx.graphics.getDeltaTime(); // Update clock for the player object - for animations
		
		// Draw map overlay
		if(screen == 1) {
			game.batch.draw(screen1Overlay, 0, 0);
		}
		else if(screen == 2) {
			game.batch.draw(screen2Overlay, 0, 0);
		}
		else if(screen == 3) {
			game.batch.draw(screen3Overlay, 0, 0);
		}
		else if(screen == 4) {
			game.batch.draw(screen4Overlay, 0, 0);
		}
		
		// Check for whether the player has touched an object
		checkPlayerObjectCollision();
				
		// Draw UI elements
		drawUI();
		
		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}
		
		// Stop drawing
		game.batch.end();
	}
	
	private void initialiseObjects() {
		// Manually initialise objects and define rectangles in which the player can stand to interact with them
		Rectangle[] interactRegions;
		
		// CS Building
		interactRegions = new Rectangle[] { 
				new Rectangle(192, 360-255, 61, 32),
				};
		objects[0] = new Building(game, "CS Building", interactRegions, 1, "E: Study", 100, 360-182, 100, 360-259, game.minigames[0], 60f, 3);
		
		// Student Hub
		interactRegions = new Rectangle[] { 
				new Rectangle(399, 360-255, 44, 68),
				};
		objects[1] = new Building(game, "Student Hub", interactRegions, 1, "E: Study", 375, 360-130, 375, 360-185, game.minigames[1], 60f, 3);
		
		// Library
		interactRegions = new Rectangle[] { 
				new Rectangle(416, 360-110, 115, 69),
				};
		objects[2] = new Building(game, "Library", interactRegions, 2, "E: Study", 440, 360-72, 440, 360-128, game.minigames[1], 60f, 3);
		
		// Lake
		interactRegions = new Rectangle[] { 
				new Rectangle(288, 360-255, 64, 32),
				new Rectangle(416, 360-223, 32, 65),
				};
		objects[3] = new Building(game, "Lake", interactRegions, 2, "E: Relax", 432, 360-250, 432, 360-319, game.minigames[3], 0f, 2);
		
		// Accomodation
		interactRegions = new Rectangle[] { 
				new Rectangle(160, 360-210, 32, 51),
				};
		objects[4] = new Bed(game, "Accomodation", interactRegions, 3, "E: Sleep", 130, 360-35, 130, 360-125);
		
		// Sports Field
		interactRegions = new Rectangle[] { 
				new Rectangle(267, 360-339, 334, 202),
				};
		objects[5] = new Building(game, "Sports Field", interactRegions, 3, "E: Play", 380, 360-201, 380, 360-288, game.minigames[5], 0f, 2);
		
		// Glasshouse
		interactRegions = new Rectangle[] { 
				new Rectangle(306, 360-222, 77, 32),
				new Rectangle(416, 360-298, 32, 55),
				new Rectangle(416, 360-158, 32, 63),
				};
		objects[6] = new Foodhall(game, "The Glasshouse", interactRegions, 4, "E: Eat", 390, 360-160, 390, 360-190, 40f, 1);
		
		// Bus to Town
		interactRegions = new Rectangle[] { 
				new Rectangle(447, 360-95, 161, 32),
				};
		objects[7] = new Building(game, "Bus", interactRegions, 4, "E: Go to Town", 415, 360-15, 415, 360-45, game.minigames[4], 0f, 2);
	}
	
	private void drawUI() {
		// If time is past 8:00pm, add a dark filter
		if(game.time > 20) {
			game.batch.draw(blackFilter, 0, 0); // Dark filter
		}
				
		// Draw energy bar
		game.batch.draw(energyBar1, 10, 308);
		TextureRegion region = new TextureRegion(energyBar2, (int)Math.round(284f*(game.energyBar.energy/100f)), energyBar2.getHeight());
		game.batch.draw(region, 10, 308);
		game.batch.draw(energyBar3, 10, 308);
		
		game.font.getData().setScale(0.25f); // Set font size
		game.font.draw(game.batch, Integer.toString((int)Math.round(game.energyBar.energy)), -17, 328, 100, Align.center, false);
		
		// Draw time and day
		game.font.getData().setScale(0.5f); // Set font size
		game.font.draw(game.batch, "Day: " + Integer.toString(game.day), 520, 290, 100, Align.center, false);
		// If midnight, display time as 00:00
		if(game.time == 24) {
			game.font.draw(game.batch, "00:00", 520, 330, 100, Align.center, false);
		}
		// Add 0 to single digit time (display 7am as 07:00 instead of 7:00)
		else if(Integer.toString(game.time).length() == 1) {
			game.font.draw(game.batch, "0" + Integer.toString(game.time) + " : 00", 520, 330, 100, Align.center, false);
		}
		else {
			game.font.draw(game.batch, Integer.toString(game.time) + " : 00", 520, 330, 100, Align.center, false);
		}
	}
	
	private void checkPlayerObjectCollision() {
		// Checks if the player is in an object's interact region(s)
		for(int i = 0; i < objects.length; i++) {
			if(objects[i] != null && objects[i].screen == screen) {
				for (Rectangle region : objects[i].interactRegions) {
		            if (region.contains(game.player.x,game.player.y-game.player.sprite.getHeight()/2)) {
		            	// Draw tooltips
						if(objects[i] instanceof Building) {
							game.font.getData().setScale(0.3f); // Set font size
							game.font.draw(game.batch, objects[i].tooltip, objects[i].tooltipX, objects[i].tooltipY, 100, Align.center, false);
							
							if(((Building) objects[i]).requiredEnergy > 0f) {
								game.font.getData().setScale(0.5f); // Set font size
								game.font.setColor(new Color(164/255f, 221/255f, 219/255f, 1));
								
								Texture energyIcon = energyIconEmpty;
								if(game.energyBar.energy >= ((Building) objects[i]).requiredEnergy){
									energyIcon = energyIconFull;
								}
								
								game.batch.draw(energyIcon, objects[i].tooltipX + 20, objects[i].tooltipY-40);
								game.font.draw(game.batch, Integer.toString((int)Math.round(((Building) objects[i]).requiredEnergy)),
										objects[i].tooltipX + 40, objects[i].tooltipY - 20,
										20, Align.left, false);
							}
							
							game.font.getData().setScale(0.3f); // Set font size
							game.font.setColor(new Color(1, 1, 1, 1));
							game.font.draw(game.batch, objects[i].name, objects[i].nameX, objects[i].nameY, 100, Align.center, false);
						}
						else {
							game.font.getData().setScale(0.3f); // Set font size
							game.font.draw(game.batch, objects[i].tooltip, objects[i].tooltipX, objects[i].tooltipY, 100, Align.center, false);
							game.font.draw(game.batch, objects[i].name, objects[i].nameX, objects[i].nameY, 100, Align.center, false);
						}
						
						// Check if interact key (E) is pressed
						if(Gdx.input.isKeyJustPressed(Keys.E)) {
							if(objects[i] instanceof Building) {
								((Building) objects[i]).interact();
							}
							else if(objects[i] instanceof Bed) {
								((Bed) objects[i]).startNewDay();
							}
							else if(objects[i] instanceof Foodhall) {
								((Foodhall) objects[i]).eat();
							}
						}
		            }
		        }
			}
		}
	}
	
	@Override
	public void show() {
		// Called when this screen becomes displayed
		game.mapMusic.play();
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
		game.mapMusic.stop();
	}

	@Override
	public void dispose() {
		
	}

}
