package com.heslington_hustle.game;

public class Minigame {
	private HeslingtonHustle game;
	
	public String name;
	public float energyChange;
	
	public Minigame(HeslingtonHustle game, String name, float energyChange) {
		this.game = game;
		this.name = name;
		this.energyChange = energyChange;
	}
	
	public void startMinigame(float difficultyScalar) {
		
	}
	
	public void endMinigame() {
		
	}
}
