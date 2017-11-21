import javax.swing.*;

import java.util.Arrays;

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
        //-----GENETIC ALGORITHM-----
        int popSize = 1000;
        population pop = new population(popSize,true);
        pop.setSettings(settings);
        // Evolve our population until we reach an optimum solution
        int generationCount = 0;
        double generationfitness = 0;
        while (generationfitness < 49) {
            generationCount++;
            System.out.println("Generation: " + generationCount + " Fittest: " + generationfitness);
            pop = geneticAlgorithm.evolvePopulation(pop);
            pop.evaluateFitness();
            generationfitness = pop.getFittest();
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);


        EasyIn.pause("Press enter to view replay");

        GameLoop solutionGame;
        solutionGame = new GameLoop(settings, frame, syncObject, AI, true,false);
        solutionGame.setNet(pop.nets[0]);
        solutionGame.loop();

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