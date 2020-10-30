package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;


public class soldier {
    //private Boolean mercenary;
    private String type;
    private Boolean armed;
    private Troop troop;
    private String name;
    private Double morale;
    private Double attack;
    private Double defence;
    private Integer shield;
    private Integer speed;
    private boolean training_completed;
    private int turns;
    private String province;
    private int cost;
    private faction faction;
    private ArrayList<Ability> abilities;

    public soldier (faction faction, String type, String name, String province) {
        this.faction = faction;
        this.type = type;
        this.name = name;
        //this.mercenary = mercenary;
        //this.name = name;
        //this.morale = 1.0;
        //this.speed = speed;
        //this.attack = attack;
        //this.shield = shield;
        //this.armed = armed;
        this.training_completed = false;
        this.turns = 0;
        this.province = province;
        //this.cost = 0;
        //this.abilities = new ArrayList<Ability>();
    }

    public double get_defence() {
        return this.defence;
    }

    public void set_defence(double new_defence) {
        this.defence = new_defence;
    }

    public faction get_faction() {
        return this.faction;
    }

    public void set_armed(boolean i) {
        this.armed = i;
    }

    public Double get_attack() {
        return this.attack;
    }

    public void set_attack(Double new_attack) {
        this.attack = new_attack;
    }

    public Double get_morale() {
        return this.morale;
    }

    public void set_morale(Double morale) {
        this.morale = morale;
    }

    public ArrayList<Ability> get_abilities() {
        return this.abilities;
    }

    public void set_abilities(ArrayList<Ability> new_abilities) {
        this.abilities = new_abilities;
    }

    public String get_name() {
        return this.name;
    }

    public int get_speed() {
        return this.speed;
    }

    public void set_speed(int new_speed) {
        this.speed = new_speed;
    }

    public void complete_training() {
        if (this.turns == 3) {
            this.training_completed = true;
        }

    }


    /*
    public Double get_speed2(){
        Double speed = this.speed/1.0;
        Province province = this.troop.get_province();
        ArrayList<Smith> smiths = province.getsmith();
        for (Smith smith : smiths) {
            if (smith.getPrortionSpeed() != -1) {
                speed = speed*smith.getPrortionSpeed();
            } else if (smith.getScalarsSpeed() != -1) {
                speed += smith.getScalarsSpeed();
            }
        }
        
        //Integer output = (int) speed; 
        return speed;
    }
    /*
    public Integer get_attack2() {
        Double attack = this.attack/1.0;
       

    }*/
}