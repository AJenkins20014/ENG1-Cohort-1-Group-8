package com.heslington_hustle.screens.minigames.BugFixer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.heslington_hustle.game.HeslingtonHustle;

public class ScatterBug {
	private HeslingtonHustle game;
	private BugFixer bugFixer;
	
	private Body body;
	
	public Sprite sprite;
	public float x;
	public float y;
	private float clock;
	private float animationClock;
	private float timeSinceAttack;
	
	private Animation<TextureRegion> spawnAnimation;
	private Texture spawnSheet;
	private int spawnSheetRows = 1;
	private int spawnSheetCols = 5;
	
	private Animation<TextureRegion> attackAnimation;
	private Texture attackSheet;
	private int attackSheetRows = 1;
	private int attackSheetCols = 3;
	
	private boolean firstAttack;
	
	public ScatterBug(HeslingtonHustle game, BugFixer bugFixer, float x, float y) {
		this.game = game;
		this.bugFixer = bugFixer;
		this.x = x;
		this.y = y;
		
		createBug();
	}
	
	private void createBug() {
		// Create body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		body = bugFixer.world.createBody(bodyDef);

		// Create hitbox in form of circle
		CircleShape circle = new CircleShape();
		circle.setRadius(4f);

		// Attach fixture to body
		body.createFixture(circle, 0.0f);

		// Dispose hitbox
		circle.dispose();			
		
		
		// Load sprite sheets and create animations
		// Spawn animation
		spawnSheet = new Texture("BugFixerMinigame/ScatterBug/Spawn.png");
		TextureRegion[][] spawnTexture = TextureRegion.split(spawnSheet,
				spawnSheet.getWidth() / spawnSheetCols,
				spawnSheet.getHeight() / spawnSheetRows);

		TextureRegion[] spawnFrames = new TextureRegion[spawnSheetCols * spawnSheetRows];
		int index = 0;
		for (int i = 0; i < spawnSheetRows; i++) {
			for (int j = 0; j < spawnSheetCols; j++) {
				spawnFrames[index++] = spawnTexture[i][j];
			}
		}
		spawnAnimation = new Animation<TextureRegion>(0.2f, spawnFrames);
		
		// Attack animation
		attackSheet = new Texture("BugFixerMinigame/ScatterBug/Attack.png");
		TextureRegion[][] attackTexture = TextureRegion.split(attackSheet,
				attackSheet.getWidth() / attackSheetCols,
				attackSheet.getHeight() / attackSheetRows);

		TextureRegion[] attackFrames = new TextureRegion[attackSheetCols * attackSheetRows];
		index = 0;
		for (int i = 0; i < attackSheetRows; i++) {
			for (int j = 0; j < attackSheetCols; j++) {
				attackFrames[index++] = attackTexture[i][j];
			}
		}
		attackAnimation = new Animation<TextureRegion>(0.5f, attackFrames);
		
		
		clock = 0;
		animationClock = 0;
		timeSinceAttack = 0;
		firstAttack = false;
	}
	
	public void update() {
		clock += Gdx.graphics.getDeltaTime();
		animationClock += Gdx.graphics.getDeltaTime();
		timeSinceAttack += Gdx.graphics.getDeltaTime();
		
		drawSprite();
		if(!firstAttack && clock > 2f) {
			attack();
			firstAttack = true;
		}
		else if(firstAttack && timeSinceAttack > 1.5f) {
			attack();
		}
	}
	
	private void drawSprite() {
		game.batch.begin();
		
		// Spawn animation
		if(clock < 1f) {
			TextureRegion currentFrame = spawnAnimation.getKeyFrame(animationClock, true);
			game.batch.draw(currentFrame, x-10, y-10);
		}
		// Attack animation
		else {
			TextureRegion currentFrame = attackAnimation.getKeyFrame(animationClock-1f, true);
			game.batch.draw(currentFrame, x-10, y-10);
		}

		game.batch.end();
	}
	
	private void attack() {
		timeSinceAttack = 0f;
		
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(0, y+100), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(0, y-100), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(x+100, 0), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(x-100, 0), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(x+100, y+100), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(x-100, y+100), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(x+100, y-100), false));
		bugFixer.enemyBullets.add(new Bullet(game, bugFixer, x, y, new Vector2(x-100, y-100), false));
		bugFixer.enemyShot.play(game.volume);
	}
	
	public void destroy() {
		bugFixer.world.destroyBody(body);
	}
}
