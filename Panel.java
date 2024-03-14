package SpaceInvaders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JPanel;

public class Panel extends JPanel implements KeyListener{
	private BufferedImage home, ship, background, playerLaser, enemyLaser, alien1, alien2;
	private int state = 0, width = 1600, height = 900;
	private Player player;
	private ArrayList<Enemy> enemies;
	private EnemySpawner spawner;
	private final String tag_player = "player", tag_enemy = "enemy";
	private ArrayList<Laser> lasers;
	private long time1, time2, deltaTime;
	private double slideBackground;

	public Panel(){
		try{
			home = ImageIO.read(new FileImageInputStream(new File("home.png")));
			background = ImageIO.read(new FileImageInputStream(new File("background.png")));
			ship = ImageIO.read(new FileImageInputStream(new File("ship.png")));
			playerLaser = ImageIO.read(new FileImageInputStream(new File("playerLaser.png")));
			enemyLaser = ImageIO.read(new FileImageInputStream(new File("enemyLaser.png")));
			alien1 = ImageIO.read(new FileImageInputStream(new File("alien1.png")));
			alien2 = ImageIO.read(new FileImageInputStream(new File("alien2.png")));
		}

		catch(IOException ioe){
			ioe.printStackTrace();
		}

		enemies = new ArrayList<Enemy>();
		spawner = new EnemySpawner(3000, 1000, 5);
		lasers = new ArrayList<Laser>();
		slideBackground = -background.getHeight();
	}

	public void setPlayGame(){
		try{
			home = ImageIO.read(new FileImageInputStream(new File("playGame.png")));
		}

		catch(IOException ioe){
			ioe.printStackTrace();
		}

		repaint();
	}

	public void setGameOver(){
		try{
			home = ImageIO.read(new FileImageInputStream(new File("gameOver.png")));
		}

		catch(IOException ioe){
			ioe.printStackTrace();
		}

		setState(0);
	}

	public void setState(int state){
		if(state == 1){
			time1 = System.currentTimeMillis();
			player = new Player(ship, tag_player, width/2 - ship.getWidth()/2, height - ship.getHeight(), 1f);
		}

		this.state = state;
		repaint();
	}

	@Override
	public void paint(Graphics graphics){
		super.paint(graphics);

		if(state == 1){
			time2 = System.currentTimeMillis();
			deltaTime = (time2 - time1);

			if(slideBackground >= 0)
				slideBackground = -this.background.getHeight();

			wrap(graphics, slideBackground, width, height + 10);
			slideBackground = slideBackground + 0.25;

			player.render(graphics);

			for (Enemy enemy: enemies)
				enemy.render(graphics);

			for (Laser laser: lasers)
				laser.render(graphics);

			player.update();

			for(int i = enemies.size() - 1; i >= 0; i--){
				if(enemies.get(i).health <= 0 || enemies.get(i).y > height)
					enemies.remove(i);

				else
					enemies.get(i).update();
			}

			spawner.update();

			for(int i = lasers.size() - 1; i >= 0; i--) {
				if (lasers.get(i).health <= 0 || lasers.get(i).y < -20 || lasers.get(i).y > height + 20)
					lasers.remove(i);

				else
					lasers.get(i).update();
			}

			time1 = System.currentTimeMillis();
			repaint();
		}

		if(state == 0)
			graphics.drawImage(home, 0, 0, this);
	}

	@Override
	public void repaint(){
		super.repaint();
	}

