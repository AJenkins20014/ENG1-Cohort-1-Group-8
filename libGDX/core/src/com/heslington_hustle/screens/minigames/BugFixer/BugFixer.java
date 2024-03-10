package com.heslington_hustle.screens.minigames.BugFixer;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.MinigameScreen;

public class BugFixer extends MinigameScreen implements Screen {

	public World world;
	private Box2DDebugRenderer debugRenderer;
	private boolean minimised;
	private float accumulator = 0f;
	private float fixedTimeStep = 1 / 144f; // Adjust as needed
	
	public Body player;
	private float playerToMouse;
	private float timeSinceAttack;
	private float attackCooldown;
	private ArrayList<Bullet> bullets = new ArrayList<>();
	public ArrayList<Bullet> enemyBullets = new ArrayList<>();
	
	private float clock;
	private float enemySpawnClock;
	private float spawnTimer;
	private Random random;
	
	public float health;
	private Sprite cursorShip;
	
	public int rows = 32;
	public int columns = 17;
	public Object[][] enemyGrid = new Object[rows][columns];
	
	private float studyPointsGained;
	private float maxStudyPointsGained;
	public int score;
	
	private Music backgroundMusic;
	private Sound playerShot;
	public Sound enemyShot;
	private Sound enemySpawn;
	
	public BugFixer(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		world = new World(new Vector2(0, 0), true); // Create world with no gravity
		debugRenderer = new Box2DDebugRenderer();
		this.studyPointsGained = 15f; // From worst possible performance
		this.maxStudyPointsGained = 100f; // From best possible performance
		this.random = new Random();
		this.attackCooldown = 0.2f;
		
		// Load textures
		Texture texture = new Texture("BugFixerMinigame/Cursor.png");
		cursorShip = new Sprite(texture);
		
		// Load sounds
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/BugFixerMusic.ogg"));
		backgroundMusic.setLooping(true);
		
		playerShot = Gdx.audio.newSound(Gdx.files.internal("BugFixerMinigame/PlayerShot.mp3"));
		enemyShot = Gdx.audio.newSound(Gdx.files.internal("BugFixerMinigame/EnemyShot.mp3"));
		enemySpawn = Gdx.audio.newSound(Gdx.files.internal("BugFixerMinigame/EnemySpawn.mp3"));
	}
	
	@Override
	public void startGame() {
		// Code to restart the game
		studyPointsGained = 15f;
		clock = 0f;
		enemySpawnClock = 0f;
		spawnTimer = 2f;
		timeSinceAttack = 1f;
		health = 100f;
		enemyGrid = new Object[rows][columns];
		bullets.clear();
		enemyBullets.clear();
		score = 0;
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		Array<Body> bodies = new Array<Body>();
	    world.getBodies(bodies);
	    for(Body body: bodies){
	        world.destroyBody(body);
	    }
	    
	    createShip();
	    createWalls();
		
		// Display game screen
		game.setScreen(this);
	}
	
