import java.io.*;

public class MineModel implements Serializable {
	
	private int BOARD_SIZE = 10;
	private double bombPercent = 0.15;//15% chance of a bomb being on a square
	
	private int[][] board;
	private boolean[][] visible;
	private boolean[][] flags;
	
	MineModel(int size){
		BOARD_SIZE = size;
		board = new int[BOARD_SIZE][BOARD_SIZE];
		visible = new boolean[BOARD_SIZE][BOARD_SIZE];
		flags = new boolean[BOARD_SIZE][BOARD_SIZE];
		resetBoard();
		calculateValues();
		hideEveryThing();
		resetFlags();
	}
	
	// Sets all values from object model file.
	public void loadModel(MineModel dummy){
		this.BOARD_SIZE = dummy.BOARD_SIZE;
		this.bombPercent = dummy.bombPercent;
		this.board = dummy.board;
		this.visible = dummy.visible;
		this.flags = dummy.flags;
	}
	
	public int[][] getBoard(){
		return board;
	}
	public boolean[][] getVisible(){
		return visible;
	}
	//initiates the bombs
	public void resetBoard(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if (Math.random() < bombPercent){
					board[i][j] = -1;
				}
				else{
					board[i][j] = 0;
				}
			}
		}
	}
	//sets all flags to false
	public void resetFlags(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				flags[i][j] = false;
			}
		}
	}
	//sets all squares to not visible
	public void hideEveryThing(){
		for(int i = 0; i < visible.length; i++){
			for(int j = 0; j < visible[i].length; j++){
				visible[i][j] = false;
			}
		}
	}
	//checks if element is out of bounds
	public boolean isOutOfBounds(int row, int col){
		if(row < 0 || row >= BOARD_SIZE){
			return true;
		}
		else if(col < 0 || col >= BOARD_SIZE){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void calculateValues(){
		int temp;
		//loop through board
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				temp = 0;
				//check around each element if not a bomb
				if(board[i][j] != -1){
					for(int xAround = -1; xAround <= 1; xAround++){
						for(int yAround = -1; yAround <= 1; yAround++){
							if(!isOutOfBounds((i+xAround),(j+yAround))){//if its not out of bounds
								if(board[i+xAround][j+yAround] == -1){
									temp++;
								}
							}
						}
					}
					board[i][j] = temp;
				}
			}
		}
	}
	
	public void openBoard(int row, int col){
		if (!isOutOfBounds(row, col)){//checks if it is out of bounds
			if (board[row][col] != -1){//checks to see if its not a bomb
				if (getVisible(row, col) == false){//check if it is revealed
					setVisible(row, col);
					//check if any squares around it are 0 and calls it on them
					if(board[row][col] == 0){
						for(int xAround = -1; xAround <= 1; xAround++){
							for(int yAround = -1; yAround <= 1; yAround++){
								if (!isOutOfBounds(row+xAround, col+yAround)){//checks if the surrounding tiles are out of bounds
								//if (board[row+xAround][col+yAround] == 0){//and if it equals 0 call openBoard on it
								openBoard(row-1, col-1);//top left
								openBoard(row-1, col);//top mid
								openBoard(row-1, col+1);//top right
								openBoard(row, col-1);//left
								openBoard(row, col+1);//right
								openBoard(row+1, col-1);//bottom left
								openBoard(row+1, col);//bottom mid
								openBoard(row+1, col+1);//bottom right
								//}
								}
							}
						}
					}
					else{
						for(int xAround = -1; xAround <= 1; xAround++){
							for(int yAround = -1; yAround <= 1; yAround++){
								if (!isOutOfBounds(row+xAround, col+yAround)){
									if (board[row+xAround][col+yAround] == 0){
										openBoard(row+xAround, col+yAround);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	//GETTERS
	public int getElement(int row, int col){
		return board[row][col];
	}
	
	public int getMines(){
		int temp = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if (board[i][j] == -1){
					temp++;
				}
			}
		}
		return temp;
	}
	
	public int minesFlagged(){
		int temp = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if (board[i][j] == -1 && flags[i][j]){
					temp++;
				}
			}
		}
		return temp;
	}
	
	public boolean getVisible(int row, int col){//
		return visible[row][col];
	}
	
	public int getNotVisible(){
		int temp = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(!visible[i][j]){
					temp++;
				}
			}
		}
		return temp;
	}
	
	public boolean getFlag(int row, int col){
		return flags[row][col];
	}
	
	public boolean isMine(int row, int col){
		if (board[row][col] == -1){
			return true;
		}
		return false;
	}
	
	public int getSize(){
		return BOARD_SIZE;
	}
	
	//SETTERS
	public void setVisible(int row, int col){
		visible[row][col] = true;
	}
	
	public void setFlag(int row, int col){
		flags[row][col] = true;
	}
	
	public void removeFlag(int row, int col){
		flags[row][col] = false;
	}
	
	public void setBombChance(double chance){
		bombPercent = chance;
	}
	
	public void middleClick(int row, int col){//Only works half the time
		int temp = 0;
		for(int xAround = -1; xAround <= 1; xAround++){
			for(int yAround = -1; yAround <= 1; yAround++){
				if (!isOutOfBounds(row+xAround, col+yAround)){//if its not out of bounds
					if (flags[row+xAround][col+yAround]){//if there is a flag around the element, add 1 to temp
						temp++;
					}
					if (temp == board[row][col]){
						if (!flags[row+xAround][col+yAround]){
							openBoard(row+xAround, col+yAround);
						}
					}
				}
			}
		}
	}
	
	// Outputs current model object to save file.
	public void saveGame(File save){
		try{
			FileOutputStream saveFile = new FileOutputStream(save);
            ObjectOutputStream oos = new ObjectOutputStream(saveFile);
            oos.writeObject(this);
            oos.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Loads serialized object into the current model.
	public void loadGame(File load){
		try{
			FileInputStream saveFile = new FileInputStream(load);
            ObjectInputStream ois = new ObjectInputStream(saveFile);
			MineModel model = (MineModel)ois.readObject();
			loadModel(model);
            ois.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
}