package de.champany.game.javajumper.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import de.champany.game.javajumper.*;


public class MoveableGameObject {
	public double posX;
	public double posY;
	public double upSpeed;
	public boolean jumped;
	public boolean lookingRight;
	public boolean movingX;
	
	public final double speedFactor = 0.075d; // 3
	public final double jumpSpeed = 6.5d;
	public final double maxSpeed = 25d;
	public final double gravitationFactor = 0.01d; // 0.4
	public final int width = 15;
	public final int height = 23;
	public final int spriteWidth = 25;
	public final int spriteHeight = 25;
	
	public final int DIRECTION_UP = 0;
	public final int DIRECTION_LEFT = 1;
	public final int DIRECTION_DOWN = 2;
	public final int DIRECTION_RIGHT = 3;
	public final int DIRECTION_MIDX = 4;
	public final int DIRECTION_MIDY = 5;
	
	public byte spriteID = 0;
	GameManager g;
	BufferedImage sprite[];
	
	// Konstruktor
	
	public MoveableGameObject(GameManager g, BufferedImage[] sprite) {
		this.g = g;
		this.sprite = sprite;
	}
	
	// Zugriffsmethoden
	
	public void doTick(long timePassed) {
		timePassed = timePassed/1000000;
		doMovementTick(timePassed);
		doGravitationTick(timePassed);
		checkColission();
	}
	
	public void doUpdate(long ticks) {
		if((ticks % 5) == 0 && movingX) switchImage();
	}
	
	public void moveLeft() {
		lookingRight = false;
		movingX = true;
	}

	public void moveRight() {
		lookingRight = true;
		movingX = true;
	}
	
	public void moveStop() {
		movingX = false;
	}
	
	public void moveJump() {
		if(canJump()) {
			jump(jumpSpeed);
		}
	}
	
	
	public void jump(double speed) {
		upSpeed = (!movingX ? (0.9) : 1 )*speed;
		jumped=true;
	}
	
	// Berechnungsmethoden
	
	public void checkColission() {
		double [] collisionBox = getCollisionBox(posX, posY,2);
		if(g.level.pixelBlockID(collisionBox[DIRECTION_LEFT],collisionBox[DIRECTION_MIDY]) != 0) {
			collisionWith(g.level.pixelBlockID(collisionBox[DIRECTION_LEFT],collisionBox[DIRECTION_MIDY]),DIRECTION_LEFT);
		}
		
		if(g.level.pixelBlockID(collisionBox[DIRECTION_MIDX],collisionBox[DIRECTION_UP]) != 0) {
			collisionWith(g.level.pixelBlockID(collisionBox[DIRECTION_MIDX],collisionBox[DIRECTION_UP]),DIRECTION_UP);
		}
		
		if(g.level.pixelBlockID(collisionBox[DIRECTION_RIGHT],collisionBox[DIRECTION_MIDY]) != 0) {
			collisionWith(g.level.pixelBlockID(collisionBox[DIRECTION_RIGHT],collisionBox[DIRECTION_MIDY]),DIRECTION_RIGHT);
		}
		
		if(g.level.pixelBlockID(collisionBox[DIRECTION_MIDX],collisionBox[DIRECTION_DOWN]) != 0) {
			collisionWith(g.level.pixelBlockID(collisionBox[DIRECTION_MIDX],collisionBox[DIRECTION_DOWN]),DIRECTION_DOWN);
		}
	}
	
	public void collisionWith(int blockID, int dir) {
		if((dir == DIRECTION_DOWN) && (blockID == 19)) {
			jumped=true;
			upSpeed=1.5*upSpeed*(-1);
			if(upSpeed > maxSpeed) {
				upSpeed = maxSpeed;
			} else if(upSpeed == 0) {
				upSpeed=1;
			}
			return;
		} else
		
		if((blockID == 25)) {
			g.level.switchLever(0, false);
		} else 	if((blockID == 26)) {
			g.level.switchLever(0, true);
		}
		
		if((blockID == 32)) {
			g.level.switchLever(1, false);
		} else 	if((blockID == 33)) {
			g.level.switchLever(1, true);
		} else

		if((blockID == 38)) {
			g.level.switchLever(2, false);
		} else 	if((blockID == 39)) {
			g.level.switchLever(2, true);
		} else
			
		if((blockID == 44)) {
			g.level.switchLever(3, false);
		} else 	if((blockID == 45)) {
			g.level.switchLever(3, true);
		}
		
		if((dir == DIRECTION_DOWN) && g.level.solidBlock(blockID)) {
			jumped = false;
			upSpeed = 0;
			return;
		}

	}
	
	private void doMovementTick(long timePassed) {
		doMovementXTick(timePassed);
		doMovementYTick(timePassed);
		doBackToLevelTick();
	}
	
	// TODO Movement-Zugroßwerdebug entfernen
	
	private void doMovementXTick(long timeFactor) {
		if (movingX) {
			double leftToGo = timeFactor*speedFactor;
			while (Math.abs(leftToGo) > 0d) {
				double stepToGo = Math.min(leftToGo,1d)*(this.lookingRight?1:-1);
				leftToGo -= Math.abs(stepToGo);
				if(lookingRight) {
					if(solidBlockInDirection(DIRECTION_RIGHT,stepToGo,0d)) {
						posX = Math.floor(posX);
						return;
					}
				} else {
					if(solidBlockInDirection(DIRECTION_LEFT,stepToGo, 0d)) {
						posX = Math.ceil(posX);
						return;
					}				
				} 
				posX+=stepToGo;
			}
		} else {
			return;
		}
	}
	
