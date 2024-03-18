package com.heslington_hustle.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.game.PopUpText;
import com.heslington_hustle.screens.minigames.ExamGame;

public class Bed extends Object {
	private Sound sleep;
	private boolean sleeping;
	
	public Bed(HeslingtonHustle game, String name, Rectangle[] interactRegions, int screen, String tooltip, int tooltipX, int tooltipY, int nameX, int nameY) {
		super(game, name, interactRegions, screen, tooltip, tooltipX, tooltipY, nameX, nameY);
		// Load sleep sound
		this.sleep = Gdx.audio.newSound(Gdx.files.internal("Map/Sleep.mp3"));
	}
	
	public void startNewDay() {
		if(sleeping) return; // Avoid accidental sleep
		
		// Adds less energy the later you sleep
		if(game.time == 24) {
			game.energyBar.addEnergy(40f);
		}
		else if(game.time > 22) {
			game.energyBar.addEnergy(60f);
		}
		else {
			game.energyBar.addEnergy(80f);
		}
		
		// Fade to black and play sleep sound
		game.fadeToBlack();
		sleep.play(game.volume);
		sleeping = true;
		
		// Wait 2 seconds for the fade to go away
		Timer.schedule(new Task() {
		    @Override
		    public void run() {
		    	// If not eaten that day, start next day with less energy
				if(game.timesEatenToday < 1) {
					game.energyBar.addEnergy(-30f);
					game.addPopUpText(new PopUpText("You feel hungry...", 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
				}
				
				game.time = 8; // Set time to 8am
				game.day++; // Cycle day
				game.timesEatenToday = 0;
				if(game.day > 7) {
					startExam();
				}
				else {
					game.addPopUpText(new PopUpText("DAY " + Integer.toString(game.day), 250, 300, 100, Align.center, false, 0.4f, new Color(1,1,1,1)), 2);
				}
				sleeping = false;
		    }
		}, 1f);
	}
	
	private void startExam() {
		// Starts the exam game at the end of the 7th day
		game.exam.startGame();
	}

}
