package com.heslington_hustle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
	
	private HeslingtonHustle game;
	
	public Texture sprite;
	public float speed;
	public float x;
	public float y;
	public String direction;
	public float clock;
	
	public Animation<TextureRegion> idleRAnimation;
	public Texture idleRSheet;
	public Animation<TextureRegion> idleLAnimation;
	public Texture idleLSheet;
	private int idleSheetRows = 1;
	private int idleSheetCols = 7;
	
	public Animation<TextureRegion> walkRAnimation;
	public Texture walkRSheet;
	public Animation<TextureRegion> walkLAnimation;
	public Texture walkLSheet;
	private int walkSheetRows = 1;
	private int walkSheetCols = 4;
	
	public Player (HeslingtonHustle game, Texture sprite, Texture idleRSheet, Texture idleLSheet, Texture walkRSheet, Texture walkLSheet) {
		this.game = game;
		this.sprite = sprite;
		this.speed = 150;
		this.x = game.camera.viewportWidth/2;
		this.y = game.camera.viewportHeight/2;
		this.direction = "R";
		
		// Idle R animation
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
	
	public void idleAnimation() {
		if(direction == "R") {
			TextureRegion currentFrame = idleRAnimation.getKeyFrame(clock, true);
			game.batch.draw(currentFrame, x-16, y-16);
		}
		else if(direction == "L") {
			TextureRegion currentFrame = idleLAnimation.getKeyFrame(clock, true);
			game.batch.draw(currentFrame, x-16, y-16);
		}
	}
	
	public void moveUp() {
		if(y < game.camera.viewportHeight - sprite.getHeight()/2) {
			y += speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 1) {
			// If player was on bottom left, scroll camera to top left
			y = 0 + sprite.getHeight();
			game.map.screen = 3;
		}
		else if(game.map.screen == 2) {
			// If player was on bottom right, scroll camera to top right
			y = 0 + sprite.getHeight();
			game.map.screen = 4;
		}
	}
	
	public void moveLeft() {
		direction = "L";
		
		if(x > 0) {
			x -= speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 2) {
			// If player was on bottom right, scroll camera to bottom left
			x = game.camera.viewportWidth - sprite.getWidth();
			game.map.screen = 1;
		}
		else if(game.map.screen == 4) {
			// If player was on top right, scroll camera to top left
			x = game.camera.viewportWidth - sprite.getWidth();
			game.map.screen = 3;
		}
	}
	
	public void moveDown() {
		if(y > 0 + sprite.getHeight()/2) {
			y -= speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 3) {
			// If player was on top left, scroll camera to bottom left
			y = game.camera.viewportHeight - sprite.getHeight();
			game.map.screen = 1;
		}
		else if(game.map.screen == 4) {
			// If player was on top right, scroll camera to bottom right
			y = game.camera.viewportHeight - sprite.getHeight();
			game.map.screen = 2;
		}
	}
	
	public void moveRight() {
		direction = "R";
		
		if(x < game.camera.viewportWidth - sprite.getWidth()) {
			x += speed*Gdx.graphics.getDeltaTime();
		}
		else if(game.map.screen == 1) {
			// If player was on bottom left, scroll camera to bottom right
			x = 0 + sprite.getWidth();
			game.map.screen = 2;
		}
		else if(game.map.screen == 3) {
			// If player was on top left, scroll camera to top right
			x = 0 + sprite.getWidth();
			game.map.screen = 4;
		}
	}
}
