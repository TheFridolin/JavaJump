package de.champany.game.javajumper;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameLevel {
	public final int width = 32;
	public final int height = 24;
	public final int blockHeight = 25;
	public int[] playerSpawn = {50,50};
	public GameManager g;
	public int[][] map = new int[width][height];
	public boolean[] lever = {false, false, false, false};
	public long[] leverActivatable = {0, 0, 0, 0};
	
	public static final int BLOCK_AIR = 0;
	public static final int[] BLOCK_NORMAL = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
	public static final int[][] BLOCK_LEVER = {{21,22,23,24,25,26},
													{28,29,30,31,32,33},
													{34,35,36,37,38,39},
													{40,41,42,43,44,45}};
	public boolean updated = false;
	
	public void switchLever(int id, boolean toState) {
		if(g.tick >= leverActivatable[id]) {
			leverActivatable[id] = g.tick+10;
			lever[id] = toState;
			updated = false;
		}
	}
	
	public GameLevel(GameManager g) {
		this.g = g;
	}
	
	public int getWidth() {
		return width*blockHeight;
	}
	
	public int getHeight() {
		return height*blockHeight;
	}

	public void doUpdate(long ticks) {
		if(!updated) {
			for(int y=0;y<=23;y++) {
				for(int x=0;x<=31;x++) {
					updateBlock(x,y);
				}
			}
		}
		updated = true;
	}
	
	public void updateBlock(int x, int y) {
		updateLeverBlockSet(x,y);
	}
	
	private void updateLeverBlockSet(int x, int y) {
		for (int i=0;i < BLOCK_LEVER.length;i++) {
			if(map[x][y] == BLOCK_LEVER[i][0] || map[x][y] == BLOCK_LEVER[i][1]) {
				if(lever[i]) {
					map[x][y] = BLOCK_LEVER[i][0];
				} else {
					map[x][y] = BLOCK_LEVER[i][1];
				}
			} else if(map[x][y] == BLOCK_LEVER[i][2] || map[x][y] == BLOCK_LEVER[i][3]) {
				if(lever[i]) {
					map[x][y] = BLOCK_LEVER[i][2];
				} else {
					map[x][y] = BLOCK_LEVER[i][3];
				}
			} else if(map[x][y] == BLOCK_LEVER[i][4] || map[x][y] == BLOCK_LEVER[i][5]) {
				if(lever[i]) {
					map[x][y] = BLOCK_LEVER[i][4];
				} else {
					map[x][y] = BLOCK_LEVER[i][5];
				}
			}
		}
	}
	
	public void render(Graphics2D g) {
		g.drawImage(this.g.res.background, 0, 0, 800, 600, null);
		for(int y=0;y<=23;y++) {
			for(int x=0;x<=31;x++) {
				BufferedImage imgToDraw = null;
				if(map[x][y]<=255) {
					imgToDraw = this.g.res.block[map[x][y]];
				}
				g.drawImage(imgToDraw,25*x,25*y,25,25,null);
			}
		}
	}
	
	public void paint(Graphics2D g) {
		render(g);
	}

	public void loadFromImage (BufferedImage level) {
		for(int y=0;y<=23;y++) {
			for(int x=0;x<=31;x++) {
				Color color = new Color(level.getRGB(x,y));
				int colorHTML = color.getRed()*256*256+color.getGreen()*256+color.getBlue();
				int block = 0;
				switch(colorHTML) {
					// LUFT
					case 0xFFFFFF: block=0; break;
					// BLOECKE
					case 0xFFFF00: block=1; break;
					case 0xFF8800: block=2; break;
					case 0xFF0000: block=3; break;
					case 0xFFAAAA: block=4; break;
					case 0xFF0066: block=5; break;
					case 0xDD00DD: block=6; break;
					case 0x0000FF: block=7; break;
					case 0x44ffff: block=8; break;
					case 0x88BBFF: block=9; break;
					case 0x00FF00: block=10; break;
					case 0x009900: block=11; break;
					case 0x995500: block=12; break;
					case 0x111111: block=13; break;
					case 0x444444: block=14; break;
					case 0xbbbbbb: block=15; break;
					case 0xdddddd: block=16; break;
					
					case 0x666666: block=17; break;
					case 0x999999: block=18; break;

					case 0xcccc00: block=19; break;
					case 0xaaaa00: block=20; break;
					// SCHALTER
					case 0xff2222: block=24; break;
					case 0xff7777: block=22; break;
					case 0xff5555: block=26; break;
					
					case 0xffff22: block=31; break;
					case 0xffff77: block=29; break;
					case 0xffff55: block=33; break;
					// TODO RICHTIGE ZAHLEN
					case 0x22ff22: block=37; break;
					case 0x77ff77: block=35; break;
					case 0x55ff55: block=39; break;

					case 0x2222ff: block=43; break;
					case 0x7777ff: block=41; break;
					case 0x5555ff: block=45; break;					
					case 0xfffff1: block=46; break;					
					
					case 0x99aa77: block=27; break;
					//Spieler :D
					case 0x000000: block=00;
					playerSpawn[0]=x*blockHeight;
					playerSpawn[1]=y*blockHeight;
					default: block=0;
				}
				map[x][y]=block;
			}
		}	
	}
	
	public int pixelBlockID(double x, double y) {
		return (int) map[posGridX(x)][posGridY(y)];
	}
	
	public int posGridX(double x) {
		return posGrid(x,width);
	}

	public int posGridY(double y) {
		return posGrid(y,height);
	}
	
	public int posGrid(double pos, int max) {
		int gridY = (int) Math.floor((pos-1)/25);
		max-=1;
		while(gridY < 0) {
			gridY += max;
		}
		while(gridY > max) {
			gridY -= max;
		}
		return gridY;
	}

	public boolean solidBlock(int id) {
		switch(id) {
			case 18: return false;
			case 22: return false;
			case 23: return false;
			case 29: return false;
			case 30: return false;			
			case 35: return false;
			case 36: return false;			
			case 41: return false;
			case 42: return false;			
			case 46: return false;			
			case 0: return false;
			default: return true;
		}
	}
}
