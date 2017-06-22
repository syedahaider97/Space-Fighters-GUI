import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

public class Jet {

	private Image image;
	private int xCor, yCor;
	private int dx, dy;
	static int imageSize;

	ArrayList<Missle> missleList = new ArrayList<Missle>();

	public Jet(int boardSize) {

		ImageIcon icon = new ImageIcon("arrow.png");
		image = icon.getImage();
		imageSize = icon.getIconHeight();
		xCor = -10;
		yCor = boardSize / 3 + 20;
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

	public ArrayList<Missle> getMissleList() {
		return missleList;
	}

	public void move() {
		// Wrapping around top and bottom
		if (yCor + dy + imageSize > Board.boardSize) {
			yCor = 0;
		}
		if (yCor + dy < -imageSize / 2) {
			yCor = Board.boardSize - imageSize;
		}

		// Preventing access off the board
		if (xCor + imageSize + dx > Board.boardSize || xCor + dx < -10) {
			return;
		}
		xCor += dx;
		yCor += dy;
	}

	public void fireMissle(String type) {

		if (type.equals("Triple")) {
			// Creates the special angled missiles and adds them to the list to
			// be drawn
			Missle m2 = new Missle(xCor, yCor, "Up");
			Missle m3 = new Missle(xCor, yCor, "Down");
			missleList.add(m2);
			missleList.add(m3);
			m2.run();
			m3.run();
		}
		if (type.equals("Clear")) {
			// Adds a column of missiles and fires them simultaneously
			for (int i = -50; i < Board.boardSize; i += 20) {
				Missle m = new Missle(0, i, "regular");
				missleList.add(m);
				m.run();
			}
			return;
		}

		// Code for regular missle fire
		Missle missle = new Missle(xCor, yCor, type);
		missleList.add(missle);
		missle.run();
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {

		case KeyEvent.VK_DOWN:
			dy = 5;
			break;
		case KeyEvent.VK_UP:
			dy = -5;
			break;
		case KeyEvent.VK_LEFT:
			dx = -5;
			break;
		case KeyEvent.VK_RIGHT:
			dx = 5;
			break;
		case KeyEvent.VK_SPACE:
			// Checks to make sure that your missile capacity has not been
			// depleted
			if (Board.missleCount > 0) {
				fireMissle(Board.missleType);
				Board.missleCount--;
			} else {
				Board.missleCountLabel.setForeground(Color.RED);
			}

			break;
		case KeyEvent.VK_C:
			if (Board.clear > 0) {
				fireMissle("Clear");
				Board.clearLabel.setText("Clear: " + --Board.clear);
			}
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
			dy = 0;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
			dx = 0;
		}
	}

}
