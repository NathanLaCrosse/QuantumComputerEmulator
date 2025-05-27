package EmulatorApplicationHelpers;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/*
 * This class represents a node where on which a quantum gate can be placed.
 */
public class GateNode {
    private String quantumGate;
    private ImageView view;

    public static Image[] gateIcons = new Image[]{
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\measurementGate.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\ReferencePoint.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\ControlPoint.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\IdentityGate.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\XGate.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\YGate.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\ZGate.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\HadamardGate.png")
        };
    public static Image[] helperSprites = new Image[] {
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\gateTop.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\gateConnector.png"),
            SpriteLoader.initializeConstantImage("src\\EmulatorApplicationHelpers\\Sprites\\gateBottom.png")
        };

    QCEInterface qce;
    // most of this constructor is actually setting up the drag+drop procedure, thus the long input list
    /*
     * @param position - inputted row,col of the node in the nodes arraylist
     */
    public GateNode(String gate, Image icon, boolean canDropOn, int[] position, QCEInterface qce) {
        quantumGate = gate;
        view = new ImageView(icon);
        this.qce = qce;

        // sets up the drop action so a new gate can be placed over this one
        // info about the drag and drop procedure can be found at https://docs.oracle.com/javase/8/javafx/events-tutorial/drag-drop.htm#CHDJFJDH 
        if(canDropOn) {
            ArrayList<ArrayList<GateNode>> nodes = qce.getNodes();
            ArrayList<ArrayList<Connector>> connectors = qce.getConnectors();
            ArrayList<Boolean> hasControlGate = qce.getIfHasControlGate();
            ArrayList<Integer> numReferences = qce.getNumReferences();

            // this forces our image to our gate only if we are not a measurement gate
            setImageViaGate();

            view.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent arg0) {
                    if(arg0.getGestureSource() != view && arg0.getDragboard().hasString()) {
                        String s = arg0.getDragboard().getString();

                        //deny placement if there are already too many reference/control points

                        boolean isControl = s.equals("ControlPoint");
                        boolean isReference = s.equals("ReferencePoint");

                        //if this gate uses several lines, find the highest point it can occupy (lowest index)
                        int highestOccupiablePoint = 0;
                        if(s.indexOf("Base") != -1) {
                            highestOccupiablePoint = 1; // incase there is no index, base size is 2 qubits
                            String numSpot = s.substring(s.indexOf("Base")+5).trim();
                            if(!numSpot.equals("")) {
                                highestOccupiablePoint = Integer.parseInt(numSpot) - 1;
                            }
                        }

                        if(((isControl || !isReference) && hasControlGate.get(position[1])) || (isReference && (numReferences.get(position[1]) == 2||!hasControlGate.get(position[1])))
                                || (s.indexOf("Base") != -1 && position[0] < highestOccupiablePoint) || quantumGate.contains("Top") || quantumGate.contains("Connector") || quantumGate.contains("Base")) {

                        }else {
                            arg0.acceptTransferModes(TransferMode.COPY);
                        }
                    }
                    arg0.consume();
                }
            });

            view.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent arg0) {
                    Dragboard db = arg0.getDragboard();
                    boolean success = false;
                    if(db.hasString()) {
                        if(db.getString().equals("ControlPoint")) {
                            //wipe the column first to make sure the user didn't already put something there
                            qce.clearColumn(position[1]);
                            hasControlGate.set(position[1], true);
                        }else if(db.getString().equals("ReferencePoint")) {
                            numReferences.set(position[1], numReferences.get(position[1]) + 1);
                            //since a reference node placement imples there is already a control, enable connections to control
                            enableConnectionsToControl(position, nodes, connectors);
                        }

                        setQuantumGate(db.getString());
                        setImageViaGate();
                        success = true;

                        //change above gates if placing a multi-qubit gate
                        if(db.getString().indexOf("Base") != -1) {
                            //extract a number if there is one after base
                            int numWipesNeeded = 1;
                            String numSpot = db.getString().substring(db.getString().indexOf("Base")+5).trim();
                            if(!numSpot.equals("")) {
                                numWipesNeeded = Integer.parseInt(numSpot) - 1;
                            }

                            for(int i = 1; i <= numWipesNeeded; i++) {
                                String typeNode = i == numWipesNeeded ? "Top" : "Connector";
                                nodes.get(position[0]-i).get(position[1]).setQuantumGate(typeNode);
                                nodes.get(position[0]-i).get(position[1]).setImageViaGate();
                                connectors.get(position[0]-i).get(position[1]).setImage(helperSprites[1]);
                            }
                        }
                    }
                    arg0.setDropCompleted(success);
                    arg0.consume();
                }
            });
        }
    }
    public GateNode(int[] position, QCEInterface qce) {
        this("Identity", gateIcons[1],true, position, qce);
    }

    public ImageView getView() {
        return view;
    }

    public String getQuantumGate() {
        return quantumGate;
    }
    public void setQuantumGate(String gate) {
        quantumGate = gate;
    }

    public void setImageViaGate() {
        if(quantumGate.indexOf("Top") != -1) {
            view.setImage(helperSprites[0]);
            return;
        }else if(quantumGate.indexOf("Base") != -1) {
            view.setImage(helperSprites[2]);
            return;
        }else if(quantumGate.indexOf("Connector") != -1) {
            view.setImage(helperSprites[1]);
            return;
        }

        int dex;
        if(quantumGate.equals("ReferencePoint")) {
            dex = 1;
        }else if(quantumGate.equals("ControlPoint")) {
            dex = 2;
        }else {
            dex = qce.indexInGateTypes(quantumGate) + 3;
        }
        view.setImage(gateIcons[dex]);
    }

    /*
     * This method is called when a reference node is placed. This method enables all connectors on the path to the control
     */
    private void enableConnectionsToControl(int[] position, ArrayList<ArrayList<GateNode>> nodes, ArrayList<ArrayList<Connector>> connectors) {
        int controlRow = 0;
        for(int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).get(position[1]).getQuantumGate().equals("ControlPoint")) {
                controlRow = i;
            }
        }
        boolean down = true;
        if(position[0] > controlRow) {down = false;}
        int dex = position[0];
        while(down && dex < controlRow) {
            connectors.get(dex).get(position[1]).showConnection();
            dex++;
        }
        while(!down && dex > controlRow) {
            dex--;
            connectors.get(dex).get(position[1]).showConnection();
        }
    }
}