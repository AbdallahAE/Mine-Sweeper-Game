import java.util.Random;

/**
 * The implementation of a Minesweeper game.
 */
public class MineSweeper{

 
    //******************************************************
    //*******    BELOW THIS LINE IS PROVIDED code    *******
    //*******            Do NOT edit code!           *******
    //*******		  Remember to add JavaDoc		 *******
    //******************************************************

    /**
     * Supported game levels including TINY, EASY, MEDIUM, HARD, or CUSTOM.
     */
    public enum Level {
        /**
         * Level Tiny with row and column = 5 and mines = 3.
         */
        TINY, 
        /**
         * Level Easy with row and column = 9 and mines = 10.
         */
        EASY, 
        /**
         * Level Medium with row and column = 16 and mines = 40.
         */
        MEDIUM, 
        /**
         * Level Hard with row = 16 and column = 30 and mines = 99.
         */
        HARD, 
        /**
         * Level Custom with custom rows, columns and mines.
         */
        CUSTOM 
    }
    
    /**
     * Rows in the game for level Easy.
     */
    private static int ROWS_EASY = 9;
    /**
     * Columns in the game for level Easy.
     */
    private static int COLS_EASY = 9;
    /**
     * Mines in the game for level Easy.
     */
    private static int MINES_EASY = 10;

    /**
     * Rows in the game for level Tiny.
     */
    private static int ROWS_TINY = 5;
    /**
     * Columns in the game for level Tiny.
     */
    private static int COLS_TINY = 5;
    /**
     * Mines in the game for level Tiny.
     */
    private static int MINES_TINY = 3;
    
    /**
     * Rows in the game for level Medium.
     */
    private static int ROWS_MEDIUM = 16;
    /**
     * Columns in the game for level Medium.
     */
    private static int COLS_MEDIUM = 16;
    /**
     * Mines in the game for level Medium.
     */
    private static int MINES_MEDIUM = 40;

    /**
     * Rows in the game for level Hard.
     */
    private static int ROWS_HARD = 16;
    /**
     * Columns in the game for level Hard.
     */
    private static int COLS_HARD = 30;
    /**
     * Mines in the game for level Hard.
     */
    private static int MINES_HARD = 99;

    /**
     * The 2d board of cells.
     */
    private DynGrid310<Cell> board;

    /**
     * Number of rows of the board.
     */
    private int rowCount;
    
    /**
     * Number of columns of the board.
     */
    private int colCount;

	/**
     * Number of mines in the board.
     */
	private int mineTotalCount;

	/**
     * Number of cells clicked / exposed.
     */
	private int clickedCount; 

	/**
     * Number of cells flagged as a mine.
     */
	private int flaggedCount; 


    /**
     * Game possible status.
     */
    public enum Status {
        /**
         * The game didn't start yet.
         */
        INIT, 
        /**
         * The game is underway.
         */
        INGAME, 
        /**
         * User clicked a mine and lost.
         */
        EXPLODED, 
        /**
         * User solved the game.
         */
        SOLVED
    }
    /**
     * The status of the game.
     */
    private Status status; 

    /**
     * String names of status.
     */
    public final static String[] Status_STRINGS = {
        "INIT", "IN_GAME", "EXPLODED", "SOLVED"
    };
    
    /**
     * Constructor, initialize game based on a provided seed for random numbers and the specified level.
     * @param seed Seed for the random numbers.
     * @param level Level of the game (Tiny, Easy, Medium, Hard).
     */
    public MineSweeper(int seed, Level level){
    
        //if level is customized, need more details (number of rows/columns/mines)
        if (level==Level.CUSTOM)
            throw new IllegalArgumentException("Customized games need more parameters!");
            
        //set number of rows, columns, mines based on the pre-defined levels
        switch(level){
            case TINY:
                rowCount = ROWS_TINY;
                colCount = COLS_TINY;
                mineTotalCount = MINES_TINY;
                break;
            case EASY:
                rowCount = ROWS_EASY;
                colCount = COLS_EASY;
                mineTotalCount = MINES_EASY;
                break;
            case MEDIUM:
                rowCount = ROWS_MEDIUM;
                colCount = COLS_MEDIUM;
                mineTotalCount = MINES_MEDIUM;
                break;
            case HARD:
                rowCount = ROWS_HARD;
                colCount = COLS_HARD;
                mineTotalCount = MINES_HARD;
                break;
            default:
                //should not be able to reach here!
                rowCount = ROWS_TINY;
                colCount = COLS_TINY;
                mineTotalCount = MINES_TINY;
		}
        
        //create an empty board of the needed size
        board = genEmptyBoard(rowCount, colCount);
        
        //place mines, and initialize cells
        initBoard(seed);
    }
    
