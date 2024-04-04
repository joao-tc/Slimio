package graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.Entity;
import entities.Player;
import main.Game;

public class UI {
	
	/*Declaracao de variaveis*/
	
		// Margem
		private int margin = 25;

	/*Renderizacao*/
	public void render(Graphics g) {
		// Life
		g.setColor(Color.red);
		g.fillRect(8, 4, 50, 8);
		g.setColor(Color.green);
		g.fillRect(8, 4, (int)((Game.player.life/Player.maxLife)*50), 8);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD, 8));
		if(Game.player.life == Player.maxLife)
			margin = 21;
		else
			margin = 25;
		g.drawString((int)Game.player.life+"/"+(int)Player.maxLife, margin, 11);
		
		//Ammo
		g.drawImage(Entity.fireIcon_en, Game.WIDTH-35, 2, null);
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString(Player.ammo+"x", Game.WIDTH-20, 15);
		
		//Coin
		g.drawImage(Entity.coin_en, Game.WIDTH-35, 20, null);
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 10));
		g.drawString(Player.coinCount+"x", Game.WIDTH-20, 32);
	}
}
