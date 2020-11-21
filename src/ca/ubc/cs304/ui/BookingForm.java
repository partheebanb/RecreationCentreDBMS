package ca.ubc.cs304.ui;

import javax.swing.*;

public class BookingForm {
    private JButton enterForm;
    private JComboBox memberComboBox;
    private JPanel jpanel;

    BookingForm() {
        JFrame frame = new JFrame("Booking Form");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
