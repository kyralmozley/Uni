gpackage uk.ac.cam.km687.oop.tick2;
import uk.ac.cam.km687.oop.tick2.World;
import java.io.*;

public class ArrayWorld extends World {

  private boolean[][] mWorld;

  public ArrayWorld(String serial)  {
    super(serial);
    // TODO: initialise mWorld
    mWorld = new boolean[getPattern().getHeight()][getPattern().getWidth()];

    getPattern().initialise(this);
  }

  // TODO: fill in the inherited formerly-abstract methods

  protected void nextGenerationImpl() {
    boolean[][] nextGeneration = new boolean[getPattern().getHeight()][getPattern().getWidth()];

  		for (int row = 0; row < getPattern().getHeight(); row++) {
  			for (int col = 0; col < getPattern().getWidth(); col++) {
  				nextGeneration[row][col] = computeCell(col,row);
  			}
  		}
  		mWorld = nextGeneration;
      incrementGenerationCount();
  }

  public boolean getCell(int col, int row) {
    if (row < 0 || row >= getPattern().getHeight()) return false;
    if (col < 0 || col >= getPattern().getWidth()) return false;

   return mWorld[row][col];
  }

  public void setCell(int col, int row, boolean value) {
    if ( (col >= 0 && col < getPattern().getWidth()) && (row >= 0 && row < getPattern().getHeight())) {
			mWorld[row][col] = value;
		}
  }
/*
  public static void main(String args[]) throws IOException {
       ArrayWorld pl = new ArrayWorld(args[0]);
       pl.play();
}*/
}
