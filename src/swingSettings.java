import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Callum on 05/07/2017.
 */
public class swingSettings extends JFrame implements KeyListener {

    JFrame frame;
    JPanel buttonArea;
    static final int frameHeight = 666;
    static final int frameWidth = 666;
    static int screenWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    static int screenHeight = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    final int buttonAreaHeight = 10;
    JPanel mechanicsOptions;
    JPanel multiplayerSize;
    String[] key = {"", ""};

    JLabel[][] labels = new JLabel[3][];
    CardLayout cl1;
    CardLayout cl2;
    int[] settings = new int[3];
    Object syncObject;
    boolean activeMenu = true;
    int[] currentMenuPlace = {0,0};
    int[][] menu = new int[][] {{1,0},{1,0,-1,1,0},{1,0,0,-1,1,-1,1,0},{1}};
    Border blackline = BorderFactory.createLineBorder(Color.black,3,true);
    Border largePadding = BorderFactory.createEmptyBorder(15,40,15,40);
    Border selected = new CompoundBorder(blackline,largePadding);

    final String SOLO = "solo";
    final String MULTIPLAYER = "multiplayer";
    final String MULTIPLAYER_STRATEGY = "multiplayer_strategy";
    final String MULTIPLAYER_FRENZY = "multiplayer_frenzy";


    public JFrame getFrame(){return frame;}

    public int[] getSettings(){return settings;}

    public void setActiveMenu(boolean active){activeMenu = active;}

    public void updateMenu(){
        for (int i = 0;i<3;i++){
            for (int j = 0; j< menu[i].length ;j++){
                // if option not selected, colour in red
                if(menu[i][j] == 0) {
                    labels[i][j].setBackground(Color.red);
                    labels[i][j].setOpaque(true);
                }
                // if option selected, colour in green
                else if(menu[i][j] == 1){
                    labels[i][j].setBackground(Color.green);
                    labels[i][j].setOpaque(true);
                }
                // highlight current selection with black border
                if(currentMenuPlace[0] == i && currentMenuPlace[1] == j){
                    labels[i][j].setBorder(selected);
                }
                // other borders have normal thin borders
                else if(menu[i][j] != -1){
                    labels[i][j].setBorder(largePadding);
                }
            }
        }
        // switch GUI options based on solo or multiplayer selection
        if (menu[0][0] == 1){
            cl1.show(mechanicsOptions,SOLO);
        }
        else{
            cl1.show(mechanicsOptions,MULTIPLAYER);
        }
        cl2.show(multiplayerSize,MULTIPLAYER_FRENZY);

        if(menu[1][3] == 1){
            cl2.show(multiplayerSize,MULTIPLAYER_STRATEGY);
        }
        else{
            cl2.show(multiplayerSize,MULTIPLAYER_FRENZY);
        }
    }

