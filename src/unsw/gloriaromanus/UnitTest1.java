package unsw.gloriaromanus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

//import unsw.gloriaromanus.*;
import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import unsw.gloriaromanus.backend.*;

public class UnitTest1{
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    private String testfile = "{\r\n    \"heavy infantry\": {\r\n        \"numTroops\":5,\r\n        \"range\": 1,\r\n        \"armour\": 5,\r\n        \"morale\": 10,\r\n        \"speed\": 10,\r\n        \"attack\": 6,\r\n        \"defenseSkill\": 10,\r\n        \"shieldDefense\": 3, \r\n        \"movementpoints\": 10\r\n    },\r\n\r\n    \"skirmishers\": {\r\n        \"numTroops\":20,\r\n        \"range\": 1,\r\n        \"armour\": 6,\r\n        \"morale\": 20,\r\n        \"speed\": 7,\r\n        \"attack\": 8,\r\n        \"defenseSkill\": 8,\r\n        \"shieldDefense\": 4, \r\n        \"movementpoints\": 6\r\n    }\r\n}";
   
    @Test
    public void blahTest2(){
        Unit u = new Unit("heavy infantry");
        //System.out.println(u.getNumTroops());
        
        //Unit te = new Unit(testfile);
        //assertEquals(u.getNumTroops(), 5);
    }

    @Test
    public void RoadTest(){
        No_road x = new No_road();
        assertEquals(x.get_MovementPoints(), 4);

        Dirt_road y = new Dirt_road();
        assertEquals(y.get_MovementPoints(), 3);

        Paved_road z = new Paved_road();
        assertEquals(z.get_MovementPoints(), 2);

        Highway q = new Highway();
        assertEquals(q.get_MovementPoints(), 1);
    }

    @Test
    public void ablility_test1() {
        //test for ablility addition
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");
        
        Province province = new Province("Britannia", owner, 50, 2.2);
        
        Unit Roman_legionary = new Unit("legionary", "Roman", "Cavalry");
        Unit Roman_pikemen = new Unit("pikemen", "Roman", "Cavalry");
        Unit Gallic_berserker = new Unit("berserker", "Gallic", "Cavalry");

        Troop new_troop = new Troop(owner.get_name());
        Troop enermy_troop = new Troop(enermy.get_name());
        
        new_troop.get_soldiers().add(Roman_legionary);
        new_troop.get_soldiers().add(Roman_pikemen);
        
        enermy_troop.get_soldiers().add(Gallic_berserker);

        province.get_my_troops().add(new_troop);
        province.get_my_troops().add(enermy_troop);

        owner.getProvinces().add(province);
        
        //add ability
        province.ablility_add();
        province.enermy_ablility_add();

        //Roman can get morale plus 1, so it should be 6
        assertEquals(Roman_legionary.get_morale(), 6);
        //pikemen can have double armour, so it should be 10
        assertEquals(Roman_pikemen.get_armour(), 10);
        //pikemen can have half speed, so it should be 1
        assertEquals(Roman_pikemen.get_speed(), 1);

        //berseker get double attack and infinity morale and no armour, shield
        assertEquals(Gallic_berserker.get_attack(), 20);
        assertEquals(Gallic_berserker.get_armour(), 0);
        assertEquals(Gallic_berserker.get_shield(), 0);
        double POSITIVE_INFINITY = 1.0 / 0.0;
        assertEquals(Gallic_berserker.get_morale(), POSITIVE_INFINITY);

    }

    @Test
    public void battle_ability_test() {
        Faction owner = new Faction("Gallic");
        Province province = new Province("Britannia", owner, 50, 2.2);
        Unit Roman_melee = new Unit("pikemen");
        Roman_melee.set_faction(owner.get_name());
        Troop new_troop = new Troop(owner.get_name());
        new_troop.get_soldiers().add(Roman_melee);
        province.get_my_troops().add(new_troop);
        owner.getProvinces().add(province);
        Roman_melee.ability_add(false);
        
        //Roman can get morale plus 1, so it should be 6
        assertEquals(Roman_melee.get_morale(), 6);
        //pikemen can have double armour, so it should be 10
        assertEquals(Roman_melee.get_armour(), 10);
        //pikemen can have half speed, so it should be 1
        assertEquals(Roman_melee.get_speed(), 1);
        

    }
}

