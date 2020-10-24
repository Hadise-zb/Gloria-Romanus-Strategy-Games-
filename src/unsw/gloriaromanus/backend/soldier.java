package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class soldier {
    private Boolean mercenary;
    private Boolean armed;
    private troop_production troop;
    private String name;
    private Integer morale;
    private Integer attack;
    private Integer shield;
    private Integer speed;

    public Soldier(Boolean mercenary, String name, Integer speed, Integer attack, Integer shield, Boolean armed, Troop troop) {
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
        Province province = this.troop.getProvince();
        ArrayList<Smith> smiths = province.getsmith();
        for (Smith smith : smiths) {
            if (smith.getProportionSpeed() != -1) {
                speed = speed*smith.getProportionSpeed();
            } else if (smith.getScalarSpeed() != -1) {
                speed += smith.getScalarSpeed();
            }
        }
        Integer output = (int) speed; 
        return output;
    }

    public Integer get_attack() {

    }
}
