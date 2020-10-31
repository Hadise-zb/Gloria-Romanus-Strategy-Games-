package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import unsw.gloriaromanus.*;
import unsw.gloriaromanus.Unit;
import unsw.gloriaromanus.backend.*;
import unsw.gloriaromanus.backend.System;


public class UnitTest{
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
        Unit u = new Unit("heavy infantry");
        //System.out.println(u.getNumTroops());
        assertEquals(u.getNumTroops(), 50);
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
        Province p = new Province("NSW", my_faction, 0, 0.1);
        testSystem.attach(my_faction);
        my_faction.addProvince(p);  

        p.addTown();
        p.addTown();
        assertEquals(my_faction.getWealth(), 0);
        assertEquals(my_faction.getTreasure(), 50);
        testSystem.endTurn();
        assertEquals(my_faction.getWealth(), 18);
        assertEquals(my_faction.getTreasure(), 52);
    }

    
}

