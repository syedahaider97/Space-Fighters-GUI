import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Board extends JFrame {

	static int boardSize = 730;

	PowerUp power;

	static JLabel lifeLabel, clearLabel, missleLabel, scoreLabel, missleCountLabel;

	static int clear;
	static int score;
	static int missleCount = 3;
	private int life = 3;

	static String missleType = "Regular";

	public Board() {
		
		//Board Setup
		
		super("Space Fighters");
		setLayout(new BorderLayout());
		setSize(boardSize, boardSize);

		//Menu bar Setup
		
		JMenuBar menubar = new JMenuBar();
		setJMenuBar(menubar);

		JMenu file = new JMenu("File");
		menubar.add(file);

		JMenuItem reset = new JMenuItem("Reset");
		file.add(reset);

		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);

		reset.addActionListener(new Reset());
		exit.addActionListener(new Exit());

		add(new Domain(this), BorderLayout.CENTER); 	//The actual Game Panel

		//Panel used to keep track of game metrics
		JPanel scorePanel = new JPanel(new GridLayout(2, 3));

		lifeLabel = new JLabel("Life: " + life);
		lifeLabel.setFont(new Font("Arial", Font.BOLD, 20));
		scorePanel.add(lifeLabel);

		missleCountLabel = new JLabel("Missle Count: " + missleCount, JLabel.CENTER);
		missleCountLabel.setFont(new Font("Arial", Font.BOLD, 20));
		scorePanel.add(missleCountLabel);

		scoreLabel = new JLabel("Score: " + score, JLabel.RIGHT);
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
		scorePanel.add(scoreLabel);

		missleLabel = new JLabel("Current Missle: " + missleType);
		missleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		scorePanel.add(missleLabel);

		scorePanel.add(new JLabel(""));		//Used for formatting

		clearLabel = new JLabel("Clears: " + clear, JLabel.RIGHT);
		clearLabel.setFont(new Font("Arial", Font.BOLD, 20));
		scorePanel.add(clearLabel);

		add(scorePanel, BorderLayout.SOUTH);
		setResizable(false);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String args[]) {

		new Board();
	}

	public class Domain extends JPanel implements ActionListener, KeyListener {
		Jet jet = new Jet(boardSize);
		Timer timer;			//Timer for missiles, movement, and fluency 
		Timer enemyTimer;		//Enemy spawn timer
		Timer powerTimer;		//Power up spawn timer
		Timer gameTimer;		//Controls gradual difficulty increase
		JFrame frame;
		int enemySpawn = 2000;

		public Domain(JFrame frame) {
			this.frame = frame;
			setBackground(Color.BLACK);
			addKeyListener(this);		//Allows the panel to have access to the keyboard
			setFocusable(true);			//-^
			requestFocusInWindow();		//-^
			
			gameTimer = new Timer(10000, new GameTimer());
			gameTimer.start();

			timer = new Timer(10, this);
			timer.start();

			enemyTimer = new Timer(enemySpawn, new EnemySpawner());
			enemyTimer.start();

			powerTimer = new Timer(3000, new PowerSpawner());
			powerTimer.start();
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(jet.getImage(), jet.getxCor(), jet.getyCor(), null);
			
			//Draw enemies as they spawn
			for (Enemy e : Enemy.enemyList) {
				g.drawImage(e.getImage(), e.getxCor(), e.getyCor(), null);
			}
			//Draw Danger line
			g.setColor(Color.RED);
			g.drawLine(boardSize / 8, 0, boardSize / 8, boardSize);

			//Draws missiles as they spawn 
			g.setColor(Color.WHITE);
			for (Missle m : jet.getMissleList()) {
				g.fillOval(m.getxCor(), m.getyCor(), m.getSize(), m.getSize());
			}
			//Draws an active Power Up if any
			if (PowerUp.alive) {
				g.drawImage(power.getImage(), power.getxCor(), power.getyCor(), null);
			}

		}

		public void actionPerformed(ActionEvent e) {
			jet.move();
			boolean hit = false;
			
			//Runs the missiles, and checks to see if a collision has occurred with any of the enemies 
			for (Missle m : jet.getMissleList()) {
				m.run();
				for (Enemy enemy : Enemy.enemyList) {
					hit = false;
					if (intersect(m, enemy)) {
						Enemy.enemyList.remove(enemy);
						hit = true;
						scoreLabel.setText("Score: " + ++score);
						break;
					}
				}
				if (hit) {
					jet.getMissleList().remove(m);
					break;
				}
			}
			
			//Checks to see if an enemy has made it passed the board
			for (Enemy enemy : Enemy.enemyList) {
				enemy.move();
				if (enemy.getxCor() < 0 || intersect(enemy,jet)) {
					life--;
					lifeLabel.setText("Life: " + life);
					Enemy.enemyList.remove(enemy);
					checkGameOver(frame);
					break;
				}
			}

			
			if (PowerUp.alive) {
				//Decrements and removes the Power Up upon expire 
				PowerUp.life--;
				if (power.getDuration() == 0) {
					PowerUp.alive = false;
				}
				if (PowerUp.life < 0) {
					missleType = "Regular";
					missleLabel.setText("Current Missle: " + missleType);
				}
				
				//Checks to see if a Power Up has been acquired 
				if (intersect(power, jet)) {
					PowerUp.alive = false;
					missleType = power.getType();
					PowerUp.life = 450;
					if (missleType.equals("Clear")) {
						clearLabel.setText("Clears: " + ++clear);
						missleType = "Regular";
					}
					missleLabel.setText("Current Missle: " + missleType);
					missleCount += 1;
					missleCountLabel.setForeground(Color.BLACK);
					missleCountLabel.setText("Missle Count: " + missleCount);
				}

			}

			repaint();
		}

		//Intersection Code
		private boolean intersect(Enemy e, Jet j) {
			Rectangle r1 = new Rectangle(j.getxCor() + 10, j.getyCor() + 10, Jet.imageSize - 20, Jet.imageSize - 20);
			Rectangle r2 = new Rectangle(e.getxCor() + 10, e.getyCor() + 10, Enemy.enemySize - 20, Enemy.enemySize - 20);
			return r1.intersects(r2);
		}
		
		private boolean intersect(Missle m, Enemy e) {
			Rectangle r1 = new Rectangle(m.getxCor(), m.getyCor(), m.getSize(), m.getSize());
			Rectangle r2 = new Rectangle(e.getxCor(), e.getyCor(), Enemy.enemySize, Enemy.enemySize);
			return r1.intersects(r2);
		}

		private boolean intersect(PowerUp p, Jet j) {
			Rectangle r1 = new Rectangle(p.getxCor(), p.getyCor(), PowerUp.size, PowerUp.size);
			Rectangle r2 = new Rectangle(j.getxCor(), j.getyCor(), Jet.imageSize, Jet.imageSize);
			return r1.intersects(r2);
		}

		//Jet Movement 
		public void keyPressed(KeyEvent e) {

			jet.keyPressed(e);
			missleCountLabel.setText("Missle Count: " + missleCount);
		}

		public void keyReleased(KeyEvent e) {
			jet.keyReleased(e);
		}

		public void keyTyped(KeyEvent arg0) {}

		//Fires the Game Over dialog box when 3 enemies have passed
		private void checkGameOver(JFrame frame) {
			if (life == 0) {
				Dialog over = new Dialog(frame);
				over.setDefaultCloseOperation(HIDE_ON_CLOSE);
				over.setVisible(true);
				over.setSize(300, 300);
				timer.stop();
				enemyTimer.stop();
				powerTimer.stop();
			}
		}

		class EnemySpawner implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				Enemy enemey = new Enemy();
				Enemy.enemyList.add(enemey);
			}
		}

		class PowerSpawner implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				power = new PowerUp();
				PowerUp.alive = true;
				missleCount++;
				missleCountLabel.setForeground(Color.BLACK);
				missleCountLabel.setText("Missle Count: " + missleCount);

			}
		}

		class GameTimer implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (enemySpawn > 800) {
					enemySpawn -= 100;
					enemyTimer.setDelay(enemySpawn);
				}
			}
		}
	}

	public static class Reset implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			//Not Implemented :(
		}
	}

	public static class Exit implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
