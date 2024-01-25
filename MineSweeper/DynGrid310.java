/**
 * A generic class that creates a 2d dynamic array.
 * @param <T> Generic object type.
 */
public class DynGrid310<T> {

	//underlying 2-d array for storage -- you MUST use this for credit!
	//Do NOT change the name or type
	/**
	 * Underlying 2-d array for storage.
	 */
	private DynArr310<DynArr310<T>> storage;	

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
	
	/**
	 * Constructor, creates and empty grid.
	 */
	public DynGrid310(){
		// constructor
		// create an empty grid (no content)		
		// only use the parameterless constructor of DynArr310 to initialize 		
		storage = new DynArr310<DynArr310<T>>();
	}

	/**
	 * Report number of rows with contents in the grid.
	 * @return Number of rows.
	 */
	public int getNumRow() {
		// report number of rows with contents in the grid
		// Note: this might be different from the max number of rows 
		// 		 of the underlying storage.
		// O(1) 
		
		return storage.size();
		
	}
	
	/**
	 * Report number of columns with contents in the grid.
	 * @return Number of columns.
	 */
	public int getNumCol() { 
		// report number of columns with contents in the grid
		// Note: similarly, this might be different from the max number of columns 
		// 		 of the underlying storage.
		// O(1) 

		if(storage.size() == 0)
			return 0;
		return storage.get(0).size();
	}
	
	/**
	* Checks whether (row,col) corresponds to a cell with content.
	* @param row Row of the cell.
	* @param col Column of the cell.
	* @return Returns true if valid cell, false otherwise.
	*/
	public boolean isValidCell(int row, int col){
		// check whether (row,col) corresponds to a cell with content
		// return true if yes, false otherwise
		//O(1)
		if(row >= getNumRow() || col >= getNumCol() || row < 0 || col < 0)
			return false;
		return true;
    }
    
	/**
	 * Reports cell value at (row, col). Throws IndexOutOfBoundsException if any index is not valid.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @return Returns the cell at (row, col).
	 */
	public T get(int row, int col){
		// report cell value at (row, col)

		// - Throw IndexOutOfBoundsException if any index is not valid
		// - Use this code to produce the correct error message for
		// the exception (do not use a different message):
		//	  "Index("+row+","+col+") out of bounds!"
		if(!isValidCell(row, col))
			throw new IndexOutOfBoundsException("Index("+row+","+col+") out of bounds!");

		// O(1)
		return storage.get(row).get(col);
	}
	
	/**
	 * Changes cell value at (row, col) to be value, and returns the old cell value.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @param value Value to set the cell to, can't be null.
	 * @return Returns the old value of the cell.
	 */
	public T set(int row, int col, T value){
		// change cell value at (row, col) to be value, and return the old cell value
		
		// Use the exception (and error message) described in set()
		// for invalid indicies.
		
		// For valid indicies, if value is null, throw IllegalArgumentException. 
		// - Use this _exact_ error message for the exception 
		//  (quotes are not part of the message):
		//    "Null values not accepted!"
		if(!isValidCell(row, col))
			throw new IndexOutOfBoundsException("Index("+row+","+col+") out of bounds!");
		if(value == null)
			throw new IllegalArgumentException("Null values not accepted!");

		// O(1)
		T oldValue = get(row, col);
		storage.get(row).set(col, value);
		
		return oldValue;
	}

	/**
	 * Inserts newRow into the grid at index.
	 * @param index Index to insert to.
	 * @param newRow Row to insert.
	 * @return Returns false if invalid index, newRow is null or empty, the number of items in newRow does not match existing rows, otherwise true.
	 */
	public boolean addRow(int index, DynArr310<T> newRow){
		// insert newRow into the grid at index, shifting rows if needed
		// a new row can be appended 
		
		// return false if newRow can not be added correctly, e.g.
		// 	- invalid index
		//  - newRow is null or empty
		//	- the number of items in newRow does not match existing rows
		//
		// return true otherwise
		// 

		// O(R) 
		if(index < 0 || index > storage.size() || newRow == null || newRow.size() == 0 || (storage.size() > 0 && newRow.size() != storage.get(0).size()))
			return false;

		storage.insert(index, newRow);

		return true;
		
	}
	
	/**
	 * Inserts newCol as a new column into the grid at index.
	 * @param index Index to insert to.
	 * @param newCol Coulmn to insert.
	 * @return Returns false if invalid index, newCol is null or empty, the number of items in newCol does not match existing columns.
	 */
	public boolean addCol(int index, DynArr310<T> newCol){
		// insert newCol as a new column into the grid at index, shifting cols if needed
		// a new column can be appended
		
		// return false if newCol can be added correctly, e.g.
		// 	- invalid index
		//  - newCol is null or empty
		//	- the number of items in newCol does not match existing columns
		//
		// return true otherwise

		// O(CR) where R is the number of rows and C is the number of columns of the grid
		if(index < 0 || index > getNumCol() || newCol == null || newCol.size() == 0 || (storage.size() != 0 && newCol.size() != storage.size()))
			return false;
		
		if(storage.size() == 0)
		{
			for(int i=0; i<newCol.size(); i++)
			{
				DynArr310<T> row = new DynArr310<>();
				row.add(newCol.get(i));
				storage.add(row);
			}
			return true;
		}
		
		for(int i=0; i<storage.size(); i++)
			storage.get(i).insert(index, newCol.get(i));
		
		return true;

	}
	
