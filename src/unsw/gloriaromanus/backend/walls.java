package unsw.gloriaromanus.backend;

public class walls extends Infrastructure {
    private float morale;
    private String type;

    public void walls() {
        this.morale = 0;
        this.type = "wall";
    }

    public void upgrade(String tower) {
        if (tower.equals("archer towers") || tower.equals("ballista towers")) {
            this.type = tower;
            this.morale = 1.0f;
        }
    }
}