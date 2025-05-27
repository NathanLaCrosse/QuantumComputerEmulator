package EmulatorApplicationHelpers;

import ComplexNumberPackage.ComplexMatrix;
import ComplexNumberPackage.ComplexNumber;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * This class is used to contain the code required to add a new gate to the emulator while the program is running.
 */
public class GateCreatorButton {
    public static Image addNewImage = SpriteLoader.initializeConstantImage("src/EmulatorApplicationHelpers/Sprites/addNew.png");
    
    private static boolean runningExtraWindow = false;

    private VBox structure;
    private HBox gateContainer;
    private QCEInterface qce;

    public GateCreatorButton(HBox gateContainer, QCEInterface qce) {
        this.gateContainer = gateContainer;
        this.qce = qce;
        structure = new VBox();
        structure.setBackground(new Background(new BackgroundImage(SpriteLoader.backgroundImage, null,null, null, null)));
        structure.setAlignment(Pos.TOP_CENTER);

        Text t = new Text("Create New Gate");
        ImageView view = new ImageView(addNewImage);

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
                    if(numQubits < 1 || numQubits > qce.getNumInputs()) {flavor.setText("That value is not supported!");return;}
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

                DraggableGate dg = new DraggableGate(gateType, SpriteLoader.placeholder);
                gateContainer.getChildren().addAll(dg.getVBox(), qce.createSeperatingLine());

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
    private void addNewGate(String type, String[][] rep) {
        String[] hold = new String[qce.getGateTypes().length + 1];
        for(int i = 0; i < hold.length-1; i++) {
            hold[i] = qce.getGateTypes()[i];
        }
        hold[hold.length - 1] = type;
        qce.setGateTypes(hold);

        ComplexMatrix[] temp = new ComplexMatrix[qce.getGateMatrices().length + 1];
        for(int i = 0; i < temp.length - 1; i++) {
            temp[i] = qce.getGateMatrices()[i];
        }
        temp[temp.length - 1] = new ComplexMatrix(rep);
        qce.setGateMatrices(temp);
    }

}