/*package de.champany.game.javajumper.helpclasses;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class Misc {
	public static void makeScreenShot(JPanel p, int width, int height, String folder) {
		BufferedImage screenShot = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = screenShot.createGraphics();
		p.paint(g);
		try {
			File file = new File("C:/LMASS/");
			file.mkdirs();
			ImageIO.write(screenShot, "png", new File(System.getProperties(). + System.currentTimeMillis() + ".png"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		g.dispose();
		screenShot.flush();
	}
}
*/