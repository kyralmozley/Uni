package uk.ac.cam.km687.oop.tick5;

import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private World mWorld = null;

    /*
    GamePanel() {
      try {
        ArrayWorld aw = new ArrayWorld("glider:richard k. guy:8:8:1:1:001 101 011 ");
        mWorld = aw;
        repaint();
      } catch (PatternFormatException e) { throw new RuntimeException();}
    }
*/
    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        /* // Sample drawing statements
        g.setColor(Color.BLACK);
        g.drawRect(200, 200, 30, 30);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(140, 140, 30, 30);
        g.fillRect(260, 140, 30, 30);
        g.setColor(Color.BLACK);
        g.drawLine(150, 300, 280, 300);
        g.drawString("@@@", 135,120);
        g.drawString("@@@", 255,120);*/

        if (mWorld == null) return;

        //getting perfect squares
        int x = this.getWidth()/ mWorld.getWidth()  ;
        int y = this.getHeight()-20  / mWorld.getHeight();
        int size = (x < y) ? x :y;

        for(int row =mWorld.getHeight()-1; row >= 0; row--) {
          for(int col=0; col < mWorld.getWidth(); col++) {
            //Border
            g.setColor(Color.GRAY);
            g.drawRect(col*(size+1), row*(size+1), size, size);

            g.setColor(mWorld.getCell(col, row) ? Color.BLACK : Color.WHITE);
            g.fillRect(col*(size+1), row*(size+1), size, size);
          }
        }
        g.setColor(Color.BLACK);
        g.drawString("Generation:" + mWorld.getGenerationCount(), 20, this.getHeight()-20);
    }

    public void display(World w) {
        mWorld = w;
        repaint();
    }
}
