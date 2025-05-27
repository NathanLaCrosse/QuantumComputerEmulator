package EmulatorApplicationHelpers;

import java.util.ArrayList;

import ComplexNumberPackage.ComplexMatrix;
import javafx.scene.image.ImageView;

/**
 * Interface to allow the application helpers to talk to the emulator.
 */

public interface QCEInterface {
    public ArrayList<QubitButton> getInputs();
    public ArrayList<ArrayList<GateNode>> getNodes();
    public ArrayList<ArrayList<Connector>> getConnectors();
    public ArrayList<Boolean> getIfHasControlGate();
    public ArrayList<Integer> getNumReferences();
    public String[] getGateTypes();
    public ComplexMatrix[] getGateMatrices();
    public int getNumInputs();

    public void setGateTypes(String[] gates);
    public void setGateMatrices(ComplexMatrix[] matrices);

    public void clearColumn(int col);
    public int indexInGateTypes(String type);
    public ImageView createSeperatingLine();
    
}
