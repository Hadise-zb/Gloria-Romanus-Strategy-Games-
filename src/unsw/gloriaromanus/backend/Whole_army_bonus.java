package unsw.gloriaromanus.backend;

public class Whole_army_bonus {
    private String name;
    private Double damage;

    public Whole_army_bonus() {
        this.name = "";
        this.damage = 0.0;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_name() {
        return this.name;
    }

    public void set_damage(Double damage) {
        this.damage = damage;
    }

    public Double get_damage() {
        return this.damage;
    }
}