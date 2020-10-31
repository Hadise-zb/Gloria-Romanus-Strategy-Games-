package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class System {
    private ArrayList<Province> provinces;
    private int turn;
    private int treasure;

    /*
    public void update() {
        for (Province province: this.provinces) {
            this.treasure += province.getTreasure();
            faction.update();
        }
        
    }
    */

    public System(){
        this.turn = 0;
    }

    public void startTurn(){
        turn +=1;
    }

    public void endTurn(){
        
    }
    
}