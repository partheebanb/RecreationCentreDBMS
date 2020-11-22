package ca.ubc.cs304.ui;

import ca.ubc.cs304.database.DatabaseConnectionHandler;
import ca.ubc.cs304.model.BranchModel;
import ca.ubc.cs304.model.MemberModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainWindow {
    private JPanel mainPanel;
    private JButton addBooking;
    private JComboBox branchComboBox;
    private DatabaseConnectionHandler dbHandler;

    private ArrayList<DisposableWindow> childrenPanel = new ArrayList<>();

    public MainWindow(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;

        JFrame frame = new JFrame("Main Window");
        frame.setContentPane(this.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Make the screen size normal
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width * 2 / 3, screenSize.height * 2 / 3);

        setupBranchComboBox();

        // When the add booking button is pressed, create a window that helps to add booking
        addBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BranchModel selectedBranch = (BranchModel) branchComboBox.getSelectedItem();
                childrenPanel.add(new BookingForm(dbHandler, selectedBranch.getId()));
            }
        });

        // We don't want the windows to be too cluttered. So if we press the main window, we also kill all the children windows
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeAllChildren();
            }
        });
    }

    private void setupBranchComboBox() {
        for (BranchModel branchModel : dbHandler.branchHandler.getBranchInfo()) {
            branchComboBox.addItem(branchModel);
        }
    }

    /*
     * THIS IS FOR UI PURPOSES ONLY THERE IS NO EXTRA FUNCTIONALITIES FOR THIS FEATURE AT ALL
     *
     * Disposable Window is a system implemented to keep the windows from being to cluttered
     * It closes all the childrens whenever the parent window is clicked so there won't be too many
     * windows running around
     */
    private void closeAllChildren() {
        for(DisposableWindow disposableWindow : childrenPanel) {
            disposableWindow.close();
        }
    }
}
