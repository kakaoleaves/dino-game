package gameObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import jade.Main;
import util.Sprite;

public class Cloud
{
	public static int MAX_SKY_LEVEL = 30;
	public static int MIN_SKY_LEVEL = 71;
	public static final double BG_CLOUD_SPEED = 1;

	private int xpos;
	private int ypos;
	BufferedImage cloudImage;
	
	public Cloud(int xpos) {
		Random rand = new Random(System.currentTimeMillis());
		// generate random ypos
		ypos = rand.nextInt(MAX_SKY_LEVEL, MIN_SKY_LEVEL);
		cloudImage = Sprite.getSprite(121, 2, 65, 20);

		// generate xpos with random gap between clouds
		this.xpos = xpos + rand.nextInt(80, Main.SCREEN_WIDTH);		
	}

	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public int getYpos() {
		return ypos;
	}

	public void setYpos(int ypos) {
		this.ypos = ypos;
	}

	public BufferedImage getCloudImage() {
		return cloudImage;
	}

	public void setCloudImage(BufferedImage cloudImage) {
		this.cloudImage = cloudImage;
	}
	
	public void update(int speed) {
		// cloud has its own speed
		xpos -= (speed - BG_CLOUD_SPEED);
	}
	
	public void draw(Graphics g) {
		g.drawImage(cloudImage, xpos, ypos, null);
	}
	
	public boolean isOutOfScreen() {
		if (xpos <= -65) {
			return true;
		}
		return false;
	}
}