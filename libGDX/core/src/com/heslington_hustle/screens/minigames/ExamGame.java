package com.heslington_hustle.screens.minigames;

import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Screen;
import com.heslington_hustle.game.HeslingtonHustle;
import com.heslington_hustle.screens.GameOverScreen;
import com.heslington_hustle.screens.MinigameScreen;

public class ExamGame extends MinigameScreen implements Screen {

	public float score;
	private float scoreCap;
	private String grade;
	public HashMap<String, Float> difficultyScalars = new HashMap<>(); // How difficult is each exam minigame (Minigame name | DifficultyScalar)
	private int currentMinigame; // Index of current mingiame
	
	public ExamGame(HeslingtonHustle game, float difficultyScalar) {
		super(game, difficultyScalar);
	}
	
	@Override
	public void startGame() {
		// Code to start the exam
		score = 0f;
		scoreCap = 100f;

		// If went either 2 days without studying, or 1 day without studying and no day with more than one study, instant fail on exam (score capped at 39).
		int daysMissed = 0;
		int daysWithMultipleStudies = 0;
		for(int i = 0; i < 7; i++) {
			if(game.timesStudied[i] == 0) {
				daysMissed++;
			}
			else if(game.timesStudied[i] > 1) {
				daysWithMultipleStudies++;
			}
		}
		if((daysMissed > 0 && daysWithMultipleStudies < 1) || daysMissed > 1) {
			scoreCap = 39f;
		}
		
		// Check if any minigames haven't yet been played, and add them to the array
		if(!game.studyPoints.containsKey("BugFixer")) {
			game.studyPoints.put("BugFixer", 0f);
		}
		if(!game.studyPoints.containsKey("BookStacker")) {
			game.studyPoints.put("BookStacker", 0f);
		}
		// etc...
		
		// Calculate starting score and difficulty scalars based on amount studied in the game
		float studyPointsTotal = 0;
		Set<String> keySet = game.studyPoints.keySet(); // Get set of minigame names
		String[] minigames = keySet.toArray(new String[0]); // Convert set to array for indexing
		for(int i = 0; i < game.studyPoints.size(); i++) {
			float studyPoints = game.studyPoints.get(minigames[i]);
			studyPointsTotal += studyPoints;
			
			/* Either studying a minigame will make it easier when it shows up in the exam?
			 * OR
			 * Studying a minigame will give you a multiplier to your score with difficulty remaining constant?		
			 */
			
			
			// OPTION 1 - Studying a minigame will make it easier when it shows up in the exam
			
			// Very Easy difficulty
			if(studyPoints > 300f) { 
				difficultyScalars.put(minigames[i], 0.25f);
			}
			// Easy difficulty
			else if(studyPoints > 200f) {
				difficultyScalars.put(minigames[i], 0.5f);
			}
			// Default difficulty - studied this minigame 2-3 times with average performance
			else if(studyPoints > 120f) {
				difficultyScalars.put(minigames[i], 1f);
			}
			// Hard difficulty
			else if(studyPoints > 60f) {
				difficultyScalars.put(minigames[i], 1.5f);
			}
			// Very hard difficulty
			else {
				difficultyScalars.put(minigames[i], 2f);
			}
			
			/*
			// OPTION 2 - Studying a minigame will give you a multiplier to your score with difficulty remaining constant
			
			// 2x multiplier
			if(studyPoints > 300f) { 
				difficultyScalars.put(minigames[i], 2f);
			}
			// 1.5x multiplier
			else if(studyPoints > 200f) {
				difficultyScalars.put(minigames[i], 1.5f);
			}
			// No Multiplier - studied this minigame 2-3 times with average performance
			else if(studyPoints > 120f) {
				difficultyScalars.put(minigames[i], 1f);
			}
			// 0.5x multiplier
			else if(studyPoints > 60f) {
				difficultyScalars.put(minigames[i], 0.5f);
			}
			// 0.25x multiplier
			else {
				difficultyScalars.put(minigames[i], 0.25f);
			}
			*/	
			
			// Display game screen
			game.setScreen(this);
		}
		
		// Average starting score will be around 420?
		score = studyPointsTotal/20; // Temporary calculation
		if(score > scoreCap) {
			score = scoreCap;
		}
		else if (score > 60f) {
			score = 60f; // Can't get above 60%? without factoring exam performance in
		}
		
		// Display game screen
		game.setScreen(this);
		
		// Loads next study minigame
		currentMinigame = 0;
		loadNextMinigame();
	}
	
	private void endGame() {
		if(score > scoreCap) {
			score = scoreCap;
		}
		
		// Calculate final grade
		if(score < 40f) {
			grade = "Fail!";
		}
		else if(score < 50f) {
			grade = "3rd";
		}
		else if(score < 60f) {
			grade = "2:2";
		}
		else if(score < 70f) {
			grade = "2:1";
		}
		else {
			grade = "First!";
		}
		
		// Debug
		System.out.print("Score: " + score + "\n");
		System.out.print("Grade: " + grade + "\n");
		
		// Check if high score was beaten
		if(game.prefs.getInteger("highScore", 0) < Math.round(score) || ((game.prefs.getInteger("highScore", 0) == 0) && Math.round(score) > 0)) {
			game.prefs.putInteger("highScore", Math.round(score));
			game.prefs.flush();
			game.setScreen(new GameOverScreen(game, score, grade, true));
		}
		else {
			game.setScreen(new GameOverScreen(game, score, grade, false));
		}
		
	}
	
	public void loadNextMinigame() {
		// Remember only to load study minigames, you aren't being tested on recreation :)
		Set<String> keySet = game.studyPoints.keySet(); // Get set of minigame names
		String[] minigames = keySet.toArray(new String[0]); // Convert set to array for indexing
		
		// Check if this is the last minigame - TODO: Add rest of minigames
		if(currentMinigame < 2) { // TODO: Change to no. of minigames
			// Check minigame name and load relevant minigame
			if(minigames[currentMinigame] == "BugFixer") {
				game.minigames[0].startGame();
				game.minigames[0].difficultyScalar = difficultyScalars.get(minigames[currentMinigame]);
				game.minigames[0].exam = true;
			}
			if(minigames[currentMinigame] == "BookStacker") {
				game.minigames[1].startGame();
				game.minigames[1].difficultyScalar = difficultyScalars.get(minigames[currentMinigame]);
				game.minigames[1].exam = true;
			}
			currentMinigame++;
		}
		else {
			endGame();
		}	
	}
	
	@Override
	public void render(float delta) {
		
	}
	
	@Override 
	public void show() {
		
	}
	
	@Override
	public void hide() {
		
	}
	
	@Override
	public void dispose() {
		
	}
}
