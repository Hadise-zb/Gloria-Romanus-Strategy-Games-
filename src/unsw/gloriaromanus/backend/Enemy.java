package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Enemy {
    private faction faction;
    private ArrayList<faction> observers;
    private ArrayList<Enemy> subscriber;

    public void update() {
        int spendBuilding = faction.getTreasure() / 2;
        while (speedBuilding > 0) {
            Province.build();
            spendBuilding -= cost;
        }
        //speedn on miltery
        //move troop
        //invade province
        this.sendMessageToSubscriber();
        return;
    }
    //design Observer design patter
    //province, soildier in province
    public void Obsever() {
        for (faction faction: this.observers) {
            faction.getInformation();
        }
    }

    public void sendMessageToSubscriber() {
        for (Enemy enemy : this.subscriber) {
            enemy.Observer();
        }
    }
    

}