    /**
     * Constructor: should only be used for customized games.
     * @param seed Seed for the random numbers.
     * @param level Level of the game, should only be Custom.
     * @param rowCount Amount of rows in the game.
     * @param colCount Amount of columns in the game.
     * @param mineCount Amount of mines in the game.
     */
    public MineSweeper(int seed, Level level, int rowCount, int colCount, int mineCount){
        
        if (level != Level.CUSTOM)
        	throw new IllegalArgumentException("Only customized games need more parameters!");
        
        //set number of rows/columns/mines
        //assume all numbers are valid (check MineGUI for additional checking code)	
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.mineTotalCount = mineCount;
        
        
        //create an empty board of the needed size: you implement this method
        board = genEmptyBoard(rowCount, colCount);
        
        //place mines, and initialize cells: you implement part of this method
       	initBoard(seed);
        
    }
       
    /**
     * Method to initialize the game, including placing mines. Should be invoked after creating an empty board.
     * @param seed Seed for the random number.
     */
    public void initBoard(int seed){
        
        //use seed to initialize a random number sequence
        Random random = new Random(seed);
        
        //randomly place mines on board
        int mineNum = 0;
        for ( ;mineNum<mineTotalCount;){
        
            //generate next (row, col)
            int row = random.nextInt(rowCount);
            int col = random.nextInt(colCount);
            
             
            //cell already has a mine: try again
            if (hasMine(row, col)){
                continue;
            }
            
            //place mine
            board.get(row,col).setMine();
            mineNum++;
        }
        //System.out.println(board);
        
        //calculate nbr counts for each cell
        for (int row=0; row<rowCount; row++){
            for (int col=0; col<colCount; col++){
            
                int count = countNbrMines(row, col);
                board.get(row,col).setCount(count);
            }
        }
        
        //initialize other game settings   
        status = Status.INIT;
           
        flaggedCount = 0;
        clickedCount = 0;

    }
    	
    /**
     * Reports number of rows.
     * @return Rows.
     */
    public int rowCount() { return rowCount; }
    
    /**
     * Reports number of columns.
     * @return Columns.
     */
    public int colCount() { return colCount; }

    /**
     * Reports whether board is solved.
     * @return Solved status of the game.
     */
    public boolean isSolved(){ return status == Status.SOLVED;    }
    
    /**
     * Reports whether a mine has exploded.
     * @return Exploded status of the game.
     */
    public boolean isExploded(){ return status == Status.EXPLODED; }

    /**
     * Displays board, use this for debugging.
     * @return String format of the board.
     */
    public String boardToString(){
        StringBuilder sb = new StringBuilder();
        
        //header of column indexes
        sb.append("- |");
        for (int j=0; j<board.getNumCol(); j++){
			sb.append(j +"|");
		}
        sb.append("\n");
        
    	for(int i=0; i<board.getNumRow(); i++){
            sb.append(i+" |");
    		for (int j=0;j<board.getNumCol(); j++){
      			sb.append(board.get(i,j).toString());
      		    sb.append("|");
      		}
      		sb.append("\n");
    	}
    	return sb.toString().trim();

    }
    
    /** Displays the game status and board.
     * @return A string representation of the board.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Board Size: " + rowCount() + " x " + colCount() + "\n");
        sb.append("Total mines: " + mineTotalCount + "\n");
        sb.append("Remaining mines: " + mineLeft() + "\n");
        sb.append("Game status: " + getStatus() + "\n");
        
        sb.append(boardToString());
        return sb.toString().trim();
    }

    //******************************************************
    //*******      Methods to report cell details    *******
    //*******     These are used by GUI for display  *******
    //******* Check Cell class for helpful operations*******
    //******************************************************

    /**
     * Returns whether the given cell is flagged.
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns true if cell at (row,col) is flagged, false otherwise, return false for invalid cell indexes.
     */
    public boolean isFlagged(int row, int col){
    	
        if (!board.isValidCell(row,col)){
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.isFlagged());
    }
    
    /**
     * Returns whether the given cell is visible or has been clicked.
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns true if cell at (row,col) is not hidden, false otherwise, return false for invalid cell indexes.
     */
    public boolean isVisible(int row, int col){
    	
        if (!board.isValidCell(row,col)){
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.visible());               
    }
    
    /**
     * Returns whether the given cell has a mine.
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns true if cell at (row,col) has a mine regardless whether it has been flagged or not, false otherwise, return false for invalid cell indexes.
     */
    public boolean hasMine(int row, int col){
    	    	
        if (!board.isValidCell(row,col)){
            return false;
        }
 
        Cell cell = board.get(row, col);
        return (cell.hasMine());               
    }
    
