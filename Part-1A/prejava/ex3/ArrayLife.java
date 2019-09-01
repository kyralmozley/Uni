package uk.ac.cam.km687.prejava.ex3;

public class ArrayLife {
	
	public static boolean getCell(boolean[][] world, int col, int row) {
		if (row < 0 || row > world.length -1) return false;
		if (col < 0 || col > world[row].length -1) return false;
		
		return world[row][col];
	}
	
	public static void setCell(boolean[][] world, int col, int row, boolean value) {
		if ( (col >= 0 && col < world[row].length) && (row >= 0 && row < world.length)) {
			world[row][col] = value;
		}
	}
	
	public static int countNeighbours(boolean[][] world, int col, int row) {
		int count = 0;
		for (int n=-1; n<2; n++) {
			for (int m=-1; m<2; m++) {
				if (getCell(world, col+m, row+n)) count ++;
			}
		}
		if (getCell(world, col, row)) count--;
		return count;
	}
	
	public static boolean computeCell(boolean[][] world, int col, int row) {
		boolean liveCell = getCell(world, col, row);
		int neighbours = countNeighbours(world, col, row);
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
	
	public static boolean[][] nextGeneration(boolean[][] world) {
		boolean[][] newworld = new boolean[world.length][];
		for(int i=0; i<world.length; i++) {
			newworld[i] = new boolean[world[i].length];
			for(int j=0; j<world[i].length; j++) {
				setCell(newworld, j, i, computeCell(world, j, i));
			}
		}
		return newworld;
	}	
		
	public static void print(boolean[][] world) {
		System.out.println("-");
		for (int row = 0; row < world.length; row++) {
			for (int col = 0; col < world.length; col++) {
				System.out.print(getCell(world, col, row) ? "#" : "_");
			}
			System.out.println();
		}
	}

	public static void play(boolean[][] world) throws java.io.IOException {
   		int userResponse = 0;
   		while (userResponse != 'q') {
      		print(world);
      		userResponse = System.in.read();
      		world = nextGeneration(world);
   		}
	}
	
	public static boolean getFromPackedLong(long packed, int position) {
        return ((packed >>> position) & 1) == 1;
	}

	public static void main(String[] args) throws java.io.IOException {
  		int size = Integer.parseInt(args[0]);
   		long initial = Long.decode(args[1]);
   		boolean[][] world = new boolean[size][size];
   		//place the long representation of the game board in the centre of "world"
   		for(int i = 0; i < 8; i++) {
      		for(int j = 0; j < 8; j++) {
         		world[i+size/2-4][j+size/2-4] = getFromPackedLong(initial,i*8+j);
      		}
   		}
   		play(world);
	}
}