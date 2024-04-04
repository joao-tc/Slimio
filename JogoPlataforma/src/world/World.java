package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.*;
import graficos.Spritesheet;
import main.Game;

public class World {

	/*Declaracao de variaveis*/
	
		// Atributos dos tiles
		public static Tile[] tiles;
		public static int WIDTH, HEIGHT;
		public static final int tileSize = 16;
	
	/*Metodo construtor*/
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));	// Abre arquivo do mapa
			int[] pixels = new int[map.getWidth()*map.getHeight()]; // Lista para ler pixels do mapa
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth()*map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getWidth(), pixels, 0, map.getWidth());
			
			// Percorre pixels da imagem e posiciona objeto de acordo com a cor
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new SkyTile(xx*tileSize, yy*tileSize, Tile.tile_sky);
					
					switch(pixelAtual) {
						case 0xFF000000: //black - floor 
							tiles[xx + (yy * WIDTH)] = new SkyTile(xx*tileSize, yy*tileSize, Tile.tile_sky);
							break;
							
						case 0xFFFFFFFF: //white - wall
							tiles[xx + (yy * WIDTH)] = new GrassTile(xx*tileSize, yy*tileSize, Tile.tile_grass);
							break;
							
						case 0xFF0026FF: //blue - player
							Game.player.setMask(2, 1, 12, 15);
							Game.player.setX(xx*tileSize);
							Game.player.setY(yy*tileSize);
							break;
							
						case 0xFF7F3300: //brown - dirt
							tiles[xx + (yy * WIDTH)] = new DirtTile(xx*tileSize, yy*tileSize, Tile.tile_dirt);
							break;
							
						case 0xFFFF0000: //red - enemy
							Enemy en = new Enemy(xx*tileSize, yy*tileSize, tileSize, tileSize, Entity.enemy_en);
							en.setMask(2, 4, 12, 12);
							Game.entities.add(en);
							Game.enemies.add(en);
							break;
							
						case 0xFFFFD800: //yellow - coin
							Game.entities.add(new Coin(xx*tileSize, yy*tileSize, tileSize, tileSize, Entity.coin_en));
							break;
							
						case 0xFFFF006E: //pink - hearth
							Game.entities.add(new Heart(xx*tileSize, yy*tileSize, tileSize, tileSize, Entity.hearth_en));
							break;
							
						case 0xFFFF6A00: //orange - fire/ammo
							Fire aux = new Fire(xx*tileSize, yy*tileSize, tileSize, tileSize, Entity.fireIcon_en);
							aux.setMask(3, 0, 11, 16);
							Game.entities.add(aux);
							break;
							
						case 0xFF57007F: //purple - spiked
							EnemySpike robson = new EnemySpike(xx*tileSize, yy*tileSize, tileSize, tileSize, Entity.enemyCorno_en);
							robson.setMask(2, 1, 12, 15);
							Game.entities.add(robson);
							Game.enemies.add(robson);
							break;
							
						case 0xFF00FF21: //green - shield
							EnemyShield wagner = new EnemyShield(xx*tileSize, yy*tileSize, tileSize, tileSize, Entity.enemyCorno_en);
							wagner.setMask(2, 4, 12, 12);
							Game.entities.add(wagner);
							Game.enemies.add(wagner);
							break;
							
						default : //defaul - floor
							tiles[xx + (yy * WIDTH)] = new SkyTile(xx*tileSize, yy*tileSize, Tile.tile_sky);
							break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Reinicia o jogo*/
    public static void restartGame() {
    	Game.entities = new ArrayList<Entity>();
    	Game.enemies = new ArrayList<Enemy>();
    	Game.spritesheet = new Spritesheet("/spritesheet.png");
    	Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
    	Game.world = new World("/map.png");
    	Game.entities.add(Game.player);
    	Player.coinCount = 0;
    	Player.ammo = 0;
    	Game.gameState = "Normal";
        return;
    }
	
	/*Metodo para verificar colisao*/
	public static boolean isFree(int x, int y) {
		int x1 = x / tileSize;
		int y1 = y / tileSize;
		
		int x2 = (x+tileSize-1) / tileSize;
		int y2 = (y+tileSize-1) / tileSize;
		
		int x3 = x / tileSize;
		int y3 = (y+tileSize-1) / tileSize;
		
		int x4 = (x+tileSize-1) / tileSize;
		int y4 = y / tileSize;
		
		return !(tiles[x1 + (y1*World.WIDTH)] instanceof HardTile ||
				tiles[x2 + (y2*World.WIDTH)] instanceof HardTile ||
				tiles[x3 + (y3*World.WIDTH)] instanceof HardTile ||
				tiles[x4 + (y4*World.WIDTH)] instanceof HardTile);
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
		}
	}
}