	private void endGame() {
		// Calculate final score
		studyPointsGained += clock/3;
		studyPointsGained += score/10;
		
		// Check BugFixer high score
		if(game.prefs.getInteger("bugFixerHighScore", 0) < score) {
			game.prefs.putInteger("bugFixerHighScore", score);
			game.prefs.flush();
		}
		
		if(studyPointsGained > maxStudyPointsGained) {
			studyPointsGained = maxStudyPointsGained;
		}
		
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		
		// Reset custom cursor - TODO
		Pixmap pixmap = new Pixmap(Gdx.files.internal("UI/Cursor.png")); // TODO (crosshair?)
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Add studyPoints score to total score for this minigame
		if(game.studyPoints.containsKey("BugFixer")) {
			game.studyPoints.put("BugFixer", (game.studyPoints.get("BugFixer") + studyPointsGained));
		}
		else {
			game.studyPoints.put("BugFixer", studyPointsGained);
		}
		
		// Update amount of times studied today
		game.timesStudied[game.day-1] += 1;
		
		
		// Stop music
		backgroundMusic.stop();
		
		game.setScreen(game.map);
		
		// TODO - Change accordingly
		if(studyPointsGained < 30f) {
			game.addPopUpText(new PopUpText("You don't feel very productive...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(studyPointsGained < 50f) {
			game.addPopUpText(new PopUpText("You feel slightly productive", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(studyPointsGained < 70f) {
			game.addPopUpText(new PopUpText("You feel productive", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else if(studyPointsGained < 90f) {
			game.addPopUpText(new PopUpText("You feel very productive", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
		else {
			game.addPopUpText(new PopUpText("You feel extremely productive!", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
		}
	}
	
	private void logicStep(float delta) {
		world.step(Math.min(Gdx.graphics.getDeltaTime(), 0.15f), 3, 3);
	}
	
	@Override
	public void render(float delta) {
		if(minimised) return;
		
		// Clear the screen
		Gdx.gl.glClearColor(16/255f, 20/255f, 31/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
		
		// Get mouse position in world coordinates
		Vector3 mousePos = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1f));
		playerToMouse = (float) Math.atan2(player.getPosition().x - mousePos.x, mousePos.y - player.getPosition().y);
		
		updateMusicVolume();
		
		
		game.batch.begin();
		
		cursorShip.setPosition(player.getPosition().x - cursorShip.getWidth()/2, player.getPosition().y - cursorShip.getHeight()/2);
		cursorShip.setRotation((float) Math.toDegrees(playerToMouse));
		cursorShip.draw(game.batch);
		
		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}
		
		game.batch.end();
				
		if(game.paused) return;
		
		movePlayer();
		
		game.batch.begin();
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && timeSinceAttack > attackCooldown) {
			attack(new Vector2(mousePos.x, mousePos.y));
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if(bullets.get(i).checkOutOfBounds()) {
				bullets.remove(i);
			}
		}
		
		for(int i = 0; i < enemyBullets.size(); i++) {
			enemyBullets.get(i).update();
			if(enemyBullets.get(i).checkOutOfBounds()) {
				enemyBullets.remove(i);
			}
		}
		
		game.batch.end();	
		
		// Uncomment this to display hitboxes
		//debugRenderer.render(world, game.camera.combined);
		
		// Accumulate the time
	    accumulator += delta;

	    // Update the physics world with a fixed timestep
	    while (accumulator >= fixedTimeStep) {
	        logicStep(fixedTimeStep);
	        accumulator -= fixedTimeStep;
	    }
	    
	    
		clock += Gdx.graphics.getDeltaTime();
		enemySpawnClock += Gdx.graphics.getDeltaTime();
		timeSinceAttack += Gdx.graphics.getDeltaTime();
		checkEnemySpawn();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if (enemyGrid[i][j] instanceof SniperBug) {
					((SniperBug) enemyGrid[i][j]).update();
			    } 
				else if (enemyGrid[i][j] instanceof ScatterBug) {
					((ScatterBug) enemyGrid[i][j]).update();
			    }
			 }
		}
		
		game.batch.begin();
		drawUI();
		game.batch.end();
		
		if(health <= 0f) {
			displayFinalScore();
		}
	}
	
	private void createShip() {
		// Create body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(game.camera.viewportWidth/2, game.camera.viewportHeight/2);
		player = world.createBody(bodyDef);

		// Create hitbox in form of circle
		CircleShape circle = new CircleShape();
		circle.setRadius(7f);

		// Create fixture definition
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.3f;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0;

		// Attach fixture to body
		@SuppressWarnings("unused")
		Fixture fixture = player.createFixture(fixtureDef);

		// Dispose hitbox
		circle.dispose();
	}
	
	private void createWalls() {
		// Floor
		BodyDef bodyDef = new BodyDef();  
		bodyDef.position.set(new Vector2(0, 0));  
		Body body = world.createBody(bodyDef);  
		PolygonShape box = new PolygonShape();  
		box.setAsBox(game.camera.viewportWidth, 0);
		body.createFixture(box, 0.0f);
		
		// Ceiling
		bodyDef.position.set(new Vector2(0, game.camera.viewportHeight));  
		body = world.createBody(bodyDef);  
		box.setAsBox(game.camera.viewportWidth, 0);
		body.createFixture(box, 0.0f);
		
		// Left Wall
		bodyDef.position.set(new Vector2(0, 0));  
		body = world.createBody(bodyDef);  
		box.setAsBox(0, game.camera.viewportHeight);
		body.createFixture(box, 0.0f);
		
		// Right Wall
		bodyDef.position.set(new Vector2(game.camera.viewportWidth, 0));  
		body = world.createBody(bodyDef);  
		box.setAsBox(0, game.camera.viewportHeight);
		body.createFixture(box, 0.0f);
		
		// Dispose hitbox
		box.dispose();

	}
	
	private void movePlayer() {
		// Apply impulse to body
		if (Gdx.input.isKeyPressed(Keys.W) && player.getLinearVelocity().y < 100000) {			
		     player.applyLinearImpulse(0, 200000*Gdx.graphics.getDeltaTime(), player.getPosition().x, player.getPosition().y, true);
		}
		if (Gdx.input.isKeyPressed(Keys.S) && player.getLinearVelocity().y > -100000) {			
		     player.applyLinearImpulse(0, -200000*Gdx.graphics.getDeltaTime(), player.getPosition().x, player.getPosition().y, true);
		}
		if (Gdx.input.isKeyPressed(Keys.A) && player.getLinearVelocity().x > -100000) {			
		     player.applyLinearImpulse(-200000*Gdx.graphics.getDeltaTime(), 0, player.getPosition().x, player.getPosition().y, true);
		}
		if (Gdx.input.isKeyPressed(Keys.D) && player.getLinearVelocity().x < 100000) {			
		     player.applyLinearImpulse(200000*Gdx.graphics.getDeltaTime(), 0, player.getPosition().x, player.getPosition().y, true);
		}
		
		// Rotate body
		player.setTransform(player.getPosition().x, player.getPosition().y, playerToMouse);
	}
	
	private void drawUI() {
		// Draw energy bar
		game.batch.draw(new Texture("BugFixerMinigame/HealthBar1.png"), game.camera.viewportWidth/2 - new Texture("BugFixerMinigame/HealthBar1.png").getWidth()/2, 350);
		TextureRegion region = new TextureRegion(new Texture("BugFixerMinigame/HealthBar2.png"), (int)Math.round(284f*(health/100f)), new Texture("BugFixerMinigame/HealthBar2.png").getHeight());
		game.batch.draw(region, game.camera.viewportWidth/2 - new Texture("BugFixerMinigame/HealthBar1.png").getWidth()/2, 350);
		game.batch.draw(new Texture("BugFixerMinigame/HealthBar3.png"), game.camera.viewportWidth/2 - new Texture("BugFixerMinigame/HealthBar1.png").getWidth()/2, 350);
		
		// Draw Score
		game.font.getData().setScale(0.3f); // Set font size
		game.font.draw(game.batch, "Score: " + Integer.toString(score), 540, 350, 100, Align.center, false);
	}
	
	private void displayFinalScore() {
		endGame();
	}
	
	private void checkEnemySpawn() {
		// Decrease time between enemy spawns as game progresses
		if(clock < 3f) {
			spawnTimer = 2f;
		}
		else if (clock < 7f) {
			spawnTimer = 1f;
		}
		else if (clock < 13f) {
			spawnTimer = 0.75f;
		}
		else if (clock < 20f) {
			spawnTimer = 0.5f;
		}
		else if (clock < 30f) {
			spawnTimer = 0.25f;
		}
		else {
			spawnTimer = 1f/((clock+10f)/10f);
		}
		
		
		// For first 10 seconds only spawn SniperBugs
		if(clock < 10f) {
			if(enemySpawnClock > spawnTimer) {
				spawnBug("SniperBug");
				enemySpawnClock = 0f;
			}
		}
		// After 10 seconds spawn ScatterBugs also
		else {
			int randomNumber = random.nextInt(10);
			if(enemySpawnClock > spawnTimer) {
				if(randomNumber < 3) {
					spawnBug("ScatterBug");
				}
				else {
					spawnBug("SniperBug");
				}
				enemySpawnClock = 0f;
			}
		}
		
	}
	
	private boolean spawnBug(String type) {
		// Spawn bug in a random, unoccupied slot
		int maxAttempts = 100; // Maximum attempts to avoid an infinite loop
        int attempts = 0;

        while (attempts < maxAttempts) {
            int randomRow = random.nextInt(rows);
            int randomColumn = random.nextInt(columns);

            if (enemyGrid[randomRow][randomColumn] == null) {
            	double distance = Math.sqrt(Math.pow((randomRow+1)*20-10 - player.getPosition().x, 2) + Math.pow((randomColumn+1)*20-10 - player.getPosition().y, 2));
                if (distance >= 50) {
                	if(type == "SniperBug") {
                		enemyGrid[randomRow][randomColumn] = new SniperBug(game, this, (randomRow+1)*20-10, (randomColumn+1)*20-10);
                	}
                	else if(type == "ScatterBug") {
                		enemyGrid[randomRow][randomColumn] = new ScatterBug(game, this, (randomRow+1)*20-10, (randomColumn+1)*20-10);
                	}
                	enemySpawn.play(game.volume);
                    return true; // Enemy successfully spawned
                }
            }

            attempts++;
        }

        System.out.println("Unable to find a valid spawn point after " + maxAttempts + " attempts. \n");
        return false; // No spawn location found
	}
	
	private void attack(Vector2 mousePos) {
		timeSinceAttack = 0f;
		
		bullets.add(new Bullet(game, this, player.getPosition().cpy().x, player.getPosition().cpy().y, mousePos, true));
		playerShot.play(game.volume);
	}
	
	private void updateMusicVolume() {
		float musicVolume = game.volume/2;
		backgroundMusic.setVolume(musicVolume);
	}
	
	@Override
	public void resize(int width, int height) {
		if(width == 0 && height == 0) {
			minimised = true;
			game.togglePause();
		}
		else {
			minimised = false;
		}
	}
	
	@Override
	public void hide() {
		// Reset custom cursor
		Pixmap pixmap = new Pixmap(Gdx.files.internal("UI/Cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Stop music
		backgroundMusic.stop();
	}
	
	@Override
	public void show() {
		// Set custom cursor
		Pixmap pixmap = new Pixmap(Gdx.files.internal("BugFixerMinigame/Crosshair.png"));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 16, 16);
		Gdx.graphics.setCursor(cursor);
				
		// Play music
		backgroundMusic.play();
	}

	@Override
	public void dispose() {
		
	}
}
