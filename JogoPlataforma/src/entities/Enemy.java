package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public class Enemy extends Entity{

	/*Declaracao de variaveis*/
		// Atributos
		protected double spd = 0.5;
		protected int life = 10;

		// Animacao
		protected int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
		protected BufferedImage[] sprites;
		protected int dir = 1;

	/*Metodo construtor herdado*/
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[4];
		for(int i = 0; i < sprites.length; i++) {
			sprites[i] = Game.spritesheet.getSprite(80+(i*16), 16, width, height);
		}
	}

	/*Logica*/
	public void tick() {
		// Gravidade
		gravity();
		
		if(!isColiddingWithPlayer()) { // Verifica colisão com o jogador
			this.x+=spd;
		if(World.isFree((int)(this.getX()+spd), this.getY()) && !isColidding((int)(this.getX()+spd), this.getY())) { // Colisao com parede
			if(x <= 0 ) // Colisao com borda da tela
				spd*=-1;
				dir*=-1;
		} else if(isColidding((int)(this.getX()+spd), this.getY())) { // Colisao com outro inimigo
			spd*=-1;
			dir*=-1;
		}else { // Colisao com outra borda
			spd*=-1;
			x+=spd*1.8;
			dir*=-1;
		}
		
		/*Tick das animacoes*/
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			if(index > maxIndex) {
				index = 0;
			}
		}
		} else { // Colidiu com o player
			
			if(Game.player.masky-Camera.y < this.masky-Camera.y && Game.player.vspd > 1) { // Morre se for pisado
				Game.player.vspd = -4;
				destroySelf();
				
			} else if(!Player.isDamage) { // Verifica iFrame do jogador
			Game.player.life-=10;
			Game.player.isDamage = true;
			
			if(Game.player.dir == Game.player.right_dir) { // Knockback no jogador
				Game.player.x -= 15;
			} else {
				Game.player.x += 15;
			}
			Game.player.vspd += -3;
			}
		}
		
		// Colisao com tiro
		collidingBullet();
		
		// Verifica vida
		if(life <= 0) {
			destroySelf();
			return;
		}
	}
	
	/*Metodo para apagar inimigo das listas de entidades*/
	public void destroySelf() {
		Game.entities.remove(this);
		Game.enemies.remove(this);
	}
	
	/*Metodo de colisao com tiro*/
	protected void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof FireShoot) {
				if(Entity.isColidding(this, e)) {
					life-=10;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}
	
	/*Metodo de colisao com jogador*/
	public boolean isColiddingWithPlayer() {
		Rectangle curEnemy = new Rectangle(this.getX()+this.maskx-Camera.x, this.getY()+this.masky-Camera.y, this.maskw, this.maskh);
		Rectangle player = new Rectangle(Game.player.getX()+Game.player.maskx-Camera.x, Game.player.getY()+Game.player.masky-Camera.y, +Game.player.maskw, +Game.player.maskh);
		
		return curEnemy.intersects(player);
	}
	
	/*Metodo de colisao com outros inimigos*/
	public boolean isColidding(int x, int y) {
		Rectangle curEnemy = new Rectangle(x, y, World.tileSize, World.tileSize);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			
			Rectangle target = new Rectangle(e.getX(), e.getY(), World.tileSize, World.tileSize);
			if(curEnemy.intersects(target))
				return true;
		}
		
		return false;
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		//g.setColor(Color.red);
		//g.fillRect(this.getX()+maskx-Camera.x, this.getY()+masky-Camera.y, maskw, maskh);
		g.drawImage(sprites[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
	}
}
