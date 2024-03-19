/**
 * Represents the Squash minigame screen.
 * It extends the MinigameScreen class and implements the Screen interface.
 * Players control a paddle to hit a ball against a wall, earning points for each hit.
 */
package com.heslington_hustle.screens.minigames.Squash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class Squash extends MinigameScreen implements Screen {
	public World world; // Stores bodies and physics
	private boolean minimised;
	@SuppressWarnings("unused")
	private Box2DDebugRenderer debugRenderer; // Can be used to display hitboxes for testing
	private float energyGained;
	private float maxEnergyGained;
	public float health;
	private int score;
	public Body player;
	public Body ball;
	private float fixedTimeStep = 1 / 500f; // Time step for physics - adjust as needed
	private float accumulator = 0f; // Clock for physics, incremented every frame

	private float clock; // Clock, incremented every frame
	private Sprite userPaddle;
	private Sprite ballSprite;
	
	private Music backgroundMusic;
	private Sound hit;

	/**
     * Constructs a Squash minigame screen.
     * @param game The game instance.
     * @param difficultyScalar The scalar value representing the game difficulty.
     */
	public Squash(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		world = new World(new Vector2(0, 0), true); // Create world with no gravity
		debugRenderer = new Box2DDebugRenderer(); // Create hitbox renderer for testing
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 60f; // From best possible performance

		// Load textures
		Texture texture = new Texture("SquashMinigame/Paddle.png");
		userPaddle = new Sprite(texture);

		Texture ballTexture= new Texture("SquashMinigame/Ball.png");
		ballSprite = new Sprite(ballTexture);
		
		// Load Sounds
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/SquashMusic.ogg"));
		backgroundMusic.setLooping(true);
				
		hit = Gdx.audio.newSound(Gdx.files.internal("DrunkDancerMinigame/Hit.mp3"));

		// Set the contact listener to handle collisions
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();
		
        // Check if the collision is between the ball and player paddle
        if ((bodyA == ball && bodyB == player) || (bodyA == player && bodyB == ball)) {
            paddleHit(ball);
        }
					}
		
				@Override
				public void endContact(Contact contact) {
					// Handle end of contact if necessary
				}
		
				@Override
				public void preSolve(Contact contact, Manifold oldManifold) {
					// Handle pre-solve event if necessary
				}
		
				@Override
				public void postSolve(Contact contact, ContactImpulse impulse) {
					// Handle post-solve event if necessary
				}
				});
	}

	/**
	 * Adds score when the ball hits the paddle and fixes issues with vertical velocity.
	 * @param ball The ball body.
	 */
	private void paddleHit(Body ball) {
		hit.play(game.volume);
		score += 10;
		
		// Check if the ball has no vertical velocity and if so, apply it.
		if(ball.getLinearVelocity().y < 50f && ball.getLinearVelocity().y > -50f) {
        	ball.applyLinearImpulse(0, 100000, ball.getPosition().x, ball.getPosition().y, true);
        }
    }
	
	/**
     * Starts the game.
     * Resets energy gained and score, creates game elements, and displays tutorial screen.
     */
	@Override
	public void startGame() {
		// Code to restart the game
		energyGained = 20f; // From worst possible performance
		score = 0;
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		
		// Destroy any currently saved bodies
		Array<Body> bodies = new Array<Body>();
	    world.getBodies(bodies);
	    for(Body body: bodies){
	        world.destroyBody(body);
	    }
		
		// Create world boundaries and player body
		createPaddle();
		createBall();
		createWalls();
		startBallMovement();
	    
		game.setScreen(new InformationScreen(game, "squashTutorial", this));
	}
	
	/**
     * Ends the game and displays the final score.
     */
	private void endGame() {
		energyGained += score/5;
		
		// Check minigame high score
		if(game.prefs.getInteger("squashHighScore", 0) < score) {
			game.prefs.putInteger("squashHighScore", score);
			game.prefs.flush();
		}
		
		if(energyGained > maxEnergyGained) {
			energyGained = maxEnergyGained;
		}
		
		game.energyBar.addEnergy(energyGained);

		// Display final score
		game.setScreen(new InformationScreen(game, "recreationGameScore", game.map, score, energyGained));
	}
	
	/**
     * Creates the player's paddle body in the game world.
     */
	private void createPaddle() {
		// Create body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(10, game.camera.viewportHeight/2);
		player = world.createBody(bodyDef);

		// Create hitbox
		PolygonShape box = new PolygonShape();  
		box.setAsBox(15, 50);

		// Create fixture definition
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 1;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0;

		// Attach fixture to body
		@SuppressWarnings("unused")
		Fixture fixture = player.createFixture(fixtureDef);

		// Dispose hitbox
		box.dispose();
	}

	/**
     * Creates the ball body in the game world.
     */
	private void createBall() {
		// Create body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(game.camera.viewportHeight/2, game.camera.viewportHeight/2);
		ball = world.createBody(bodyDef);

		// Create hitbox in form of circle
		CircleShape circle = new CircleShape();
		circle.setRadius(7f);

		// Create fixture definition
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 1f;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 1;

		// Attach fixture to body
		@SuppressWarnings("unused")
		Fixture fixture = ball.createFixture(fixtureDef);

		// Dispose hitbox
		circle.dispose();
	}
	
	/**
     * Creates the boundaries of the game world.
     */
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

	/**
     * Performs one logic step in the physics world with a fixed timestep.
     * This method is called repeatedly to update the physics simulation.
     * @param delta The time step for the physics simulation.
     */
	private void logicStep(float delta) {
		// Define the base acceleration
	    float baseAccelerationX = 10f; // Adjust this value as needed
	    float baseAccelerationY = 0f; // Adjust this value as needed

	    // Retrieve the ball's current velocity
	    Vector2 velocity = ball.getLinearVelocity();

	    // Calculate the acceleration based on the current velocity direction
	    float accelerationX = velocity.x >= 0 ? baseAccelerationX : -baseAccelerationX;
	    float accelerationY = velocity.y >= 0 ? baseAccelerationY : -baseAccelerationY;

	    // Update the velocity based on the acceleration
	    velocity.x += accelerationX * delta * difficultyScalar * ((clock+1)/2);
	    velocity.y += accelerationY * delta * difficultyScalar * ((clock+1)/2);
	    ball.setLinearVelocity(velocity);

	    // Step the physics world
	    world.step(Math.min(delta, 0.15f), 3, 3);
	}

	/**
     * Renders the game screen.
     * @param delta The time elapsed since the last frame.
     */
	@Override
	public void render(float delta) {
		if(minimised) return;
		
		// Clear the screen
		Gdx.gl.glClearColor(16/255f, 20/255f, 31/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Set projection matrix of the batch to the camera
		game.batch.setProjectionMatrix(game.camera.combined);
		game.camera.update();
		
		game.batch.begin();
		
		// Draw paddle
		userPaddle.setPosition(player.getPosition().x - userPaddle.getWidth()/2, player.getPosition().y - userPaddle.getHeight()/2);
		userPaddle.draw(game.batch);

		// Draw ball
		ballSprite.setPosition(ball.getPosition().x - ballSprite.getWidth()/2, ball.getPosition().y - ballSprite.getHeight()/2);
		ballSprite.draw(game.batch);

		// Check if player has paused the game
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			game.togglePause();
		}
		if(game.paused) {
			game.pauseMenu();
		}
		
		// Draw Score
        game.font.getData().setScale(0.3f); // Set font size
        game.font.draw(game.batch, "Score: " + Integer.toString((int) (score)), 540, 350, 100, Align.center, false);

		game.batch.end();
	
		if(game.paused) return;
		// Anything that shouldn't happen while the game is paused should go here
		
		// Uncomment to display hitboxes for testing
		//debugRenderer.render(world, game.camera.combined);

		// Accumulate the time for physics
		accumulator += Gdx.graphics.getDeltaTime();
		clock += Gdx.graphics.getDeltaTime();

		// End the game if the ball goes past the paddle
		if (ball.getPosition().x <= 20) {
			endGame();
			return;
		}
		
		// Check if the user is issuing any move commands
		movePlayer();

		// Update the physics world with a fixed timestep
		while (accumulator >= fixedTimeStep) {
			logicStep(fixedTimeStep);
			accumulator -= fixedTimeStep;
		}
		
		updateMusicVolume();
	}
	
