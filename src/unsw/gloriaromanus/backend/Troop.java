package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class Troop {
    private String name;
    private ArrayList<soldier> soldiers;
    private Province current_province;
    
    public void move(Province destination) {
        
    }

    public Province get_province() {
        return this.current_province;
    }
}

