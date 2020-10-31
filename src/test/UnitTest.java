package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import unsw.gloriaromanus.*;
import unsw.gloriaromanus.Unit;
import unsw.gloriaromanus.backend.*;
<<<<<<< HEAD
import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;
=======
import unsw.gloriaromanus.backend.System;

>>>>>>> origin/YZ_new

public class UnitTest{
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
        Unit u = new Unit("heavy infantry");
        //System.out.println(u.getNumTroops());
        assertEquals(u.getNumSoldiers(), 50);
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

<<<<<<< HEAD
    @Test
    public void wealthTest(){
        Faction au = new Faction();
        Province p = new Province("NSW", au, 0, 0.1);
        au.addProvince(p);
        p.addTown();
        p.addTown();
        assertEquals(p.getNumTown(), 2);

        p.solicitTownWealth();
        assertEquals(p.getWealth(), 20);

        au.solicitTax();
        assertEquals(au.getTreasure(), 2);
    }

    @Test
    public void campaignvictoryTest(){
        // Create a faction
        Faction my_faction = new Faction();
        System testSystem = new System(my_faction);
        

        
    }

    // test soldier ablility
    @Test
    public void legionary_test() {
        Faction owner = new Faction();
        Province province = new Province("Britannia", owner, 50, 2.2);
        Unit Roman_melee = new Unit("melee cavalry");
        Troop new_troop = new Troop();
        province.get_my_troops().add(Roman_melee);

    }

    
=======
    public static void main(String[] arg){
        String name = "skirmishers";
        Unit new_unit = new Unit(name);
        assert(new_unit.getNumTroops()==20);
        System.out.println(new_unit.getNumTroops());

        Unit x = new Unit("heavy infantry");
        System.out.println(x.getNumTroops());
    }
>>>>>>> origin/YZ_m2
}

