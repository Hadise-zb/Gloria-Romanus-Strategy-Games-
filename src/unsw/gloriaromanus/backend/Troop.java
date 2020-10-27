package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class Troop {
    private String faction;
    private String name;
    private ArrayList<soldier> soldiers;
    private Province current_province;
    
    public void ability_add(soldier soldier){
        if (this.faction.equals("Gallic") || this.faction.equals("Celtic") || this.faction.equals("Germanic")) {
            Ability ability = new Ability();
            ability.set_name("Berserker rage");
            ability.ability_add(soldier);
        } else if (this.faction.equals("Roman")) {
            Ability ability = new Ability();
            ability.set_name("Legionary eagle");
            ability.ability_add(soldier);
        }
        
        if (this.name.equals("pikemen") || this.name.equals("hoplite")) {
            Ability ability = new Ability();
            ability.set_name("Phalanx");
            ability.ability_add(soldier);
        } else if (this.name.equals("javelin-skirmisher")) {
            Ability ability = new Ability();
            ability.set_name("skirmisher anti-armour");
            ability.ability_add(soldier);
        } else if (this.name.equals("melee cavalry")) {

        } else if () {
            
        }
        
        //ArrayList<Ability> abilities = soldier.get_abilities();
        //abilities.add(this);
        //soldier.set_abilities(abilities);
    }

    public void move(Province destination) {
        
    }

    public Province get_province() {
        return this.current_province;
    }

    public ArrayList<soldier> get_soldiers() {
        return this.soldiers;
    }
}

