import javax.swing.*;

/**
 * Created by mcmah on 13/11/2017.
 */
public class GameLoop {
    int[] settings;
    boolean AI;
    myNet net;
    final int SNAKE_SIZE = 5;
    int mapSize;
    snakeLogic[] snakeLogics;
    map gameMap;
    swingGUI gui;
    Object syncObject;
    boolean showgui = false;

    public GameLoop(int[] settings, JFrame frame, Object syncObject, boolean AI, boolean showgui, boolean slowmode) {

        this.AI = AI;
        this.showgui = showgui;

        switch (settings[2]) {
            case 0:
                this.mapSize = 6;
                break;//only one menu choice offers small setting
            case 4:
            case 6:
            case 1:
                this.mapSize = 10;
                break;// medium/normal map size option
            case 7:
            case 2:
                this.mapSize = 15;
                break; // large map size option
            default:
                throw new Error();
        }

        snakeLogic.resetPlayerNumbers();//static method keeping track of index of players needs resetting
        snakeLogics = new snakeLogic[settings[0]]; //create snake array
        gameMap = new map(mapSize, settings[0]); //create shared map for all players
        for (int i = 0; i < settings[0]; i++) {
            snakeLogics[i] = new snakeLogic(SNAKE_SIZE, gameMap); //add snakes to the array based on number of players
            gameMap.setSeparateMaps(snakeLogics[i]); //link the individual snakes up to the shared map
        }
        gameMap.combine(); //add the snake positions to the shared map
        if (syncObject == null){this.syncObject = null;}
        if(showgui) {
            this.syncObject = syncObject;
            this.gui = new swingGUI(gameMap, frame, syncObject, false);
        }
        if ( ! slowmode && syncObject != null){gui.setSlowMode(slowmode);}

        int playerTurn = 0;

        switch (settings[1]) {//default value for slowMode already set to true for start of game
            case 0:
            case 4: if(syncObject != null) {gui.setSlowMode(false); break;}//if a fast paced mode is selected, change slowMode to false
        }
    }

    public void setNet(myNet net){this.net = net;}

    public void setShowgui(boolean showgui){this.showgui = showgui;}

    public double loop() {
        if(showgui){gui.showGUI();}
        String[] commands = new String[gameMap.players];
        if(AI){
            for (int i = 0; i < gameMap.players; i++) {//get neural net starting directions
                snakeLogics[i].updateNNinputs();
                commands[i] = net.calcOutput(snakeLogics[i].NNfeatures,"w");
            }
            //System.out.println("gui key is:"+gui.key[0]);

        }
        else {
            synchronized (syncObject) { //wait for players to select initial direction before starting
                try {
                    syncObject.wait();
                } catch (InterruptedException e) {
                    throw new Error();
                }
            }
            for (int i = 0; i < gameMap.players; i++) {
                commands[i] = gui.key[i];
            }
        }
        for (int i = 0; i < gameMap.players; i++) {
            snakeLogics[i].setMovingDirection(commands[i]); //set initial directions as chosen by each player
        }

        while (true) {//run the game logic each iteration of a loop

            for (int i = 0; i < gameMap.players; i++) {
                if (snakeLogics[i].isAlive) {
                    snakeLogics[i].moveSnake(commands[i]);//move each player
                    //DO WE NEED THIS???gameMap.setSeparateMaps(snakeLogics[i]);//add the new moves to the shared map
                }
            }
            gameMap.combine();//update the shared map

            //gameMap.displayMap();
            if(showgui){gui.showGUI();}
            if (gameMap.gameStatus == false) {//if the game is over, stop the loop
                break;
            }


            if(syncObject != null) {//non AI behaviour each turn
                if (gui.getSlowMode() == true) {//depending on game style, wait for user input each move
                    synchronized (syncObject) {
                        try {
                            syncObject.wait();
                        } catch (InterruptedException e) {
                            throw new Error();
                        }
                    }
                }
                else {//DO WE NEED THIS?? if (AI == false) || showgui == true) {//or wait 100 milliseconds and keep running the game logic without the need for user input
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if(AI) {//AI behaviour each turn
                for (int i = 0; i < gameMap.players; i++) {
                    snakeLogics[i].updateNNinputs();
                    commands[i] = net.calcOutput(snakeLogics[i].NNfeatures, "w");
                }
                //System.out.println("gui key is:"+gui.key[0]);

            }
            else{
                for (int i = 0; i < gameMap.players; i++) {
                    commands[i] = gui.key[i];
                }
            }
            //System.out.println(snakeLogics[0].snakeSize);
        }
        if(syncObject != null){gui.frame.dispose();}
        //return (double) (snakeLogics[0].snakeSize *20 + snakeLogics[0].movesMade);
        return (double) (snakeLogics[0].movesMade); //our fitness parameter
    }

    public void resetSnakes(){
        for (int i = 0; i < gameMap.players; i++) {
            snakeLogics[i].resetSnake();
        }
        gameMap.combine();
    }
}
