import java.util.ArrayList;

import ComplexNumberPackage.ComplexMatrix;
import ComplexNumberPackage.ComplexNumber;
import RealNumberPackage.*;
import EmulatorApplicationHelpers.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/***** Quantum Computer Emulator!
 * Guide to this program and how to work on it
 * 
 * This program allows the user to drag and drop quantum gates onto a quantum circuit to build simple programs. You can
 * also create your own gates which are made through unitary matrices. Fractional inputs and complex inputs are accepted
 * such as 1dsqrt2 + 5i (1/sqrt(2) + 5i)
 * 
 * If you place a control point in a column, it will only accept up to 2 reference points past that. This gates can only be
 * reset by hitting the reset buttons at the top of each column. Only 1 control point can be on each column. This can be used
 * to create Controlled-NOT and Toffoli gates.
 * 
 * If you want to change the number of inputs or gates, we have some handy final variables for that! Just look at
 * NUM_GATE_NODES and NUM_INPUTS
 * 
 * @author Nathan LaCrosse
 * @version 2/18/2024
 */

public class QuantumComputerEmulator implements QCEInterface {
    public static Image backgroundImage, addNewImage, defaultIcon;
    public static Image[] seperatingLines;

    public final static int NUM_GATE_NODES = 5; // default is 5
    public final static int NUM_INPUTS = 5; // default is 5

    private static int horizontalOffset = 0, gateBarOffset = 0;

    public static Image[] gateIcons;
    public static Image[] connectionIcons;

    public static String[] gateTypes = {"Identity","Pauli-X","Pauli-Y","Pauli-Z","Hadamard","GROVERALG-Base-5","INVERSIONMEAN-Base-4","INVMEAN-Base-3"};
    public static ComplexMatrix[] gateMatrices;
    public static String[][][] stringRepresentations = {
        {
            {"1","0"},
            {"0","1"}
        }, 
        {
            {"0","1"},
            {"1","0"}
        },
        {
            {"0","-i"},
            {"i","0"}
        },
        {
            {"1","0"},
            {"0","-1"}
        },
        {
            {"1dsqrt2","1dsqrt2"},
            {"1dsqrt2","-1dsqrt2"}
        },
        buildPickOutMatrix(4, new int[][]{{1,1,1,1}}),
        buildInversionAboutMeanMatrix(4),
        buildInversionAboutMeanMatrix(3)
    };

    public static Image[] helperSprites;

    public static void main(String[] args) throws Exception {
        App.main(args);
    }

    private ArrayList<ArrayList<GateNode>> nodes = new ArrayList<>();
    public ArrayList<ArrayList<GateNode>> getNodes() {
        return nodes;
    }
    private ArrayList<ArrayList<Connector>> connectors = new ArrayList<>();
    public ArrayList<ArrayList<Connector>> getConnectors() {
        return connectors;
    }

    //these two arraylists help build controlled gates. hasControlGate tells if there is a control node in the
    //column it is in, numReferences notes how many reference points are in said column.
    private ArrayList<Boolean> hasControlGate = new ArrayList<>();
    public ArrayList<Boolean> getIfHasControlGate() {
        return hasControlGate;
    }
    private ArrayList<Integer> numReferences = new ArrayList<>();
    public ArrayList<Integer> getNumReferences() {
        return numReferences;
    }

