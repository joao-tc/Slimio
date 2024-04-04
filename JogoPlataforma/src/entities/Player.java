package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graficos.Spritesheet;
import main.Game;
import world.Camera;
import world.World;

public class Player extends Entity{

	/*Declaracao de variaveis*/
	
		//Comandos do jogador
		public boolean right, left, jump;
		public double spd = 1.4;
		public int jumpForce = 4;
		private int jBuffer = 5;
		private int jDelay = 0;
		public boolean shoot = false;
		
		// Player Stats
		public double life = 100;
		public static final double maxLife = 100;
		public static int coinCount = 0;
		public static int ammo = 0;
		public static boolean hasFire = false;
	
		//Controle da animacao
		private BufferedImage[] rightPlayer;
		private BufferedImage[] leftPlayer;
		private BufferedImage playerDamage;
		private BufferedImage playerShootD;
		private BufferedImage playerShootE;
		private int frames = 0, maxFrames = 8, index = 0, maxIndex = 3;
		public int right_dir = 0, left_dir = 1;
		public int dir = right_dir;
		private boolean moved = false;
		public static boolean isDamage = false;
		private int iFrames = 0;
	
	/*Metodo construtor*/
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		//Inicia os sprites
		playerDamage = Game.spritesheet.getSprite(0, 48, 16, 16);
		playerShootD = Game.spritesheet.getSprite(16, 48, 16, 16);
		playerShootE = Game.spritesheet.getSprite(16, 64, 16, 16);
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		for(int i = 0; i < rightPlayer.length; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(0+(i*16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(0+(i*16), 16, 16, 16);
		}
		
	}
	
	/*Setters*/
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	/***/

	/*Logica*/
	public void tick() {
		// Gravidade
		gravity();
		
		// Verifica colisoes
		moved = false;
		if(right && World.isFree((int)(x+spd), this.getY())) {
			moved = true;
			x+=spd;
			dir = right_dir;
		} else if(left && World.isFree((int)(x-spd), this.getY()) && this.getX() > 0) {
			moved = true;
			x-=spd;
			dir = left_dir;
		}
		
		// Logica de pulo
		if(jDelay <= 0) {
			if(jBuffer > 0 && jump) {
				vspd-=jumpForce;
				jBuffer = 0;
				jDelay = 20;
			} else if(!World.isFree(this.getX(), this.getY()+grvt)) {
				jBuffer++;
			}
		} else {
			jDelay--;
		}
		
		// Tick nas animacoes
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				if(index > maxIndex) {
					index = 0;
				}
			}
		}
		
		// Chamada de metodos de colisao
		this.checkCollisionHearth();
		this.checkCollisionCoin();
		this.checkCollisionFire();
		
		// iFrames
		if(isDamage) {
			this.iFrames++;
			if(this.iFrames == 20) {
				iFrames = 0;
				isDamage = false;
			}
		}
		
		// Logica de tiro
		if(hasFire) { // Tem municao
			if(shoot) { // Atirou
				ammo--;
				shoot = false;
				int dx = 0;
				int px = 16;
				int py = 2;
				FireShoot bullet;
				if(dir == right_dir) { // Cria tiro baseado na posicao
					dx = 1;
					bullet = new FireShoot(this.getX()+px, this.getY()+py, 7, 3, Entity.fireD_en, dx);
					bullet.setDir(1);
				} else {
					dx = -1;
					bullet = new FireShoot(this.getX()-px, this.getY()+py, 3, 3, Entity.fireE_en, dx);
					bullet.setDir(0);
				}
				bullet.setMask(2, 3, 12, 10);
				Game.bullets.add(bullet);
			}
		}
		
		// Chamada de metodo
		collidingBullet();
		
		// Verifica vida e reinicia o jogo
		if(life <=0) {
			Game.gameState = "GameOver";
		}
		
		// Camera clamp
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH*16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT*16 - Game.HEIGHT);
	}
	
	/*Metodo de colisao com tiros*/
	private void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			if(e instanceof FireShoot) {
				if(Entity.isColidding(this, e)) {
					this.life-=50;
					Game.bullets.remove(i);
					System.out.println("Colisão");
					return;
				}
			}
		}
	}
	
	/*Metodo de colisao com coracao*/
	public void checkCollisionHearth() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Heart && life < 100) {
				if(Entity.isColidding(this, atual)) {
					life+=25;
					if(life > 100)
						life=100;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	/*Metodo de colisao com moeda*/
	public void checkCollisionCoin() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Coin) {
				if(Entity.isColidding(this, atual)) {
					Player.coinCount++;
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	/*Metodo de colisao com fogo(municao)*/
	public void checkCollisionFire() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Fire) {
				if(Entity.isColidding(this, atual)) {
					Player.ammo++;
					Game.entities.remove(atual);
				}
			}
		}
		if(Player.ammo > 0) { // Habilida arma
			Player.hasFire = true;
		} else {
			Player.hasFire = false;
		}
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		//g.setColor(Color.blue);
		//g.fillRect(this.getX()+maskx-Camera.x, this.getY()+masky-Camera.y, maskw, maskh);
		
		if(!isDamage) { //Prioridade 1 - Sofreu dano
			if(hasFire) { //Prioridade 2 - Tem Municao
				if(dir == right_dir) {
					g.drawImage(playerShootD, this.getX()-Camera.x, this.getY()-Camera.y, null);
				} else if(dir == left_dir) {
					g.drawImage(playerShootE, this.getX()-Camera.x, this.getY()-Camera.y, null);
				}
			} else if(World.isFree(this.getX(), this.getY()+grvt)) { //Prioridade 3 - Esta no ar
				if(dir == right_dir) {
					g.drawImage(rightPlayer[2], this.getX()-Camera.x, this.getY()-Camera.y, null);
				} else if(dir == left_dir) {
					g.drawImage(leftPlayer[2], this.getX()-Camera.x, this.getY()-Camera.y, null);
				}	
		} else if(moved == false) {//Prioridade 4 - Moveu
			if(dir == right_dir) {
				g.drawImage(rightPlayer[0], this.getX()-Camera.x, this.getY()-Camera.y, null);
			} else if(dir == left_dir) {
				g.drawImage(leftPlayer[0], this.getX()-Camera.x, this.getY()-Camera.y, null);
			} 
			
		} else if(dir == right_dir) {//Prioridade 5 - Idle
			g.drawImage(rightPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		} else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX()-Camera.x, this.getY()-Camera.y, null);
		} 
		
		} else { // Dano
			g.drawImage(playerDamage, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
	}
}
