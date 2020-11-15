package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
//import javafx.scene.control.

import java.util.List;
import java.util.ArrayList;

import unsw.gloriaromanus.backend.*;

public class InvasionMenuController extends MenuController{
    @FXML
    private TextField invading_province;
    @FXML
    private TextField opponent_province;
    @FXML
    private TextArea output_terminal;
    @FXML
    private TextField current_turn;
    @FXML
    private TextField faction_treasure;

    //my fix here
    @FXML
    private Label player;

    @FXML
    private ChoiceBox <String> troop_choice;

    @FXML
    private TextField nexthumanprovince;
    @FXML
    private TextField currenthumanprovince;

    @FXML
    private ChoiceBox <String> unit_choice;

    @FXML
    private ChoiceBox <Double> tax_choice;

    private String whose_turn = "human";

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void setInvadingProvince(String p) {
        invading_province.setText(p);
    }

    public void setOpponentProvince(String p) {
        opponent_province.setText(p);
    }

    public void sethumannextProvince(String p) {
        nexthumanprovince.setText(p);
    }

    public void sethumancurrentProvince(String p) {
        currenthumanprovince.setText(p);
    }

    public void appendToTerminal(String message) {
        output_terminal.appendText(message + "\n");
    }

    public void setTurn(int n){
        current_turn.setText(String.valueOf(n));
    }

    public void setTreasure(double n){
        faction_treasure.setText(String.valueOf(n));
    }

    @FXML
    public void initialize() {
        List<String> new_list = new ArrayList<String>();
        new_list.add("pikemen");
        new_list.add("hoplite");
        new_list.add("horse archer");
        new_list.add("druid");
        new_list.add("Egyptian Archer");
        new_list.add("Archer Man");
        new_list.add("CamelArcher");
        new_list.add("Cannon");
        new_list.add("Chariot");
        new_list.add("Horse Lancer");
        new_list.add("Horse Heavy Cavalry");
        new_list.add("Slinger Man");
        new_list.add("Spearman");
        new_list.add("Swordsman");
        unit_choice.getItems().addAll(new_list);
        //Label player = new Label("");

        //Systemcontrol new_system = new Systemcontrol();
        List<Double> taxList = new ArrayList<Double>();
        taxList.add(0.1);
        taxList.add(0.15);
        taxList.add(0.2);
        taxList.add(0.3);
        
        tax_choice.getItems().addAll(taxList);

    }

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

    
    @FXML
    public void clickedmoveButton(ActionEvent e) throws IOException {
        getParent().clickedmoveButton(e);
        
        currenthumanprovince.setText("");
        nexthumanprovince.setText("");
        invading_province.setText("");
        opponent_province.setText("");
        //output_terminal.clear();
        troop_choice.getItems().clear();

        getParent().clear_feature();
    }

    @FXML
    public void clickedmakesure() {
        String my_unit = troop_choice.getValue();
        getParent().set_moved_unit(my_unit);
    }

    @FXML
    public void clickedrecuitbutton(ActionEvent e) throws IOException {
        //Systemcontrol system = getParent().get_system();
        String human_unit = unit_choice.getValue();
        String province = getParent().recuit_unit(human_unit);
        unit_remove();
        unit_add(province);
    }

    @FXML
    public void clickedComfirmButton(ActionEvent e) throws IOException{
        Double taxRate = tax_choice.getValue();
        getParent().clickedComfirmButtom(taxRate);
    }
    
    @FXML
    public void clickEndturn(ActionEvent e) throws IOException {
        //Systemcontrol system = getParent().get_system();
        //getParent().
        if (this.whose_turn.equals("human")) {
            //player.setFont(new Font("Arial", 24));
            player.setText("player2");
            this.whose_turn = "enermy";
            System.out.println(player.getText());
        } else {
            this.whose_turn = "human";
            player.setText("player1");
            System.out.println(player.getText());
        }

        //
        getParent().EndTurn();
        //
        getParent().clear_feature();

        currenthumanprovince.setText("");
        nexthumanprovince.setText("");
        invading_province.setText("");
        opponent_province.setText("");
        output_terminal.clear();
        troop_choice.getItems().clear();
        //getParent().clear_feature();
     
    }

    @FXML
    public void clikedclearfeature() {
        currenthumanprovince.setText("");
        nexthumanprovince.setText("");
        invading_province.setText("");
        opponent_province.setText("");
        output_terminal.clear();
        troop_choice.getItems().clear();

        getParent().clear_feature();
    }

    public void unit_add(String province) {
        //System.out.println(province);
        System.out.println(troop_choice.getValue());
        List<String> new_list = new ArrayList<String>();
        Systemcontrol system = getParent().get_system();
        for (Province p : system.get_myfaction().getProvinces()) {
            System.out.println(p.get_units());
            if (p.get_name().equals(province)) {
                //System.out.println("ha");
                for (Unit u : p.get_units()) {
                    
                    new_list.add(u.get_name());
                }
                break;
            }
        }
        //for (String u : new_list) {
        System.out.println(new_list);
        //}
        troop_choice.getItems().addAll(new_list);
        /*
        List<String> new_list = new ArrayList<String>();
        String a = "A";
        String b = "B";
        String c = "C";
        new_list.add(a);
        new_list.add(b);
        new_list.add(c);
        troop_choice.getItems().addAll(new_list);
        */
    }
    
    public void unit_add_enermy(String province) {
        System.out.println(province);

        List<String> new_list = new ArrayList<String>();
        Systemcontrol system = getParent().get_system();
        for (Province p : system.get_enermyfaction().getProvinces()) {
            if (p.get_name().equals(province)) {
                System.out.println("ha");
                for (Unit u : p.get_units()) {
                    System.out.println(u.get_name());
                    new_list.add(u.get_name());
                }
                break;
            }
        }
        troop_choice.getItems().addAll(new_list);
    }

    public void unit_remove() {
        troop_choice.getItems().clear();    
    }

    public String judge_turn() {
        return this.whose_turn;
    }
    
}
