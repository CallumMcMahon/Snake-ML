import javax.swing.*;
import java.util.Arrays;

/**
 * Created by Callum on 19/11/2017.
 */
public class population {

    myNet[] nets;

    GameLoop fitnessTester;
    static int[] settings;

    public static void setSettings(int[] settings){population.settings = settings;}

    public population(int population, boolean initialise){
        nets = new myNet[population];

        if(initialise){
            for(int i=0;i<population;i++){
                nets[i] = new myNet(5,3);
            }
        }
    }

    public void evaluateFitness(){
        for (int i=0;i<size();i++){
            fitnessTester = new GameLoop(this.settings,null,null,true,false,false);
            fitnessTester.setNet(nets[i]);
            nets[i].fitness = fitnessTester.loop();
            if(nets[i].fitness >10){ System.out.println(nets[i].fitness);}
        }
        Arrays.sort(nets);
    }

    public int size(){return nets.length;}

    public void saveNet(int index, myNet bestNet) {
        nets[index] = bestNet;
    }

    public double getFittest(){return nets[0].fitness;}

}
