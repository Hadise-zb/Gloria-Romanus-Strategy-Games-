package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import java.lang.Object;
import java.util.Random;

public class Ability {
    private String name;

    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    public void ability(String name) {
        this.name = name;
    }
    
    public String get_name() {
        return this.name;
    }

    public void set_name(String name) {
        this.name = name;
    }
    
    public Whole_army_bonus ability_add(Unit soldier){
        Whole_army_bonus bonus = new Whole_army_bonus();
        if (this.name.equals("Berserker rage")) {
            //throw new ArithmeticException("Can't add ablility");
            Berserker_rage_add(soldier);
        } else if (this.name.equals("Legionary eagle")) {
            //throw new ArithmeticException("Not Roman");
            Legionary_eagle_add(soldier);
        } else if (this.name.equals("Phalanx")) {
            Phalanx_add(soldier);
        } else if (this.name.equals("Heroic charge")) {
            Heroic_charge(soldier);
        } else if (this.name.equals("skirmisher anti-armour")) {
            //skirmisher_add(soldier);
        } else if (this.name.equals("Elephants running amok")) {
            bonus = Elephants_running_amok_add(soldier);
        } 
        /*
        else if (this.name.equals("Druidic fervour")) {
            bonus.set_name("Druidic fervour"); 
        }
        */
        //ArrayList<Ability> abilities = soldier.get_abilities();
        //abilities.add(this);
        //soldier.set_abilities(abilities);

        return bonus;
    } 

    public void ability_remove(Unit soldier){
        ArrayList<Ability> abilities = soldier.get_abilities();
        abilities.remove(this);
        soldier.set_abilities(abilities);
    }

    /*
    public void skirmisher_add(Unit soldier) {
        soldier.set_armour((soldier.get_armour()) / 2);
    }
    */

    public void Phalanx_add(Unit soldier) {
        soldier.set_armour((soldier.get_armour()) * 2);
        soldier.set_speed((soldier.get_speed()) / 2);
    }

    public void Berserker_rage_add(Unit soldier){      
        soldier.set_morale(POSITIVE_INFINITY);
        soldier.set_attack((soldier.get_attack()) * 2);
        soldier.set_armed(false);
        soldier.set_armour(0.0);
        soldier.set_shield(0.0);  
    }

    public void Legionary_eagle_add(Unit soldier){
        
        soldier.set_morale(soldier.get_morale() + 1);        
    } 

    public void Legionary_eagle_remove(Unit soldier){
        
        if ((soldier.get_morale() - 0.2) > 1) {
            soldier.set_morale(soldier.get_morale() - 0.2);
        } else {
            soldier.set_morale(1.0);
        }
        
    }

    public void Heroic_charge(Unit soldier) {
        soldier.set_attack(soldier.get_attack() * 2);
        soldier.set_morale(soldier.get_morale() * 1.5);
    }
    public Whole_army_bonus Elephants_running_amok_add(Unit soldier) {
        Whole_army_bonus bonus = new Whole_army_bonus();
        Random rnd = new Random();
        int i = rnd.nextInt(100);

        if(i <= 5){
            bonus.set_name("Elephants_running_amok");
            bonus.set_damage(soldier.get_attack());
        }

        return bonus;
    }

}
