/**
 * Created by mcmah on 15/11/2017.
 */
public class myNet {


    double[][] weights1;
    double[][] weights2;
    double[][] biases;
    final int hiddenNeurons = 5;
    double fitness;

    public double sigmoidFunction(double sum) {
        return 1.0 / (1 + Math.exp(-1.0 * sum));
    }

    public double getFitness() {
        return fitness;
    }
    public double[][][] getWeights(){return new double[][][]{weights1,weights2};}

    public void setFitness(double fitness) {
        this.fitness = fitness;
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
                C[i] += A[i][k] * B[k];
            }
        }

        return C;
    }

    public double[] sigmoidVector(double[] vector){
        double[] sig_form = new double[vector.length];
        for (int i = 0;i<vector.length;i++){
            sig_form[i] = sigmoidFunction(vector[i]);
        }
        return sig_form;
    }

    public myNet(double[][] weights1,double[][] weights2){this.weights1=weights1;this.weights2=weights2;}

    public myNet(int inputSize,int outputSize){
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
        }
    }

    public String calcOutput(double[] input, String movingDirection){
        double[] biasinput;
        double[] hiddenLayer;
        double[] outputLayer;

        biasinput = new double[input.length+1];
        for(int i=0; i<input.length; i++) {
            biasinput[i] = input[i];
        }
        biasinput[input.length]=1;//bias

        hiddenLayer = new double[input.length+1];
        double[] tempHidden = sigmoidVector(matrixvectormult(weights1,biasinput));
        //copy to larger array to add bias
        for(int i=0; i<input.length; i++) {
            hiddenLayer[i] = tempHidden[i];
        }
        hiddenLayer[hiddenNeurons] = 1;//bias

        outputLayer = sigmoidVector(matrixvectormult(weights2,hiddenLayer));

        int index = 0;
        double largest = Integer.MIN_VALUE;
        for ( int i = 0; i < outputLayer.length; i++ ){
            if ( outputLayer[i] > largest ){
                largest = outputLayer[i];
                index = i;
            }
        }

        switch(movingDirection){
            case "w": index = (index+3) % 4;break;
            case "d": break;
            case "s": index = (index+1) % 4;break;
            case "a": index = (index+2) % 4;break;
        }

        //if(index<0){index+=3}//in java sign of result equals sign of dividend

        switch(index){
            case 0: return "w";
            case 1: return "d";
            case 2: return "s";
            case 3: return "a";
            default: return "q";
        }
    }

}
