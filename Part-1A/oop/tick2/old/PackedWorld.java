package uk.ac.cam.km687.oop.tick2;
import uk.ac.cam.km687.oop.tick2.World;
import uk.ac.cam.km687.oop.tick2.PatternLengthException;
import uk.ac.cam.km687.oop.tick2.Pattern;
import java.io.*;

public class PackedWorld extends World {

  private long mWorld;

  public PackedWorld(String serial) throws PatternLengthException {
    super(serial);

    if(getPattern().getWidth()*getPattern().getHeight() > 64) {
      throw new PatternLengthException("Packedworld has a max size of 64 cells");
    }

    getPattern().initialise(this);
  }

  protected void nextGenerationImpl() {
    long next = 0L;
    for(int row = 0; row < getPattern().getWidth(); row++) {
      for(int col = 0; col < getPattern().getHeight(); col++) {
        if (computeCell(col, row)) {
          next = next | (1L << row*getPattern().getWidth() + col);
        } else {
          next = next & ~(1L << row*getPattern().getWidth() + col);
        }
      }
    }
    mWorld = next;
    incrementGenerationCount();
  }

  public boolean getCell(int col, int row) {
    if (row < 0 || row >= getPattern().getHeight()) return false;
    if (col < 0 || col >= getPattern().getWidth()) return false;

    return get(row*getPattern().getWidth()+col);
  }

  public void setCell(int col, int row, boolean value) {
    if (row < 0 || row >= getPattern().getHeight()) return ;
    if (col < 0 || col >= getPattern().getWidth()) return ;

    set(row*getPattern().getWidth()+col, value);
  }

  public boolean get(int position) {
    long check = (mWorld >> (position)&1);
    return (check == 1);
  }

  public void set(int position, boolean value) {
    if (value) {
      mWorld = mWorld | (1L << (position));
    } else {
      mWorld = mWorld & ~(1L << (position));
    }
  }
/*
  public static void main(String args[]) throws IOException, PatternLengthException {
         PackedWorld pl = new PackedWorld(args[0]);
         pl.play();


  }*/
}