    /**
     * Returns the count associated with the cell.
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns the count associated with cell at (row,col) has a mine return -2 for invalid cell indexes.
     */
    public int getCount(int row, int col){
    	
        if (!board.isValidCell(row,col)){
            return -2;
        }
 
        Cell cell = board.get(row, col);
        return (cell.getCount());                    
    }
    
    //******************************************************
    //*******      Methods to report game status     *******
    //*******     These are used by GUI for display  *******
    //******************************************************

    /**
     * Reports how many mines have not be flagged.
     * @return Number of mines left to flag.
     */
    public int mineLeft() { 
    	// report how many mines have not be flagged
    	return mineTotalCount-flaggedCount; 
    	
    }
    
    /**
     * Reports current game status.
     * @return String representation of the game's status.
     */
    public String getStatus() { 
    	// report current game status
    	return Status_STRINGS[status.ordinal()]; 
    	
    }


    //******************************************************
    //*******  Methods reserved for testing/grading  *******
    //******************************************************

    /**
     * Returns the game board.
     * @return Game board.
     */
    public DynGrid310<Cell> getBoard(){ return board;}

    /**
     * Sets game board.
     * @param newBoard Game board.
     * @param mineCount Number of mines.
     */
    public void setBoard(DynGrid310<Cell> newBoard, int mineCount) {
		//set board
		this.board = newBoard;
		
		//set size
		rowCount = board.getNumRow();
		colCount = board.getNumCol();
		
        //set other features
        status = Status.INIT;
           
        flaggedCount = 0;
        clickedCount = 0;
        mineTotalCount = mineCount;
	}

    //******************************************************
    //*******       END of PROVIDED code             *******
    //******************************************************


    //******************************************************
    //*******        Code you need to implement      *******
    //*******		   Remember to add JavaDoc		 *******
    //******************************************************

    // ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

    //*******************************************************
    //******* Methods to support board initialization *******
    //*******************************************************
	
    /**
     * Creates a grid with rowNum x colNum with individual cells in default state.
     * @param rowNum Number of rows.
     * @param colNum Number of columns.
     * @return Returns a game board with default cells (no mines), if rowNum or colNum is not positive, returns null.
     */
    public static DynGrid310<Cell> genEmptyBoard(int rowNum, int colNum){
        
        //create and return a grid with rowNum x colNum individual cells in it
        // - all cells are default cell objects (no mines)
        // - if rowNum or colNum is not positive, return null
    	
        //amortized O(rowCount x colCount)
        if(rowNum < 0 || colNum < 0)
    	    return null;

        DynGrid310<Cell> board = new DynGrid310<>();
    
        for(int i=0; i<rowNum; i++)
        {
            DynArr310<Cell> col = new DynArr310<>(colNum);
            for(int j=0; j<colNum; j++)
                col.add(new Cell());
            board.addRow(i, col);
        }
        return board;
    }
    
    /**
     * Counts the number of mines in neighbor cells of given cell.
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns number of neighboring mines, return -2 for invalid row / col indexes, return -1 if the cell given has a mine underneath it.
     */
    public int countNbrMines(int row, int col){
        // count the number of mines in the neighbor cells of cell (row, col)
        // return -2 for invalid row / col indexes
        // return -1 if cell at (row, col) has a mine underneath it
    	
        // O(1)
        if(!board.isValidCell(row, col))
    	    return -2;
        if(hasMine(row, col))
            return -1;
        
        int count = 0;
        if(hasMine(row-1, col-1))
            count++;
        if(hasMine(row-1, col))
            count++;
        if(hasMine(row-1, col+1))
            count++;
        if(hasMine(row, col-1))
            count++;
        if(hasMine(row, col+1))
            count++;
        if(hasMine(row+1, col-1))
            count++;
        if(hasMine(row+1, col))
            count++;
        if(hasMine(row+1, col+1))
            count++;
    	return count;
    }
    

    //******************************************************
    //*******   Methods to support game operations   *******
    //******************************************************
  
