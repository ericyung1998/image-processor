import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutDialog extends JDialog
{
    public AboutDialog(JFrame parent)
	 {
        setTitle("About");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(Box.createRigidArea(new Dimension(0, 10)));

        ImageIcon icon = new ImageIcon("logo.png");
        JLabel label = new JLabel(icon);
        label.setAlignmentX(0.5f);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel courseName = new JLabel("COMP1022P - Introduction to Computing with Java");
		JLabel assName = new JLabel("Programming Assignment");
        courseName.setFont(new Font("Arial", Font.PLAIN, 12));
        courseName.setAlignmentX(0.5f);
		assName.setFont(new Font("Arial", Font.PLAIN, 12));
        assName.setAlignmentX(0.5f);
        add(courseName);
		add(assName);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        });

        close.setAlignmentX(0.5f);
        add(close);

        setModalityType(ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 230);
        setLocationRelativeTo(parent);		  
    }
}