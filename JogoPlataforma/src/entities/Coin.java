package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Coin extends Entity{
	
	/*Declaracao de variaveis*/
	private int frames = 0, maxFrames = 10, index = 0, maxIndex = 3;
	private BufferedImage[] sprites;

	/*Metodo construtor herdado*/
	public Coin(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[4];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = Game.spritesheet.getSprite(96+(i*16), 0, width, height);
		}
	}

	/*Sobrepoe tick*/
	public void tick() {
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
	}
	
}
