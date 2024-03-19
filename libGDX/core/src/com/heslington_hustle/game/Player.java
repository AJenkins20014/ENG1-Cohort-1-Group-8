/**
 * The Player class represents the player character in the game.
 * It manages the player's position, movement, animations, and collision detection.
 */
package com.heslington_hustle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player {
	private HeslingtonHustle game;
	
	public Texture sprite;
	public int avatarNumber;
	public float speed;
	public float x;
	public float y;
	public String direction;
	public float clock;
	
	// Idle animation when player is facing to the right
	public Animation<TextureRegion> idleRAnimation;
	public Texture idleRSheet;
	// Idle animation when player is facing to the left
	public Animation<TextureRegion> idleLAnimation;
	public Texture idleLSheet;
	// Rows and columns for the idle sprite sheets
	private int idleSheetRows = 1;
	private int idleSheetCols = 7;
	
	// Idle animation when player is walking to the right
	public Animation<TextureRegion> walkRAnimation;
	public Texture walkRSheet;
	// Idle animation when player is walking to the left
	public Animation<TextureRegion> walkLAnimation;
	public Texture walkLSheet;
	// Rows and columns for the walk sprite sheets
	private int walkSheetRows = 1;
	private int walkSheetCols = 4;
	
	// Regions that the player should not be able to walk in
	private Rectangle[] inaccessibleRegionsScreen1;
	private Rectangle[] inaccessibleRegionsScreen2;
	private Rectangle[] inaccessibleRegionsScreen3;
	private Rectangle[] inaccessibleRegionsScreen4;
	
	/**
     * Constructs a new Player with the specified parameters.
     * @param game The main game instance
     * @param sprite The texture for the player's sprite
     * @param idleRSheet The sprite sheet for the idle animation when the player is facing to the right
     * @param idleLSheet The sprite sheet for the idle animation when the player is facing to the left
     * @param walkRSheet The sprite sheet for the walk animation when the player is walking to the right
     * @param walkLSheet The sprite sheet for the walk animation when the player is walking to the left
     * @param avatarNumber The number representing the player's avatar
     */
	public Player (HeslingtonHustle game, Texture sprite, Texture idleRSheet, Texture idleLSheet, Texture walkRSheet, Texture walkLSheet, int avatarNumber) {
		this.game = game;
		this.sprite = sprite;
		this.avatarNumber = avatarNumber;
		this.speed = 150;
		// Start player at bed
		this.x = 173;
		this.y = 360-185;
		this.direction = "R"; // Starting direction to face is right
		
		this.idleLSheet = idleLSheet;
		this.idleRSheet = idleRSheet;
		this.walkLSheet = walkLSheet;
		this.walkRSheet = walkRSheet;
		
		// Initialize animations
		initializeAnimations();
		
		// Initialize inaccessible regions
        initializeInaccessibleRegions();
	}
	
	/**
     * Renders the idle animation of the player.
     */
	public void idleAnimation() {
		if(direction == "R") {
			// Get current animation frame depending on the value of the clock
			TextureRegion currentFrame = idleRAnimation.getKeyFrame(clock, true);
			game.batch.draw(currentFrame, x-16, y-16);
		}
		else if(direction == "L") {
			TextureRegion currentFrame = idleLAnimation.getKeyFrame(clock, true);
			game.batch.draw(currentFrame, x-16, y-16);
		}
	}
	
	/**
     * Moves the player upwards.
     */
	public void moveUp() {
		if(y > game.camera.viewportHeight - sprite.getHeight()/2) {
			if(game.map.screen == 1) {
				// If player was on bottom left, scroll camera to top left
				y = 0 + sprite.getHeight();
				game.map.screen = 3;
			}
			else if(game.map.screen == 2) {
				// If player was on bottom right, scroll camera to top right
				y = 0 + sprite.getHeight();
				game.map.screen = 4;
			}
			else {
				return;
			}
		}
		
		// If player can move to the region, move them there. Gdx.graphics.getDeltaTime() is essential for movement to be the same on all framerates
		if (canMoveTo(x, y-sprite.getHeight()/2 + speed * Gdx.graphics.getDeltaTime())) {
            y += speed * Gdx.graphics.getDeltaTime();
        }
	}
	
	/**
     * Moves the player to the left.
     */
	public void moveLeft() {
		direction = "L";
		
		if(x < 0 + sprite.getWidth()/2) {
			if(game.map.screen == 2) {
				// If player was on bottom right, scroll camera to bottom left
				x = game.camera.viewportWidth - sprite.getWidth();
				game.map.screen = 1;
			}
			else if(game.map.screen == 4) {
				// If player was on top right, scroll camera to top left
				x = game.camera.viewportWidth - sprite.getWidth();
				game.map.screen = 3;
			}
			else {
				return;
			}
		}
		
		if (canMoveTo(x - speed * Gdx.graphics.getDeltaTime(), y-sprite.getHeight()/2)) {
            x -= speed * Gdx.graphics.getDeltaTime();
        }
	}
	
	/**
     * Moves the player downwards.
     */
	public void moveDown() {
		if(y < 0 + sprite.getHeight()/2) {
			if(game.map.screen == 3) {
				// If player was on top left, scroll camera to bottom left
				y = game.camera.viewportHeight - sprite.getHeight();
				game.map.screen = 1;
			}
			else if(game.map.screen == 4) {
				// If player was on top right, scroll camera to bottom right
				y = game.camera.viewportHeight - sprite.getHeight();
				game.map.screen = 2;
			}
			else {
				return;
			}
		}
		
		if (canMoveTo(x, y-sprite.getHeight()/2 - speed * Gdx.graphics.getDeltaTime())) {
            y -= speed * Gdx.graphics.getDeltaTime();
        }
	}
	
	/**
     * Moves the player to the right.
     */
	public void moveRight() {
		direction = "R";
		
		if(x > game.camera.viewportWidth - sprite.getWidth()/2) {
			if(game.map.screen == 1) {
				// If player was on bottom left, scroll camera to bottom right
				x = 0 + sprite.getWidth();
				game.map.screen = 2;
			}
			else if(game.map.screen == 3) {
				// If player was on top left, scroll camera to top right
				x = 0 + sprite.getWidth();
				game.map.screen = 4;
			}
			else {
				return;
			}
		}
		
		if (canMoveTo(x + speed * Gdx.graphics.getDeltaTime(), y-sprite.getHeight()/2)) {
            x += speed * Gdx.graphics.getDeltaTime();
        }
	}
	
	/**
     * Initializes the animations of the player.
     */
	private void initializeAnimations() {
		// Idle R animation
		// Split the sprite sheet into multiple sprites
		TextureRegion[][] idleRTexture = TextureRegion.split(idleRSheet,
				idleRSheet.getWidth() / idleSheetCols,
				idleRSheet.getHeight() / idleSheetRows);
		TextureRegion[] idleRFrames = new TextureRegion[idleSheetCols * idleSheetRows];
		int index = 0;
		for (int i = 0; i < idleSheetRows; i++) {
			for (int j = 0; j < idleSheetCols; j++) {
				idleRFrames[index++] = idleRTexture[i][j];
			}
		}
		// Create animation out of the individual frames
		idleRAnimation = new Animation<TextureRegion>(0.2f, idleRFrames);
				
		// Idle L animation
		TextureRegion[][] idleLTexture = TextureRegion.split(idleLSheet,
				idleLSheet.getWidth() / idleSheetCols,
				idleLSheet.getHeight() / idleSheetRows);
		TextureRegion[] idleLFrames = new TextureRegion[idleSheetCols * idleSheetRows];
		index = 0;
		for (int i = 0; i < idleSheetRows; i++) {
			for (int j = 0; j < idleSheetCols; j++) {
				idleLFrames[index++] = idleLTexture[i][j];
			}
		}
		idleLAnimation = new Animation<TextureRegion>(0.2f, idleLFrames);
				
		// Walk R animation
		TextureRegion[][] walkRTexture = TextureRegion.split(walkRSheet,
				walkRSheet.getWidth() / walkSheetCols,
				walkRSheet.getHeight() / walkSheetRows);
		TextureRegion[] walkRFrames = new TextureRegion[walkSheetCols * walkSheetRows];
		index = 0;
		for (int i = 0; i < walkSheetRows; i++) {
			for (int j = 0; j < walkSheetCols; j++) {
				walkRFrames[index++] = walkRTexture[i][j];
			}
		}
		walkRAnimation = new Animation<TextureRegion>(0.2f, walkRFrames);
				
		// Walk L animation
		TextureRegion[][] walkLTexture = TextureRegion.split(walkLSheet,
				walkLSheet.getWidth() / walkSheetCols,
				walkLSheet.getHeight() / walkSheetRows);
		TextureRegion[] walkLFrames = new TextureRegion[walkSheetCols * walkSheetRows];
		index = 0;
		for (int i = 0; i < walkSheetRows; i++) {
			for (int j = 0; j < walkSheetCols; j++) {
				walkLFrames[index++] = walkLTexture[i][j];
			}
		}
		walkLAnimation = new Animation<TextureRegion>(0.2f, walkLFrames);
	}
	
	/**
     * Checks if the player can move to the specified target position.
     * @param targetX The target x-coordinate of the player's position.
     * @param targetY The target y-coordinate of the player's position.
     * @return true If the player can move to the target position, false otherwise.
     */
	private boolean canMoveTo(float targetX, float targetY) {
        // Check if the target position lies within any of the inaccessible regions
		if(game.map.screen == 1) {
			for (Rectangle region : inaccessibleRegionsScreen1) {
	            if (region.contains(targetX, targetY)) {
	                return false; // Movement is not allowed
	            }
	        }
	        return true; // Movement is allowed
		}
		else if(game.map.screen == 2) {
			for (Rectangle region : inaccessibleRegionsScreen2) {
	            if (region.contains(targetX, targetY)) {
	                return false;
	            }
	        }
	        return true;
		}
		else if(game.map.screen == 3) {
			for (Rectangle region : inaccessibleRegionsScreen3) {
	            if (region.contains(targetX, targetY)) {
	                return false;
	            }
	        }
	        return true;
		}
		else {
			for (Rectangle region : inaccessibleRegionsScreen4) {
	            if (region.contains(targetX, targetY)) {
	                return false;
	            }
	        }
	        return true;
		}
    }
	
	/**
     * Initializes the regions that the player should not be able to walk in.
     */
	private void initializeInaccessibleRegions() {
		// Manually draw rectangles for all the regions that the player cannot move to
		// Can add be easily modified if the map is to be changed
		inaccessibleRegionsScreen1 = new Rectangle[] { 
				new Rectangle(191, 360-65, 226, 66),
				new Rectangle(447, 360-65, 192, 65),
				new Rectangle(0, 360-96, 161, 97),
				new Rectangle(0, 360-224, 256, 129),
				new Rectangle(0, 360-256, 192, 32),
				new Rectangle(0, 360-360, 256, 105),
				new Rectangle(287, 360-224, 114, 130),
				new Rectangle(608, 360-224, 32, 129),
				new Rectangle(287, 360-320, 288, 65),
				new Rectangle(441, 360-224, 136, 130),
				new Rectangle(254, 360-360, 386, 9),
				new Rectangle(608, 360-360, 32, 105),
				new Rectangle(400, 360-208, 42, 114),
				};
		inaccessibleRegionsScreen2 = new Rectangle[] { 
				new Rectangle(0, 360-65, 97, 66),
				new Rectangle(0, 360-224, 97, 130),
				new Rectangle(0, 360-360, 640, 105),
				new Rectangle(352, 360-256, 288, 34),
				new Rectangle(255, 360-224, 161, 66),
				new Rectangle(127, 360-224, 98, 162),
				new Rectangle(127, 360-32, 290, 33),
				new Rectangle(351, 360-128, 66, 66),
				new Rectangle(447, 360-360, 193, 253),
				new Rectangle(607, 360-76, 33, 77),
				new Rectangle(447, 360-32, 130, 33),
				new Rectangle(530, 360-108, 110, 46),
				new Rectangle(447, 360-78, 53, 16),
				new Rectangle(127, 360-128, 194, 66),
				};
		inaccessibleRegionsScreen3 = new Rectangle[] { 
				new Rectangle(0, 360-360, 161, 200),
				new Rectangle(0, 360-160, 640, 160),
				new Rectangle(191, 360-224, 97, 66),
				new Rectangle(191, 360-360, 97, 105),
				new Rectangle(447, 360-360, 193, 40),
				new Rectangle(280, 360-360, 137, 33),
				new Rectangle(576, 360-360, 64, 105),
				new Rectangle(576, 360-224, 64, 225),
				};
		inaccessibleRegionsScreen4 = new Rectangle[] { 
				new Rectangle(0, 360-360, 97, 105),
				new Rectangle(0, 360-224, 97, 161),
				new Rectangle(127, 360-360, 290, 137),
				new Rectangle(127, 360-192, 290, 98),
				new Rectangle(447, 360-360, 130, 265),
				new Rectangle(607, 360-360, 33, 296),
				new Rectangle(373, 360-245, 110, 112),
				new Rectangle(0, 360-64, 640, 64),
				};
    }
}
