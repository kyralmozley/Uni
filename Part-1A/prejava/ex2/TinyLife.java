package uk.ac.cam.km687.prejava.ex2;

public class TinyLife {
	
	public static boolean getCell(long world, int col, int row) {
		if ( (col < 0 || col > 7) || (row < 0 || row > 7)) {
			return false;
		} else {
			int position = row*8+col;
			return PackedLong.get(world, position);
		}
	}
	
	public static long setCell(long world, int col, int row, boolean newval) {
		if ( (col < 0 || col > 7) || (row < 0 || row > 7)) {
			return 0L;
		} else {
			int position = row*8+col;
			return PackedLong.set(world, position, newval);
		}
	}
	
	public static int countNeighbours(long world, int col, int row) {
		int count = 0;
		for (int n=-1; n<2; n++) {
			for (int m=-1; m<2; m++) {
				if (getCell(world, col+m, row+n)) count ++;
			}
		}
		if (getCell(world, col, row)) count--;
		return count;
	}
	
	public static boolean computeCell(long world, int col, int row) {
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
	
	public static long nextGeneration(long world) {
		long newworld = 0L;
		for (int n=0; n<8; n++) {
			for (int m=0; m<8; m++) {
				newworld = setCell(newworld, n, m, computeCell(world, n, m));	
			} 
		}	
		return newworld;
	}	
		
	public static void print(long world) {
		System.out.println("-");
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				System.out.print(getCell(world, col, row) ? "#" : "_");
			}
			System.out.println();
		}
	}

	public static void play(long world) throws java.io.IOException {
   		int userResponse = 0;
   		while (userResponse != 'q') {
      		print(world);
      		userResponse = System.in.read();
      		world = nextGeneration(world);
   		}
	}
	
	public static void main(String[] args) throws java.io.IOException {
   		play(Long.decode(args[0]));
	}
}