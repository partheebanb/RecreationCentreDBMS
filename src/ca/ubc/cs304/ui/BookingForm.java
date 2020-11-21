package ca.ubc.cs304.ui;

import ca.ubc.cs304.helper.DateUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

public class BookingForm {
    private JButton enterForm;
    private JComboBox memberComboBox;
    private JPanel jpanel;
    private JSpinner dateSpinner;

    BookingForm() {
        JFrame frame = new JFrame("Booking Form");
        frame.setContentPane(this.jpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        setupDateSpinner();
        setupEnterButton();
    }

    public void setupEnterButton() {
        enterForm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:
            }
        });
    }

    public void setupDateSpinner() {
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "h:mm a, EEE, dd-MMM-yyyy");
        dateSpinner.setEditor(timeEditor);
        dateSpinner.setValue(new Date(new java.util.Date().getTime()));
    }

    public static void main(String[] args) {
        new BookingForm();
    }

    private void createUIComponents() {
        dateSpinner = new JSpinner(new SpinnerDateModel());
    }
}