	public void wrap(Graphics graphics, double slideBackground, int x2, int y2){
		for(int x = 0; x < x2; x = x + background.getWidth()){
			for(double y = slideBackground; y < y2; y = y + background.getHeight()){
				graphics.drawImage(background, x, (int)y, this);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent ke){
	}

	boolean keys[] = new boolean[5];

	@Override
	public void keyPressed(KeyEvent ke){
		int c = ke.getKeyCode();
		if (c == KeyEvent.VK_UP) keys[0] = true;
		if (c == KeyEvent.VK_LEFT) keys[1] = true;
		if (c == KeyEvent.VK_DOWN) keys[2] = true;
		if (c == KeyEvent.VK_RIGHT) keys[3] = true;
		if (c == KeyEvent.VK_SPACE) keys[4] = true;
	}

	@Override
	public void keyReleased(KeyEvent ke){
		int c = ke.getKeyCode();
		if (c == KeyEvent.VK_UP) keys[0] = false;
		if (c == KeyEvent.VK_LEFT) keys[1] = false;
		if (c == KeyEvent.VK_DOWN) keys[2] = false;
		if (c == KeyEvent.VK_RIGHT) keys[3] = false;
		if (c == KeyEvent.VK_SPACE) keys[4] = false;
	}

	public class Laser extends GameObject{
		float speed;
		public int health = 1;

		public Laser(BufferedImage image, String tag, float x, float y, float speed){
			super(tag, x, y, image);
			this.speed = speed;
			collider = new BoxCollider(x, y, image.getWidth(), image.getHeight());
		}

		void update(){
			y = y + speed * deltaTime;
			collider.moveCollider(x, y);
		}
	}

	public class Player extends GameObject{  
		float speed;
		float cooldown = 100;
		float leftTime;

		Player(BufferedImage image, String tag, float x, float y, float speed){
			super(tag, x, y, image);
			this.speed = speed;
			collider = new BoxCollider(x, y, image.getWidth(), image.getHeight());
			leftTime = 0;
		}

		void update(){
			if(Account.account.getHealth() == 0){
				setGameOver();
				Account.scores.add(new Score(Account.account.getUsername(), Account.account.getScore()));
				Collections.sort(Account.scores);
				Account.account.setScore(0);
				Account.account.setHealth(3);
				Account.account.setLevel(1);
				enemies.removeAll(enemies);
				spawner = new EnemySpawner(3000, 1000, 5);
			}

			if(leftTime > 0)
				leftTime = leftTime - deltaTime;

			if(keys[0])
				y = y - speed * deltaTime;

			if(keys[2])
				y = y + speed * deltaTime;

			if(keys[1])
				x = x - speed * deltaTime;

			if(keys[3])
				x = x + speed * deltaTime;

			if(x < 0)
				x = 0;

			else if(x + image.getWidth() > width)
				x = width - image.getWidth();

			if(y < 0)
				y = 0;

			else if(y + image.getHeight() > height)
				y = height - image.getHeight();

			collider.moveCollider(x, y);

			for (Laser laser : lasers){
				if(collider.isCollided(laser.collider) && laser.tag != tag){
					Account.account.decreaseHealth();
					SpaceInvaders.newLabel.setText("x" + Account.account.getHealth() + "        |        " + "Level: " + Account.account.getLevel() + "        |        " + "Score: " + Account.account.getScore() + "        |        " + "Username: " + Account.account.getUsername());
					laser.health--;
				}
			}

			for (Enemy enemy : enemies){
				if(collider.isCollided(enemy.collider) && enemy.tag != tag) {
					Account.account.decreaseHealth();
					SpaceInvaders.newLabel.setText("x" + Account.account.getHealth() + "        |        " + "Level: " + Account.account.getLevel() + "        |        " + "Score: " + Account.account.getScore() + "        |        " + "Username: " + Account.account.getUsername());
					enemy.health = enemy.health - 100;
					x = width / 2 - ship.getWidth()/2;
					y = height - ship.getHeight();
				}
			}

			if(keys[4])
				shoot();
		}

		void shoot(){
			if(leftTime <= 0) {
				Laser laser = new Laser(playerLaser, tag, x + image.getWidth()/2 - playerLaser.getWidth()/2, y - 3*playerLaser.getHeight()/4, -1.5f);
				lasers.add(laser);
				leftTime = cooldown;
			}
		}
	}

	public class Enemy extends GameObject{
		float speed;
		float cooldownMin = 1000.0f;
		float cooldownMax = 5000.0f;
		float leftTime;
		public int health;
		int score;

		Enemy(BufferedImage image, String tag, float x, float y, float speed, int health, int score){
			super(tag, x, y, image);
			this.speed = speed;
			this.health = health;
			this.score = score;
			collider = new BoxCollider(x, y, image.getWidth(), image.getHeight());
			leftTime = (float)(Math.random() * (cooldownMax - cooldownMin) + cooldownMin);
		}

		void update(){
			leftTime = leftTime - deltaTime;
			y = y + speed * deltaTime;
			collider.moveCollider(x, y);
			shoot();

			for(Laser laser : lasers){
				if(collider.isCollided(laser.collider) && laser.tag != tag){
					health = health - 100;
					laser.health--;
					Account.account.addScore(score);
					SpaceInvaders.newLabel.setText("x" + Account.account.getHealth() + "        |        " + "Level: " + Account.account.getLevel() + "        |        " + "Score: " + Account.account.getScore() + "        |        " + "Username: " + Account.account.getUsername());
				}
			}
		}

		void shoot(){
			if(leftTime <= 0){
				Laser laser = new Laser(enemyLaser, tag, x + image.getWidth()/2 - enemyLaser.getWidth()/2, y + enemyLaser.getHeight(), 1.0f);
				lasers.add(laser);
				leftTime = (float)(Math.random() * (cooldownMax - cooldownMin) + cooldownMin);
			}
		}
	}

	public class GameObject{
		BoxCollider collider;
		float x, y;
		BufferedImage image;
		String tag;

		public GameObject(String tag, float x, float y, BufferedImage image){
			this.tag = tag;
			this.x = x;
			this.y = y;
			this.image = image;
		}

		void render(Graphics graphics){
			graphics.drawImage(image, (int)x, (int)y, Panel.this);
		}

		void update(){
		}
	}

	public class BoxCollider{
		public float x, y, sizeX, sizeY;

		public BoxCollider(float x, float y, float sizeX, float sizeY){
			this.x = x;
			this.y = y;
			this.sizeX = sizeX;
			this.sizeY = sizeY;
		}

		boolean isCollided(BoxCollider box){
			return x < box.x + box.sizeX && x + sizeX > box.x && y < box.y + box.sizeY && y + sizeY > box.y;
		}

		void moveCollider(float x, float y){
			this.x = x;
			this.y = y;
		}
	}

	public class EnemySpawner{
		float max, min, leftTime;
		public int maxEnemy, enemyCount;
		float multiplier = 1;
		int probability = 9;

		public EnemySpawner(float max, float min, int maxEnemy){
			this.max = max;
			this.min = min;
			leftTime = (float)(Math.random() * (max - min) + min);
			this.maxEnemy = maxEnemy;
			enemyCount = 0;
		}

		void update(){
			leftTime = leftTime - deltaTime;

			if(leftTime <= 0 && enemyCount < maxEnemy){
				int x = (int)(Math.random() * 10) + 1;
				Enemy enemy;

				if(Account.account.getLevel() == 2)
					multiplier = 1.25f;

				else if(Account.account.getLevel() == 3)
					multiplier = 1.5f;

				if(x < probability)
					enemy = new Enemy(alien1, tag_enemy, (int)(Math.random() * (width - alien1.getWidth())), -alien1.getHeight(), multiplier * 0.25f, (int)(multiplier * 500), 10);

				else
					enemy = new Enemy(alien2, tag_enemy, (int)(Math.random() * (width - alien2.getWidth())), -alien2.getHeight(), multiplier * 0.45f, (int)(multiplier * 1000), 20);

				enemies.add(enemy);
				enemyCount++;
				leftTime = (float)(Math.random() * (max - min) + min);
			}

			else if(enemyCount == maxEnemy){
				Account.account.increaseLevel();

				if(Account.account.getLevel() == 4){
					setGameOver();
					Account.scores.add(new Score(Account.account.getUsername(), Account.account.getScore()));
					Collections.sort(Account.scores);
					Account.account.setScore(0);
					Account.account.setHealth(3);
					Account.account.setLevel(1);
					enemies.removeAll(enemies);
					spawner = new EnemySpawner(3000, 1000, 5);
				}

				SpaceInvaders.newLabel.setText("x" + Account.account.getHealth() + "        |        " + "Level: " + Account.account.getLevel() + "        |        " + "Score: " + Account.account.getScore() + "        |        " + "Username: " + Account.account.getUsername());
				maxEnemy = maxEnemy + 1;
				enemyCount = 0;
				probability = probability - 2;
			}
		}
	}
}