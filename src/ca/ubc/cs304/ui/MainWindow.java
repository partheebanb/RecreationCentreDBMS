package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    private JPanel mainPanel;
    private JButton addBooking;
    private JComboBox branchComboBox;
    private DatabaseConnectionHandler dbHandler;

    public MainWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Main Window");
        frame.setContentPane(this.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        setupBranchComboBox();

        addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BranchModel selectedBranch = (BranchModel) branchComboBox.getSelectedItem();
                new BookingForm(dbHandler, selectedBranch.getId());
            }
        });
    }

    private void setupBranchComboBox() {
        for (BranchModel branchModel : dbHandler.branchHandler.getBranchInfo()) {
            branchComboBox.addItem(branchModel);
        }
    }
}
