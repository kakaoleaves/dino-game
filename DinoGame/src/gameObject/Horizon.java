package gameObject;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import gameObject.Trex.TrexState;
import jade.Main;

public class Horizon
{
	public static final int MAX_CLOUDS = 6;

	public static final int MAX_OBSTACLE_LENGTH = 3;
	public static final int MAX_OBSTACLE_DUPLICATION = 2;

	private LinkedList<Cloud> clouds;
	private LinkedList<Ground> grounds;

	private LinkedList<Obstacle> obstacles;
	private int obstacleDuplication = 0; // check duplication of obstacles
	private int obstacleType = 0; // previous obstacle type for duplication check
	private Random rand;

	public Horizon() {
		rand = new Random(System.currentTimeMillis());

		// generate clouds
		clouds = new LinkedList<>();
		clouds.add(new Cloud(0));
		clouds.add(new Cloud(clouds.getLast().getXpos()));

		// generate grounds
		grounds = new LinkedList<>();
		int numOfGround = (int) Math.ceil((double) Main.SCREEN_WIDTH / Ground.GROUND_WIDTH) + 2;

		for (int i = 0; i < numOfGround; i++) {
			grounds.add(new Ground(i * Ground.GROUND_WIDTH));
		}

		// generate obstacle
		obstacles = new LinkedList<>();
		obstacleType = rand.nextInt(2);

		switch (obstacleType) {
		case 0: // small cactus
			obstacles.add(new Cactus(Ground.GROUND_WIDTH, 0));
			break;
		case 1: // big cactus
			obstacles.add(new Cactus(Ground.GROUND_WIDTH, 1));
			break;
		}
	}

	public void update(int speed) {
		ListIterator<Cloud> cloudItertor = clouds.listIterator();
		ListIterator<Ground> groundItertor = grounds.listIterator();
		ListIterator<Obstacle> obstacleIterator = obstacles.listIterator();

		// update all clouds' xpos
		while (cloudItertor.hasNext()) {
			cloudItertor.next().update(speed);
		}

		// check whether the first cloud is out of screen. If it is true, remove
		Cloud cloudsHead = clouds.getFirst();
		if (cloudsHead.isOutOfScreen()) {
			clouds.removeFirst();
		}

		// generate clouds for infinite horizon
		while (clouds.size() < MAX_CLOUDS) {
			Cloud cloudsTail = clouds.getLast();
			clouds.add(new Cloud(cloudsTail.getXpos()));
		}

		// update all grounds' xpos
		while (groundItertor.hasNext()) {
			groundItertor.next().update(speed);
		}

		// check whether first ground is out of range
		Ground groundsHead = grounds.getFirst();
		if (groundsHead.getXpos() <= -Ground.GROUND_WIDTH) {
			Ground groundsTail = grounds.getLast();
			grounds.removeFirst();
			grounds.add(new Ground(groundsTail.getXpos() + Ground.GROUND_WIDTH));
		}

		// update all obstacles' xpos
		while (obstacleIterator.hasNext()) {
			obstacleIterator.next().update(speed);
		}

		// check whether the first obstacle is out of screen. If it is true, remove
		Obstacle obstaclesHead = obstacles.getFirst();

		if (obstaclesHead.isOutOfSceen()) {
			obstacles.removeFirst();
		}

		// generate obstacles for infinite horizon
		while (obstacles.size() < MAX_OBSTACLE_LENGTH) {
			Obstacle obstaclesTail = obstacles.getLast();
			int obtype = rand.nextInt(3);

			// check duplication for obstacles
			if (obtype == obstacleType) {
				obstacleDuplication++;
			}
			if (obstacleDuplication > MAX_OBSTACLE_DUPLICATION) {
				obstacleType += 1;
				obstacleType %= 3;
				obstacleDuplication = 0;
			} else {
				obstacleType = obtype;
			}

			switch (obstacleType) {
			case 0: // small cactus
				obstacles.add(new Cactus(obstaclesTail.getXpos(), 0));
				break;
			case 1: // big cactus
				obstacles.add(new Cactus(obstaclesTail.getXpos(), 1));
				break;
			case 2: // pterosaur
				obstacles.add(new Pterosaur(obstaclesTail.getXpos()));
				break;
			}
		}
	}

	public void draw(Graphics g) {
		ListIterator<Cloud> cloudItertor = clouds.listIterator();
		ListIterator<Ground> groundItertor = grounds.listIterator();
		ListIterator<Obstacle> obstacleIterator = obstacles.listIterator();

		while (cloudItertor.hasNext()) {
			cloudItertor.next().draw(g);
		}

		while (groundItertor.hasNext()) {
			groundItertor.next().draw(g);
		}

		while (obstacleIterator.hasNext()) {
			obstacleIterator.next().draw(g);
		}
	}

	public boolean checkCollision(Trex trex) {
		// check the collision between obstacles and trex
		ListIterator<Obstacle> obstacleIterator = obstacles.listIterator();
		while (obstacleIterator.hasNext()) {
			Rectangle obstacleBound = obstacleIterator.next().getBound();
			if (trex.getState() == TrexState.DUCKING) {
				if (obstacleBound.intersects(trex.getBodyBound()))
					return true;
			} else {
				if (obstacleBound.intersects(trex.getHeadBound()) || obstacleBound.intersects(trex.getBodyBound())
						|| obstacleBound.intersects(trex.getFootBound()))
					return true;
			}
		}
		return false;

	}

	public void reset() {
		// clear the obstacles and generate new one
		obstacles.clear();

		obstacleType = rand.nextInt(2);

		switch (obstacleType) {
		case 0:
			obstacles.add(new Cactus(Ground.GROUND_WIDTH, 0));
			break;
		case 1:
			obstacles.add(new Cactus(Ground.GROUND_WIDTH, 1));
			break;
		}
	}
}