	/**
	 * Removes and returns a row at index, shifts rows as to remove the gap.
	 * @param index Index to remove.
	 * @return Retuns removed row, if invalid index returns null.
	 */
	public DynArr310<T> removeRow(int index){
		// remove and return a row at index, shift rows as needed to remove the gap		
		// return null for an invalid index
		if(index >= storage.size() || index < 0)
			return null;

		// O(R) where R is the number of rows of the grid
		DynArr310<T> removedRow = storage.get(index);
		storage.remove(index);

		return removedRow;
	}

	/**
	 * Removes and returns a column at index, shifts cols to remove the gap.
	 * @param index Index to remove.
	 * @return Returns removed column, if invalid index returns null.
	 */
	public DynArr310<T> removeCol(int index){
		// remove and return a column at index, shift cols as needed to remove the gap
		// return null for an invalid index
		if(index >= getNumCol() || index < 0)
			return null;
		// O(RC) where R is the number of rows and C is the number of columns 

		DynArr310<T> removedCol = new DynArr310<T>();
		for(int i=0; i<storage.size(); i++)
		{
			removedCol.add(storage.get(i).get(index));
			storage.get(i).remove(index);
		}
		if(getNumCol() == 0)
		{
			for(int i=0,size=storage.size(); i<size; i++)
				storage.remove(0);
		}

		return removedCol;
	}


	

	//******************************************************
	//*******     BELOW THIS LINE IS PROVIDED code   *******
	//*******             Do NOT edit code!          *******
	//*******		   Remember to add JavaDoc		 *******
	//******************************************************
	
	/**
	 * Displays the grid in string format.
	 * @return A string representation of the grid.
	 */
	@Override
	public String toString(){
		if(getNumRow() == 0 || getNumCol() == 0 ){ return "empty board"; }
    	StringBuilder sb = new StringBuilder();
    	for(int i=0; i<getNumRow(); i++){
            sb.append("|");
    		for (int j=0;j<getNumCol(); j++){
      			sb.append(get(i,j).toString());
      		    sb.append("|");
      		}
      		sb.append("\n");
    	}
    	return sb.toString().trim();

	}

	//******************************************************
	//*******     BELOW THIS LINE IS TESTING CODE    *******
	//*******      Edit it as much as you'd like!    *******
	//*******		Remember to add JavaDoc			 *******
	//******************************************************

	/**
	 * Tests the DynGrid310 class.
	 * @param args String array (not used).
	 */
	public static void main(String[] args){
		//These are _sample_ tests. If you're seeing all the "yays" that's
		//an excellend first step! But it does NOT guarantee your code is 100%
		//working... You may edit this as much as you want, so you can add
		//own tests here, modify these tests, or whatever you need!

		//create a grid of strings
		DynGrid310<String> sgrid = new DynGrid310<>();
		
		//prepare one row to add
		DynArr310<String> srow = new DynArr310<>();
		srow.add("English");
		srow.add("Spanish");
		srow.add("German");
		
		//addRow and checking
		if (sgrid.getNumRow() == 0 && sgrid.getNumCol() == 0 && !sgrid.addRow(1,srow)
			&& sgrid.addRow(0,srow) && sgrid.getNumRow() == 1 && sgrid.getNumCol() == 3){
			System.out.println("Yay 1");
		}
		
		//get, set, isValidCell
		if (sgrid.get(0,0).equals("English") && sgrid.set(0,1,"Espano").equals("Spanish") 
			&& sgrid.get(0,1).equals("Espano") && sgrid.isValidCell(0,0) 
			&& !sgrid.isValidCell(-1,0) && !sgrid.isValidCell(3,2)) {
			System.out.println("Yay 2");
		}

		//a grid of integers
		DynGrid310<Integer> igrid = new DynGrid310<Integer>();
		boolean ok = true;

		//add some rows (and implicitly some columns)
		for (int i=0; i<3; i++){
			DynArr310<Integer> irow = new DynArr310<>();
			irow.add((i+1) * 10);
			irow.add((i+1) * 11);
        
			ok = ok && igrid.addRow(igrid.getNumRow(),irow);
		}
		
		//toString
		//System.out.println(igrid);
		if (ok && igrid.toString().equals("|10|11|\n|20|22|\n|30|33|")){
			System.out.println("Yay 3");		
		}
				
		//prepare a column 
		DynArr310<Integer> icol = new DynArr310<>();
		
		//add two rows
		icol.add(-10);
		icol.add(-20);
		
		//attempt to add, should fail
		ok = igrid.addCol(1,icol);
		
		//expand column to three rows
		icol.add(-30);
		
		//addCol and checking
		if (!ok && !igrid.addCol(1,null) && igrid.addCol(1,icol) && 
			igrid.getNumRow() == 3 && igrid.getNumCol() == 3){
			System.out.println("Yay 4");		
		}
		
		//System.out.println(igrid);
		
		//removeRow
		if (igrid.removeRow(5) == null && 
			igrid.removeRow(1).toString().equals("[20, -20, 22]") && 
			igrid.getNumRow() == 2 && igrid.getNumCol() == 3 ){
			System.out.println("Yay 5");	
		}
		
		//removeCol
		String a = igrid.removeCol(0).toString();
		String b = igrid.removeCol(1).toString();
		String c = igrid.removeCol(0).toString();
		if (a.equals("[10, 30]") && 
			b.equals("[11, 33]") &&
			c.equals("[-10, -30]") &&
			igrid.getNumRow() == 0 && igrid.getNumCol() == 0 ){
			System.out.println("Yay 6");	
		}
		else
		{
			System.out.println(a);
			System.out.println(b);
			System.out.println(c);
			System.out.println(igrid.getNumCol());
			System.out.println(igrid.getNumRow());
		}


		
				
	}
	
}