    public void choice(){
        // update choices is the menu system
        String direction="";
        for (String press : key) {
            if (!press.equals("")) {
                direction = press;
                key = new String[] {"",""};
            }
        }
        switch (direction) {
            case "w":
                if(currentMenuPlace[0] == 1){
                    currentMenuPlace[0] -= 1;
                    for (int i = 0;i<menu[currentMenuPlace[0]].length;i++){
                        if(menu[currentMenuPlace[0]][i]==1){currentMenuPlace[1] = i;break;}
                    }
                }
                else if(currentMenuPlace[0] == 2){
                    currentMenuPlace[0] -= 1;
                    if(currentMenuPlace[1] <3){
                        for (int i = 0;i <2;i++){
                            if(menu[currentMenuPlace[0]][i]==1){currentMenuPlace[1] = i;break;}
                        }
                    }
                    else{
                        for (int i = 3;i <5;i++){
                            if(menu[currentMenuPlace[0]][i]==1){currentMenuPlace[1] = i;break;}
                        }
                    }
                }
                break;
            case "a":
                if (currentMenuPlace[1] -1 >=0 && menu[currentMenuPlace[0]][currentMenuPlace[1]-1] != -1) {
                    menu[currentMenuPlace[0]][currentMenuPlace[1]] = 0;
                    menu[currentMenuPlace[0]][currentMenuPlace[1]-1] = 1;
                    currentMenuPlace[1] -= 1;
                }
                break;
            case "s":
                if (currentMenuPlace[0] == 0){
                    if (currentMenuPlace[1] == 0){
                        for(int i = 0;i<2;i++){
                            if(menu[currentMenuPlace[0]+1][i]==1){currentMenuPlace[1] = i;break;}
                        }
                    }
                    else{
                        for(int i = 3;i<5;i++){
                            if(menu[currentMenuPlace[0]+1][i]==1){currentMenuPlace[1] = i;break;}
                        }
                    }
                }
                else if(currentMenuPlace[0] == 1){
                    if(currentMenuPlace[1] <2){
                        for(int i = 0;i<3;i++){
                            if(menu[currentMenuPlace[0]+1][i]==1){currentMenuPlace[1] = i;break;}
                        }
                    }
                    else if (currentMenuPlace[1] == 3){
                        currentMenuPlace[1] = 4;
                    }
                    else{
                        for(int i = 6;i<8;i++){
                            if(menu[currentMenuPlace[0]+1][i]==1){currentMenuPlace[1] = i;break;}
                        }
                    }
                }
                else{
                    if (currentMenuPlace[1] < 3){
                        settings[0] = 0;
                        settings[2] = currentMenuPlace[1];
                        for(int i = 0;i<2;i++){
                            if (menu[1][i] == 1){settings[1] = i;break;}
                        }
                    }
                    else if(currentMenuPlace[1] == 4){
                        settings = new int[] {1,3,4};
                    }
                    else{
                        settings = new int[]{1,4,currentMenuPlace[1]};
                    }
                    //changing number of players from 0-indexed to 1-indexed
                    settings[0]+=1;
                    synchronized(syncObject) {
                        syncObject.notify();
                    }
                }
                currentMenuPlace[0] += 1;
                break;
            case "d":
                if (currentMenuPlace[1] +2 <= menu[currentMenuPlace[0]].length && menu[currentMenuPlace[0]][currentMenuPlace[1]+1] != -1) {
                    menu[currentMenuPlace[0]][currentMenuPlace[1]] = 0;
                    menu[currentMenuPlace[0]][currentMenuPlace[1]+1] = 1;
                    currentMenuPlace[1] += 1;
                }
                break;
            default: throw new Error();
        }
    }

