/**
 * Created by Callum on 28/06/2017.
 */

import javax.swing.*;

public class Main {

    // app window that is used throughout
    static JFrame frame;
    //object to pause program during setting selection
    static Object syncObject = new Object();

    public static void main(String[] args) {
        final int SNAKE_SIZE = 5;
        int[] settings;

        boolean AI;

        if(args.length > 0 ) {
        /* GUI SETTINGS */
            swingSettings settingsMenu = new swingSettings(syncObject);
            synchronized (syncObject) {
                try {
                    syncObject.wait();
                } catch (InterruptedException e) {
                    throw new Error();
                }
            }
            //remove menu
            settingsMenu.setActiveMenu(false);
            //retrieve chosen settings
            settings = settingsMenu.getSettings();
            frame = settingsMenu.getFrame();
            AI = false;
        }
        else{
            //if training AI, use default map size settings
            settings = new int[]{1,0,1};
            frame = swingSettings.makeFirstFrame();
            AI = true;
        }
        if(!AI){
            // game with user input
            GameLoop humangame = new GameLoop(settings,frame,syncObject,false,true,true,0);
            humangame.loop();
        }
        else {
            /*-----GENETIC ALGORITHM-----*/
            // number of agents to simulate each epoch
            int popSize = 100000;
            population pop = new population(popSize, true);
            pop.setSettings(settings);

            //set convergence criterion
            int generationCount = 0;
            boolean converged = false;
            int convergeEpochs = 8;
            double[] generationfitness = new double[convergeEpochs];

            // Evolve our population until we reach an optimum solution
            while (!converged) {
                generationCount++;
                System.out.println("Generation: " + generationCount + " Fittest: " + generationfitness[(generationCount-1)%convergeEpochs]);
                // recombination and mutation of the top performing agents
                pop = geneticAlgorithm.evolvePopulation(pop);
                pop.evaluateFitness();
                generationfitness[generationCount%convergeEpochs] = pop.getFittest();
                converged = true;
                // convergence check
                for (int i = 1; i < convergeEpochs; i++) {
                    if(generationfitness[i] != generationfitness[i-1]){
                        converged = false;
                    }
                }
                //show intermediary performance
                if(generationCount%10==0){
                    GameLoop solutionGame;
                    solutionGame = new GameLoop(settings, frame, syncObject, AI, true, false, 0);
                    solutionGame.setNet(pop.nets[0]);
                    solutionGame.loop();
                }
            }

            System.out.println("Solution found!");
            System.out.println("Generation: " + generationCount+ " Fittest: " + generationfitness[0]);

            EasyIn.pause("Press enter to view replay");

            //show final best agent
            GameLoop solutionGame;
            solutionGame = new GameLoop(settings, frame, syncObject, AI, true, false, 0);
            solutionGame.setNet(pop.nets[0]);
            solutionGame.loop();
        }

    }

//unused command line settings
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