package uk.ac.cam.km687.oop.tick4;
import java.io.*;
import java.util.*;

public class ArrayWorld extends World implements Cloneable {

  private boolean[][] mWorld;
  private boolean[] mDeadRow;

  public ArrayWorld(Pattern serial) throws PatternFormatException {
    super(serial);
    // TODO: initialise mWorld
    mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];
    mDeadRow = new boolean[getPattern().getWidth()];
    getPattern().initialise(this);
    /*for(int row =0; row < getPattern().getWidth(); row++) {
        if (!Arrays.asList(mWorld[row]).contains(true)) {
        //dead
        mWorld[row] = mDeadRow;
      }
    }*/
    //Set all empty rows to reference mDeadRow
		rowScan:
		for (int row = 0; row < mWorld.length; row++) {
			for (boolean cell : mWorld[row]) {
				if (cell) continue rowScan;
			}
			mWorld[row] = mDeadRow;
		}
  }

  public ArrayWorld(ArrayWorld toCopy) {
    super(toCopy);
    this.mDeadRow = toCopy.mDeadRow;

    //deep copy the array
    mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];
    //dead cells ref mDeadRow
    for (int row = 0; row < getPattern().getHeight(); row++) {
      if (toCopy.mWorld[row] == mDeadRow) {
        mWorld[row] = mDeadRow;
      } else {
				for (int col = 0; col < getPattern().getWidth(); col++) {
					mWorld[row][col] = toCopy.getCell(col, row);
				}
	  }
	}
}

  @Override
	public Object clone() throws CloneNotSupportedException {
    ArrayWorld cloned = (ArrayWorld) super.clone();
    //deep copy of the array
    boolean[][] arrCopy = new boolean[getPattern().getHeight()][getPattern().getWidth()];
    for (int row = 0; row < getPattern().getHeight(); row++) {
        for (int col = 0; col < getPattern().getWidth(); col++) {
            arrCopy[row][col] = this.getCell(col, row);
          }
    }

    cloned.mWorld = arrCopy;
    return cloned;
	}

// TODO: fill in the inherited formerly-abstract methods
  protected void nextGenerationImpl() {
    boolean[][] nextGeneration = new boolean[getPattern().getHeight()][getPattern().getWidth()];

    for(int row = 0; row < getPattern().getHeight(); row++) {
      for(int col = 0; col < getPattern().getWidth(); col++) {
        boolean nextCell = computeCell(col, row);
        nextGeneration[row][col] = nextCell;
      }
    }
    mWorld = nextGeneration;
  }

  public boolean getCell(int col, int row) {
    if (row < 0 || row >= getPattern().getHeight()) return false;
    if (col < 0 || col >= getPattern().getWidth()) return false;

    return mWorld[row][col];
  }

  public void setCell(int col, int row, boolean val) {
    if( (col >=0 && col < getPattern().getWidth()) && (row >= 0 && row < getPattern().getHeight())) {
      mWorld[row][col] = val;
    }
  }
  /*
  public static void main(String args[]) throws IOException {
    ArrayWorld pl = new ArrayWorld(args[0]);
    pl.play();
  }*/

}
