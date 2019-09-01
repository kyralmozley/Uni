package uk.ac.cam.km687.oop.tick3;
public class Pattern implements Comparable<Pattern> {

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


    public Pattern(String format) throws PatternFormatException {
       //TODO: initialise all fields of this class using contents of
       //      'format' to determine the correct values (this code
       //      is similar tothat you used in the new ArrayLife constructor
        String[] inputs = format.split(":");

        if (inputs.length == 0) {
          throw new PatternFormatException("Please specify a pattern.");
        }
        if (inputs.length != 7)  {
          throw new PatternFormatException("Invalid pattern format: Incorrect number of fields in pattern (found "+inputs.length+").");
        }

        mName = inputs[0];
        mAuthor = inputs[1];

        try {mWidth = Integer.parseInt(inputs[2]);}
		    catch (NumberFormatException e) {
			  throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('"+inputs[2]+"' given).");}

		    try {mHeight = Integer.parseInt(inputs[3]);}
		    catch (NumberFormatException e) {
			  throw new PatternFormatException("Invalid pattern format: Could not interpret the height field as a number ('"+inputs[3]+"' given).");}

		    try {mStartCol = Integer.parseInt(inputs[4]);}
		    catch (NumberFormatException e) {
		    throw new PatternFormatException("Invalid pattern format: Could not interpret the startX field as a number ('"+inputs[4]+"' given).");}

		    try {mStartRow = Integer.parseInt(inputs[5]);}
		    catch (NumberFormatException e) {
			  throw new PatternFormatException("Invalid pattern format: Could not interpret the startY field as a number ('"+inputs[5]+"' given).");}

		    for (int i = 0; i < inputs[6].length(); i++) {
			       if (inputs[6].charAt(i) != '0' && inputs[6].charAt(i) != '1' && inputs[6].charAt(i) != ' ') {
				           throw new PatternFormatException("Invalid pattern format: Malformed pattern '"+inputs[6]+"'.");
			       }
		    }

        mCells = inputs[6];
    }

    public void initialise(World world) throws PatternFormatException {
       //TODO: update the values in the 2D array representing the state of
       //      'world' as expressed by the contents of the field 'mCells'.

        String[] cellsArray = mCells.split(" ");
        for(int i=0; i< cellsArray.length; i++) {
        	char[] cellrow = cellsArray[i].toCharArray();
        	for(int j=0; j<cellrow.length; j++) {
//        		world[i+mStartRow][j+mStartCol] = (cellrow[j] == '1');
            if (cellrow[j] != '0' && cellrow[j] != '1') {
			           throw new PatternFormatException("Invalid initialisation character: "+cellrow[j]);
			      }
            world.setCell(mStartCol+j, mStartRow+i, cellrow[j] == '1');
        	}
        }

    }

    @Override

    public int compareTo(Pattern o) {
      // TODO sort by pattern name only
      return getName().compareTo(o.getName());
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
