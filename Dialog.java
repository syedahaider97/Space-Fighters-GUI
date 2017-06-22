import javax.swing.*;
import java.awt.*;

public class Dialog extends JDialog{

	public Dialog(JFrame frame) {
		
		//Dialog Box setup code
		super(frame,"Game Over",true);
		setLayout(new FlowLayout());
		setSize(360,200);
		setLocationRelativeTo(frame);
		
		//Labels and buttons
		JLabel prompt = new JLabel("Game Over");
		prompt.setFont(new Font("Arial",Font.BOLD,36));
		prompt.setForeground(Color.RED);
		add(prompt);

		JLabel prompt2 = new JLabel("Final Score: " + Board.score);
		prompt2.setFont(new Font("Arial",Font.BOLD,36));
		add(prompt2);
		
		JButton reset = new JButton("Reset");
		reset.setPreferredSize(new Dimension(130, 40));
		reset.addActionListener(new Board.Reset());
		add(reset);
		
		JButton exit = new JButton("Exit");
		exit.setPreferredSize(new Dimension(130,40));
		exit.addActionListener(new Board.Exit());
		add(exit);
		
	}
}
