package mines;

import java.util.Random;

public class Mines
{
	private int height, width;
	private Cordinate board[][];
	private Random rand= new Random();
	//A flag indicating if all the cells are opened.
	private boolean showAll;
	
	/*An inner class that represents the game board and holds its data. */
	private class Cordinate
	{
		private boolean mines, opened, flag;
		private int minesAround;
	}
	
	/*A constructor. */
	public Mines(int height, int width, int numMines)
	{
		int i, j;
		this.height = height;
		this.width = width;
		showAll = false;
		board = new Cordinate[height][width];
		//Initialize the entire game board.
		for (i = 0; i < height; i++)
			for (j = 0; j < width; j++)
				board[i][j] = new Cordinate();
		//Add mines to the board randomly.
		for (i = 0; i < numMines; i++)
		{
			if (!addMine(rand.nextInt(height), rand.nextInt(width)))
					i--;		
		}
	}
	
	/*Adds a mine to the board. */
	public boolean addMine(int i, int j)
	{
		if (!board[i][j].mines)
		{
			board[i][j].mines = true;
			//Updating the amount of mines for each member of the board.
			updateMinesAroundCell();
			return true;
		}
		return false;
	}
	
	/*Opens a cell. */
	public boolean open(int i, int j) 
	{
		int r, c;
		//Check if cell at index i, j is already opened.
	    if (!board[i][j].opened) 
	    {
	    	 //If cell has a mine, return false.
	        if (board[i][j].mines) 
	            return false;
	        board[i][j].opened = true;
	        //If cell has no mines around, recursively open all adjacent cells.
	        if (board[i][j].minesAround == 0) 
	        {
	            for (r = i - 1; r <= i + 1; r++) 
	            {
	                for (c = j - 1; c <= j + 1; c++)
	                {
	                    if (r >= 0 && r < height && c >= 0 && c < width) 
	                        open(r, c);
	                }
	            }
	        }
	    }
	    return true;
	}
	
	/*Adds/Removes a flag from a cell. */
	public void toggleFlag(int x, int y) 
	{
		//If cell has a flag, remove it.
		if (board[x][y].flag)
			board[x][y].flag = false;
		//If the cell is not already opened, set the flag
		else if (!board[x][y].opened)
			board[x][y].flag = true;
	}
	
	/*Returns true if all the cells that aren't mines opened. */
	public boolean isDone() 
	{
		int i, j;
		for (i = 0; i < height; i++)
			for (j = 0; j < width; j++)
			{
				//If a cell is not a mine and not opened, return false
				if (!board[i][j].mines && !board[i][j].opened)
					return false;
			}
		return true;
	}
	
	/*Returns a string representation of the cell. */
	public String get(int i, int j) 
	{
		//Cell has a flag and is not opened.
	    if(board[i][j].flag && !board[i][j].opened) 
	    	return "F";
	    if(board[i][j].opened || showAll) 
	    {
	        if(board[i][j].mines) 
	        	return "X";
	        //If there are any mines around the cell.
	        if(board[i][j].minesAround > 0) 
	        	return "" + board[i][j].minesAround;
	        return " ";
	    }
	    return ".";
	}
	
	/*Sets showAll's value. */
	public void setShowAll(boolean showAll)
	{
		this.showAll = showAll;
	}
	
	/*Returns a string representation of the board.*/
	public String toString() 
	{
		int i, j;
		StringBuilder str = new StringBuilder();
		for (i = 0; i < height; i++)
		{
			for (j = 0; j < width; j++)
			{
				str.append(get(i, j));
			}
			str.append("\n");
		}
		return str.toString();
	}
	
	/*Updates the number of mines around each cell. */
	private void updateMinesAroundCell() 
	{
		int i, j;
		for (i = 0; i < height; i++) 
			for (j = 0; j < width; j++) 
				board[i][j].minesAround = minesAroundCell(i, j);		
	}
	

	/*Returns the amount of mines around the cell. */
	private int minesAroundCell(int i, int j) 
	{
	    int cnt = 0, r, c;
	    //Loop through the 8 cells around the current cell.
	    for (r = i - 1; r <= i + 1; r++) 
	    {
	        for (c = j - 1; c <= j + 1; c++) 
	        {
	            if (r >= 0 && r < height && c >= 0 && c < width) 
	            {
	                if (board[r][c].mines) 
	                    cnt++;
	            }
	        }
	    }
	    //Return the number of mines around the cell.
	    return cnt;
	}
}