package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.util.ArrayList;

import javax.swing.JFrame;

import entities.Enemy;
import entities.Entity;
import entities.FireShoot;
import entities.Player;
import graficos.Spritesheet;
import graficos.UI;
import world.World;

public class Game extends Canvas implements Runnable, KeyListener{
	
	/*Declaracao de variaveis*/
		// serialVersion
		private static final long serialVersionUID = 1L;
		
		// Frame e game loop
		public static JFrame frame;
		public static final int WIDTH = 240, HEIGHT = 160, SCALE = 3;
		private boolean isRunning = true;
    	private Thread thread;
    	private int curLevel = 0, maxLevel = 10;
    	public static String gameState = "Menu";
    	
    	// Objetos
    	private BufferedImage image;
    	public static ArrayList<Entity> entities;
    	public static ArrayList<Enemy> enemies;
    	public static ArrayList<FireShoot> bullets;
    	public static Spritesheet spritesheet;
    	public static Player player;
    	public static World world;
    	public static UI ui;
    	public static Menu menu;

    /*Metodo construtor*/
    public Game() {
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        addKeyListener(this);
        initFrame();
        
        // Iniciando objetos
        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<FireShoot>();
        spritesheet = new Spritesheet("/spritesheet.png");
        menu = new Menu();
        player = new Player(0, 0, 16, 16, spritesheet.getSprite(32, 0, 16, 16));
        world = new World("/map.png");
        entities.add(player);
    }

    /*Inicia JFrame*/
    public void initFrame() {
        frame = new JFrame("Slimio");
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /*Inicio do jogo*/
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    /*Metodo de seguranca*/
    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*Main*/
    public static void main(String[] args) {
        Game game = new Game();
        frame.setIconImage(Entity.gameIcon);
        game.start();
    }

    /*logica do jogo*/
    public void tick() {
    	if(gameState == "Normal") {
    	for(int i = 0; i < entities.size(); i++) {
    		Entity e = entities.get(i);
    		e.tick();
    	}
    	for(int i = 0; i < bullets.size(); i++) {
    		bullets.get(i).tick();
    	}
    	} else if(gameState == "GameOver"){
    		//System.out.println("Game Over!");
    	} else if(gameState == "Menu" || gameState == "Pause") {
    		menu.tick();
    	}
    }

    /*Renderizacao*/
    public void render() {
    	// Cria e limpa buffer
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(Color.gray);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Rendereizacao do jogo
        world.render(g);
        for(int i = 0; i < entities.size(); i++) {
    		Entity e = entities.get(i);
    		e.render(g);
    	}
        for(int i = 0; i < bullets.size(); i++) {
    		bullets.get(i).render(g);
    	}
        
        ui.render(g);
        /***/
        
        // Limpa buffer
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        
        if(gameState == "GameOver") {
        	Graphics2D g2 = (Graphics2D) g;
        	g2.setColor(new Color(0, 0, 0, 100));
        	g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
        	g.setFont(new Font("arial", Font.BOLD, 28));
        	g.setColor(Color.white);
        	g.drawString("Game Over!", WIDTH*SCALE/2-70, HEIGHT*SCALE/2);
        	g.setFont(new Font("arial", Font.BOLD, 18));
        	g.drawString("R to restart", WIDTH*SCALE/2-40, HEIGHT*SCALE/2+30);
        } else if(gameState == "Menu" || gameState == "Pause") {
        	menu.render(g);
        }
        
        bs.show();
    }

    /*Game loop*/
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }
            
            if(System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer+=1000;
            } 
        }
        stop();
    }

	/*Eventos do teclado*/
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE ||
				e.getKeyCode() == KeyEvent.VK_W ||
					e.getKeyCode() == KeyEvent.VK_UP){
			player.jump = true;
			
			if(gameState == "Menu" || gameState == "Pause") {
				menu.up = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_E || 
				e.getKeyCode() == KeyEvent.VK_S ||
					e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.shoot = true;
			
			if(gameState == "Menu" || gameState == "Pause") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			if(gameState != "Menu" || gameState == "Pause")
				World.restartGame();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(gameState == "Menu" || gameState == "Pause") {
				menu.enter = true;
			} 
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "Pause";
		}
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE ||
				e.getKeyCode() == KeyEvent.VK_W ||
					e.getKeyCode() == KeyEvent.VK_UP){
			player.jump = false;
			
			if(gameState == "Menu" || gameState == "Pause") {
				menu.up = false;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_E || 
				e.getKeyCode() == KeyEvent.VK_S ||
					e.getKeyCode() == KeyEvent.VK_DOWN) {
			player.shoot = false;
			
			if(gameState == "Menu" || gameState == "Pause") {
				menu.down = false;
			}
		}
		
		
	}
	
	public void keyTyped(KeyEvent e) {}
	/***/
}