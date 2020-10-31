package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;
import unsw.gloriaromanus.*;
import java.util.Random;

public class Province {
    private ArrayList<Infrastructure> infrastructures;
    private String name;
    private double wealth;
    private ArrayList<Smith> smithes;
    private Faction owner;
    private Double taxRate;
    private ArrayList<Troop> troops = new ArrayList<Troop>();
    private ArrayList<Troop> Enermy_troops = new ArrayList<Troop>();
    private int numTown;
    private boolean engaged;

    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;

    public Province(String name, Faction owner, int wealth, Double tax) {
        this.name = name;
        this.owner = owner;
        this.wealth = wealth;
        this.taxRate = tax; 
        this.smithes = new ArrayList<Smith>();
        //this.buildings = new ArrayList<Building>();
        this.troops = new ArrayList<Troop>(); 
        this.Enermy_troops = new ArrayList<Troop>();
        this.numTown = 0;
        this.engaged = false;
    }

    public void set_engaged(boolean i) {
        this.engaged = i;
    }

    public boolean get_engaged() {
        return this.engaged;
    }

    public void addTown(){
        numTown+=1;
    }
    public int getNumTown(){
        return numTown;
    }

    public double getWealth(){
        return wealth;
    }

    public void solicitTownWealth(){
        if (taxRate == 0.1){
            wealth += (10*numTown);
        }
        if (taxRate == 0.2){
            wealth -= (10*numTown);
        }
        if (taxRate == 0.25){
            wealth -= (30*numTown);
            for (Troop t: troops){
                t.decreaseMorale();
            }
        }
        if (this.wealth < 0){
            this.wealth = 0;
        }
    }

    public double tributeTax(){
        double tax = (taxRate*wealth);
        wealth = wealth - (taxRate*wealth);
        return tax;
    }


    //recuit soldiers
    public void recuit(String type, String name) {
        soldier soldier = new soldier(this.owner, type, name, this.name);
        
    }

