package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;

import unsw.gloriaromanus.*;
import unsw.gloriaromanus.backend.*;
//import org.json.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import unsw.gloriaromanus.backend.Systemcontrol;
//import unsw.gloriaromanus.backend.Systemcontrol;
import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class UnitTest{
       
    @Test
    public void blahTest2(){
        Unit u = new Unit("heavy infantry", "AUSTRALIA", "Cavalry");
        //System.out.println(u.getNumTroops());
        assertEquals(u.getNumSoldiers(), 10);
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

    /*
    Initial setup of each faction
    Treasure: 50;
    Wealth: 0;
    */
    @Test
    public void wealthTest_esssential(){
        // Test for essential methods in wealth and tax part
        Faction au = new Faction("AUSTRALIA");
        Province p = new Province("NSW", au, 0, 0.1);
        au.addProvince(p);
        p.addTown();
        p.addTown();
        assertEquals(p.getNumTown(), 3);

        p.solicitTownWealth();
        assertEquals(p.getWealth(), 20);

        au.solicitTax();
        assertEquals(au.getTreasure(), 52);
    }

    @Test
    public void wealthTest(){

        // Test for Low tax rate 10%
        Faction my_faction = new Faction("AUSTRALIA");
        Faction enermy_faction = new Faction("Rome");
        Systemcontrol testSystem = new Systemcontrol(my_faction, enermy_faction);
        Province p = new Province("NSW", my_faction, 0, 0.1);
        testSystem.attach(my_faction);
        my_faction.addProvince(p);

        p.addTown();
        p.addTown();
        assertEquals(my_faction.getWealth(), 0);
        assertEquals(my_faction.getTreasure(), 50);

        testSystem.endTurn();

        assertEquals(my_faction.getWealth(), 87.966);
        assertEquals(my_faction.getTreasure(), 52);

        testSystem.endTurn();

        assertEquals(my_faction.getWealth(), 34.2);
        assertEquals(my_faction.getTreasure(), 55.8);

        testSystem.endTurn();
        assertEquals(my_faction.getWealth(), 48.78);
        assertEquals(my_faction.getTreasure(), 61.22);

        // Test for normal tax rate 15%
        // Now change the tax rate of province p to normal
        p.adjustTaxRate(0.15);
    
        testSystem.endTurn();
        assertEquals(my_faction.getWealth(), 41.463);
        assertEquals(my_faction.getTreasure(), 68.537);

        testSystem.endTurn();
        assertEquals(Math.round(my_faction.getWealth()), 35); // Eaxt number is 35.24355
        assertEquals(Math.round(my_faction.getTreasure()), 75);// Eaxt number is 74.75645


        // Test for High tax 20%
        // Now change the tax rate of province p to High
        p.adjustTaxRate(0.2);
        testSystem.endTurn();
        assertEquals(Math.round(my_faction.getWealth()), 12); // 12.19484
        assertEquals(Math.round(my_faction.getTreasure()), 78); // 78.04871

        testSystem.endTurn();
        assertEquals(Math.round(my_faction.getWealth()), 0); // 0
        assertEquals(Math.round(my_faction.getTreasure()), 78); // 78.04871

        // Adjust back to low tax to accumulate wealth
        p.adjustTaxRate(0.1);
        testSystem.endTurn();
        testSystem.endTurn();
        testSystem.endTurn();
        testSystem.endTurn();

        assertEquals(my_faction.getWealth(), 61.902);
        assertEquals(Math.round(my_faction.getTreasure()), 96); // 83.62871

        // Test for Very high tax 30%
        p.adjustTaxRate(0.3);
        

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

        Troop new_troop = new Troop(owner, province);
        Troop enermy_troop = new Troop(enermy, province);
        
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
        assertEquals(Roman_legionary.get_morale(), 4);
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
    public void recruitTest(){
        Faction my_faction = new Faction("AUSTRALIA");
        Faction enermy_faction = new Faction("Rome");
        Systemcontrol testSystem = new Systemcontrol(my_faction, enermy_faction);
        Province p = new Province("NSW", my_faction, 0, 0.1);
        testSystem.attach(my_faction);
        my_faction.addProvince(p);

        Unit new_unit = new Unit("legionary", "AUSTRALIA", "Cavalry");
        my_faction.requestTraining(p, new_unit, 5, testSystem.getTurn());
        
        assertEquals(p.getUnit().size(), 0);
        testSystem.endTurn();
        assertEquals(p.getUnit().get(0), new_unit);

        Unit second_unit = new Unit("legionary", "AUSTRALIA", "Cavalry");

    }

    @Test
    public void battleResolverTest(){
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");
        
        Province province = new Province("Britannia", owner, 50, 2.2);
        
        Unit horse_archer = new Unit("horse archer", "Roman", "Cavalry");
        
        Unit melee_infantry = new Unit("melee infantry", "Gallic", "Cavalry");
        //Unit Roman_elephant = new Unit("Shield charge", "Gallic", "Cavalry");
        
        // set the same numbers, but melee_infantry is stronger, so I will lose this battle
        horse_archer.setNumSoldiers(10);
        melee_infantry.setNumSoldiers(10);

        Troop new_troop = new Troop(owner, province);
        Troop enermy_troop = new Troop(enermy, province);
        
        new_troop.get_soldiers().add(horse_archer);
        enermy_troop.get_soldiers().add(melee_infantry);
    
        province.get_my_troops().add(new_troop);
        province.get_enermy_troops().add(enermy_troop);

        owner.getProvinces().add(province);

        String result = province.battle(owner, enermy);

        assertEquals(result, "lose"); 
    }

    @Test
    public void testAbility(){
        //test for ablility addition
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");
        
        Province province = new Province("Britannia", owner, 50, 2.2);
        
        Unit Roman_legionary = new Unit("legionary", "Roman", "Cavalry");
        Unit Roman_pikemen = new Unit("pikemen", "Roman", "Cavalry");
        Unit Roman_melee_cavalry = new Unit("melee cavalry", "Roman", "Cavalry");
        Unit druid = new Unit("druid", "Roman", "Cavalry");
        Unit Gallic_berserker = new Unit("berserker", "Gallic", "Cavalry");
        // add more elephant to increase the probability.
        Unit Roman_elephant = new Unit("elephant", "Gallic", "Cavalry");
        Unit Roman_elephant2 = new Unit("elephant", "Gallic", "Cavalry");
        Unit Roman_elephant3 = new Unit("elephant", "Gallic", "Cavalry");
        Unit Roman_elephant4 = new Unit("elephant", "Gallic", "Cavalry");
        
        // set enermy 50 numbers, which is higher than the double of my armies
        Gallic_berserker.setNumSoldiers(50);
        druid.setNumSoldiers(1);

        Troop new_troop = new Troop(owner, province);
        Troop enermy_troop = new Troop(enermy, province);
        
        new_troop.get_soldiers().add(Roman_legionary);
        new_troop.get_soldiers().add(Roman_pikemen);
        new_troop.get_soldiers().add(Roman_melee_cavalry);
        new_troop.get_soldiers().add(Roman_elephant);
        new_troop.get_soldiers().add(Roman_elephant2);
        new_troop.get_soldiers().add(Roman_elephant3);
        new_troop.get_soldiers().add(Roman_elephant4);
        
    
        enermy_troop.get_soldiers().add(Gallic_berserker);

        province.get_my_troops().add(new_troop);
        province.get_enermy_troops().add(enermy_troop);

        owner.getProvinces().add(province);
        
        //add ability
        province.ablility_add();
        province.enermy_ablility_add();

        //Roman can get morale plus 1, so it should be 6
        assertEquals(Roman_legionary.get_morale(), 4.0);
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

    @Test
    public void testmovement(){
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");

        Systemcontrol system = new Systemcontrol(owner, enermy);
        system.set_enermy(enermy);

        Province my_province = new Province("Britannia", owner, 50, 2.2);
        Province enermy_province = new Province("Belgica", owner, 50, 2.2);
        Province enermy_province2 = new Province("Lugdunensis", owner, 50, 2.2);

        Troop new_troop = new Troop(owner, my_province);
        Troop new_troop2 = new Troop(owner, my_province);
        
        Unit Roman_legionary = new Unit("legionary", "Roman", "Cavalry");
        Unit Roman_pikemen = new Unit("pikemen", "Roman", "Cavalry");

        new_troop.get_soldiers().add(Roman_legionary);
        new_troop.get_soldiers().add(Roman_pikemen);

        my_province.get_my_troops().add(new_troop);

        // move new_troop from my_province to enermy_province, this shoulde be accepted
        boolean accept1 = system.movement(new_troop, my_province, enermy_province);
        assertEquals(accept1, true);
    
        my_province.get_my_troops().add(new_troop2);
        
        // new_troop is already moved to enermy_province, so can't move again.
        boolean accept2 = system.movement(new_troop, my_province, enermy_province2);
        assertEquals(accept2, false);

        // The troop already moved, the troop in enermy_province should be updated.
        boolean judge_troop = false;
        for (Troop troop : enermy_province.get_my_troops()) {
            if (troop.equals(new_troop)) {
                judge_troop = true;
            }
        }
        assertEquals(judge_troop, true);

        
    }

    @Test
    public void testenagege(){
        Faction owner = new Faction("Roman");
        Faction enermy = new Faction("Gallic");

        Systemcontrol system = new Systemcontrol(owner, enermy);
        system.set_enermy(enermy);

        Province my_province = new Province("Britannia", owner, 50, 2.2);
        Province enermy_province = new Province("Belgica", enermy, 50, 2.2);
        
        Unit horse_archer = new Unit("horse archer", "Roman", "Cavalry");
        
        Unit melee_infantry = new Unit("melee infantry", "Gallic", "Cavalry");
        
        // set my soldiers 30 and enermy 10
        horse_archer.setNumSoldiers(1);
        melee_infantry.setNumSoldiers(10);

        Troop new_troop = new Troop(owner, my_province);
        Troop enermy_troop = new Troop(enermy, enermy_province);
        
        new_troop.get_soldiers().add(melee_infantry);
        enermy_troop.get_soldiers().add(horse_archer);

        my_province.get_my_troops().add(new_troop);
        enermy_province.get_enermy_troops().add(enermy_troop);

        owner.getProvinces().add(my_province);
        enermy.getProvinces().add(enermy_province);
        String result = system.engage(my_province.get_name(), enermy_province.get_name());
        // lose this battle and the faction will not change
        assertEquals(result, "lose");
        assertEquals(enermy_province.getFaction(), enermy);
        
    }

    @Test
    public void test_save() throws ClassNotFoundException {
        Faction owner = new Faction("Roman");;
        Faction enermy = new Faction("Gallic");

        Systemcontrol system = new Systemcontrol(owner, enermy);
        system.set_enermy(enermy);

        system.saveProgress();

        owner.set_name("test_name");
        
        system.continueProgress();

        assertEquals(owner.get_name(), "test_name");
    }


}