    /**
     * A recursive function that sets the cells visible to all those that are zero count.
     * @param row Row of the cell.
     * @param col Column of the cell.
     */
    private void zeroCount(int row, int col)
    {
        // Check if the cell is not a zero count and doesn't have a bomb, set it to visible
        // Else if the cell is a zero count, recursivily call the method again but on adjacent cells

        if(board.get(row, col).getCount() > 0)
        {
            board.get(row, col).setVisible();
            clickedCount++;
            return;
        }
        else if(board.get(row, col).getCount() == 0)
        {
            board.get(row, col).setVisible();
            clickedCount++;
            if(board.isValidCell(row-1, col-1) && !board.get(row-1, col-1).visible())
                zeroCount(row-1, col-1);
            if(board.isValidCell(row-1, col) && !board.get(row-1, col).visible())
                zeroCount(row-1, col);
            if(board.isValidCell(row-1, col+1) && !board.get(row-1, col+1).visible())
                zeroCount(row-1, col+1);
            if(board.isValidCell(row, col-1) && !board.get(row, col-1).visible())
                zeroCount(row, col-1);
            if(board.isValidCell(row, col+1) && !board.get(row, col+1).visible())
                zeroCount(row, col+1);
            if(board.isValidCell(row+1, col-1) && !board.get(row+1, col-1).visible())
                zeroCount(row+1, col-1);
            if(board.isValidCell(row+1, col) && !board.get(row+1, col).visible())
                zeroCount(row+1, col);
            if(board.isValidCell(row+1, col+1) && !board.get(row+1, col+1).visible())
                zeroCount(row+1, col+1);
        }
    }

    /**
     * Opens the cell at given row and col: for a valid cell location - no change if cell is already flagged or exposed - if cell has a mine, open it would explode the mine - otherwise, open this cell and return number of mines adjacent to it, if the cell is not adjacent to any mines also open all zero-count cells that are connected to this cell, as well as all cells that are orthogonally or diagonally adjacent to those zero-count cells.
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns if cell is already flagged or exposed, return -2, if cell has a mine return -1, return number of mines adjacent to it.
     */
    public int clickAt(int row, int col){
    	// open cell located at (row,col)
    	// for a valid cell location:
    	//	- no change if cell is already flagged or exposed, return -2
    	//  - if cell has a mine, open it would explode the mine, 
    	//		update game status accordingly and return -1
    	//  - otherwise, open this cell and return number of mines adjacent to it
    	//		- if the cell is not adjacent to any mines (i.e. a zero-count cell), 
    	//			also open all zero-count cells that are connected to this cell, 
    	//			as well as all cells that are orthogonally or diagonally adjacent 
    	//			to those zero-count cells. 
    	//		- HINT: recursion can really help! Consider define private helper methods.
    	//  - update game status as needed
    	//	- update other game features as needed
    	//
    	// for an invalid cell location:
    	//	- no change and return -2
    	int result = -2;
        if(!board.isValidCell(row, col) || board.get(row, col).isFlagged() || board.get(row, col).visible())
    	    result = -2;
        else if(board.get(row, col).hasMine())
        {
            board.get(row, col).setVisible();
            status = Status.EXPLODED;
            result = -1;
        }
        else if(board.get(row, col).getCount() == 0)
        {
            zeroCount(row, col);
            result = 0;
        }
        else
        {
            board.get(row, col).setVisible();
            clickedCount++;
            result = board.get(row, col).getCount();
        }
        if(clickedCount + mineTotalCount == rowCount * colCount)
            status = Status.SOLVED;
        else if(status != Status.EXPLODED)
            status = Status.INGAME;
        return result;
   	}
    
    /**
     * Flags the cell located at (row,col).
     * @param row Row of the cell.
     * @param col Coulmn of the cell.
     * @return Returns whether the cell is flagged or not.
     */
    public boolean flagAt(int row, int col){
        //flag at cell located at (row,col), 
        //return whether the cell is flagged or not
        //
        //	- no change if cell is not hidden (already open), return false
        //	- otherwise, flag the cell as needed and update relevant game features
        //  - update game status as needed
        //
        // - return false for an invalid cell location
        // O(1)
        if(!board.isValidCell(row, col))
            return false;
        
        if(board.get(row, col).visible())
            return false;
        
        board.get(row, col).setFlagged();
        flaggedCount++;
        return true;
         
    }

    /**
     * Un-flags the cell located at (row,col).
     * @param row Row of the cell.
     * @param col Column of the cell.
     * @return Returns whether the cell is updated from flagged to unflagged.
     */
    public boolean unFlagAt(int row, int col){
        //Un-flag at cell located at (row,col), 
        //return whether the cell is updated from flagged to unflagged 
        //
        //	- no change if cell is not flagged before, return false
        //	- otherwise, unflag the cell and update relevant game features
        
        // - return false for an invalid cell location
        // O(1)
        if(!board.isValidCell(row, col) || !board.get(row, col).isFlagged())
            return false;
        
        board.get(row, col).unFlagged();
        flaggedCount--;
        return true;
      
    }

    
       

