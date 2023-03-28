package jade;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import gameObject.Horizon;
import gameObject.ScoreBoard;
import gameObject.Trex;
import gameObject.Trex.TrexState;
import util.Sprite;
import util.Time;

public class Dinosaur extends JPanel implements Runnable, KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum GameState {
		READY, RUN, OVER
	};
	public static final int INITIAL_SPEED = 11;
	public static final int FPS = 60;
	public static final int NANO = 1000000;

	public double msPerFrame = (double) 1000 / FPS;
	public double lastFrameTime;

	private int speed; // game speed
	private GameState gameState; // READY, RUN, OVER

	private Trex trex;
	private Horizon horizon;
	private ScoreBoard scoreBoard;

	private BufferedImage gameOverImg;
	private BufferedImage replayButtonImg;

	private boolean isKeyPressed;
	
	public Dinosaur() {
		gameState = GameState.READY;
		trex = new Trex();
		horizon = new Horizon();
		scoreBoard = new ScoreBoard();
		lastFrameTime = 0;
		speed = 0;
		gameOverImg = Sprite.getSprite(939, 21, 277, 16);
		replayButtonImg = Sprite.getSprite(160, 96, 50, 44);
	}

	public void gameUpdate(double dt) {
		if (gameState == GameState.RUN) {
			trex.update();
			horizon.update(speed);
			scoreBoard.update(dt, msPerFrame, speed);
			if (horizon.checkCollision(trex)) {
				gameOver();
			}
		}
	}

	public void paint(Graphics g) {
		g.setColor(Color.decode("#f7f7f7"));
		g.fillRect(0, 0, getWidth(), getHeight());

		if (gameState != null) {
			switch (gameState) {
			case READY:
				trex.draw(g);
				break;
			case RUN:
				horizon.draw(g);
				trex.draw(g);
				scoreBoard.draw(g);
				break;
			case OVER:
				horizon.draw(g);
				trex.draw(g);
				scoreBoard.draw(g);
				g.drawImage(gameOverImg, 312, 50, null);
				g.drawImage(replayButtonImg, 425, 100, null);
				break;
			}
		}
	}
	
	public void runGame() {
		gameState = GameState.RUN;
		trex.setState(TrexState.RUNNING);
		this.speed = INITIAL_SPEED;
	}

	public void gameOver() {
		gameState = GameState.OVER;
		trex.setState(TrexState.CRASHED);
		scoreBoard.saveHighScoreFile();
		isKeyPressed = true;
		try {
			 Clip clip = AudioSystem.getClip();
			 File audioFile = new File("data//dead.wav");
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
		TimerTask gameOverTask = new TimerTask() {
			public void run() {
				isKeyPressed = false;
			}
		};
		Timer gameOverTimer = new Timer("gameOverTimer");
		long delay = 1500L;
		gameOverTimer.schedule(gameOverTask, delay);
	}

	public void resetGame() {
		trex.reset();
		horizon.reset();
		scoreBoard.reset();
		this.gameState = GameState.RUN;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long lastTime = 0;
		long elapsed;
		int msSleep;
		int nanoSleep;
		while (true) {
			
			double time = Time.getTime();
			double deltaTime = time - lastFrameTime;
			lastFrameTime = time;

			gameUpdate(deltaTime);
			repaint();
			elapsed = (lastTime + (long) (msPerFrame * NANO) - System.nanoTime());
			msSleep = (int) (elapsed / NANO);
			nanoSleep = (int) (elapsed % NANO);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep, nanoSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (!isKeyPressed) {
			isKeyPressed = true;
			switch (gameState) {
			case READY:
				if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP)
					runGame();
				break;
			case RUN:
				if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
					trex.startJump();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (trex.getState() == TrexState.JUMPING) {
						return;
					} else {
						trex.setState(TrexState.DUCKING);
					}
				}
				break;
			case OVER:
				if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
					resetGame();
				}
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		isKeyPressed = false;
		if (gameState == GameState.RUN && trex.getState() == TrexState.DUCKING) {
			trex.setState(TrexState.RUNNING);
		} else if (gameState == GameState.READY && trex.getState() == TrexState.JUMPING) {
			trex.setState(TrexState.WAITING);			
		}
	}
}