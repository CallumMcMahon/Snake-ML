import javax.swing.*;

import static java.lang.Thread.sleep;

/**
 * Created by Callum on 28/06/2017.
 */
public class Main {
    public static void main(String[] args) {
        final int SNAKE_SIZE = 5;
        int[] settings;
        JFrame frame;
        Object syncObject = new Object(); //have main method wait for settings class to finish
        boolean AI;

        if(args.length == 0) {


        /* GUI SETTINGS */
            swingSettings settingsMenu = new swingSettings(syncObject);
            synchronized (syncObject) {
                try {
                    syncObject.wait();
                } catch (InterruptedException e) {
                    throw new Error();
                }
            }
            settingsMenu.setActiveMenu(false);
            settings = settingsMenu.getSettings();
            settings[0] += 1;//correcting number of players from off-by-one error
            frame = settingsMenu.getFrame();
            AI = false;
        }
        else{
            settings = new int[]{1,0,1};
            frame = swingSettings.makeFirstFrame();
            AI = true;
        }

        GameLoop[] newgame = new GameLoop[101];

        double maxfit = 0;
        int maxindex = 0;
        double tempfitness;
        for (int k = 0;k<100;k++) {
            newgame[k] = new GameLoop(settings, frame, syncObject, AI, false);
            newgame[k].setNet();
            tempfitness = newgame[k].loop();
            newgame[k].net.fitness =tempfitness;
            if (newgame[k].net.fitness>maxfit){maxfit = newgame[k].net.fitness; maxindex = k;}
        }
        newgame[100] = new GameLoop(settings,frame,syncObject,true,true);
        newgame[100].setNet(newgame[maxindex].getNet());
        newgame[100].loop();

    }

    public static int[] settings(){
        System.out.println("Enter map size.");
        int mapSize = EasyIn.getInt();
        while(mapSize<3 | mapSize >32){
            System.out.println("Enter within a sensible range");
            mapSize = EasyIn.getInt();
        }
        System.out.println("Enter snake size");
        int snakeSize;
        snakeSize = EasyIn.getInt();
        while(snakeSize<1 | snakeSize >mapSize/2){
            if (snakeSize<1){
                System.out.println("Snake has to be a positive size! Please enter a new size.");
            }
            else{
                System.out.println("Snake cannot be that big! Please enter a new size.");
            }
            snakeSize = EasyIn.getInt();
        }
        int players = 0;
        System.out.println("Choose the number of players: 1 or 2");
        players = EasyIn.getInt();
        while(players<1 || players > (mapSize-1)/2){
            System.out.println("please enter a sensible number of players");
            players = EasyIn.getInt();
        }
        return new int[] {snakeSize,mapSize, players};
    } //not used anymore, command line settings
}