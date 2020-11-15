package unsw.gloriaromanus.backend;

import java.io.Serializable;

class Save implements Serializable {
    private Systemcontrol system;
    
    public Save (Systemcontrol new_system) {
        //Faction human = new Faction("");
        //Faction enermy = new Faction("");
        //this.system = new Systemcontrol(human, enermy);
        this.system = new_system;
    }
}