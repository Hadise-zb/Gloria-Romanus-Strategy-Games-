package unsw.gloriaromanus.backend;
import unsw.gloriaromanus.*;


public abstract class Smith {
    private int cost;
    private Double portion_speed;
    private Double scalar_speed;

    public Smith(int cost) {
        this.cost = cost;
    }

    public Double getScalarsSpeed(){
        return this.scalar_speed;
    }

    public Double getPrortionSpeed(){
        return this.portion_speed;
    }


    public void upgraded_helmets(int i, Unit unit) {
        unit.set_helmet(i);
    } 

    public void Upgraded_armour_suit(Unit unit) {
        unit.set_armour(0.5);
    }

    public void Upgraded_weapon(Unit unit) {

    }

    


}