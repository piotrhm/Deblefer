package com.example.deblefer.Multiplayer;

public class UserData {

	private String name;
	private int win;
	private int loss;
	
	public UserData (String name, int win, int loss) {
		this.name = name;
		this.win = win;
		this.loss = loss;
	}
	
	public String getName() { return name; }

	public int getWin() {
		return win;
	}
	
	public int getLoss() {
		return loss;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setWin(int win) {
		this.win = win;
	}
	
	public void setLoss(int loss) {
		this.loss = loss;
	}
	
}
