import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
/**
 * Created by mcmah on 18/07/2017.
 */
public class testGUI {
    static JFrame frame;
    static JPanel buttonArea;

    public static void main(String[] args) {
        Border blackline = BorderFactory.createLineBorder(Color.black,3,true);
        frame = new JFrame("testing");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200,140,600,600);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10,10));
        panel.setPreferredSize(new Dimension(600,600));
        frame.getContentPane().add(panel);
        panel.setBorder(blackline);

        buttonArea = new JPanel();
        //buttonArea.setLayout(new BoxLayout(buttonArea, BoxLayout.PAGE_AXIS));
        buttonArea.setPreferredSize(new Dimension(560,200));
        //buttonArea.setLocation(0,100);
        //buttonArea.setAlignmentY(Component.CENTER_ALIGNMENT);
        buttonArea.setBorder(blackline);
        //buttonArea.setHorizontalAlignment(JLabel.CENTER);
        //buttonArea.setVerticalAlignment(JLabel.CENTER);
        panel.add(buttonArea,BorderLayout.CENTER);

    }

}
