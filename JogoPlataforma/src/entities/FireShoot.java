package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public class FireShoot extends Entity{

	/*Declaracao de variaveis*/
		// Atributos
		public int dx;
		private double spd = 2;
		private int life = 100, curLife = 0;
		
		//Animacao
		private BufferedImage spritesD[];
		private BufferedImage spritesE[];
		private int frames = 0, maxFrames = 5, index = 0, maxIndex = 1, dir;
	
	// Metodo construtor herdado
	public FireShoot(int x, int y, int width, int height, BufferedImage sprite, int dx) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		
		spritesD = new BufferedImage[2];
		spritesE = new BufferedImage[2];
		
		for(int i = 0; i < spritesD.length; i++) {
			spritesD[i] = Game.spritesheet.getSprite(32+(i*16), 48, 16, 16);
			spritesE[i] = Game.spritesheet.getSprite(32+(i*16), 64, 16, 16);
		}
	}
	
	/*Logica*/
	public void tick() {
		x+=dx*spd;
		curLife++;
		if(curLife>=life) {
			Game.bullets.remove(this);
			return;
		}
		
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
	}
	
	/*Indica a direcao*/
	public void setDir(int dir) {
		this.dir = dir;
	}
	
	/*Renderizacao*/
	public void render(Graphics g ) {
		//g.setColor(Color.magenta);
		//g.fillRect(this.getX()+maskx-Camera.x, this.getY()+masky-Camera.y, maskw, maskh);
		if(dir == 1) {
			g.drawImage(spritesD[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		} else {
			g.drawImage(spritesE[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
		
	}
	
	/*Modifica sprite*/
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
}
