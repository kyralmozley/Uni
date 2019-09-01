package uk.ac.cam.km687.oop.tick5;

import java.awt.Color;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private World mWorld = null;

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        // Paint the background white
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
/*
       // Sample drawing statements
        g.setColor(Color.BLACK);
        g.drawRect(200, 200, 30, 30);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(140, 140, 30, 30);
        g.fillRect(260, 140, 30, 30);
        g.setColor(Color.BLACK);
        g.drawLine(150, 300, 280, 300);
        g.drawString("@@@", 135,120);
        g.drawString("@@@", 255,120);
*/
        //drawing the World
        if (mWorld != null) {
          int w = this.getWidth()/mWorld.getWidth();
          int h = this.getHeight()/mWorld.getHeight();
          int size = (w < h) ? w : h;

          for(int col=0; col < mWorld.getWidth(); col++) {
            for(int row = mWorld.getHeight()-1; row >=0; row--) {
              //border outline
              g.setColor(Color.GRAY);
              g.drawRect(col*(size+1), row*(size+1), size, size);

              //cells
              g.setColor((mWorld.getCell(col, row)) ? Color.BLACK : Color.WHITE);
              g.fillRect(col*(size+1), row*(size+1), size, size);
            }
          }
          g.setColor(Color.BLACK);
          g.drawString("Generation: " + mWorld.getGenerationCount(), 20, this.getHeight()-20);

    }}

    public void display(World w) throws PatternFormatException  {

        mWorld = w;
        repaint();
      }

}
