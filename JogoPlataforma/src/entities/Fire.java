package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import world.Camera;

public class Fire extends Entity{

	/*Metodo construtor*/
	public Fire(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
	
	/*Anula logica (gravidade)*/
	public void tick() {
		
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		//g.setColor(Color.red);
		//g.fillRect(this.getX()+maskx-Camera.x, this.getY()+masky-Camera.y, maskw, maskh);
		g.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, (int)width, (int)height, null);
	}

}
