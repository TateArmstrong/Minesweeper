import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.*;

public class MinePanel extends JPanel{
	
	private int SIZE = 12;
	
	private MineModel model;
	private MouseHandler handler = new MouseHandler();
	private JButton[][] squares;
	private TitledBorder border;
	public MineMenu menu = new MineMenu();
	private JFileChooser fc;
	
	MinePanel(){
		squares = new JButton[SIZE][SIZE];
		model = new MineModel(SIZE);
		setLayout(new GridLayout(SIZE, SIZE));
		border = new TitledBorder("Minesweeper - " + model.getMines());
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.TOP);
		setBorder(border);
		fc = new JFileChooser(".");
		fc.setSelectedFile(new File("MinesweeperSave.ser"));
		fc.setFileFilter(new FileNameExtensionFilter("Minesweeper Save File", "ser"));
		
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				squares[i][j] = new JButton();
				squares[i][j].addMouseListener(handler);
				squares[i][j].setBackground(new Color(204, 204, 204));
				squares[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
				add(squares[i][j]);
			}
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawString("WATS UP", this.getWidth()/2, this.getHeight()/2);
	}
	
	private class MouseHandler extends MouseAdapter{
		
		public void mousePressed(MouseEvent e){
			
			for(int i = 0; i < squares.length; i++){
				for(int j = 0; j < squares[i].length; j++){
					if (e.getSource() == squares[i][j]){
						if(e.getButton() == MouseEvent.BUTTON3){
							rightClickFunc(i, j);
						}
						else if(e.getButton() == MouseEvent.BUTTON1){
							loseCheck(i, j);
							model.openBoard(i, j);
							winCheck();
							setUpButtons();
						}
						else if(e.getButton() == MouseEvent.BUTTON2){
							model.middleClick(i, j);
							setUpButtons();
						}
					}
				}
			}
		}
	}
	
	private void setUpButtons(){
		//loop through buttons and set them to their elements if they are visible
		for(int i = 0; i < squares.length; i++){
			for(int j = 0; j < squares[i].length; j++){
				if (model.getFlag(i, j) == true){
					squares[i][j].setText("F");
					squares[i][j].setForeground(Color.RED);
				}
				else if(model.getFlag(i, j) == false){
					squares[i][j].setText("");
					squares[i][j].setForeground(Color.RED);
				}
				if (model.getVisible(i, j) == true){//not finished
					squares[i][j].setBackground(new Color(153, 153, 153));
					switch (model.getElement(i, j)){
						case 1:
							squares[i][j].setForeground(Color.blue); break;
						case 2:
							squares[i][j].setForeground(new Color(0, 204, 0)); break;
						case 3:
							squares[i][j].setForeground(new Color(204, 0, 0)); break;
						case 4:
							squares[i][j].setForeground(new Color(102, 0, 153)); break;
						case 5:
							squares[i][j].setForeground(new Color(153, 0, 0)); break;
					}
					if (model.getElement(i, j) != 0){
						squares[i][j].setText(Integer.toString(model.getElement(i, j)));
					}
				}
				else{
					squares[i][j].setBackground(new Color(204, 204, 204));
				}
			}
		}
	}
	
	private void winCheck(){
		border.setTitle("Minesweeper - " + (model.getMines()-model.minesFlagged()));
		if (model.getNotVisible() == model.getMines()){
			revealAll();
			JOptionPane.showMessageDialog(null, "Dang! how did you do that?!");
			//System.exit(1);
		}
		repaint();
	}
	
	private void loseCheck(int row, int col){
		if (model.getElement(row, col) == -1){
			revealAll();
			JOptionPane.showMessageDialog(null, "Dang Bro, You really stepped on a mine?");
			//System.exit(1);
		}
	}
	
	private void rightClickFunc(int row, int col){
		if (!model.getVisible(row, col)){//check if its visible
			if (!model.getFlag(row, col)){// check if there is not a flag
				model.setFlag(row, col);
				setUpButtons();
				winCheck();
			}
			else{
				model.removeFlag(row, col);
				setUpButtons();
				winCheck();
			}
		}
	}
	
	private void revealAll(){
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				model.setVisible(i, j);
			}
		}
		setUpButtons();
	}
	
	class MineMenu extends JMenuBar{
	
		MenuListener ml = new MenuListener();
		
		public MineMenu(){
			JMenu file = new JMenu("File");
			
			JMenu item1 = new JMenu("New");
			JMenuItem item2 = new JMenuItem("Save");
			item2.addActionListener(ml);
			JMenuItem item3 = new JMenuItem("Load");
			item3.addActionListener(ml);
			JMenuItem item4 = new JMenuItem("Quit");
			item4.addActionListener(ml);
			
			JMenuItem s1 = new JMenuItem("Difficulty: 1");
			s1.addActionListener(ml);
			JMenuItem s2 = new JMenuItem("Difficulty: 2");
			s2.addActionListener(ml);
			JMenuItem s3 = new JMenuItem("Difficulty: 3");
			s3.addActionListener(ml);
			
			item1.add(s1);
			item1.add(s2);
			item1.add(s3);
			
			file.add(item1);
			file.add(item2);
			file.add(item3);
			file.add(item4);
			
			this.add(file);
		}
		
		class MenuListener implements ActionListener {
			
			public void actionPerformed(ActionEvent e) {
				System.out.println("User selected: " + e.getActionCommand());
				if(e.getActionCommand().equals("Difficulty: 1")){
					model.setBombChance(0.15);
					model.resetBoard();
					model.calculateValues();
					model.hideEveryThing();
					model.resetFlags();
					setUpButtons();
				}
				else if(e.getActionCommand().equals("Difficulty: 2")){
					model.setBombChance(0.25);
					model.resetBoard();
					model.calculateValues();
					model.hideEveryThing();
					model.resetFlags();
					setUpButtons();
				}
				else if(e.getActionCommand().equals("Difficulty: 3")){
					model.setBombChance(0.35);
					model.resetBoard();
					model.calculateValues();
					model.hideEveryThing();
					model.resetFlags();
					setUpButtons();
				}
				else if(e.getActionCommand().equals("Load")){
					int returnVal = fc.showOpenDialog(null);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						System.out.println("File chosen: " + file);
						model.loadGame(file);// TO-DO: Implement dynamic board size
						setUpButtons();
					}
				}
				else if(e.getActionCommand().equals("Save")){
					int returnVal = fc.showSaveDialog(null);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						System.out.println("File chosen: " + file);
						model.saveGame(file);
					}
				}
				else if(e.getActionCommand().equals("Quit")){
					System.exit(0);
				}
			}
		}
	}
}