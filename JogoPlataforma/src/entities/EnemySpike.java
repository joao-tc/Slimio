package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class EnemySpike extends Enemy{

	/*Metodo construtor herdado*/
	public EnemySpike(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		this.sprite = sprite;
		
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = Game.spritesheet.getSprite(80+(i*16), 32, 16, 16);
		}
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
	}
}
