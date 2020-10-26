package unsw.gloriaromanus.backend;

public class troop_production extends Infrastructure {
    
    private int heavy_infantry;
    private int spearmen;
    private int missile_infantry;
    private int melee_cavalry;
    private int horse_archers;
    private int elephants;
    private int chariots;
    private int artillery;

    private int attack;
    private int speed;

    public void troop_production() {
        this.heavy_infantry = 0;
        this.missile_infantry = 0;
        this.melee_cavalry = 0;
        this.horse_archers = 0;
        this.elephants = 0;
        this.chariots = 0;
        this.artillery = 0;
    }

    public void recruit_heavy_infantry(int i) {
        this.heavy_infantry = this.heavy_infantry + i;
    }

    public void recruit_missile_infantry(int i) {
        this.missile_infantry = this.missile_infantry + i;
    }

    public void recruit_melee_cavalry(int i) {
        this.melee_cavalry = this.melee_cavalry + i;
    }
    
    public void recruit_horse_archers(int i) {
        this.horse_archers = this.horse_archers + i;
    }

    public void recruit_elephants(int i) {
        this.elephants = this.elephants + i;
    }

}