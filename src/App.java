import ComplexNumberPackage.ComplexMatrix;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;

public class App extends Application {
    static ComplexMatrix dynamics, state;
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to the application portal! Enter a letter from below to run that program:");
        System.out.println("[a]: Quantum Computer Emulator");
        System.out.println("[b]: Matrix Calculator");

        String letter = in.nextLine().trim().toLowerCase();

        switch (letter) {
            case "b":
                MatrixCalculatorApp.matrixCalculator(primaryStage);
                break;
            default:
                QuantumComputerEmulator.build(primaryStage, this);
                break;
        }
    }
}