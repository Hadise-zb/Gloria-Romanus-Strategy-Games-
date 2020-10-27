package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Province {
    private ArrayList<Infrastructure> infrastructures;
    private String name;
    private Integer wealth;
    private ArrayList<Smith> smithes;
    private faction onwer;
    private Double tax;
    private ArrayList<Troop> troops;

    public Province(String name, faction owner, Integer wealth, Double tax) {
        this.name = name;
        this.onwer = owner;
        this.wealth = wealth;
        this.tax = tax; 
        this.smithes = new ArrayList<Smith>();
        //this.buildings = new ArrayList<Building>();
        this.troops = new ArrayList<Troop>();    
    }

    // add ablities to soldiers
    public void ablility_add() {
        for (Troop troop : troops) {
            for (soldier soldier : troop.get_soldiers()) {                
                troop.ability_add(soldier);      
            }
        }
    }

    /*
    public ArrayList<Smith> getsmith() {
        return this.smithes;
    }

    public Integer getWealth() {
        int output = 0;
        for (Wealth wealth : this.wealths) {
            output = output * wealth.getPorportion();
            output = output + wealth.getProportion();
        }
        return output;
    }

    public Integer getTreasure() {
        return (int)this.getWealth()*this.tax;
    }
    
    public Boolean construct(Building building) {
        if (this.owner.getTreasure() < building.getcost()) {
            return false;
        }
        this.owner.speed(bulding.getcost());

        this.building.add(building);
    }
    */
}