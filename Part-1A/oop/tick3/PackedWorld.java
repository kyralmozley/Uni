package uk.ac.cam.km687.oop.tick3;
import java.io.*;

public class PackedWorld extends World {
  private long mWorld;

  public PackedWorld(String format) throws PatternFormatException  {
    super(format);
    if (getPattern().getHeight()*getPattern().getWidth() > 64) {
      throw new IllegalArgumentException("Board more than 64 cells");
    }
    getPattern().initialise(this);
  }
  public PackedWorld(Pattern p) throws PatternFormatException  {
    super(p);
    getPattern().initialise(this);
  }

  public boolean getCell(int col, int row) {
    if (row < 0 || row >= getPattern().getHeight()) return false;
    if (col < 0 || col >= getPattern().getWidth()) return false;

    long check = (mWorld >> (row*getPattern().getWidth()+col) & 1);
    return (check ==1);

  }

  public void setCell(int col, int row, boolean val) {
    if (row < 0 || row >= getPattern().getHeight()) return;
    if (col < 0 || col >= getPattern().getWidth()) return;

    if (val) {
      mWorld = mWorld | (1L << (row*getPattern().getWidth() + col));
    } else {
      mWorld = mWorld & ~(1L << (row*getPattern().getWidth()+col));
    }
  }


  public void nextGenerationImpl() {
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

  }

/*
  public static void main(String args[]) throws IOException {
    PackedWorld pl = new PackedWorld("Glider:Richard K. Guy:6:7:1:1:001 101 011");
    pl.play();

  }*/

}
