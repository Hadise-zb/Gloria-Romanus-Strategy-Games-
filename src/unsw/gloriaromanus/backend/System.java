package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class System {
    private ArrayList<Province> provinces;
    private Integer turns;
    private Integer treasure;

    public void update() {
        for (Province province: this.provinces) {
            this.treasure += province.getTreasure();
        }

    }
}