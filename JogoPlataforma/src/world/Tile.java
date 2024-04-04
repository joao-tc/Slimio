package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Tile {

	/*Declaracao de variaveis*/
		
		// Declara sprites
		public static BufferedImage tile_sky = Game.spritesheet.getSprite(80, 0, 16, 16);
		public static BufferedImage tile_grass = Game.spritesheet.getSprite(64, 0, 16, 16);
		public static BufferedImage tile_dirt = Game.spritesheet.getSprite(64, 16, 16, 16);
		private BufferedImage sprite;
	
		// Coordenada
		private int x, y;
	
	/*Metodo construtor*/
	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		g.drawImage(sprite, x-Camera.x, y-Camera.y, null);
	}
}
