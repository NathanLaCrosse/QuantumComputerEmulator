package EmulatorApplicationHelpers;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/*
 * This class represents a draggable gate that the user can use to set up the circuit
 */
public class DraggableGate {
    public static Image backgroundImage = SpriteLoader.initializeConstantImage("src/EmulatorApplicationHelpers/Sprites/greySquare.png");

    private VBox v;

    public DraggableGate(String gateType, Image img) {
        v = new VBox();
        Text t = new Text(" << " + gateType + " >> ");
        ImageView preview = new ImageView(img);
        v.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                //begins a drag and drop with the gate
                //info about the drag and drop procedure can be found at https://docs.oracle.com/javase/8/javafx/events-tutorial/drag-drop.htm#CHDJFJDH 
                Dragboard db = preview.startDragAndDrop(TransferMode.COPY);

                //put the gatetype into the drag's data
                ClipboardContent content = new ClipboardContent();
                content.putString(gateType);
                db.setContent(content);

                arg0.consume();
            }
        });
        v.getChildren().addAll(t,preview);
        v.setAlignment(Pos.TOP_CENTER);
        v.setBackground(new Background(new BackgroundImage(backgroundImage, null,null, null, null)));
    }

    public VBox getVBox() {
        return v;
    }
}