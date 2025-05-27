package EmulatorApplicationHelpers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;

/*
 * This class visualizes the connection between qubits, sometimes representing a full gate.
 */
public class Connector {

    public static Image empty = SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\emptyConnection.png");
    public static Image filled = SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\connection.png");

    private ImageView view;

    public Connector() {
        view = new ImageView(empty);
    }

    public ImageView getView() {
        return view;
    }

    public void showConnection() {
        view.setImage(filled);
    }
    public void hideConnection() {
        view.setImage(empty);
    }

    public void setImage(Image img) {
        view.setImage(img);
    }
}