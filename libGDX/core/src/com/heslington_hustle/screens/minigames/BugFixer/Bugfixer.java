package com.heslington_hustle.screens.minigames.BugFixer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.MinigameScreen;

public class Bugfixer extends MinigameScreen implements Screen {

	private World world;
	private Box2DDebugRenderer debugRenderer;
	private boolean minimised;
	
	private Body player;
	private float playerToMouse;
	
	private Sprite cursorShip;
	
	public Bugfixer(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
		world = new World(new Vector2(0, 0), true); // Create world with no gravity
		debugRenderer = new Box2DDebugRenderer();
		
		// Set custom cursor - TODO
		Pixmap pixmap = new Pixmap(Gdx.files.internal("UI/Cursor.png")); // TODO (crosshair?)
		Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
		Gdx.graphics.setCursor(cursor);
		
		// Load textures
		Texture texture = new Texture("BugFixerMinigame/Cursor.png");
		cursorShip = new Sprite(texture);
		
		createShip();
		createWalls();
		
		// If in borderless, set to fullscreen to pause game if unfocused
		if(game.isBorderless) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
	}
	
	private void logicStep(float delta) {
		world.step(delta, 3, 3);
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
		
		// Call parent render method
		super.render(delta);
		
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
		
		
		movePlayer();
		
		if(game.paused) return;
		debugRenderer.render(world, game.camera.combined);
		logicStep(delta);
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
		if (Gdx.input.isKeyPressed(Keys.W) && player.getLinearVelocity().y < 10000) {			
		     player.applyLinearImpulse(0, 200000*Gdx.graphics.getDeltaTime(), player.getPosition().x, player.getPosition().y, true);
		}
		if (Gdx.input.isKeyPressed(Keys.S) && player.getLinearVelocity().y > -10000) {			
		     player.applyLinearImpulse(0, -200000*Gdx.graphics.getDeltaTime(), player.getPosition().x, player.getPosition().y, true);
		}
		if (Gdx.input.isKeyPressed(Keys.A) && player.getLinearVelocity().x > -10000) {			
		     player.applyLinearImpulse(-200000*Gdx.graphics.getDeltaTime(), 0, player.getPosition().x, player.getPosition().y, true);
		}
		if (Gdx.input.isKeyPressed(Keys.D) && player.getLinearVelocity().x < 10000) {			
		     player.applyLinearImpulse(200000*Gdx.graphics.getDeltaTime(), 0, player.getPosition().x, player.getPosition().y, true);
		}
		
		// Rotate body
		player.setTransform(player.getPosition().x, player.getPosition().y, playerToMouse);
	}
	
	private void endGame() {
		if(game.isBorderless) {
			Gdx.graphics.setUndecorated(true);
			Gdx.graphics.setWindowedMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		game.setScreen(game.map);
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
	public void dispose() {
		
	}
}
