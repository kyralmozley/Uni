package uk.ac.cam.km687.oop.tick3;
import java.io.*;
import java.net.*;
import java.util.*;

public class GameOfLife {

    private World mWorld;
    private PatternStore mStore;

    public GameOfLife(World w) {
      mWorld = w;
    }

    public GameOfLife(PatternStore p) {
      mStore = p;
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
/*
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
    }*/
    public void play() throws IOException {

        String response="";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please select a pattern to play (l to list:");
        while (!response.equals("q")) {
                response = in.readLine();
                System.out.println(response);
                if (response.equals("f")) {
                        if (mWorld==null) System.out.println("Please select a pattern to play (l to list):");
                        else {
                                mWorld.nextGeneration();
                                print();
                        }
                }
                else if (response.equals("l")) {
                        List<Pattern> names = mStore.getPatternsNameSorted();
                        int i=0;
                        for (Pattern p : names) {
                                System.out.println(i+" "+p.getName()+"  ("+p.getAuthor()+")");
                                i++;
                        }
                }
                else if (response.startsWith("p")) {

                   List<Pattern> names = mStore.getPatternsNameSorted();

                   // TODO: Extract the integer after the p in response
                    int patternNum = Integer.parseInt(response.substring(2));

                   // TODO: Get the associated pattern
                   Pattern p = mStore.getPatternsNameSorted().get(patternNum);

                   // TODO: Initialise mWorld using PackedWorld or ArrayWorld based
                   //       on pattern world size
                   try {
                     if (p.getWidth()*p.getHeight()<64) {
                       mWorld = new PackedWorld(p);
                     } else {mWorld = new ArrayWorld(p);}
                     print();
                   } catch (PatternFormatException e) {
					                throw new IOException("Invalid pattern");
				           }

                   print();
                }

        }
    }

    public static void main(String args[]) throws IOException {

        if (args.length!=1) {
                System.out.println("Usage: java GameOfLife <path/url to store>");
                return;
        }

        try {
                PatternStore ps = new PatternStore(args[0]);
                GameOfLife gol = new GameOfLife(ps);
                gol.play();
        }
        catch (IOException ioe) {
                System.out.println("Failed to load pattern store");
        }


    }


}
