package EmulatorApplicationHelpers;

import java.util.ArrayList;

/**
 * Interface to allow the application helpers to talk to the emulator.
 */

public interface QCEInterface {
    public ArrayList<ArrayList<GateNode>> getNodes();
    public ArrayList<ArrayList<Connector>> getConnectors();
    public ArrayList<Boolean> getIfHasControlGate();
    public ArrayList<Integer> getNumReferences();
    public void clearColumn(int col);
    public int indexInGateTypes(String type);
}
