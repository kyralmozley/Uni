package uk.ac.cam.km687.oop.tick2;
import uk.ac.cam.km687.oop.tick2.GameOfLife;

public abstract class World extends GameOfLife {

  private int mGeneration; //how many generations
  Pattern mPattern;

  public World(String pattern) {
    this.mPattern = new Pattern(pattern);
    this.mGeneration = 0;
  }
  public World() {
    
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

  public void nextGeneration() {
    nextGenerationImpl();
  }

  protected abstract void nextGenerationImpl();

  public abstract boolean getCell(int c, int r);

  public abstract void setCell(int c, int r, boolean value);

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

}
