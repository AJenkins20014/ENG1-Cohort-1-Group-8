package com.heslington_hustle.screens.minigames.BookStacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.heslington_hustle.game.HeslingtonHustle;

public class BookSegment {
	public Texture sprite;
	private HeslingtonHustle game;
	private float x;
	private float y;
	public int i;
	public int j;
	enum State {
		MOVING,
		STATIONARY,
		FALLING
	}
	enum Position{
		MIDDLE,
		LEFT_END,
		RIGHT_END
	}
	
	public State state;
	public Position  position;
	public static int counter;
	public BookSegment(HeslingtonHustle game,int i,int j,Position position,State state) {
		this.game = game;
		this.sprite = new Texture("BookStackerMinigame/book.png");
		this.i = i;
		this.j = j;
		this.setX(j*40);
		this.setY(i*40);
		this.state = state;
		this.position = position; 
		
		
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void update() {
		
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)&& this.state == State.MOVING && BookStacker.blockDrop == false) {
				this.state = State.FALLING;
				BookStacker.changeHeightandSpeed = true;
				BookStacker.bookStack.play(game.volume);
			
			
		}
		if (BookStacker.clock >= 0.75*BookStacker.fallSpeed){
			if(this.state == State.MOVING){
				move();
			} 
			
		}
		if (BookStacker.clock >=0.01){
			if(this.state == State.FALLING) {
				fall();
			}
		}
		drawSprite();
	}
	
	private void move() {
		
		if(BookStacker.currentLength > 2){
		
			// direction right on right and edge swap move left
			if (BookStacker.direction == BookStacker.Direction.RIGHT){
				if(this.position == Position.RIGHT_END && this.j ==15) {
					
					BookStacker.direction = BookStacker.Direction.LEFT;
					((BookSegment) BookStacker.bookGrid[this.i][this.j-1]).position = Position.RIGHT_END;
					((BookSegment) BookStacker.bookGrid[this.i][this.j-BookStacker.currentLength+1]).position = Position.MIDDLE;
					BookStacker.bookGrid[this.i][this.j-(BookStacker.currentLength)] = new BookSegment(game, i,this.j-(BookStacker.currentLength),Position.LEFT_END,State.MOVING);
					
					kill();
				}
			//direction right on left, move right
				else if(this.position == Position.LEFT_END && this.j != 16-BookStacker.currentLength) {
					((BookSegment) BookStacker.bookGrid[this.i][this.j+1]).position = Position.LEFT_END;
					((BookSegment) BookStacker.bookGrid[this.i][this.j+BookStacker.currentLength-1]).position = Position.MIDDLE;
					BookStacker.bookGrid[this.i][this.j+(BookStacker.currentLength)] = new BookSegment(game, i,this.j+(BookStacker.currentLength),Position.RIGHT_END,State.MOVING);
					kill();
				}
			//direction left on left and edge swap move right
			}
			else if(BookStacker.direction == BookStacker.Direction.LEFT) {
				 if(this.position == Position.LEFT_END && this.j ==0){
					
					BookStacker.direction = BookStacker.Direction.RIGHT;
					((BookSegment) BookStacker.bookGrid[this.i][this.j+1]).position = Position.LEFT_END;
					((BookSegment) BookStacker.bookGrid[this.i][this.j+BookStacker.currentLength-1]).position = Position.MIDDLE;
					BookStacker.bookGrid[this.i][this.j+(BookStacker.currentLength)] = new BookSegment(game, i,this.j+(BookStacker.currentLength),Position.RIGHT_END,State.MOVING);
					kill();
				}
			//direction left on right edge, swap move left
				else if(this.position == Position.RIGHT_END && this.j != -1+BookStacker.currentLength) {
					((BookSegment) BookStacker.bookGrid[this.i][this.j-1]).position = Position.RIGHT_END;
					((BookSegment) BookStacker.bookGrid[this.i][this.j-BookStacker.currentLength+1]).position = Position.MIDDLE;
					BookStacker.bookGrid[this.i][this.j-(BookStacker.currentLength)] = new BookSegment(game, i,this.j-(BookStacker.currentLength),Position.LEFT_END,State.MOVING);
					kill();
				}
			}
		}
		else if(BookStacker.currentLength > 1) {
			//CHANGE TO LEFT
			if (BookStacker.direction == BookStacker.Direction.RIGHT){
				if(this.position == Position.RIGHT_END && this.j ==15) {
					
					BookStacker.direction = BookStacker.Direction.LEFT;
					((BookSegment) BookStacker.bookGrid[this.i][this.j-1]).position = Position.RIGHT_END;
					BookStacker.bookGrid[this.i][this.j-(BookStacker.currentLength)] = new BookSegment(game, i,this.j-(BookStacker.currentLength),Position.LEFT_END,State.MOVING);
					
					kill();
				}
				//MOVE RIGHT
				else if(this.position == Position.LEFT_END && this.j != 16-BookStacker.currentLength) {
					((BookSegment) BookStacker.bookGrid[this.i][this.j+1]).position = Position.LEFT_END;
					BookStacker.bookGrid[this.i][this.j+(BookStacker.currentLength)] = new BookSegment(game, i,this.j+(BookStacker.currentLength),Position.RIGHT_END,State.MOVING);
					kill();
				}
			}
			//CHANGE TO RIGHT
			else if(BookStacker.direction == BookStacker.Direction.LEFT) {
				 if(this.position == Position.LEFT_END && this.j ==0){
					
					BookStacker.direction = BookStacker.Direction.RIGHT;
					((BookSegment) BookStacker.bookGrid[this.i][this.j+1]).position = Position.LEFT_END;
					BookStacker.bookGrid[this.i][this.j+(BookStacker.currentLength)] = new BookSegment(game, i,this.j+(BookStacker.currentLength),Position.RIGHT_END,State.MOVING);
					kill();
				}
				//MOVE LEFT
				else if(this.position == Position.RIGHT_END && this.j != -1+BookStacker.currentLength) {
					((BookSegment) BookStacker.bookGrid[this.i][this.j-1]).position = Position.RIGHT_END;
					BookStacker.bookGrid[this.i][this.j-(BookStacker.currentLength)] = new BookSegment(game, i,this.j-(BookStacker.currentLength),Position.LEFT_END,State.MOVING);
					kill();
				}
			}
		}
		else {
			if (BookStacker.direction == BookStacker.Direction.RIGHT){
				if(this.j ==15) {
					BookStacker.direction = BookStacker.Direction.LEFT;
					BookStacker.bookGrid[this.i][this.j-(BookStacker.currentLength)] = new BookSegment(game, i,this.j-(BookStacker.currentLength),Position.LEFT_END,State.MOVING);
					kill();
				}
				else {
					BookStacker.bookGrid[this.i][this.j+(BookStacker.currentLength)] = new BookSegment(game, i,this.j+(BookStacker.currentLength),Position.RIGHT_END,State.MOVING);
					kill();
				}
			}
			else if (BookStacker.direction == BookStacker.Direction.LEFT){
				if(this.j ==0) {
					BookStacker.direction = BookStacker.Direction.RIGHT;
					BookStacker.bookGrid[this.i][this.j+(BookStacker.currentLength)] = new BookSegment(game, i,this.j+(BookStacker.currentLength),Position.LEFT_END,State.MOVING);
					kill();
				}
		
				else {
					BookStacker.bookGrid[this.i][this.j-(BookStacker.currentLength)] = new BookSegment(game, i,this.j-(BookStacker.currentLength),Position.RIGHT_END,State.MOVING);
					kill();
				}
				
			}	
				
		}
	}
	public void fall() {
		//STOP FALLING
		if(this.i == BookStacker.currentHeight){
			this.state = State.STATIONARY;
			counter +=1;
			if(BookStacker.bookGrid[this.i-1][this.j] == null) { //// DELETES SEGMENTS THAT HIT NOTHING
				counter -=1;
				BookStacker.currentLength -= 1;
				for (int row = 0; row < 7; row++) { 
					if (BookStacker.bookGrid[row][j] instanceof BookSegment) {
						((BookSegment) BookStacker.bookGrid[row][this.j]).kill();
					}
					
				}
					
				
			}
		}
		//FALL
		else {
			BookStacker.bookGrid[this.i-1][this.j] = new BookSegment(game, this.i-1,this.j,this.position,this.state);
			kill();
			
		}
		
		
	}
	
	private void drawSprite() {
		// TODO Auto-generated method stub
		game.batch.begin();
		game.batch.draw(sprite,x,y);
		game.batch.end();
	}
		
	public void kill() {
		BookStacker.clock = 0; // This resets clock after a move is made
		this.sprite.dispose();
		 BookStacker.bookGrid[this.i][this.j] = null;
		
	
	}

}
