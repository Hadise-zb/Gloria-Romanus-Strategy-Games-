package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;

import java.util.List;
import java.util.ArrayList;

public class InvasionMenuController extends MenuController{
    @FXML
    private TextField invading_province;
    @FXML
    private TextField opponent_province;
    @FXML
    private TextArea output_terminal;
    @FXML
    private ChoiceBox <String> troop_choice;

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    public void setInvadingProvince(String p) {
        invading_province.setText(p);
    }

    public void setOpponentProvince(String p) {
        opponent_province.setText(p);
    }

    public void appendToTerminal(String message) {
        output_terminal.appendText(message + "\n");
    }

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
