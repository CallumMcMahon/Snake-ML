import java.util.Arrays;

public class snakeLogic {
    int snakeSize;
    boolean isAlive;
    int mapSize;
    int[] headPos;
    String movingDirection;
    double[] NNfeatures = new double[5];
    int movesMade = 0;
    int map[][];
    boolean growTail;
    int playerNumber;
    static int nextPlayerNumber;
    static map globalMap;

    public void eatenApple(){
        if (Arrays.equals(headPos,globalMap.getApplePos())){
            growTail = true;
        }
    }

    public void setMovingDirection(String direction){movingDirection = direction;}

    public String oppositeDirection(String moving){
        switch(moving){
            case "w": return "s";
            case "a": return "d";
            case "s": return "w";
            case "d": return "a";
            default: throw new Error();
        }
    }

    public void moveSnake(String command){
        if(command.matches("w|a|s|d")) {    //user chose to move
            movesMade += 1;
            if(command.equals(oppositeDirection(movingDirection))){ //stop it from moving backwards into itself
                command = movingDirection;
            }
            if (collisionDetection(headPos,command)[0] != -1){
                headPos = collisionDetection(headPos,command);
                eatenApple();   //check if head moves into apple position
                growBody();
                map[headPos[0]][headPos[1]] = 1;
                if (growTail == true) {                                   //increase snake size variable after we've
                    snakeSize += 1;                                       //used it updating shorter snake
                    growTail = false;
                }
                movingDirection = command;
            }
            else {
                if(NNfeatures[0] == 0 && NNfeatures[1] == 0 && NNfeatures[2] == 0){
                    System.out.println("Collision! Player "+playerNumber+" dead! Moves:" + movesMade + " Snake size:" + snakeSize);
                }
                isAlive = false;
            }
        }
        else {       //user chose to quit
            System.out.println("Quit. Game over.");
            globalMap.gameStatus = false;
        }
    }

    public void updateNNinputs(){
        NNfeatures = new double[]{0,0,0,1,1};
        double[] clear = {0,0,0,0};
        for (int i = 0; i < 6; i++) {
            if (clear[0] == i && collisionDetection(new int[]{headPos[0]-i, headPos[1]}, "w")[0] != -1) {
                clear[0] += 1;
            }
            if (clear[1] == i && collisionDetection(new int[]{headPos[0], headPos[1] + i}, "d")[0] != -1) {
                clear[1] += 1;
            }
            if (clear[2] == i && collisionDetection(new int[]{headPos[0] + i, headPos[1]}, "s")[0] != -1) {
                clear[2] += 1;
            }
            if (clear[3] == i && collisionDetection(new int[]{headPos[0], headPos[1] - i}, "a")[0] != -1) {
                clear[3] += 1;
            }
        }
        int start = 0;
        switch(movingDirection){
            case "w": start = 3; break;
            case "d": start = 0; NNfeatures[4] = -1; break;
            case "s": start = 1; NNfeatures[3] = -1; NNfeatures[4] = -1; break;
            case "a": start = 2; NNfeatures[3] = -1; break;
        }
        int j = 0;
        for (int i=start;j<3; j++){
            NNfeatures[j] = (clear[i]-3.0)/3.0;
            i = (i+1) % 4;
        }
        NNfeatures[3] =0;//*= (globalMap.getApplePos()[0] - headPos[0])/mapSize; // left/right distance
        NNfeatures[4] =0;//*= (globalMap.getApplePos()[1] - headPos[1])/mapSize; // up/down distance
        for(int i = 0;i<5;i++){
            //System.out.println(NNfeatures[i]); //debug output
        }
        //System.out.println(movingDirection); //debug output
        //System.out.println("---------------"); //debug output
    }

    public int[] collisionDetection(int[] point, String direction){
        int[][] recentMap = globalMap.getCombinedMap();
        int[] notSafe = {-1,-1};                                                    //or {-1,-1} if collision detected
        switch(direction) {
            case "w":   if(point[0]>0 && recentMap[point[0]-1][point[1]]<1){
                            return new int[] {point[0]-1,point[1]};
                        }
                        return notSafe;
            case "a":   if(point[1]>0 && recentMap[point[0]][point[1]-1]<1){
                            return new int[] {point[0],point[1]-1};
                        }
                        return notSafe;
            case "s":   if(point[0]<mapSize-1 && recentMap[point[0]+1][point[1]]<1){
                            return new int[] {point[0]+1,point[1]};
                        }
                        return notSafe;
            case "d":   if(point[1]<mapSize-1 && recentMap[point[0]][point[1]+1]<1){
                            return new int[] {point[0],point[1]+1};
                        }
                        return notSafe;
            default:    return notSafe;
        }
    }

    public void growBody(){
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                if(map[row][col] != 0){
                    map[row][col] += 1;
                }
                if(growTail == false) {
                    if (map[row][col] == snakeSize+1) {
                        map[row][col] = 0;
                    }
                }
            }
        }
    }

    public int[][] getMap() {return map;}

    public static int genNextPlayer(){return nextPlayerNumber++;}

    public snakeLogic(int snakeSize, map globalMap){
        this.snakeSize = snakeSize;
        this.isAlive = true;
        this.mapSize = globalMap.getMapSize();
        playerNumber = genNextPlayer();
        resetSnake();
        this.globalMap = globalMap;
        growTail = false;
        movingDirection = "w";
    }

    public void resetSnake(){
        this.map = new int[mapSize][mapSize];
        if (playerNumber % 2 == 0){
            for (int row = 5/**mapSize - snakeSize - 1*/, i = 1; i <= snakeSize; row++, i++) {
                map[row][playerNumber +5] = i;
            }
            headPos = new int[]{5/*mapSize - snakeSize -1*/, playerNumber +5};
        }
        else{
            for (int row = snakeSize, i = 1; i <= snakeSize; row--, i++) {
                map[row][mapSize - playerNumber -1] = i;
            }
            headPos = new int[]{snakeSize, mapSize - playerNumber -1};
        }
    }

    public static void resetPlayerNumbers(){ nextPlayerNumber = 0; }
}