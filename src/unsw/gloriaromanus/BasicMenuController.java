package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;

import java.util.List;
import java.util.ArrayList;

public class BasicMenuController extends MenuController{

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    @FXML
    private ChoiceBox <String> choose_player1;

    @FXML
    private ChoiceBox <String> choose_player2;

    private String human_faction;

    private String enermy_faction;

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }


    @FXML
    public void initialize() {
        List<String> new_list = new ArrayList<String>();
        new_list.add("Roman");
        new_list.add("Carthaginian");
        new_list.add("Gaul");
        new_list.add("Celtic Briton");
        new_list.add("Spanish");
        new_list.add("Numidian");
        new_list.add("Egyptian");
        new_list.add("Pontus");
        choose_player1.getItems().addAll(new_list);
        choose_player2.getItems().addAll(new_list);
    }

    @FXML
    public void confirmplayer1() {
        this.human_faction = choose_player1.getValue();
    }

    @FXML
    public void confirmplayer2() {
        this.enermy_faction = choose_player2.getValue();
    }

    public String get_human_unit() {
        return this.human_faction;
    }

    public String get_enermy_unit() {
        return this.enermy_faction;
    }
}
