package gameObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import util.Sprite;

public class Ground
{
	public static final int GROUND_YPOS = 190;
	public static final int GROUND_WIDTH = 581;

	int xpos;
	BufferedImage groundImage;
	
	public Ground(int xpos) {
		this.xpos = xpos;
		Random rand = new Random(System.currentTimeMillis());
		// generate random ground images with 3 types
		groundImage = Sprite.getSprite(GROUND_WIDTH * (rand.nextInt(3)) + 1, 75, GROUND_WIDTH, 20); 
	}
	
	public int getXpos() {
		return xpos;
	}

	public void setXpos(int xpos) {
		this.xpos = xpos;
	}

	public void draw(Graphics g) {
		g.drawImage(groundImage, xpos, GROUND_YPOS, null);
	}
	
	public void update(int speed) {
		xpos -= speed;
	}
}
