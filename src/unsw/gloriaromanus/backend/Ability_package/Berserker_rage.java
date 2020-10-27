package unsw.gloriaromanus.backend.Ability_package;

import unsw.gloriaromanus.backend.*;
import java.util.ArrayList;

public class Berserker_rage extends Ability {
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    @Override
    public void ability_add(soldier soldier){
        if (!(soldier.get_name().equals("Gallic") || soldier.get_name().equals("Celtic") || soldier.get_name().equals("Germanic"))) {
            throw new ArithmeticException("Can't add ablility");
        }

        soldier.set_morale(POSITIVE_INFINITY);
        soldier.set_attack((soldier.get_attack()) * 2);
        soldier.set_armed(false);

        ArrayList<Ability> abilities = soldier.get_abilities();
        abilities.add(this);
        soldier.set_abilities(abilities);
    }
}
