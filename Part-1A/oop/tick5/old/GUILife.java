package uk.ac.cam.km687.oop.tick5;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GUILife extends JFrame implements ListSelectionListener{

//Copy across the mStore, mWorld and mCachedWorld state and the copyWorld method from GameOfLife to GUILife.
  private World mWorld;
  private PatternStore mStore;
  private ArrayList<World> mCachedWorlds = new ArrayList<>();
  private boolean mPlaying = false;
  private Timer mTimer;
  private final int mFrameRate = 500;

  private JButton mPlayButton;
  private JPanel mGamePanel;
  private GamePanel gp;


  public GUILife(PatternStore ps) {
    super("Game of Life");
    mStore=ps;
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1024,768);

    add(createPatternsPanel(),BorderLayout.WEST);
    add(createControlPanel(),BorderLayout.SOUTH);
    add(createGamePanel(),BorderLayout.CENTER);

  }

  private JPanel createGamePanel() {
      GamePanel mGamePanel = new GamePanel();
      addBorder(mGamePanel,"Game Panel");
      return mGamePanel;
  }

  private void addBorder(JComponent component, String title) {
    Border etch = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    Border tb = BorderFactory.createTitledBorder(etch,title);
    component.setBorder(tb);
  }


  private JPanel createPatternsPanel() {
    JPanel patt = new JPanel();
    addBorder(patt,"Patterns");
    // TODO

    JList<Object> list = new JList<>(mStore.getPatternsNameSorted().toArray());
    list.addListSelectionListener(this);
    JScrollPane scroll = new JScrollPane(list);
	  scroll.setViewportView(list);

	  patt.setLayout(new BorderLayout());
	  patt.add(scroll,BorderLayout.CENTER);

    return patt;
  }

  private JPanel createControlPanel()  {
    JPanel ctrl =  new JPanel();
    addBorder(ctrl,"Controls");
    // TODO  to create three JButtons with the labels
    ctrl.setLayout(new GridLayout(1,3));

    JButton back = new JButton("< Back");
    back.addActionListener(e -> {if(mPlaying) runOrPause(); moveBackward();});
    ctrl.add(back,BorderLayout.WEST);

   	mPlayButton = new JButton("Play");
    mPlayButton.addActionListener(e -> runOrPause());
    ctrl.add(mPlayButton,BorderLayout.CENTER);

   	JButton forward = new JButton("Forward >");
    forward.addActionListener(e -> {if(mPlaying) runOrPause(); moveForward();});
   	ctrl.add(forward,BorderLayout.EAST);

    return ctrl;
  }

  private World copyWorld(boolean useCloning) {
    // TODO later
    if (useCloning == false) {
      if (mWorld instanceof ArrayWorld) {
        return new ArrayWorld((ArrayWorld) mWorld);
      }
      if (mWorld instanceof PackedWorld) {
        return new PackedWorld ((PackedWorld) mWorld);
      }
      else {
        throw new RuntimeException ("unsupported world type");
      }
    } else {
      try {
        return (World) mWorld.clone();
      } catch (Exception e) {
        throw new RuntimeException ("unsupported world type");
      }
    } // return null;
  }

  private void moveForward()  {
    if(mWorld != null)
    try{
      if(mWorld.getGenerationCount()+1 < mCachedWorlds.size()) {
        mWorld = mCachedWorlds.get(mWorld.getGenerationCount()+1);
        ((GamePanel) mGamePanel).display(mWorld);
      } else {
        mCachedWorlds.add(mWorld);
        mWorld = this.copyWorld(true);
        mWorld.nextGeneration();
        ((GamePanel) mGamePanel).display(mWorld);
      }
    } catch (PatternFormatException pfe) {
      throw new RuntimeException();
    }

  }

  private void moveBackward()   {
    if (mWorld != null) {
      try {
      if (mWorld.getGenerationCount() != 0) {
        if(!mCachedWorlds.contains(mWorld)) mCachedWorlds.add(mWorld);
        mWorld = mCachedWorlds.get(mWorld.getGenerationCount() -1);
        ((GamePanel) mGamePanel).display(mWorld);
      }
    } catch (PatternFormatException pfe) {
      throw new RuntimeException();
    }
    }
  }

  private void runOrPause()  {
      if (mPlaying) {
          mTimer.cancel();
          mPlaying=false;
          mPlayButton.setText("Play");
      }
      else {
          mPlaying=true;
          mPlayButton.setText("Stop");
          mTimer = new Timer(true);
          mTimer.scheduleAtFixedRate(new TimerTask() {
              @Override
              public void run() {
                  moveForward();
              }
          }, 0, 500);
      }
  }

  @Override
  public void valueChanged(ListSelectionEvent e)  {
  @SuppressWarnings("unchecked")
      JList<Pattern> list = (JList<Pattern>) e.getSource();
      Pattern p = list.getSelectedValue();
        // TODO
        // Based on size, create either a PackedWorld or ArrayWorld
        // from p. Clear the cache, set mWorld and put it into
        // the now-empty cache. Tell the game panel to display
        // the new mWorld.

        if(mPlaying) runOrPause(); //pause evolution

        try {
          if (p.getHeight()*p.getWidth() > 64) {
            mWorld = new ArrayWorld(p);
          } else {
            mWorld = new PackedWorld(p);
          }
          mCachedWorlds = new ArrayList<>();
          mCachedWorlds.add(mWorld);
          gp.display(mWorld);
          repaint();

        } catch (PatternFormatException pfe) {
          throw new RuntimeException();
        }


  }

  public static void main(String[] args) throws IOException {

        PatternStore ps = new PatternStore("http://www.cl.cam.ac.uk/teaching/1617/OOProg/ticks/life.txt");
        GUILife gui = new GUILife(ps);
        gui.setVisible(true);

    }

}
