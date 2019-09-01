package uk.ac.cam.km687.oop.tick1;

public class ArrayLife {

    private int mWidth;
    private int mHeight;
    private boolean[][] mWorld;
    private Pattern mPattern;

    public ArrayLife(String format)  {
        // TODO: initialise mPattern, mWorld, mHeight and mWidth
		mPattern = new Pattern(format);
		
		mHeight = mPattern.getHeight();
		mWidth = mPattern.getWidth();
		mWorld = new boolean[mHeight][mWidth];
		    	
    	mPattern.initialise(mWorld);
    }
    


	/*
	public ArrayLife(int size, long initial) {
		
		mHeight=size;
		mWidth=size;
		mWorld=new boolean[mHeight][mWidth];
	
	// TODO: initialise mWorld using loops, initial and getPackedLong() 
		for (int i=0; i < mHeight; i++) {
			for (int j=0; j < mWidth; j++) {
				mWorld[i][j] = getFromPackedLong(initial, mWidth*i + j);
			}
		}
	}
	*/
	/*
	public ArrayLife(String format) {
    //TODO: Determine the dimensions of the game board
    //format: NAME:AUTHOR:WIDTH:HEIGHT:STARTUPPERCOL:STARTUPPERROW:CELLS
    	String[] inputs = format.split(":");
        mWidth = Integer.parseInt(inputs[2]);
        mHeight = Integer.parseInt(inputs[3]);

    	mWorld = new boolean[mHeight][mWidth];
    	
    	for (int i=0; i<mWidth; i++) {
    		for(int j=0; j<mHeight; j++) {
    			mWorld[j][i] = false;
    		}
    	}
    			
		int startcol = Integer.parseInt(inputs[4]);
		int startrow = Integer.parseInt(inputs[5]);
		String[] cells = inputs[6].split(" ");
		
        //TODO: Using loops, update the appropriate cells of mWorld
        //      to 'true'
        for (int i = 0; i<cells.length; i++) {
        	char[] cellrow = cells[i].toCharArray();
        	for (int j=0; j<cellrow.length; j++) {
        		if (cellrow[j] == '1') {
                    setCell((j+startcol), (i+startrow), true);
                }  
        		
        	}
        }
        
	}*/

	
	
	public boolean getCell(int col, int row) {
   		if (row < 0 || row >= mHeight) return false;
   		if (col < 0 || col >= mWidth) return false;

	   return mWorld[row][col];
	}
	
	public void setCell(int col, int row, boolean value) {
		if ( (col >= 0 && col < mWidth) && (row >= 0 && row < mHeight)) {
			mWorld[row][col] = value;
		}
	}
	
	private int countNeighbours(int col, int row) {
		int count = 0;
		for (int n=-1; n<2; n++) {
			for (int m=-1; m<2; m++) {
				if (getCell(col+m, row+n)) count ++;
			}
		}
		if (getCell(col, row)) count--;
		return count;
	}
	
	private boolean computeCell(int col, int row) {
		boolean liveCell = getCell(col, row);
		int neighbours = countNeighbours(col, row);
		boolean nextCell = false;
		
		if (neighbours < 2) {
			nextCell = false;
		}
		if (liveCell) {
			if (neighbours == 2 || neighbours == 3) {
				nextCell = true;
			}
			if (neighbours > 3) {
				nextCell = false;
			}
		}
		if (!liveCell && neighbours == 3) {
			nextCell = true;
		}
		return nextCell;	
	}
    public void nextGeneration() {
        boolean[][] nextGeneration = new boolean[mWorld.length][];
        for (int y = 0; y < mWorld.length; ++y) {
            nextGeneration[y] = new boolean[mWorld[y].length];
            for (int x = 0; x < mWorld[y].length; ++x) {
                boolean nextCell = computeCell(x, y);
                nextGeneration[y][x]=nextCell;
            }
        }
        mWorld = nextGeneration;
    }
		
	public void print() {
		System.out.println("-");
		for (int row = 0; row < mHeight; row++) {
			for (int col = 0; col < mWidth; col++) {
				System.out.print(getCell(col, row) ? "#" : "_");
			}
			System.out.println();
		}
	}

	public void play() throws java.io.IOException {
   		int userResponse = 0;
   		while (userResponse != 'q') {
      		print();
      		userResponse = System.in.read();
      		nextGeneration();
   		}
	}
	
	/*
	public static void main(String[] args) throws Exception {
  		int size = Integer.parseInt(args[0]);
  		long initial = Long.decode(args[1]);

  		ArrayLife al = new ArrayLife(size, initial);
  		al.play();
	}*/
	public static void main(String[] args) throws Exception {
  		ArrayLife al = new ArrayLife(args[0]);
  		al.play();
	}
	

}