	public void doMovementYTick(long timePassed) {
		if(upSpeed!=0) {
			double timeFactor = (double)timePassed/40;
			double leftToGo = Math.abs(timeFactor * upSpeed);
			while (Math.abs(leftToGo) > 0d) {
				double stepToGo = Math.min(Math.abs(leftToGo),1d)*((upSpeed >= 0)?1:-1);
				leftToGo -= Math.abs(stepToGo);
				if(upSpeed > 0.0d) { // Fliegt nach oben
					if(!solidBlockInDirection(DIRECTION_UP, 0d, stepToGo)) {
						posY -= stepToGo;
					} else {
						posY = Math.ceil(posY);
						upSpeed = 0;
						return;
					}
				} else { // Fliegt nach unten
					if(!solidBlockInDirection(DIRECTION_DOWN, 0d, stepToGo)) {
						posY -= stepToGo;
					} else {
						posY=Math.floor(posY);
						jumped = false;
						return;
					}
				}
			}
		}
	}
	
	public void fitInLevel(int dir, double x, double y) {
		
	}

	private void doGravitationTick(long timePassed) {
		if(!solidBlockInDirection(DIRECTION_DOWN,0d,upSpeed-gravitationFactor*timePassed)) { // kein Block unter den Füssen
			if(upSpeed >= -maxSpeed) {
				upSpeed -= gravitationFactor*timePassed;
			}
		}	
	}
	
	private void doBackToLevelTick() {
		if(posX < 0) {
			posX = g.level.getWidth();
		} else if(posX > g.level.getWidth()) {
			posX=0;
		}
		
		if(posY < 0) {
			posY = g.level.getHeight();
		} else if(posY > g.level.getHeight()) {
			posY = 0;
		}
	}
	
	public boolean canJump () {
		if(jumped || upSpeed<0) return false;
		return true;
	}
	
	public double[] getCollisionBox() {
		return getCollisionBox(posX,posY);
	}

	public double[] getCollisionBox(double x, double y) {
		return getCollisionBox(x, y, 0);
	}
	
	public double[] getCollisionBox(double x, double y, int outset) {
		return new double[] {y-height-outset,x-((width-1)/2)-outset,y+outset,x+((width-1)/2)+outset,x,y-(0.5*height)};
	}

	public boolean solidBlockInDirection(int direction, double goingRight, double goingUp) {
		double [] collisionBox = getCollisionBox(posX+goingRight, posY-goingUp);
		switch(direction) {
			case 0: {
				return
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_LEFT],collisionBox[DIRECTION_UP])) ||
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_RIGHT],collisionBox[DIRECTION_UP]));
			}
			case 2: {
				return
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_LEFT],collisionBox[DIRECTION_DOWN])) ||
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_RIGHT],collisionBox[DIRECTION_DOWN]));
			}
			case 1: {
				return
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_LEFT],collisionBox[DIRECTION_UP])) ||
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_LEFT],collisionBox[DIRECTION_DOWN]));
			}
			case 3: {
				return
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_RIGHT],collisionBox[DIRECTION_UP])) ||
					g.level.solidBlock(g.level.pixelBlockID(collisionBox[DIRECTION_RIGHT],collisionBox[DIRECTION_DOWN]));
			}
			default:{
				return false;
			}
		}
	}
	
	// Zeichnungsmethoden
		
	public void doPaint(Graphics2D g) {
		int x = (int) (posX - (spriteWidth-1) / 2);
		int y = (int) (posY - spriteHeight);
		BufferedImage spriteToDraw = sprite[spriteID];
		paint(g, spriteToDraw,x, y);
		double [] collisionBox = getCollisionBox();
		if (collisionBox[DIRECTION_UP] < 0d) {
			paint(g, spriteToDraw, x, y+this.g.level.getHeight());
		} if (collisionBox[DIRECTION_DOWN] > this. g.level.getWidth()) {
			paint(g, spriteToDraw, x, y-this.g.level.getHeight());
		} if (collisionBox[DIRECTION_LEFT] < 0d) {
			paint(g, spriteToDraw, x+this.g.level.getWidth(), y);
		} if (collisionBox[DIRECTION_RIGHT] > this.g.level.getHeight()) {
			paint(g, spriteToDraw, x-this.g.level.getWidth(), y);
		}
	}
	
	public void paint(Graphics2D g, BufferedImage sprite, int x, int y) {
		g.drawImage(sprite,x+(!lookingRight?(25):(0)),y,25*(lookingRight?(1):(-1)),25,null);
	}


	public void init() {
		posX = g.level.playerSpawn[0]+11;
		posY = g.level.playerSpawn[1]+25;
		upSpeed = 0;
		jumped = false;
		lookingRight = true;
		movingX = false;
	}
	
	public void switchImage() {
		spriteID++;
		if(spriteID > (sprite.length-1)) {
			spriteID = 0;
		}
	}
}
