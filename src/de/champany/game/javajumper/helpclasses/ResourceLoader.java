package de.champany.game.javajumper.helpclasses;

import java.awt.image.*;

import javax.imageio.ImageIO;

public class ResourceLoader {
	public BufferedImage[] player;
	public BufferedImage loadingBackground, loadingChampany, background;
	public BufferedImage[] level = new BufferedImage[6];
	public BufferedImage[] block = new BufferedImage[256];

	public static BufferedImage loadImage(String src) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(ResourceLoader.class.getResourceAsStream(src));
		} catch (Exception ex) {
			System.err.println("Pfad: "+src);
		}
		return img;
	}
	
	public ResourceLoader() {
		level[0] = loadImage("/img/game/level.png");
		level[1] = loadImage("/img/game/level01.png");
		level[2] = loadImage("/img/game/level02.png");
		level[3] = loadImage("/img/game/level03.png");
		level[4] = loadImage("/img/game/level04.png");
		level[5] = loadImage("/img/game/level05.png");
		background = loadImage("/img/game/background.png");
		block = splitSpriteMap(loadImage("/img/game/blocks.png"), 16, 16);
		player = splitSpriteMap(loadImage("/img/game/player.png"),1,4);
		loadingChampany = loadImage("/img/loader/champany.png");
		loadingBackground = loadImage("/img/loader/background.png");
	}
	
	public BufferedImage[] splitSpriteMap(BufferedImage img, int sourceX, int sourceY) {
		BufferedImage[] output = new BufferedImage[sourceX*sourceY];
		int c = 0;
		for(int x=0; x<sourceX; x++) {
			for(int y=0; y<sourceY; y++) {
				output[c] = getSpriteOf(img, x, y);
				c++;
			}
		}
		return output;
	}
	
	public static BufferedImage getSpriteOf(BufferedImage src, int x, int y) {
		return src.getSubimage(y*25,x*25,25,25);
	}
	

}
