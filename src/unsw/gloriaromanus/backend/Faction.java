package unsw.gloriaromanus.backend;

import java.util.ArrayList;
import unsw.gloriaromanus.*;

public class Faction implements TurnObserver{
    private ArrayList<Province> provinces_belong = new ArrayList<Province>();
    private ArrayList<Faction> neighboors = new ArrayList<Faction>();
    private ArrayList<TrainingRequest> requests = new ArrayList<TrainingRequest>();
    
    private Unit unit;
    //private Road road;
    //private int turn;
    private String name; 
    private double treasure;
    private double wealth;

    // Initially every faction have 50 treasure but 0 wealth
    public Faction(String name) {
        this.treasure = 50;
        this.wealth = 0;
        this.name = name;
    }

    public boolean requestTraining(Province p, Unit type, int numSoldiers, int turn){
        int budget = type.training_budget(numSoldiers);

        // check if this faction have enough balance
        if (budget > this.treasure) return false;
        if (!p.trainingSlot_available()) return false;
        
        else {
            treasure -= budget;
            TrainingRequest newReuest = new TrainingRequest(p, type, numSoldiers, turn);
            requests.add(newReuest);
            p.occupyTrainningSlot(type);
            return true;
        }
    }

    public void addRequest(TrainingRequest request){
        requests.add(request);
    }

    public void rmRequest(TrainingRequest request){
        requests.remove(request);
    }

    public void setWealth(){
        for (Province p : provinces_belong){
            wealth = p.getWealth();
        }
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_name() {
        return this.name;
    }

    public ArrayList<Unit> get_soldiers() {
        ArrayList<Unit> soldiers = new ArrayList<Unit>();
        soldiers.add(this.unit);
        return soldiers;
    }

    public ArrayList<Province> getProvinces() {
        return this.provinces_belong;
    }

    public void addProvince(Province province) {
        this.provinces_belong.add(province);
    }

    public ArrayList<Faction> get_neighboors() {
        return this.neighboors;
    }

    public void set_neighboors(Faction neighboor) {
        
    }

    public void attack(Province enermy) {
        boolean occupied = false;
        for (Province i : provinces_belong) {
            if (enermy.equals(i)) {
                occupied = true;
            }
        }

        if (occupied = true) {
            return;
        }
    }


    public void solicitTax(){
        for (Province p: provinces_belong){
            treasure += p.tributeTax();
        }
    }

    public double getTreasure(){
        return this.treasure;
    }

    public double getWealth(){
        return this.wealth;
    }


    // this function will check if all the province have been conquered
    

    // This function will check if the treasury goal have been achieved
    
    
    // This function will check if the wealth goal have been achieved
    

    public int getNumProvince(){
        return provinces_belong.size();
    }

    @Override
    public void update(int turn){

        // update the wealth
        for (Province p : provinces_belong){
            p.solicitTownWealth();
        }
        this.solicitTax();
        this.setWealth();

        // update the training soldiers
        for (TrainingRequest r : requests){
            if (r.getFinishTurn() == turn){
                // check if the province occupied
                if (!provinces_belong.contains(r.getProvince())){
                    rmRequest(r);
                }
                else{
                    r.vocateTrainingSlot();
                    r.sentTrainedSoldier();
                }
            }
        }
    }
}
