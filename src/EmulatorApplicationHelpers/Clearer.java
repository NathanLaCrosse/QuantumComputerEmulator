package EmulatorApplicationHelpers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/*
 * This class can be used to create buttons to clear a specified column in the quantum circuit, in case the user messes up
 */
public class Clearer {
    private Button b;

    public Clearer(int col, QCEInterface qce) {
        b = new Button("Clear");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                qce.clearColumn(col);   
            }
        });
    }

    public Button getButton() {
        return b;
    }
}