    public QuantumComputerEmulator(Stage primaryStage, Application a) throws Exception {
        addNewImage = new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/addNew.png"));
        backgroundImage = new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/greySquare.png"));
        defaultIcon = new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/F2Icon.png"));

        seperatingLines = new Image[] {
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/seperatingLine.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/horizontalSeperatingLine.png"))
        };
        gateIcons = new Image[]{
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/measurementGate.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ReferencePoint.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ControlPoint.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/IdentityGate.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/XGate.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/YGate.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ZGate.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/HadamardGate.png"))
        };
        connectionIcons = new Image[]{
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/emptyConnection.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/connection.png"))
        };
        helperSprites = new Image[] {
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/gateTop.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/gateConnector.png")),
            new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/gateBottom.png"))
        };
        DraggableGate[] dragGates = {
            new DraggableGate("Identity", gateIcons[3]),
            new DraggableGate("Pauli-X", gateIcons[4]),
            new DraggableGate("Pauli-Y", gateIcons[5]),
            new DraggableGate("Pauli-Z", gateIcons[6]),
            new DraggableGate("Hadamard", gateIcons[7]),
            new DraggableGate("ReferencePoint",new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ReferencePoint.png"))),
            new DraggableGate("ControlPoint",new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ControlPoint.png"))),
            new DraggableGate("GROVERALG-Base-5", defaultIcon),
            new DraggableGate("INVERSIONMEAN-Base-4", defaultIcon),
            new DraggableGate("INVMEAN-Base-3", defaultIcon)
        };

        gateMatrices = new ComplexMatrix[stringRepresentations.length];
        for(int i = 0; i < gateMatrices.length; i++) {
            gateMatrices[i] = new ComplexMatrix(stringRepresentations[i]);
        }

        BorderPane root = new BorderPane();
		Scene scene = new Scene(root,800,450);
        VBox qubitLayer = new VBox(); // the qubit layer vbox contains the entire circuit 
        qubitLayer.setPadding(new Insets(10,20,10,20));
        qubitLayer.setAlignment(Pos.TOP_LEFT);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                int multiplier = 0;
                if(k.getText().equalsIgnoreCase("a")) {
                    multiplier = 1;
                }else if(k.getText().equalsIgnoreCase("d")) {
                    multiplier = -1;
                }
                horizontalOffset += multiplier * 20;
                if(horizontalOffset > 0) {horizontalOffset = 0;}
                qubitLayer.setTranslateX(horizontalOffset);
            }
        });

        ArrayList<QubitButton> qbuttons = new ArrayList<QubitButton>();
        for(int i = 0; i < NUM_INPUTS; i++) {
            qbuttons.add(new QubitButton());
        }

        for(int i = 0; i < NUM_GATE_NODES; i++) {
            hasControlGate.add(false);
            numReferences.add(0);
        }

        //adds the clear buttons
        HBox clearBox = new HBox();
        clearBox.setTranslateX(130+horizontalOffset);
        clearBox.setSpacing(90);
        for(int i = 0; i < NUM_GATE_NODES; i++) {
            Clearer c = new Clearer(i, this);
            clearBox.getChildren().add(c.getButton());
        }
        qubitLayer.getChildren().add(clearBox);

        //adds alll the gate nodes, connectors and measurement gates
        for(int i = 0; i < qbuttons.size(); i++) {
            nodes.add(new ArrayList<GateNode>());

            //set up this row
            HBox horizontalLayer = new HBox();
            horizontalLayer.setTranslateX(horizontalOffset);
            Button qb = qbuttons.get(i).getButton();
            horizontalLayer.getChildren().add(qb);

            //create gate notes
            for(int k = 0; k < NUM_GATE_NODES; k++) {
                //create linking line
                horizontalLayer.getChildren().add(createCenteredLine(100));
                //make the node
                GateNode gn = new GateNode(new int[]{i,k},this);
                horizontalLayer.getChildren().add(gn.getView());
                nodes.get(i).add(gn);
            }
            //add the measurement gate on
            horizontalLayer.getChildren().add(createCenteredLine(100));
            MeasurementGate mg = new MeasurementGate(qbuttons, nodes, hasControlGate, numReferences);
            horizontalLayer.getChildren().add(mg.getView());

            //add whole row of qubit and gates to the VBox
            qubitLayer.getChildren().add(horizontalLayer);

            //creation of the connection layer to help visualize where gates get their qubits from
            connectors.add(new ArrayList<Connector>());

            //sets up the connection layer between rows, so we can display multi-qubit gates
            HBox connectionLayer = new HBox();
            connectionLayer.setTranslateX(136.5+horizontalOffset);
            connectionLayer.setSpacing(101);
            for(int k = 0; k < NUM_GATE_NODES; k++) {
                Connector c = new Connector();
                connectionLayer.getChildren().add(c.getView());
                connectors.get(i).add(c);
            }

            qubitLayer.getChildren().add(connectionLayer);
        }
        root.setTop(qubitLayer);

        //vbox to help format the bars included in the draggable gate area.
        VBox gateArea = new VBox();
        //horizontal bar creation for formatting draggable gate area
        ImageView horizontalLine = new ImageView(seperatingLines[1]);
        horizontalLine.setScaleX(100);

        //code for setting up the draggable gate area
        HBox gateContainer = new HBox();

        //creating the button to add new gates - will be put into boxes later
        GateCreatorButton gc = new GateCreatorButton(gateContainer);

