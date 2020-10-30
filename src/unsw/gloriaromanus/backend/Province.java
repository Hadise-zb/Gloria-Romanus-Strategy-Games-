package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import unsw.gloriaromanus.*;
import java.util.Random;

public class Province {
    private ArrayList<Infrastructure> infrastructures;
    private String name;
    private Integer wealth;
    private ArrayList<Smith> smithes;
    private faction owner;
    private Double tax;
    private ArrayList<Troop> troops;
    private ArrayList<Troop> Enermy_troops;

    public Province(String name, faction owner, Integer wealth, Double tax) {
        this.name = name;
        this.owner = owner;
        this.wealth = wealth;
        this.tax = tax; 
        this.smithes = new ArrayList<Smith>();
        //this.buildings = new ArrayList<Building>();
        this.troops = new ArrayList<Troop>(); 
        this.Enermy_troops = new ArrayList<Troop>();
    }

    //recuit soldiers
    public void recuit(String type, String name) {
        soldier soldier = new soldier(this.owner, type, name, this.name);
        
    }

    // add ablities to soldiers
    public void ablility_add() {
        int my_num = 0;
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                my_num = my_num + unit.getNumSoldiers();
            }
        }

        int enermy_num = 0;
        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                enermy_num = enermy_num + unit.getNumSoldiers();
            }
        }
        boolean heroic = false;
        if (my_num < (enermy_num / 2)) {
            heroic = true;
        }

        int Druid = 0;
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                Whole_army_bonus bonus = unit.ability_add(heroic);
                if (!bonus.get_name().equals("")) {
                    if (bonus.get_name().equals("Elephants_running_amok")) {
                        damage_inflited(bonus);
                    }
                }
                if (unit.get_name().equals("druid")) {
                    Druid += unit.getNumSoldiers();
                }

            }
        }

        if (Druid != 0) {
            Druidic_fervour(Druid);
        }

        boolean Cantabrian_circle_1 = false;
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                if (unit.get_name().equals("horse-archer")) {
                    Cantabrian_circle_1 = true;
                }
            }
        }

        //boolean Cantabrian_circle_2 = false;
        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                if (unit.get_name().equals("missile")) {
                    //Cantabrian_circle_2 = true;
                    if (Cantabrian_circle_1 == true) {
                        unit.setNumSoldiers(unit.getNumSoldiers() / 2);
                    }
                }
            }
        }



    }

    
    public void Druidic_fervour(int Druid) {
        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                if (Druid <= 5) {
                    unit.set_morale(unit.get_morale()*(1 + (Druid * 0.1)));
                } else {
                    unit.set_morale(unit.get_morale()*1.5);
                } 
            }
        }

        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                if (Druid <= 5) {
                    unit.set_morale(unit.get_morale()*(1 - (Druid * 0.05)));
                } else {
                    unit.set_morale(unit.get_morale()*0.75);
                } 
            }
        }
    }
    
    public void damage_inflited(Whole_army_bonus bonus) {
        for (Troop troop : troops) {
            //List<Integer> givenList = Arrays.asList(1, 2, 3);
            Random rand = new Random();
            ArrayList<Unit> units = troop.get_soldiers();
            int randomIndex = rand.nextInt(units.size());
            Unit randomElement = units.get(randomIndex);
            randomElement.set_blood(randomElement.get_blood() - bonus.get_damage());
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