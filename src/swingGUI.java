import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.*;

/**
 * Created by Callum on 29/06/2017.
 */

public class swingGUI extends JFrame implements KeyListener{
    private int tileSize;
    JFrame frame;
    JPanel panel;
    int row;
    int col;
    map gameMap;
    ImageIcon apple;
    String[] key = {"",""};
    Object syncObject;
    boolean slowMode = true;

    public void setSlowMode(boolean slowMode){this.slowMode = slowMode;}

    public boolean getSlowMode(){return slowMode;}

    public void allMovesMade(){
        boolean complete = true;
        for (int i = 0; i < gameMap.players; i++) {
            if (gameMap.separateMaps[i].isAlive == true && key[i].equals("")) { //check if any moved haven't been specified
                complete = false;
            }
        }
        if(complete){ //when all players have chosen their move, start next turn
            synchronized(syncObject) {
                syncObject.notify();
            }
        }
    }

    //setup initial frame
    public swingGUI(map gameMap, JFrame frame, Object syncObject,boolean showgui) {
        this.syncObject = syncObject;
        this.gameMap = gameMap;
        this.frame = frame;
        this.frame.getContentPane().removeAll();
        panel = new JPanel();
        this.frame.getContentPane().add(panel);

        row = gameMap.getMapSize();
        col = gameMap.getMapSize();
        if (row > 10){tileSize = 45;} else {tileSize = 80;}
        this.frame.setBounds(100, 100, row * tileSize, col * tileSize);
        panel.setLayout(new GridLayout(row, col));
        this.frame.addKeyListener(this);
        apple = new ImageIcon(new ImageIcon(getClass().getResource("apple.png")).getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        if(showgui) {
            requestFocusInWindow();
            this.frame.setVisible(true);
            this.frame.setResizable(false);
        }
    }

    public void showGUI(){
        panel.removeAll(); //create new screen
        panel.setLayout(new GridLayout(row, col));
        JLabel[][] grid = new JLabel[row][col]; //2D array to store all the Jlabel game tiles
        Color colour = null;
        for (int i = 0; i < row; i++) { //go through every tile
            for (int j = 0; j < col; j++) {
                grid[i][j] = new JLabel();
                grid[i][j].setBorder(new LineBorder(Color.BLACK));
                grid[i][j].setSize(50, 50);

                for(int p = 0;p<gameMap.players;p++) { //go through each colour
                    switch (p) {
                        case 0:
                            colour = Color.red;
                            break;
                        case 1:
                            colour = Color.yellow;
                            break;
                        case 2:
                            colour = Color.green;
                            break;
                        case 3:
                            colour = Color.blue;
                            break;
                        default:
                            colour = Color.pink; //default colour if more than 4 players are playing
                    }
                    //if tile belongs to their snake, assign it their colour
                    if (gameMap.separateMaps[p].map[i][j] > 0 && gameMap.separateMaps[p].isAlive == true) {
                        grid[i][j].setBackground(colour);
                        break;
                    }
                }
                if (gameMap.getCombinedMap()[i][j] == -2){ //show an apple on the apple tile "-2"
                    grid[i][j].setIcon(apple);
                }

                grid[i][j].setOpaque(true);
                panel.add(grid[i][j]);//add it to the game window
            }
        }

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.repaint();
        if(slowMode){ //resetSnake move commands if in turn based games after each turn
            for(int i = 0;i<key.length;i++){
                key[i] = "";
            }
        }

    }

    public void showEndScreen(){
        //TODO make end screen
    }




    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) { //setting snake movement instruction based on key press
        switch(e.getKeyCode()){
            case KeyEvent.VK_W: key[0] = "w";break;
            case KeyEvent.VK_A: key[0] = "a";break;
            case KeyEvent.VK_S: key[0] = "s";break;
            case KeyEvent.VK_D: key[0] = "d";break;
            case KeyEvent.VK_UP: key[1] = "w";break;
            case KeyEvent.VK_LEFT: key[1] = "a";break;
            case KeyEvent.VK_DOWN: key[1] = "s";break;
            case KeyEvent.VK_RIGHT: key[1] = "d";break;
            case KeyEvent.VK_ESCAPE: gameMap.setGameStatus(false);
            default: break;
        }
        if(slowMode){ //in turn based modes only start new turn when key event happens
            allMovesMade();
        }
        else {
            synchronized(syncObject) {
                syncObject.notify();
            }
        }
    }
}