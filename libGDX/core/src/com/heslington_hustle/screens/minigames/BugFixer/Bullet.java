/**
 * Represents a bullet in the BugFixer minigame.
 * Bullets can be fired by the player ship or enemy bugs.
 */
package com.heslington_hustle.screens.minigames.BugFixer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.heslington_hustle.game.HeslingtonHustle;

public class Bullet {
	private HeslingtonHustle game;
	private BugFixer bugFixer;
	
	private float x, y;
	private Vector2 destination;
	private float speed;
	private float directionX;
	private float directionY;
	private float rotation;
	private boolean playerBullet;
	
	private Animation<TextureRegion> animation;
	private Texture sheet;
	private int sheetRows = 1;
	private int sheetCols = 5;
	private float clock;
	
	/**
     * Constructs a new Bullet instance.
     * @param game The main game instance
     * @param bugFixer The BugFixer minigame instance
     * @param x The initial x-coordinate of the bullet
     * @param y The initial y-coordinate of the bullet
     * @param destination The destination position of the bullet
     * @param playerBullet Indicates whether the bullet was fired by the player
     */
	public Bullet(HeslingtonHustle game, BugFixer bugFixer, float x, float y, Vector2 destination, boolean playerBullet) {
		this.game = game;
		this.bugFixer = bugFixer;
		this.x = x;
		this.y = y;
		this.destination = destination;
		this.speed = 100f;
		this.playerBullet = playerBullet;
		
		// Set up animation
		String path = "BugFixerMinigame/Bullet.png";
		if(this.playerBullet) {
			this.speed = 300f;
			path = "BugFixerMinigame/PlayerBullet.png";
		}
		sheet = new Texture(path);
		TextureRegion[][] texture = TextureRegion.split(sheet,
				sheet.getWidth() / sheetCols,
				sheet.getHeight() / sheetRows);

		TextureRegion[] frames = new TextureRegion[sheetCols * sheetRows];
		int index = 0;
		for (int i = 0; i < sheetRows; i++) {
			for (int j = 0; j < sheetCols; j++) {
				frames[index++] = texture[i][j];
			}
		}
		animation = new Animation<TextureRegion>(0.1f, frames);
		
		clock = 0;
		
		// Calculate the direction vector
        float directionLength = (float) Math.sqrt((this.destination.x - this.x) * (this.destination.x - this.x) + (this.destination.y - this.y) * (this.destination.y - this.y));
        rotation = (float) Math.atan2(this.x - this.destination.x, this.destination.y - this.y);

        if (directionLength != 0) {
            this.directionX = (this.destination.x - this.x) / directionLength;
            this.directionY = (this.destination.y - this.y) / directionLength;
        } else {
            // Destination is the same as current position
            this.directionX = 0;
            this.directionY = 0;
        }
	}
	
	/**
     * Updates the bullet's position, animation, and collision detection.
     * Called every frame in the BugFixer minigame render method.
     */
	public void update() {
		// Called every frame in the BugFixer render() method
		move();
		drawSprite();
		checkCollision();
		
		// Increment clock
		clock += Gdx.graphics.getDeltaTime();
	}
	
	/**
     * Moves the bullet according to its direction and speed.
     */
	private void move() {
        x += directionX * speed*Gdx.graphics.getDeltaTime();
        y += directionY * speed*Gdx.graphics.getDeltaTime();
	}
	
	/**
     * Draws the bullet sprite on the screen.
     */
	private void drawSprite() {
		TextureRegion currentFrame = animation.getKeyFrame(clock, true);
		Sprite sprite = new Sprite(currentFrame);
		
		sprite.setRotation((float) Math.toDegrees(rotation));
		sprite.setX(x-5);
		sprite.setY(y-5);
		sprite.draw(game.batch);
	}
	
	/**
     * Checks if the bullet is out of bounds of the game screen.
     * @return true If the bullet is out of bounds, false otherwise
     */
	public boolean checkOutOfBounds() {
		if(x < -10f || x > 650f || y < -10f || y > 370f) {
			return true;
		}
		else return false;
	}
	
	/**
     * Checks for collisions between the bullet and game entities,
     * such as enemy bugs or the player ship.
     */
	private void checkCollision() {
		if(playerBullet) {
			for (int i = 0; i < bugFixer.rows; i++) {
				for (int j = 0; j < bugFixer.columns; j++) {
					if (bugFixer.enemyGrid[i][j] instanceof SniperBug) {
						double distance = Math.sqrt(Math.pow(x - ((SniperBug)bugFixer.enemyGrid[i][j]).x, 2) + Math.pow(y - ((SniperBug)bugFixer.enemyGrid[i][j]).y, 2));
			            if (distance  < 10f) {
			            	x = -100f;
			            	y = -100f;
			            	((SniperBug) bugFixer.enemyGrid[i][j]).destroy();
				            bugFixer.enemyGrid[i][j] = null;
				            bugFixer.score += 10f;
			            }
				    } 
					else if (bugFixer.enemyGrid[i][j] instanceof ScatterBug) {
						double distance = Math.sqrt(Math.pow(x - ((ScatterBug)bugFixer.enemyGrid[i][j]).x, 2) + Math.pow(y - ((ScatterBug)bugFixer.enemyGrid[i][j]).y, 2));
			            if (distance  < 10f) {
			            	x = -100f;
			            	y = -100f;
			            	((ScatterBug) bugFixer.enemyGrid[i][j]).destroy();
							bugFixer.enemyGrid[i][j] = null;
							bugFixer.score += 15f;
			            }
				    }
				 }
			}
		}
		else {
			double distance = Math.sqrt(Math.pow(x - bugFixer.player.getPosition().x, 2) + Math.pow(y - bugFixer.player.getPosition().y, 2));
            if (distance  < 15f) {
            	x = -100f;
            	y = -100f;
            	bugFixer.health -= 5f;
            }
		}
	}
}
