import java.awt.*;
import javax.swing.*;

/**
 * This class helps with visualizing complex numbers
 */

public class ComplexVisualizer {
    
    public static void createVisualizationOfComplexNums(ComplexNumber[] nums) {
        ComplexFrame f = new ComplexFrame(nums);
    }

}
class ComplexFrame extends JFrame {

    public ComplexFrame(ComplexNumber[] nums) {
        add(new ComplexPanel(nums));
        pack();
        setVisible(true);
    }

}
class ComplexPanel extends JPanel {
    ComplexNumber[] nums;

    public ComplexPanel(ComplexNumber[] nums) {
        setPreferredSize(new Dimension(200, 200));
        this.nums = nums;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(0,100,200,100);
        g.drawLine(100,0,100,200);

        for(int i = 0; i < nums.length; i++) {
            g.setColor(Color.RED);
            int x = (int)(nums[i].realPart().valueOf() * 10 + 98);
            int y = (int)(nums[i].imaginaryPart().valueOf() * -10 + 98);
            g.fillOval(x, y, 4, 4);
            
            //vector line (optional)
            g.drawLine(100,100,x+2,y+2);
        }
    }
}