/**
 * Starts or resets the ball's movement.
 */
private void startBallMovement() {
    float initialSpeed = 100000f; // Set this to your desired initial ball speed
    float angle = MathUtils.random(-MathUtils.PI / 4, MathUtils.PI / 4); // Bias towards horizontal movement

    // Ensure the ball starts moving to the right by adding π/2 to the angle if it's negative
    if (angle < 0) {
        angle += 2 * MathUtils.PI;
    }

    float delta = fixedTimeStep; // Use the fixed time step
    Vector2 velocity = new Vector2(MathUtils.cos(angle) * initialSpeed * delta, MathUtils.sin(angle) * initialSpeed * delta);
    ball.setLinearVelocity(velocity);
}

/**
 * Moves the player's paddle based on user input.
 */
private void movePlayer() {
    float maxSpeed = 200000f; // Adjust this value as needed
    Vector2 velocity = player.getLinearVelocity();

    if (Gdx.input.isKeyPressed(Keys.W)) {
        player.setLinearVelocity(0, maxSpeed*fixedTimeStep);
    } else if (Gdx.input.isKeyPressed(Keys.S)) {
        player.setLinearVelocity(0, -maxSpeed*fixedTimeStep);
    } else if (velocity.y != 0) {
        // If no key is pressed and there is vertical movement, stop the paddle
        player.setLinearVelocity(0, 0);
    }
    player.setAngularVelocity(0);
}

/**
 * Called when the game window is resized.
 * @param width The new width of the window.
 * @param height The new height of the window.
 */
@Override
public void resize(int width, int height) {
	// Called when the game window is resized
	if(width == 0 && height == 0) {
		// Pause the game if the game window is minimised
		minimised = true;
		game.togglePause();
	}
	else {
		minimised = false;
	}
}

/**
 * Called when the screen is hidden.
 */
@Override
public void hide() {
	// Stop music
	backgroundMusic.stop();
}

/**
 * Called when the screen is shown.
 */
@Override
public void show() {
	// Play music
	backgroundMusic.play();
}

/**
 * Updates the volume of the background music based on game settings.
 */
private void updateMusicVolume() {
	float musicVolume = game.volume/2;
	backgroundMusic.setVolume(musicVolume);
}

}
