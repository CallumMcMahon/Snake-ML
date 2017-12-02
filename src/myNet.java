import java.util.Arrays;
import java.util.Random;

/**
 * Created by mcmah on 15/11/2017.
 */
public class myNet implements Comparable<myNet>{

    double[][][] allWeights;
    double[][] weights1;
    double[][] weights2;
    int[] layerSizes;
    double fitness;
    int outputDirection;
    Random randomiser = new Random();

    public double sigmoidFunction(double sum) {
        return 1.0 / (1 + Math.exp(-1.0 * sum));
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(myNet differentNet) {//order nets from best to worst for mating
        return Double.compare(differentNet.fitness,this.fitness);
    }

    public static double[] matrixvectormult(double[][] A, double[] B) {

        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[] C = new double[aRows];
        for (int i = 0; i < aRows; i++) {
            C[i] = 0.00000;

        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int k = 0; k < aColumns; k++) { // aColumn
                C[i] +=  A[i][k] * B[k];
            }
        }

        return C;
    } //basic inefficient implementation of matrix multiplication

    public double[] sigmoidVector(double[] vector){ //apply the sigmoid function to each element in vector
        double[] sig_form = new double[vector.length];
        for (int i = 0;i<vector.length;i++){
            sig_form[i] = sigmoidFunction(vector[i]);
        }
        return sig_form;
    }

    public myNet(int inputSize,int outputSize){ //initialise weights randomly with uniform distribution and range [-1,1]
        layerSizes = new int[]{inputSize,4,3,outputSize};
        allWeights = new double[layerSizes.length-1][][];
        outputDirection = 0;

        for (int i = 0; i < allWeights.length; i++) {
            //System.out.println("weight matrix "+layerSizes[i+1]+"by"+(layerSizes[i]+1));
            allWeights[i] = new double[layerSizes[i+1]][layerSizes[i]+1];
            for (int j = 0; j < allWeights[i].length; j++) {
                for (int k = 0; k < allWeights[i][j].length; k++) {
                    allWeights[i][j][k] = 2*randomiser.nextGaussian();
                }
            }
        }

/**
        weights1 = new double[hiddenNeurons][inputSize+1];
        for (int i = 0; i < weights1.length; i++) {
            for (int j = 0;j<weights1[i].length;j++) {
                weights1[i][j] = (Math.random() * 2 ) -1.0;
            }
        }
        weights2 = new double[outputSize][inputSize+1];
        for (int i = 0; i < weights2.length; i++) {
            for (int j = 0;j<weights2[i].length;j++) {
                weights2[i][j] = (Math.random() * 2 ) -1.0;
            }
        }**/
    }

    static double[] addBias(double[] a) {
        a  = Arrays.copyOf(a, a.length + 1);
        a[a.length - 1] = 1.0;
        return a;
    }

    public String calcOutput(double[] input, String movingDirection){ //feed-forward application of the network
        double[][] nodeValues = new double[4][];
        /**
        double[] biasinput;
        double[] hiddenLayer;
        double[] outputLayer;
        **/

        nodeValues[0] = addBias(input);
        for(int i = 0;i<allWeights.length;i++){
            //System.out.println("weights matrix "+allWeights[i].length+"x"+allWeights[i][0].length);
            //System.out.println("node dimention "+nodeValues[i].length);
            nodeValues[i+1] = sigmoidVector(matrixvectormult(allWeights[i],nodeValues[i]));
            nodeValues[i+1] = addBias(nodeValues[i+1]);
        }

        /**
        biasinput = new double[input.length+1];
        for(int i=0; i<input.length; i++) {
            biasinput[i] = input[i];
        }
        biasinput[input.length]=1.0;//bias

        hiddenLayer = new double[input.length+1];
        double[] tempHidden = sigmoidVector(matrixvectormult(weights1,biasinput));
        //copy to larger array to add bias
        for(int i=0; i<input.length; i++) {
            hiddenLayer[i] = tempHidden[i];
        }
        hiddenLayer[hiddenNeurons] = 1.0;//bias

        outputLayer = sigmoidVector(matrixvectormult(weights2,hiddenLayer));
        **/

        int index = 0;
        double largest = Integer.MIN_VALUE;
        for ( int i = 0; i < layerSizes[layerSizes.length-1]; i++ ){//loop through output layer elements
            if ( nodeValues[layerSizes.length-1][i] > largest ){//find largest
                largest = nodeValues[layerSizes.length-1][i];
                index = i;
            }
        }

        outputDirection = index;
        //System.out.println(index);

        switch(movingDirection){//convert where to turn into a direction relative to map
            case "w": index = (index+3) % 4;break;
            case "d": break;
            case "s": index = (index+1) % 4;break;
            case "a": index = (index+2) % 4;break;
        }

        //if(index<0){index+=3}//in java sign of result equals sign of dividend

        switch(index){//convert direction into string command
            case 0: return "w";
            case 1: return "d";
            case 2: return "s";
            case 3: return "a";
            default: return "q";
        }
    }

}
