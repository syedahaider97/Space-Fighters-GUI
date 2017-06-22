import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Enemy {

	Image image;
	static int enemySize;
	private int xCor, yCor;
	private int dx;
	static ArrayList<Enemy> enemyList = new ArrayList<Enemy>();

	public Enemy() {

		ImageIcon ii = new ImageIcon("ghost.png");
		image = ii.getImage();
		enemySize = ii.getIconHeight() * 7 / 8;
		Random rand = new Random(System.currentTimeMillis());
		xCor = Board.boardSize;
		yCor = rand.nextInt(Board.boardSize * 4 / 5);
		dx = -2;
	}

	public Image getImage() {
		return image;
	}

	public int getxCor() {
		return xCor;
	}

	public int getyCor() {
		return yCor;
	}

	public void move() {
		xCor += dx;
	}
}
