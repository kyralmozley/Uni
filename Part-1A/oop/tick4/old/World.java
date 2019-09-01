package uk.ac.cam.km687.oop.tick4;
import java.io.*;

public abstract class World implements Cloneable {
    private int mGeneration;
    private Pattern mPattern;

    public World(Pattern pattern) throws PatternFormatException {
      mPattern = pattern;
      this.mGeneration = 0;
    }

    public World(World toCopy) {
      this.mPattern = toCopy.getPattern();
      this.mGeneration = toCopy.getGenerationCount();
    }

    @Override
	  public Object clone() throws CloneNotSupportedException {
		    return super.clone();
	  }

    public int getWidth() {
      return mPattern.getWidth();
    }

    public int getHeight() {
      return mPattern.getHeight();
    }

    public int getGenerationCount() {
      return mGeneration;
    }

    protected void incrementGenerationCount() {
        mGeneration++;
    }

    protected Pattern getPattern() {
      return mPattern;
    }



    public abstract boolean getCell(int col, int row);

    public abstract void setCell(int col, int row, boolean val);

    protected int countNeighbours(int x, int y) {
      int count = 0;
     	for (int n=-1; n<2; n++) {
     		for (int m=-1; m<2; m++) {
     			if (getCell(x+m, y+n)) count ++;
     		}
     	}
     	if (getCell(x, y)) count--;

      return count;
    }

    protected boolean computeCell(int x, int y) {
      boolean liveCell = getCell(x, y);
  		int neighbours = countNeighbours(x, y);
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
        nextGenerationImpl();
        mGeneration++;
      }

    protected abstract void nextGenerationImpl();


}
