import javax.swing.*;
import javax.swing.UIManager;

public class MineFrame extends JFrame{
	private static final int SCREEN_WIDTH = 900;
    private static final int SCREEN_HEIGHT = 947;
	
	MineFrame(){
		setTitle("Minesweeper");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		MinePanel panel = new MinePanel();
		add(panel);
		setJMenuBar(panel.menu);
		setVisible(true);
	}
}