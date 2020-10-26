package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class soldier {
    private Boolean mercenary;
    private Boolean armed;
    private Troop troop;
    private String name;
    private Integer morale;
    private Integer attack;
    private Integer shield;
    private Integer speed;

    public void Soldier(Boolean mercenary, String name, Integer moral, Integer speed, Integer attack, Integer shield, Boolean armed, Troop troop) {
        this.mercenary = mercenary;
        this.name = name;
        this.morale = morale;
        this.speed = speed;
        this.attack = attack;
        this.shield = shield;
        this.armed = armed;
    }
    
    public Integer get_speed(){
        Double speed = this.speed/1.0;
        Province province = this.troop.get_province();
        ArrayList<Smith> smiths = province.getsmith();
        for (Smith smith : smiths) {
            if (smith.getPrortionSpeed() != -1) {
                speed = speed*smith.getPrortionSpeed();
            } else if (smith.getScalarsSpeed() != -1) {
                speed += smith.getScalarsSpeed();
            }
        }
        Integer output = (int) speed; 
        return speed;
    }

    public Integer get_attack() {
        Double attack = this.attack/1.0;
        Province 

    }
}
