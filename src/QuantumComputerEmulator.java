import java.util.ArrayList;

import ComplexNumberPackage.ComplexMatrix;
import RealNumberPackage.*;
import EmulatorApplicationHelpers.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
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

    public String[] gateTypes = {"Identity","Pauli-X","Pauli-Y","Pauli-Z","Hadamard","GROVERALG-Base-5","INVERSIONMEAN-Base-4","INVMEAN-Base-3"};
    public ComplexMatrix[] gateMatrices;
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

    public String[] getGateTypes() {
        return gateTypes;
    }
    public void setGateTypes(String[] types) {
        gateTypes = types;
    }
    public ComplexMatrix[] getGateMatrices() {
        return gateMatrices;
    }
    public void setGateMatrices(ComplexMatrix[] matrices) {
        gateMatrices = matrices;
    }

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

    public int getNumInputs() {
        return QuantumComputerEmulator.NUM_INPUTS;
    }

    private ArrayList<QubitButton> qbuttons = new ArrayList<QubitButton>();
    public ArrayList<QubitButton> getInputs() {
        return qbuttons;
    }

    public QuantumComputerEmulator(Stage primaryStage, Application a) throws Exception {
        DraggableGate[] dragGates = {
            new DraggableGate("Identity", GateNode.gateIcons[3]),
            new DraggableGate("Pauli-X", GateNode.gateIcons[4]),
            new DraggableGate("Pauli-Y", GateNode.gateIcons[5]),
            new DraggableGate("Pauli-Z", GateNode.gateIcons[6]),
            new DraggableGate("Hadamard", GateNode.gateIcons[7]),
            new DraggableGate("ReferencePoint",new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ReferencePoint.png"))),
            new DraggableGate("ControlPoint",new Image(a.getClass().getResourceAsStream("EmulatorApplicationHelpers/Sprites/ControlPoint.png"))),
            new DraggableGate("GROVERALG-Base-5", SpriteLoader.placeholder),
            new DraggableGate("INVERSIONMEAN-Base-4", SpriteLoader.placeholder),
            new DraggableGate("INVMEAN-Base-3", SpriteLoader.placeholder)
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
            MeasurementGate mg = new MeasurementGate(this);
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
        ImageView horizontalLine = new ImageView(SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\horizontalSeperatingLine.png"));
        horizontalLine.setScaleX(100);

        //code for setting up the draggable gate area
        HBox gateContainer = new HBox();

        //creating the button to add new gates - will be put into boxes later
        GateCreatorButton gc = new GateCreatorButton(gateContainer, this);

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
        MeasurementGate mg = new MeasurementGate(this);
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
    public ImageView createSeperatingLine() {
        ImageView v = new ImageView(SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\seperatingLine.png"));
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