    public static JFrame makeFirstFrame(){
        // create frame and set make exitting application kill process gracefully
        JFrame frame = new JFrame("Snakes on a pane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds((screenWidth-frameWidth)/2, (screenHeight-frameHeight)/2,frameWidth,frameHeight);
        return frame;
    }

    public swingSettings(Object syncObject) {
        this(syncObject, makeFirstFrame());
    }

    public swingSettings(Object syncObject, JFrame frame) {
        // get control of sync object and window frame
        this.syncObject = syncObject;
        this.frame = frame;

        // construct menu
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(frameWidth,frameHeight));
        this.frame.getContentPane().removeAll();
        this.frame.getContentPane().add(panel);

        buttonArea = new JPanel();
        buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.PAGE_AXIS));
        buttonArea.setPreferredSize(new Dimension(frameWidth,(frameHeight+buttonAreaHeight)/2));
        buttonArea.setLocation(0,(frameHeight+buttonAreaHeight)/2);
        buttonArea.setBorder(new EmptyBorder((frameHeight-buttonAreaHeight-10)/2,0,0,0));
        buttonArea.setBorder(blackline);
        panel.add(buttonArea);


        JPanel numOfPlayers = new JPanel();
        mechanicsOptions = new JPanel();
        mechanicsOptions.setLayout(new CardLayout());
        labels[0] = new JLabel[2];
        labels[1] = new JLabel[5];
        labels[2] = new JLabel[8];

        labels[0][0] = new JLabel("Solo");
        labels[0][1] = new JLabel("Multiplayer");
        numOfPlayers.add(labels[0][0]);
        numOfPlayers.add(labels[0][1]);
        buttonArea.add(numOfPlayers);
        buttonArea.add(mechanicsOptions);

        JPanel soloOptions = new JPanel();
        soloOptions.setLayout(new BoxLayout(soloOptions, BoxLayout.PAGE_AXIS));
        JPanel soloGamemode = new JPanel();
        JPanel soloSize = new JPanel();
        soloOptions.add(soloGamemode);
        soloOptions.add(soloSize);
        mechanicsOptions.add(soloOptions, SOLO);

        labels[1][0] = new JLabel("classic");
        labels[1][1] = new JLabel("complete the map");
        soloGamemode.add(labels[1][0]);
        soloGamemode.add(labels[1][1]);

        labels[2][0] = new JLabel("small");
        labels[2][1] = new JLabel("normal");
        labels[2][2] = new JLabel("large");
        soloSize.add(labels[2][0]);
        soloSize.add(labels[2][1]);
        soloSize.add(labels[2][2]);

        JPanel multiplayerOptions = new JPanel();
        JPanel multiplayerGamemode = new JPanel();
        multiplayerSize = new JPanel();
        multiplayerOptions.setLayout(new BoxLayout(multiplayerOptions, BoxLayout.PAGE_AXIS));
        multiplayerSize.setLayout(new CardLayout());
        multiplayerOptions.add(multiplayerGamemode);
        multiplayerOptions.add(multiplayerSize);
        mechanicsOptions.add(multiplayerOptions, MULTIPLAYER);

        labels[1][3] = new JLabel("Strategy game");
        labels[1][4] = new JLabel("Frenzy");
        multiplayerGamemode.add(labels[1][3]);
        multiplayerGamemode.add(labels[1][4]);

        JPanel multiplayerStrategySize = new JPanel();
        labels[2][4] = new JLabel("Standard");
        multiplayerStrategySize.add(labels[2][4]);

        JPanel multiplayerFrenzySize = new JPanel();
        multiplayerOptions.add(multiplayerFrenzySize);
        labels[2][6] = new JLabel("normal");
        labels[2][7] = new JLabel("large");
        multiplayerFrenzySize.add(labels[2][6]);
        multiplayerFrenzySize.add(labels[2][7]);

        multiplayerSize.add(multiplayerStrategySize, MULTIPLAYER_STRATEGY);
        multiplayerSize.add(multiplayerFrenzySize, MULTIPLAYER_FRENZY);

        cl1 = (CardLayout)(mechanicsOptions.getLayout());
        cl2 = (CardLayout)(multiplayerSize.getLayout());

        frame.addKeyListener(this);
        requestFocusInWindow();
        frame.setVisible(true);
        frame.setResizable(false);
        updateMenu();
    }
    @Override
    public void keyTyped (KeyEvent e){}

    @Override
    public void keyReleased (KeyEvent e){}

    @Override
    public void keyPressed (KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                key[0] = "w";
                break;
            case KeyEvent.VK_A:
                key[0] = "a";
                break;
            case KeyEvent.VK_S:
                key[0] = "s";
                break;
            case KeyEvent.VK_D:
                key[0] = "d";
                break;
            case KeyEvent.VK_UP:
                key[1] = "w";
                break;
            case KeyEvent.VK_LEFT:
                key[1] = "a";
                break;
            case KeyEvent.VK_DOWN:
                key[1] = "s";
                break;
            case KeyEvent.VK_RIGHT:
                key[1] = "d";
                break;
        }
        if(activeMenu){
            choice();
            updateMenu();
        }
    }
}