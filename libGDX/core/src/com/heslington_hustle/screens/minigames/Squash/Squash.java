package com.heslington_hustle.screens.minigames.Squash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.InformationScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class Squash extends MinigameScreen implements Screen {
	public World world; // Stores bodies and physics
	private boolean minimised;
	private Box2DDebugRenderer debugRenderer; // Can be used to display hitboxes for testing
	private float energyGained;
	private float maxEnergyGained;
	public float health;
	private int score;
	public Body player;
	public Body ball;
	private float fixedTimeStep = 1 / 144f; // Time step for physics - adjust as needed
	private float accumulator = 0f; // Clock for physics, incremented every frame
    private float playerToMouse; // Angle of the player ship to the cursor (in radians, should be converted to degrees)
	private float maxStudyPointsGained;

	private float studyPointsGained;	
	private float clock; // Clock, incremented every frame
	private Sprite userPaddle; // Player paddle
	private Sprite ballSprite;

	public Squash(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		world = new World(new Vector2(0, 0), true); // Create world with no gravity
		debugRenderer = new Box2DDebugRenderer(); // Create hitbox renderer for testing
		this.energyGained = 20f; // From worst possible performance
		this.maxEnergyGained = 60f; // From best possible performance
		// TODO Auto-generated constructor stub

		// Load textures
		Texture texture = new Texture("BugFixerMinigame/Cursor.png");
		userPaddle = new Sprite(texture);

		Texture ballTexture= new Texture("BugFixerMinigame/Cursor.png");
		ballSprite = new Sprite(ballTexture);

		// Set the contact listener to handle collisions
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Body bodyA = contact.getFixtureA().getBody();
				Body bodyB = contact.getFixtureB().getBody();
		
        // Check if the collision is between the ball and player paddle
        if ((bodyA == ball && bodyB == player) || (bodyA == player && bodyB == ball)) {
            reverseBallDirection(ball);
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

	private void reverseBallDirection(Body ball) {

		score += 10;

        // Reverse the horizontal velocity of the ball to change its direction
        Vector2 velocity = ball.getLinearVelocity();
        ball.setLinearVelocity(velocity.x * -1, velocity.y);

		    // Factor by which the speed will increase upon collision
			float speedIncreaseFactor = 3f;

			// Calculate the new speed with an increase
			float newSpeedX = -velocity.x * speedIncreaseFactor;
			float newSpeedY = velocity.y * speedIncreaseFactor;
		
			// Set the new velocity to the ball with increased speed
			ball.setLinearVelocity(newSpeedX, newSpeedY);
    }
	
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
		startBallMovement();

		// Display tutorial - TODO: create a tutorial in InformationScreen and rename the string in the constructor below to fit
	    //game.setScreen(new InformationScreen(game, "THISMINIGAMETutorial", this));
	    
		// Display game screen - TODO: Remove, uncomment out for testing purposes
		game.setScreen(new InformationScreen(game, "pingPongtutorial", this));
		
		// Temporary - TODO: REMOVE
		//endGame();
	}
	
	private void endGame() {
		// Add difficulty modifier to score
		score = (int) (score*(1/difficultyScalar));
		
		// Calculate final score
		studyPointsGained += clock/3;
		studyPointsGained += score/10;
		
		// Check BugFixer high score
		if(game.prefs.getInteger("Squash", 0) < score) {
			game.prefs.putInteger("Squash", score);
			game.prefs.flush();
		}
		
		if(studyPointsGained > maxStudyPointsGained) {
			studyPointsGained = maxStudyPointsGained;
		}
		
		// Reset custom cursor 
		Pixmap pixmap = new Pixmap(Gdx.files.internal("UI/Cursor.png"));
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Add studyPoints score to total score for this minigame
		if(game.studyPoints.containsKey("Squash")) {
			game.studyPoints.put("Squash", (game.studyPoints.get("Squash") + studyPointsGained));
		}
		else {
			game.studyPoints.put("Squash", studyPointsGained);
		}
		
		// Update amount of times studied today
		game.timesStudied[game.day-1] += 1;

		// Display final score
		game.setScreen(new InformationScreen(game, "recreationGameScore", game.map, score, energyGained));
		
	}
	
	private void createPaddle() {
		// Create body definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(10, game.camera.viewportHeight/2);
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
		fixtureDef.density = 0.3f;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0;

		// Attach fixture to body
		Fixture fixture = ball.createFixture(fixtureDef);

		// Dispose hitbox
		circle.dispose();
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
		
		
		game.batch.begin();
		
		
		// CODE TO RENDER SPRITES GOES HERE
		// Draw player ship
		userPaddle.setPosition(player.getPosition().x - userPaddle.getWidth()/2, player.getPosition().y - userPaddle.getHeight()/2);
		userPaddle.setRotation((float) Math.toDegrees(playerToMouse));
		userPaddle.draw(game.batch);

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


		// Uncomment to display hitboxes for testing
		debugRenderer.render(world, game.camera.combined);

		// Accumulate the time for physics
		accumulator += Gdx.graphics.getDeltaTime();

		// Update the physics world with a fixed timestep
		while (accumulator >= fixedTimeStep) {
			logicStep(fixedTimeStep);
			accumulator -= fixedTimeStep;
		}
		
				
		if(game.paused) return;
		// Anything that shouldn't happen while the game is paused should go here

		if (ball.getPosition().x <= -10) {
			endGame();
			return;
		}

		
		// Check if the user is issuing any move commands
		movePlayer();
		checkBallBounds();

	}

// Add a method to start or reset the ball's movement
private void startBallMovement() {
    float initialSpeed = 60f; // Set this to your desired initial ball speed
    float angle = MathUtils.random(-MathUtils.PI / 4, MathUtils.PI / 4); // Bias towards horizontal movement

    // Ensure the ball starts moving to the right by adding π/2 to the angle if it's negative
    if (angle < 0) {
        angle += 2 * MathUtils.PI;
    }

    Vector2 velocity = new Vector2(MathUtils.cos(angle) * initialSpeed, MathUtils.sin(angle) * initialSpeed);
    ball.setLinearVelocity(velocity);
}

private void checkBallBounds() {
    Vector2 position = ball.getPosition();
    Vector2 velocity = ball.getLinearVelocity();

    // Vertical boundaries
    float lowerBound = 10;
    float upperBound = 350;
    // Right horizontal boundary
    float rightBound = 630;

    // Check vertical boundaries
    if (position.y <= lowerBound && velocity.y < 0) {
        // Ball is moving down and has hit the bottom boundary
        ball.setLinearVelocity(velocity.x, Math.abs(velocity.y));
    } else if (position.y >= upperBound && velocity.y > 0) {
        // Ball is moving up and has hit the top boundary
        ball.setLinearVelocity(velocity.x, -Math.abs(velocity.y));
    }

    // Check right horizontal boundary
    if (position.x >= rightBound && velocity.x > 0) {
        // Ball is moving right and has hit the right boundary
        ball.setLinearVelocity(-Math.abs(velocity.x), velocity.y);
    }
}

private void movePlayer() {
    float maxSpeed = 100f; // Adjust this value as needed
    Vector2 velocity = player.getLinearVelocity();

    if (Gdx.input.isKeyPressed(Keys.W)) {
        player.setLinearVelocity(velocity.x, maxSpeed);
    } else if (Gdx.input.isKeyPressed(Keys.S)) {
        player.setLinearVelocity(velocity.x, -maxSpeed);
    } else if (velocity.y != 0) {
        // If no key is pressed and there is vertical movement, stop the paddle
        player.setLinearVelocity(velocity.x, 0);
    }
}

}
