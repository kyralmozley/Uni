package uk.ac.cam.km687.oop.tick2;

import java.io.*;


public class GameOfLife {

    private World mWorld;

    public GameOfLife(World w) {
        mWorld = w;
    }

    public void print() {
  		System.out.println("-" + mWorld.getGenerationCount());
  		for (int row = 0; row < mWorld.getHeight(); row++) {
  			for (int col = 0; col < mWorld.getWidth(); col++) {
  				System.out.print(mWorld.getCell(col, row) ? "#" : "_");
  			}
  			System.out.println();
  		}
  	}

    public void play() throws java.io.IOException  {
      int userResponse = 0;
      while (userResponse != 'q') {
          print();
          userResponse = System.in.read();
          mWorld.nextGeneration();
      }
    }

    public static void main(String args[]) throws IOException {

      World w=null;

      // TODO: initialise w as an ArrayWorld or a PackedWorld
      // based on the command line input
      w = new ArrayWorld(args[0]);

      GameOfLife gol = new GameOfLife(w);
      gol.play();

    }


}
