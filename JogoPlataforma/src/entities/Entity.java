package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.World;

public abstract class Entity {

	/* Declaracao de variaveis */

	// Declaracao dos sprites
	public static BufferedImage enemy_en = Game.spritesheet.getSprite(80, 16, 16, 16);
	public static BufferedImage coin_en = Game.spritesheet.getSprite(96, 0, 16, 16);
	public static BufferedImage hearth_en = Game.spritesheet.getSprite(0, 32, 16, 16);
	public static BufferedImage fireIcon_en = Game.spritesheet.getSprite(32, 32, 16, 16);
	public static BufferedImage fireD_en = Game.spritesheet.getSprite(32, 48, 16, 16);
	public static BufferedImage fireE_en = Game.spritesheet.getSprite(32, 64, 16, 16);
	public static BufferedImage enemyCorno_en = Game.spritesheet.getSprite(80, 32, 16, 16);
	public static BufferedImage enemyShield_en = Game.spritesheet.getSprite(80, 48, 16, 16);
	public static BufferedImage gameIcon = Game.spritesheet.getSprite(0, 0, 16, 16);
	protected BufferedImage sprite;

	// Atributos
	protected double x, y, width, height;
	protected int vspd = 0;
	protected int maxG = 8;
	protected int gBuffer = 0;
	public int grvt = 1;

	// Mascara de colisao
	protected int maskx, masky, maskw, maskh;

	/* Metodo Construtor */
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskx = 0;
		this.masky = 0;
		this.maskw = width;
		this.maskh = height;
	}

	/* Modifica mascara */
	public void setMask(int x, int y, int width, int height) {
		this.maskx = x;
		this.masky = y;
		this.maskw = width;
		this.maskh = height;
	}

	/* Getters */
	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	/***/

	/* Verifica colisao de duas entidades */
	public static boolean isColidding(Entity e1, Entity e2) {
		Rectangle maske1 = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, (int) e1.getWidth(),
				(int) e1.getHeight());
		Rectangle maske2 = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, (int) e2.getWidth(),
				(int) e2.getHeight());

		return maske1.intersects(maske2);
	}

	/* Logica */
	public void tick() {
		gravity();
	}

	/* Aplica gravidade nas entidades */
	public void gravity() {
		y += vspd;
		if (World.isFree(this.getX(), this.getY() + vspd)) {
			if (vspd <= maxG) {
				if (gBuffer == 5) {
					vspd += grvt;
					gBuffer = 0;
				} else {
					gBuffer++;
				}
			}
		} else if (World.isFree(this.getX(), this.getY() + grvt) && vspd <= maxG) {
			vspd = 0;
		} else {
			vspd = 0;
		}

		if (!World.isFree(this.getX(), this.getY())) {
			y -= 1;
		}
	}

	/* Renderizacao */
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
