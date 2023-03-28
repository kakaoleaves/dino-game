package gameObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import util.Sprite;

public class ScoreBoard
{
	private double score;
	private int highscore;
	BufferedImage highImage;
	BufferedImage[] numbers;
	
	public ScoreBoard() {
		score = 0;
		highscore = 0;

		// get high score data from file
		try {
			FileInputStream fileReader = new FileInputStream ("data//score.data");
			ObjectInputStream binaryReader = new ObjectInputStream(fileReader);
			highscore = binaryReader.readInt();
			binaryReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		highImage = Sprite.getSprite(1084, 1, 28, 16);
		numbers = new BufferedImage[10];
		numbers[0] = Sprite.getSprite(939, 1, 14, 16);
		numbers[1] = Sprite.getSprite(954, 1, 14, 16);
		numbers[2] = Sprite.getSprite(968, 1, 14, 16);
		numbers[3] = Sprite.getSprite(982, 1, 14, 16);
		numbers[4] = Sprite.getSprite(997, 1, 14, 16);
		numbers[5] = Sprite.getSprite(1011, 1, 14, 16);
		numbers[6] = Sprite.getSprite(1026, 1, 14, 16);
		numbers[7] = Sprite.getSprite(1041, 1, 14, 16);
		numbers[8] = Sprite.getSprite(1055, 1, 14, 16);
		numbers[9] = Sprite.getSprite(1069, 1, 14, 16);		
	}
	
	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setHighScore(int highscore) {
		this.highscore = highscore;
	}

	public int getHighScore() {
		return highscore;
	}
	

	public void update(double dt, double msPerFrame, int speed) {
		// audio play for every 100 score
		int prevScore = (int) Math.floor(score);
		score += (dt * speed * 10 / (msPerFrame));		
		int afterScore = (int) Math.floor(score);
		
		if (afterScore > 0 && afterScore % 100 == 0 && prevScore % 100 != 0) {
			try {
				 Clip clip = AudioSystem.getClip();
				 File audioFile = new File("data//scoreup.wav");
				 AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				 clip.open(audioStream);
				 clip.start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// update high score if current score is higher than it
		if (score > highscore) {
			highscore = (int) Math.floor(score);
		}
	}

	public void draw(Graphics g) {
		g.drawImage(highImage, 667, 10, null);
		
		
		for (int i = 0; i < 5; i++) { 
			g.drawImage(numbers[((int) Math.floor(highscore / Math.pow(10, i))) % 10], 770 - (15 * i), 10, null);			
		}
		
		for (int i = 0; i < 5; i++) {
			g.drawImage(numbers[((int) (Math.floor(score) / Math.pow(10, i))) % 10], 860 - (15 * i), 10, null);
		}
	}
	
	public void reset() {
		score = 0;
	}
	
	public void saveHighScoreFile() {
		// save high score to a file
		try {
			FileOutputStream fileStream = new FileOutputStream("data//score.data");
			ObjectOutputStream binaryWriter = new ObjectOutputStream(fileStream);
			binaryWriter.writeInt(highscore);
			binaryWriter.flush();
			binaryWriter.close();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
