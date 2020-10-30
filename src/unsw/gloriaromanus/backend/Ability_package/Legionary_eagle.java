/*
package unsw.gloriaromanus.backend.Ability_package;

import unsw.gloriaromanus.backend.*;
import java.util.ArrayList;

public class Legionary_eagle extends Ability {
    private double morale_bonus = 1;
    private double morale_penalty = -0.2;

    @Override
    public void ability_add(soldier soldier){
        if (!soldier.get_name().equals("Roman")) {
            throw new ArithmeticException("Not Roman");
        }
        
        ArrayList<Ability> abilities = soldier.get_abilities();
        abilities.add(this);
        soldier.set_abilities(abilities);
        
        soldier.set_morale(soldier.get_morale() + this.morale_bonus);
    } 

    @Override
    public void ability_remove(soldier soldier){
        if (!soldier.get_name().equals("Roman")) {
            throw new ArithmeticException("Not Roman");
        }

        ArrayList<Ability> abilities = soldier.get_abilities();
        abilities.add(this);
        soldier.set_abilities(abilities);
        
        if ((soldier.get_morale() + this.morale_penalty) > 1) {
            soldier.set_morale(soldier.get_morale() + this.morale_penalty);
        } else {
            soldier.set_morale(1.0);
        }
    } 
}
*/