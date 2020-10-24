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

    public Province(String name, faction owner, Integer wealth, Double tax) {
        this.name = name;
        this.onwer = owner;
        this.wealth = wealth;
        this.tax = tax; 
        this.smithes = new ArrayList<Smith>();
        this.buildings = new ArrayList<Building>();       
    }

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
}