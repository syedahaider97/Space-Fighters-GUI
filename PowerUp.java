import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;
public class PowerUp {
	
	Image image;
	static int size;
	static int life;
	static boolean alive = false;
	
	private int xCor, yCor;
	private int duration;
	String type;
	
	String[] powerUps = {"Large","Large","Large","Triple","Triple","Clear"}; //Offsets the number of Power Ups in circulation
	
	public PowerUp() {
		//Places a random Power Up on the board
		Random rand = new Random(System.currentTimeMillis());
		xCor = rand.nextInt(Board.boardSize - Board.boardSize/5) + Board.boardSize/8;
		yCor = rand.nextInt(Board.boardSize - Board.boardSize/7) + Board.boardSize/8;
		
		duration = 700;
		
		type = powerUps[rand.nextInt(powerUps.length)];
		
		ImageIcon ii = new ImageIcon("None.png");
		if(type.equals("Triple")) {
			ii = new ImageIcon("Triple.png");
		}
		else if(type.equals("Large")) {
			ii = new ImageIcon("Large.png");
		}
		else if(type.equals("Clear")) {
			ii = new ImageIcon("Clear.png");
		}
		else if(type.equals("Speed")) {
			ii = new ImageIcon("Speed.png");
		}
		image = ii.getImage();
		
		size = ii.getIconHeight();
	}
	
	public Image getImage(){
		return image;
	}
	public int getxCor() {
		return xCor;
	}
	public int getyCor() {
		return yCor;
	}
	public int getDuration() {
		return duration;
	}
	public String getType() {
		return type;
	}
}
