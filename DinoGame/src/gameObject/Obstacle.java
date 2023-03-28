package gameObject;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Obstacle
{	
	public static final float OBSTACLE_MAX_GAP_COEFFICIENT = 1.5f;
	public static final float OBSTACLE_GAP_COEFFICIENT = 0.6f;
	
	public abstract void update(int speed);
	public abstract void draw(Graphics g);
	public abstract Rectangle getBound();
	public abstract int getXpos();
	public abstract boolean isOutOfSceen();
}
