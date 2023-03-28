package gameObject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import jade.Main;
import util.Sprite;

public class Cactus extends Obstacle
{	
	private int xpos;
	private int ypos;
	private Rectangle bound;
	private BufferedImage cactusImage;
	
	
	public Cactus(int xpos, int cactusType) {
		ypos = 210 - (cactusType == 0 ? 48 : 69);

		Random rand = new Random(System.currentTimeMillis());
		int cactusImageType = rand.nextInt(3);
		/* generate short cactus for type 0, and long cactus for type 1.
		 * cactus will be larger for bigger image type number
		 */
		if (cactusType == 0) { 
			switch (cactusImageType) {
			case 0:
				cactusImage = Sprite.getSprite(325, 3, 22, 48);
				break;
			case 1:
				cactusImage = Sprite.getSprite(350, 3, 46, 48);
				break;
			case 2:
				cactusImage = Sprite.getSprite(399, 3, 71, 48);
				break;
			}
		} else {
			switch (cactusImageType) {
			case 0:
				cactusImage = Sprite.getSprite(474, 3, 34, 69);
				break;
			case 1:
				cactusImage = Sprite.getSprite(511, 3, 70, 69);
				break;
			case 2:
				cactusImage = Sprite.getSprite(583, 3, 107, 69);
				break;
			}
		}
		
		// random gap for each obstacle
		int minGap = Math.round(Obstacle.OBSTACLE_GAP_COEFFICIENT * Main.SCREEN_WIDTH);
		int maxGap = Math.round(Obstacle.OBSTACLE_MAX_GAP_COEFFICIENT * minGap);
		this.xpos = xpos + rand.nextInt(minGap, maxGap);
		
		// bound should be a little bit smaller than the image, because the image isn't really rectangle.
		bound = new Rectangle(xpos + 5, ypos, cactusImage.getWidth() - 10, cactusImage.getHeight());
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
	
	public void setBound(Rectangle bound) {
		this.bound = bound;
	}

	public BufferedImage getCactusImage() {
		return cactusImage;
	}

	public void setCactusImage(BufferedImage cactusImage) {
		this.cactusImage = cactusImage;
	}

	@Override
	public void update(int speed) {
		// TODO Auto-generated method stub
		xpos -= speed;
		bound.x = xpos + 5;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(cactusImage, xpos, ypos, null);
	}

	@Override
	public Rectangle getBound() {
		// TODO Auto-generated method stub
		return bound;
	}

	@Override
	public boolean isOutOfSceen() {
		// TODO Auto-generated method stub
		if (xpos <= -cactusImage.getWidth()) {
			return true;
		}		
		return false;
	}

}
