public class Missle {

	private int xCor, yCor;
	private double dx, dy;
	private int size;
	private String missleType;

	public Missle(int x, int y, String type) {
		xCor = x + Jet.imageSize / 2;
		yCor = y + Jet.imageSize / 2 - 9;
		missleType = type;

		size = 20;

		if (type.equals("Large")) {
			size = 35;
		}
	}

	public int getxCor() {
		return xCor;
	}

	public int getyCor() {
		return yCor;
	}

	public int getSize() {
		return size;
	}

	public void run() {
		//For use with triple
		if (missleType.equals("Up")) {
			dx = 2;
			dy = 2;
		} else if (missleType.equals("Down")) {
			dx = 2;
			dy = -2;
		} else {
			//For use with all other missiles
			dx = 2;
			dy = 0;
		}

		xCor += dx;
		yCor += dy;
	}
}
