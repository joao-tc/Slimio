package graficos;

import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {

	/*Declaracao de variaveis*/
	private BufferedImage spritesheet;
	
	/*Metodo construtor para ler o arquivo*/
	public Spritesheet(String path) {
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*Cira sprite baseado no arquivo*/
	public BufferedImage getSprite(int x, int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
}