    //******************************************************
    //*******     BELOW THIS LINE IS TESTING CODE    *******
    //*******      Edit it as much as you'd like!    *******
    //*******		Remember to add JavaDoc			 *******
    //******************************************************

    
    /**
     * Tests the MineSweeper class.
     * @param args String array (not used).
     */
    public static void main(String args[]){
        //basic: get an empty board with no mines
        DynGrid310<Cell> myBoard = MineSweeper.genEmptyBoard(3,4);

        //board size, all 12 cells should be in the default state, no mines
        if (myBoard.getNumRow() == 3 && myBoard.getNumCol()==4 &&
    		!myBoard.get(0,0).hasMine() && !myBoard.get(1,3).visible() &&
    		!myBoard.get(2,2).isFlagged() && myBoard.get(2,1).getCount()==-1){
    		System.out.println("Yay 0");
    	}

        //init a game at TINY level
        //use the same random number sequence as GUI  - 
        //	this will create the same board as Table 2 of p1 spec PDF.
        // you can change this for your own testing.

        Random random = new Random(10);
        MineSweeper game = new MineSweeper(random.nextInt(),Level.TINY);

        //print out the initial board and verify game setting
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 |?|?|?|?|?|
        //1 |?|?|?|?|?|
        //2 |?|?|?|?|?|
        //3 |?|?|?|?|?|
        //4 |?|?|?|?|?|    
        
        //countNbrMines 
        if (game.countNbrMines(0,0) == 0 && game.countNbrMines(4,2) == 1 &&
        	game.countNbrMines(3,3) == 3 &&	game.countNbrMines(2,3) == -1 &&
        	game.countNbrMines(5,5) == -2){
        	System.out.println("Yay 1");
        }
        
        //first click at (3,3)
        if (game.clickAt(-1,0) == -2 && game.clickAt(3,3) == 3 &&
        	game.isVisible(3,3) && !game.isVisible(0,0) && 
        	game.getStatus().equals("IN_GAME") && game.mineLeft() == 3){
        	System.out.println("Yay 2");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 |?|?|?|?|?|
        //1 |?|?|?|?|?|
        //2 |?|?|?|?|?|
        //3 |?|?|?|3|?|
        //4 |?|?|?|?|?|

        //click at a mine cell
        if (game.clickAt(2,3) == -1 && game.isVisible(2,3) &&
        	game.getStatus().equals("EXPLODED") ){
        	System.out.println("Yay 3");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 |?|?|?|?|?|
        //1 |?|?|?|?|?|
        //2 |?|?|?|X|?|
        //3 |?|?|?|3|?|
        //4 |?|?|?|?|?|

        //start over with the same board
        random = new Random(10);
        game = new MineSweeper(random.nextInt(),Level.TINY);
        game.clickAt(3,3);
        
        //flag and unflag
        if (game.flagAt(2,3) && !game.isVisible(2,3)  &&
        	game.isFlagged(2,3) && game.flagAt(2,4) && 
        	game.mineLeft() == 1 && game.unFlagAt(2,3) &&
        	!game.isFlagged(2,3) && game.mineLeft() == 2){
        	System.out.println("Yay 4");
        }
        
        //cell state & operations
        // - a flagged cell can not be clicked
        // - flag a cell already flagged does not change anything but still returns true
        // - an opened cell cannot be flagged or unflagged
        // - a hidden cell not flagged cannot be unflagged
        if (game.clickAt(2,4) == -2 && game.flagAt(2,4) &&
			!game.flagAt(3,3) && !game.unFlagAt(3,3) &&
			!game.unFlagAt(2,3)){
        	System.out.println("Yay 5");
        }

		//clicking on a zero-count cell
		if (game.clickAt(0,0) == 0 && game.isVisible(0,0) && game.isVisible(4,0) &&
			game.isVisible(0,4) && game.isVisible(3,2) && !game.isVisible(3,4) &&
			!game.isVisible(4,3)){
        	System.out.println("Yay 6");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 | | | | | |
        //1 | | |1|2|2|
        //2 | | |1|?|F|
        //3 | | |2|3|?|
        //4 | | |1|?|?|
        
        //open all none-mine cells without any explosion solve the game!
        if (game.clickAt(4,4) == 1 && game.clickAt(3,4) == 3 && 
			game.getStatus().equals("SOLVED")){
        	System.out.println("Yay 7");
        }
        //System.out.println(game);
        //expected board:
        //- |0|1|2|3|4|
        //0 | | | | | |
        //1 | | |1|2|2|
        //2 | | |1|?|F|
        //3 | | |2|3|3|
        //4 | | |1|?|1|
    } 

}