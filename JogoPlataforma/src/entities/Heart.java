package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Heart extends Entity{
	
	/*Declaracao de variaveis*/
		
		// Animacao
		private int frames = 0, maxFrames = 50, index = 0, maxIndex = 1;
		private BufferedImage[] sprites;

	/*Metodo construtor herdado*/	
	public Heart(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(0, 32, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(16, 32, 16, 16);
	}
	
	/*Logica*/
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
