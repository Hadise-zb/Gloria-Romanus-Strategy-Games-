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
    private ChoiceBox <String> troop_choice;

    @FXML
    public void clickedInvadeButton(ActionEvent e) throws IOException {
        getParent().clickedInvadeButton(e);
    }

    @FXML
    public void clickedmoveButton(ActionEvent e) throws IOException {
        String my_troop = troop_choice.getValue();
        getParent().clickedmoveButton(e, my_troop);
    }

    @FXML
    public void clickedrecuitbutton(ActionEvent e) throws IOException {
        //getParent().clickedrecuitbutton(e);
        List<String> new_list = new ArrayList<String>();
        String a = "A";
        String b = "B";
        String c = "C";
        new_list.add(a);
        new_list.add(b);
        new_list.add(c);
        troop_choice.getItems().addAll(new_list);
    }
}
