package unsw.gloriaromanus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
        //Unit u = new Unit("heavy infantry");
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
        Unit Roman_melee_cavalry = new Unit("melee cavalry", "Roman", "Cavalry");
        Unit druid = new Unit("druid", "Roman", "Cavalry");
        Unit Gallic_berserker = new Unit("berserker", "Gallic", "Cavalry");
        Unit Roman_elephant = new Unit("elephant", "Gallic", "Cavalry");
        
        // set enermy 50 numbers, which is higher than the double of my armies
        Gallic_berserker.setNumSoldiers(50);

        Troop new_troop = new Troop(owner.get_name());
        Troop enermy_troop = new Troop(enermy.get_name());
        
        new_troop.get_soldiers().add(Roman_legionary);
        new_troop.get_soldiers().add(Roman_pikemen);
        new_troop.get_soldiers().add(Roman_melee_cavalry);
        new_troop.get_soldiers().add(Roman_elephant);
    
        enermy_troop.get_soldiers().add(Gallic_berserker);

        province.get_my_troops().add(new_troop);
        province.get_enermy_troops().add(enermy_troop);

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

        // less than the half of enermy, charge double and morale get *1.5
        assertEquals(Roman_melee_cavalry.get_charge(), 10);
        assertEquals(Roman_melee_cavalry.get_morale(), 7.5);

        new_troop.get_soldiers().add(druid);
        assertEquals(Roman_legionary.get_morale(), 6.6);
        //assertEquals(Gallic_berserker.get_attack(), 20);

    }

    @Test
    public void battle_ability_test() {
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");
        
        Province province = new Province("Britannia", owner, 50, 2.2);
        
        Unit Cantabrian = new Unit("horse-archer", "Roman", "Cavalry");
        Unit Druidic = new Unit("Druidic fervour", "Roman", "Cavalry");
        //Unit Roman_melee_cavalry = new Unit("melee cavalry", "Roman", "Cavalry");
        Unit Gallic_berserker = new Unit("Shield charge", "Gallic", "Cavalry");
        Unit Roman_elephant = new Unit("Shield charge", "Gallic", "Cavalry");
        
        // set enermy 50 numbers, which is higher than the double of my armies
        Gallic_berserker.setNumSoldiers(50);

        Troop new_troop = new Troop(owner.get_name());
        Troop enermy_troop = new Troop(enermy.get_name());
        
        //new_troop.get_soldiers().add(Roman_legionary);
        //new_troop.get_soldiers().add(Roman_pikemen);
        //new_troop.get_soldiers().add(Roman_melee_cavalry);
        new_troop.get_soldiers().add(Roman_elephant);
        
        enermy_troop.get_soldiers().add(Gallic_berserker);

        province.get_my_troops().add(new_troop);
        province.get_enermy_troops().add(enermy_troop);

        owner.getProvinces().add(province);
        

    }
    /*
    public static void main(String[] arg) {
        //test for ablility addition
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");
        
        Province province = new Province("Britannia", owner, 50, 2.2);
        
        Unit Roman_legionary = new Unit("legionary", "Roman", "Cavalry");
        Unit Roman_pikemen = new Unit("pikemen", "Roman", "Cavalry");
        Unit Roman_melee_cavalry = new Unit("melee cavalry", "Roman", "Cavalry");
        Unit druid = new Unit("druid", "Roman", "Cavalry");
        Unit Gallic_berserker = new Unit("berserker", "Gallic", "Cavalry");
        Unit Roman_elephant = new Unit("elephant", "Gallic", "Cavalry");
        
        // set enermy 50 numbers, which is higher than the double of my armies
        Gallic_berserker.setNumSoldiers(50);
        druid.setNumSoldiers(1);

        Troop new_troop = new Troop(owner.get_name());
        Troop enermy_troop = new Troop(enermy.get_name());
        
        new_troop.get_soldiers().add(Roman_legionary);
        new_troop.get_soldiers().add(Roman_pikemen);
        new_troop.get_soldiers().add(Roman_melee_cavalry);
        new_troop.get_soldiers().add(Roman_elephant);
        
    
        enermy_troop.get_soldiers().add(Gallic_berserker);

        province.get_my_troops().add(new_troop);
        province.get_enermy_troops().add(enermy_troop);

        owner.getProvinces().add(province);
        
        //add ability
        province.ablility_add();
        province.enermy_ablility_add();

        //Roman can get morale plus 1, so it should be 6
        assertEquals(Roman_legionary.get_morale(), 6.0);
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

        // less than the half of enermy, charge double and morale get *1.5
        assertEquals(Roman_melee_cavalry.get_charge(), 10);
        assertEquals(Roman_melee_cavalry.get_morale(), 7.5);


        //Now add druid in new_troop, 
        new_troop.get_soldiers().add(druid);
        province.ablility_add();

        BigDecimal bd = new BigDecimal(Roman_legionary.get_morale()).setScale(2, RoundingMode.HALF_UP);
        double improved_morale = bd.doubleValue();
        assertEquals(improved_morale, 7.7);
    }
    */

    public static void main(String[] arg) {
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");
        
        Province province = new Province("Britannia", owner, 50, 2.2);
        
        Unit horse_archer = new Unit("horse archer", "Roman", "Cavalry");
        //Unit Druidic = new Unit("Druidic fervour", "Roman", "Cavalry");
        //Unit Roman_melee_cavalry = new Unit("melee cavalry", "Roman", "Cavalry");
        Unit melee_infantry = new Unit("melee infantry", "Gallic", "Cavalry");
        //Unit Roman_elephant = new Unit("Shield charge", "Gallic", "Cavalry");
        
        // set enermy 50 numbers, which is higher than the double of my armies
        horse_archer.setNumSoldiers(10);
        melee_infantry.setNumSoldiers(10);

        Troop new_troop = new Troop(owner.get_name());
        Troop enermy_troop = new Troop(enermy.get_name());
        
        new_troop.get_soldiers().add(horse_archer);
        enermy_troop.get_soldiers().add(melee_infantry);
        //new_troop.get_soldiers().add(Roman_melee_cavalry);

        province.get_my_troops().add(new_troop);
        province.get_enermy_troops().add(enermy_troop);

        owner.getProvinces().add(province);

        String result = province.battle();

        assertEquals(result, "lose"); 
        //System.out.println(result);

    }
}

