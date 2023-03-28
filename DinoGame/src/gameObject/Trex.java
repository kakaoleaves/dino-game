package gameObject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import util.Animation;
import util.Sprite;

public class Trex
{
	public enum TrexState {
		WAITING, RUNNING, CRASHED, JUMPING, DUCKING
	};

	public static final int GROUND_YPOS = 150;
	public static final float GRAVITY = 0.4f;

	private int xpos;
	private int ypos;
	private float speedY;

	// bound for ducking : ducking has rectangle image, so use this
	private Rectangle bound; 

	// bound for running or jumping : divide 3 bound for more accurate collision check
	private Rectangle headBound;
	private Rectangle bodyBound;
	private Rectangle footBound;

	private TrexState state;

	private BufferedImage ready;
	private BufferedImage jumping;
	private BufferedImage crashedImage;
	private Animation normalRunAnim;
	private Animation duckingAnim;

	public Trex(){
		xpos = 50;
		ypos = GROUND_YPOS;

		ready = Sprite.getSprite(55, 5, 64, 66);
		jumping = Sprite.getSprite(1220, 4, 60, 63);
		crashedImage = Sprite.getSprite(1476, 4, 60, 63);
		BufferedImage[] normalRunFrames = { Sprite.getSprite(1348, 4, 60, 63), Sprite.getSprite(1412, 4, 60, 63) };
		normalRunAnim = new Animation(normalRunFrames, 10);

		BufferedImage[] duckingFrames = { Sprite.getSprite(1603, 4, 80, 63), Sprite.getSprite(1689, 4, 80, 63) };
		duckingAnim = new Animation(duckingFrames, 10);

		headBound = new Rectangle(xpos + 30, ypos, 30, 20);
		bodyBound = new Rectangle(xpos, ypos + 22, 40, 25);
		footBound = new Rectangle(xpos + 15, ypos + 49, 20, 13);
		bound = new Rectangle(xpos, ypos + 27, duckingAnim.getSprite().getWidth(),
				duckingAnim.getSprite().getHeight() - 38);
		state = TrexState.WAITING;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public void setBound(Rectangle bound) {
		this.bound = bound;
	}

	public Rectangle getBound() {
		return bound;
	}

	public void setHeadBound(Rectangle bound) {
		this.headBound = bound;
	}

	public Rectangle getHeadBound() {
		return headBound;
	}

	public void setBodyBound(Rectangle bound) {
		this.bodyBound = bound;
	}

	public Rectangle getBodyBound() {
		return bodyBound;
	}

	public void setFootBound(Rectangle bound) {
		this.footBound = bound;
	}

	public Rectangle getFootBound() {
		return footBound;
	}

	public void setState(TrexState trexState) {
		state = trexState;
	}

	public TrexState getState() {
		return state;
	}

	public void draw(Graphics g) {
		switch (state) {
		case WAITING:
			g.drawImage(ready, xpos, ypos, null);
			break;
		case RUNNING:
			g.drawImage(normalRunAnim.getSprite(), xpos, ypos, null);
			break;
		case CRASHED:
			g.drawImage(crashedImage, xpos, ypos, null);
			break;
		case JUMPING:
			g.drawImage(jumping, xpos, ypos, null);
			break;
		case DUCKING:
			g.drawImage(duckingAnim.getSprite(), xpos, ypos, null);
			break;
		}
	}

	public void update() {
		normalRunAnim.update();
		duckingAnim.update();
		
		if (ypos >= GROUND_YPOS) {
			if (state != TrexState.DUCKING) {
				state = TrexState.RUNNING;
			}
		} else {
			// jump
			speedY += GRAVITY;
			ypos += speedY;
			headBound.y = ypos;
			bodyBound.y = ypos + 22;
			footBound.y = ypos + 49;
		}
		// update for running
		bound.x = xpos;
		headBound.x = xpos + 30;
		bodyBound.x = xpos;
		footBound.x = xpos + 15;
	}

	public void startJump() {
		if (state != TrexState.JUMPING) {
			try {
				 Clip clip = AudioSystem.getClip();
				 File audioFile = new File("data//jump.wav");
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
			if (ypos >= GROUND_YPOS) {
				speedY = -10.5f;
				ypos += speedY;
			}
			state = TrexState.JUMPING;
		}
	}

	public void reset() {
		speedY = 0;

		ypos = GROUND_YPOS;
		headBound.y = ypos;
		bodyBound.y = ypos + 22;
		footBound.y = ypos + 49;

		state = TrexState.RUNNING;
	}
}