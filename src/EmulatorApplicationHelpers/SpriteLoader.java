package EmulatorApplicationHelpers;

import java.io.FileInputStream;

import javafx.scene.image.Image;

/**
 * This file helps manage all of the images that we load into the emulator.
 */
public class SpriteLoader {
    // Helper function to manage FileInputStreams
    public static Image initializeConstantImage(String spritePath) {
        Image im = null;

        try {
            im = new Image(new FileInputStream(spritePath));
        }catch (Exception e) {
            e.printStackTrace();
        }

        return im;
    }
}
