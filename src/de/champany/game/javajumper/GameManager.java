package de.champany.game.javajumper;

import java.awt.*;

import javax.swing.*;

import de.champany.game.javajumper.entity.MoveableGameObject;
import de.champany.game.javajumper.helpclasses.ResourceLoader;


import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GameManager extends JPanel {
	private static final long serialVersionUID = 755014834986695206L;
	
	public final static int FPS = 25;
	
	public ResourceLoader res;
	public GameLevel level;
	public MoveableGameObject player;
	
	public long tick = 0;
	public long lastNanoTime = 0;
	public long nanoTime = 0;
	public int levelID = 4;
	public enum gameState {
		notStarted,running,paused,noFocus,dead,won
	}
	
	public final static int KEY_UP = 1;
	public final static int KEY_LEFT = 2;
	public final static int KEY_DOWN = 3;
	public final static int KEY_RIGHT = 4;
	public final static int KEY_RESTART = 5;
	public static final int KEY_DEBUG = 6;
	public static final int KEY_CHEAT = 7;
	
	public GameManager() {
		res = new ResourceLoader();
		level = new GameLevel(this);
		level.loadFromImage(res.level[levelID]);
		player = new MoveableGameObject(this, res.player);
		player.init();
	}
	
	public void start() {
		tick = 0;
		Timer t = new Timer((int)1000/FPS, new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				nanoTime = System.nanoTime();
				tick++;
				player.doTick(nanoTime - lastNanoTime);
				doUpdate(tick);
				System.out.println((System.nanoTime()-nanoTime)/1000);
				repaint();
				lastNanoTime = nanoTime;
			}
		});
		t.start();
	}
	
	public void doUpdate(long ticks) {
		player.doUpdate(ticks);
		level.doUpdate(ticks);
	}
	
	public void keyPressed(int keyID) {
		switch(keyID) {
			case KEY_RIGHT:
				player.moveRight();
				break;
			case KEY_LEFT:
				player.moveLeft();
				break;
			case KEY_UP:
				player.moveJump();
				break;
			case KEY_RESTART:
				player.init();
				break;
			case KEY_DEBUG:
				debug();
				break;
			case KEY_CHEAT:
				cheat();
				break;
		}
	}
	
	public void cheat() {
		player.upSpeed = 5;
	}
	
	public void keyReleased(int keyID) {
		switch(keyID) {
		case KEY_RIGHT:
		case KEY_LEFT:
			player.moveStop();
			break;
		}
	}
	
	public void debug() {
		levelID++;
		if(levelID >= (res.level.length)) {
			levelID = 0;
		}
		level=new GameLevel(this);
		level.loadFromImage(res.level[levelID]);
		player = new MoveableGameObject(this, res.player);
		player.init();
	}
	
	public BufferedImage renderScreen() {
		BufferedImage screen = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) screen.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);				
		level.paint(g);
		player.doPaint(g);
		return screen;
	}
	
	public void paintComponent(Graphics gNot2D) {
		super.paintComponent(gNot2D);
		Graphics2D gWindow = (Graphics2D) gNot2D;
		gWindow.drawImage(renderScreen(), 0, 0, null);
		/*g.drawImage(res.loadingBackground, 0,0,800,600,null);
		g.drawImage(res.loadingChampany, 250,0,300,600,null);*/	}
}
