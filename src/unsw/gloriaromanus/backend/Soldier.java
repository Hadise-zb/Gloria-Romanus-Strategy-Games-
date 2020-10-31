package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.util.List;


public class Soldier {
    //private Boolean mercenary;
    private Unit type;
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
    private Faction faction;
    private ArrayList<Ability> abilities;

    public Soldier (Faction faction, Unit type, String name, String province) {
        this.faction = faction;
        this.type = type;
        this.name = name;
        this.training_completed = false;
        this.turns = 0;
        this.province = province;
    }

    public double get_defence() {
        return this.defence;
    }

    public void set_defence(double new_defence) {
        this.defence = new_defence;
    }

    public Faction get_faction() {
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

}
