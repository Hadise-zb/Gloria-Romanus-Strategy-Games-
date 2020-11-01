package unsw.gloriaromanus.backend;

import java.util.ArrayList;

public class TrainingRequest{
    
    private Province province;
    private Unit unit;
    private int num;
    private int finishTurn;


    public TrainingRequest(Province p, Unit type, int numSoldiers, int turn){
        this.province = p;
        this.unit = type;
        this.unit.setNumSoldiers(numSoldiers);
        this.num = numSoldiers;
        this.finishTurn = turn + type.getTrainingTurn();
    }

    public int getFinishTurn(){
        return this.finishTurn;
    }
    
    public Province getProvince(){
        return this.province;
    }

    public void vocateTrainingSlot(){
        province.vocateTrainingSlot(unit);
    }

    public void sentTrainedSoldier(){
        province.addUnit(unit);
    }

}
