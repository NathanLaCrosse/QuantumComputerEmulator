package EmulatorApplicationHelpers;

import java.util.ArrayList;

import ComplexNumberPackage.ComplexMatrix;
import ComplexNumberPackage.ComplexNumber;
import RealNumberPackage.BinaryHelper;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*
 * This class handles the specialized measurement gate. This gate converts the whole circuit into a matrix in order to perform
 * the measurement action.
 */
public class MeasurementGate extends GateNode {
    private ArrayList<QubitButton> inputs;
    private ArrayList<ArrayList<GateNode>> gates;
    private ArrayList<Boolean> hasControlGate;
    private ArrayList<Integer> numReferences;

    public MeasurementGate(QCEInterface qce) {
        super("Measurement", GateNode.gateIcons[0],false,null,qce);

        this.inputs = qce.getInputs();
        this.gates = qce.getNodes();
        this.hasControlGate = qce.getIfHasControlGate();
        this.numReferences = qce.getNumReferences();
        getView().setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event arg0) {
                measureCircuit();
            }
        });
    }

    private void measureCircuit() {
        ComplexMatrix circuit = ComplexMatrix.findIdentityMatrix((int)(Math.pow(2,inputs.size())));
        for(int i = gates.get(0).size() - 1; i >= 0; i--) { // this will break if the gate lists all have different sizes, however this should never happen
            ComplexMatrix step;

            if(hasControlGate.get(i)) {
                //if there is a controlled structure, we want to set that up, as we permit a controlled structure or a bunch of other "normal" gates
                int[] controlDexes = new int[numReferences.get(i)];
                int outputDex = 0;
                int cdex = 0;
                for(int gdex = 0; gdex < inputs.size(); gdex++) {
                    if(gates.get(gdex).get(i).getQuantumGate().equals("ReferencePoint")) {
                        controlDexes[cdex] = gdex;
                        cdex++;
                    }else if(gates.get(gdex).get(i).getQuantumGate().equals("ControlPoint")) {
                        outputDex = gdex;
                    }
                }

                //this line of code makes sure we use the proper amount of inputs, depending on if this is a toffoli gate or not
                int control2 = controlDexes[0];
                if(controlDexes.length > 1) {control2 = controlDexes[1];}

                //side note - all controlled not gates are just (in this case) a toffoli with the control bits the same
                step = buildToffoliGateMatrix(controlDexes[0], control2, outputDex);
            }else {
                // if there isn't a controlled structure, simply tensor everything together
                int accessRow = 0;
                String gatetype = gates.get(accessRow).get(i).getQuantumGate();
                //make sure to skip over -Connector and -Top gates as they have no matrix
                while(gatetype.indexOf("Connector") != -1 || gatetype.indexOf("Top") != -1) {
                    accessRow++;
                    gatetype = gates.get(accessRow).get(i).getQuantumGate();
                }
                step = obtainGateMatrix(gates.get(accessRow).get(i));
                accessRow++;
                while(accessRow < inputs.size()) {
                    gatetype = gates.get(accessRow).get(i).getQuantumGate();
                    while(gatetype.indexOf("Connector") != -1 || gatetype.indexOf("Top") != -1) {
                        accessRow++;
                        gatetype = gates.get(accessRow).get(i).getQuantumGate();
                    }
                    step = ComplexMatrix.tensorProduct(step, obtainGateMatrix(gates.get(accessRow).get(i)));
                    accessRow++;
                }
            }

            try {
                circuit = ComplexMatrix.matrixMultiply(circuit,step);
            }catch(Exception e) {
                e.printStackTrace();
            }
            
        }

        ComplexMatrix state = inputs.get(0).retrieveStateMatrix();
        for(int i = 1; i < inputs.size(); i++) {
            state = ComplexMatrix.tensorProduct(state, inputs.get(i).retrieveStateMatrix());
        }

        ComplexMatrix result;
        try {
            result = ComplexMatrix.matrixMultiply(circuit, state);
        }catch(Exception e) {
            e.printStackTrace();
            return;
        }

        Double[] probabilityDistribution = new Double[result.getMatrix().length];
        try {
            for(int i = 0; i < probabilityDistribution.length; i++) {
                probabilityDistribution[i] = ComplexNumber.modulusSquare(result.getMatrix()[i][0]);
            }
        }catch(Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        String probText = "Probability Distribution:\n";
        for(int i = 0; i < probabilityDistribution.length; i++) {
            int[] binRep = BinaryHelper.convertNumberToBinaryArray(i);
            binRep = BinaryHelper.fitBitArrayToSize(binRep, inputs.size());
            for(int k = 0; k < binRep.length; k++) {probText+=binRep[k];}
            probText += " - " + (probabilityDistribution[i]*100) + "%\n";
        }

        //creates an ascending probability array to help observe a value
        Double[] calcProb = new Double[probabilityDistribution.length];
        for(int i = 0; i < calcProb.length; i++) {
            double sum = 0.0;
            for(int k = i; k >= 0; k--) {
                sum += probabilityDistribution[k];
            }
            calcProb[i] = sum;
        }
        //creates a random number and uses that to decide which state we observe
        double randNum = Math.random();
        int dex = -1;
        for(int i = 0; i < calcProb.length; i++) {
            if(randNum < calcProb[i]) {
                dex = i;
                break;
            }else if(i == calcProb.length - 1) {
                dex = i; // we are at the last available option
            }
        }
        String observedValue = "";
        int[] binRep = BinaryHelper.convertNumberToBinaryArray(dex);
        binRep = BinaryHelper.fitBitArrayToSize(binRep, inputs.size());
        for(int i = 0; i < binRep.length; i++)
            observedValue += binRep[i];

        //display our measurment information onto an alert to show the user what was observed and the distribution
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("Observation");
        a.setHeaderText("Observed Value: " + observedValue);
        a.setContentText(probText);
        a.show();

        System.out.println("Observed Value: " + observedValue);
        System.out.println(probText);
    }

    private ComplexMatrix obtainGateMatrix(GateNode g) {
        int foundDex = -1;
        for(int i = 0; i < qce.getGateTypes().length; i++) {
            if(g.getQuantumGate().equals(qce.getGateTypes()[i])) {
                foundDex = i;
            }
        }
        return qce.getGateMatrices()[foundDex];
    }

    public ComplexMatrix buildToffoliGateMatrix(int control1Dex, int control2Dex, int outputDex) {
        //fill a blank string array to fill later with 1s
        int dim = (int)(Math.pow(2,inputs.size()));
        String[][] blankMatrix = new String[dim][dim];
        for(int i = 0; i < blankMatrix.length; i++) {
            for(int k = 0; k < blankMatrix.length; k++) {
                blankMatrix[i][k] = "0";
            }
        }

        //now we figure out where the 1s go through our control dexes and output dexes
        //for each column, we need to find which row it has to go to. this corresponds with finding its binary representation and 
        //manually performing the Toffoli's gate operation
        for(int i = 0; i < dim; i++) {
            int[] bitAr = BinaryHelper.convertNumberToBinaryArray(i);
            //make sure the bit array is the proper size, add extra zeroes if its not
            if(bitAr.length < inputs.size()) {
                int[] change = new int[inputs.size()];
                int dex = 0;
                int dif = inputs.size() - bitAr.length;
                while(dex < dif) {
                    change[dex] = 0;
                    dex++;
                }
                while (dex < inputs.size()) {
                    change[dex] = bitAr[dex - dif];
                    dex++;
                }
                bitAr = change;
            }
            //now with the proper binary string, we perform the toffoli gate operation based off of this method's inputs
            if(bitAr[control1Dex] == 1 && bitAr[control2Dex] == 1) {
                bitAr[outputDex] = bitAr[outputDex] == 0 ? 1 : 0;
            }
            //now we turn our binary array back into an index - which is the row we want to set to "1"
            int changeRowDex = BinaryHelper.convertBinaryArrayToNumber(bitAr);

            blankMatrix[changeRowDex][i] = "1";
        }

        return new ComplexMatrix(blankMatrix);
    }
}
