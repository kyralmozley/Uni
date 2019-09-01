package uk.ac.cam.km687.oop.tick4;
import java.io.*;
import java.util.*;

public class ArrayWorld extends World implements Cloneable {

  private boolean[][] mWorld;
  private boolean[] mDeadRow;

  public ArrayWorld(String serial) throws PatternFormatException   {
    super(serial);
    // TODO: initialise mWorld
    mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];
    mDeadRow = new boolean[getPattern().getWidth()];
    getPattern().initialise(this);

    rowScan:
    for (int row = 0; row < mWorld.length; row++) {
      for(boolean cell : mWorld[row]) {
        if (cell) continue rowScan;
      }
      mWorld[row] = mDeadRow;
    }
  }
  public ArrayWorld(Pattern p) throws PatternFormatException {
    super(p);
    // TODO: initialise mWorld
    mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];
    mDeadRow = new boolean[getPattern().getWidth()];
    getPattern().initialise(this);

    rowScan:
    for (int row = 0; row < mWorld.length; row++) {
      for(boolean cell : mWorld[row]) {
        if (cell) continue rowScan;
      }
      mWorld[row] = mDeadRow;
    }
  }

  public ArrayWorld(ArrayWorld toCopy) {
    super(toCopy);
    this.mDeadRow = toCopy.mDeadRow;

    //deep copy Array
    mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];

    for (int row = 0; row < getPattern().getHeight(); row++) {
          //set dead cells to ref mDeadRow
      if (toCopy.mWorld[row] == mDeadRow) {
        mWorld[row] = mDeadRow;
      } else {
        //copy columns iteratively
        for(int col = 0; col < getPattern().getWidth(); col++) {
          mWorld[row][col] = toCopy.getCell(col, row);
        }
      }
    }
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    ArrayWorld cloned = (ArrayWorld) super.clone();
    //deep copy Array
    boolean[][] arrCopy = new boolean[getPattern().getHeight()][getPattern().getWidth()];
    for(int row = 0; row < getPattern().getHeight(); row++) {
      for(int col = 0; col < getPattern().getWidth(); col++) {
        arrCopy[row][col] = this.getCell(col, row);
      }
    }
    rowScan:
    for (int row = 0; row < arrCopy.length; row++) {
      for(boolean cell : arrCopy[row]) {
        if (cell) continue rowScan;
      }
      arrCopy[row] = mDeadRow;
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
