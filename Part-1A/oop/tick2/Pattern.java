package uk.ac.cam.km687.oop.tick2;

public class Pattern {

    private String mName;
    private String mAuthor;
    private int mWidth;
    private int mHeight;
    private int mStartCol;
    private int mStartRow;
    private String mCells;
    //World mWorld = new boolean[mHeight][mWidth];
    //TODO: write public 'get' methods for ALL of the fields above;
    //      for instance 'getName' should be written as:
    public String getName() {
       return mName;
    }
    public String getAuthor() {
    	return mAuthor;
    }
    public int getWidth() {
    	return mWidth;
    }
    public int getHeight() {
    	return mHeight;
    }
    public int getStartCol() {
    	return mStartCol;
    }
    public int getStartRow() {
    	return mStartRow;
    }
    public String getCells() {
    	return mCells;
    }


    public Pattern(String format) {
       //TODO: initialise all fields of this class using contents of
       //      'format' to determine the correct values (this code
       //      is similar tothat you used in the new ArrayLife constructor
        String[] inputs = format.split(":");
        mName = inputs[0];
        mAuthor = inputs[1];
        mWidth = Integer.parseInt(inputs[2]);
        mHeight = Integer.parseInt(inputs[3]);
		mStartCol = Integer.parseInt(inputs[4]);
		mStartRow = Integer.parseInt(inputs[5]);
		mCells = inputs[6];

    }

    public void initialise(World world) {
       //TODO: update the values in the 2D array representing the state of
       //      'world' as expressed by the contents of the field 'mCells'.

        String[] cellsArray = mCells.split(" ");
        for(int i=0; i< cellsArray.length; i++) {
        	char[] cellrow = cellsArray[i].toCharArray();
        	for(int j=0; j<cellrow.length; j++) {
//        		world[i+mStartRow][j+mStartCol] = (cellrow[j] == '1');
            world.setCell(mStartCol+j, mStartRow+i, cellrow[j] == '1');
        	}
        }

    }

    public static void main(String[] args) throws Exception {
  		Pattern world = new Pattern(args[0]);
  		System.out.println("Name: " + world.getName());
  		System.out.println("Author: " + world.getAuthor());
  		System.out.println("Width: " + world.getWidth());
  		System.out.println("Height: " + world.getHeight());
  		System.out.println("StartCol: " + world.getStartCol());
  		System.out.println("StartRow: " + world.getStartRow());
  		System.out.println("Pattern: " + world.getCells());
	}
}
