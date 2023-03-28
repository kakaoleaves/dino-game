package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite
{
	public static BufferedImage spriteSheet;
	public static BufferedImage loadSprite(String file) {
		BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File("data/" + file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {        	
            e.printStackTrace();
        }

        return sprite;
	}
	

    public static BufferedImage getSprite(int xGrid, int yGrid, int xSize, int ySize) {

        if (spriteSheet == null) {
            spriteSheet = loadSprite("dino");
        }

        return spriteSheet.getSubimage(xGrid, yGrid, xSize, ySize);
    }
}