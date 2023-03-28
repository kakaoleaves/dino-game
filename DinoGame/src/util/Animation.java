package util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation
{
	private int frameCount;
	private int frameDelay;
	private int currentFrame;
	private int animationDirection;
	private int totalFrames;
	
	private boolean stopped;
	private List<Frame> frames;
	
	public Animation(BufferedImage[] frames, int frameDelay) {
		this.frames = new ArrayList<Frame>();
		
		for (int i=0; i < frames.length; i++) {
			addFrame(frames[i], frameDelay);
		}
		
		this.frameCount = 0;
		this.frameDelay = frameDelay;
		this.stopped = false;
		this.currentFrame = 0;
		this.animationDirection = 1;
		this.totalFrames = this.frames.size();
	}

	public void update() {
        if (!stopped) {
            frameCount++;

//            if (frameCount > totalFrames -1) {
//            	currentFrame = 0;
//            }  else if (currentFrame < 0) {
//                currentFrame = totalFrames - 1;
//            }
            
            if (frameCount > frameDelay) {
                frameCount = 0;
                currentFrame += animationDirection;

                if (currentFrame > totalFrames - 1) {
                    currentFrame = 0;
                }
                else if (currentFrame < 0) {
                    currentFrame = totalFrames - 1;
                }
            }
        }

    }
	
	public void addFrame(BufferedImage frame, int duration) {
		if (duration <= 0) {
            System.err.println("Invalid duration: " + duration);
            throw new RuntimeException("Invalid duration: " + duration);
        }

        frames.add(new Frame(frame, duration));
        currentFrame = 0;
	}	
	
	public BufferedImage getSprite() {
		return frames.get(currentFrame).getFrame();
	}
}