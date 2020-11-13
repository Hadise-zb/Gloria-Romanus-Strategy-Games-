package unsw.gloriaromanus.backend;

import java.util.ArrayList;
//import java.util.List;
//import unsw.gloriaromanus.*;
import java.util.Random;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Province {
    //private ArrayList<Infrastructure> infrastructures;
    private String name;
    private double wealth;
    //private ArrayList<Smith> smithes;
    private Faction owner;
    private double taxRate;
    private ArrayList<Troop> troops = new ArrayList<Troop>();
    private ArrayList<Troop> Enermy_troops = new ArrayList<Troop>();
    private int numTown;
    private boolean engaged;
    private ArrayList<Unit> my_units = new ArrayList<Unit>();
    private ArrayList<Unit> enermy_units = new ArrayList<Unit>();


    // When the soldiers are trainned, they will be sent to the localtroop
    private ArrayList<Unit> units = new ArrayList<Unit>();

    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;

    private ArrayList<Unit> trainingSlots = new ArrayList<Unit>();
    
    public Faction getFaction(){
        return owner;
    }

    public ArrayList<Unit> get_units() {
        return this.my_units;
    }

    public boolean trainingSlot_available(){
        if (trainingSlots.size()<2) return true;
        else return false;
    }

    public void occupyTrainningSlot(Unit u){
        if (!trainingSlots.contains(u)) trainingSlots.add(u);
    }

    public void vocateTrainingSlot(Unit u){
        if (trainingSlots.contains(u)) trainingSlots.remove(u);    
    }


    public Province(String name, Faction owner, int wealth, Double tax) {
        this.name = name;
        this.owner = owner;
        this.wealth = wealth;
        this.taxRate = tax; 
        //this.smithes = new ArrayList<Smith>();
        this.troops = new ArrayList<Troop>(); 
        this.Enermy_troops = new ArrayList<Troop>();
        this.my_units = new ArrayList<Unit>();
        this.enermy_units = new ArrayList<Unit>();
        this.numTown = 0;
        this.engaged = false;
        owner.getProvinces().add(this);
    }

    public void adjustTaxRate(double rate){
        this.taxRate = rate;
    }

    public void addUnit(Unit u){
        this.units.add(u);
    }

    public ArrayList<Unit> getUnit(){
        return units;
    }


    public String get_name() {
        return this.name;
    }
   
    public ArrayList<Troop> get_my_troops() {
        return this.troops;
    }

    public void set_my_troops(ArrayList<Troop> new_troops) {
        this.troops = new_troops;
    }

    public ArrayList<Troop> get_enermy_troops() {
        return this.Enermy_troops;
    }

    public void set_enermy_troops(ArrayList<Troop> new_troops) {
        this.Enermy_troops = new_troops;
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

    // this function will be called in the begining of each turn
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


    // Each province can train at most two units of soldiers at the same time
    //recuit soldiers
    //public void recuit(String type, String name) {
        //Soldier soldier = new Soldier(this.owner, type, name, this.name);
        
    //}

    public void units_add() {
        
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                my_units.add(unit);
                //System.out.println(unit.get_name());
            }
        }

        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                enermy_units.add(unit);
                //System.out.println(unit.get_name());
            }
        }

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

        double Druid = 0.0;
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                Whole_army_bonus bonus = unit.ability_add(heroic);
                if (!bonus.get_name().equals("")) {
                    if (bonus.get_name().equals("Elephants_running_amok")) {
                        damage_inflited(bonus);
                    }
                }
                
                if (unit.get_name().equals("druid")) {
                    Druid = Druid + unit.getNumSoldiers();
                }

            }
        }

        if (Druid != 0.0) {
            Druidic_fervour(Druid);
        }
    }

    public void enermy_ablility_add() {
        double my_num = 0;
        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                my_num = my_num + unit.getNumSoldiers();
            }
        }

        double enermy_num = 0;
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                enermy_num = enermy_num + unit.getNumSoldiers();
            }
        }
        boolean heroic = false;
        if (my_num < (enermy_num / 2)) {
            heroic = true;
        }

        Double Druid = 0.0;
        for (Troop troop : Enermy_troops) {
            for (Unit unit : troop.get_soldiers()) {
                Whole_army_bonus bonus = unit.ability_add(heroic);
                if (!bonus.get_name().equals("")) {
                    if (bonus.get_name().equals("Elephants_running_amok")) {
                        damage_inflited(bonus);
                    }
                }
                
                if (unit.get_name().equals("druid")) {
                    Druid = Druid + unit.getNumSoldiers();
                }

            }
        }
        if (Druid != 0.0) {
            Druidic_fervour(Druid);
        }
    }

    
    public void Druidic_fervour(Double Druid) {
        for (Troop troop : troops) {
            for (Unit unit : troop.get_soldiers()) {
                if (Druid <= 5) {
                    unit.set_morale(unit.get_morale()*(1.0 + (Druid * 0.1)));
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
    
    


    public String battle(Faction me, Faction enermy) {
        String win = "draw";
        int count = 0;

        this.units_add();

        // add ability to units before battle, some ability will be added during battle
        this.ablility_add();
        this.enermy_ablility_add();

        
        while (!this.my_units.isEmpty() || !this.enermy_units.isEmpty()) {
            
            Random rand = new Random();
            ArrayList<Unit> m_units = this.my_units;
            int randomIndex = rand.nextInt(m_units.size());
            Unit my_randomUnit = m_units.get(randomIndex);

            Random rand2 = new Random();
            ArrayList<Unit> e_units = this.enermy_units;
            int randomIndex2 = rand2.nextInt(e_units.size());
            Unit enermy_randomUnit = m_units.get(randomIndex2);
            

            //set numbers of engagement
            my_randomUnit.set_num_engagements(my_randomUnit.get_num_engagements() + 1);
            enermy_randomUnit.set_num_engagements(enermy_randomUnit.get_num_engagements() + 1);
            
            //set ablility for engagement
            if (my_randomUnit.get_name().equals("melee infantry")) {
                if (my_randomUnit.get_num_engagements() == 4) {
                    my_randomUnit.set_attack(my_randomUnit.get_attack()+my_randomUnit.get_shield());
                }
            } else if (my_randomUnit.get_name().equals("javelin skirmisher")) {
                enermy_randomUnit.set_armour(enermy_randomUnit.get_armour() / 2);
            } else if (my_randomUnit.get_name().equals("horse archer") && unit_type(my_randomUnit).equals("missile")) {
                enermy_randomUnit.setNumSoldiers(enermy_randomUnit.getNumSoldiers() / 2);
            }

            if (enermy_randomUnit.get_name().equals("melee infantry")) {
                if (enermy_randomUnit.get_num_engagements() == 4) {
                    enermy_randomUnit.set_attack(enermy_randomUnit.get_attack()+enermy_randomUnit.get_shield());
                } 
            } else if (enermy_randomUnit.get_name().equals("javelin skirmisher")) {
                my_randomUnit.set_armour(my_randomUnit.get_armour() / 2);
            } else if (enermy_randomUnit.get_name().equals("horse archer") && unit_type(my_randomUnit).equals("missile")) {
                my_randomUnit.setNumSoldiers(my_randomUnit.getNumSoldiers() / 2);
            }

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

                //The base-level chance of engagement to be a melee engagement 
                //(where the engagement has both a melee and missile unit) is increased by 10% x (speed of melee unit - speed of missile unit) (value of this formula can be negative)
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
                    enermy_dead_num = (0.1 * enermy_randomUnit.getNumSoldiers()) * (my_randomUnit.get_attack() / (enermy_randomUnit.get_armour() + enermy_randomUnit.get_shield() + enermy_randomUnit.get_defence())) * (nextGaussian() + 1);
                } else if (unit_type(my_randomUnit).equals("missile")) {
                    enermy_dead_num = (0.1 * enermy_randomUnit.getNumSoldiers()) * (my_randomUnit.get_attack() / (enermy_randomUnit.get_armour() + enermy_randomUnit.get_shield())) * (nextGaussian() + 1);
                }
                if (unit_type(enermy_randomUnit).equals("melee")) {
                    my_dead_num = (0.1 *my_randomUnit.getNumSoldiers()) * (enermy_randomUnit.get_attack() / (my_randomUnit.get_armour() + my_randomUnit.get_shield() + enermy_randomUnit.get_defence())) * (nextGaussian() + 1);
                } else if (unit_type(enermy_randomUnit).equals("missile")) {
                    my_dead_num = (0.1 *my_randomUnit.getNumSoldiers()) * (enermy_randomUnit.get_attack() / (my_randomUnit.get_armour() + my_randomUnit.get_shield())) * (nextGaussian() + 1);
                }
            }

            if (engagement == "missile") {
                if (unit_type(my_randomUnit).equals("melee")) {
                    enermy_dead_num = 0.0;
                } else if (unit_type(my_randomUnit).equals("missile")) {
                    enermy_dead_num = (0.1 * enermy_randomUnit.getNumSoldiers()) * (my_randomUnit.get_attack() / (enermy_randomUnit.get_armour() + enermy_randomUnit.get_shield())) * (nextGaussian() + 1);
                }
                if (unit_type(enermy_randomUnit).equals("melee")) {
                    my_dead_num = 0.0;
                } else if (unit_type(enermy_randomUnit).equals("missile")) {
                    my_dead_num = (0.1 *my_randomUnit.getNumSoldiers()) * (enermy_randomUnit.get_attack() / (my_randomUnit.get_armour() + my_randomUnit.get_shield())) * (nextGaussian() + 1);
                }
            }

            double my_left_soldiers = my_randomUnit.getNumSoldiers() - my_dead_num;
            //System.out.println("my"+ my_left_soldiers);
            BigDecimal bd1 = new BigDecimal(my_left_soldiers).setScale(0, RoundingMode.HALF_UP);
            double num1 = bd1.doubleValue();
            //System.out.println("my"+ num1);
            if (num1 <= 0.0) {
                my_units.remove(my_randomUnit);
            } else {
                my_randomUnit.setNumSoldiers(my_left_soldiers);;
            }

            double enermy_left_soldiers = enermy_randomUnit.getNumSoldiers() - enermy_dead_num;
            //System.out.println("en"+ enermy_left_soldiers);
            BigDecimal bd2 = new BigDecimal(enermy_left_soldiers).setScale(0, RoundingMode.HALF_UP);
            double num2 = bd2.doubleValue();
            //System.out.println("my"+ num2);
            if (num2 <= 0.0) {
                enermy_units.remove(enermy_randomUnit);
            } else {
                my_randomUnit.setNumSoldiers(enermy_left_soldiers);
            }
            
            count++;

            if (this.my_units.isEmpty() && this.enermy_units.isEmpty()) {
                win = "draw";
                troops_return_back(this); 
                return win;
            } else if (this.my_units.isEmpty()) {
                win = "lose";
                System.out.println("en");
                return win;
            } else if (this.enermy_units.isEmpty()) {
                win = "win";
                this.owner.getProvinces().remove(this);
                this.owner = me;
                return win;
            } 
    
            if (count == 200) {
                win = "draw";
                return win;
            }
            
        }
        return win;
        
    }

    public void troops_return_back(Province province) {
        for (Troop t : province.get_my_troops()) {
            t.get_province().get_my_troops().add(t);
        }
    }

    public String unit_type(Unit unit) {
        if (unit.get_name().equals("artillery") || unit.get_name().equals("horse archers") || unit.get_name().equals("missile infantry")) {
            return "missile";
        } else {
            return "melee";
        }
    }

    public double nextGaussian() {
        Random rand = new Random();
        if (haveNextNextGaussian) {
          haveNextNextGaussian = false;
          return nextNextGaussian;
        } else {
          double v1, v2, s;
          do {
            v1 = 2 * rand.nextDouble() - 1;   // between -1.0 and 1.0
            v2 = 2 * rand.nextDouble() - 1;   // between -1.0 and 1.0
            s = v1 * v1 + v2 * v2;
          } while (s >= 1 || s == 0);
          double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
          nextNextGaussian = v2 * multiplier;
          haveNextNextGaussian = true;
          return v1 * multiplier;
        }
    }

    
}