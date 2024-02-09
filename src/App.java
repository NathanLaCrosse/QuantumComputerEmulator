import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.stage.Stage;

//reminder - ctrl+shift+p enter javafx to create new project

public class App extends Application {
    static ComplexMatrix dynamics, state;
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        

        QuantumComputerEmulator.build(primaryStage, this);

        //commenting the below method back in and commenting out the above will instead run a matrix calculator

        //buildCalculator(primaryStage);
    }

    private void buildCalculator(Stage primaryStage) {
        BorderPane root = new BorderPane();
        
        HBox inputManager = new HBox();
        inputManager.setSpacing(50);

        ArrayList<ArrayList<TextField>> in1 = new ArrayList<>();
        buildInputMatrix(in1, inputManager, "Unitary Matrix for Dynamics");
        ArrayList<ArrayList<TextField>> in2 = new ArrayList<>();
        buildInputMatrix(in2, inputManager, "Initial State (input only 1 position)");

        Button buildButton = new Button();
        buildButton.setText("Build");
        buildButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    dynamics = buildFromTextFields(in1);
                    state = buildFromTextFields(in2);
                }catch (Exception e) {
                    return;
                }
                quantumSystemPositionDisplay(root);
            }
            
        });
        inputManager.getChildren().add(buildButton);
        
        //root.setTop(adaptButtons);
        root.setCenter(inputManager);
        //root.setRight(build);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Quantum Position System");
        primaryStage.show();

        //always run this code :)
        Image img = new Image(getClass().getResourceAsStream("gup.jpg"));
        primaryStage.getIcons().add(img);
    }

    private static void quantumSystemPositionDisplay(BorderPane root) {
        VBox vb = new VBox();
        vb.setSpacing(20);

        Label sys = new Label();
        sys.setText(state.toString());

        Button incrementTime = new Button();
        incrementTime.setText("Increment Time");
        incrementTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    state = ComplexMatrix.matrixMultiply(dynamics, state);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                sys.setText(state.toString());
            }
        });
        Button decrementTime = new Button();
        decrementTime.setText("Decrement Time");
        decrementTime.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    state = ComplexMatrix.matrixMultiply(ComplexMatrix.adjoint(dynamics), state);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                sys.setText(state.toString());
            }
        });

        vb.getChildren().addAll(sys,incrementTime);
        root.setCenter(vb);
    }

    private static void matrixCalculator(Stage primaryStage) {
        BorderPane root = new BorderPane();
        
        HBox inputManager = new HBox();
        inputManager.setSpacing(50);

        ArrayList<ArrayList<TextField>> in1 = new ArrayList<>();
        buildInputMatrix(in1, inputManager, "Matrix 1");
        ArrayList<ArrayList<TextField>> in2 = new ArrayList<>();
        buildInputMatrix(in2, inputManager, "Matrix 2 (Used in calculations marked by *)");
        
        HBox botRow = new HBox();
        botRow.setSpacing(100);
        Label result = new Label();

        VBox operationsManager = new VBox();
        
        ComboBox<String> opChoices = new ComboBox<>();
        opChoices.getItems().addAll(ops); // adds all choices into it

        Button performOperation = new Button();
        performOperation.setText("Calculate");
        performOperation.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
                ComplexMatrix m1 = buildFromTextFields(in1);
                ComplexMatrix m2 = buildFromTextFields(in2);
                int dex = opChoices.getSelectionModel().getSelectedIndex();
                if(dex == -1) dex = 0;
                try {
                    result.setText("Result: " + matrixOps[dex].operate(m1, m2));
                }catch(Exception e) {
                    //System.out.println(e.getMessage());
                    result.setText("A problem occurred!\n"+e.getMessage());
                }
            }
        });
        
        operationsManager.getChildren().addAll(opChoices,performOperation);
        botRow.getChildren().addAll(operationsManager,result);

        VBox manager = new VBox();
        manager.setSpacing(100);
        manager.getChildren().addAll(inputManager, botRow);
        
        root.setCenter(manager);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Matrix Calculator");
        primaryStage.show();
    }

    /*
     * Creates a ComplexMatrix object based off of a arraylist of textfields.
     */
    private static ComplexMatrix buildFromTextFields(ArrayList<ArrayList<TextField>> textMatrix) {
        String[][] m = new String[textMatrix.size()][textMatrix.get(0).size()];
        for(int i = 0; i < m.length; i++) {
            for(int k = 0; k < m[0].length; k++) {
                String s = textMatrix.get(i).get(k).getText();
                if(s.trim().equals("")) s = "0";
                m[i][k] = s;
            }
        }
        return new ComplexMatrix(m);
    }
    /**
     * Creates an input array for one to enter into a matrix of any size into a program.
     * @param tfs - An empty arraylist for the textfields to be stored in for later use
     * @param addTo - An HBox to add the entirity of the input matrix to
     * @param text - Text in the label displayed above the input matrix.
     */
    private static void buildInputMatrix(ArrayList<ArrayList<TextField>> tfs, HBox addTo, String text) {
        VBox skeleton = new VBox();
        HBox storage = new HBox();

        ArrayList<TextField> start = new ArrayList<>();
        TextField startText = new TextField();
        startText.setPromptText("Enter Number"); 
        startText.setMaxSize(90, 20);
        start.add(startText);
        tfs.add(start);

        Button addRow1 = new Button();
        addRow1.setText("+Row");
        addRow1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                tfs.add(new ArrayList<TextField>());
                int endDex = tfs.size() - 1;
                for(int i = 0; i < tfs.get(0).size(); i++) {
                    tfs.get(endDex).add(createNewMatrixTextField());
                }
                rebuildTextfields(tfs, storage);
            }
        });
        Button removeRow1 = new Button();
        removeRow1.setText("-Row");
        removeRow1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if(tfs.size() <= 1) return;
                int endDex = tfs.size() - 1;
                while(tfs.get(endDex).size() > 0) {
                    tfs.get(endDex).remove(0);
                }
                tfs.remove(endDex);
                rebuildTextfields(tfs, storage);
            }
        });
        Button addCol1 = new Button();
        addCol1.setText("+Col");
        addCol1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                for(int i = 0; i < tfs.size(); i++) {
                    tfs.get(i).add(createNewMatrixTextField());
                }
                rebuildTextfields(tfs, storage);
            }
        });
        Button removeCol1 = new Button();
        removeCol1.setText("-Col");
        removeCol1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                if(tfs.get(0).size() <= 1) return;
                for(int i = 0; i < tfs.size(); i++) {
                    tfs.get(i).remove(tfs.get(i).size() - 1);
                }
                rebuildTextfields(tfs, storage);
            }
        });
        HBox adaptButtons = new HBox();
        adaptButtons.getChildren().addAll(addRow1,removeRow1,addCol1,removeCol1);

        Label l = new Label(text);

        skeleton.getChildren().addAll(l, adaptButtons, storage);
        addTo.getChildren().add(skeleton);

        rebuildTextfields(tfs, storage);
    }
    /**
     * A helper method that refreshes the textfields after they have been modified. Should be called whenever a text field
     * arraylist is changed.
     */
    private static void rebuildTextfields(ArrayList<ArrayList<TextField>> tfs, HBox skeleton) {
        skeleton.getChildren().clear();
        VBox vb = new VBox();
        for(int i = 0; i < tfs.size(); i++) {
            HBox hb = new HBox();
            hb.getChildren().addAll(tfs.get(i));
            vb.getChildren().add(hb);
        }
        skeleton.getChildren().add(vb);
    }
    /**
     * A helper method to help minimize code size by setting up a textfield for the matricies.
     */
    private static TextField createNewMatrixTextField() {
        TextField tf = new TextField();
        tf.setPromptText("Enter Number");
        tf.setMaxSize(90, 20);
        return tf;
    }

    // Array full of all string representations of the operators
    private static String[] ops = {
        "Addition*","Subtraction*","Matrix Mult*","Transpose","Adjoint","Inner Product*","Norm","Tensor Product*","Normalize Vector","Expected Value*","Variance*"};
    // Array full of all the operations can be selected
    private static MatrixOperation[] matrixOps = {
        new MatrixOperation<ComplexMatrix>() { //addition
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.add(a, b);
            }
        },
        new MatrixOperation<ComplexMatrix>() { //subtraction
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.subtract(a, b);
            }
        },
        new MatrixOperation<ComplexMatrix>() { //matrix multiplication
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.matrixMultiply(a, b);
            }
        },
        new MatrixOperation<ComplexMatrix>() { //transpose
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.transpose(a);
            }
        },
        new MatrixOperation<ComplexMatrix>() { //adjoint
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.adjoint(a);
            }
        },
        new MatrixOperation<ComplexNumber>() { //inner product
            @Override public ComplexNumber operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.innerProduct(a,b);
            }
        },
        new MatrixOperation<Double>() { //norm
            @Override public Double operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.norm(a);
            }
        },
        new MatrixOperation<ComplexMatrix>() { //tensor product
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.tensorProduct(a, b);
            }
        },
        new MatrixOperation<ComplexMatrix>() { //normalize vector
            @Override public ComplexMatrix operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.normalizeVector(a);
            }
        },
        new MatrixOperation<ComplexNumber>() { //find expected value
            @Override public ComplexNumber operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.expectedValue(a, b);
            }
        },new MatrixOperation<ComplexNumber>() { //variance
            @Override public ComplexNumber operate(ComplexMatrix a, ComplexMatrix b) throws Exception {
                return ComplexMatrix.variance(a, b);
            }
        }
    };

}
/**
 * Performs an operation on two matricies
 */
@FunctionalInterface
interface MatrixOperation<R> {
    public R operate(ComplexMatrix a, ComplexMatrix b) throws Exception;
} 