package de.champany.game.javajumper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JavaJumper extends JApplet implements KeyListener{
	private static final long serialVersionUID = 1L;
	public static GameManager panel;
	public String version = "0.3a";
	public int build = 19;
	public boolean keyPressed[][] = new boolean[2][64];
	public enum screenType {
		applet, window
	}
	
	public static int keyToCode(int keyCode) {
		switch(keyCode) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
		case KeyEvent.VK_SPACE:
			return GameManager.KEY_UP;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			return GameManager.KEY_LEFT;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			return GameManager.KEY_DOWN;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			return GameManager.KEY_RIGHT;
		case KeyEvent.VK_R:
			return GameManager.KEY_RESTART;
		case KeyEvent.VK_F3:
			return GameManager.KEY_DEBUG;
		case KeyEvent.VK_C:
			return GameManager.KEY_CHEAT;
		case KeyEvent.VK_ESCAPE:
			System.exit(0); break;
		default: return 0;
		}
		return 0;
	}
	
	public void keyPressed(KeyEvent e) {
		keyPressed[0][JavaJumper.keyToCode(e.getKeyCode())] = true;
		panel.keyPressed(JavaJumper.keyToCode(e.getKeyCode()));
		
	}
	
	public void keyReleased(KeyEvent e) {
		keyPressed[0][JavaJumper.keyToCode(e.getKeyCode())] = false;
		panel.keyReleased(JavaJumper.keyToCode(e.getKeyCode()));
	}

	public void keyTyped(KeyEvent e) {
	}

	// TODO KeyReleaseBug töten!!!
	public JavaJumper() {	
		init(screenType.applet);
	}
	
	public JavaJumper(screenType screen) {	
		init(screen);
	}
	
	public void init(screenType screen) {
		Container content = null;
		JFrame window = null;
		if(screen == screenType.window) {
			window = new JFrame();
			window.setTitle("Java-Jumper "+this.version+" [by TheFridolin | BuildID #"+build+"]");
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.setResizable(false);
			content = window.getContentPane();
			content.setPreferredSize(new Dimension(800,600));
		} else if(screen == screenType.applet) {
			content = this.getContentPane();
			content.setPreferredSize(new Dimension(800,600));
		}
		panel = new GameManager();
		panel.start();
		content.add(panel);
		if(screen == screenType.window) {
			window.setIconImage(panel.res.player[0]);
			window.addKeyListener(this);
			window.pack();
			window.setLocationRelativeTo(null);
			window.setVisible(true);
			setFocusable(true);
			requestFocus();

		} else if(screen == screenType.applet) {
			addKeyListener(this);
			setVisible(true);
			setFocusable(true);
			requestFocus();
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {public void run() {
		    new JavaJumper(screenType.window);}});
	}
}
