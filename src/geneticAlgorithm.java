/**
 * Created by Callum on 19/11/2017.
 */
public class geneticAlgorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.1;
    private static final boolean elitism = true;
    private static final int tournamentSize = 5;
    private static int breedingSize = 100;

    /* Public methods */

    // Evolve a population
    public static population evolvePopulation(population pop) {
        if (pop.size() <400){breedingSize = pop.size()/2;}
        population newPopulation = new population(pop.size(), false);

        int elitismOffset;
        if (elitism) {
            elitismOffset = 3;
        } else {
            elitismOffset = 0;
        }

        // Keep our best individual
        if (elitism) {
            for(int i =0;i<elitismOffset;i++) {
                newPopulation.saveNet(i, pop.nets[i]);
            }
        }

        // Crossover population

        // Loop over the population size and create new individuals with
        // crossover
        for (int i = elitismOffset; i < pop.size(); i++) {
            myNet net1 = randomSelection(pop);
            myNet net2 = randomSelection(pop);
            myNet newNet = crossover(net1, net2);
            newPopulation.saveNet(i, newNet);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.nets[i]);
        }

        return newPopulation;
    }

    // Crossover individuals
    private static myNet crossover(myNet net1, myNet net2) {
        myNet newNet = new myNet(6,3);
        // Loop through genes
        for (int i = 0; i < newNet.allWeights.length; i++) {
            for (int j = 0; j < newNet.allWeights[i].length; j++) {
                for (int k = 0; k < newNet.allWeights[i][j].length; k++) {
                    if (Math.random() <= uniformRate) {
                        newNet.allWeights[i][j][k] = net1.allWeights[i][j][k];
                    } else {
                        newNet.allWeights[i][j][k] = net2.allWeights[i][j][k];
                    }
                }
            }
        }


        /**
        for (int i = 0; i < newNet.weights1.length; i++) {
            for (int j = 0; j < newNet.weights1[0].length; j++){
                // Crossover
                if (Math.random() <= uniformRate) {
                    newNet.weights1[i][j] = net1.weights1[i][j];
                } else {
                    newNet.weights1[i][j] = net2.weights1[i][j];
                }
            }
        }
        for (int i = 0; i < newNet.weights2.length; i++) {
            for (int j = 0; j < newNet.weights2[0].length; j++){
                // Crossover
                if (Math.random() <= uniformRate) {
                    newNet.weights2[i][j] = net1.weights2[i][j];
                } else {
                    newNet.weights2[i][j] = net2.weights2[i][j];
                }
            }
        }
         **/
        return newNet;
    }

    // Mutate an individual
    private static void mutate(myNet net) {
        // Loop through genes

        for (int i = 0; i < net.allWeights.length; i++) {
            for (int j = 0; j < net.allWeights[i].length; j++) {
                for (int k = 0; k < net.allWeights[i][j].length; k++) {
                    if (Math.random() <= mutationRate) {
                        net.allWeights[i][j][k] = 2*net.randomiser.nextGaussian();
                    }
                }
            }
        }

        /**
        for (int i = 0; i < net.weights1.length; i++) {
            for (int j = 0; j < net.weights1[0].length; j++){
                // Crossover
                if (Math.random() <= mutationRate) {
                    net.weights1[i][j] = (Math.random() * 2 ) -1.0;
                }
            }
        }
        for (int i = 0; i < net.weights2.length; i++) {
            for (int j = 0; j < net.weights2[0].length; j++){
                // Crossover
                if (Math.random() <= uniformRate) {
                    net.weights2[i][j] = (Math.random() * 2) - 1.0;
                }
            }
        }
         **/
    }

    // Select individuals for crossover
    private static myNet randomSelection(population pop) {
        return pop.nets[((int) (Math.random() * breedingSize))];
    }

    private static myNet tournamentSelection(population pop) {
        // Create a tournament population
        population tournament = new population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveNet(i, pop.nets[randomId]);
        }
        // Get the fittest
        tournament.evaluateFitness();
        myNet fittest = tournament.nets[0];
        return fittest;
    }
}