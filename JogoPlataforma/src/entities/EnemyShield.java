package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public class EnemyShield extends Enemy{
	
	/*Inicia sprites*/
	private BufferedImage spriteRight[];
	private BufferedImage spriteLeft[];

	/*Metodo Construtor herdado*/
	public EnemyShield(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		// Inicia sprites
		spriteRight = new BufferedImage[4];
		spriteLeft = new BufferedImage[4];
		this.sprite = sprite;
		for(int i = 0; i < spriteRight.length; i++) {
			spriteRight[i] = Game.spritesheet.getSprite(80+(i*16), 48, 16, 16);
			spriteLeft[i] = Game.spritesheet.getSprite(80+(i*16), 64, 16, 16);
		}
	}
	
	/*Logica*/
	public void tick() {
		// Gravidade
		gravity();
		
		// Documentacao - Enemy
		if(!isColiddingWithPlayer()) { 
			this.x+=spd;
			if(World.isFree((int)(this.getX()+spd), this.getY()) && !isColidding((int)(this.getX()+spd), this.getY())) {
				if(this.x <= 0) {
					spd*=-1;
					dir*=-1;
				}
			} else {
				spd*=-1;
				dir*=-1;
			}
			
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
			} else { // Colisao com o player
				
				if(Game.player.masky-Camera.y < this.masky-Camera.y && Game.player.vspd > 1) {
					Game.player.vspd = -4;
					destroySelf();
				} else if(!Player.isDamage) {
				Game.player.life-=10;
				Game.player.isDamage = true;
				
				if(Game.player.dir == Game.player.right_dir) {
					Game.player.x -= 15;
				} else {
					Game.player.x += 15;
				}
				
				Game.player.vspd += -3;
				}
			}
		
		if(life <= 0) {
			destroySelf();
			return;
		}
		
		// Metodo proprio para refletir tiros
		reflectBullet();
	}
	
	/*Metodo para refletir tiros*/
	public void reflectBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			FireShoot e = Game.bullets.get(i);
			if(e instanceof FireShoot) {
				if(Entity.isColidding(this, e)) {
					if(this.dir*-1 == e.dx) {
						if(this.dir == 1) {
							e.dx*=-1;
							e.x++;
							e.setDir(1);
						} else {
							e.dx*=-1;
							e.x-=20;
							e.setDir(0);;
						}
					} else {
						life-=10;
						Game.bullets.remove(i);
					}
					return;
				}
			}
		}
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		if(dir == -1) {
			g.drawImage(spriteRight[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		} else {
			g.drawImage(spriteLeft[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
	}
	
}
