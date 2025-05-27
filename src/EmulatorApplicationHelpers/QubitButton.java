package EmulatorApplicationHelpers;

import ComplexNumberPackage.ComplexMatrix;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/*
 * Instances of this class create buttons the user can interact with to control the input values of the qubits.
 * Inside this class, it stores both the button the user will interact with and the current stored value of the qubit
 */
public class QubitButton {
    private Button b;
    private boolean state; // false = |0>    true = |1>
    
    public QubitButton() {
        state = false;
        b = new Button("|0>");
        b.setMinWidth(35);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                changeState();
            } 
        });
    }
    private void changeState() {
        state = !state;
        b.setText(state ? "|1>" : "|0>");
    }
    public Button getButton() {
        return b;
    }
    public ComplexMatrix retrieveStateMatrix() {
        String[] s = {"0","0"};
        int changeDex = state ? 1 : 0;
        s[changeDex] = "1";
        return new ComplexMatrix(s);
    }
}