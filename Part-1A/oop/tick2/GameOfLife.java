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

            if (args.length == 1) {
              w = new ArrayWorld(args[0]);
            } else if(args[0].equals("--array")) {
              w = new ArrayWorld(args[1]);
            } else if(args[0].equals("--packed")) {
              w = new PackedWorld(args[1]);
            } else {
              throw new IllegalArgumentException("Please provide packed or array");
            }

            GameOfLife gol = new GameOfLife(w);
            gol.play();
    }


}