    // add ablities to soldiers
    public void ablility_add() {
        double my_num = 0;
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                my_num = my_num + unit.getNumSoldiers();
            }
        }

        double enermy_num = 0;
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
        Random rand = new Random();
        ArrayList<Troop> troops = this.troops;
        int randomIndex = rand.nextInt(troops.size());
        Troop randomElement = troops.get(randomIndex);
        
        Random rand2 = new Random();
        ArrayList<Unit> units = randomElement.get_soldiers();
        int randomIndex2 = rand2.nextInt(units.size());
        Unit randomElement2 = units.get(randomIndex2);
        randomElement2.set_blood(randomElement2.get_blood() - bonus.get_damage());

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

    public String battle() {
        String win = "draw";
        int count = 0;
        while (this.troops.isEmpty() || this.Enermy_troops.isEmpty()) {
            Random rand = new Random();
            ArrayList<Troop> troops = this.troops;
            int randomIndex = rand.nextInt(troops.size());
            Troop randomElement = troops.get(randomIndex);

            Random rand2 = new Random();
            ArrayList<Unit> units = randomElement.get_soldiers();
            int randomIndex2 = rand2.nextInt(units.size());
            Unit my_randomUnit = units.get(randomIndex2);

            Random enermy_rand = new Random();
            ArrayList<Troop> enermy_troops = this.Enermy_troops;
            int randomIndex3 = enermy_rand.nextInt(enermy_troops.size());
            Troop randomElement3 = troops.get(randomIndex3);

            Random enermy_rand2 = new Random();
            ArrayList<Unit> enermy_units = randomElement3.get_soldiers();
            int randomIndex4 = enermy_rand2.nextInt(enermy_units.size());
            Unit enermy_randomUnit = units.get(randomIndex4);

            String engagement = "";

            double melee_speed = 0;
            double missile_speed = 0; 
            
            if (unit_type(my_randomUnit).equals("melee") && unit_type(enermy_randomUnit).equals("melee")) {
                engagement = "melee";
            } else if (unit_type(my_randomUnit).equals("missile") && unit_type(enermy_randomUnit).equals("missile")) {
                engagement = "missile";
            } else {
                if (unit_type(my_randomUnit).equals("melee") && !(unit_type(enermy_randomUnit).equals("melee"))) {
                    melee_speed = my_randomUnit.getNumSoldiers();
                    missile_speed = enermy_randomUnit.getNumSoldiers();
                } else if (!(unit_type(my_randomUnit).equals("melee")) && (unit_type(enermy_randomUnit).equals("melee"))) {
                    melee_speed = enermy_randomUnit.getNumSoldiers();
                    missile_speed = my_randomUnit.getNumSoldiers();
                }

                Double probability = 50 + 0.1 * (melee_speed - missile_speed);

                Random rnd = new Random();
                int i = rnd.nextInt(100);

                if(i <= probability){
                    engagement = "melee";
                } else {
                    engagement = "missile";
                }

                
            }

            Double my_dead_num = 0.0;
            Double enermy_dead_num = 0.0;
            if (engagement == "melee") {
                if (unit_type(my_randomUnit).equals("melee")) {
                    enermy_dead_num = (0.1 * enermy_randomUnit.getNumSoldiers()) * (my_randomUnit.get_attack() / (enermy_randomUnit.get_armour() + enermy_randomUnit.get_shield() + enermy_randomUnit.get_defence())) * nextGaussian();
                } else if (unit_type(my_randomUnit).equals("missile")) {
                    enermy_dead_num = (0.1 * enermy_randomUnit.getNumSoldiers()) * (my_randomUnit.get_attack() / (enermy_randomUnit.get_armour() + enermy_randomUnit.get_shield())) * nextGaussian();
                }
                if (unit_type(enermy_randomUnit).equals("melee")) {
                    my_dead_num = (0.1 *my_randomUnit.getNumSoldiers()) * (enermy_randomUnit.get_attack() / (my_randomUnit.get_armour() + my_randomUnit.get_shield() + enermy_randomUnit.get_defence())) * nextGaussian();
                } else if (unit_type(enermy_randomUnit).equals("missile")) {
                    my_dead_num = (0.1 *my_randomUnit.getNumSoldiers()) * (enermy_randomUnit.get_attack() / (my_randomUnit.get_armour() + my_randomUnit.get_shield())) * nextGaussian();
                }
            }

            if (engagement == "missile") {
                if (unit_type(my_randomUnit).equals("melee")) {
                    enermy_dead_num = 0.0;
                } else if (unit_type(my_randomUnit).equals("missile")) {
                    enermy_dead_num = (0.1 * enermy_randomUnit.getNumSoldiers()) * (my_randomUnit.get_attack() / (enermy_randomUnit.get_armour() + enermy_randomUnit.get_shield())) * nextGaussian();
                }
                if (unit_type(enermy_randomUnit).equals("melee")) {
                    my_dead_num = 0.0;
                } else if (unit_type(enermy_randomUnit).equals("missile")) {
                    my_dead_num = (0.1 *my_randomUnit.getNumSoldiers()) * (enermy_randomUnit.get_attack() / (my_randomUnit.get_armour() + my_randomUnit.get_shield())) * nextGaussian();
                }
            }

            double my_left_soldiers = my_randomUnit.getNumSoldiers() - my_dead_num;
            if (my_left_soldiers <= 0) {
                randomElement.get_soldiers().remove(my_randomUnit);
            } else {
                my_randomUnit.setNumSoldiers(my_left_soldiers);;
            }

            double enermy_left_soldiers = enermy_randomUnit.getNumSoldiers() - enermy_dead_num;
            if (enermy_left_soldiers <= 0) {
                randomElement3.get_soldiers().remove(enermy_randomUnit);
            } else {
                my_randomUnit.setNumSoldiers(enermy_left_soldiers);
            }
            count++;
        }
        if (this.troops.isEmpty() && this.Enermy_troops.isEmpty()) {
            win = "draw";
        } else if (this.troops.isEmpty()) {
            win = "lose";
        } else if (this.Enermy_troops.isEmpty()) {
            win = "win";
        } 

        if (count == 200) {
            win = "draw";
        }
        return win;
    }

    public String unit_type(Unit unit) {
        return "";
    }

    public double nextGaussian() {
        if (haveNextNextGaussian) {
          haveNextNextGaussian = false;
          return nextNextGaussian;
        } else {
          double v1, v2, s;
          do {
            v1 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
            v2 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
            s = v1 * v1 + v2 * v2;
          } while (s >= 1 || s == 0);
          double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
          nextNextGaussian = v2 * multiplier;
          haveNextNextGaussian = true;
          return v1 * multiplier;
        }
      }
}