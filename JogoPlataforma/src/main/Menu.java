package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Entity;

public class Menu {

	/*Declaracao de variaveis*/
		// Sprites
		private BufferedImage sprite1, sprite2, sprite3;
		
		// Controle do menu
		public String[] options = {"Play", "Exit"};
		public boolean up, down, enter = false;
		public int curOption = 0;
		public int maxOption = options.length-1;
	
	/*Inicia os sprites*/
	public Menu() {
		sprite1 = Game.spritesheet.getSprite(16, 48, 16, 16);
		sprite2 = Game.spritesheet.getSprite(32, 48, 16, 16);
		sprite3 = Game.spritesheet.getSprite(80, 16, 16, 16);
	}
	
	/*Logica*/
	public void tick() {
		if(up) {
			up = false;
			curOption--;
			if(curOption < 0) {
				curOption = maxOption;
			}
		} else if(down) {
			down = false;
			curOption++;
			if(curOption > maxOption) {
				curOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			if(options[curOption] == "Play") {
				Game.gameState = "Normal";
			} else if(options[curOption] == "Exit") {
				System.exit(1);
			}
		} 
		
	}
	
	/*Renderizacao*/
	public void render(Graphics g) {
		// Background do menu e pause
		if(Game.gameState == "Menu") {
			g.setColor(Color.cyan);
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		} else {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(200, 200, 300, 150);
		}
		
		// Renderiza sprites
		g.drawImage(sprite1, 250, 110, 64, 64, null);
		g.drawImage(sprite2, 320, 125, 48, 48, null);
		g.drawImage(sprite3, 400, 110, 64, 64, null);
		
		// Mostra opcoes
		g.setFont(new Font("arial", Font.BOLD, 28));
    	g.setColor(Color.white);
    	g.drawString("Slimio", Game.WIDTH*Game.SCALE/2-60, Game.HEIGHT*Game.SCALE/2);
    	g.setFont(new Font("arial", Font.BOLD, 23));
    	g.drawString("Play", Game.WIDTH*Game.SCALE/2-40, Game.HEIGHT*Game.SCALE/2+40);
    	g.drawString("Exit", Game.WIDTH*Game.SCALE/2-40, Game.HEIGHT*Game.SCALE/2+80);
    	
    	// Ponteiro
    	if(options[curOption] == "Play") {
    		g.drawString(">", Game.WIDTH*Game.SCALE/2-60, Game.HEIGHT*Game.SCALE/2+40);
    	} else if(options[curOption] == "Exit") {
    		g.drawString(">", Game.WIDTH*Game.SCALE/2-60, Game.HEIGHT*Game.SCALE/2+80);
    	}
	}
	
}