        gateContainer.setAlignment(Pos.BOTTOM_LEFT);
        gateContainer.setSpacing(0);
        gateContainer.getChildren().add(createSeperatingLine());
        gateContainer.getChildren().add(gc.getBox());
        gateContainer.getChildren().add(createSeperatingLine());
        for(int i = 0; i < dragGates.length; i++) {
            gateContainer.getChildren().add(dragGates[i].getVBox());
            gateContainer.getChildren().add(createSeperatingLine());
        }

        //creation of buttons to help navigate the gate bar
        HBox gateNavButtons = new HBox();
        Button navRight = new Button(">");
        navRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                gateBarOffset -= 50;
                gateContainer.setTranslateX(gateBarOffset);
            }
        });
        Button navLeft = new Button("<");
        navLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if(gateBarOffset + 50 > 0) {return;}
                gateBarOffset+=50;
                gateContainer.setTranslateX(gateBarOffset);
            }
        });

        //pull everything together and set to root
        MeasurementGate mg = new MeasurementGate(qbuttons, nodes, hasControlGate, numReferences);
        gateNavButtons.getChildren().addAll(mg.getView(),navLeft,navRight);
        gateArea.getChildren().addAll(gateNavButtons, horizontalLine, gateContainer);
        gateArea.setAlignment(Pos.BOTTOM_LEFT);
        root.setBottom(gateArea);

        primaryStage.setTitle("Quantum Computer Emulator");
		primaryStage.setScene(scene);

        //loading the icon image of the Bloch Sphere
        Image img = new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/blochSphere.png"));
        primaryStage.getIcons().add(img);

		primaryStage.show();
    }
    public static ImageView createSeperatingLine() {
        ImageView v = new ImageView(seperatingLines[0]);
        v.setScaleY(1.4);
        return v;
    }
    /**
     * Returns a VBox containing a centered line with the horizontal length dist, used for setting up qubit lines
     */
    private static VBox createCenteredLine(int dist) {
        VBox lineCenterer = new VBox();
        lineCenterer.setAlignment(Pos.CENTER);
        lineCenterer.getChildren().add(new Line(0, 0, dist, 0));
        return lineCenterer;
    }
    /**
     * Helper method to clear a column of all gates and connections
     */
    public void clearColumn(int col) {
        for(int i = 0; i < nodes.size(); i++) {
            nodes.get(i).get(col).setQuantumGate("Identity");
            nodes.get(i).get(col).setImageViaGate();
            connectors.get(i).get(col).hideConnection();
        }
        hasControlGate.set(col, false);
        numReferences.set(col, 0);
    }
    public int indexInGateTypes(String type) {
        for(int i = 0; i < gateTypes.length; i++) {
            if(gateTypes[i].equals(type)) {return i;}
        }
        return -1;
    }

    /**
     * This method is a tool to help design matrices for a function that "picks out" certain binary values. This can be used to help
     * create matrices for Grover's Algorithm or something similiar
     * @param inputLength - the number of input bits (ingore control bits)
     * @param pickOut - array of binary arrays representing which states to "pick out"
     * @return - a matrix representing the specificed "picking out" operation
     */
    public static String[][] buildPickOutMatrix(int inputLength, int[][] pickOut) {
        String[][] matrixRep = buildBlankStringArrayForMatrixConstruction(inputLength+1);

        for(int i = 0; i < matrixRep.length; i++) {
            int[] fullBitAr = BinaryHelper.convertNumberToBinaryArray(i);

            //make sure the bit array is the proper size, add extra zeroes if its not
            fullBitAr = BinaryHelper.fitBitArrayToSize(fullBitAr, inputLength + 1);

            //array to ignore last bit - last bit is output of matrix
            int[] checkAr = new int[fullBitAr.length - 1];
            for(int k = 0; k < checkAr.length; k++) {
                checkAr[k] = fullBitAr[k];
            }

            //figure out if we are on the index of a still life. if so, flip the last bit in fullBitAr (changes to proper index)
            for(int k = 0; k < pickOut.length; k++) {
                if(sameArray(checkAr, pickOut[k])) {
                    fullBitAr[fullBitAr.length - 1] = fullBitAr[fullBitAr.length - 1] == 0 ? 1 : 0;
                }
            }

            //convert back to an index and swap the proper row to a "1"
            int dex = BinaryHelper.convertBinaryArrayToNumber(fullBitAr);

            //modify the string array
            matrixRep[dex][i] = "1";
        }

        return matrixRep;
    }
    /**
     * Returns true if both arrays contain the same elements
     */
    private static boolean sameArray(int[] a1, int[] a2) {
        if(a1.length != a2.length) return false;
        for(int i = 0; i < a1.length; i++) {
            if(a1[i] != a2[i]) return false;
        }
        return true;
    }
    /**
     * Creates an array representation of the inversion about the mean matrix for the specified amount of inputs
     */
    public static String[][] buildInversionAboutMeanMatrix(int numInputs) {
        int dim = (int)(Math.pow(2, numInputs));
        double offDiagonalVal = 2.0 / dim;
        double onDiagonalVal = -1 + offDiagonalVal;
        String[][] matrixRep = new String[dim][dim];
        for(int i = 0; i < matrixRep.length; i++) {
            for(int k = 0; k < matrixRep[0].length; k++) {
                if(i == k) {
                    matrixRep[i][k] = Double.toString(onDiagonalVal);
                }else {
                    matrixRep[i][k] = Double.toString(offDiagonalVal);
                }
            }
        }
        return matrixRep;
    }
    /**
     * Prints a 2d string array in the stringRepresentation format
     * @param ar
     */
    private static void printMatrixAr(String[][] ar) {
        for(int i = 0; i < ar.length; i++) {
            System.out.print("{");
            for(int k = 0; k < ar.length; k++) {
                System.out.print("\"" + ar[i][k] + "\"");
                if(k < ar.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("}");
            if(i < ar.length - 1) {
                System.out.print(",");
            }
            System.out.println();
        }
    }
    /**
     * Helper method to set up a blank matrix for creating matrix representations of operations
     */
    private static String[][] buildBlankStringArrayForMatrixConstruction(int numInputs) {
        int dim = (int)(Math.pow(2, numInputs));
        String[][] a = new String[dim][dim];
        for(int i = 0; i < a.length; i++) {
            for(int k = 0; k < a[0].length; k++) {
                a[i][k] = "0";
            }
        }
        return a;
    }
}

/*
 * This class handles the specialized measurement gate. This gate converts the whole circuit into a matrix in order to perform
 * the measurement action.
 */
class MeasurementGate extends GateNode {
    private ArrayList<QubitButton> inputs;
    private ArrayList<ArrayList<GateNode>> gates;
    private ArrayList<Boolean> hasControlGate;
    private ArrayList<Integer> numReferences;

    public MeasurementGate(ArrayList<QubitButton> inputs, ArrayList<ArrayList<GateNode>> gates, ArrayList<Boolean> hasControlGate, ArrayList<Integer> numReferences) {
        super("Measurement", QuantumComputerEmulator.gateIcons[0],false,null,null);
        this.inputs = inputs;
        this.gates = gates;
        this.hasControlGate = hasControlGate;
        this.numReferences = numReferences;
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
        for(int i = 0; i < QuantumComputerEmulator.gateTypes.length; i++) {
            if(g.getQuantumGate().equals(QuantumComputerEmulator.gateTypes[i])) {
                foundDex = i;
            }
        }
        return QuantumComputerEmulator.gateMatrices[foundDex];
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

/*
 * This class can be used to create buttons to clear a specified column in the quantum circuit, in case the user messes up
 */
class Clearer {
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
/*
 * This class is used to contain the code required to add a new gate to the emulator while the program is running.
 */
class GateCreatorButton {
    private static boolean runningExtraWindow = false;

    VBox structure;
    HBox gateContainer;

    public GateCreatorButton(HBox gateContainer) {
        this.gateContainer = gateContainer;
        structure = new VBox();
        structure.setBackground(new Background(new BackgroundImage(QuantumComputerEmulator.backgroundImage, null,null, null, null)));
        structure.setAlignment(Pos.TOP_CENTER);

        Text t = new Text("Create New Gate");
        ImageView view = new ImageView(QuantumComputerEmulator.addNewImage);

        structure.getChildren().addAll(t, view);

        structure.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event arg0) {
                if(runningExtraWindow) {return;}
                runningExtraWindow = true;
                createQubitPrompt();
            }
        });
    }

    public VBox getBox() {
        return structure;
    }

    /*
     * Creates the first stage of custom gate creation - asking how many qubits the gate will use. Once a number is inputted,
     * it will prompt the next stage of gate creation.
     */
    public void createQubitPrompt() {
        Stage stage = new Stage();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 230, 100);
        stage.setScene(scene);

        Text t = new Text("Enter the number of qubits for the gate: ");
        TextField tf = new TextField();
        tf.setPromptText("Enter a number");
        tf.setMaxSize(100, 30);

        Text flavor = new Text();
        Button confirm = new Button("Confirm");
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                int numQubits = 0;
                try {
                    numQubits = Integer.parseInt(tf.getText());
                    if(numQubits < 1 || numQubits > QuantumComputerEmulator.NUM_INPUTS) {flavor.setText("That value is not supported!");return;}
                    createMatrixBuilder(numQubits);
                    stage.close();
                }catch (NumberFormatException e) {
                    flavor.setText("Improper input detected!");
                    return;
                }
            }
        });

        VBox skeleton = new VBox();
        skeleton.getChildren().addAll(t,tf,confirm,flavor);
        root.setCenter(skeleton);

        stage.show();
    }
    /**
     * Creates the next stage in custom gate creation - has the user input the matrix for the custom matrix.
     */
    public void createMatrixBuilder(int numQubits) {
        Stage stage = new Stage();
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 400,400);
        stage.setScene(scene);

        Text prompt = new Text("Enter the matrix corresponding to the gate's operation:");
        Text zeroText = new Text("Empty spaces will be considered as 0");
        VBox structure = new VBox();
        structure.getChildren().addAll(prompt,zeroText);

        int powTwo = (int)(Math.pow(2, numQubits));
        TextField[][] tfs = new TextField[powTwo][powTwo];

        //add all the textfields for matrix
        for(int i = 0; i < powTwo; i++) {
            HBox horizontalInput = new HBox();
            for(int k = 0; k < powTwo; k++) {
                TextField tf = new TextField();
                tfs[i][k] = tf;
                horizontalInput.getChildren().add(tf);
            }
            structure.getChildren().add(horizontalInput);
        }

        //Ask the user for a name for the gate for storage purposes
        Text namePrompt = new Text("Enter the gate's name: ");
        TextField name = new TextField();
        name.setMaxWidth(100);

        HBox nameBox = new HBox();
        nameBox.getChildren().addAll(namePrompt,name);

        //button to confirm input and add it to gatetypes and string reps
        Button confirm = new Button("Confirm");
        confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                //note that we only need the string rep and not the complexMatrix version for this - end goal is to add to stringReps in emulator class
                String[][] m = new String[powTwo][powTwo];
                for(int i = 0; i < m.length; i++) {
                    for(int k = 0; k < m.length; k++) {
                        String s = tfs[i][k].getText();
                        if(s.trim().isEmpty()) {s = "0";}
                        try {
                            ComplexNumber.parseComplexNumber(s);
                        }catch(Exception e) {
                            Alert a = new Alert(AlertType.ERROR);
                            a.setHeaderText("Improper input detected");
                            a.show();
                        }
                        m[i][k] = s;
                    }
                }
                
                String gateType = name.getText();
                //add the proper base string and size if we are over 1 qubit
                if(numQubits != 1) {
                    gateType += "-Base-" + numQubits;
                } 

                addNewGate(gateType, m);

                DraggableGate dg = new DraggableGate(gateType, QuantumComputerEmulator.defaultIcon);
                gateContainer.getChildren().addAll(dg.getVBox(), QuantumComputerEmulator.createSeperatingLine());

                stage.close();
                runningExtraWindow = false;
            }
        });

        structure.getChildren().addAll(nameBox, confirm);

        root.setCenter(structure);
        stage.show();
    }

    /**
     * Adds a new gateType and stringRepresentation of a gate into its respective arrays stored in the Quantum Computer Emulator
     * note: the string representation is first turned into a ComplexMatrix object
     */
    private static void addNewGate(String type, String[][] rep) {
        String[] hold = new String[QuantumComputerEmulator.gateTypes.length + 1];
        for(int i = 0; i < hold.length-1; i++) {
            hold[i] = QuantumComputerEmulator.gateTypes[i];
        }
        hold[hold.length - 1] = type;
        QuantumComputerEmulator.gateTypes = hold;

        ComplexMatrix[] temp = new ComplexMatrix[QuantumComputerEmulator.gateMatrices.length + 1];
        for(int i = 0; i < temp.length - 1; i++) {
            temp[i] = QuantumComputerEmulator.gateMatrices[i];
        }
        temp[temp.length - 1] = new ComplexMatrix(rep);
        QuantumComputerEmulator.gateMatrices = temp;
    }

}