import java.util.concurrent.ThreadLocalRandom;

public class map {
    int mapSize;
    int players;
    snakeLogic[] separateMaps;
    static int[][] combinedMap;
    boolean gameStatus = true;
    int[] applePos = {0,0};

    public int getMapSize() {return mapSize;}

    public int[][] getCombinedMap(){return combinedMap;}

    public int[] getApplePos(){return applePos;}

    public void setGameStatus(boolean status){gameStatus = status;}

    public void setSeparateMaps(snakeLogic snake){
        separateMaps[snake.playerNumber] = snake;
    }

    public void spawnApple(){
        int row,col;
        do {
            row = ThreadLocalRandom.current().nextInt(0, mapSize);
            col = ThreadLocalRandom.current().nextInt(0, mapSize);
        }while (combinedMap[row][col] != 0);
        combinedMap[row][col] = -2;
        applePos = new int[]{row,col};
    }

    public boolean mapCovered(){
        for (int row = 0; row < mapSize; row++) {   //check every tile for non-snake space
            for (int col = 0; col < mapSize; col++) {
                if (combinedMap[row][col] < 1){
                    return false;   //if it finds some, return false
                }
            }
        }
        return true;    //else return true
    }

    public void combine(){
        combinedMap = new int[mapSize][mapSize]; //generate fresh map to place new state on
        int deadPlayers = 0;
        for (int i = 0;i<players;i++){
            if(separateMaps[i].isAlive) { //check if they're still alive
                int[][] currentMap = separateMaps[i].getMap();
                for (int row = 0; row < mapSize; row++) {
                    for (int col = 0; col < mapSize; col++) {
                        combinedMap[row][col] += currentMap[row][col]; //populate combinedMap with snake body
                    }
                }
            }
            else{
                deadPlayers += 1;
            }
        }
        if(deadPlayers == players){
            //System.out.println("Game over!");
            gameStatus = false;
        }
        deadPlayers = 0;
        //if no-one has eaten apple keep in same place
        if (combinedMap[applePos[0]][applePos[1]] == 0){  //checks if any snake body occupies where the apple should be
            combinedMap[applePos[0]][applePos[1]] = -2; //recreate the apple there if nothing has changed
        }
        else if(mapCovered() == false){   //if apple was eaten and there is room, spawn a new one
            spawnApple();
        }
        else{
            System.out.println("Game complete");
            gameStatus = false;
            //TODO create settings, restart, quit menu
        }
    }

    public void displayMap(){
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                if(col == mapSize -1){
                    System.out.print(combinedMap[row][col]);
                }
                else if(combinedMap[row][col] < 10 && combinedMap[row][col] >= 0){
                    System.out.print(combinedMap[row][col] + "  ");
                }
                else {
                    System.out.print(combinedMap[row][col] + " ");
                }
            }
            System.out.println() ;
        }
    }

    public map(int mapSize, int players){
        this.mapSize = mapSize;
        combinedMap = new int[mapSize][mapSize];
        this.players = players;
        separateMaps = new snakeLogic[players];
        //spawnApple();
    }
}