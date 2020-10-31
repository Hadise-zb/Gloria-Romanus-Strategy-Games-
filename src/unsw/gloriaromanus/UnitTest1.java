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
        assertEquals(u.getNumTroops(), 5);
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

    
}

