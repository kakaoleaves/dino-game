package gameObject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import jade.Main;
import util.Animation;
import util.Sprite;

public class Pterosaur extends Obstacle
{
	private int xpos;
	private int ypos;
	private Rectangle bound;
	private int speed;
	
	private Animation pterosaurAnim;
	
	public Pterosaur(int xpos) {
		Random rand = new Random(System.currentTimeMillis());

		// random ypos
		ypos = rand.nextInt(80, 150);
		
		// pternosaur has random speed
		speed = rand.nextInt(0, 4) -1;

		BufferedImage[] pteroFrames = {Sprite.getSprite(191, 4, 62, 53), Sprite.getSprite(258, 4, 62, 53)}; 
		pterosaurAnim = new Animation(pteroFrames, 10);

		// random gap for obstacles
		int minGap = Math.round(speed * 62 + Obstacle.OBSTACLE_GAP_COEFFICIENT * Main.SCREEN_WIDTH);
		int maxGap = Math.round(Obstacle.OBSTACLE_MAX_GAP_COEFFICIENT * minGap);
		this.xpos = xpos + rand.nextInt(minGap, maxGap);

		bound = new Rectangle(xpos + 5, ypos + 10, pterosaurAnim.getSprite().getWidth() - 10, 28);
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
	
	@Override
	public void update(int speed) {
		// TODO Auto-generated method stub
		pterosaurAnim.update();
		xpos -= (speed + this.speed);		
		bound.x = xpos + 5;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(pterosaurAnim.getSprite(), xpos, ypos, null);
	}

	@Override
	public Rectangle getBound() {
		// TODO Auto-generated method stub
		return bound;
	}

	@Override
	public boolean isOutOfSceen() {
		// TODO Auto-generated method stub
		if (xpos <= -pterosaurAnim.getSprite().getWidth()) {
			return true;
		}		
		return false;
	}
}
